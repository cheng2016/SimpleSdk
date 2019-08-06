package com.u8.sdk.base;

import com.u8.sdk.ProductQueryResult;
import com.u8.sdk.U8Order;
import com.u8.sdk.verify.UToken;

import java.util.List;

public interface IU8SDKListener {
    void onResult(int paramInt, String paramString);

    void onLoginResult(String paramString);

    void onSwitchAccount();

    void onSwitchAccount(String paramString);

    void onLogout();

    void onAuthResult(UToken paramUToken);

    void onProductQueryResult(List<ProductQueryResult> paramList);

    void onSinglePayResult(int paramInt, U8Order paramU8Order);
}


