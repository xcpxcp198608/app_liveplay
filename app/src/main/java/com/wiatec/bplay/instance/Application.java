package com.wiatec.bplay.instance;

import com.px.common.utils.CommonApplication;
import com.wiatec.bplay.task.ImageTask;
import com.wiatec.bplay.task.TokenTask;

import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * application
 */

public class Application extends CommonApplication {

    public static String PATH_AD_IMAGE;
    public static String PATH_DOWNLOAD;
    private static ExecutorService executorService;

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
}
