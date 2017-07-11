package com.wiatec.bplay.model;

import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.pojo.ImageInfo;

import java.io.File;
import java.util.Random;

import javax.inject.Inject;

/**
 * ad image data model
 */
public class AdImageProvider implements LoadService<ImageInfo> {

    @Inject
    public AdImageProvider() {
    }

    //从已经下载的图片文件中随机选取一张
    @Override
    public void load(final OnLoadListener<ImageInfo> onLoadListener) {
        File file = new File(Application.PATH_AD_IMAGE);
        if(!file.exists()) return;
        File[] imageFiles = file.listFiles();
        if(imageFiles != null && imageFiles.length > 0){
            int random = new Random().nextInt(imageFiles.length);
            File file1 = imageFiles[random];
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setUrl(file1.getAbsolutePath());
            onLoadListener.onLoad(true, imageInfo);
//            Logger.d(imageInfo.toString());
        }
    }
}
