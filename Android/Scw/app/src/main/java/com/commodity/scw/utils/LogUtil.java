package com.commodity.scw.utils;

import android.util.Log;

/**
 * 作者：liyushen on 2016/9/12 11:12
 * 功能：对日志进行管理
 * 在DeBug模式开启，其它模式关闭
 */
public class LogUtil {
    /**
     * 是否开启debug
     */
    private static LogUtil instance;
    public boolean isDebug = true;

    public static LogUtil instance() {
        if (instance == null) {
            instance = new LogUtil();
        }
        return instance;
    }

    /**
     * 错误
     * Write By LILIN
     * 2014-5-8
     *
     * @param clazz
     * @param msg
     */
    public void e(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.e(clazz.getSimpleName(), msg + "");
        }
    }

    /**
     * 信息
     * Write By LILIN
     * 2014-5-8
     *
     * @param clazz
     * @param msg
     */
    public  void i(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.i(clazz.getSimpleName(), msg + "");
        }
    }

    /**
     * 警告
     * Write By LILIN
     * 2014-5-8
     *
     * @param clazz
     * @param msg
     */
    public  void w(Class<?> clazz, String msg) {
        if (isDebug) {
            Log.w(clazz.getSimpleName(), msg + "");
        }
    }
}