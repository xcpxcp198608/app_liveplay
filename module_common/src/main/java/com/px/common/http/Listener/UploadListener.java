package com.px.common.http.Listener;

import com.px.common.utils.SPUtil;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;


public abstract class UploadListener implements Callback {

    public abstract void onSuccess(Response response) throws IOException;
    public abstract void onFailure(String e);

    @Override
    public void onFailure(Call call, IOException e) {
        onFailure(e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        onSuccess(response);
    }
}
