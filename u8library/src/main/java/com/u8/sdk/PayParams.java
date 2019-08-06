package com.u8.sdk;

import org.json.JSONObject;


public class PayParams {
    private String productId;
    private String productName;
    private String productDesc;
    private int price;
    private int ratio;
    private int buyNum;
    private int coinNum;
    private String serverId;
    private String serverName;
    private String roleId;
    private String roleName;
    private int roleLevel;
    private String payNotifyUrl;
    private String vip;
    private String orderID;
    private String extension;
    private String channelOrderID;
    private int state;

    public String toString() {
        JSONObject json = new JSONObject();

        try {
            json.put("productId", this.productId);
            json.put("price", this.price);
            json.put("orderID", this.orderID);
            json.put("payNotifyUrl", this.payNotifyUrl);
            json.put("extension", this.extension);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json.toString();
    }


    public String getProductId() {
        return this.productId;
    }


    public void setProductId(String productId) {
        this.productId = productId;
    }


    public String getProductName() {
        return this.productName;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getProductDesc() {
        return this.productDesc;
    }


    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }


    public int getPrice() {
        return this.price;
    }


    public void setPrice(int price) {
        this.price = price;
    }


    public int getBuyNum() {
        return this.buyNum;
    }


    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }


    public int getCoinNum() {
        return this.coinNum;
    }


    public void setCoinNum(int coinNum) {
        this.coinNum = coinNum;
    }


    public String getServerId() {
        return this.serverId;
    }


    public void setServerId(String serverId) {
        this.serverId = serverId;
    }


    public String getRoleId() {
        return this.roleId;
    }


    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


    public String getRoleName() {
        return this.roleName;
    }


    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }


    public int getRoleLevel() {
        return this.roleLevel;
    }


    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }


    public String getExtension() {
        return this.extension;
    }


    public void setExtension(String extension) {
        this.extension = extension;
    }


    public int getRatio() {
        return this.ratio;
    }


    public void setRatio(int ratio) {
        this.ratio = ratio;
    }


    public String getServerName() {
        return this.serverName;
    }


    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    public String getVip() {
        return this.vip;
    }


    public void setVip(String vip) {
        this.vip = vip;
    }


    public String getPayNotifyUrl() {
        return this.payNotifyUrl;
    }


    public void setPayNotifyUrl(String payNotifyUrl) {
        this.payNotifyUrl = payNotifyUrl;
    }


    public String getOrderID() {
        return this.orderID;
    }


    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }


    public String getChannelOrderID() {
        return this.channelOrderID;
    }


    public void setChannelOrderID(String channelOrderID) {
        this.channelOrderID = channelOrderID;
    }


    public int getState() {
        return this.state;
    }


    public void setState(int state) {
        this.state = state;
    }
}


