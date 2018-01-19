package com.wiatec.bplay.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.listener.StringListener;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.LDFamInfo;

import java.io.IOException;
import java.util.List;


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
