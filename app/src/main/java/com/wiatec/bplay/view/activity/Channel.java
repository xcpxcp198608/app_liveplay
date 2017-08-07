package com.wiatec.bplay.view.activity;

import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.ImageInfo;

import java.util.List;

/**
 * channel
 */

public interface Channel extends Common {

    void loadChannel(boolean execute, List<ChannelInfo> channelInfoList);
    void loadFavorite(boolean execute, List<ChannelInfo> channelInfoList);
}
