package com.wiatec.bplay.adapter;

import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.LiveChannelInfo;

import java.util.List;

/**
 * channel adapter
 */

public class LiveChannelAdapter extends BaseRecycleAdapter<ChannelViewHolder> {

    private List<LiveChannelInfo> liveChannelInfoList;

    public LiveChannelAdapter(List<LiveChannelInfo> liveChannelInfoList) {
        this.liveChannelInfoList = liveChannelInfoList;
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
    protected void bindHolder(final ChannelViewHolder holder, final int position) {
        LiveChannelInfo liveChannelInfo = liveChannelInfoList.get(position);
        holder.textView.setText(liveChannelInfo.getTitle());
        ImageMaster.load(liveChannelInfo.getPreview(), holder.imageView, R.drawable.img_hold3,
                R.drawable.img_hold3);
    }

    @Override
    public int getItemCounts() {
        return liveChannelInfoList.size();
    }
}
