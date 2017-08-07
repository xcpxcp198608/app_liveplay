package com.wiatec.bplay.adapter;

import android.view.View;

import com.px.common.adapter.BaseRecycleAdapter;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.TvSeriesInfo;

import java.util.List;

/**
 * channel adapter
 */

public class TvSeriousAdapter extends BaseRecycleAdapter<TvSeriousViewHolder> {

    private List<TvSeriesInfo> tvSeriesInfoList;

    public TvSeriousAdapter(List<TvSeriesInfo> tvSeriesInfoList) {
        this.tvSeriesInfoList = tvSeriesInfoList;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.item_rcv_channel;
    }

    @Override
    protected TvSeriousViewHolder createHolder(View view) {
        return new TvSeriousViewHolder(view);
    }

    @Override
    protected void bindHolder(final TvSeriousViewHolder holder, final int position) {
        TvSeriesInfo tvSeriesInfo = tvSeriesInfoList.get(position);
        holder.textView.setText(tvSeriesInfo.getName());
        ImageMaster.load(tvSeriesInfo.getUrl(), holder.imageView, R.drawable.img_hold3,
                R.drawable.img_hold3);
    }

    @Override
    public int getItemCounts() {
        return tvSeriesInfoList.size();
    }
}
