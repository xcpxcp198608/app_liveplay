package com.wiatec.bplay.adapter;

import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelTypeInfo;

import java.util.List;

/**
 * channel type adapter
 */

public class ChannelTypeAdapter extends BaseRecycleAdapter<ChannelTypeViewHolder> {

    private List<ChannelTypeInfo> channelTypeInfoList;

    public ChannelTypeAdapter(List<ChannelTypeInfo> channelTypeInfoList) {
        this.channelTypeInfoList = channelTypeInfoList;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_channel_type;
    }

    @Override
    protected ChannelTypeViewHolder createHolder(View view) {
        return new ChannelTypeViewHolder(view);
    }

    @Override
    protected void bindHolder(ChannelTypeViewHolder holder, int position) {
        ChannelTypeInfo channelTypeInfo = channelTypeInfoList.get(position);
        ImageMaster.load(channelTypeInfo.getIcon(),holder.imageView, R.drawable.img_hold1 ,
                R.drawable.img_hold1);
    }

    @Override
    public int getItemCounts() {
        return channelTypeInfoList.size();
    }
}
