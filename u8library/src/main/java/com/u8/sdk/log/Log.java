package com.u8.sdk.log;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class Log {
    private static Log instance = new Log();

    private boolean isInited = false;

    private boolean enable = false;

    private String level = "DEBUG";
    private boolean local = true;
    private boolean remote = true;
    private int remoteInterval = 1000;
    private String remoteUrl = "";


    private List<ILog> logPrinters = new ArrayList();


    public static void d(String tag, String msg) {
        try {
            if (!"DEBUG".equalsIgnoreCase(instance.level)) {
                return;
            }

            for (ILog printer : instance.logPrinters) {
                printer.d(tag, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void i(String tag, String msg) {
        try {
            if (!"DEBUG".equalsIgnoreCase(instance.level) &&
                    !"INFO".equalsIgnoreCase(instance.level)) {
                return;
            }

            for (ILog printer : instance.logPrinters) {
                printer.i(tag, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void w(String tag, String msg) {
        try {
            if ("ERROR".equalsIgnoreCase(instance.level)) {
                return;
            }

            for (ILog printer : instance.logPrinters) {
                printer.w(tag, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void w(String tag, String msg, Throwable e) {
        try {
            if ("ERROR".equalsIgnoreCase(instance.level)) {
                return;
            }

            for (ILog printer : instance.logPrinters) {
                printer.w(tag, msg, e);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public static void e(String tag, String msg) {
        try {
            for (ILog printer : instance.logPrinters) {
                printer.e(tag, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void e(String tag, String msg, Throwable e) {
        try {
            for (ILog printer : instance.logPrinters) {
                printer.e(tag, msg, e);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public static void init(Context context) {
        try {
            if (instance.isInited) {
                return;
            }

            instance.parseConfig(context);

            instance.logPrinters.clear();

            if (!instance.enable) {
                Log.d("ULOG", "the log is not enabled.");

                return;
            }
            if (instance.local) {
                instance.logPrinters.add(new ULocalLog());
            }

            if (instance.remote) {
                instance.logPrinters.add(new URemoteLog(instance.remoteUrl, instance.remoteInterval));
            }

            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

                public void uncaughtException(Thread t, Throwable e) {
                    (new Thread(new Runnable(this, e) {


                        public void run() {
                            try {
                                Log.e("U8SDK", "Application Crashed!!!");
                                e.printStackTrace();

                                if (instance.remote) {
                                    URemoteLogPrinter printer = new URemoteLogPrinter();
                                    printer.printImmediate(instance.remoteUrl, new ULog("ERROR", "Crash", "Application Crashed!!!", e));
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                System.exit(0);
                            }

                        }
                    })).start();

                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            });


            instance.isInited = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void destory() {
        try {
            if (instance.logPrinters != null) {
                for (ILog printer : instance.logPrinters) {
                    printer.destory();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseConfig(Context ctx) {
        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), 128);
            if (appInfo != null && appInfo.metaData != null) {
                if (appInfo.metaData.containsKey("ulog.enable")) {
                    this.enable = appInfo.metaData.getBoolean("ulog.enable");
                }

                if (appInfo.metaData.containsKey("ulog.level")) {
                    this.level = appInfo.metaData.getString("ulog.level");
                }

                if (appInfo.metaData.containsKey("ulog.local")) {
                    this.local = appInfo.metaData.getBoolean("ulog.local");
                }

                if (appInfo.metaData.containsKey("ulog.remote")) {
                    this.remote = appInfo.metaData.getBoolean("ulog.remote");
                }

                if (appInfo.metaData.containsKey("ulog.remote_interval")) {
                    this.remoteInterval = appInfo.metaData.getInt("ulog.remote_interval");
                }

                if (appInfo.metaData.containsKey("ulog.remote_url")) {
                    this.remoteUrl = appInfo.metaData.getString("ulog.remote_url");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


