package com.wiatec.bplay.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtils;
import com.px.common.utils.SPUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.PlayChannelAdapter;
import com.wiatec.bplay.databinding.ActivityPlayBinding;
import com.wiatec.bplay.entity.ResultInfo;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.manager.PlayManager;
import com.wiatec.bplay.model.UserContentResolver;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.sql.FavoriteChannelDao;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * play
 */

public class PlayActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        PlayManager.PlayListener,View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private ActivityPlayBinding binding;
    private SurfaceHolder surfaceHolder;
    private PlayManager playManager;
    private MediaPlayer mediaPlayer;
    private FavoriteChannelDao favoriteChannelDao;
    private String errorMessage = "";
    private boolean send = true;
    private int currentPlayPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play);
        surfaceHolder = binding.surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        List<ChannelInfo> channelInfoList = (List<ChannelInfo>) getIntent().getSerializableExtra("channelInfoList");
        int position = getIntent().getIntExtra("position", 0);
        playManager = new PlayManager(channelInfoList, position);
        playManager.setPlayListener(this);
        favoriteChannelDao = FavoriteChannelDao.getInstance();
        binding.flPlay.setOnClickListener(this);
        binding.ibtStartStop.setOnClickListener(this);
        binding.ibtReport.setOnClickListener(this);
        binding.cbFavorite.setOnCheckedChangeListener(this);
        showFavoriteStatus();
        showChannelList(channelInfoList);
    }

    private void showFavoriteStatus(){
        if(favoriteChannelDao.exists(playManager.getChannelInfo())){
            binding.cbFavorite.setChecked(true);
        }else{
            binding.cbFavorite.setChecked(false);
        }
    }

    private void showChannelList(final List<ChannelInfo> channelInfoList){
        PlayChannelAdapter playChannelAdapter = new PlayChannelAdapter(channelInfoList);
        binding.rcvChannel.setAdapter(playChannelAdapter);
        binding.rcvChannel.setLayoutManager(new LinearLayoutManager(PlayActivity.this));
        playChannelAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                playManager.setChannelInfo(channelInfoList.get(position));
                playManager.dispatchChannel();
            }
        });
        playChannelAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    view.setSelected(true);
                }else{
                    view.setSelected(false);
                }
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        playManager.dispatchChannel();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseMediaPlayer();
    }

    @Override
    public void play(final String url) {
        showFavoriteStatus();
        playVideo(handleUrl(url));
    }

    @Override
    public void playAd() {
        startActivity(new Intent(PlayActivity.this, AdScreenActivity.class));
        finish();
    }

    private List<String> handleUrl(String url){
        List<String> urlList;
        if(url.contains("#")){
            urlList = new ArrayList<>(Arrays.asList(url.split("#")));
        }else{
            urlList = new ArrayList<>();
            urlList.add(url);
        }
        List<String> urlList1 = new ArrayList<>();
        for (String u : urlList){
            if (u.contains("protv.company")){
                String streamToken = (String) SPUtils.get("streamToken", "123");
                u += "?token=" + streamToken;
            }
            urlList1.add(u);
        }
        return urlList1;
    }

    @Override
    public void launchApp(String packageName) {
        if(AppUtil.isInstalled(PlayActivity.this , packageName)) {
            AppUtil.launchApp(PlayActivity.this, packageName);
        }else{
            EmojiToast.show(getString(R.string.notice1), EmojiToast.EMOJI_SAD);
            AppUtil.launchApp(PlayActivity.this, Constant.packageName.market);
        }
        finish();
    }

    private void playVideo(final List<String> urlList) {
        sendNetSpeed();
        binding.pbPlay.setVisibility(View.VISIBLE);
        try {
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            Logger.d(urlList.get(currentPlayPosition));
            mediaPlayer.reset();
            mediaPlayer.setDataSource(urlList.get(currentPlayPosition));
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_pause);
                    binding.pbPlay.setVisibility(View.GONE);
                    EmojiToast.show(playManager.getChannelInfo().getName()+" playing" , EmojiToast.EMOJI_SMILE);
                    binding.tvNetSpeed.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                    Logger.d("onInfo:" + what + "/" + extra);
                    if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
                        binding.pbPlay.setVisibility(View.VISIBLE);
                        binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    }
                    if(what == MediaPlayer.MEDIA_INFO_BUFFERING_END){
                        binding.pbPlay.setVisibility(View.GONE);
                        binding.tvNetSpeed.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
//                    Logger.d("onError:" + what + "/" + extra);
                    PlayOtherUrlOnVideo(urlList);
                    binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
//                    Logger.d("onCompletions");
                    PlayOtherUrlOnVideo(urlList);
                }
            });
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
    }

    private void PlayOtherUrlOnVideo(List<String> urlList){
        currentPlayPosition ++;
        if(currentPlayPosition >= urlList.size()){
            currentPlayPosition =0 ;
        }
        playVideo(urlList);
    }

    private void releaseMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        send = false;
    }

    private void showErrorReportDialog(){
        errorMessage = getString(R.string.error_msg1);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        if(window == null) return;
        dialog.setContentView(R.layout.dialog_error_report);
        RadioGroup radioGroup = (RadioGroup) window.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.rbMessage1);
        Button button = (Button) window.findViewById(R.id.btSend);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId){
                    case R.id.rbMessage1:
                        errorMessage = getString(R.string.error_msg1);
                        break;
                    case R.id.rbMessage2:
                        errorMessage = getString(R.string.error_msg2);
                        break;
                    case R.id.rbMessage3:
                        errorMessage = getString(R.string.error_msg3);
                    case R.id.rbMessage4:
                        errorMessage = getString(R.string.error_msg4);
                        break;
                    default:
                        break;
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendErrorReport(errorMessage);
                dialog.dismiss();
            }
        });
    }

    private void sendErrorReport(String message) {
        String userName = UserContentResolver.get("userName");
        if(TextUtils.isEmpty(userName)) userName = "test";
        HttpMaster.post(Constant.url.channel_send_error_report)
                .parames("userName",userName)
                .parames("channelName",playManager.getChannelInfo().getName())
                .parames("message", message)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo>(){}.getType());
                        if(resultInfo == null) return;
                        if(resultInfo.getCode() == ResultInfo.CODE_OK) {
                            EmojiToast.show(resultInfo.getMessage(), EmojiToast.EMOJI_SMILE);
                        }else{
                            EmojiToast.show(resultInfo.getMessage(), EmojiToast.EMOJI_SAD);
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cbFavorite:
                ChannelInfo channelInfo = playManager.getChannelInfo();
                if(isChecked){
                    if(favoriteChannelDao.insertOrUpdate(channelInfo)){
                        binding.cbFavorite.setChecked(true);
                    }
                }else{
                    favoriteChannelDao.delete(channelInfo);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.flPlay:
                if(binding.llController.getVisibility() == View.VISIBLE){
                    binding.llController.setVisibility(View.GONE);
                    binding.rcvChannel.setVisibility(View.GONE);
                }else{
                    if(mediaPlayer.isPlaying()){
                        binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_pause);
                    }else{
                        binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_play);
                    }
                    binding.llController.setVisibility(View.VISIBLE);
                    binding.rcvChannel.setVisibility(View.VISIBLE);
                    binding.ibtStartStop.requestFocus();
                }
                break;
            case R.id.ibtReport:
                showErrorReportDialog();
                break;
            case R.id.ibtStartStop:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_play);
                }else{
                    mediaPlayer.start();
                    binding.ibtStartStop.setBackgroundResource(R.drawable.bg_button_pause);
                }
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(binding.llController.getVisibility() == View.VISIBLE){
                binding.llController.setVisibility(View.GONE);
                binding.rcvChannel.setVisibility(View.GONE);
                return true;
            }
        }
        if((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP &&
                binding.llController.getVisibility() == View.GONE) ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            playManager.previousChannel();
        }
        if((event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN &&
                binding.llController.getVisibility() == View.GONE) ||
                event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT){
            playManager.nextChannel();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void sendNetSpeed(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (send){
                    int s1 = NetUtils.getNetSpeedBytes();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int s2 = NetUtils.getNetSpeedBytes();
                    float f  = (s2-s1)/2/1024F;
                    DecimalFormat decimalFormat = new DecimalFormat("##0.00");
                    String s = decimalFormat.format(f);
                    Message m = handler.obtainMessage();
                    m.what = 1;
                    m.obj = s;
                    handler.sendMessage(m);
                }
            }
        }).start();
    }

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String s = msg.obj.toString();
                    binding.tvNetSpeed.setText(s + "kbs");
                    if(playManager.getChannelInfo().isLocked() && playManager.getLevel() <= 2 &&
                            !playManager.isExperience()) {
                        long lastExperienceTime = (long) SPUtils.get("lastExperienceTime", 0L);
                        if (lastExperienceTime + 300000 < System.currentTimeMillis()) {
                            boolean isLastExperience = (boolean) SPUtils.get("isLastExperience", true);
                            SPUtils.put("isLastExperience", !isLastExperience);
                            SPUtils.put("lastExperienceTime", System.currentTimeMillis());
                            EmojiToast.show(getString(R.string.notice3), EmojiToast.EMOJI_SAD);
                            finish();
                        }
                    }
                    break;
            }
        }
    };
}
