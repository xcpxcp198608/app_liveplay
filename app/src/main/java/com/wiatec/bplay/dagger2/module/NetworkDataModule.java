package com.wiatec.bplay.dagger2.module;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

import dagger.Module;

/**
 * the module of data from network
 */
@Module
public class NetworkDataModule {

    //自定义注解，用于将此module中的类在application中初始化，全局单例提供
    @Scope
    @Retention(RetentionPolicy.RUNTIME)
    public @interface SingleModule{}

}
