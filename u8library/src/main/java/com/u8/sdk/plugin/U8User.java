package com.u8.sdk.plugin;

import android.util.Log;

import com.u8.sdk.IUser;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.analytics.UDAgent;
import com.u8.sdk.base.PluginFactory;
import com.u8.sdk.impl.SimpleDefaultUser;


public class U8User {
    private static U8User instance;
    private IUser userPlugin;

    public void init() {
        this.userPlugin = (IUser) PluginFactory.getInstance().initPlugin(1);
        if (this.userPlugin == null) {
            this.userPlugin = new SimpleDefaultUser();
        }
    }

    public static U8User getInstance() {
        if (instance == null) {
            instance = new U8User();
        }

        return instance;
    }


    public boolean isSupport(String method) {
        if (this.userPlugin == null) {
            return false;
        }
        return this.userPlugin.isSupportMethod(method);
    }


    public void login() {
        if (this.userPlugin == null) {
            return;
        }
        Log.d("U8SDK", "u8sdk begin to call login...");
        this.userPlugin.login();
    }

    public void login(String customData) {
        if (this.userPlugin == null) {
            return;
        }
        this.userPlugin.loginCustom(customData);
    }

    public void switchLogin() {
        if (this.userPlugin == null) {
            return;
        }

        this.userPlugin.switchLogin();
    }

    public void showAccountCenter() {
        if (this.userPlugin == null) {
            return;
        }

        this.userPlugin.showAccountCenter();
    }


    public void logout() {
        if (this.userPlugin == null) {
            return;
        }

        this.userPlugin.logout();
    }


    public void submitExtraData(UserExtraData extraData) {
        if (this.userPlugin == null) {
            return;
        }

        if (U8SDK.getInstance().isUseU8Analytics()) {
            UDAgent.getInstance().submitUserInfo(U8SDK.getInstance().getContext(), extraData);
        }

        if (extraData.getDataType() == 3) {
            U8Pay.getInstance().checkFailedOrders();
        }

        this.userPlugin.submitExtraData(extraData);
    }


    public void exit() {
        if (this.userPlugin == null) {
            return;
        }
        this.userPlugin.exit();
    }


    public void postGiftCode(String code) {
        if (this.userPlugin == null) {
            return;
        }
        this.userPlugin.postGiftCode(code);
    }

    public void queryProducts() {
        if (this.userPlugin == null) {
            return;
        }
        this.userPlugin.queryProducts();
    }


    public void queryAntiAddiction() {
        if (this.userPlugin == null) {
            return;
        }
        this.userPlugin.queryAntiAddiction();
    }
}


