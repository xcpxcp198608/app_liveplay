package com.wiatec.bplay.manager;

import android.text.TextUtils;

import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.AESUtil;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtil;
import com.px.common.utils.SysUtil;
import com.wiatec.bplay.R;
import com.wiatec.bplay.instance.Constant;
import com.wiatec.bplay.model.UserContentResolver;
import com.wiatec.bplay.pojo.ChannelInfo;
import com.wiatec.bplay.sql.HistoryChannelDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * play manager
 */

public class PlayManager {

    private List<ChannelInfo> channelInfoList;
    private int currentPosition;
    private ChannelInfo channelInfo;
    private PlayListener mPlayListener;
    private int level;
    private static final long DURATION = 300000;
    private static final int DURATION_MINUTE = (int) (DURATION / 1000 / 60);
    private String experience;
    private HistoryChannelDao historyChannelDao;

    public PlayManager(List<ChannelInfo> channelInfoList, int currentPosition) {
        this.channelInfoList = channelInfoList;
        this.currentPosition = currentPosition;
        historyChannelDao = HistoryChannelDao.getInstance();
        channelInfo = channelInfoList.get(currentPosition);
        String levelStr = UserContentResolver.get("userLevel");
        try {
            level = Integer.parseInt(levelStr);
        }catch (Exception e){
            level = 1;
        }
        experience = UserContentResolver.get("experience");
    }

    public interface PlayListener{
        void play(String url);
        void playAd();
        void launchApp(String packageName);
    }

    public void setPlayListener(PlayListener playListener){
        mPlayListener = playListener;
    }

    public ChannelInfo getChannelInfo(){
        return channelInfo;
    }

    public void setChannelInfo(ChannelInfo channelInfo) {
        this.channelInfo = channelInfo;
    }

    //get user level
    public int getLevel() {
        return level;
    }

    // check user is in experience
    public boolean isExperience(){
        return "true".equals(experience);
    }

    public void dispatchChannel(){
        if(!channelInfo.isLocked()) {
            handlePlay();
            return;
        }
        if(level > 2){
            handlePlay();
            return;
        }
        if("true".equals(experience)){
            handlePlay();
            return;
        }
        if(channelInfo.getType() == 2){
            if(mPlayListener != null) mPlayListener.playAd();
            return;
        }
        long lastExperienceTime = (long) SPUtil.get("lastExperienceTime", 0L);
        boolean isLastExperience = (boolean) SPUtil.get("isLastExperience", true);
        if(lastExperienceTime + DURATION > System.currentTimeMillis() && isLastExperience){
            handlePlay();
            return;
        }
        int minute = 0;
        if (lastExperienceTime <= System.currentTimeMillis() - DURATION){
            SPUtil.put("lastExperienceTime", System.currentTimeMillis());
        }else {
            long leftTime = System.currentTimeMillis() - lastExperienceTime;
            minute = (int) (leftTime / 1000 / 60);
            minute = DURATION_MINUTE - minute;
            minute = minute < 1 && minute > 0 ? 1 : minute;
        }
        if(minute <= 0) SPUtil.put("isLastExperience", true);
        EmojiToast.showLong(CommonApplication.context.getString(R.string.notice2) + " " + minute +
                CommonApplication.context.getString(R.string.notice22), EmojiToast.EMOJI_SMILE);
        if(mPlayListener != null) mPlayListener.playAd();
    }

    private void handlePlay(){
        int type = channelInfo.getType();
        historyChannelDao.insertOrUpdate(channelInfo);
        String url = AESUtil.decrypt(channelInfo.getUrl(), AESUtil.KEY);
        if(type == 1){ //live
            if(mPlayListener != null) mPlayListener.play(url);
        }else if(type == 3) { //relay
            HttpMaster.get(url)
                    .enqueue(new StringListener() {
                        @Override
                        public void onSuccess(String s) throws IOException {
                            if(s == null) return;
                            if(mPlayListener != null) mPlayListener.play(s);
                        }

                        @Override
                        public void onFailure(String e) {
                            Logger.d(e);
                        }
                    });
        }else if(type == 2){ // app
            if(mPlayListener != null) mPlayListener.launchApp(AESUtil.decrypt(channelInfo.getUrl(),
                    AESUtil.KEY));
        }else{
            Logger.d("type error");
        }
    }

    public void previousChannel(){
        currentPosition -- ;
        if(currentPosition < 0) currentPosition = channelInfoList.size() - 1;
        channelInfo = channelInfoList.get(currentPosition);
        dispatchChannel();
    }

    public void nextChannel(){
        currentPosition ++ ;
        if(currentPosition >= channelInfoList.size()) currentPosition = 0;
        channelInfo = channelInfoList.get(currentPosition);
        dispatchChannel();
    }

    public void startView(String tag){
        if(tag == null) return;
        String username = UserContentResolver.get("userName");
        if(TextUtils.isEmpty(username)) username = "default";
        String country = getChannelInfo().getCountry();
        if(country.contains("&")){
            country = country.replaceAll("&", " ");
        }
        String name = getChannelInfo().getName();
        if(name.contains("&")){
            name = name.replaceAll("&", "");
        }
        HttpMaster.post(Constant.url.start_view)
                .parames("tag", tag)
                .parames("channelName", country + ":" + name)
                .parames("username", username)
                .parames("mac", SysUtil.getEthernetMac())
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {

                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });
    }

    public void stopView(String tag){
        if(tag == null) return;
        HttpMaster.post(Constant.url.stop_view)
                .parames("tag", tag)
                .enqueue(new StringListener() {
                    @Override
                    public void onSuccess(String s) throws IOException {

                    }

                    @Override
                    public void onFailure(String e) {

                    }
                });
    }

    public static List<String> parseUrl(String url){
        List<String> urlList;
        if(url.contains("#")){
            urlList = new ArrayList<>(Arrays.asList(url.split("#")));
        }else{
            urlList = new ArrayList<>();
            urlList.add(url);
        }
        List<String> urlList1 = new ArrayList<>();
        for (String u : urlList){
            if (u.contains("protv.company")){
                String streamToken = (String) SPUtil.get("streamToken", "123");
                u += "?token=" + streamToken;
            }
            urlList1.add(u);
        }
        return urlList1;
    }
}
