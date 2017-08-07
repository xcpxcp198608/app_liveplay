package com.wiatec.bplay.view.activity;

import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.UpgradeInfo;

/**
 * splash activity interface
 */

public interface Splash extends Common {
    
    void checkUpgrade(boolean upgrade, UpgradeInfo upgradeInfo);
}
