package com.wiatec.bplay.presenter;

import com.wiatec.bplay.model.ChannelType2Provider;
import com.wiatec.bplay.model.LoadServiceWithParam;
import com.wiatec.bplay.pojo.ChannelType2Info;
import com.wiatec.bplay.view.activity.ChannelType2;

import java.util.List;

/**
 * Created by patrick on 14/08/2017.
 * create time : 2:10 PM
 */

public class ChannelType2Presenter extends BasePresenter<ChannelType2> {

    private ChannelType2 channelType2;
    private ChannelType2Provider channelType2Provider;

    public ChannelType2Presenter(ChannelType2 channelType2) {
        this.channelType2 = channelType2;
        channelType2Provider = new ChannelType2Provider();
    }

    public void loadChannelType2(String type){
        channelType2Provider.load(type , new LoadServiceWithParam.OnLoadListener<List<ChannelType2Info>>() {
            @Override
            public void onLoad(boolean execute, List<ChannelType2Info> channelType2InfoList) {
                channelType2.loadChannelType2(execute, channelType2InfoList);
            }
        });
    }

}
