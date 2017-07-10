package com.wiatec.bplay.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.px.common.utils.Logger;
import com.wiatec.bplay.model.UserContentResolver;
import com.wiatec.bplay.presenter.BasePresenter;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * base activity
 */

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    protected T presenter;
    protected abstract T createPresenter();
    private Subscription keyEventSubscription;
    protected boolean isSubscribe = true;
    protected int userLevel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        presenter = createPresenter();
        presenter.attach(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //通过Content provider从btv_launcher的sp中获取当前用户level
        String level = UserContentResolver.get("userLevel");
        try {
            userLevel = Integer.parseInt(level);
        }catch (Exception e){
            userLevel = 1;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        subscribeKeyEvent();
    }

    private void subscribeKeyEvent(){
        //不需要按键事件监听的页面在start中将 isSubscribe 设置为false
        if(!isSubscribe){
            return;
        }
        //用户等级大于2级时不进行按键事件监听
        if(userLevel > 2){
            return;
        }
        keyEventSubscription = Observable.timer(1200 , TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startActivity(new Intent(BaseActivity.this , AdScreenActivity.class));
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyEventSubscription != null){
            keyEventSubscription.unsubscribe();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        subscribeKeyEvent();
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        release();
    }

    @Override
    protected void onStop() {
        super.onStop();
        release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
        release();
    }

    private void release(){
        if(keyEventSubscription != null){
            keyEventSubscription.unsubscribe();
        }
    }
}
