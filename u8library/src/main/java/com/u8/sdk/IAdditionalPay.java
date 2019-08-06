package com.u8.sdk;

public interface IAdditionalPay extends IPay {
    void checkFailedOrder(PayParams paramPayParams);

    boolean needQueryResult();
}


