package com.u8.sdk.platform;

import com.u8.sdk.ProductQueryResult;
import com.u8.sdk.U8Order;
import com.u8.sdk.verify.UToken;

import java.util.List;

public interface U8InitListener {
    void onInitResult(int paramInt, String paramString);

    void onLoginResult(int paramInt, UToken paramUToken);

    void onSwitchAccount(UToken paramUToken);

    void onLogout();

    void onPayResult(int paramInt, String paramString);

    void onSinglePayResult(int paramInt, U8Order paramU8Order);

    void onProductQueryResult(List<ProductQueryResult> paramList);

    void onDestroy();

    void onResult(int paramInt, String paramString);
}


