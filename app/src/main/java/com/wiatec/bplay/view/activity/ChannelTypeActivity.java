package com.wiatec.bplay.view.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.http.Bean.DownloadInfo;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.DownloadListener;
import com.px.common.image.ImageMaster;
import com.px.common.utils.AppUtil;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.FileUtils;
import com.px.common.utils.SPUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.ChannelTypeAdapter;
import com.wiatec.bplay.databinding.ActivityChannelTypeBinding;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.model.UserContentResolver;
import com.wiatec.bplay.pojo.ChannelTypeInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.presenter.ChannelTypePresenter;
import com.wiatec.bplay.task.TokenTask;

import java.util.List;

/**
 * channel type activity
 */

public class ChannelTypeActivity extends BaseActivity<ChannelTypePresenter> implements ChannelType, View.OnClickListener {

    private ActivityChannelTypeBinding binding;
    private int type;

    @Override
    protected ChannelTypePresenter createPresenter() {
        return new ChannelTypePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel_type);
        type = getIntent().getIntExtra("type", 0);
        if(type == 9){
            binding.llSearch.setVisibility(View.VISIBLE);
        }
        presenter.loadAdImage();
        presenter.loadChannelType(type+"");
        binding.btRetry.setOnClickListener(this);
        binding.ibtHistory.setOnClickListener(this);
        binding.ibtSearch.setOnClickListener(this);
    }

    @Override
    public void onLoadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
//            ImageMaster.load(imageInfo.getUrl(), binding.ivChannelType, R.drawable.img_hold,
//                    R.drawable.img_hold);
        }
    }

    @Override
    public void onLoadChannelType(boolean execute, final List<ChannelTypeInfo> channelTypeInfoList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            binding.tvLoading.setText(getString(R.string.data_load_error));
            binding.btRetry.setVisibility(View.VISIBLE);
            binding.btRetry.requestFocus();
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        ChannelTypeAdapter channelTypeAdapter = new ChannelTypeAdapter(channelTypeInfoList);
        binding.rcvChannelType.setAdapter(channelTypeAdapter);
        binding.rcvChannelType.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rcvChannelType.requestFocus();
        channelTypeAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    Zoom.zoomIn10to11(view);
                }else{
                    Zoom.zoomOut11to10(view);
                }
                presenter.loadAdImage();
            }
        });
        channelTypeAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Application.getExecutorService().execute(new TokenTask());
                ChannelTypeInfo channelTypeInfo = channelTypeInfoList.get(position);
                handleProtect(channelTypeInfo);
            }
        });
        channelTypeAdapter.setOnItemLongClickListener(new BaseRecycleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                ChannelTypeInfo channelTypeInfo = channelTypeInfoList.get(position);
                boolean isSetting = (boolean) SPUtils.get(channelTypeInfo.getTag()+"protect", false);
                if(!isSetting) {
                    showInputPasswordDialog(channelTypeInfo);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btRetry:
                binding.tvLoading.setText(getString(R.string.data_loading));
                binding.pbLoading.setVisibility(View.VISIBLE);
                binding.btRetry.setVisibility(View.GONE);
                presenter.loadChannelType(type+"");
                break;
            case R.id.ibtHistory:
                Intent intent = new Intent(ChannelTypeActivity.this, ChannelActivity.class);
                intent.putExtra(Constant.key.channel_type, Constant.key.type_history);
                startActivity(intent);
                break;
            case R.id.ibtSearch:
                String key = binding.etSearch.getText().toString().trim();
                Intent intent1 = new Intent(ChannelTypeActivity.this, ChannelActivity.class);
                intent1.putExtra(Constant.key.channel_type, Constant.key.type_search);
                intent1.putExtra(Constant.key.key_search, key);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    private void handleProtect(ChannelTypeInfo channelTypeInfo){
        String tag = channelTypeInfo.getTag();
        boolean isProtect = (boolean) SPUtils.get(tag, true);
        boolean isSetting = (boolean) SPUtils.get(tag+"protect", false);
        String password = (String) SPUtils.get(tag+"protectpassword", "");
        if(channelTypeInfo.getType() == 2){
            showChannel(channelTypeInfo);
            return;
        }
        if(isProtect){
            if(TextUtils.isEmpty(password)) {
                showSettingPasswordDialog(tag);
            }else{
                if(isSetting) {
                    showInputPasswordDialog(channelTypeInfo);
                }else{
                    showSettingPasswordDialog(tag);
                }
            }
        }else{
            showChannel(channelTypeInfo);
        }
    }

    private void showSettingPasswordDialog(final String tag) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setContentView(R.layout.dialog_set_protect_password);
        final EditText etP1 = (EditText) window.findViewById(R.id.etP1);
        final EditText etP2 = (EditText) window.findViewById(R.id.etP2);
        Button btConfirm = (Button) window.findViewById(R.id.btConfirm);
        Button btCancel = (Button) window.findViewById(R.id.btCancel);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p1 = etP1.getText().toString().trim();
                String p2 = etP2.getText().toString().trim();
                if(TextUtils.isEmpty(p1) || TextUtils.isEmpty(p2) || !p1.equals(p2)){
                    EmojiToast.show(getString(R.string.password_format_error), EmojiToast.EMOJI_SAD);
                    return;
                }
                SPUtils.put(tag+"protectpassword", p1);
                SPUtils.put(tag, true);
                SPUtils.put(tag+"protect", true);
                dialog.dismiss();
                EmojiToast.show(getString(R.string.password_setting_success), EmojiToast.EMOJI_SMILE);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.put(tag+"protectpassword", "");
                SPUtils.put(tag, false);
                dialog.dismiss();
                EmojiToast.show(getString(R.string.parent_control_disabled), EmojiToast.EMOJI_SMILE);
            }
        });
    }

    private void showInputPasswordDialog(final ChannelTypeInfo channelTypeInfo) {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setContentView(R.layout.dialog_input_password);
        final EditText etPassword = (EditText) window.findViewById(R.id.etPassword);
        Button btConfirm = (Button) window.findViewById(R.id.btConfirm);
        Button btReset = (Button) window.findViewById(R.id.btReset);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = etPassword.getText().toString().trim();
                if(TextUtils.isEmpty(p)){
                    EmojiToast.show(getString(R.string.password_format_error), EmojiToast.EMOJI_SAD);
                    return;
                }
                String cp = (String) SPUtils.get(channelTypeInfo.getTag()+"protectpassword", "");
                if(cp.equals(p)){
                    showChannel(channelTypeInfo);
                    dialog.dismiss();
                }else{
                    EmojiToast.show(getString(R.string.password_incorrect), EmojiToast.EMOJI_SMILE);
                }
            }
        });
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showResetDialog(channelTypeInfo);
            }
        });
    }

    private void showResetDialog(final ChannelTypeInfo channelTypeInfo){
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        Window window = dialog.getWindow();
        if(window == null) return;
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        window.setContentView(R.layout.dialog_input_password);
        final EditText etPassword = (EditText) window.findViewById(R.id.etPassword);
        etPassword.setHint("type in your username");
        Button btConfirm = (Button) window.findViewById(R.id.btConfirm);
        Button btReset = (Button) window.findViewById(R.id.btReset);
        btReset.setVisibility(View.GONE);
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = etPassword.getText().toString().trim();
                if(TextUtils.isEmpty(p)){
                    EmojiToast.show(getString(R.string.password_format_error), EmojiToast.EMOJI_SAD);
                    return;
                }
                String username = UserContentResolver.get("userName");
                if(username.equals(p)){
                    showSettingPasswordDialog(channelTypeInfo.getTag());
                    dialog.dismiss();
                }else{
                    EmojiToast.show(getString(R.string.username_incorrect), EmojiToast.EMOJI_SMILE);
                }
            }
        });
    }

    private void showChannel(ChannelTypeInfo channelTypeInfo){
        if("HISTORIES".equals(channelTypeInfo.getTag())){
            Intent intent = new Intent(ChannelTypeActivity.this, ChannelActivity.class);
            intent.putExtra(Constant.key.channel_type, Constant.key.type_history);
            startActivity(intent);
            return;
        }
        if(channelTypeInfo.getFlag() == 1){
            Intent intent = new Intent(ChannelTypeActivity.this, ChannelTypeActivity1.class);
            intent.putExtra("type", channelTypeInfo.getTag());
            startActivity(intent);
        }else if(channelTypeInfo.getFlag() == 2){
            Intent intent = new Intent(ChannelTypeActivity.this, ChannelTypeActivity2.class);
            intent.putExtra("type", channelTypeInfo.getTag());
            startActivity(intent);
        }else if(channelTypeInfo.getFlag() == 3){
            if(AppUtil.isInstalled(ChannelTypeActivity.this, Constant.packageName.access)) {
                AppUtil.launchApp(ChannelTypeActivity.this, channelTypeInfo.getTag());
            }else{
                showInstallNoticeDialog("Access2.0", Constant.url.access, Constant.packageName.access);
            }
        }else {
            Intent intent = new Intent(ChannelTypeActivity.this, ChannelActivity.class);
            intent.putExtra(Constant.key.channel_type, channelTypeInfo.getTag());
            startActivity(intent);
        }
    }

    private void showInstallNoticeDialog(String name, final String url, final String packageName){
        final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.
                AlertDialog.Builder(ChannelTypeActivity.this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if(window == null) return;
        window.setContentView(R.layout.dialog_update);
        Button btConfirm = (Button) window.findViewById(R.id.bt_confirm);
        Button btCancel = (Button) window.findViewById(R.id.bt_cancel);
        TextView textView = (TextView) window.findViewById(R.id.tv_info);
        textView.setText(getString(R.string.install_notice) + " " + name + ", " +
                getString(R.string.install_notice1));
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
                        if(AppUtil.isApkCanInstall(ChannelTypeActivity.this,
                                Application.PATH_DOWNLOAD, downloadInfo.getName())){
                            AppUtil.installApk(ChannelTypeActivity.this,
                                    Application.PATH_DOWNLOAD, downloadInfo.getName());
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
}
