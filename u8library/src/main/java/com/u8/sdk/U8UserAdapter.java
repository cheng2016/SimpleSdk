package com.u8.sdk;


public abstract class U8UserAdapter
        implements IUser {
    public void login() {
    }

    public void loginCustom(String customData) {
    }

    public void switchLogin() {
    }

    public boolean showAccountCenter() {
        return false;
    }

    public void logout() {
    }

    public void submitExtraData(UserExtraData extraData) {
    }

    public void exit() {
    }

    public void postGiftCode(String code) {
    }

    public void realNameRegister() {
    }

    public void queryAntiAddiction() {
    }

    public abstract boolean isSupportMethod(String paramString);

    public void queryProducts() {
    }
}


