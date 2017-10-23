package com.wiatec.bplay.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * channel info
 */

public class LiveChannelInfo implements Parcelable {

    private int id;
    private String name;
    private String url;
    private String playUrl;
    private String preview;
    private String category;
    private boolean available;
    /**
     * 1:default live
     */
    private int type;
    private String startTime;
    private int userId;
    private UserInfo userInfo;

    public LiveChannelInfo() {
    }

    public LiveChannelInfo(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "ChannelInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", playUrl='" + playUrl + '\'' +
                ", preview='" + preview + '\'' +
                ", category='" + category + '\'' +
                ", available=" + available +
                ", type=" + type +
                ", startTime='" + startTime + '\'' +
                ", userId=" + userId +
                ", userInfo=" + userInfo +
                '}';
    }

    private static class UserInfo implements Parcelable {
        private int id;
        private String username;
        private String password;
        private String email;
        private String phone;
        private String icon;
        private boolean status;
        private String registerTime;
        private ChannelInfo channelInfo;

        public UserInfo() {
        }

        public UserInfo(int id) {
            this.id = id;
        }

        public UserInfo(String username) {
            this.username = username;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getRegisterTime() {
            return registerTime;
        }

        public void setRegisterTime(String registerTime) {
            this.registerTime = registerTime;
        }

        public ChannelInfo getChannelInfo() {
            return channelInfo;
        }

        public void setChannelInfo(ChannelInfo channelInfo) {
            this.channelInfo = channelInfo;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", email='" + email + '\'' +
                    ", phone='" + phone + '\'' +
                    ", status=" + status +
                    ", registerTime='" + registerTime + '\'' +
                    ", channelInfo=" + channelInfo +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.username);
            dest.writeString(this.password);
            dest.writeString(this.email);
            dest.writeString(this.phone);
            dest.writeString(this.icon);
            dest.writeByte(this.status ? (byte) 1 : (byte) 0);
            dest.writeString(this.registerTime);
            dest.writeParcelable(this.channelInfo, flags);
        }

        protected UserInfo(Parcel in) {
            this.id = in.readInt();
            this.username = in.readString();
            this.password = in.readString();
            this.email = in.readString();
            this.phone = in.readString();
            this.icon = in.readString();
            this.status = in.readByte() != 0;
            this.registerTime = in.readString();
            this.channelInfo = in.readParcelable(ChannelInfo.class.getClassLoader());
        }

        public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
            @Override
            public UserInfo createFromParcel(Parcel source) {
                return new UserInfo(source);
            }

            @Override
            public UserInfo[] newArray(int size) {
                return new UserInfo[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.playUrl);
        dest.writeString(this.preview);
        dest.writeString(this.category);
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeString(this.startTime);
        dest.writeInt(this.userId);
        dest.writeParcelable(this.userInfo, flags);
    }

    protected LiveChannelInfo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.url = in.readString();
        this.playUrl = in.readString();
        this.preview = in.readString();
        this.category = in.readString();
        this.available = in.readByte() != 0;
        this.type = in.readInt();
        this.startTime = in.readString();
        this.userId = in.readInt();
        this.userInfo = in.readParcelable(UserInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<LiveChannelInfo> CREATOR = new Parcelable.Creator<LiveChannelInfo>() {
        @Override
        public LiveChannelInfo createFromParcel(Parcel source) {
            return new LiveChannelInfo(source);
        }

        @Override
        public LiveChannelInfo[] newArray(int size) {
            return new LiveChannelInfo[size];
        }
    };
}
