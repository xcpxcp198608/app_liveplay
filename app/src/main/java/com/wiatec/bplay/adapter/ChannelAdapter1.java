package com.wiatec.bplay.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.px.common.animator.Zoom;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.util.List;

/**
 * Created by patrick on 15/07/2017.
 * create time : 9:05 AM
 */

public class ChannelAdapter1 extends RecyclerView.Adapter<ChannelViewHolder>{

    private List<ChannelInfo> channelInfoList;
    private OnItemClickListener onItemClickListener;
    private OnItemFocusListener onItemFocusListener;
    private Context context;

    public ChannelAdapter1(List<ChannelInfo> channelInfoList) {
        this.channelInfoList = channelInfoList;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_channel,
                parent, false);
        context = parent.getContext();
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChannelViewHolder holder, final int position) {
        ChannelInfo channelInfo = channelInfoList.get(position);
        holder.textView.setText(channelInfo.getName());
        ImageMaster.load(context, channelInfo.getIcon(), holder.imageView, R.drawable.img_hold3,
                R.drawable.img_hold3);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClick(v, position);
                }
            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(onItemFocusListener != null){
                    onItemFocusListener.onFocus(v, position, hasFocus);
                }
                if(hasFocus){
                    Zoom.zoomIn10to11(v);
                    holder.textView.setSelected(true);
                }else{
                    Zoom.zoomOut11to10(v);
                    holder.textView.setSelected(false);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return channelInfoList.size();
    }

    public interface OnItemClickListener{
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemFocusListener{
        void onFocus(View view, int position, boolean hasFocus);
    }

    public void setOnItemFocusListener(OnItemFocusListener onItemFocusListener){
        this.onItemFocusListener = onItemFocusListener;
    }
}
