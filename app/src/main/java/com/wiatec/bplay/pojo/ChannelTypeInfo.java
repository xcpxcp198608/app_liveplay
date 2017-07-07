package com.wiatec.bplay.pojo;

/**
 * channel type info
 */

public class ChannelTypeInfo {

    private int id;
    private String name;
    private String icon;
    private int flag;
    private int isLock;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    @Override
    public String toString() {
        return "ChannelTypeInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", flag=" + flag +
                ", isLock=" + isLock +
                '}';
    }
}
