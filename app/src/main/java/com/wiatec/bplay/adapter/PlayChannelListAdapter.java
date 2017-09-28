package com.wiatec.bplay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.util.List;

/**
 * Created by patrick on 28/09/2017.
 * create time : 4:02 PM
 */

public class PlayChannelListAdapter extends BaseAdapter {

    private List<ChannelInfo> channelInfoList;
    private Context context;

    public PlayChannelListAdapter(Context context, List<ChannelInfo> channelInfoList) {
        this.channelInfoList = channelInfoList;
        this.context = context;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_play_channel, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(channelInfoList.get(position).getName());
        return convertView;
    }

    class ViewHolder{
        public TextView textView;
    }
}
