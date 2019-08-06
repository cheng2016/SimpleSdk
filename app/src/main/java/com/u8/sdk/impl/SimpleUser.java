package com.u8.sdk.impl;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
import com.u8.sdk.IUser;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.impl.listeners.ISDKExitListener;
import com.u8.sdk.impl.listeners.ISDKListener;
import com.u8.sdk.impl.listeners.ISDKLoginListener;
import org.json.JSONObject;

public class SimpleUser implements IUser {
  public SimpleUser(Activity paramActivity) { DefaultSDKPlatform.getInstance().init(U8SDK.getInstance().getContext(), new ISDKListener() {
          public void onFailed(int param1Int) {
            Log.e("U8SDK", "default sdk inti failed.");
            U8SDK.getInstance().onResult(2, "init failed");
          }
          
          public void onSuccess() {
            Log.d("U8SDK", "default sdk init success");
            U8SDK.getInstance().onResult(1, "init success");
          }
        }); }
  
  private void tip(String paramString) { Toast.makeText(U8SDK.getInstance().getContext(), paramString, 1).show(); }
  
  public void exit() { DefaultSDKPlatform.getInstance().exitSDK(U8SDK.getInstance().getContext(), new ISDKExitListener() {
          public void onCancel() {}
          
          public void onExit() {
            U8SDK.getInstance().getContext().finish();
            System.exit(0);
          }
        }); }
  
  public boolean isSupportMethod(String paramString) { return true; }
  
  public void login() { DefaultSDKPlatform.getInstance().login(U8SDK.getInstance().getContext(), new ISDKLoginListener() {
          public void onFailed(int param1Int) {}
          
          public void onSuccess(String param1String1, String param1String2) {
            try {
              JSONObject jSONObject = new JSONObject();
              jSONObject.put("userId", param1String1);
              jSONObject.put("username", param1String2);
              U8SDK.getInstance().onLoginResult(jSONObject.toString());
              return;
            } catch (Exception e) {
              U8SDK.getInstance().onResult(5, param1String1.getMessage());
              e.printStackTrace();
              return;
            } 
          }
        }); }
  
  public void loginCustom(String paramString) { login(); }
  
  public void logout() { tip("调用登出接口成功，测试界面无登出功能"); }
  
  public void postGiftCode(String paramString) {}
  
  public void queryAntiAddiction() { tip("游戏中暂时不需要调用该接口"); }
  
  public void queryProducts() {}
  
  public void realNameRegister() { tip("游戏中暂时不需要调用该接口"); }
  
  public boolean showAccountCenter() {
    tip("调用[个人中心]接口成功，测试界面没有个人中心帐号界面");
    return true;
  }
  
  public void submitExtraData(UserExtraData paramUserExtraData) {
    tip("调用[提交扩展数据]接口成功，详细数据可以看logcat日志");
    DefaultSDKPlatform.getInstance().submitGameData(U8SDK.getInstance().getContext(), paramUserExtraData);
  }
  
  public void switchLogin() { login(); }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\SimpleUser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */