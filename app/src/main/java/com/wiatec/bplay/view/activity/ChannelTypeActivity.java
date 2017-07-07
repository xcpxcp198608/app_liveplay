package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.image.ImageMaster;
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
            ImageMaster.load(imageInfo.getUrl(), binding.ivChannelType, R.drawable.img_ld_gold,
                    R.drawable.img_ld_gold);
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
                Intent intent = new Intent(ChannelTypeActivity.this , ChannelActivity.class);
                intent.putExtra(Constant.key.channel_type, channelTypeInfoList.get(position).getName());
                startActivity(intent);
            }
        });
    }
}
