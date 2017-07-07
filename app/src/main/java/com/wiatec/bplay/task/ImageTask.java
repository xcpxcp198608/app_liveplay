package com.wiatec.bplay.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.DownloadListener;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.Logger;
import com.wiatec.bplay.instance.Application;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.pojo.ImageInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * download ad image on background
 */

public class ImageTask implements Runnable {
    @Override
    public void run() {
        loadImage();
    }

    //get image list, execute download
    private void loadImage() {
        HttpMaster.get(Constant.url.ad_image)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        if(e.getMessage() != null) Logger.d(e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String s = response.body().string();
                        if(s == null) {
                            return;
                        }
                        List<ImageInfo> imageInfoList = new Gson().fromJson(s,
                                new TypeToken<List<ImageInfo>>(){}.getType());
                        if(imageInfoList == null || imageInfoList.size() <= 0){
                            Logger.d("image list gson parse error");
                            return;
                        }
                        List<String> fileNameList = new ArrayList<>();
                        for(ImageInfo imageInfo: imageInfoList){
                            fileNameList.add(imageInfo.getName());
                            download(imageInfo);
                        }
                        deleteOldImage(Application.PATH_ADIMAGE, fileNameList);
                    }
                });
    }

    // download image
    private void download(ImageInfo imageInfo){
        HttpMaster.download(CommonApplication.context)
                .name(imageInfo.getName())
                .url(imageInfo.getUrl())
                .path(Application.PATH_ADIMAGE)
                .startDownload(null);
    }

    private void deleteOldImage(String path, List<String> fileNameList) {
        File file = new File(path);
        if(file.exists()){
            File [] files = file.listFiles();
            if(files.length <= 0){
                return;
            }
            for (File file1 : files) {
                if (!fileNameList.contains(file1.getName())) {
                    //Logger.d(file1.getName() + " need delete");
                    file1.delete();
                }
            }

        }
    }
}
