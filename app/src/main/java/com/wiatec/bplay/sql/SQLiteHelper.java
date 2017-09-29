package com.wiatec.bplay.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * sql helper
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LivePlay";
    public static final String FAVORITE_TABLE_NAME = "favorite";
    public static final String HISTORY_TABLE_NAME = "history";
    private static final String CREATE_FAVORITE_TABLE = "create table if not exists "+ FAVORITE_TABLE_NAME
            +"(_id integer primary key autoincrement, channelId integer, sequence integer, " +
            "tag text, name text, url text, icon text, country text, type integer, style integer, " +
            "visible boolean, locked boolean)";
    private static final String CREATE_HISTORY_TABLE = "create table if not exists "+ HISTORY_TABLE_NAME
            +"(_id integer primary key autoincrement, channelId integer, sequence integer, " +
            "tag text, name text, url text, icon text, country text, type integer, style integer, " +
            "visible boolean, locked boolean, viewTime integer)";
    private static final String DROP_FAVORITE_TABLE = "drop table if exists " + FAVORITE_TABLE_NAME;
    private static final String DROP_HISTORY_TABLE = "drop table if exists " + HISTORY_TABLE_NAME;
    private static final int VERSION = 6;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_TABLE);
        db.execSQL(CREATE_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_FAVORITE_TABLE);
        db.execSQL(DROP_HISTORY_TABLE);
        onCreate(db);
    }
}
