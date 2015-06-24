package com.syncworks.slightapp.util;

import android.util.Log;

/**
 * Created by vosami on 2015-06-24.
 * Log 메시지
 */
public final class Logger {
    private final static boolean DEBUG = true;

    // Verbose 메시지
    public static void v(Object o, String message, Object... args) {
        if (DEBUG) {
            String name = o.getClass().getSimpleName();
            Log.v(name, formatString(message, args));
        }
    }
    // Debug 메시지
    public static void d(Object o, String message, Object... args) {
        if (DEBUG) {
            String name = o.getClass().getSimpleName();
            Log.d(name, formatString(message, args));
        }
    }
    // Info 메시지
    public static void i(Object o, String message, Object... args) {
        if (DEBUG) {
            String name = o.getClass().getSimpleName();
            Log.i(name, formatString(message, args));
        }
    }
    // Warning 메시지
    public static void w(Object o, String message, Object... args) {
        if (DEBUG) {
            String name = o.getClass().getSimpleName();
            Log.w(name, formatString(message, args));
        }
    }
    // Error 메시지
    public static void e(Object o, String message, Object... args) {
        if (DEBUG) {
            String name = o.getClass().getSimpleName();
            Log.e(name, formatString(message, args));
        }
    }

    protected static String formatString(String message, Object... args) {
        // If no varargs are supplied, treat it as a request to log the string without formatting.
        String retVal = message;
        for (int i=0;i<args.length;i++) {
            retVal = retVal + " "+ (i+1) + ":" + args[i].toString();
        }
        return retVal;
    }
}
