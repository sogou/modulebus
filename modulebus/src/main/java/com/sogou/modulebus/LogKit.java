package com.sogou.modulebus;

import android.util.Log;

public class LogKit {

    private static final String TAG = "modulebus";

    /**
     * 日志打印级别
     */
    @LogLevel
    private static int sLevel;

    /**
     * 是否开启日志
     * @param debug
     */
    public static void init(boolean debug){
        if (debug){
            sLevel = LogLevel.LOG_W;
        }else{
            sLevel = LogLevel.LOG_NONE;
        }
    }

    /**
     * 日志打印级别设置
     * @param level
     */
    public static void init(@LogLevel int level){
        sLevel = level;
    }

    public static void log(String msg) {
        log(TAG, msg);
    }

    public static void log(String tag, String msg) {
        log(tag, msg, null);
    }

    public static void log(String tag, String msg, Throwable tr) {

        if (sLevel == LogLevel.LOG_NONE) return;

        switch (sLevel){
            case LogLevel.LOG_V:
                Log.v(tag, msg, tr);
                break;
            case LogLevel.LOG_D:
                Log.d(tag, msg, tr);
                break;
            case LogLevel.LOG_I:
                Log.i(tag, msg, tr);
                break;
            case LogLevel.LOG_W:
                Log.w(tag, msg, tr);
                break;
            case LogLevel.LOG_E:
                Log.e(tag, msg, tr);
                break;
        }
    }

}
