package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityPlayBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.manager.PlayManager;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.io.IOException;
import java.util.List;

/**
 * play
 */

public class PlayActivity extends AppCompatActivity implements SurfaceHolder.Callback, PlayManager.PlayListener{

    private ActivityPlayBinding binding;
    private SurfaceHolder surfaceHolder;
    private PlayManager playManager;
    private MediaPlayer mediaPlayer;

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
        playVideo(url);
    }

    @Override
    public void playAd() {
        startActivity(new Intent(PlayActivity.this, AdScreenActivity.class));
        finish();
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

    private void playVideo(final String url) {
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
                    EmojiToast.show(playManager.getChannelInfo().getName()+" playing" , EmojiToast.EMOJI_SMILE);
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    binding.pbPlay.setVisibility(View.VISIBLE);
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    binding.pbPlay.setVisibility(View.VISIBLE);
                    playVideo(url);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PREVIOUS){
            playManager.previousChannel();
        }
        if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT || event.getKeyCode() == KeyEvent.KEYCODE_MEDIA_NEXT){
            playManager.nextChannel();
        }
        return super.onKeyDown(keyCode, event);
    }
}
