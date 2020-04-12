package com.acorn.downloadsimulator;

import android.util.Log;

import java.util.Date;

/**
 * Created by acorn on 2020/4/11.
 */
public class LogUtil {
    private static final String TAG = "beaver";

    public static void i(String msg) {
        System.out.println(Thread.currentThread().getName() + " " + new Date(System.currentTimeMillis()).toString() + ":" + msg);
    }

    public static void e(String msg) {
        System.out.println(Thread.currentThread().getName() + " " + new Date(System.currentTimeMillis()).toString() + ":" + "Error!! " + msg);
    }
}
