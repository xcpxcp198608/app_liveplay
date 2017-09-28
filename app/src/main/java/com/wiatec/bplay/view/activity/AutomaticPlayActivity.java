package com.wiatec.bplay.view.activity;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;

import com.px.common.utils.Logger;
import com.px.common.utils.NetUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityAutomaticPlayBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.manager.PlayManager;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AutomaticPlayActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private ActivityAutomaticPlayBinding binding;
    private String url;
    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private int currentUrlPosition = 0;
    private boolean send = true;
    private int time;
    private boolean exit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_automatic_play);
        surfaceHolder = binding.surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        url = getIntent().getStringExtra(Constant.key.key_url);
        time = getIntent().getIntExtra(Constant.key.key_duration, 0);
    }

    private void play(final List<String> urlList){
        sendNetSpeed();
        binding.pbPlay.setVisibility(View.VISIBLE);
        try {
            if(mediaPlayer == null){
                mediaPlayer = new android.media.MediaPlayer();
            }
            Logger.d(urlList.get(currentUrlPosition));
            mediaPlayer.reset();
            mediaPlayer.setDataSource(urlList.get(currentUrlPosition));
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(android.media.MediaPlayer mp) {
                    setInterval();
                    binding.pbPlay.setVisibility(View.GONE);
                    binding.tvNetSpeed.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnInfoListener(new android.media.MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(android.media.MediaPlayer mp, int what, int extra) {
                    if(what == android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START){
                        binding.pbPlay.setVisibility(View.VISIBLE);
                        binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    }
                    if(what == android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END){
                        binding.pbPlay.setVisibility(View.GONE);
                        binding.tvNetSpeed.setVisibility(View.GONE);
                    }
                    return false;
                }
            });
            mediaPlayer.setOnErrorListener(new android.media.MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(android.media.MediaPlayer mp, int what, int extra) {
                    playNextUrl(urlList);
                    binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new android.media.MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(android.media.MediaPlayer mp) {
                    playNextUrl(urlList);
                }
            });
            mediaPlayer.setOnSeekCompleteListener(new android.media.MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(android.media.MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
    }

    private void playNextUrl(List<String> urlList){
        currentUrlPosition ++;
            if(currentUrlPosition >= urlList.size()){
                currentUrlPosition =0 ;
            }
        play(urlList);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        play(PlayManager.parseUrl(url));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void releaseMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        send = false;
        releaseMediaPlayer();
    }

    private void setInterval(){
        if(time <= 0 ) return;
        exit = false;
        Observable.timer(time, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        exit = true;
                        finish();
                    }
                });
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
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(exit) return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
