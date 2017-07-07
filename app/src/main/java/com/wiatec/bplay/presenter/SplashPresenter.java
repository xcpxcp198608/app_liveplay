package com.wiatec.bplay.presenter;

import com.wiatec.bplay.model.AdImageProvider;
import com.wiatec.bplay.model.UpgradeProvider;
import com.wiatec.bplay.model.LoadService;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.UpgradeInfo;
import com.wiatec.bplay.view.activity.Splash;

/**
 * splash presenter
 */

public class SplashPresenter extends BasePresenter<Splash> {

    private AdImageProvider adImageProvider;
    private UpgradeProvider upgradeProvider;
    private Splash splash;

    public SplashPresenter(Splash splash){
        this.splash = splash;
        adImageProvider = new AdImageProvider();
        upgradeProvider = new UpgradeProvider();
    }

    //调用model - AdImageProvider 获取需要的Image文件
    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    splash.loadAdImage(execute, imageInfo);
                }
            });
        }
    }

    //检查app upgradeProvider
    public void checkUpgrade(){
        if(upgradeProvider != null){
            upgradeProvider.load(new LoadService.OnLoadListener<UpgradeInfo>() {
                @Override
                public void onLoad(boolean execute, UpgradeInfo upgradeInfo) {
                    splash.checkUpgrade(execute, upgradeInfo);
                }
            });
        }
    }
}
