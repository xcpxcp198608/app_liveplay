package com.wiatec.bplay.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityAdScreenBinding;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.UpgradeInfo;
import com.wiatec.bplay.presenter.SplashPresenter;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * ad screen
 */

public class AdScreenActivity extends BaseActivity<SplashPresenter> implements Splash {

    private ActivityAdScreenBinding binding;
    private Subscription autoChangeImageSubscription;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ad_screen);
        presenter.loadAdImage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isSubscribe = false;
        autoChangeImageSubscription = Observable.interval(8000, TimeUnit.MILLISECONDS)
                .repeat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        presenter.loadAdImage();
                    }
                });
    }

    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivAdScreen, R.drawable.img_hold4,
                    R.drawable.img_hold4);
        }
    }

    //ignore this
    @Override
    public void checkUpgrade(boolean upgrade, UpgradeInfo upgradeInfo) {

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

    private void release(){
        if(autoChangeImageSubscription != null){
            autoChangeImageSubscription.unsubscribe();
        }
    }
}
