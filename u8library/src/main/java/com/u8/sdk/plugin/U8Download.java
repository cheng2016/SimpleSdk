package com.u8.sdk.plugin;

import android.util.Log;

import com.u8.sdk.IDownload;
import com.u8.sdk.base.PluginFactory;


public class U8Download {
    private static U8Download instance;
    private IDownload downloadPlugin;

    public static U8Download getInstance() {
        if (instance == null) {
            instance = new U8Download();
        }
        return instance;
    }


    public void init() {
        this.downloadPlugin = (IDownload) PluginFactory.getInstance().initPlugin(6);
    }


    public boolean isSupport(String method) {
        if (isPluginInited()) {
            return this.downloadPlugin.isSupportMethod(method);
        }
        return false;
    }


    public void download(String url, boolean showConfirm, boolean force) {
        if (isPluginInited()) {
            this.downloadPlugin.download(url, showConfirm, force);
        }
    }

    private boolean isPluginInited() {
        if (this.downloadPlugin == null) {
            Log.e("U8SDK", "The download plugin is not inited or inited failed.");
            return false;
        }
        return true;
    }
}


