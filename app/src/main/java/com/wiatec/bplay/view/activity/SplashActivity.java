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
import com.px.common.utils.FileUtil;
import com.px.common.utils.Logger;
import com.px.common.utils.NetUtil;
import com.px.common.utils.SPUtil;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivitySplashBinding;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.UpgradeInfo;
import com.wiatec.bplay.presenter.SplashPresenter;
import com.wiatec.bplay.task.ImageTask;
import com.wiatec.bplay.task.TokenTask;

import java.util.Timer;

/**
 * splash activity
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements Splash {

    ActivitySplashBinding binding;
    private Timer timer;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        binding.tvVersion.setText(AppUtil.getVersionName(getPackageName()));
        presenter.loadAdImage();
        Application.getExecutorService().execute(new ImageTask());
        if(timer != null) timer = null;
        timer = new Timer();
        timer.schedule(new TokenTask(), 0,  3000000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isSubscribe = false;
        if(!"BTVi3".equals(Build.MODEL)  &&
                !"BTV3".equals(Build.MODEL) &&
                !"Z69".equals(Build.MODEL) &&
                !"X96".equals(Build.MODEL)){
            showDeviceNotSupportDialog();
            return;
        }
        boolean showAgree = (boolean) SPUtil.get("agree", true);
        boolean showConsent = (boolean) SPUtil.get("consent_agree", true);
        if(showAgree) {
            showAgreement();
        }else if(showConsent){
            showConsentDialog();
        }else {
            if (NetUtil.isConnected()) {
                presenter.checkUpgrade();
            } else {
                nextPage();
            }
        }
    }

    private void showAgreement() {
        final AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_update);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        Button btCancel = (Button) window.findViewById(R.id.bt_cancel);
        TextView tvTitle = (TextView) window.findViewById(R.id.tvTitle);
        TextView textView = (TextView) window.findViewById(R.id.tv_info);
        btConfirm.setText(getString(R.string.agree));
        btCancel.setText(getString(R.string.reject));
        tvTitle.setText(getString(R.string.agreement));
        textView.setTextSize(15);
        textView.setText(getString(R.string.agreement_content));
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put("agree", false);
                alertDialog.dismiss();
                boolean showConsent = (boolean) SPUtil.get("consent_agree", true);
                if(showConsent){
                    showConsentDialog();
                }else{
                    if(NetUtil.isConnected()){
                        presenter.checkUpgrade();
                    }else{
                        nextPage();
                    }
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showConsentDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_update);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        Button btCancel = (Button) window.findViewById(R.id.bt_cancel);
        TextView tvTitle = (TextView) window.findViewById(R.id.tvTitle);
        TextView textView = (TextView) window.findViewById(R.id.tv_info);
        btConfirm.setText(getString(R.string.ok));
        btCancel.setText(getString(R.string.reject));
        tvTitle.setText(getString(R.string.consent_title));
        textView.setTextSize(15);
        textView.setText(getString(R.string.consent_content));
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.put("consent_agree", false);
                alertDialog.dismiss();
                if(NetUtil.isConnected()){
                    presenter.checkUpgrade();
                }else{
                    nextPage();
                }
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onLoadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivSplash, R.drawable.img_bg_splash,
                    R.drawable.img_bg_splash);
        }
    }

    @Override
    public void checkUpgrade(boolean upgrade, UpgradeInfo upgradeInfo) {
        if(upgrade){
            showUpgradeDialog(upgradeInfo);
        }else{
            try {
                FileUtil.delete(Application.PATH_DOWNLOAD, upgradeInfo.getPackageName());
                Thread.sleep(3000);
            } catch (Exception e) {
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

    private void nextPage(){
        long  currentTime = System.currentTimeMillis();
        long recorderTime = (long) SPUtil.get("recorderTime" , 0L);
        if (currentTime >= recorderTime + 10800000){
            startActivity(new Intent(SplashActivity.this, AdVideoActivity.class));
        }else{
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, ChannelTypeActivity.class);
            intent.putExtra("type", 9);
            startActivity(intent);
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return event.getKeyCode() == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }
}
