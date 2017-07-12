package com.wiatec.bplay.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.wiatec.bplay.entity.ResultInfo;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.sql.FavoriteChannelDao;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * channel provider
 */

public class ChannelProvider implements ChannelLoadService<List<ChannelInfo>> {

    @Inject
    public ChannelProvider() {

    }

    @Override
    public void load(String type, final OnLoadListener<List<ChannelInfo>> onLoadListener) {
        HttpMaster.get(Constant.url.channel+type+Constant.url.token)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo<ChannelInfo> resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo<ChannelInfo>>(){}.getType());
                        if(resultInfo == null){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        List<ChannelInfo> channelInfoList = resultInfo.getData();
                        if(channelInfoList == null || channelInfoList.size() <= 0){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, channelInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }

    @Override
    public void loadFavorite(OnLoadListener<List<ChannelInfo>> onLoadListener) {
        FavoriteChannelDao favoriteChannelDao = FavoriteChannelDao.getInstance();
        List<ChannelInfo> channelInfoList = favoriteChannelDao.queryAll();
        if(channelInfoList == null || channelInfoList.size() <= 0){
            onLoadListener.onLoad(false, null);
            return;
        }
        onLoadListener.onLoad(true, channelInfoList);
    }
}
