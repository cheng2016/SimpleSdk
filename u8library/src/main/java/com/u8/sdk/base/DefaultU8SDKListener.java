package com.u8.sdk.base;

import com.u8.sdk.ProductQueryResult;
import com.u8.sdk.U8Order;
import com.u8.sdk.verify.UToken;

import java.util.List;

public class DefaultU8SDKListener implements IU8SDKListener {
    public void onResult(int code, String msg) {
    }

    public void onLoginResult(String result) {
    }

    public void onSwitchAccount() {
    }

    public void onSwitchAccount(String result) {
    }

    public void onLogout() {
    }

    public void onAuthResult(UToken authResult) {
    }

    public void onProductQueryResult(List<ProductQueryResult> result) {
    }

    public void onSinglePayResult(int code, U8Order order) {
    }
}


