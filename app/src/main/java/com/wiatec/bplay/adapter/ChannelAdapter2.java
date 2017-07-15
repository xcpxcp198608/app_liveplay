package com.wiatec.bplay.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.px.common.animator.Zoom;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.util.List;

/**
 * Created by patrick on 15/07/2017.
 * create time : 9:30 AM
 */

public class ChannelAdapter2 extends BaseAdapter {

    private Context context;
    private List<ChannelInfo> channelInfoList;
    private LayoutInflater layoutInflater;

    public ChannelAdapter2(Context context, List<ChannelInfo> channelInfoList) {
        this.context = context;
        this.channelInfoList = channelInfoList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return channelInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return channelInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_rcv_channel, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChannelInfo channelInfo = channelInfoList.get(position);
        viewHolder.textView.setText(channelInfo.getName());
        ImageMaster.load(context, channelInfo.getIcon(), viewHolder.imageView, R.drawable.img_hold3,
                R.drawable.img_hold3);
        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
        private TextView textView;
    }
}
