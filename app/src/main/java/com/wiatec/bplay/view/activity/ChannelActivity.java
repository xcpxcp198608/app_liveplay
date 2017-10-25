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
import com.px.common.utils.Logger;
import com.wiatec.bplay.R;
import com.wiatec.bplay.adapter.ChannelAdapter;
import com.wiatec.bplay.adapter.LiveChannelAdapter;
import com.wiatec.bplay.databinding.ActivityChannelBinding;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.LiveChannelInfo;
import com.wiatec.bplay.presenter.ChannelPresenter;

import java.io.Serializable;
import java.util.List;

/**
 * channel activity
 */

public class ChannelActivity extends BaseActivity<ChannelPresenter> implements Channel {

    private ActivityChannelBinding binding;
    private String type;

    @Override
    protected ChannelPresenter createPresenter() {
        return new ChannelPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel);
        type = getIntent().getStringExtra(Constant.key.channel_type);
        String key = getIntent().getStringExtra(Constant.key.key_search);
        if(Constant.key.type_favorite.equals(type)){
            presenter.loadFavorite();
        }else if(Constant.key.type_history.equals(type)){
            presenter.loadHistory();
        }else if(Constant.key.type_search.equals(type)){
            presenter.loadSearch(key);
        }else if(Constant.key.type_live_channel.equals(type)){
            presenter.loadLiveChannel();
        }else {
            presenter.loadChannel(type);
        }
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
            //
        }
    }

    @Override
    public void loadChannel(boolean execute, final List<ChannelInfo> channelInfoList) {
        load(execute, channelInfoList);
    }

    @Override
    public void loadFavorite(boolean execute, List<ChannelInfo> channelInfoList) {
        load(execute, channelInfoList);
    }

    @Override
    public void loadHistory(boolean execute, List<ChannelInfo> channelInfoList) {
        load(execute, channelInfoList);
    }

    @Override
    public void loadSearch(boolean execute, List<ChannelInfo> channelInfoList) {
        load(execute, channelInfoList);
    }

    private void load(boolean execute, final List<ChannelInfo> channelInfoList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            if(Constant.key.type_favorite.equals(type)){
                binding.tvLoading.setText(getString(R.string.favorite_load_empty));
            }else if(Constant.key.type_history.equals(type)){
                binding.tvLoading.setText(getString(R.string.history_load_empty));
            }else if(Constant.key.type_search.equals(type)){
                binding.tvLoading.setText(getString(R.string.search_load_empty));
            }else {
                binding.tvLoading.setText(getString(R.string.data_load_error));
                binding.btRetry.setVisibility(View.VISIBLE);
                binding.btRetry.requestFocus();
            }
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        binding.tvTotal.setText(channelInfoList.size()+"");
        binding.tvSplit.setVisibility(View.VISIBLE);
        binding.tvPosition.setText(1+"");

        ChannelAdapter channelAdapter = new ChannelAdapter(channelInfoList);
        binding.rcvChannel.setAdapter(channelAdapter);
        binding.rcvChannel.setLayoutManager(new GridLayoutManager(this, 5));
        channelAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int style = channelInfoList.get(position).getStyle();
                Application.setChannelInfoList(channelInfoList);
                if(style == 1){
                    launchFMPlay(channelInfoList, position);
                }else {
                    launchPlay(channelInfoList, position);
                }
            }
        });
        channelAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    binding.tvPosition.setText((position + 1) + "");
                    Zoom.zoomIn10to11(view);
                    view.setSelected(true);
                }else{
                    Zoom.zoomOut11to10(view);
                    view.setSelected(false);
                }
            }
        });
    }

    @Override
    public void loadLiveChannel(boolean execute, final List<LiveChannelInfo> liveChannelInfoList) {
        if(!execute){
            binding.pbLoading.setVisibility(View.GONE);
            if(Constant.key.type_favorite.equals(type)){
                binding.tvLoading.setText(getString(R.string.favorite_load_empty));
            }else if(Constant.key.type_history.equals(type)){
                binding.tvLoading.setText(getString(R.string.history_load_empty));
            }else if(Constant.key.type_search.equals(type)){
                binding.tvLoading.setText(getString(R.string.search_load_empty));
            }else {
                binding.tvLoading.setText(getString(R.string.data_load_error));
                binding.btRetry.setVisibility(View.VISIBLE);
                binding.btRetry.requestFocus();
            }
            return;
        }
        binding.llLoading.setVisibility(View.GONE);
        binding.tvTotal.setText(liveChannelInfoList.size()+"");
        binding.tvSplit.setVisibility(View.VISIBLE);
        binding.tvPosition.setText(1+"");

        LiveChannelAdapter liveChannelAdapter = new LiveChannelAdapter(liveChannelInfoList);
        binding.rcvChannel.setAdapter(liveChannelAdapter);
        binding.rcvChannel.setLayoutManager(new GridLayoutManager(this, 5));
        liveChannelAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                launchLivePlay(liveChannelInfoList.get(position));
            }
        });
        liveChannelAdapter.setOnItemFocusListener(new BaseRecycleAdapter.OnItemFocusListener() {
            @Override
            public void onFocus(View view, int position, boolean hasFocus) {
                if(hasFocus){
                    binding.tvPosition.setText((position + 1) + "");
                    Zoom.zoomIn10to11(view);
                    view.setSelected(true);
                }else{
                    Zoom.zoomOut11to10(view);
                    view.setSelected(false);
                }
            }
        });
    }

    private void launchPlay(List<ChannelInfo> channelInfoList, int position){
        Intent intent = new Intent(ChannelActivity.this , PlayActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void launchLivePlay(LiveChannelInfo liveChannelInfo){
        Intent intent = new Intent(ChannelActivity.this , PlayLiveActivity.class);
        intent.putExtra("liveChannelInfo", liveChannelInfo);
        startActivity(intent);
    }

    private void launchFMPlay(List<ChannelInfo> channelInfoList, int position){
        Intent intent = new Intent(ChannelActivity.this , FMPlayActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
