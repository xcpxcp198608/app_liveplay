package com.px.common.http.Listener;

import com.px.common.utils.SPUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;


public abstract class StringListener implements Callback {

    public abstract void onSuccess (String  s) throws IOException;
    public abstract void onFailure (String e);

    @Override
    public void onFailure(Call call, IOException e) {
        if(e.getMessage() == null){
            return;
        }
        Observable.just(e.getMessage())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        onFailure(s);
                    }
                });

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Headers headers = response.headers();
        List<String> cookies = headers.values("Set-Cookie");
        if(cookies != null && cookies.size() > 0 ) {
            String session = cookies.get(0);
            String cookie = session.substring(0, session.indexOf(";"));
            SPUtils.put("cookie", cookie);
        }
        Observable.just(response)
                .map(new Func1<Response, String>() {
                    @Override
                    public String call(Response response) {
                        try {
                            return response.body().string();
                        } catch (IOException e) {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(e.getMessage() != null) {
                            onFailure(e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            if(s!=null) {
                                onSuccess(s);
                            }else{
                                onFailure("response data is empty");
                            }
                        } catch (IOException e) {
                            onFailure("response data is empty");
                        }
                    }
                });
    }
}
