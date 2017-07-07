package com.wiatec.bplay.model;

import android.content.ContentResolver;
import android.net.Uri;

import com.px.common.utils.CommonApplication;

/**
 * get user information from btv_launcher by content provider
 */
public class UserContentResolver {

    private static final ContentResolver contentResolver = CommonApplication.context.getContentResolver();
    private static final String AUTH = "content://com.wiatec.btv_launcher.provide.UserContentProvider/user/";

    public static String get(String key){
        Uri uri = Uri.parse(AUTH + key);
        String result = "";
        try {
            result = contentResolver.getType(uri);
        }catch (Exception e){
            return result;
        }
        return result;
    }
}