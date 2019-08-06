package com.u8.sdk.platform;

import android.app.Activity;
import android.text.TextUtils;

import com.u8.sdk.PayParams;
import com.u8.sdk.ProductQueryResult;
import com.u8.sdk.U8Order;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.base.IU8SDKListener;
import com.u8.sdk.log.Log;
import com.u8.sdk.plugin.U8Pay;
import com.u8.sdk.plugin.U8User;
import com.u8.sdk.utils.ResourceHelper;
import com.u8.sdk.verify.UToken;

import java.util.List;


public class U8Platform {
    private static U8Platform instance;
    private boolean isSwitchAccount = false;

    public static U8Platform getInstance() {
        if (instance == null) {
            instance = new U8Platform();
        }
        return instance;
    }


    public void init(Activity context, final U8InitListener callback) {
        if (callback == null) {
            Log.d("U8SDK", "U8InitListener must be not null.");

            return;
        }

        try {
            U8SDK.getInstance().setSDKListener(new IU8SDKListener() {
                public void onResult(final int code, final String msg) {
                    Log.d("U8SDK", "onResult.code:" + code + ";msg:" + msg);

                    U8SDK.getInstance().runOnMainThread(new Runnable() {
                        public void run() {
                            switch (code) {
                                case 1:
                                    callback.onInitResult(1, msg);
                                    return;
                                case 2:
                                    callback.onInitResult(2, msg);
                                    return;
                                case 5:
                                    callback.onLoginResult(5, null);
                                    return;
                                case 10:
                                    callback.onPayResult(10, msg);
                                    return;
                                case 11:
                                    callback.onPayResult(11, msg);
                                    return;
                                case 33:
                                    callback.onPayResult(33, msg);
                                    return;
                                case 34:
                                    callback.onPayResult(34, msg);
                                    return;
                                case 35:
                                    callback.onPayResult(35, msg);
                                    return;
                                case 40:
                                    callback.onDestroy();
                                    return;
                            }
                            callback.onResult(code, msg);
                        }
                    });
                }


                public void onLogout() {
                    U8SDK.getInstance().runOnMainThread(new Runnable() {
                        public void run() {
                            callback.onLogout();
                        }
                    });
                }


                public void onSwitchAccount() {
                    U8SDK.getInstance().runOnMainThread(new Runnable() {
                        public void run() {
                            callback.onLogout();
                        }
                    });
                }


                public void onLoginResult(String data) {
                    Log.d("U8SDK", "SDK 登录成功,不用做处理，在onAuthResult中处理登录成功, 参数如下:");
                    Log.d("U8SDK", data);
                    U8Platform.this.isSwitchAccount = false;
                }


                public void onSwitchAccount(String data) {
                    Log.d("U8SDK", "SDK 切换帐号并登录成功,不用做处理，在onAuthResult中处理登录成功, 参数如下:");
                    Log.d("U8SDK", data);
                    U8Platform.this.isSwitchAccount = true;
                }


                public void onAuthResult(final UToken authResult) {
                    U8SDK.getInstance().runOnMainThread(new Runnable() {

                        public void run() {
                            if (U8Platform.this.isSwitchAccount){
                                if (authResult.isSuc()) {
                                    callback.onSwitchAccount(authResult);
                                } else {
                                    Log.e("U8SDK", "switch account auth failed.");
                                }
                            } else{

                                if (!authResult.isSuc()) {
                                    callback.onLoginResult(5, null);
                                    return;
                                }
                                callback.onLoginResult(4, authResult);
                            }
                        }
                    });
                }


                public void onProductQueryResult(List<ProductQueryResult> result) {
                    if (result == null) {
                        Log.e("U8SDK", "product query result with null. ");
                        return;
                    }
                    Log.d("U8SDK", "product query result:" + result.size());
                    callback.onProductQueryResult(result);
                }


                public void onSinglePayResult(int code, U8Order order) {
                    callback.onSinglePayResult(code, order);
                }
            });

            U8SDK.getInstance().init(context);
            U8SDK.getInstance().onCreate();
        } catch (Exception e) {
            callback.onInitResult(2, e.getMessage());
            Log.e("U8SDK", "init failed.", e);
            e.printStackTrace();
        }
    }


    public void login(Activity context) {
        U8SDK.getInstance().setContext(context);
        U8SDK.getInstance().runOnMainThread(new Runnable() {
            public void run() {
                U8User.getInstance().login();
            }
        });
    }


    public void loginCustom(Activity context, final String loginType) {
        U8SDK.getInstance().setContext(context);
        U8SDK.getInstance().runOnMainThread(new Runnable() {
            public void run() {
                if (U8User.getInstance().isSupport("loginCustom")) {
                    U8User.getInstance().login(loginType);
                } else {
                    U8User.getInstance().login();
                }
            }
        });
    }


    public void logout() {
        U8SDK.getInstance().runOnMainThread(new Runnable() {
            public void run() {
                if (U8User.getInstance().isSupport("logout")) {
                    U8User.getInstance().logout();
                }
            }
        });
    }


    public void switchAccount() {
        U8SDK.getInstance().runOnMainThread(new Runnable() {
            public void run() {
                U8User.getInstance().switchLogin();
            }
        });
    }


    public void showAccountCenter() {
        U8SDK.getInstance().runOnMainThread(new Runnable() {
            public void run() {
                if (U8User.getInstance().isSupport("showAccountCenter")) {
                    U8User.getInstance().showAccountCenter();
                }
            }
        });
    }


    public void submitExtraData(final UserExtraData data) {
        U8SDK.getInstance().runOnMainThread(new Runnable() {

            public void run() {
                if (TextUtils.isEmpty(data.getRoleID()) || TextUtils.isEmpty(data.getRoleName()) || TextUtils.isEmpty(data.getServerName()) || TextUtils.isEmpty(data.getRoleLevel())) {

                    Log.e("U8SDK", "roleID, roleName, roleLevel, serverID, serverName cannot be null");
                    ResourceHelper.showTipStr(U8SDK.getInstance().getContext(), "roleID, roleName, roleLevel, serverID, serverName等几个字段必传");
                }

                U8User.getInstance().submitExtraData(data);
            }
        });
    }


    public void exitSDK(final U8ExitListener callback) {
        U8SDK.getInstance().runOnMainThread(new Runnable() {
            public void run() {
                if (U8User.getInstance().isSupport("exit")) {
                    U8User.getInstance().exit();
                } else if (callback != null) {
                    callback.onGameExit();
                }
            }
        });
    }


    public void queryAntiAddiction() {
        if (U8User.getInstance().isSupport("queryAntiAddiction")) {
            U8SDK.getInstance().runOnMainThread(new Runnable() {
                public void run() {
                    U8User.getInstance().queryAntiAddiction();
                }
            });
        }
    }


    public void pay(Activity context, final PayParams data) {
        U8SDK.getInstance().setContext(context);
        U8SDK.getInstance().runOnMainThread(new Runnable() {
            public void run() {
                U8Pay.getInstance().pay(data);
            }
        });
    }


    public void queryProducts(Activity context) {
        U8SDK.getInstance().setContext(context);
        U8SDK.getInstance().runOnMainThread(new Runnable() {
            public void run() {
                U8User.getInstance().queryProducts();
            }
        });
    }
}


