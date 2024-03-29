package com.u8.sdk.impl;

import android.widget.Toast;

import com.u8.sdk.IUser;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;


public class SimpleDefaultUser
        implements IUser {
    public boolean isSupportMethod(String methodName) {
        return true;
    }


    public void login() {
        tip("调用[登录]接口成功，这个默认的实现，还需要经过打包工具来打出最终的渠道包");
    }


    public void loginCustom(String customData) {
    }


    public void switchLogin() {
        tip("调用[切换帐号]接口成功，还需要经过打包工具来打出最终的渠道包");
    }


    public boolean showAccountCenter() {
        tip("调用[个人中心]接口成功，还需要经过打包工具来打出最终的渠道包");

        return true;
    }


    public void logout() {
        tip("调用[登出接口]接口成功，还需要经过打包工具来打出最终的渠道包");
    }


    public void submitExtraData(UserExtraData extraData) {
        tip("调用[提交扩展数据]接口成功，还需要经过打包工具来打出最终的渠道包");
    }


    public void exit() {
        tip("调用[退出游戏确认]接口成功，还需要经过打包工具来打出最终的渠道包");
    }


    public void realNameRegister() {
        tip("游戏中暂时不需要调用该接口");
    }


    public void queryAntiAddiction() {
        tip("游戏中暂时不需要调用该接口");
    }


    private void tip(String msg) {
        Toast.makeText(U8SDK.getInstance().getContext(), msg, 1).show();
    }


    public void postGiftCode(String code) {
        tip("调用[上传礼包兑换码]接口成功，还需要经过打包工具来打出最终的渠道包");
    }


    public void queryProducts() {
        tip("调用[商品查询]接口成功，还需要经过打包工具来打出最终的渠道包");
    }
}


