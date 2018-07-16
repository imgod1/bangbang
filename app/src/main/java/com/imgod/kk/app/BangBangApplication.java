package com.imgod.kk.app;

import android.app.Application;
import android.content.Context;

import com.imgod.kk.utils.LogUtils;
import com.tencent.bugly.Bugly;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * BangBangApplication.java是液总汇的类。
 *
 * @author imgod1
 * @version 2.0.0 2018/5/28 10:31
 * @update imgod1 2018/5/28 10:31
 * @updateDes
 * @include {@link }
 * @used {@link }
 */
public class BangBangApplication extends Application {
    private static final String TAG = "BangBangApplication";
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initOkHttp();
        initBugly();
    }
    private void initBugly() {
        Bugly.init(getApplicationContext(), "057f96dda9", false);
    }
    private void initOkHttp() {
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext())) {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                cookieStore.put(url, cookies);
                cookieStore.put(HttpUrl.parse("1hengchang.com"), cookies);
                for (Cookie cookie : cookies) {
                    LogUtils.e(TAG, "save cookie Name:" + cookie.name());
                    LogUtils.e(TAG, "save cookie Value:" + cookie.value());
                }
                super.saveFromResponse(url, cookies);
            }

            @Override
            public synchronized List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(HttpUrl.parse("www.mf178.cn"));

                if (null != cookies && cookies.size() > 0) {
                    for (Cookie cookie : cookies) {
                        LogUtils.e(TAG, "load cookie Name:" + cookie.name());
                        LogUtils.e(TAG, "load cookie Value:" + cookie.value());
                    }
                } else {
                    LogUtils.e(TAG, "没加载到cookie");
                }

//                return super.loadForRequest(url);
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }

        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .cookieJar(cookieJar)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        //为所有的网络请求统一添加header
                        Request.Builder rb;
                        rb = original.newBuilder().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

                        Request request = rb.build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(new LoggerInterceptor("mifeng"))
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }


}
