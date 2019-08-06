package com.u8.sdk.log;

import com.u8.sdk.utils.U8HttpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class URemoteLogPrinter {
    private List<ULog> logs;
    private String url;
    private int interval = 1000;


    private Timer timer;


    private boolean running;


    public URemoteLogPrinter(String remoteUrl, int interval) {
        this.logs = Collections.synchronizedList(new ArrayList());
        this.url = remoteUrl;
        this.interval = interval;
    }


    public void print(ULog log) {
        start();
        synchronized (this.logs) {
            this.logs.add(log);
        }
    }


    public void printImmediate(String url, ULog log) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("log", log.toJSON());
        U8HttpUtils.httpPost(url, params);
    }

    public List<ULog> getAndClear() {
        synchronized (this.logs) {
            List<ULog> all = new ArrayList<ULog>(this.logs);
            this.logs.clear();
            return all;
        }
    }

    public void start() {
        if (this.running) {
            return;
        }

        this.running = true;
        TimerTask task = new LogPrintTask();
        this.timer = new Timer(true);
        this.timer.scheduleAtFixedRate(task, 100L, this.interval);
    }

    public void stop() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.running = false;
    }

    public URemoteLogPrinter() {
    }

    class LogPrintTask
            extends TimerTask {
        public void run() {
            try {
                List<ULog> logs = URemoteLogPrinter.this.getAndClear();

                if (logs.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for (ULog log : logs) {
                        sb.append(log.toJSON()).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1).append("]");

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("log", sb.toString());

                    U8HttpUtils.httpPost(URemoteLogPrinter.this.url, params);
                }

            } catch (Exception e) {
                e.printStackTrace();
                URemoteLogPrinter.this.stop();
            }
        }
    }
}


