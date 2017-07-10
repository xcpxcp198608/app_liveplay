package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.ChannelAdapter;
import com.wiatec.bplay.databinding.ActivityChannelBinding;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.presenter.ChannelPresenter;

import java.io.Serializable;
import java.util.List;

/**
 * channel activity
 */

public class ChannelActivity extends BaseActivity<ChannelPresenter> implements Channel {

    private ActivityChannelBinding binding;

    @Override
    protected ChannelPresenter createPresenter() {
        return new ChannelPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel);
//        presenter.loadAdImage();
        final String type = getIntent().getStringExtra(Constant.key.channel_type);
        presenter.loadChannel(type);
        binding.btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvLoading.setText(getString(R.string.data_loading));
                binding.pbLoading.setVisibility(View.VISIBLE);
                binding.btRetry.setVisibility(View.GONE);
                presenter.loadChannel(type);
            }
        });
    }

    @Override
    public void loadAdImage(boolean isSuccess, ImageInfo imageInfo) {
        if(isSuccess){
            ImageMaster.load(imageInfo.getUrl(), binding.ivChannel, R.drawable.img_ld_gold,
                    R.drawable.img_ld_gold);
        }
    }

    @Override
    public void loadChannel(boolean execute, final List<ChannelInfo> channelInfoList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            binding.tvLoading.setText(getString(R.string.data_load_error));
            binding.btRetry.setVisibility(View.VISIBLE);
            binding.btRetry.requestFocus();
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        binding.tvTotal.setText(channelInfoList.size()+"");
        binding.tvSplit.setVisibility(View.VISIBLE);
        binding.tvPosition.setText(1+"");
        final ChannelAdapter channelAdapter = new ChannelAdapter(channelInfoList);
        binding.rcvChannel.setAdapter(channelAdapter);
        binding.rcvChannel.setLayoutManager(new GridLayoutManager(this, 5,
                GridLayoutManager.VERTICAL, false));
        channelAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    Zoom.zoomIn10to11(view);
                    binding.tvPosition.setText((position+1)+"");
                    view.setSelected(true);
                }else{
                    Zoom.zoomOut11to10(view);
                    view.setSelected(false);
                }
            }
        });
        channelAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                launchPlay(channelInfoList, position);
            }
        });
    }

    private void launchPlay(List<ChannelInfo> channelInfoList, int position){
        Intent intent = new Intent(ChannelActivity.this , PlayActivity.class);
        intent.putExtra("channelInfoList", (Serializable) channelInfoList);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
