package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtils;
import com.px.common.utils.SPUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivitySplashBinding;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.UpgradeInfo;
import com.wiatec.bplay.presenter.SplashPresenter;

/**
 * splash activity
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements Splash {

    ActivitySplashBinding binding;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        binding.tvVersion.setText(AppUtil.getVersionName(SplashActivity.this , getPackageName()));
        presenter.loadAdImage();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!"BTVi3".equals(Build.MODEL) && !"MorphoBT E110".equals(Build.MODEL) &&
                !"BTV3".equals(Build.MODEL)){
            showDeviceNotSupportDialog();
            return;
        }
        if(NetUtils.isConnected(this)){
            presenter.checkUpgrade();
        }else{
            nextPage();
        }
    }

    /**
     * 判断图片资源是否获取成功
     * 成功： 显示图片
     * @param isSuccess 图片资源获取是否成功
     * @param imageInfo 图片信息，url
     */
    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivSplash, R.drawable.img_bg_splash,
                    R.drawable.img_bg_splash);
        }
    }

    /**
     * 判断是否需要更新
     * 需要： 弹出更新对话框
     * 不需要： 执行 nextPage()
     * @param upgrade 是否需要更新
     */
    @Override
    public void checkUpgrade(boolean upgrade, UpgradeInfo upgradeInfo) {
        if(upgrade){
            showUpgradeDialog(upgradeInfo);
        }else{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Logger.d(e.toString());
            }
            nextPage();
        }
    }

    private void showDeviceNotSupportDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_no_support);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 显示app upgrade 对话框
     * @param upgradeInfo upgrade info
     */
    private void showUpgradeDialog(final UpgradeInfo upgradeInfo) {
        AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_update);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        Button btCancel = (Button) window.findViewById(R.id.bt_cancel);
        TextView textView = (TextView) window.findViewById(R.id.tv_info);
        textView.setText(upgradeInfo.getInfo());
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this , UpgradeActivity.class);
                intent.putExtra("upgradeInfo" , upgradeInfo);
                startActivity(intent);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 向下一页面跳转
     */
    private void nextPage(){
        long  currentTime = System.currentTimeMillis();
        long recorderTime = (long) SPUtils.get(SplashActivity.this, "recorderTime" , 0L);
        if (currentTime >= recorderTime+10800000){
            startActivity(new Intent(SplashActivity.this, AdVideoActivity.class));
        }else{
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
