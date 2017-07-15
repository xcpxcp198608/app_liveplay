package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.px.common.utils.SPUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityAdVideoBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.model.UserContentResolver;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * ad video
 */

public class AdVideoActivity extends AppCompatActivity {

    private ActivityAdVideoBinding binding;
    private int time = 0;
    private Subscription subscription;
    private static final int SKIP_TIME = 15;
    private int userLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ad_video);
        String videoTime = UserContentResolver.get("adTime");
        try {
            time = Integer.parseInt(videoTime);
        }catch (Exception e){
            time = 0;
        }
        binding.btSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipAds();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String level = UserContentResolver.get("userLevel");
        try {
            userLevel = Integer.parseInt(level);
        }catch (Exception e){
            userLevel = 1;
        }
        if(userLevel >= 4){
            skipAds();
            return;
        }
        playVideo();
        if(time >0){
            binding.llDelay.setVisibility(View.VISIBLE);
            subscription = Observable.interval(0,1, TimeUnit.SECONDS).take(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            int i = (int) (time -1 -aLong);
                            binding.tvDelayTime.setText(i +" s");
                            if(userLevel >= 2){
                                int j = (int) (SKIP_TIME -aLong);
                                if(j <0){
                                    j = 0;
                                }
                                binding.tvTime.setText(" "+j + "s");
                                if(time - i > SKIP_TIME){
                                    binding.btSkip.setVisibility(View.VISIBLE);
                                    binding.btSkip.requestFocus();
                                }
                            }
                        }
                    });
        }
    }

    //播放video
    private void playVideo() {
        binding.videoView.setVideoPath(Constant.path.ad_video);
        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                SPUtils.put(AdVideoActivity.this, "recorderTime", System.currentTimeMillis());
                binding.videoView.start();
            }
        });
        binding.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                skipAds();
                return true;
            }
        });
        binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                binding.llDelay.setVisibility(View.GONE);
                skipAds();
            }
        });
    }

    //跳过影片广告
    private void skipAds() {
        release();
        startActivity(new Intent(AdVideoActivity.this, MainActivity.class));
        finish();
    }

    private void release(){
        if(binding.videoView != null ){
            binding.videoView.stopPlayback();
        }
        if(subscription != null){
            subscription.unsubscribe();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return event.getKeyCode() == KeyEvent.KEYCODE_BACK
                || event.getKeyCode() == KeyEvent.KEYCODE_HOME || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }
}
