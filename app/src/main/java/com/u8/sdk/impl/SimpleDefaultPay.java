package com.u8.sdk.impl;

import android.widget.Toast;
import com.u8.sdk.IPay;
import com.u8.sdk.PayParams;
import com.u8.sdk.U8SDK;

public class SimpleDefaultPay implements IPay {
  public boolean isSupportMethod(String paramString) { return true; }
  
  public void pay(PayParams paramPayParams) { Toast.makeText(U8SDK.getInstance().getContext(), "调用[支付接口]接口成功，PayParams中的参数，除了extension，其他的请都赋值，最后还需要经过打包工具来打出最终的渠道包", 1).show(); }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\SimpleDefaultPay.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */