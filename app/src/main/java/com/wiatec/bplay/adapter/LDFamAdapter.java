package com.wiatec.bplay.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.px.common.image.ImageMaster;
import com.wiatec.bplay.R;
import com.wiatec.bplay.pojo.LDFamInfo;

import java.util.List;


public class LDFamAdapter extends BaseAdapter {

    private Context context;
    private List<LDFamInfo> mList;
    private LayoutInflater layoutInflater;

    public LDFamAdapter(Context context, List<LDFamInfo> mList) {
        this.context = context;
        this.mList = mList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_ld_fam , parent ,false);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tv_message);
            viewHolder.ivImg1 = (ImageView) convertView.findViewById(R.id.iv_img1);
            //viewHolder.ivImg2 = (ImageView) convertView.findViewById(R.id.iv_img2);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LDFamInfo ldFamInfo = mList.get(position);
        if("Sponsor".equals(ldFamInfo.getUserName())){
            viewHolder.tvUserName.setTextColor(Color.rgb(0,0,255));
            viewHolder.tvMessage.setTextColor(Color.rgb(0,0,0));
            viewHolder.tvTime.setVisibility(View.GONE);
        }else if("Announcement".equals(ldFamInfo.getUserName())){
            viewHolder.tvUserName.setTextColor(Color.rgb(255,0,0));
            viewHolder.tvMessage.setTextColor(Color.rgb(255,0,0));
            viewHolder.tvTime.setVisibility(View.VISIBLE);
        }else if("Tips & Tricks".equals(ldFamInfo.getUserName())){
            viewHolder.tvUserName.setTextColor(Color.rgb(0, 255,0));
            viewHolder.tvMessage.setTextColor(Color.rgb(0,0,0));
            viewHolder.tvTime.setVisibility(View.GONE);
        }else{
            viewHolder.tvUserName.setTextColor(context.getResources().getColor(R.color.colorGray1));
            viewHolder.tvMessage.setTextColor(Color.rgb(0,0,0));
            viewHolder.tvTime.setVisibility(View.VISIBLE);
        }
        viewHolder.tvUserName.setText(ldFamInfo.getUserName());
        String time = ldFamInfo.getTime().substring(0 , ldFamInfo.getTime().length() -10);
        viewHolder.tvTime.setText(time);
        viewHolder.tvMessage.setText(ldFamInfo.getMessage());
        ImageMaster.load(context, ldFamInfo.getImg1(), viewHolder.ivImg1);
        return convertView;
    }

    private static class ViewHolder {
        private TextView tvUserName;
        private TextView tvTime;
        private TextView tvMessage;
        private ImageView ivImg1;
        private ImageView ivImg2;
    }

    public void notifyChange(List<LDFamInfo> ldFamInfoList){
        this.mList = ldFamInfoList;
        notifyDataSetChanged();
    }
}
