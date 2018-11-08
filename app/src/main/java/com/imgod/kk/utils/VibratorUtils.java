package com.imgod.kk.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * VibratorUtils.java
 *
 * @author imgod1
 * @version 2.0.0 2018/11/8 11:54
 * @update imgod1 2018/11/8 11:54
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class VibratorUtils {
    private static Vibrator vibrator;

    //震动1秒
    public static void startVibrator(Context context) {
        startVibrator(context, 1000);
    }

    //震动指定时间
    public static void startVibrator(Context context, long time) {
        long[] patter = {time};
        startVibrator(context, patter);
    }

    //震动一个数组
    public static void startVibrator(Context context, long[] patter) {
        if (null == vibrator) {
            vibrator = (Vibrator) context.getApplicationContext().getSystemService(context.VIBRATOR_SERVICE);
        }
        vibrator.vibrate(patter, 0);//0 代表重复 1代表不重复
    }

    public static void stopVibrator() {
        if (null != vibrator) {
            vibrator.cancel();
        }
    }
}
