package com.u8.sdk.impl.services;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.u8.sdk.PayParams;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.impl.listeners.ISDKListener;
import com.u8.sdk.impl.listeners.ISDKLoginListener;
import com.u8.sdk.impl.listeners.ISDKPayListener;
import com.u8.sdk.impl.listeners.ISDKRegisterOnekeyListener;
import com.u8.sdk.utils.EncryptUtils;
import com.u8.sdk.utils.GUtils;
import com.u8.sdk.utils.ResourceHelper;
import com.u8.sdk.utils.U8HttpUtils;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONObject;

public class SdkManager {
  private static SdkManager instance;
  
  private String lastUserID;
  
  public static SdkManager getInstance() {
    if (instance == null)
      instance = new SdkManager(); 
    return instance;
  }
  
  public void init(Context paramContext, ISDKListener paramISDKListener) { paramISDKListener.onSuccess(); }
  
  public void login(String paramString1, String paramString2, final ISDKLoginListener listener) {
    Log.d("U8SDK", "sdk login called. username:" + paramString1 + ";password:" + paramString2);
    String str = System.currentTimeMillis() + "";
    final HashMap hashMap = new HashMap();
    hashMap.put("name", paramString1);
    hashMap.put("password", paramString2);
    hashMap.put("gameAppId", U8SDK.getInstance().getAppID() + "");
    hashMap.put("time", str);
    hashMap.put("sign", EncryptUtils.md5Sign(hashMap, U8SDK.getInstance().getAppKey()));
    GUtils.runInThread(new Runnable() {
          public void run() {
            str = U8HttpUtils.httpPost(url, hashMap);
            Log.d("U8SDK", "default login result:" + str);
            try {
              JSONObject jSONObject = new JSONObject(str);
              if (jSONObject.getInt("state") == 1) {
                JSONObject jSONObject1 = jSONObject.getJSONObject("data");
                String str1 = jSONObject1.optString("userId");
                String str2 = jSONObject1.optString("username");
                SdkManager.access$002(SdkManager.this, str1);
                listener.onSuccess(str1, str2);
                return;
              } 
              listener.onFailed(0);
              return;
            } catch (Exception str) {
              listener.onFailed(0);
              str.printStackTrace();
              return;
            } 
          }
        });
  }
  
  public void pay(Activity paramActivity, PayParams paramPayParams, final ISDKPayListener listener) {
    if (TextUtils.isEmpty(this.lastUserID)) {
      Log.d("U8SDK", "sdk now not logined. please login first.");
      ResourceHelper.showTip(paramActivity, "R.string.x_pay_no_login");
      return;
    } 
    final LinkedHashMap linkedHashMap = new LinkedHashMap();
    linkedHashMap.put("uid", this.lastUserID);
    linkedHashMap.put("price", paramPayParams.getPrice() + "");
    linkedHashMap.put("gameAppId", U8SDK.getInstance().getAppID() + "");
    linkedHashMap.put("productID", paramPayParams.getProductId());
    linkedHashMap.put("productName", paramPayParams.getProductName());
    linkedHashMap.put("orderID", paramPayParams.getOrderID());
    linkedHashMap.put("time", System.currentTimeMillis() + "");
    linkedHashMap.put("payNotifyUrl", paramPayParams.getExtension());
    linkedHashMap.put("sign", EncryptUtils.md5Sign(linkedHashMap, U8SDK.getInstance().getAppKey()));
    GUtils.runInThread(new Runnable() {
          public void run() {
            str = U8HttpUtils.httpPost(url, linkedHashMap);
            Log.d("U8SDK", "default pay result:" + str);
            try {
              if ((new JSONObject(str)).getInt("state") == 1) {
                listener.onSuccess("success");
                return;
              } 
              listener.onFailed(0);
              return;
            } catch (Exception str) {
              listener.onFailed(0);
              str.printStackTrace();
              return;
            } 
          }
        });
  }
  
  public void register(String paramString1, String paramString2, final ISDKLoginListener listener) {
    String str = System.currentTimeMillis() + "";
    final HashMap hashMap = new HashMap();
    hashMap.put("name", paramString1);
    hashMap.put("password", paramString2);
    hashMap.put("gameAppId", U8SDK.getInstance().getAppID() + "");
    hashMap.put("time", str);
    hashMap.put("sign", EncryptUtils.md5Sign(hashMap, U8SDK.getInstance().getAppKey()));
    GUtils.runInThread(new Runnable() {
          public void run() {
            str = U8HttpUtils.httpPost(url, hashMap);
            Log.d("U8SDK", "default register result:" + str);
            try {
              JSONObject jSONObject = new JSONObject(str);
              if (jSONObject.getInt("state") == 1) {
                JSONObject jSONObject1 = jSONObject.getJSONObject("data");
                String str1 = jSONObject1.optString("userId");
                String str2 = jSONObject1.optString("username");
                SdkManager.access$002(SdkManager.this, str1);
                listener.onSuccess(str1, str2);
                return;
              } 
              listener.onFailed(0);
              return;
            } catch (Exception str) {
              listener.onFailed(0);
              str.printStackTrace();
              return;
            } 
          }
        });
  }
  
  public void registerOnekey(String paramString, final ISDKRegisterOnekeyListener listener) {
    paramString = System.currentTimeMillis() + "";
    final HashMap hashMap = new HashMap();
    hashMap.put("gameAppId", U8SDK.getInstance().getAppID() + "");
    hashMap.put("time", paramString);
    hashMap.put("sign", EncryptUtils.md5Sign(hashMap, U8SDK.getInstance().getAppKey()));
    GUtils.runInThread(new Runnable() {
          public void run() {
            str = U8HttpUtils.httpPost(url, hashMap);
            Log.d("U8SDK", "default fast register result:" + str);
            try {
              JSONObject jSONObject = new JSONObject(str);
              if (jSONObject.getInt("state") == 1) {
                JSONObject jSONObject1 = jSONObject.getJSONObject("data");
                String str1 = jSONObject1.optString("userId");
                String str2 = jSONObject1.optString("username");
                String str3 = jSONObject1.optString("password");
                SdkManager.access$002(SdkManager.this, str1);
                listener.onSuccess(str1, str2, str3);
                return;
              } 
              listener.onFailed(0);
              return;
            } catch (Exception str) {
              listener.onFailed(0);
              str.printStackTrace();
              return;
            } 
          }
        });
  }
  
  public void submitGameData(Activity paramActivity, UserExtraData paramUserExtraData, ISDKListener paramISDKListener) {
    Log.d("U8SDK", "submitGameData called. the data is:");
    Log.d("U8SDK", paramUserExtraData.toString());
    paramISDKListener.onSuccess();
  }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\services\SdkManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */