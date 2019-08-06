package com.u8.sdk;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Process;

import com.u8.sdk.log.Log;


public class U8Application
        extends Application {
    public void onCreate() {
        super.onCreate();

        U8SDK.getInstance().onAppCreateAll(this);

        if (isMainProcess(this)) {
            U8SDK.getInstance().onAppCreate(this);
        } else {
            Log.d("U8SDK", "application oncreate called in sub process. do not init again...");
        }
    }


    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (isMainProcess(base)) {
            U8SDK.getInstance().onAppAttachBaseContext(this, base);
        }
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isMainProcess(this)) {
            U8SDK.getInstance().onAppConfigurationChanged(this, newConfig);
        }
    }


    @Override
    public void onTerminate() {
        if (isMainProcess(this)) {
            U8SDK.getInstance().onTerminate();
        }
    }


    private boolean isMainProcess(Context context) {
        try {
            return context.getPackageName().equals(getCurrentProcessName(context));
        } catch (Exception e) {
            e.printStackTrace();


            return true;
        }
    }

    private String getCurrentProcessName(Context context) {
        int pid = Process.myPid();
        String processName = "";
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
            if (process.pid == pid) {
                processName = process.processName;
            }
        }
        return processName;
    }
}


