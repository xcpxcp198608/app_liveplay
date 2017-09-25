package com.wiatec.bplay.presenter;

import com.wiatec.bplay.model.AdImageProvider;
import com.wiatec.bplay.model.ChannelLoadService;
import com.wiatec.bplay.model.ChannelProvider;
import com.wiatec.bplay.model.LoadService;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.view.activity.Channel;

import java.util.List;

import javax.inject.Inject;

/**
 * channel presenter
 */

public class ChannelPresenter extends BasePresenter {

    private Channel channel;
    AdImageProvider adImageProvider;
    ChannelProvider channelProvider;

    public ChannelPresenter(Channel channel) {
        this.channel = channel;
        adImageProvider= new AdImageProvider();
        channelProvider = new ChannelProvider();
    }

    //调用model - loadService 获取需要的Image文件
    public void loadAdImage(){
        if(adImageProvider != null){
            adImageProvider.load(new LoadService.OnLoadListener<ImageInfo>() {
                @Override
                public void onLoad(boolean execute, ImageInfo imageInfo) {
                    channel.loadAdImage(execute, imageInfo);
                }
            });
        }
    }

    //调用model - channelLoadService 获取需要的Image文件
    public void loadChannel(String type){
        if(channelProvider != null){
            channelProvider.load(type, new ChannelLoadService.OnLoadListener<List<ChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelInfo> channelInfos) {
                    channel.loadChannel(execute, channelInfos);
                }
            });
        }
    }

    public void loadFavorite(){
        if(channelProvider != null){
            channelProvider.loadFavorite(new ChannelLoadService.OnLoadListener<List<ChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelInfo> channelInfos) {
                    channel.loadFavorite(execute, channelInfos);
                }
            });
        }
    }

    public void loadHistory(){
        if(channelProvider != null){
            channelProvider.loadHistory(new ChannelLoadService.OnLoadListener<List<ChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelInfo> channelInfos) {
                    channel.loadHistory(execute, channelInfos);
                }
            });
        }
    }

    public void loadSearch(String key){
        if(channelProvider != null){
            channelProvider.loadSearch(key, new ChannelLoadService.OnLoadListener<List<ChannelInfo>>() {
                @Override
                public void onLoad(boolean execute, List<ChannelInfo> channelInfos) {
                    channel.loadSearch(execute, channelInfos);
                }
            });
        }
    }
}
