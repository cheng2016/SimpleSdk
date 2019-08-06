package com.u8.sdk.analytics;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.utils.GUtils;
import com.u8.sdk.verify.UToken;


public class UDAgent {
    private static UDAgent instance;

    public static UDAgent getInstance() {
        if (instance == null) {
            instance = new UDAgent();
        }
        return instance;
    }


    public void init(Activity context) {
        try {
            UDevice device = UDManager.getInstance().collectDeviceInfo(context, Integer.valueOf(U8SDK.getInstance().getAppID()), Integer.valueOf(U8SDK.getInstance().getCurrChannel()), Integer.valueOf(U8SDK.getInstance().getSubChannel()));
            if (device == null) {
                Log.e("U8SDK", "collect device info failed");
                return;
            }
            SubmitTask task = new SubmitTask(new ISubmitListener(this, context, device) {
                public void run() {
                    UDManager.getInstance().submitDeviceInfo(context, U8SDK.getInstance().getAnalyticsURL(), U8SDK.getInstance().getAppKey(), device);
                }
            });
            task.execute(new Void[0]);
        } catch (Exception e) {
            Log.e("U8SDK", "submit device info failed.\n" + e.getMessage());
            e.printStackTrace();
        }
    }


    public void submitUserInfo(Activity context, UserExtraData user) {
        try {
            UToken token = U8SDK.getInstance().getUToken();
            if (token == null) {
                Log.e("U8SDK", "utoken is null. submit user info failed.");

                return;
            }
            UUserLog log = new UUserLog();
            boolean sendable = false;
            switch (user.getDataType()) {
                case 2:
                    log.setOpType(Integer.valueOf(1));
                    sendable = true;
                    break;
                case 3:
                    sendable = true;
                    log.setOpType(Integer.valueOf(2));
                    break;
                case 4:
                    sendable = true;
                    log.setOpType(Integer.valueOf(3));
                    break;
                case 5:
                    sendable = true;
                    log.setOpType(Integer.valueOf(4));
                    break;
            }

            if (sendable) {
                log.setUserID(Integer.valueOf(token.getUserID()));
                log.setAppID(Integer.valueOf(U8SDK.getInstance().getAppID()));
                log.setChannelID(Integer.valueOf(U8SDK.getInstance().getCurrChannel()));
                log.setServerID(user.getServerID() + "");
                log.setServerName(user.getServerName());
                log.setRoleID(user.getRoleID());
                log.setRoleName(user.getRoleName());
                log.setRoleLevel(user.getRoleLevel());
                log.setDeviceID(GUtils.getDeviceID(context));


                SubmitTask task = new SubmitTask(new ISubmitListener(this, context, log) {
                    public void run() {
                        UDManager.getInstance().submitUserInfo(context, U8SDK.getInstance().getAnalyticsURL(), U8SDK.getInstance().getAppKey(), log);
                    }
                });
                task.execute(new Void[0]);
            }

        } catch (Exception e) {
            Log.e("U8SDK", "submit user info failed.\n" + e.getMessage());
            e.printStackTrace();
        }
    }


    class SubmitTask
            extends AsyncTask<Void, Void, Void> {
        private UDAgent.ISubmitListener runListener;


        public SubmitTask(UDAgent.ISubmitListener run) {
            this.runListener = run;
        }


        protected Void doInBackground(Void... arg) {
            if (this.runListener != null) {
                this.runListener.run();
            }

            return null;
        }
    }

    static interface ISubmitListener {
        void run();
    }
}


