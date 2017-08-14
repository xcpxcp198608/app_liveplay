package com.wiatec.bplay.model;

/**
 * Created by patrick on 04/07/2017.
 * create time : 5:53 PM
 */

public interface LoadServiceWithParam<T> {

    void load(String param, OnLoadListener<T> onLoadListener);
    interface OnLoadListener<T>{
        void onLoad(boolean execute, T t);
    }
}
