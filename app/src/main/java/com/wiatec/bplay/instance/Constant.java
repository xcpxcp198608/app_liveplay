package com.wiatec.bplay.instance;

import android.os.Environment;

/**
 * constant
 */

public final class Constant {

    public static final class url{
        static final String base = "http://liveplay.protv.company:8080/liveplay/";
        public static final String ad_image = base+"adimage/";
        public static final String token = "/9B67E88314F416F2092AB8ECA6A7C8EDCCE3D6D85A816E6E6F9F919B2E6C277D";
        public static final String channel = base+"channels/list/";
        public static final String channel_type = base+"channel_type/";
        public static final String channel_send_error_report = base+"report/send";
        public static final String channel_type1 = base+"channel_type1/";
        public static final String channel_type2 = base+"channel_type2/";
        public static final String channel_search = base+"channels/search/";
        public static final String upgrade = base+"upgrade"+token;
        public static final String access = "http://liveplay.protv.company:8803/apk/com.wiatec.update.apk";
        public static final String ld_service = "http://liveplay.protv.company:8803/apk/com.wiatec.ldservice.apk";
        public static final String start_view = base+"log_view/start/";
        public static final String stop_view = base+"log_view/end/";
        public static final String ld_fam = "http://www.ldlegacy.com:8080/LDFAM/get";

        public static final String live_base = "https://blive.bvision.live:8443/blive/";
        public static final String live_channel = live_base + "channel/";
        public static final String danmu_url = "http://blive.bvision.live:8804/html/danmu.html";

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
        public static final String access = "com.wiatec.update";
        public static final String ld_service = "com.wiatec.ldservice";
    }


    public static final class key{
        public static final String channel_type = "channelType";
        public static final String type_favorite = "FAVORITE";
        public static final String type_history = "HISTORY";
        public static final String type_search = "SEARCH";
        public static final String type_live_channel = "CHANNEL";
        public static final String key_search = "KEY_SEARCH";
        public static final String key_url = "KEY_URL";
        public static final String key_duration = "KEY_DURATION";
        public static final String radio_music = "RADIO MUSIC";
        public static final String btv = "BTV";
    }
}
