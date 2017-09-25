package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.px.common.utils.AESUtil;
import com.px.common.utils.Logger;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.utils.LibUtil;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.List;


public class PlayerActivity extends AppCompatActivity {
    private IVLCVout vlcVout;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    private long totalTime = 0;
    private IVLCVout.Callback callback;
    private MediaPlayer.EventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            setContentView(R.layout.activity_player);
            Intent intent = getIntent();
            if (intent == null) return;
            List<ChannelInfo> channelInfoList = (List<ChannelInfo>) intent.getSerializableExtra("channelInfoList");
            int position = intent.getIntExtra("position", 0);
            surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            LibVLC libvlc = LibUtil.getLibVLC(null);
            surfaceHolder.setKeepScreenOn(true);
            mediaPlayer = new MediaPlayer(libvlc);
            vlcVout = mediaPlayer.getVLCVout();
            callback = new IVLCVout.Callback() {
                @Override
                public void onNewLayout(IVLCVout ivlcVout, int i, int i1, int i2, int i3, int i4, int i5) {

                }

                @Override
                public void onSurfacesCreated(IVLCVout ivlcVout) {

                }

                @Override
                public void onSurfacesDestroyed(IVLCVout ivlcVout) {

                }
            };
            vlcVout.addCallback(callback);
            vlcVout.setVideoView(surfaceView);
            vlcVout.attachViews();
            String url = AESUtil.decrypt(channelInfoList.get(position).getUrl(), AESUtil.KEY);
            Logger.d(url);
            Media media = new Media(libvlc, Uri.parse(url));
            mediaPlayer.setMedia(media);
            eventListener = new MediaPlayer.EventListener() {
                @Override
                public void onEvent(MediaPlayer.Event event) {
                    try {
                        if (event.getTimeChanged() == 0 || totalTime == 0 || event.getTimeChanged() > totalTime) {
                            return;
                        }
                        //播放结束
                        if (mediaPlayer.getPlayerState() == Media.State.Ended) {
                            mediaPlayer.setTime(0);
                            mediaPlayer.stop();
                        }
                    } catch (Exception e) {
                        Logger.d(e.toString());
                    }
                }
            };
            mediaPlayer.setEventListener(eventListener);
            mediaPlayer.play();
        } catch (Exception e) {
            Logger.d(e.toString());
        }
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            pausePlay();
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            Logger.d(e.toString());
        }
    }

    private void pausePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        vlcVout.detachViews();
        vlcVout.removeCallback(callback);
        mediaPlayer.setEventListener(null);
    }

    private void resumePlay() {
        vlcVout.setVideoView(surfaceView);
        vlcVout.attachViews();
        vlcVout.addCallback(callback);
        mediaPlayer.setEventListener(eventListener);
    }



}