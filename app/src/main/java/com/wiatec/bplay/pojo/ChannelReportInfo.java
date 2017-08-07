package com.wiatec.bplay.pojo;

/**
 * Created by patrick on 03/08/2017.
 * create time : 5:54 PM
 */

public class ChannelReportInfo {
    private String userName;
    private String channelName;
    private String message;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChannelReportInfo{" +
                "userName='" + userName + '\'' +
                ", channelName='" + channelName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
