package com.wiatec.bplay.dagger2.component;

import com.wiatec.bplay.dagger2.module.NetworkDataModule;
import com.wiatec.bplay.presenter.SplashPresenter;
import com.wiatec.bplay.view.activity.Splash;

import dagger.Component;

/**
 * splash component
 */
@NetworkDataModule.SingleModule
@Component(modules = {NetworkDataModule.class})
public interface NetworkDataComponent {

}
