package com.vcom.publiclibrary.utils;

import android.util.Log;

public class Logger {
    private Logger() {
    }

    /**
     * 日志的等级，默认为1，
     * 可以显示所有的日志等级
     * ①应用开发阶段，等级调为0可以保留所有等级的日志
     * ②在应用上线的时候，将日志等级调到2等级，那么只会保留w和e的日志信息
     * ③在应用上线的时候，将日志等级调到0等级，将会清楚所有日志信息，推荐使用6
     */
    private static int LOG_LEVEL = 6;

    /***************** 1、error级别的日志管理 *****************/
    /**
     * error级别的日志
     *
     * @param tag 日志的tag
     * @param msg 日志的输出信息
     */
    public static void e(String tag, String msg) {
        if (LOG_LEVEL <= 1)
            Log.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= 1)
            Log.e(tag, msg, tr);
    }

    /***************** 2、warm级别的日志管理 *****************/
    public static void w(String tag, String msg) {
        if (LOG_LEVEL <= 2)
            Log.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= 2)
            Log.w(tag, msg, tr);
    }

    /***************** 3、info级别的日志管理 *****************/
    public static void i(String tag, String msg) {
        if (LOG_LEVEL <= 3)
            Log.i(tag, msg);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= 3)
            Log.i(tag, msg, tr);
    }

    /***************** 4、debug级别的日志管理 *****************/
    public static void d(String tag, String msg) {
        if (LOG_LEVEL <= 4)
            Log.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= 4)
            Log.d(tag, msg, tr);
    }

    /***************** 5、verbose级别的日志管理 *****************/
    public static void v(String tag, String msg) {
        if (LOG_LEVEL <= 5)
            Log.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (LOG_LEVEL <= 5)
            Log.v(tag, msg, tr);
    }
    /***************** 6、更改日志显示等级 *****************/
    /**
     * 更改日志显示等级
     *
     * @param logLevel
     */
    public static void changeLogLevel(LogLevel logLevel) {
        switch (logLevel) {
            case VERBOSE:
                LOG_LEVEL = 5;
                break;
            case DEBUG:
                LOG_LEVEL = 4;
                break;
            case ERROR:
                LOG_LEVEL = 3;
                break;
            case INFO:
                LOG_LEVEL = 2;
                break;
            case WARN:
                LOG_LEVEL = 1;
                break;
            case NOLOG:
                LOG_LEVEL = 6;
                break;
            default:
                LOG_LEVEL = 0;
                break;
        }
    }

    /***************** 7、 日志等级的枚举类 *****************/
    /**
     * 日志等级的枚举类
     *
     * @author hacket
     */
    public enum LogLevel {
        VERBOSE, DEBUG, INFO, WARN, ERROR, NOLOG
    }
}
