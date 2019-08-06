package com.u8.sdk.analytics;


public class UUserLog {
    public static final int OP_CREATE_ROLE = 1;
    public static final int OP_ENTER_GAME = 2;
    public static final int OP_LEVEL_UP = 3;
    public static final int OP_EXIT = 4;
    private Integer userID;
    private Integer appID;
    private Integer channelID;
    private String serverID;
    private String serverName;
    private String roleID;
    private String roleName;
    private String roleLevel;
    private String deviceID;
    private Integer opType;

    public Integer getUserID() {
        return this.userID;
    }


    public void setUserID(Integer userID) {
        this.userID = userID;
    }


    public Integer getAppID() {
        return this.appID;
    }


    public void setAppID(Integer appID) {
        this.appID = appID;
    }


    public Integer getChannelID() {
        return this.channelID;
    }


    public void setChannelID(Integer channelID) {
        this.channelID = channelID;
    }


    public String getServerID() {
        return this.serverID;
    }


    public void setServerID(String serverID) {
        this.serverID = serverID;
    }


    public String getServerName() {
        return this.serverName;
    }


    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    public String getRoleID() {
        return this.roleID;
    }


    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }


    public String getRoleName() {
        return this.roleName;
    }


    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public String getRoleLevel() {
        return this.roleLevel;
    }


    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }


    public String getDeviceID() {
        return this.deviceID;
    }


    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }


    public Integer getOpType() {
        return this.opType;
    }


    public void setOpType(Integer opType) {
        this.opType = opType;
    }
}


