package com.wiatec.bplay.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.px.common.http.HttpMaster;
import com.px.common.http.listener.DownloadListener;
import com.px.common.http.pojo.DownloadInfo;
import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.FileUtil;
import com.wiatec.bplay.R;
import com.wiatec.bplay.databinding.ActivityMainBinding;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.presenter.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter> implements Common, View.OnClickListener {

    private ActivityMainBinding binding;

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        presenter.loadAdImage();
        binding.ibtBasic.setOnClickListener(this);
        binding.ibtBvision.setOnClickListener(this);
        binding.ibtPremium.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.ibtBasic:
                if(AppUtil.isInstalled(Constant.packageName.access)) {
                    intent.setClass(MainActivity.this, ChannelTypeActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }else{
                    showInstallNoticeDialog("Access2.0", Constant.url.access, Constant.packageName.access);
                }
                break;
            case R.id.ibtBvision:
                intent.setClass(MainActivity.this, BVisionActivity.class);
                startActivity(intent);
                break;
            case R.id.ibtPremium:
                if(AppUtil.isInstalled(Constant.packageName.ld_service)) {
                    intent.setClass(MainActivity.this, ChannelTypeActivity.class);
                    intent.putExtra("type", 9);
                    startActivity(intent);
                }else{
                    showInstallNoticeDialog("VIP Experience", Constant.url.ld_service, Constant.packageName.ld_service);
                }
                break;
            default:
                break;
        }
    }

    private void showInstallNoticeDialog(String name, final String url, final String packageName){
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_update);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        Button btCancel = (Button) window.findViewById(R.id.bt_cancel);
        TextView textView = (TextView) window.findViewById(R.id.tv_info);
        textView.setText(getString(R.string.install_notice) + " " + name + ", " + getString(R.string.install_notice1));
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showDownloadDialog(url, packageName);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void showDownloadDialog(String url, String packageName) {
        FileUtil.delete(Application.PATH_DOWNLOAD, packageName +".apk");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.download_title));
        progressDialog.setMessage(getString(R.string.download_message));
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        HttpMaster.download(this)
                .url(url)
                .path(Application.PATH_DOWNLOAD)
                .name(packageName+".apk")
                .startDownload(new DownloadListener() {
                    @Override
                    public void onPending(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onStart(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(downloadInfo.getProgress());
                    }

                    @Override
                    public void onPause(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onProgress(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(downloadInfo.getProgress());
                    }

                    @Override
                    public void onFinished(DownloadInfo downloadInfo) {
                        progressDialog.setProgress(100);
                        progressDialog.dismiss();
                        if(AppUtil.isApkCanInstall(Application.PATH_DOWNLOAD, downloadInfo.getName())){
                            AppUtil.installApk(Application.PATH_DOWNLOAD, downloadInfo.getName(), "");
                        }else{
                            if(FileUtil.isExists(Application.PATH_DOWNLOAD, downloadInfo.getName())){
                                FileUtil.delete(Application.PATH_DOWNLOAD, downloadInfo.getName());
                            }
                            EmojiToast.show(getString(R.string.install_error), EmojiToast.EMOJI_SAD);
                        }
                    }

                    @Override
                    public void onCancel(DownloadInfo downloadInfo) {

                    }

                    @Override
                    public void onError(DownloadInfo downloadInfo) {
                        EmojiToast.show(getString(R.string.download_error), EmojiToast.EMOJI_SAD);
                    }
                });
    }

    @Override
    public void onLoadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivMain, R.drawable.img_hold,
                    R.drawable.img_hold);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
