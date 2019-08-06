package com.u8.sdk;

import android.content.Context;
import android.content.res.Configuration;

public interface IApplicationListener {
    void onProxyCreate();

    void onProxyAttachBaseContext(Context paramContext);

    void onProxyConfigurationChanged(Configuration paramConfiguration);

    void onProxyTerminate();
}


