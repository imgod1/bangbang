package com.imgod.kk.app;

/**
 * Constants.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/7/6 16:56
 * @update imgod1 2018/7/6 16:56
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class Constants {
    public static final String MIFENG_KEY = "56124619";
    public static final String MIFENG_SECRET = "b4895e19198db5844f10d3b7d63cec41";
    public static String TOKEN = "";

    public interface REQUEST_STATUS {
        String SUCCESS = "succeed";
        String FAILED = "Failure";
    }

    //运营商类型
    public interface OPERATOR_TYPE {
        int DEFAULT = 0;//默认 不限制运营商
        int MOBILE = 1;//中国移动
        int UNICOM = 2;//中国联通
        int TELECOM = 3;//中国电信
    }

    //省份列表
    public static String[] PROVINCE_ARRAY = {
            "不限"
    };

    public static final String PROVINCE_ALL = "ALL";
    //省份列表
//    public static String[] PROVINCE_ARRAY = {
//            "不限",
//            "山东","江苏","四川","陕西","湖北","北京","天津",
//            "上海","广东","广西","浙江","河南","甘肃","吉林",
//            "辽宁","内蒙古","新疆","黑龙江","福建","河北","重庆",
//            "安徽","海南","江西","山西","湖南","青海","贵州","宁夏",
//            "云南","西藏","台湾","香港","澳门"
//    };

    //话费面额
    public static int[] AMOUNT_ARRAY = {
            10, 20, 30, 50, 100, 200, 300, 500
    };

    //报单的时候的充值类型
    public interface RECHARGE_TYPE {
        int SUCCESS = 1;//成功下单并充值成功
        int FAILED = 2;//下单失败 我没充 尽量让用户反复确认
    }

}
