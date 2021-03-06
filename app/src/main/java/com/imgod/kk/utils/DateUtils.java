package com.imgod.kk.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateUtils.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/7/9 16:49
 * @update imgod1 2018/7/9 16:49
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class DateUtils {
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE_TIME_ALL_NUMBER = "yyyyMMddHHmmss";

    public static String getFormatDateTimeFromMillSecons(long millSeconds) {
        Date date = new Date(millSeconds);
        DateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME);
        String dateStr = df.format(date);
        return dateStr;
    }

    public static String getFormatDateTimeFrom(Date date) {
        DateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME_ALL_NUMBER);
        String dateStr = df.format(date);
        return dateStr;
    }

    public static String reFormat(String date, String f1, String f2) {
        SimpleDateFormat df1 = new SimpleDateFormat(f1);
        SimpleDateFormat df2 = new SimpleDateFormat(f2);
        Date d1 = new Date();
        try {
            d1 = df1.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return df2.format(d1);
    }
}
