package com.wiatec.bplay.adapter;

import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelType1Info;
import com.wiatec.bplay.pojo.ChannelType2Info;

import java.util.List;

/**
 * channel adapter
 */

public class ChannelType1Adapter extends BaseRecycleAdapter<ChannelType1ViewHolder> {

    private List<ChannelType1Info> channelType1InfoList;

    public ChannelType1Adapter(List<ChannelType1Info> channelType1InfoList) {
        this.channelType1InfoList = channelType1InfoList;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_channel_type;
    }

    @Override
    protected ChannelType1ViewHolder createHolder(View view) {
        return new ChannelType1ViewHolder(view);
    }

    @Override
    protected void bindHolder(final ChannelType1ViewHolder holder, final int position) {
        ChannelType1Info channelType1Info = channelType1InfoList.get(position);
        holder.textView.setText(channelType1Info.getName());
        ImageMaster.load(channelType1Info.getUrl(), holder.imageView, R.drawable.img_hold3,
                R.drawable.img_hold3);
    }

    @Override
    public int getItemCounts() {
        return channelType1InfoList.size();
    }
}
