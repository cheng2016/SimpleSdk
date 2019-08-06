package com.u8.sdk;

public interface IAnalytics extends IPlugin {
    public static final int PLUGIN_TYPE = 5;

    void startLevel(String paramString);

    void failLevel(String paramString);

    void finishLevel(String paramString);

    void startTask(String paramString1, String paramString2);

    void failTask(String paramString);

    void finishTask(String paramString);

    void payRequest(String paramString1, String paramString2, double paramDouble, String paramString3);

    void pay(String paramString, double paramDouble, int paramInt);

    void buy(String paramString, int paramInt, double paramDouble);

    void use(String paramString, int paramInt, double paramDouble);

    void bonus(String paramString, int paramInt1, double paramDouble, int paramInt2);

    void login(String paramString);

    void logout();

    void levelup(int paramInt);
}


