package com.wiatec.bplay.instance;

import com.px.common.utils.CommonApplication;
import com.wiatec.bplay.task.ImageTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * application
 */

public class Application extends CommonApplication {

    public static String PATH_AD_IMAGE;
    public static String PATH_DOWNLOAD;

    @Override
    public void onCreate() {
        super.onCreate();
        PATH_AD_IMAGE = getExternalFilesDir("ad_images").getAbsolutePath();
        PATH_DOWNLOAD = getExternalFilesDir("download").getAbsolutePath();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new ImageTask());

    }

}
