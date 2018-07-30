package com.imgod.kk.utils;

import android.util.Log;

import com.imgod.kk.BuildConfig;

import static android.content.ContentValues.TAG;

/**
 * @author imgod1
 * @version 2.0.0 2018/5/26 14:42
 * @update imgod1 2018/5/26 14:42
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class LogUtils {
    public static boolean DEBUG = false;

    /**
     * 设置日志的最长内容
     */
    private static int LOG_MAXLENGTH = 4058;

    /**
     * 日志 方便隐藏
     *
     * @param tag 标签
     * @param msg 内容
     * @update imgod 修改重复输出问题 2017年10月11日 13:00:16
     */
    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG || LogUtils.DEBUG) {
            if (msg.length() > LOG_MAXLENGTH) {
                int strLength = msg.length();
                int start = 0;
                int end = LOG_MAXLENGTH;
                for (int i = 0; i < 100; i++) {
                    //剩下的文本还是大于规定长度则继续重复截取并输出
                    if (strLength > end) {
                        Log.e(TAG + i, msg.substring(start, end));
                        start = end;
                        end = end + LOG_MAXLENGTH;
                    } else {
                        Log.e(TAG, msg.substring(start, strLength));
                        break;
                    }
                }
            } else {
                Log.e(tag, msg);
            }
        }
    }

    /**
     * 日志 方便隐藏
     *
     * @param tag 标签
     * @param msg 内容
     * @update imgod 修改重复输出问题 2017年10月11日 13:00:16
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG || LogUtils.DEBUG) {
            if (msg.length() > LOG_MAXLENGTH) {
                int strLength = msg.length();
                int start = 0;
                int end = LOG_MAXLENGTH;
                for (int i = 0; i < 100; i++) {
                    //剩下的文本还是大于规定长度则继续重复截取并输出
                    if (strLength > end) {
                        Log.d(TAG + i, msg.substring(start, end));
                        start = end;
                        end = end + LOG_MAXLENGTH;
                    } else {
                        Log.d(TAG, msg.substring(start, strLength));
                        break;
                    }
                }
            } else {
                Log.d(tag, msg);
            }
        }
    }
}
