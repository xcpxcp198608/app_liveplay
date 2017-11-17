package com.wiatec.bplay.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.wiatec.bplay.entity.ResultInfo;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.LDFamInfo;
import com.wiatec.bplay.pojo.LiveChannelInfo;
import com.wiatec.bplay.sql.FavoriteChannelDao;
import com.wiatec.bplay.sql.HistoryChannelDao;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * channel provider
 */

public class BVisionProvider implements LoadService<List<LDFamInfo>> {

    @Override
    public void load(final OnLoadListener<List<LDFamInfo>> onLoadListener) {
        HttpMaster.get(Constant.url.ld_fam)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        List<LDFamInfo> ldFamInfoList = new Gson().fromJson(s,
                                new TypeToken<List<LDFamInfo>>(){}.getType());
                        if(ldFamInfoList == null || ldFamInfoList.size() <= 0){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, ldFamInfoList);
                    }

                    @Override
                    public void onFailure(String e) {
                        onLoadListener.onLoad(false, null);
                    }
                });
    }
}
