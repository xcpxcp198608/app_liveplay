package com.wiatec.bplay.instance;

import com.px.common.constant.CommonApplication;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * application
 */

public class Application extends CommonApplication {

    public static String PATH_AD_IMAGE;
    public static String PATH_DOWNLOAD;
    private static ExecutorService executorService;
    private static List<ChannelInfo> channelInfoList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        PATH_AD_IMAGE = getExternalFilesDir("ad_images").getAbsolutePath();
        PATH_DOWNLOAD = getExternalFilesDir("download").getAbsolutePath();
        executorService = Executors.newCachedThreadPool();
    }

    public static ExecutorService getExecutorService(){
        return executorService;
    }

    public static List<ChannelInfo> getChannelInfoList() {
        return channelInfoList;
    }

    public static void setChannelInfoList(List<ChannelInfo> channelInfoList) {
        Application.channelInfoList = channelInfoList;
    }
}
