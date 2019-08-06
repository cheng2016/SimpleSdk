package com.u8.sdk;

public interface IPush extends IPlugin {
    public static final int PLUGIN_TYPE = 3;

    void scheduleNotification(String paramString);

    void startPush();

    void stopPush();

    void addTags(String... paramVarArgs);

    void removeTags(String... paramVarArgs);

    void addAlias(String paramString);

    void removeAlias(String paramString);
}


