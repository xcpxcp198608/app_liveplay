package com.wiatec.bplay.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;

import com.px.common.http.Bean.DownloadInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.DownloadListener;
import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityUpgradeBinding;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.UpgradeInfo;
import com.wiatec.bplay.presenter.SplashPresenter;

/**
 * upgrade app
 */

public class UpgradeActivity extends BaseActivity<SplashPresenter> implements Splash {

    private ActivityUpgradeBinding binding;

    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upgrade);
        presenter.loadAdImage();
        UpgradeInfo upgradeInfo = getIntent().getParcelableExtra("upgradeInfo");
        if(upgradeInfo == null){
            return;
        }
        startUpgrade(upgradeInfo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isSubscribe = false;
    }

    private void startUpgrade(UpgradeInfo upgradeInfo) {
        HttpMaster.download(UpgradeActivity.this)
                .url(upgradeInfo.getUrl())
                .name(upgradeInfo.getPackageName())
                .path(Application.PATH_DOWNLOAD)
                .startDownload(new DownloadListener() {
                    @Override
                    public void onPending(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(DownloadInfo downloadInfo) {
                        binding.llProgress.setVisibility(View.VISIBLE);
                        binding.tvProgress.setText("0%");
                        binding.pbUpgrade.setProgress(0);
                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        binding.tvProgress.setText(downloadInfo.getProgress()+"%");
                        binding.pbUpgrade.setProgress(downloadInfo.getProgress());
                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {
                        binding.tvProgress.setText("100%");
                        binding.pbUpgrade.setProgress(100);
                        binding.llProgress.setVisibility(View.GONE);
                        if(AppUtil.isApkCanInstall(UpgradeActivity.this, downloadInfo.getPath(),
                                downloadInfo.getName())){
                            AppUtil.installApk(UpgradeActivity.this, downloadInfo.getPath(),
                                    downloadInfo.getName());
                        }else{
                            EmojiToast.show("download error", EmojiToast.EMOJI_SAD);
                        }
                    }

                    @Override
                    public void onCancel(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onError(DownloadInfo downloadInfo) {

                    }
                });
    }

    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivUpgrade, R.drawable.img_bg_splash,
                    R.drawable.img_bg_splash);
        }
    }

    //ignore this
    @Override
    public void checkUpgrade(boolean upgrade, UpgradeInfo upgradeInfo) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
