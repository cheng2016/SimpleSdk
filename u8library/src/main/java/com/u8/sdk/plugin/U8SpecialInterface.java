package com.u8.sdk.plugin;

import android.app.Activity;

import com.u8.sdk.ISpecialInterface;


public class U8SpecialInterface
        implements ISpecialInterface {
    private static U8SpecialInterface instance;
    private ISpecialInterface plugin;

    public static U8SpecialInterface getInstance() {
        if (instance == null) {
            instance = new U8SpecialInterface();
        }
        return instance;
    }


    public void setSpecialInterface(ISpecialInterface plugin) {
        this.plugin = plugin;
    }


    public boolean isFromGameCenter(Activity context) {
        if (this.plugin != null) {
            return this.plugin.isFromGameCenter(context);
        }
        return false;
    }


    public void showGameCenter(Activity context) {
        if (this.plugin != null) {
            this.plugin.showGameCenter(context);
        }
    }


    public void performFeature(Activity context, String type) {
        if (this.plugin != null)
            this.plugin.performFeature(context, type);
    }
}


