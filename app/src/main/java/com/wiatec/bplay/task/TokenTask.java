package com.wiatec.bplay.task;

import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.AESUtil;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtils;
import com.wiatec.bplay.instance.Application;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by patrick on 14/09/2017.
 * create time : 5:26 PM
 */

public class TokenTask extends TimerTask {

    private static final String URL = "http://apius.protv.company/v1/get_token.do?";
    private static final String PRE="BTVi35C41E7";
    private static final String PWD="Ho2oMcqUZMMvFzqb";

    @Override
    public void run() {
        loadToken();
    }

    private void loadToken(){
        long time = System.currentTimeMillis();
        time = time / 1000;
        String t = AESUtil.MD5(PRE+PWD+time);
        String url = URL + "reg_date="+time+"&token="+t;
        HttpMaster.get(url)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject data = jsonObject.getJSONObject("data");
                            String streamToken = data.getString("token");
                            SPUtils.put(Application.context, "streamToken", streamToken);
                        } catch (JSONException e) {
                            Logger.d("token json format error");
                        }
                    }

                    @Override
                    public void onFailure(String e) {
                        Logger.d(e);
                    }
                });
    }
}
