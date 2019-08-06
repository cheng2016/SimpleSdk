package com.u8.sdk.impl.common;

public static enum PayPlatformType {
  ALIPAY, UNION, WEIXIN, XCOIN;
  
  static  {
    UNION = new PayPlatformType("UNION", 2);
    XCOIN = new PayPlatformType("XCOIN", 3);
    $VALUES = new PayPlatformType[] { ALIPAY, WEIXIN, UNION, XCOIN };
  }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\common\PayPlatformType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */