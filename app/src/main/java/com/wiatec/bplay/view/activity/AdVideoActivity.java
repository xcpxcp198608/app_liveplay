package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.px.common.utils.SPUtil;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityAdVideoBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.model.UserContentResolver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ad video
 */

public class AdVideoActivity extends AppCompatActivity {

    private ActivityAdVideoBinding binding;
    private int time = 0;
    private Disposable disposable;
    private static final int SKIP_TIME = 15;
    private int userLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ad_video);
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
    }

    private void playVideo() {
        binding.videoView.setVideoPath(Constant.path.ad_video);
        binding.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                time = mp.getDuration() / 1000 + 1 ;
                showTime();
                SPUtil.put("recorderTime", System.currentTimeMillis());
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

    private void showTime(){
        if(time >0){
            binding.llDelay.setVisibility(View.VISIBLE);
            disposable = Observable.interval(0,1, TimeUnit.SECONDS).take(time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) {
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

    private void skipAds() {
        release();
        startActivity(new Intent(AdVideoActivity.this, MainActivity.class));
        finish();
    }

    private void release(){
        if(binding.videoView != null ){
            binding.videoView.stopPlayback();
        }
        if(disposable != null){
            disposable.dispose();
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
