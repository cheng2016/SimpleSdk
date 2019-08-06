package com.u8.sdk.analytics;


public class UDevice {
    private String deviceID;
    private Integer appID;
    private Integer channelID;
    private Integer subChannelID;
    private String mac;
    private String deviceType;
    private Integer deviceOS;
    private String deviceDpi;

    public Integer getChannelID() {
        return this.channelID;
    }


    public void setChannelID(Integer channelID) {
        this.channelID = channelID;
    }


    public String getDeviceID() {
        return this.deviceID;
    }


    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }


    public String getMac() {
        return this.mac;
    }


    public void setMac(String mac) {
        this.mac = mac;
    }


    public String getDeviceType() {
        return this.deviceType;
    }


    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


    public Integer getDeviceOS() {
        return this.deviceOS;
    }


    public void setDeviceOS(Integer deviceOS) {
        this.deviceOS = deviceOS;
    }


    public String getDeviceDpi() {
        return this.deviceDpi;
    }


    public void setDeviceDpi(String deviceDpi) {
        this.deviceDpi = deviceDpi;
    }


    public Integer getAppID() {
        return this.appID;
    }


    public void setAppID(Integer appID) {
        this.appID = appID;
    }


    public Integer getSubChannelID() {
        return this.subChannelID;
    }


    public void setSubChannelID(Integer subChannelID) {
        this.subChannelID = subChannelID;
    }
}


