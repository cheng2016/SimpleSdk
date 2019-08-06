package com.u8.sdk.log;

import android.util.Log;


public class ULocalLog
        implements ILog {
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }


    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }


    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }


    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }


    public void w(String tag, String msg, Throwable e) {
        Log.w(tag, msg, e);
    }


    public void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
    }

    public void destory() {
    }
}


