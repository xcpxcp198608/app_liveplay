package com.wiatec.bplay.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * sql helper
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LivePlay";
    public static final String TABLE_NAME = "favorite";
    private static final String CREATE_TABLE = "create table if not exists "+TABLE_NAME
            +"(_id integer primary key autoincrement, channelId integer, sequence integer, " +
            "tag text, name text, url text, icon text, type text, country text, style text, " +
            "visible integer, locked boolean)";
    private static final String DROP_TABLE = "drop table if exists " + TABLE_NAME;
    private static final int VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
