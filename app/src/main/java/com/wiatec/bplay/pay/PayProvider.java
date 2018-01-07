package com.wiatec.bplay.pay;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.Logger;
import com.wiatec.bplay.entity.ResultInfo;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.model.LoadService;
import com.wiatec.bplay.pojo.LiveChannelInfo;

import java.io.IOException;
import java.util.List;

/**
 * Created by patrick on 06/11/2017.
 * create time : 4:24 PM
 */

public class PayProvider {

    public void payVerify(String payerName, int publisherId, String paymentId,
                           final LoadService.OnLoadListener<ResultInfo<PayResultInfo>> onLoadListener){
        String url = Constant.url.live_base + "pay/verify/" + payerName + "/" + publisherId;
        if(!TextUtils.isEmpty(paymentId)){
            url = url + "/" + paymentId;
        }
        HttpMaster.post(url)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        ResultInfo<PayResultInfo> resultInfo = new Gson().fromJson(s,
                                new TypeToken<ResultInfo<PayResultInfo>>(){}.getType());
                        if(resultInfo == null){
                            onLoadListener.onLoad(false, null);
                            return;
                        }
                        onLoadListener.onLoad(true, resultInfo);
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                        onLoadListener.onLoad(false, null);
                    }
                });
    }
}
