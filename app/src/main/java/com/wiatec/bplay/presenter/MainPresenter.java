package com.wiatec.bplay.presenter;

import com.wiatec.bplay.model.AdImageProvider;
import com.wiatec.bplay.model.LoadService;
import com.wiatec.bplay.model.UpgradeProvider;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.view.activity.Common;

/**
 * splash presenter
 */

public class MainPresenter extends BasePresenter<Common> {

    AdImageProvider adImageProvider;
    UpgradeProvider upgradeProvider;
    private Common common;

    public MainPresenter(Common common){
        this.common = common;
        adImageProvider = new AdImageProvider();
        upgradeProvider = new UpgradeProvider();
    }

    //调用model - AdImageProvider 获取需要的Image文件
    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    common.onLoadAdImage(execute, imageInfo);
                }
            });
        }
    }

}
