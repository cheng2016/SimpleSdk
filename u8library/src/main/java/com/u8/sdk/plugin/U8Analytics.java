package com.u8.sdk.plugin;

import com.u8.sdk.IAnalytics;
import com.u8.sdk.base.PluginFactory;


public class U8Analytics {
    private static U8Analytics instance;
    private IAnalytics analyticsPlugin;

    public static U8Analytics getInstance() {
        if (instance == null) {
            instance = new U8Analytics();
        }

        return instance;
    }


    public void init() {
        this.analyticsPlugin = (IAnalytics) PluginFactory.getInstance().initPlugin(5);
    }


    public boolean isSupport(String method) {
        if (this.analyticsPlugin == null) {
            return false;
        }
        return this.analyticsPlugin.isSupportMethod(method);
    }


    public void startLevel(String level) {
        if (this.analyticsPlugin == null) {
            return;
        }

        this.analyticsPlugin.startLevel(level);
    }


    public void failLevel(String level) {
        if (this.analyticsPlugin == null) {
            return;
        }

        this.analyticsPlugin.failLevel(level);
    }


    public void finishLevel(String level) {
        if (this.analyticsPlugin == null) {
            return;
        }

        this.analyticsPlugin.finishLevel(level);
    }


    public void startTask(String task, String type) {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.startTask(task, type);
    }


    public void failTask(String task) {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.failTask(task);
    }


    public void finishTask(String task) {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.finishTask(task);
    }


    public void payRequest(String orderID, String productID, double money, String currency) {
        if (this.analyticsPlugin == null)
            return;
        this.analyticsPlugin.payRequest(orderID, productID, money, currency);
    }


    public void pay(String orderID, double money, int num) {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.pay(orderID, money, num);
    }


    public void buy(String item, int num, double price) {
        if (this.analyticsPlugin == null) {
            return;
        }

        this.analyticsPlugin.buy(item, num, price);
    }


    public void use(String item, int num, double price) {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.use(item, num, price);
    }


    public void bonus(String item, int num, double price, int trigger) {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.bonus(item, num, price, trigger);
    }


    public void login(String userID) {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.login(userID);
    }


    public void logout() {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.logout();
    }


    public void levelup(int level) {
        if (this.analyticsPlugin == null) {
            return;
        }
        this.analyticsPlugin.levelup(level);
    }
}


