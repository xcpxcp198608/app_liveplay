package com.wiatec.bplay.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiatec.bplay.R;

/**
 * ChannelTypeViewHolder
 */

public class PlayChannelViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;

    public PlayChannelViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textView);

    }
}
