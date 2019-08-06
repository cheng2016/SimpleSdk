package com.u8.sdk.verify;

import org.json.JSONObject;


public class UToken {
    private boolean suc;
    private int userID;
    private String sdkUserID;
    private String username;
    private String sdkUsername;
    private String token;
    private String extension;
    private String timestamp;

    public UToken() {
        this.suc = false;
    }


    public UToken(int userID, String sdkUserID, String username, String sdkUsername, String token, String extension, String timestamp) {
        this.userID = userID;
        this.sdkUserID = sdkUserID;
        this.username = username;
        this.sdkUsername = sdkUsername;
        this.token = token;
        this.extension = extension;
        this.timestamp = timestamp;
        this.suc = true;
    }


    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put("suc", this.suc);
            json.put("userID", this.userID);
            json.put("sdkUserID", this.sdkUserID);
            json.put("username", this.username);
            json.put("sdkUsername", this.sdkUsername);
            json.put("token", this.token);
            json.put("extension", this.extension);
            json.put("timestamp", this.timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }


    public int getUserID() {
        return this.userID;
    }


    public void setUserID(int userID) {
        this.userID = userID;
    }


    public String getSdkUserID() {
        return this.sdkUserID;
    }


    public void setSdkUserID(String sdkUserID) {
        this.sdkUserID = sdkUserID;
    }


    public String getToken() {
        return this.token;
    }


    public void setToken(String token) {
        this.token = token;
    }


    public boolean isSuc() {
        return this.suc;
    }


    public void setSuc(boolean suc) {
        this.suc = suc;
    }


    public String getExtension() {
        return this.extension;
    }


    public void setExtension(String extension) {
        this.extension = extension;
    }


    public String getUsername() {
        return this.username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getSdkUsername() {
        return this.sdkUsername;
    }


    public void setSdkUsername(String sdkUsername) {
        this.sdkUsername = sdkUsername;
    }
}


