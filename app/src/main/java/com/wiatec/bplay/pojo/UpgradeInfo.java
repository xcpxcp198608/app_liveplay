package com.wiatec.bplay.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * upgrade info
 */

public class UpgradeInfo implements Parcelable {

    private int id;
    private String packageName;
    private String url;
    private String version;
    private int code;
    private String info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "UpgradeInfo{" +
                "id=" + id +
                ", packageName='" + packageName + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", code=" + code +
                ", info='" + info + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.packageName);
        dest.writeString(this.url);
        dest.writeString(this.version);
        dest.writeInt(this.code);
        dest.writeString(this.info);
    }

    public UpgradeInfo() {
    }

    protected UpgradeInfo(Parcel in) {
        this.id = in.readInt();
        this.packageName = in.readString();
        this.url = in.readString();
        this.version = in.readString();
        this.code = in.readInt();
        this.info = in.readString();
    }

    public static final Parcelable.Creator<UpgradeInfo> CREATOR = new Parcelable.Creator<UpgradeInfo>() {
        @Override
        public UpgradeInfo createFromParcel(Parcel source) {
            return new UpgradeInfo(source);
        }

        @Override
        public UpgradeInfo[] newArray(int size) {
            return new UpgradeInfo[size];
        }
    };
}
