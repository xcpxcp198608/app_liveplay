package com.wiatec.bplay.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.wiatec.bplay.entity.ResultInfo;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ChannelTypeInfo;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * channel type data model
 */

public class ChannelTypeProvider implements LoadService<List<ChannelTypeInfo>>{

    @Inject
    public ChannelTypeProvider() {
    }

    @Override
    public void load(final OnLoadListener<List<ChannelTypeInfo>> onLoadListener) {
        HttpMaster.get(Constant.url.channel_type)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo<ChannelTypeInfo> resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo<ChannelTypeInfo>>(){}.getType());
                        if(resultInfo == null){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        List<ChannelTypeInfo> channelTypeInfoList = resultInfo.getData();
                        if(channelTypeInfoList == null || channelTypeInfoList.size() <= 0){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, channelTypeInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }
}
