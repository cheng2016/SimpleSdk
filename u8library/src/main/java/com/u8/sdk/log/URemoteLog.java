package com.u8.sdk.log;


public class URemoteLog
        implements ILog {
    private URemoteLogPrinter printer;

    public URemoteLog(String url, int interval) {
        this.printer = new URemoteLogPrinter(url, interval);
    }


    public void d(String tag, String msg) {
        this.printer.print(new ULog("DEBUG", tag, msg));
    }


    public void i(String tag, String msg) {
        this.printer.print(new ULog("INFO", tag, msg));
    }


    public void w(String tag, String msg) {
        this.printer.print(new ULog("WARNING", tag, msg));
    }


    public void w(String tag, String msg, Throwable e) {
        this.printer.print(new ULog("WARNING", tag, msg, e));
    }


    public void e(String tag, String msg) {
        this.printer.print(new ULog("ERROR", tag, msg));
    }


    public void e(String tag, String msg, Throwable e) {
        this.printer.print(new ULog("ERROR", tag, msg, e));
    }


    public void destory() {
        this.printer.stop();
    }
}


