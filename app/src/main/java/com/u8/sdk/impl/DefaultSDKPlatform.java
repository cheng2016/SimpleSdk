package com.u8.sdk.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
import com.u8.sdk.PayParams;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.impl.listeners.ISDKExitListener;
import com.u8.sdk.impl.listeners.ISDKListener;
import com.u8.sdk.impl.listeners.ISDKLoginListener;
import com.u8.sdk.impl.listeners.ISDKPayListener;
import com.u8.sdk.impl.services.SdkManager;

public class DefaultSDKPlatform {
  private static DefaultSDKPlatform instance;
  
  private PayParams lastPayData;
  
  private ISDKLoginListener loginListener;
  
  private ISDKPayListener payListener;
  
  public static DefaultSDKPlatform getInstance() {
    if (instance == null)
      instance = new DefaultSDKPlatform(); 
    return instance;
  }
  
  public void exitSDK(Activity paramActivity, final ISDKExitListener listener) {
    AlertDialog.Builder builder = new AlertDialog.Builder(paramActivity);
    builder.setTitle("退出确认");
    builder.setMessage("亲，现在还早，要不要再玩一会？");
    builder.setCancelable(true);
    builder.setPositiveButton("好吧", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) { listener.onCancel(); }
        });
    builder.setNeutralButton("一会再玩", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) { listener.onExit(); }
        });
    builder.show();
  }
  
  public PayParams getLastPayData() { return this.lastPayData; }
  
  public void init(Context paramContext, ISDKListener paramISDKListener) { SdkManager.getInstance().init(paramContext, paramISDKListener); }
  
  public void login(Activity paramActivity, ISDKLoginListener paramISDKLoginListener) {
    this.loginListener = paramISDKLoginListener;
    paramActivity.startActivity(new Intent(paramActivity, com.u8.sdk.impl.activities.LoginActivity.class));
  }
  
  public void loginFailCallback() {
    if (this.loginListener != null)
      this.loginListener.onFailed(0); 
    this.loginListener = null;
  }
  
  public void loginSucCallback(String paramString1, String paramString2) {
    if (this.loginListener != null)
      this.loginListener.onSuccess(paramString1, paramString2); 
    this.loginListener = null;
  }
  
  public void pay(Activity paramActivity, PayParams paramPayParams, ISDKPayListener paramISDKPayListener) {
    this.payListener = paramISDKPayListener;
    this.lastPayData = paramPayParams;
    Toast.makeText(U8SDK.getInstance().getContext(), paramPayParams.toString(), 1).show();
    paramActivity.startActivity(new Intent(paramActivity, com.u8.sdk.impl.activities.PayActivity.class));
  }
  
  public void payFailCallback() {
    if (this.payListener != null)
      this.payListener.onFailed(0); 
    this.payListener = null;
    this.lastPayData = null;
  }
  
  public void paySucCallback() {
    if (this.payListener != null)
      this.payListener.onSuccess(""); 
    this.payListener = null;
    this.lastPayData = null;
  }
  
  public void submitGameData(Activity paramActivity, final UserExtraData data) { SdkManager.getInstance().submitGameData(paramActivity, paramUserExtraData, new ISDKListener() {
          public void onFailed(int param1Int) {}
          
          public void onSuccess() { Toast.makeText(U8SDK.getInstance().getContext(), data.toString(), 1).show(); }
        }); }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\DefaultSDKPlatform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */