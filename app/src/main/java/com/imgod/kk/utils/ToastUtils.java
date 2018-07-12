package com.imgod.kk.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ToastUtils.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/5/26 16:23
 * @update imgod1 2018/5/26 16:23
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class ToastUtils {
    private static Toast mToast;

    public static void showToastShort(Context context, String content) {
        if (null == mToast) {
            mToast = new Toast(context);
        }
        mToast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
