package com.wiatec.bplay.manager;

import com.px.common.http.HttpMaster;
import com.px.common.http.Listener.StringListener;
import com.px.common.utils.AESUtil;
import com.px.common.utils.CommonApplication;
import com.px.common.utils.EmojiToast;
import com.px.common.utils.Logger;
import com.px.common.utils.SPUtils;
import com.wiatec.bplay.R;
import com.wiatec.bplay.model.UserContentResolver;
import com.wiatec.bplay.pojo.ChannelInfo;

import java.io.IOException;
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

    public PlayManager(List<ChannelInfo> channelInfoList, int currentPosition) {
        this.channelInfoList = channelInfoList;
        this.currentPosition = currentPosition;
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
        long lastExperienceTime = (long) SPUtils.get("lastExperienceTime", 0L);
        boolean isLastExperience = (boolean) SPUtils.get("isLastExperience", true);
        if(lastExperienceTime + DURATION > System.currentTimeMillis() && isLastExperience){
            handlePlay();
            return;
        }
        int minute = 0;
        if (lastExperienceTime <= System.currentTimeMillis() - DURATION){
            SPUtils.put("lastExperienceTime", System.currentTimeMillis());
        }else {
            long leftTime = System.currentTimeMillis() - lastExperienceTime;
            minute = (int) (leftTime / 1000 / 60);
            minute = DURATION_MINUTE - minute;
            minute = minute < 1 && minute > 0 ? 1 : minute;
        }
        if(minute <= 0) SPUtils.put("isLastExperience", true);
        EmojiToast.showLong(CommonApplication.context.getString(R.string.notice2) + " " + minute +
                " minutes", EmojiToast.EMOJI_SMILE);
        if(mPlayListener != null) mPlayListener.playAd();
    }

    private void handlePlay(){
        int type = channelInfo.getType();
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
}
