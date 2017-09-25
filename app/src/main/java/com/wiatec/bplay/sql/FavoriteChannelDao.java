package com.wiatec.bplay.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.px.common.utils.CommonApplication;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * channel dao
 */

public class FavoriteChannelDao {

    private SQLiteDatabase sqLiteDatabase;
    private static FavoriteChannelDao instance;

    private FavoriteChannelDao(){
        sqLiteDatabase = new SQLiteHelper(CommonApplication.context).getWritableDatabase();
    }

    public static FavoriteChannelDao getInstance(){
        if(instance == null){
            synchronized (FavoriteChannelDao.class){
                if(instance == null){
                    instance = new FavoriteChannelDao();
                }
            }
        }
        return instance;
    }

    public boolean insertOrUpdate(ChannelInfo channelInfo){
        if(exists(channelInfo)){
            return update(channelInfo);
        }else{
            return insert(channelInfo);
        }
    }

    public boolean exists(ChannelInfo channelInfo){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.FAVORITE_TABLE_NAME, null, "tag=?",
                new String[]{channelInfo.getTag()}, null, null, null);
        boolean exists = cursor.moveToNext();
        cursor.close();
        return exists;
    }

    public boolean insert(ChannelInfo channelInfo){
        boolean flag = true;
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put("channelId", channelInfo.getChannelId());
            contentValues.put("sequence", channelInfo.getSequence());
            contentValues.put("tag", channelInfo.getTag());
            contentValues.put("name", channelInfo.getName());
            contentValues.put("url", channelInfo.getUrl());
            contentValues.put("icon", channelInfo.getIcon());
            contentValues.put("type", channelInfo.getType());
            contentValues.put("country", channelInfo.getCountry());
            contentValues.put("style", channelInfo.getStyle());
            contentValues.put("visible", channelInfo.isVisible());
            contentValues.put("locked", channelInfo.isLocked());
            sqLiteDatabase.insert(SQLiteHelper.FAVORITE_TABLE_NAME, null, contentValues);
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    public boolean update(ChannelInfo channelInfo){
        boolean flag = true;
        try{
            ContentValues contentValues = new ContentValues();
            contentValues.put("channelId", channelInfo.getChannelId());
            contentValues.put("sequence", channelInfo.getSequence());
            contentValues.put("name", channelInfo.getName());
            contentValues.put("url", channelInfo.getUrl());
            contentValues.put("icon", channelInfo.getIcon());
            contentValues.put("type", channelInfo.getType());
            contentValues.put("country", channelInfo.getCountry());
            contentValues.put("style", channelInfo.getStyle());
            contentValues.put("visible", channelInfo.isVisible());
            contentValues.put("locked", channelInfo.isLocked());
            sqLiteDatabase.update(SQLiteHelper.FAVORITE_TABLE_NAME, contentValues, "tag=?",
                    new String[]{channelInfo.getTag()});
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    public boolean delete(ChannelInfo channelInfo){
        boolean flag = true;
        try{
            sqLiteDatabase.delete(SQLiteHelper.FAVORITE_TABLE_NAME, "tag=?", new String[]{channelInfo.getTag()});
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    public List<ChannelInfo> queryAll(){
        Cursor cursor = sqLiteDatabase.query(SQLiteHelper.FAVORITE_TABLE_NAME, null, "_id>?",
                new String[]{"0"}, null, null, "name");
        List<ChannelInfo> channelInfoList = new ArrayList<>();
        while (cursor.moveToNext()){
            ChannelInfo channelInfo = new ChannelInfo();
            channelInfo.setChannelId(cursor.getInt(cursor.getColumnIndex("channelId")));
            channelInfo.setSequence(cursor.getInt(cursor.getColumnIndex("sequence")));
            channelInfo.setTag(cursor.getString(cursor.getColumnIndex("tag")));
            channelInfo.setName(cursor.getString(cursor.getColumnIndex("name")));
            channelInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            channelInfo.setIcon(cursor.getString(cursor.getColumnIndex("icon")));
            channelInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
            channelInfo.setCountry(cursor.getString(cursor.getColumnIndex("country")));
            channelInfo.setStyle(cursor.getInt(cursor.getColumnIndex("style")));
            channelInfo.setVisible(cursor.getInt(cursor.getColumnIndex("visible")) == 1);
            channelInfo.setLocked(cursor.getInt(cursor.getColumnIndex("locked")) == 1);
            channelInfoList.add(channelInfo);
        }
        return channelInfoList;
    }
}
