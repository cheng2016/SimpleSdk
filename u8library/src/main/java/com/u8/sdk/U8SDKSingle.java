package com.u8.sdk;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;

import com.u8.sdk.base.IU8SDKListener;
import com.u8.sdk.log.Log;
import com.u8.sdk.plugin.U8Pay;
import com.u8.sdk.utils.ResourceHelper;
import com.u8.sdk.utils.StoreUtils;
import com.u8.sdk.verify.U8Proxy;
import com.u8.sdk.verify.UCheckResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class U8SDKSingle {
    private final Timer timer = new Timer();


    private boolean autoChecking;

    private List<String> handlingOrders = Collections.synchronizedList(new ArrayList());


    public static U8SDKSingle getInstance() {
        if (instance == null) {
            instance = new U8SDKSingle();
        }
        return instance;
    }


    private void onSinglePayResult(int code, PayParams params) {
        if (params == null) {
            return;
        }


        U8Order order = new U8Order(params.getOrderID(), params.getProductId(), params.getProductName(), params.getExtension());
        for (IU8SDKListener listener : U8SDK.getInstance().getAllListeners()) {
            listener.onSinglePayResult(code, order);
        }
    }


    public void handleResult(int code, String msg) {
        Log.d("U8SDK", "handleResult in U8SDKSingle. code:" + code + ";msg:" + msg);


        if (U8Pay.getInstance().getCurrPayParams() != null) {

            PayParams order = U8Pay.getInstance().getCurrPayParams();
            String orderID = order.getOrderID();

            switch (code) {

                case 10:
                case 35:
                    updateOrderState(orderID, 1);
                    this.handlingOrders.add(orderID);
                    if (U8Pay.getInstance().needQueryResult()) {

                        Log.d("U8SDK", "pay success. start pay check task");
                        startTask(new PayCheckTask(), order);
                        break;
                    }
                    Log.d("U8SDK", "pay success. start pay complete task");
                    startTask(new PayCompleteTask(), order);
                    break;


                case 11:
                case 33:
                    removeOrder(orderID);
                    onSinglePayResult(code, order);
                    break;
                case 34:
                    updateOrderState(orderID, 2);
                    this.handlingOrders.add(orderID);
                    if (U8Pay.getInstance().needQueryResult()) {
                        startPayCheckTask(order);
                    }
                    break;
            }


            U8Pay.getInstance().setCurrPayParams(null);
        }


        String orderID = msg;
        PayParams order = null;
        switch (code) {
            case 50:
                updateOrderState(orderID, 1);

                if (this.handlingOrders.contains(orderID)) {
                    this.handlingOrders.remove(orderID);
                }

                this.handlingOrders.add(orderID);
                order = getOrder(orderID);

                if (order != null) {
                    if (U8Pay.getInstance().needQueryResult()) {
                        Log.d("U8SDK", "pay sucesss. start check retry task.");
                        startTask(new PayCheckRetryTask(), order);
                        break;
                    }
                    startTask(new PayCompleteRetryTask(), order);

                    break;
                }
                Log.e("U8SDK", "order not exists in local cache. ");
                break;

            case 52:
                code = 34;
                break;
            case 51:
                order = removeOrder(msg);
                code = 11;
                onSinglePayResult(code, order);
                break;
        }
    }

    @SuppressLint({"NewApi"})
    private void startPayCheckTask(PayParams order) {
        PayCheckTask task = new PayCheckTask();
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new PayParams[]{order});
        } else {
            task.execute(new PayParams[]{order});
        }
    }


    private void onPayCheckResult(PayParams order, UCheckResult result) {
        if (result == null || result.getState() != 1) {

            this.handlingOrders.remove(order.getOrderID());
            ResourceHelper.showTipStr(U8SDK.getInstance().getContext(), "订单状态检查失败，稍后将自动重试");

            return;
        }
        Log.d("U8SDK", "onPayCheckResult called. pay success");
        this.handlingOrders.remove(order.getOrderID());
        removeOrder(order.getOrderID());
        onSinglePayResult(10, order);
    }


    private void onPayCheckRetryResult(PayParams order, UCheckResult result) {
        this.handlingOrders.remove(order.getOrderID());

        if (result == null || result.getState() != 1) {

            Log.d("U8SDK", "pay check retry failed. will auto retry again. order id:" + order.getOrderID());
        } else {

            removeOrder(order.getOrderID());
            onSinglePayResult(10, order);
        }
    }


    private void onPayCompleteResult(boolean suc, PayParams order) {
        Log.d("U8SDK", "onPayCompleteResult:" + suc);
        this.handlingOrders.remove(order.getOrderID());
        if (suc) {
            removeOrder(order.getOrderID());
            onSinglePayResult(10, order);
        } else {
            ResourceHelper.showTipStr(U8SDK.getInstance().getContext(), "支付发货失败，稍后将自动重试");
        }
    }

    private void onPayCompleteRetryResult(boolean suc, PayParams order) {
        Log.d("U8SDK", "onPayCompleteRetryResult:" + suc);
        this.handlingOrders.remove(order.getOrderID());
        if (suc) {
            removeOrder(order.getOrderID());
            onSinglePayResult(10, order);
        }
    }


    public void startAutoTask() {
        if (this.autoChecking) {
            Log.w("U8SDK", "auto task already started. just igore.");

            return;
        }
        this.autoChecking = true;

        if (U8Pay.getInstance().needQueryResult()) {
            this.timer.schedule(this.autoCheckTask, 60000L, 60000L);
        } else {
            this.timer.schedule(this.autoCompleteTask, 60000L, 60000L);
        }
    }

    public void stopAutoTask() {
        this.autoChecking = false;
        if (this.timer != null) {
            this.timer.cancel();
        }
    }


    @SuppressLint({"NewApi"})
    class PayCompleteTask
            extends AsyncTask<PayParams, Void, Boolean> {
        private PayParams order;
        private ProgressDialog processTip;

        protected void onPreExecute() {
            this.processTip = SDKTools.showProgressTip(U8SDK.getInstance().getContext(), "正在完成支付，请稍后...");
        }


        protected Boolean doInBackground(PayParams... args) {
            this.order = args[0];
            Log.d("U8SDK", "begin to send pay complete req..." + this.order.getOrderID());
            return Boolean.valueOf(U8Proxy.completePay(this.order));
        }


        protected void onPostExecute(Boolean result) {
            U8SDKSingle.this.onPayCompleteResult(result.booleanValue(), this.order);
            SDKTools.hideProgressTip(this.processTip);
        }
    }


    @SuppressLint({"NewApi"})
    class PayCompleteRetryTask
            extends AsyncTask<PayParams, Void, Boolean> {
        private PayParams order;


        protected Boolean doInBackground(PayParams... args) {
            this.order = args[0];
            Log.d("U8SDK", "begin to send pay complete retry req..." + this.order.getOrderID());
            return Boolean.valueOf(U8Proxy.completePay(this.order));
        }


        protected void onPostExecute(Boolean result) {
            U8SDKSingle.this.onPayCompleteRetryResult(result.booleanValue(), this.order);
        }
    }


    @SuppressLint({"NewApi"})
    class PayCheckTask
            extends AsyncTask<PayParams, Void, UCheckResult> {
        private PayParams order;

        private ProgressDialog processTip;


        protected void onPreExecute() {
            this.processTip = SDKTools.showProgressTip(U8SDK.getInstance().getContext(), "正在完成支付，请稍后...");
        }


        protected UCheckResult doInBackground(PayParams... args) {
            this.order = args[0];

            Log.d("U8SDK", "begin to send pay check req..." + this.order.getOrderID());

            int tryNum = 0;
            UCheckResult result = null;
            do {
                if (tryNum > 0) {
                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                tryNum++;
                result = U8Proxy.check(this.order);
            }
            while ((result == null || result.getState() != 1) && tryNum < 3);


            return result;
        }


        protected void onPostExecute(UCheckResult result) {
            SDKTools.hideProgressTip(this.processTip);
            U8SDKSingle.this.onPayCheckResult(this.order, result);
        }
    }


    @SuppressLint({"NewApi"})
    class PayCheckRetryTask
            extends AsyncTask<PayParams, Void, UCheckResult> {
        private PayParams order;


        protected UCheckResult doInBackground(PayParams... args) {
            this.order = args[0];

            Log.d("U8SDK", "begin to send pay check retry req..." + this.order.getOrderID());
            return U8Proxy.check(this.order);
        }


        protected void onPostExecute(UCheckResult result) {
            U8SDKSingle.this.onPayCheckRetryResult(this.order, result);
        }
    }


    private TimerTask autoCheckTask = new TimerTask() {

        public void run() {
            List<PayParams> orders = U8SDKSingle.this.getCachedOrders();

            Log.d("U8SDK", "begin auto check failed orders");

            if (orders == null || orders.size() == 0) {
                Log.d("U8SDK", "there is no order in cache.");


                return;
            }

            for (final PayParams order : orders) {

                if (order.getState() != 1) {
                    Log.d("U8SDK", "order state is not suc. just ignore." + order.getOrderID());

                    continue;
                }
                if (U8SDKSingle.this.handlingOrders.contains(order.getOrderID())) {
                    Log.d("U8SDK", "order current in handling orders. just ignore." + order.getOrderID());

                    continue;
                }
                final UCheckResult result = U8Proxy.check(order);

                U8SDK.getInstance().runOnMainThread(new Runnable() {
                    public void run() {
                      onPayCheckRetryResult(order, result);
                    }
                });
            }
        }
    };


    private TimerTask autoCompleteTask = new TimerTask() {

        public void run() {
            List<PayParams> orders = U8SDKSingle.this.getCachedOrders();
            if (orders == null || orders.size() == 0) {
                return;
            }

            for (final PayParams order : orders) {

                if (order.getState() != 1) {
                    continue;
                }

                if (U8SDKSingle.this.handlingOrders.contains(order.getOrderID())) {
                    continue;
                }

                final boolean suc = U8Proxy.completePay(order);

                U8SDK.getInstance().runOnMainThread(new Runnable() {
                    public void run() {
                        onPayCompleteRetryResult(suc, order);
                    }
                });
            }
        }
    };


    private static final String PAY_STORE_KEY = "u8paystorekey";

    private static final int STATE_NORMAL = 0;
    private static final int STATE_PAY_SUC = 1;

    public List<PayParams> getCachedOrders() {
        String c = StoreUtils.getString(U8SDK.getInstance().getContext(), "u8paystorekey");
        if (TextUtils.isEmpty(c)) {
            return null;
        }


        try {
            JSONArray array = new JSONArray(c);
            List<PayParams> result = new ArrayList<PayParams>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.getJSONObject(i);

                String orderID = j.optString("orderId", null);
                if (!TextUtils.isEmpty(orderID)) {


                    Log.d("U8SDK", "begin get pay order in cache. orderID:" + orderID);


                    try {
                        PayParams order = new PayParams();
                        order.setOrderID(orderID);
                        order.setProductId(j.optString("productId"));
                        order.setProductName(j.optString("productName"));
                        order.setExtension(j.optString("extension"));
                        order.setState(j.optInt("state", 0));

                        result.add(order);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();


            return null;
        }
    }

    private static final int STATE_PAY_UNKOWN = 2;
    private static final int MAX_TRY_NUM = 3;
    private static final int TRY_INTERVAL_SECS = 10;
    private static U8SDKSingle instance;

    public void updateOrderState(String orderID, int state) {
        Log.d("U8SDK", "begin update order state:" + orderID);

        if (TextUtils.isEmpty(orderID)) {
            return;
        }

        String g = StoreUtils.getString(U8SDK.getInstance().getContext(), "u8paystorekey");
        if (TextUtils.isEmpty(g)) {
            g = "[]";
        }


        try {
            JSONArray array = new JSONArray(g);
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                if (orderID.equals(item.optString("orderId", ""))) {
                    item.put("state", state);

                    break;
                }
            }
            StoreUtils.putString(U8SDK.getInstance().getContext(), "u8paystorekey", array.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void storeOrder(PayParams p) {
        Log.d("U8SDK", "begin store order:");

        String g = StoreUtils.getString(U8SDK.getInstance().getContext(), "u8paystorekey");
        if (TextUtils.isEmpty(g)) {
            g = "[]";
        }


        try {
            if (p != null) {
                Log.d("U8SDK", "store order pay data info:");
                JSONObject extra = new JSONObject();


                extra.put("orderId", p.getOrderID());
                extra.put("productId", p.getProductId());
                extra.put("productName", p.getProductName());
                extra.put("extension", p.getExtension());
                extra.put("state", 0);

                JSONArray array = new JSONArray(g);
                array.put(extra);
                StoreUtils.putString(U8SDK.getInstance().getContext(), "u8paystorekey", array.toString());

                Log.d("U8SDK", "store order success." + extra.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public PayParams getOrder(String orderID) {
        Log.d("U8SDK", "begin to remove order from store." + orderID);

        if (TextUtils.isEmpty(orderID)) {
            return null;
        }

        String g = StoreUtils.getString(U8SDK.getInstance().getContext(), "u8paystorekey");
        if (TextUtils.isEmpty(g)) {
            g = "[]";
        }


        try {
            JSONArray array = new JSONArray(g);
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                if (orderID.equals(item.optString("orderId", ""))) {
                    Log.d("U8SDK", "get order from store:" + orderID);
                    PayParams order = new PayParams();
                    order.setOrderID(orderID);
                    order.setProductId(item.optString("productId"));
                    order.setProductName(item.optString("productName"));
                    order.setExtension(item.optString("extension"));
                    order.setState(item.optInt("state", 0));
                    return order;
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public PayParams removeOrder(String orderID) {
        Log.d("U8SDK", "begin to remove order from store." + orderID);

        if (TextUtils.isEmpty(orderID)) {
            return null;
        }

        String g = StoreUtils.getString(U8SDK.getInstance().getContext(), "u8paystorekey");
        if (TextUtils.isEmpty(g)) {
            g = "[]";
        }

        PayParams order = null;


        try {
            JSONArray array = new JSONArray(g);
            JSONArray left = new JSONArray();
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                if (!orderID.equals(item.optString("orderId", ""))) {
                    left.put(item);
                } else {
                    Log.d("U8SDK", "remove order from store:" + orderID);
                    order = new PayParams();
                    order.setOrderID(orderID);
                    order.setProductId(item.optString("productId"));
                    order.setProductName(item.optString("productName"));
                    order.setExtension(item.optString("extension"));
                    order.setState(item.optInt("state", 0));
                }
            }

            StoreUtils.putString(U8SDK.getInstance().getContext(), "u8paystorekey", left.toString());

            Log.d("U8SDK", "remove order success." + orderID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return order;
    }


    @SuppressLint({"NewApi"})
    private void startTask(AsyncTask<PayParams, ?, ?> task, PayParams order) {
        if (Build.VERSION.SDK_INT >= 11) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new PayParams[]{order});
        } else {
            task.execute(new PayParams[]{order});
        }
    }
}


