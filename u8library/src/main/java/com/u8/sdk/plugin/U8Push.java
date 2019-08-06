package com.u8.sdk.plugin;

import android.util.Log;

import com.u8.sdk.IPush;
import com.u8.sdk.base.PluginFactory;


public class U8Push {
    private static U8Push instance;
    private IPush pushPlugin;

    public void init() {
        this.pushPlugin = (IPush) PluginFactory.getInstance().initPlugin(3);
    }


    public static U8Push getInstance() {
        if (instance == null) {
            instance = new U8Push();
        }

        return instance;
    }

    public boolean isSupport(String method) {
        if (isPluginInited()) {
            return this.pushPlugin.isSupportMethod(method);
        }

        return false;
    }


    public void scheduleNotification(String msgs) {
        if (isPluginInited()) {
            this.pushPlugin.scheduleNotification(msgs);
        }
    }


    public void startPush() {
        if (isPluginInited()) {
            this.pushPlugin.startPush();
        }
    }


    public void stopPush() {
        if (isPluginInited()) {
            this.pushPlugin.stopPush();
        }
    }


    public void addTags(String... tags) {
        if (isPluginInited()) {
            this.pushPlugin.addTags(tags);
        }
    }


    public void removeTags(String... tags) {
        if (isPluginInited()) {
            this.pushPlugin.removeTags(tags);
        }
    }


    public void addAlias(String alias) {
        if (isPluginInited()) {
            this.pushPlugin.addAlias(alias);
        }
    }


    public void removeAlias(String alias) {
        if (isPluginInited()) {
            this.pushPlugin.removeAlias(alias);
        }
    }

    private boolean isPluginInited() {
        if (this.pushPlugin == null) {
            Log.e("U8SDK", "The push plugin is not inited or inited failed.");
            return false;
        }
        return true;
    }
}


