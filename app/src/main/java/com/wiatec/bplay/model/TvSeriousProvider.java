package com.wiatec.bplay.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.TvSeriesInfo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

/**
 * ad image data model
 */
public class TvSeriousProvider implements LoadService<List<TvSeriesInfo>> {

    @Override
    public void load(final OnLoadListener<List<TvSeriesInfo>> onLoadListener) {
        HttpMaster.get(Constant.url.tv_series)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        List<TvSeriesInfo> tvSeriesInfoList = new Gson().fromJson(s,
                                new TypeToken<List<TvSeriesInfo>>(){}.getType());
                        if(tvSeriesInfoList == null || tvSeriesInfoList.size() <= 0){
                            onLoadListener.onLoad(false, null);
                        }
                        onLoadListener.onLoad(true, tvSeriesInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                        onLoadListener.onLoad(false, null);
                    }
                });
    }
}
