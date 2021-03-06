package com.px.common.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;


/**
 * 基于rx java的事件传递处理
 */

public class RxBus {

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    private final Subject<Object ,Object> rxBus = new SerializedSubject<>(PublishSubject.create());

    // RxBus单例 ，全局只有一个实例， 双重校验锁保证多线程调用
    private RxBus (){}
    private static volatile RxBus instance;
    public static RxBus getDefault(){
        if(instance == null){
            synchronized (RxBus.class){
                if(instance == null){
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    //发送事件（事件由调用者定义）
    public void post(Object object){
        rxBus.onNext(object);
    }

    /**
     * 根据事件类型进行订阅过滤返回一个事件类型对应的Observable
     * @param event  事件
     * @param <T> 事件类型
     * @return 返回事件类型对应的Observable,订阅时直接使用subscribe()
     */
    public <T> Observable<T> toObservable(final Class<T> event){
        return rxBus
                .subscribeOn(Schedulers.io())
                .filter(new Func1<Object, Boolean>() {
                    @Override
                    public Boolean call(Object o) {
                        return event.isInstance(o);
                    }
                })
                .cast(event);
    }

}
