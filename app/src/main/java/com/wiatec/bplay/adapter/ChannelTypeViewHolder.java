package com.wiatec.bplay.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiatec.bplay.R;

/**
 * ChannelTypeViewHolder
 */

public class ChannelTypeViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView textView;

    public ChannelTypeViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        textView = (TextView) itemView.findViewById(R.id.textView);

    }
}
