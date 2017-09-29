package com.wiatec.bplay.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * channel info
 */

public class ChannelInfo implements Parcelable {

    //primary key
    private int id;
    //channel id
    private int channelId;
    //sequence
    private int sequence;
    //query index
    private String tag;
    //channel name
    private String name;
    //stream url
    private String url;
    //channel icon
    private String icon;
    //the country of channel
    private String country;
    //channel type  1--live , 2--app , 3 -- relay
    private int type;
    //the style of channel
    private int style;
    //visible (0 - gone , 1 - visible)
    private boolean visible;
    private boolean locked;

    //for history sql
    private long viewTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public long getViewTime() {
        return viewTime;
    }

    public void setViewTime(long viewTime) {
        this.viewTime = viewTime;
    }

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "id=" + id +
                ", channelId=" + channelId +
                ", sequence=" + sequence +
                ", tag='" + tag + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", country='" + country + '\'' +
                ", type=" + type +
                ", style=" + style +
                ", visible=" + visible +
                ", locked=" + locked +
                ", viewTime=" + viewTime +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.channelId);
        dest.writeInt(this.sequence);
        dest.writeString(this.tag);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.icon);
        dest.writeString(this.country);
        dest.writeInt(this.type);
        dest.writeInt(this.style);
        dest.writeByte(this.visible ? (byte) 1 : (byte) 0);
        dest.writeByte(this.locked ? (byte) 1 : (byte) 0);
        dest.writeLong(this.viewTime);
    }

    public ChannelInfo() {
    }

    protected ChannelInfo(Parcel in) {
        this.id = in.readInt();
        this.channelId = in.readInt();
        this.sequence = in.readInt();
        this.tag = in.readString();
        this.name = in.readString();
        this.url = in.readString();
        this.icon = in.readString();
        this.country = in.readString();
        this.type = in.readInt();
        this.style = in.readInt();
        this.visible = in.readByte() != 0;
        this.locked = in.readByte() != 0;
        this.viewTime = in.readLong();
    }

    public static final Parcelable.Creator<ChannelInfo> CREATOR = new Parcelable.Creator<ChannelInfo>() {
        @Override
        public ChannelInfo createFromParcel(Parcel source) {
            return new ChannelInfo(source);
        }

        @Override
        public ChannelInfo[] newArray(int size) {
            return new ChannelInfo[size];
        }
    };
}
