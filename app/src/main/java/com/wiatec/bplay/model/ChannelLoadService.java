package com.wiatec.bplay.model;

/**
 * Created by patrick on 06/07/2017.
 * create time : 9:44 AM
 */

public interface ChannelLoadService<T> {


    void load(String type, ChannelLoadService.OnLoadListener<T> onLoadListener);
    void loadFavorite(ChannelLoadService.OnLoadListener<T> onLoadListener);
    void loadHistory(ChannelLoadService.OnLoadListener<T> onLoadListener);
    void loadSearch(String key, ChannelLoadService.OnLoadListener<T> onLoadListener);
    interface OnLoadListener<T>{
        void onLoad(boolean execute, T t);
    }
}
