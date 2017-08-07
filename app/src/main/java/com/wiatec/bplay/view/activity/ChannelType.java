package com.wiatec.bplay.view.activity;

import com.wiatec.bplay.pojo.ChannelTypeInfo;
import com.wiatec.bplay.pojo.ImageInfo;

import java.util.List;

/**
 * channel type
 */

public interface ChannelType extends Common {

    void loadChannelType(boolean execute, List<ChannelTypeInfo> channelTypeInfoList);
}
