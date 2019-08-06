package com.u8.sdk.impl;

import android.widget.Toast;
import com.u8.sdk.IUser;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;

public class SimpleDefaultUser implements IUser {
  private void tip(String paramString) { Toast.makeText(U8SDK.getInstance().getContext(), paramString, 1).show(); }
  
  public void exit() { tip("调用[退出游戏确认]接口成功，还需要经过打包工具来打出最终的渠道包"); }
  
  public boolean isSupportMethod(String paramString) { return true; }
  
  public void login() { tip("调用[登录]接口成功，这个默认的实现，还需要经过打包工具来打出最终的渠道包"); }
  
  public void loginCustom(String paramString) {}
  
  public void logout() { tip("调用[登出接口]接口成功，还需要经过打包工具来打出最终的渠道包"); }
  
  public void postGiftCode(String paramString) { tip("调用[上传礼包兑换码]接口成功，还需要经过打包工具来打出最终的渠道包"); }
  
  public void queryAntiAddiction() { tip("游戏中暂时不需要调用该接口"); }
  
  public void queryProducts() { tip("调用[商品查询]接口成功，还需要经过打包工具来打出最终的渠道包"); }
  
  public void realNameRegister() { tip("游戏中暂时不需要调用该接口"); }
  
  public boolean showAccountCenter() {
    tip("调用[个人中心]接口成功，还需要经过打包工具来打出最终的渠道包");
    return true;
  }
  
  public void submitExtraData(UserExtraData paramUserExtraData) { tip("调用[提交扩展数据]接口成功，还需要经过打包工具来打出最终的渠道包"); }
  
  public void switchLogin() { tip("调用[切换帐号]接口成功，还需要经过打包工具来打出最终的渠道包"); }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\SimpleDefaultUser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */