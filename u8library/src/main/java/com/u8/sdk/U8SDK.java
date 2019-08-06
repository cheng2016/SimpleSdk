package com.u8.sdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
//import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.u8.sdk.analytics.UDAgent;
import com.u8.sdk.base.IActivityCallback;
import com.u8.sdk.base.IU8SDKListener;
import com.u8.sdk.base.PluginFactory;
import com.u8.sdk.log.Log;
import com.u8.sdk.plugin.U8Analytics;
import com.u8.sdk.plugin.U8Download;
import com.u8.sdk.plugin.U8Pay;
import com.u8.sdk.plugin.U8Push;
import com.u8.sdk.plugin.U8Share;
import com.u8.sdk.plugin.U8User;
import com.u8.sdk.verify.U8Proxy;
import com.u8.sdk.verify.UToken;

import java.util.ArrayList;
import java.util.List;


public class U8SDK {
    private String sdkUserID = null;
    private UToken tokenData = null;


    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private List<IU8SDKListener> listeners = new ArrayList();
    private List<IActivityCallback> activityCallbacks = new ArrayList();
    private List<IApplicationListener> applicationListeners = new ArrayList(2);
    private static final String DEFAULT_PKG_NAME = "com.u8.sdk";
    private static final String APP_PROXY_NAME = "U8_APPLICATION_PROXY_NAME";
    private static final String APP_GAME_NAME = "U8_Game_Application";
    private static U8SDK instance;

    public static U8SDK getInstance() {
        if (instance == null) {
            instance = new U8SDK();
        }
        return instance;
    }

    private Application application;
    private Activity context;
    private SDKParams developInfo;
    private Bundle metaData;

    public SDKParams getSDKParams() {
        return this.developInfo;
    }


    public Bundle getMetaData() {
        return this.metaData;
    }


    public int getSubChannel() {
        if (this.developInfo == null || !this.developInfo.contains("U8_Sub_Channel")) {
            return 0;
        }
        return this.developInfo.getInt("U8_Sub_Channel");
    }


    public int getCurrChannel() {
        if (this.developInfo == null || !this.developInfo.contains("U8_Channel")) {
            return 0;
        }
        return this.developInfo.getInt("U8_Channel");
    }


    public int getAppID() {
        if (this.developInfo == null || !this.developInfo.contains("U8_APPID")) {
            return 0;
        }

        return this.developInfo.getInt("U8_APPID");
    }

    public String getAppKey() {
        if (this.developInfo == null || !this.developInfo.contains("U8_APPKEY")) {
            return "";
        }

        return this.developInfo.getString("U8_APPKEY");
    }

    public String getPayPrivateKey() {
        if (this.developInfo == null || !this.developInfo.contains("U8_PAY_PRIVATEKEY")) {
            return "";
        }

        return this.developInfo.getString("U8_PAY_PRIVATEKEY");
    }


    public boolean isSingleGame() {
        if (this.developInfo == null || !this.developInfo.contains("U8_SINGLE_GAME")) {
            return false;
        }

        return this.developInfo.getBoolean("U8_SINGLE_GAME").booleanValue();
    }


    public boolean isAuth() {
        return (getAuthURL() != null);
    }


    public boolean isGetOrder() {
        return (getOrderURL() != null);
    }


    public String getOrderURL() {
        if (this.developInfo == null) {
            return null;
        }

        if (this.developInfo.contains("U8_ORDER_URL")) {
            return this.developInfo.getString("U8_ORDER_URL");
        }

        String baseUrl = getU8ServerURL();
        if (baseUrl == null) {
            return null;
        }

        return baseUrl + "/pay/getOrderID";
    }

    public String getAuthURL() {
        if (this.developInfo == null) {
            return null;
        }

        if (this.developInfo.contains("U8_AUTH_URL")) {
            return this.developInfo.getString("U8_AUTH_URL");
        }

        String baseUrl = getU8ServerURL();
        if (baseUrl == null) {
            return null;
        }

        return baseUrl + "/user/getToken";
    }


    public String getAnalyticsURL() {
        if (this.developInfo == null) {
            return null;
        }

        if (this.developInfo.contains("U8_ANALYTICS_URL")) {
            return this.developInfo.getString("U8_ANALYTICS_URL");
        }

        String baseUrl = getU8ServerURL();
        if (baseUrl == null) {
            return null;
        }

        return baseUrl + "/user";
    }


    public String getPayCompleteURL() {
        if (this.developInfo == null) {
            return null;
        }


        String baseUrl = getU8ServerURL();
        if (baseUrl == null) {
            return null;
        }

        return baseUrl + "/pay/complete";
    }


    public String getPayCheckURL() {
        if (this.developInfo == null) {
            return null;
        }


        String baseUrl = getU8ServerURL();
        if (baseUrl == null) {
            return null;
        }

        return baseUrl + "/pay/check";
    }


    public String getU8ServerURL() {
        if (this.developInfo == null || !this.developInfo.contains("U8SERVER_URL")) {
            return null;
        }

        String url = this.developInfo.getString("U8SERVER_URL");
        if (url == null || url.trim().length() == 0) {
            return null;
        }

        while (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }


    public boolean isUseU8Analytics() {
        if (this.developInfo == null || !this.developInfo.contains("U8_ANALYTICS")) {
            return false;
        }
        String use = this.developInfo.getString("U8_ANALYTICS");
        return "true".equalsIgnoreCase(use);
    }


    public boolean isSDKShowSplash() {
        if (this.developInfo == null || !this.developInfo.contains("U8_SDK_SHOW_SPLASH")) {
            return false;
        }

        String show = this.developInfo.getString("U8_SDK_SHOW_SPLASH");
        return "true".equalsIgnoreCase(show);
    }


    public String getSDKVersionCode() {
        if (this.developInfo == null || !this.developInfo.contains("U8_SDK_VERSION_CODE")) {
            return "";
        }

        return this.developInfo.getString("U8_SDK_VERSION_CODE");
    }


    public String getSDKVersionName() {
        if (this.developInfo == null || !this.developInfo.contains("U8_SDK_VERSION_NAME")) {
            return "";
        }

        return this.developInfo.getString("U8_SDK_VERSION_NAME");
    }

    public void setSDKListener(IU8SDKListener listener) {
        if (!this.listeners.contains(listener) && listener != null) {
            this.listeners.add(listener);
        }
    }


    public void setActivityCallback(IActivityCallback callback) {
        if (!this.activityCallbacks.contains(callback) && callback != null) {
            this.activityCallbacks.add(callback);
        }
    }


    public Application getApplication() {
        return this.application;
    }


    public String getSDKUserID() {
        return this.sdkUserID;
    }


    public UToken getUToken() {
        return this.tokenData;
    }


    public void onAppCreate(Application application) {
        this.application = application;
        for (IApplicationListener lis : this.applicationListeners) {
            lis.onProxyCreate();
        }
    }


    public void onAppCreateAll(Application application) {
        for (IApplicationListener lis : this.applicationListeners) {
            if (lis instanceof IFullApplicationListener) {
                ((IFullApplicationListener) lis).onProxyCreateAll();
            }
        }
    }


    public void onAppAttachBaseContext(Application application, Context context) {
        this.application = application;

        try {
//            MultiDex.install(context);
            Log.init(context);
        } catch (Exception e) {
            e.printStackTrace();
        }


        this.applicationListeners.clear();


        try {
            PluginFactory.getInstance().loadPluginInfo(context);
            this.developInfo = PluginFactory.getInstance().getSDKParams(context);
            this.metaData = PluginFactory.getInstance().getMetaData(context);

            if (this.metaData.containsKey("U8_APPLICATION_PROXY_NAME")) {
                String proxyAppNames = this.metaData.getString("U8_APPLICATION_PROXY_NAME");
                String[] proxyApps = proxyAppNames.split(",");
                for (String proxy : proxyApps) {
                    if (!TextUtils.isEmpty(proxy)) {
                        Log.d("U8SDK", "add a new application listener:" + proxy);
                        IApplicationListener listener = newApplicationInstance(application, proxy);
                        if (listener != null) {
                            this.applicationListeners.add(listener);
                        }
                    }
                }
            }

            if (this.metaData.containsKey("U8_Game_Application")) {
                String gameAppName = this.metaData.getString("U8_Game_Application");
                IApplicationListener listener = newApplicationInstance(application, gameAppName);
                if (listener != null) {
                    Log.e("U8SDK", "add a game application listener:" + gameAppName);
                    this.applicationListeners.add(listener);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (IApplicationListener lis : this.applicationListeners) {
            lis.onProxyAttachBaseContext(context);
        }
    }


    public void onAppConfigurationChanged(Application application, Configuration newConfig) {
        for (IApplicationListener lis : this.applicationListeners) {
            lis.onProxyConfigurationChanged(newConfig);
        }
    }

    public void onTerminate() {
        for (IApplicationListener lis : this.applicationListeners) {
            lis.onProxyTerminate();
        }
        Log.destory();
    }


    private IApplicationListener newApplicationInstance(Application application, String proxyAppName) {
        if (proxyAppName == null || SDKTools.isNullOrEmpty(proxyAppName)) {
            return null;
        }

        if (proxyAppName.startsWith(".")) {
            proxyAppName = "com.u8.sdk" + proxyAppName;
        }

        try {
            Class clazz = Class.forName(proxyAppName);
            return (IApplicationListener) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {

            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return null;
    }


    public void init(Activity context) {
        this.context = context;
        try {
            if (isUseU8Analytics()) {
                UDAgent.getInstance().init(context);
            }

            U8Push.getInstance().init();
            U8User.getInstance().init();
            U8Pay.getInstance().init();
            U8Share.getInstance().init();
            U8Analytics.getInstance().init();
            U8Download.getInstance().init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void runOnMainThread(Runnable runnable) {
        if (this.mainThreadHandler != null) {
            this.mainThreadHandler.post(runnable);

            return;
        }
        if (this.context != null) {
            this.context.runOnUiThread(runnable);
        }
    }


    public void setContext(Activity context) {
        this.context = context;
    }


    public Activity getContext() {
        return this.context;
    }


    public List<IU8SDKListener> getAllListeners() {
        return this.listeners;
    }


    public void onResult(final int code, final String msg) {
        Log.d("U8SDK", "onResult in U8SDK. code:" + code + ";msg:" + msg);

        if (getInstance().isSingleGame()) {
            runOnMainThread(new Runnable() {
                public void run() {
                    U8SDKSingle.getInstance().handleResult(code, msg);
                }
            });
        }


        for (IU8SDKListener listener : this.listeners) {
            listener.onResult(code, msg);
        }
    }


    public void onLoginResult(String result) {
        for (IU8SDKListener listener : this.listeners) {
            listener.onLoginResult(result);
        }

        if (isAuth()) {
            startAuthTask(result);
        }
    }


    public void onSwitchAccount() {
        for (IU8SDKListener listener : this.listeners) {
            listener.onSwitchAccount();
        }
    }

    public void onSwitchAccount(String result) {
        for (IU8SDKListener listener : this.listeners) {
            listener.onSwitchAccount(result);
        }

        if (isAuth()) {
            startAuthTask(result);
        }
    }

    public void onLogout() {
        for (IU8SDKListener listener : this.listeners) {
            listener.onLogout();
        }
    }


    private void onAuthResult(UToken token) {
        if (token.isSuc()) {
            this.sdkUserID = token.getSdkUserID();
            this.tokenData = token;
        }

        for (IU8SDKListener listener : this.listeners) {
            listener.onAuthResult(token);
        }
    }


    public void onProductQueryResult(List<ProductQueryResult> result) {
        for (IU8SDKListener listener : this.listeners) {
            listener.onProductQueryResult(result);
        }
    }


    public void onPayResult(PayParams order) {
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    public void onBackPressed() {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onBackPressed();
            }
        }
    }


    public void onCreate() {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onCreate();
            }
        }
    }


    public void onStart() {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onStart();
            }
        }
    }

    public void onPause() {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onPause();
            }
        }
    }


    public void onResume() {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onResume();
            }
        }
    }


    public void onNewIntent(Intent newIntent) {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onNewIntent(newIntent);
            }
        }
    }


    public void onStop() {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onStop();
            }
        }
    }


    public void onDestroy() {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onDestroy();
            }
        }


        if (getInstance().isSingleGame()) {
            U8SDKSingle.getInstance().stopAutoTask();
        }
    }


    public void onRestart() {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onRestart();
            }
        }
    }


    public void attachBaseContext(Activity activity, Context newBase) {
        this.context = activity;
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.attachBaseContext(newBase);
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onConfigurationChanged(newConfig);
            }
        }
    }

    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (this.activityCallbacks != null) {
            for (IActivityCallback callback : this.activityCallbacks) {
                callback.onRequestPermissionResult(requestCode, permissions, grantResults);
            }
        }
    }


    @SuppressLint({"NewApi"})
    private void startAuthTask(String result) {
        AuthTask authTask = new AuthTask();
        if (Build.VERSION.SDK_INT >= 11) {

            authTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{result});
        } else {

            authTask.execute(new String[]{result});
        }
    }


    @SuppressLint({"NewApi"})
    class AuthTask
            extends AsyncTask<String, Void, UToken> {
        protected UToken doInBackground(String... args) {
            String result = args[0];
            Log.d("U8SDK", "begin to auth...");
            return U8Proxy.auth(result);
        }


        protected void onPostExecute(UToken token) {
            U8SDK.this.onAuthResult(token);
        }
    }
}


