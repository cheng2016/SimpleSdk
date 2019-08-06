package com.u8.sdk;

import org.json.JSONException;
import org.json.JSONObject;


public class UserExtraData {
    public static final int TYPE_SELECT_SERVER = 1;
    public static final int TYPE_CREATE_ROLE = 2;
    public static final int TYPE_ENTER_GAME = 3;
    public static final int TYPE_LEVEL_UP = 4;
    public static final int TYPE_EXIT_GAME = 5;
    public static final int TYPE_ENTER_COPY = 6;
    public static final int TYPE_EXIT_COPY = 7;
    public static final int TYPE_VIP_LEVELUP = 8;
    private int dataType;
    private String roleID;
    private String roleName;
    private String roleLevel;
    private int serverID;
    private String serverName;
    private int moneyNum;
    private long roleCreateTime;
    private long roleLevelUpTime;
    private String vip;
    private int roleGender;
    private String professionID = "0";
    private String professionName = "无";
    private String power = "0";
    private String partyID = "0";
    private String partyName = "无";
    private String partyMasterID = "0";
    private String partyMasterName = "无";


    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put("dataType", this.dataType);
            json.put("roleID", this.roleID);
            json.put("roleName", this.roleName);
            json.put("roleLevel", this.roleLevel);
            json.put("serverID", this.serverID);
            json.put("serverName", this.serverName);
            json.put("roleCreateTime", this.roleCreateTime);
            json.put("vip", this.vip);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }


    public int getDataType() {
        return this.dataType;
    }


    public void setDataType(int dataType) {
        this.dataType = dataType;
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


    public int getServerID() {
        return this.serverID;
    }


    public void setServerID(int serverID) {
        this.serverID = serverID;
    }


    public String getServerName() {
        return this.serverName;
    }


    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    public int getMoneyNum() {
        return this.moneyNum;
    }


    public void setMoneyNum(int moneyNum) {
        this.moneyNum = moneyNum;
    }


    public long getRoleCreateTime() {
        return this.roleCreateTime;
    }


    public void setRoleCreateTime(long roleCreateTime) {
        this.roleCreateTime = roleCreateTime;
    }


    public long getRoleLevelUpTime() {
        return this.roleLevelUpTime;
    }


    public void setRoleLevelUpTime(long roleLevelUpTime) {
        this.roleLevelUpTime = roleLevelUpTime;
    }


    public String getVip() {
        return this.vip;
    }


    public void setVip(String vip) {
        this.vip = vip;
    }


    public int getRoleGender() {
        return this.roleGender;
    }


    public void setRoleGender(int roleGender) {
        this.roleGender = roleGender;
    }


    public String getProfessionID() {
        return this.professionID;
    }


    public void setProfessionID(String professionID) {
        this.professionID = professionID;
    }


    public String getProfessionName() {
        return this.professionName;
    }


    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }


    public String getPower() {
        return this.power;
    }


    public void setPower(String power) {
        this.power = power;
    }


    public String getPartyID() {
        return this.partyID;
    }


    public void setPartyID(String partyID) {
        this.partyID = partyID;
    }


    public String getPartyName() {
        return this.partyName;
    }


    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }


    public String getPartyMasterID() {
        return this.partyMasterID;
    }


    public void setPartyMasterID(String partyMasterID) {
        this.partyMasterID = partyMasterID;
    }


    public String getPartyMasterName() {
        return this.partyMasterName;
    }


    public void setPartyMasterName(String partyMasterName) {
        this.partyMasterName = partyMasterName;
    }
}


