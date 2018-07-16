package com.imgod.kk.utils;

import android.content.Context;

/**
 * ScreenUtils.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/7/10 14:59
 * @update imgod1 2018/7/10 14:59
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class ScreenUtils {
    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }
}
