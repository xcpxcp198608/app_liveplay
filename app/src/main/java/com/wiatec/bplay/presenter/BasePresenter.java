package com.wiatec.bplay.presenter;

import java.lang.ref.WeakReference;

/**
 * base presenter
 */

public abstract class BasePresenter<T> {

    private WeakReference<T> weakReference;

    public void attach(T t){
        if(weakReference == null){
            weakReference = new WeakReference<>(t);
        }
    }

    public void detach(){
        if(weakReference != null){
            weakReference.clear();
            weakReference = null;
        }
    }

}
