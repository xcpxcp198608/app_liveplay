package com.wiatec.bplay.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * main activity view pager adapter
 */

public class MainViewPagerAdapter extends PagerAdapter {

    private List<View> list;
    private OnItemPageClick onItemPageClick;

    public MainViewPagerAdapter(List<View> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = list.get(position);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemPageClick != null) onItemPageClick.onClick(v, position);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public interface OnItemPageClick{
        void onClick(View view, int position);
    }

    public void setOnItemPageClick(OnItemPageClick onItemPageClick){
        this.onItemPageClick = onItemPageClick;
    }
}
