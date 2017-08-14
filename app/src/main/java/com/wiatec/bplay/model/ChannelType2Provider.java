package com.wiatec.bplay.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ChannelType2Info;

import java.io.IOException;
import java.util.List;

/**
 * Created by patrick on 14/08/2017.
 * create time : 2:07 PM
 */

public class ChannelType2Provider implements LoadServiceWithParam<List<ChannelType2Info>> {
    @Override
    public void load(String param , final OnLoadListener<List<ChannelType2Info>> onLoadListener) {
        Logger.d(param);
        HttpMaster.get(Constant.url.channel_type2 + param + Constant.url.token)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        Logger.d(s);
                        List<ChannelType2Info> channelType2InfoList = new Gson().fromJson(s,
                                new TypeToken<List<ChannelType2Info>>(){}.getType());
                        if(channelType2InfoList != null && channelType2InfoList.size() > 0){
                            onLoadListener.onLoad(true, channelType2InfoList);
                        }else{
                            onLoadListener.onLoad(false, null);
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                        Logger.d(e);
                    }
                });
    }
}
