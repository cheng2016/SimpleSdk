package com.u8.sdk;

public interface IUser extends IPlugin {
    public static final int PLUGIN_TYPE = 1;

    void login();

    void loginCustom(String paramString);

    void switchLogin();

    boolean showAccountCenter();

    void logout();

    void submitExtraData(UserExtraData paramUserExtraData);

    void exit();

    void postGiftCode(String paramString);

    void realNameRegister();

    void queryAntiAddiction();

    void queryProducts();
}


