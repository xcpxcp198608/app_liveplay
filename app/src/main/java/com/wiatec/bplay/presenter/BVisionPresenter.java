package com.wiatec.bplay.presenter;

import com.wiatec.bplay.model.AdImageProvider;
import com.wiatec.bplay.model.BVisionProvider;
import com.wiatec.bplay.model.ChannelTypeProvider;
import com.wiatec.bplay.model.LoadService;
import com.wiatec.bplay.model.LoadServiceWithParam;
import com.wiatec.bplay.pojo.ChannelTypeInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.LDFamInfo;
import com.wiatec.bplay.view.activity.BVision;
import com.wiatec.bplay.view.activity.ChannelType;

import java.util.List;

/**
 * channel type presenter
 */

public class BVisionPresenter extends BasePresenter<BVision> {

    private BVision bVision;
    AdImageProvider adImageProvider;
    ChannelTypeProvider channelTypeProvider;
    BVisionProvider bVisionProvider;


    public BVisionPresenter(BVision bVision) {
        this.bVision = bVision;
        adImageProvider = new AdImageProvider();
        channelTypeProvider = new ChannelTypeProvider();
        bVisionProvider = new BVisionProvider();
    }

    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    bVision.onLoadAdImage(execute, imageInfo);
                }
            });
        }
    }

    public void loadChannelType(String type){
        if(channelTypeProvider != null){
            channelTypeProvider.load(type, new LoadServiceWithParam.OnLoadListener<List<ChannelTypeInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelTypeInfo> channelTypeInfoList) {
                    bVision.onLoadChannelType(execute , channelTypeInfoList);
                }
            });
        }
    }

    public void loadLDFam(){
        if(bVisionProvider != null){
            bVisionProvider.load(new LoadService.OnLoadListener<List<LDFamInfo>>() {
                @Override
                public void onLoad(boolean execute, List<LDFamInfo> ldFamInfoList) {
                    bVision.onLoadLDFam(execute, ldFamInfoList);
                }
            });
        }
    }
}
