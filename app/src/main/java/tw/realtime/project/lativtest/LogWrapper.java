package tw.realtime.project.lativtest;

import android.util.Log;

/**
 * Created by vexonelite on 2017/3/17.
 */
public class LogWrapper {


    public static void showLog(int mode, String tag, String message) {

        if ( (null == tag) || (null == message) ) {
            return;
        }

        switch (mode) {
            case Log.DEBUG: {
                Log.d(tag, message);
                break;
            }
            case Log.INFO: {
                Log.i(tag, message);
                break;
            }
            case Log.WARN: {
                Log.w(tag, message);
                break;
            }
            case Log.ERROR: {
                Log.e(tag, message);
                break;
            }

        }
    }

    public static void showLog(int mode, String tag, String message, Throwable throwable) {
        if ( (null == tag) || (null == message) || (null == throwable) ) {
            return;
        }

        switch (mode) {
            case Log.DEBUG: {
                Log.d(tag, message, throwable);
                break;
            }
            case Log.INFO: {
                Log.i(tag, message, throwable);
                break;
            }
            case Log.WARN: {
                Log.w(tag, message, throwable);
                break;
            }
            case Log.ERROR: {
                Log.e(tag, message, throwable);
                break;
            }
        }
    }

    public static void showLongLog(int mode, String tag, String message) {
        if ( (null == tag) || (null == message) ) {
            return;
        }
        int maxLogSize = 1000;
        for(int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            LogWrapper.showLog(mode, tag, message.substring(start, end));
        }
    }
}
