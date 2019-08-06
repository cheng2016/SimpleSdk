package com.u8.sdk.plugin;

import android.util.Log;

import com.u8.sdk.IShare;
import com.u8.sdk.ShareParams;
import com.u8.sdk.base.PluginFactory;


public class U8Share {
    private static U8Share instance;
    private IShare sharePlugin;

    public static U8Share getInstance() {
        if (instance == null) {
            instance = new U8Share();
        }

        return instance;
    }


    public void init() {
        this.sharePlugin = (IShare) PluginFactory.getInstance().initPlugin(4);
    }


    public boolean isSupport(String method) {
        if (isPluginInited()) {
            return this.sharePlugin.isSupportMethod(method);
        }
        return false;
    }


    public void share(ShareParams params) {
        if (isPluginInited()) {
            this.sharePlugin.share(params);
        }
    }

    private boolean isPluginInited() {
        if (this.sharePlugin == null) {
            Log.e("U8SDK", "The share plugin is not inited or inited failed.");
            return false;
        }
        return true;
    }
}


