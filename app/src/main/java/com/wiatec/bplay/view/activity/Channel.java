package com.wiatec.bplay.view.activity;

import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.ImageInfo;
import com.wiatec.bplay.pojo.LiveChannelInfo;

import java.util.List;

/**
 * channel
 */

public interface Channel extends Common {

    void loadChannel(boolean execute, List<ChannelInfo> channelInfoList);
    void loadLiveChannel(boolean execute, List<LiveChannelInfo> liveChannelInfoList);
    void loadFavorite(boolean execute, List<ChannelInfo> channelInfoList);
    void loadHistory(boolean execute, List<ChannelInfo> channelInfoList);
    void loadSearch(boolean execute, List<ChannelInfo> channelInfoList);
}
