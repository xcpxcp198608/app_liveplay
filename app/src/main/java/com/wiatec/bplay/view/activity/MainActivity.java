package com.wiatec.bplay.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.px.common.http.Bean.DownloadInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.DownloadListener;
import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.FileUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.MainViewPagerAdapter;
import com.wiatec.bplay.adapter.MainViewPagerTransform;
import com.wiatec.bplay.databinding.ActivityMainBinding;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements Common {

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
        initViewPager();
    }

    private List<View> createViewList() {
        List<View> viewList = new ArrayList<>();
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_premium, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_btv, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_bvision, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_premium, binding.viewPager, false));
        viewList.add(LayoutInflater.from(this).inflate(R.layout.item_view_btv, binding.viewPager, false));
        return viewList;
    }

    private void initViewPager(){
        binding.viewPager.setOffscreenPageLimit(5);
        binding.viewPager.setPageMargin(100);
        binding.viewPager.setPageTransformer(true, new MainViewPagerTransform());
        final List<View> viewList = createViewList();
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(viewList);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setCurrentItem(2);
        adapter.setOnItemPageClick(new MainViewPagerAdapter.OnItemPageClick() {
            @Override
            public void onClick(View view, int position) {
               launchShortcut(position);
            }
        });
        binding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                presenter.loadAdImage();
                if(position == 0){
                    binding.viewPager.setCurrentItem(viewList.size()-2);
                }else if(position == viewList.size() -1){
                    binding.viewPager.setCurrentItem(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void launchShortcut(int position){
        Intent intent = new Intent();
        switch (position){
            case 1:
                if(AppUtil.isInstalled(MainActivity.this, Constant.packageName.access)) {
                    intent.setClass(MainActivity.this, ChannelTypeActivity.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }else{
                    showInstallNoticeDialog("Access2.0", Constant.url.access, Constant.packageName.access);
                }
                break;
            case 2:
                intent.setClass(MainActivity.this, ChannelActivity.class);
                intent.putExtra(Constant.key.channel_type, "BVISION");
                startActivity(intent);
                break;
            case 3:
                if(AppUtil.isInstalled(MainActivity.this, Constant.packageName.ldservice)) {
                    intent.setClass(MainActivity.this, ChannelTypeActivity.class);
                    startActivity(intent);
                }else{
                    showInstallNoticeDialog("GoldService", Constant.url.ldservice, Constant.packageName.ldservice);
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
                        if(AppUtil.isApkCanInstall(MainActivity.this, Application.PATH_DOWNLOAD, downloadInfo.getName())){
                            AppUtil.installApk(MainActivity.this, Application.PATH_DOWNLOAD, downloadInfo.getName());
                        }else{
                            if(FileUtils.isExists(Application.PATH_DOWNLOAD, downloadInfo.getName())){
                                FileUtils.delete(Application.PATH_DOWNLOAD, downloadInfo.getName());
                            }
                            EmojiToast.show(getString(R.string.install_error), EmojiToast.EMOJI_SAD);
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
            ImageMaster.load(imageInfo.getUrl(), binding.ivMain, R.drawable.img_hold4,
                    R.drawable.img_hold4);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
