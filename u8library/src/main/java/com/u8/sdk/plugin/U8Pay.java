package com.u8.sdk.plugin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import com.u8.sdk.IAdditionalPay;
import com.u8.sdk.IPay;
import com.u8.sdk.PayParams;
import com.u8.sdk.SDKTools;
import com.u8.sdk.U8SDK;
import com.u8.sdk.U8SDKSingle;
import com.u8.sdk.base.PluginFactory;
import com.u8.sdk.impl.SimpleDefaultPay;
import com.u8.sdk.log.Log;
import com.u8.sdk.verify.U8Proxy;
import com.u8.sdk.verify.UOrder;

import java.util.List;


public class U8Pay {
    private static U8Pay instance;
    private IPay payPlugin;

    public static U8Pay getInstance() {
        if (instance == null) {
            instance = new U8Pay();
        }
        return instance;
    }

    public void init() {
        this.payPlugin = (IPay) PluginFactory.getInstance().initPlugin(2);
        if (this.payPlugin == null) {
            this.payPlugin = new SimpleDefaultPay();
        }
    }

    public boolean isSupport(String method) {
        if (this.payPlugin == null) {
            return false;
        }

        return this.payPlugin.isSupportMethod(method);
    }


    public void pay(PayParams data) {
        if (this.payPlugin == null) {
            return;
        }

        if (U8SDK.getInstance().isSingleGame()) {
            if (this.currPayParams != null) {
                Toast.makeText(U8SDK.getInstance().getContext(), "上一次支付未完成，请稍后再试，或重启再试", 1).show();


                return;
            }
        }

        this.currPayParams = data;

        Log.d("U8SDK", "****PayParams Print Begin****");
        Log.d("U8SDK", "productId=" + data.getProductId());
        Log.d("U8SDK", "productName=" + data.getProductName());
        Log.d("U8SDK", "productDesc=" + data.getProductDesc());
        Log.d("U8SDK", "buyNum=" + data.getBuyNum());
        Log.d("U8SDK", "price=" + data.getPrice());
        Log.d("U8SDK", "coinNum=" + data.getCoinNum());
        Log.d("U8SDK", "serverId=" + data.getServerId());
        Log.d("U8SDK", "serverName=" + data.getServerName());
        Log.d("U8SDK", "roleId=" + data.getRoleId());
        Log.d("U8SDK", "roleName=" + data.getRoleName());
        Log.d("U8SDK", "roleLevel=" + data.getRoleLevel());
        Log.d("U8SDK", "vip=" + data.getVip());
        Log.d("U8SDK", "orderID=" + data.getOrderID());
        Log.d("U8SDK", "extension=" + data.getExtension());
        Log.d("U8SDK", "****PayParams Print End****");

        if (U8SDK.getInstance().isGetOrder()) {

            startOrderTask(data);
        } else {
            this.payPlugin.pay(data);
        }
    }


    public boolean needQueryResult() {
        if (!(this.payPlugin instanceof IAdditionalPay)) {
            Log.w("U8SDK", "IPay error. single pay channel must implement IAdditionalPay interface");
            return false;
        }

        IAdditionalPay plugin = (IAdditionalPay) this.payPlugin;
        return plugin.needQueryResult();
    }


    public void checkFailedOrders() {
        if (!U8SDK.getInstance().isSingleGame()) {
            Log.d("U8SDK", "curr is not single game. no need check failed orders");

            return;
        }
        Log.d("U8SDK", "begin check orders pay.");

        if (!(this.payPlugin instanceof IAdditionalPay)) {
            Log.e("U8SDK", "IPay error. single pay channel must implement IAdditionalPay interface.");


            return;
        }

        U8SDKSingle.getInstance().startAutoTask();


        List<PayParams> orders = U8SDKSingle.getInstance().getCachedOrders();
        if (orders == null || orders.size() == 0) {
            Log.d("U8SDK", "there is no cached orders");

            return;
        }
        for (PayParams order : orders) {
            ((IAdditionalPay) this.payPlugin).checkFailedOrder(order);
        }
    }


    @SuppressLint({"NewApi"})
    private void startOrderTask(PayParams data) {
        GetOrderTask authTask = new GetOrderTask(data);
        if (Build.VERSION.SDK_INT >= 11) {

            authTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        } else {

            authTask.execute(new Void[0]);
        }
    }


    @SuppressLint({"NewApi"})
    class GetOrderTask
            extends AsyncTask<Void, Void, UOrder> {
        private PayParams data;
        private ProgressDialog processTip;

        public GetOrderTask(PayParams data) {
            this.data = data;
        }


        protected void onPreExecute() {
            this.processTip = SDKTools.showProgressTip(U8SDK.getInstance().getContext(), "正在启动支付，请稍后...");
        }


        protected UOrder doInBackground(Void... args) {
            Log.d("U8SDK", "begin to get order id from u8server...");
            return U8Proxy.getOrder(this.data);
        }


        protected void onPostExecute(UOrder order) {
            SDKTools.hideProgressTip(this.processTip);


            try {
                if (order == null) {
                    Log.e("U8SDK", "get order from u8server failed.");
                    U8Pay.this.currPayParams = null;
                    Toast.makeText(U8SDK.getInstance().getContext(), "获取订单号失败", 0).show();

                    return;
                }
                this.data.setOrderID(order.getOrder());
                this.data.setExtension(order.getExtension());
                this.data.setProductId(order.getProductID());

                Log.d("U8SDK", "get order from u8server success. orderID:" + order.getOrder() + ";extension:" + order.getExtension() + ";productId:" + order.getProductID());

                if (U8SDK.getInstance().isSingleGame()) {
                    U8SDKSingle.getInstance().storeOrder(this.data);
                }

                U8Pay.this.payPlugin.pay(this.data);
            } catch (Exception e) {
                U8Pay.this.currPayParams = null;
                e.printStackTrace();
            }
        }
    }


    public PayParams getCurrPayParams() {
        return this.currPayParams;
    }


    public void setCurrPayParams(PayParams order) {
        this.currPayParams = order;
    }
}


