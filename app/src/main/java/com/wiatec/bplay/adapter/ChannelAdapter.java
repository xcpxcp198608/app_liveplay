package com.wiatec.bplay.adapter;

import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.animator.Zoom;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.util.List;

/**
 * channel adapter
 */

public class ChannelAdapter extends BaseRecycleAdapter<ChannelViewHolder> {

    private List<ChannelInfo> channelInfoList;
    public ChannelViewHolder holder;

    public ChannelAdapter(List<ChannelInfo> channelInfoList) {
        this.channelInfoList = channelInfoList;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_channel;
    }

    @Override
    protected ChannelViewHolder createHolder(View view) {
        return new ChannelViewHolder(view);
    }

    @Override
    protected void bindHolder(final ChannelViewHolder holder, int position) {
        this.holder = holder;
        ChannelInfo channelInfo = channelInfoList.get(position);
        holder.textView.setText(channelInfo.getName());
        ImageMaster.load(channelInfo.getIcon(), holder.imageView, R.drawable.img_hold2,
                R.drawable.img_hold2);
    }

    @Override
    public int getItemCounts() {
        return channelInfoList.size();
    }
}
