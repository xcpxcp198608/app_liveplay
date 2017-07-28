package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.px.common.animator.Zoom;
import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.EmojiToast;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityMoviesBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.model.UserContentResolver;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.UpgradeInfo;
import com.wiatec.bplay.presenter.SplashPresenter;

/**
 * +movies activity
 */

public class MoviesActivity extends BaseActivity<SplashPresenter> implements Splash ,View.OnClickListener,View.OnFocusChangeListener{

    private ActivityMoviesBinding binding;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies);
        presenter.loadAdImage();
        initListener();
    }

    private void initListener() {
        binding.ibt1.setOnClickListener(this);
        binding.ibt2.setOnClickListener(this);
        binding.ibt3.setOnClickListener(this);
        binding.ibt1.setOnFocusChangeListener(this);
        binding.ibt2.setOnFocusChangeListener(this);
        binding.ibt3.setOnFocusChangeListener(this);
    }


    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivMovies, R.drawable.img_hold4,
                    R.drawable.img_hold4);
        }
    }

    //ignore this
    @Override
    public void checkUpgrade(boolean upgrade, UpgradeInfo upgradeInfo) {

    }

    @Override
    public void onClick(View v) {
        int level;
        String levelStr = UserContentResolver.get("userLevel");
        try {
            level = Integer.parseInt(levelStr);
        }catch (Exception e){
            level = 1;
        }
        if(level <= 2){
            String experience = UserContentResolver.get("experience");
            if(!"true".equals(experience)) {
                EmojiToast.show(CommonApplication.context.getString(R.string.authority), EmojiToast.EMOJI_SAD);
                startActivity(new Intent(MoviesActivity.this, AdScreenActivity.class));
                return;
            }
        }
        switch (v.getId()){
            case R.id.ibt1:
                launchApp(Constant.packageName.terrarium_tv);
                break;
            case R.id.ibt2:
                launchApp(Constant.packageName.popcom);
                break;
            case R.id.ibt3:
                launchApp(Constant.packageName.tv_house);
                break;
            default:
                break;
        }
    }

    private void launchApp(String packageName){
        if(AppUtil.isInstalled(MoviesActivity.this , packageName)) {
            AppUtil.launchApp(MoviesActivity.this, packageName);
        }else{
            EmojiToast.show(getString(R.string.notice1), EmojiToast.EMOJI_SAD);
            AppUtil.launchApp(MoviesActivity.this, Constant.packageName.market);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            Zoom.zoomIn10to11(v);
            presenter.loadAdImage();
        }else{
            Zoom.zoomOut11to10(v);
        }
    }
}
