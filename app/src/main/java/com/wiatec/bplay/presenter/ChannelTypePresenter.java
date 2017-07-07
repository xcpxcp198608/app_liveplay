package com.wiatec.bplay.presenter;

import com.wiatec.bplay.model.AdImageProvider;
import com.wiatec.bplay.model.ChannelTypeProvider;
import com.wiatec.bplay.model.LoadService;
import com.wiatec.bplay.pojo.ChannelTypeInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.view.activity.ChannelType;

import java.util.List;

/**
 * channel type presenter
 */

public class ChannelTypePresenter extends BasePresenter<ChannelType> {

    private ChannelType channelType;
    private AdImageProvider adImageProvider;
    private ChannelTypeProvider channelTypeProvider;


    public ChannelTypePresenter(ChannelType channelType) {
        this.channelType = channelType;
        channelTypeProvider = new ChannelTypeProvider();
        adImageProvider = new AdImageProvider();
    }

    //调用model - AdImageProvider 获取需要的Image文件
    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    channelType.loadAdImage(execute, imageInfo);
                }
            });
        }
    }

    public void loadChannelType(){
        if(channelTypeProvider != null){
            channelTypeProvider.load(new LoadService.OnLoadListener<List<ChannelTypeInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelTypeInfo> channelTypeInfoList) {
                    channelType.loadChannelType(execute , channelTypeInfoList);
                }
            });
        }
    }
}
