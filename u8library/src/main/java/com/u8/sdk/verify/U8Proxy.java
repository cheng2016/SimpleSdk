package com.u8.sdk.verify;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.u8.sdk.PayParams;
import com.u8.sdk.U8SDK;
import com.u8.sdk.log.Log;
import com.u8.sdk.utils.EncryptUtils;
import com.u8.sdk.utils.GUtils;
import com.u8.sdk.utils.U8HttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


public class U8Proxy {
    @SuppressLint({"DefaultLocale"})
    public static UToken auth(String result) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("appID", U8SDK.getInstance().getAppID() + "");
            params.put("channelID", "" + U8SDK.getInstance().getCurrChannel());
            params.put("subChannelID", "" + U8SDK.getInstance().getSubChannel());
            params.put("extension", result);
            params.put("sdkVersionCode", U8SDK.getInstance().getSDKVersionCode());
            params.put("deviceID", GUtils.getDeviceID(U8SDK.getInstance().getContext()));

            StringBuilder sb = new StringBuilder();
            sb.append("appID=").append(U8SDK.getInstance().getAppID() + "")
                    .append("channelID=").append(U8SDK.getInstance().getCurrChannel())
                    .append("extension=").append(result).append(U8SDK.getInstance().getAppKey());

            String sign = EncryptUtils.md5(sb.toString()).toLowerCase();

            params.put("sign", sign);

            String authResult = U8HttpUtils.httpGet(U8SDK.getInstance().getAuthURL(), params);

            Log.d("U8SDK", "sign str:" + sb.toString());
            Log.d("U8SDK", "The sign is " + sign + " The auth result is " + authResult);

            return parseAuthResult(authResult);
        } catch (Exception e) {
            Log.e("U8SDK", "u8server auth exception.", e);
            e.printStackTrace();


            return new UToken();
        }
    }


    public static UOrder getOrder(PayParams data) {
        try {
            UToken tokenInfo = U8SDK.getInstance().getUToken();
            if (tokenInfo == null && !U8SDK.getInstance().isSingleGame()) {
                Log.e("U8SDK", "The user not logined. the token is null");
                return null;
            }

            Map<String, String> params = new HashMap<String, String>();
            params.put("userID", (tokenInfo == null) ? "0" : ("" + tokenInfo.getUserID()));
            params.put("productID", data.getProductId());
            params.put("productName", data.getProductName());
            params.put("productDesc", data.getProductDesc());
            params.put("money", "" + (data.getPrice() * 100));
            params.put("roleID", "" + data.getRoleId());
            params.put("roleName", data.getRoleName());
            params.put("roleLevel", data.getRoleLevel() + "");
            params.put("serverID", data.getServerId());
            params.put("serverName", data.getServerName());
            params.put("extension", data.getExtension());
            params.put("notifyUrl", data.getPayNotifyUrl());

            if (U8SDK.getInstance().isSingleGame()) {

                params.put("appID", U8SDK.getInstance().getAppID() + "");
                params.put("channelID", U8SDK.getInstance().getCurrChannel() + "");
                params.put("subChannelID", U8SDK.getInstance().getSubChannel() + "");
            }


            params.put("signType", "md5");
            String sign = generateSign(tokenInfo, data);
            params.put("sign", sign);
            params.put("sdkVersionCode", U8SDK.getInstance().getSDKVersionCode());

            String orderResult = U8HttpUtils.httpPost(U8SDK.getInstance().getOrderURL(), params);

            Log.d("U8SDK", "The order result is " + orderResult);

            return parseOrderResult(data.getProductId(), orderResult);
        } catch (Exception e) {
            e.printStackTrace();


            return null;
        }
    }


    @SuppressLint({"DefaultLocale"})
    public static boolean completePay(PayParams order) {
        try {
            if (order == null) return false;

            String t = System.nanoTime() + "";

            Map<String, String> params = new HashMap<String, String>();
            params.put("appID", U8SDK.getInstance().getAppID() + "");
            params.put("channelID", "" + U8SDK.getInstance().getCurrChannel());
            params.put("orderID", order.getOrderID());
            params.put("channelOrderID", (order.getChannelOrderID() == null) ? "" : order.getChannelOrderID());
            params.put("t", t);

            StringBuilder sb = new StringBuilder();
            sb.append("appID=").append(U8SDK.getInstance().getAppID() + "")
                    .append("channelID=").append(U8SDK.getInstance().getCurrChannel())
                    .append("orderID=").append(order.getOrderID())
                    .append("channelOrderID=").append((order.getChannelOrderID() == null) ? "" : order.getChannelOrderID())
                    .append("t=").append(t)
                    .append(U8SDK.getInstance().getAppKey());

            String sign = EncryptUtils.md5(sb.toString()).toLowerCase();

            params.put("sign", sign);

            String result = U8HttpUtils.httpPost(U8SDK.getInstance().getPayCompleteURL(), params);

            Log.d("U8SDK", "completePay sign str:" + sb.toString());
            Log.d("U8SDK", "completePay the sign is " + sign + " The http get result is " + result);

            if ("SUCCESS".equalsIgnoreCase(result)) {
                return true;
            }
            return false;

        } catch (Exception e) {
            Log.e("U8SDK", "u8server completePay exception.", e);
            e.printStackTrace();


            return false;
        }
    }


    @SuppressLint({"DefaultLocale"})
    public static UCheckResult check(PayParams order) {
        try {
            if (order == null) return null;

            String t = System.nanoTime() + "";

            Map<String, String> params = new HashMap<String, String>();
            params.put("appID", U8SDK.getInstance().getAppID() + "");
            params.put("channelID", "" + U8SDK.getInstance().getCurrChannel());
            params.put("orderID", order.getOrderID());
            params.put("t", t);

            StringBuilder sb = new StringBuilder();
            sb.append("appID=").append(U8SDK.getInstance().getAppID() + "")
                    .append("channelID=").append(U8SDK.getInstance().getCurrChannel())
                    .append("orderID=").append(order.getOrderID())
                    .append("t=").append(t)
                    .append(U8SDK.getInstance().getAppKey());

            String sign = EncryptUtils.md5(sb.toString()).toLowerCase();

            params.put("sign", sign);

            String result = U8HttpUtils.httpPost(U8SDK.getInstance().getPayCheckURL(), params);

            Log.d("U8SDK", "pay check sign str:" + sb.toString());
            Log.d("U8SDK", "pay check the sign is " + sign + " The http post result is " + result);

            if (TextUtils.isEmpty(result)) {
                return null;
            }

            try {
                JSONObject json = new JSONObject(result);
                int suc = json.optInt("suc", 0);
                int state = json.optInt("state", 0);

                return new UCheckResult(suc, state);
            } catch (Exception e) {
                e.printStackTrace();

            }


        } catch (Exception e) {
            Log.e("U8SDK", "u8server check exception.", e);
            e.printStackTrace();
        }

        return null;
    }


    @SuppressLint({"DefaultLocale"})
    private static String generateSign(UToken token, PayParams data) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append("userID=").append((token == null) ? "0" : (token.getUserID() + "")).append("&")
                .append("productID=").append((data.getProductId() == null) ? "" : data.getProductId()).append("&")
                .append("productName=").append((data.getProductName() == null) ? "" : data.getProductName()).append("&")
                .append("productDesc=").append((data.getProductDesc() == null) ? "" : data.getProductDesc()).append("&")
                .append("money=").append(data.getPrice() * 100).append("&")
                .append("roleID=").append((data.getRoleId() == null) ? "" : data.getRoleId()).append("&")
                .append("roleName=").append((data.getRoleName() == null) ? "" : data.getRoleName()).append("&")
                .append("roleLevel=").append(data.getRoleLevel()).append("&")
                .append("serverID=").append((data.getServerId() == null) ? "" : data.getServerId()).append("&")
                .append("serverName=").append((data.getServerName() == null) ? "" : data.getServerName()).append("&")
                .append("extension=").append((data.getExtension() == null) ? "" : data.getExtension());


        if (!TextUtils.isEmpty(data.getPayNotifyUrl())) {
            sb.append("&notifyUrl=").append(data.getPayNotifyUrl());
        }


        if (U8SDK.getInstance().isSingleGame()) {


            sb.append("&appID=").append(U8SDK.getInstance().getAppID() + "");
            sb.append("&channelID=").append(U8SDK.getInstance().getCurrChannel() + "");

            if (U8SDK.getInstance().getSubChannel() > 0) {
                sb.append("&subChannelID=").append(U8SDK.getInstance().getSubChannel() + "");
            }
        }


        sb.append(U8SDK.getInstance().getAppKey());

        String encoded = URLEncoder.encode(sb.toString(), "UTF-8");

        Log.d("U8SDK", "The encoded getOrderID sign is " + encoded);


        String sign = EncryptUtils.md5(encoded).toLowerCase();


        Log.d("U8SDK", "The getOrderID sign is " + sign);

        return sign;
    }


    private static UToken parseAuthResult(String authResult) {
        if (authResult == null || TextUtils.isEmpty(authResult)) {
            return new UToken();
        }

        try {
            JSONObject jsonObj = new JSONObject(authResult);
            int state = jsonObj.getInt("state");

            if (state != 1) {
                Log.d("U8SDK", "auth failed. the state is " + state);
                return new UToken();
            }

            JSONObject jsonData = jsonObj.getJSONObject("data");

            return new UToken(jsonData.getInt("userID"), jsonData
                    .getString("sdkUserID"), jsonData
                    .getString("username"), jsonData
                    .getString("sdkUserName"), jsonData
                    .getString("token"), jsonData
                    .getString("extension"), jsonData
                    .optString("timestamp"));
        } catch (JSONException e) {

            e.printStackTrace();


            return new UToken();
        }
    }

    private static UOrder parseOrderResult(String productID, String orderResult) {
        try {
            JSONObject jsonObj = new JSONObject(orderResult);
            int state = jsonObj.getInt("state");

            if (state != 1) {
                Log.d("U8SDK", "get order failed. the state is " + state);
                return null;
            }

            JSONObject jsonData = jsonObj.getJSONObject("data");

            return new UOrder(jsonData.getString("orderID"), jsonData.optString("extension", ""), jsonData.optString("productID", productID));
        } catch (Exception e) {
            e.printStackTrace();


            return null;
        }
    }
}


