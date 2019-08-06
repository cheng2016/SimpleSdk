package com.u8.sdk.log;

public interface ILog {
    void d(String paramString1, String paramString2);

    void i(String paramString1, String paramString2);

    void w(String paramString1, String paramString2);

    void w(String paramString1, String paramString2, Throwable paramThrowable);

    void e(String paramString1, String paramString2);

    void e(String paramString1, String paramString2, Throwable paramThrowable);

    void destory();
}


