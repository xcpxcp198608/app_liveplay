package com.wiatec.bplay.adapter;

import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.util.List;

/**
 * channel adapter
 */

public class PlayChannelAdapter extends BaseRecycleAdapter<PlayChannelViewHolder> {

    private List<ChannelInfo> channelInfoList;

    public PlayChannelAdapter(List<ChannelInfo> channelInfoList) {
        this.channelInfoList = channelInfoList;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_play_channel;
    }

    @Override
    protected PlayChannelViewHolder createHolder(View view) {
        return new PlayChannelViewHolder(view);
    }

    @Override
    protected void bindHolder(final PlayChannelViewHolder holder, final int position) {
        ChannelInfo channelInfo = channelInfoList.get(position);
        holder.textView.setText(channelInfo.getName());
    }

    @Override
    public int getItemCounts() {
        return channelInfoList.size();
    }
}
