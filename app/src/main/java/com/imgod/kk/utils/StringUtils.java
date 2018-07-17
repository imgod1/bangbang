package com.imgod.kk.utils;

import android.text.TextUtils;

/**
 * StringUtils.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/7/17 10:38
 * @update imgod1 2018/7/17 10:38
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class StringUtils {
    private static final String TAG = "StringUtils";

    public static String getFileNameFromPath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return null;
        }

        int startPosition = filePath.lastIndexOf("/") + 1;
        int endPosition = filePath.length();
        String result = filePath.substring(startPosition, endPosition);
        LogUtils.e(TAG, "getFileNameFromPath: result:" + result);
        return result;

    }
}
