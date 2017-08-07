package com.wiatec.bplay.adapter;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * main view pager transform
 */

public class MainViewPagerTransform implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        if(position < -1){
            page.setAlpha(0.2f);
            page.setScaleX(1f);
            page.setScaleY(1f);
        }else if(position <= 0){
            page.setAlpha(1.0f);
            page.setScaleX(1.2f);
            page.setScaleY(1.2f);
        }else if (position <= 1){
            page.setScaleX(1.2f);
            page.setScaleY(1.2f);
            page.setAlpha(1-position);
        }else{
            page.setScaleX(1f);
            page.setScaleY(1f);
            page.setAlpha(0.2f);
        }
    }
}
