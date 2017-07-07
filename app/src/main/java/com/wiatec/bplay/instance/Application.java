package com.wiatec.bplay.instance;

import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.wiatec.bplay.dagger2.component.NetworkDataComponent;
import com.wiatec.bplay.task.ImageTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * application
 */

public class Application extends CommonApplication {

    private static ExecutorService executorService;
    public static String PATH_ADIMAGE;
    public static String PATH_DOWNLOAD;

    @Override
    public void onCreate() {
        super.onCreate();
        PATH_ADIMAGE = getExternalFilesDir("adimages").getAbsolutePath();
        PATH_DOWNLOAD = getExternalFilesDir("download").getAbsolutePath();
        //initial thread pool
        executorService = Executors.newCachedThreadPool();
        //execute image download task
        executorService.execute(new ImageTask());

    }

    public static ExecutorService getExecutorService(){
        return executorService;
    }

}
