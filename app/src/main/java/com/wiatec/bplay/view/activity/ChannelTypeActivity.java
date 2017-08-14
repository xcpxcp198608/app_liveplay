package com.wiatec.bplay.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.image.ImageMaster;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.SPUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.ChannelTypeAdapter;
import com.wiatec.bplay.databinding.ActivityChannelTypeBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ChannelTypeInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.presenter.ChannelTypePresenter;

import java.util.List;

/**
 * channel type activity
 */

public class ChannelTypeActivity extends BaseActivity<ChannelTypePresenter> implements ChannelType {

    private ActivityChannelTypeBinding binding;

    @Override
    protected ChannelTypePresenter createPresenter() {
        return new ChannelTypePresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel_type);
        presenter.loadAdImage();
        presenter.loadChannelType();
        binding.btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvLoading.setText(getString(R.string.data_loading));
                binding.pbLoading.setVisibility(View.VISIBLE);
                binding.btRetry.setVisibility(View.GONE);
                presenter.loadChannelType();
            }
        });
    }

    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivChannelType, R.drawable.img_hold4,
                    R.drawable.img_hold4);
        }
    }

    @Override
    public void loadChannelType(boolean execute, final List<ChannelTypeInfo> channelTypeInfoList) {
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
        binding.rcvChannelType.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        channelTypeAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    Zoom.zoomIn10to11(view);
                    presenter.loadAdImage();
                    binding.rcvChannelType.smoothToCenter(position);
                }else{
                    Zoom.zoomOut11to10(view);
                }
            }
        });
        channelTypeAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ChannelTypeInfo channelTypeInfo = channelTypeInfoList.get(position);
                handleProtect(channelTypeInfo);
            }
        });
    }

    private void handleProtect(ChannelTypeInfo channelTypeInfo){
        String tag = channelTypeInfo.getTag();
        boolean isProtect = (boolean) SPUtils.get(ChannelTypeActivity.this, tag, true);
        boolean isSetting = (boolean) SPUtils.get(ChannelTypeActivity.this, tag+"protect", false);
        String password = (String) SPUtils.get(ChannelTypeActivity.this, "protectpassword", "");
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
                    EmojiToast.show("password format error", EmojiToast.EMOJI_SAD);
                    return;
                }
                SPUtils.put(ChannelTypeActivity.this, "protectpassword", p1);
                SPUtils.put(ChannelTypeActivity.this, tag, true);
                SPUtils.put(ChannelTypeActivity.this, tag+"protect", true);
                dialog.dismiss();
                EmojiToast.show("password setting successfully", EmojiToast.EMOJI_SMILE);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.put(ChannelTypeActivity.this, "protectpassword", "");
                SPUtils.put(ChannelTypeActivity.this, tag, false);
                dialog.dismiss();
                EmojiToast.show("protect password dismiss", EmojiToast.EMOJI_SMILE);
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
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = etPassword.getText().toString().trim();
                if(TextUtils.isEmpty(p)){
                    EmojiToast.show("password format error", EmojiToast.EMOJI_SAD);
                    return;
                }
                String cp = (String) SPUtils.get(ChannelTypeActivity.this, "protectpassword", "");
                if(cp.equals(p)){
                    showChannel(channelTypeInfo);
                    dialog.dismiss();
                }else{
                    EmojiToast.show("password incorrect", EmojiToast.EMOJI_SMILE);
                }
            }
        });
    }

    private void showChannel(ChannelTypeInfo channelTypeInfo){
        if(channelTypeInfo.getFlag() == 1){
            Intent intent = new Intent(ChannelTypeActivity.this, ChannelType2Activity.class);
            intent.putExtra("type", channelTypeInfo.getTag());
            startActivity(intent);
        }else {
            Intent intent = new Intent(ChannelTypeActivity.this, ChannelActivity.class);
            intent.putExtra(Constant.key.channel_type, channelTypeInfo.getName());
            startActivity(intent);
        }
    }
}
