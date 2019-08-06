package com.u8.sdk.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

public interface IActivityCallback {
    void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent);

    void onCreate();

    void onStart();

    void onPause();

    void onResume();

    void onNewIntent(Intent paramIntent);

    void onStop();

    void onDestroy();

    void onRestart();

    void onBackPressed();

    void onConfigurationChanged(Configuration paramConfiguration);

    void attachBaseContext(Context paramContext);

    void onRequestPermissionResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt);
}


