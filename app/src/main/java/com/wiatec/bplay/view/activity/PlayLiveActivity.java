package com.wiatec.bplay.view.activity;

import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityPlayLiveBinding;
import com.wiatec.bplay.instance.Constant;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * play
 */

public class PlayLiveActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener{

    private ActivityPlayLiveBinding binding;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private boolean send = true;
    private boolean isJSLoaded = false;

    private boolean isNeedPaid = false;

    private String channel = "";
    private String userId = "";
    private String title = "";
    private String message ="";
    private String playUrl ="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_play_live);
        surfaceHolder = binding.surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        liveChannelInfo = getIntent().getParcelableExtra("liveChannelInfo");
        isNeedPaid = getIntent().getBooleanExtra("isNeedPaid", false);

        channel =  getIntent().getStringExtra("id");
        userId =  getIntent().getStringExtra("userId");
        title =  getIntent().getStringExtra("title");
        message =  getIntent().getStringExtra("message");
        playUrl = getIntent().getStringExtra("playUrl");

        if(!TextUtils.isEmpty(message)) {
            binding.tvTitle.setText(message);
            binding.tvTitle.setVisibility(View.VISIBLE);
        }
        binding.btSend.setOnClickListener(this);
        binding.switchDanMu.setOnCheckedChangeListener(this);
        initWebView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isNeedPaid){
            SPUtil.put("already_preview" + userId + title, true);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EmojiToast.showLong("Preview over.  To continue watch, please click the channel and confirm to proceed with payment.", EmojiToast.EMOJI_SAD);
                            finish();
                        }
                    });
                }
            }, 60000);
        }
        if(binding.switchDanMu.isChecked()){
            binding.etMessage.requestFocus();
            binding.etMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        sendGoEasyMessage();
                        return true;
                    }
                    return false;
                }
            });
            loadWebView();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        play(playUrl);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseMediaPlayer();
    }

    private void play(final String url) {
        Logger.d(url);
        sendNetSpeed();
        binding.pbPlay.setVisibility(View.VISIBLE);
        try {
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    binding.pbPlay.setVisibility(View.GONE);
                    binding.tvNetSpeed.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
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
                    binding.tvNetSpeed.setVisibility(View.VISIBLE);
                    play(playUrl);
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play(playUrl);
                }
            });
        } catch (IOException e) {
            Logger.d(e.getMessage());
        }
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
        releaseWebView();
    }

    private void sendNetSpeed(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (send){
                    int s1 = NetUtil.getNetSpeedBytes();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    int s2 = NetUtil.getNetSpeedBytes();
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

    class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return true;
        }
    }

    private void initWebView(){
        binding.webView.setWebViewClient(new MyWebViewClient());
        binding.webView.setBackgroundColor(0);
        WebSettings webSettings = binding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
    }

    private void loadWebView(){
        isJSLoaded = false;
        binding.webView.loadUrl(Constant.url.danmu_url);
        binding.webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress >= 100){
                    if(!isJSLoaded) {
                        binding.webView.loadUrl("javascript:showDanMu('" + channel + "')");
                        isJSLoaded = true;
                    }
                }
            }
        });
    }

    private void unloadWebView(){
        binding.webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        isJSLoaded = false;
    }

    private void releaseWebView(){
        binding.webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        isJSLoaded = false;
        binding.webView.clearHistory();
        ((ViewGroup) binding.webView.getParent()).removeView(binding.webView);
        binding.webView.destroy();
    }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btSend){
            sendGoEasyMessage();
        }
    }

    private void sendGoEasyMessage(){
        String message = binding.etMessage.getText().toString();
        if(TextUtils.isEmpty(message)){
            return;
        }
        HttpMaster.post("http://rest-hangzhou.goeasy.io/publish")
                .param("appkey", "BC-6a9b6c468c894389881bc1df7d90cddb")
                .param("channel", channel)
                .param("content", message)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        binding.etMessage.setText("");
                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.switchDanMu){
            if(isChecked){
                binding.webView.setVisibility(View.VISIBLE);
                binding.etMessage.setVisibility(View.VISIBLE);
                binding.btSend.setVisibility(View.VISIBLE);
                loadWebView();
            }else{
                binding.webView.setVisibility(View.GONE);
                binding.etMessage.setVisibility(View.GONE);
                binding.btSend.setVisibility(View.GONE);
                unloadWebView();
            }
        }
    }
}
