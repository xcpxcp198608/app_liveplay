package com.wiatec.bplay.instance;

import android.os.Environment;

/**
 * constant
 */

public final class Constant {

    public static final class url{
        static final String base = "http://www.ldlegacy.com:8080/liveplay/";
        public static final String ad_image = base+"adimage/";
        public static final String token = "/9B67E88314F416F2092AB8ECA6A7C8EDCCE3D6D85A816E6E6F9F919B2E6C277D";
        public static final String channel = base+"channel1s/list/";
        public static final String channel_type = base+"channel_type"+token;
        public static final String channel_send_error_report = base+"report/send";
        public static final String channel_type1 = base+"channel_type1/";
        public static final String channel_type2 = base+"channel_type2/";
        public static final String upgrade = base+"upgrade"+token;
    }

    public static final class path{
        public static final String ad_video = Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/Android/data/com.wiatec.btv_launcher/files/download/btvad.mp4";
    }

    public static final class packageName{
        public static final String market = "com.px.bmarket";
        public static final String btv = "org.xbmc.kodi";
        public static final String tv_house = "com.fanshi.tvvideo";
        public static final String terrarium_tv = "com.nitroxenon.terrarium";
        public static final String popcom = "pct.droid";
    }


    public static final class key{
        public static final String channel_type = "channelType";
        public static final String type_favorite = "FAVORITE";
        public static final String radio_music = "RADIO MUSIC";
        public static final String btv = "BTV";
    }
}
