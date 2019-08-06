package com.u8.sdk.impl;

import android.app.Activity;
import com.u8.sdk.IPay;
import com.u8.sdk.PayParams;
import com.u8.sdk.U8SDK;
import com.u8.sdk.impl.listeners.ISDKPayListener;

public class SimplePay implements IPay {
  public SimplePay(Activity paramActivity) {}
  
  public boolean isSupportMethod(String paramString) { return true; }
  
  public void pay(PayParams paramPayParams) { DefaultSDKPlatform.getInstance().pay(U8SDK.getInstance().getContext(), paramPayParams, new ISDKPayListener() {
          public void onFailed(int param1Int) { U8SDK.getInstance().onResult(11, "pay failed."); }
          
          public void onSuccess(String param1String) { U8SDK.getInstance().onResult(10, "pay success"); }
        }); }
}


