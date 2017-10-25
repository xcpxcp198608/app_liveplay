package com.px.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by patrick on 25/10/2017.
 * create time : 10:05 AM
 */

public class TimeUtil {

    public static long getUnixFromStr(String time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                new Locale("en"));
        Date date;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            return 0;
        }
        return date.getTime();
    }

    public static String getStringTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                new Locale("en"));
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static String getMediaTime(int ms) {
        int hour, minute, second;
        hour = ms / 3600000;
        minute = (ms - hour * 3600000) / 60000;
        second = (ms - hour * 3600000 - minute * 60000) / 1000;
        String sHour, sMinute, sSecond;
        if (hour < 10) {
            sHour = "0" + hour;
        } else {
            sHour = hour+"";
        }
        if (minute < 10) {
            sMinute = "0" + minute;
        } else {
            sMinute = minute + "";
        }
        if (second < 10) {
            sSecond = "0" + second;
        } else {
            sSecond = second + "";
        }
        return sHour + ":" + sMinute + ":" + sSecond;
    }
}
