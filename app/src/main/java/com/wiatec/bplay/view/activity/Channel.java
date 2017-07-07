package com.wiatec.bplay.view.activity;

import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.pojo.ImageInfo;

import java.util.List;

/**
 * channel
 */

public interface Channel {

    void loadAdImage(boolean execute, ImageInfo imageInfo);
    void loadChannel(boolean execute, List<ChannelInfo> channelInfoList);
}
