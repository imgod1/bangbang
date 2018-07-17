package com.imgod.kk;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imgod.kk.app.Constants;
import com.imgod.kk.response_model.BaseResponse;
import com.imgod.kk.response_model.GetTaskResponse;
import com.imgod.kk.utils.BitmapUtils;
import com.imgod.kk.utils.DateUtils;
import com.imgod.kk.utils.GsonUtil;
import com.imgod.kk.utils.LogUtils;
import com.imgod.kk.utils.MediaPlayUtils;
import com.imgod.kk.utils.SPUtils;
import com.imgod.kk.utils.ScreenUtils;
import com.imgod.kk.utils.StringUtils;
import com.imgod.kk.utils.ToastUtils;
import com.imgod.kk.views.RowView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    public static final int RUSH_MODEL_NOT_RUSH = 0;//不抢购
    public static final int RUSH_MODEL_RUSH = 1;//抢购
    private int rush_model = RUSH_MODEL_NOT_RUSH;


    public static final int TYPE_NORMAL = 0x00;//正常登陆
    public static final int TYPE_RELOGIN = 0x01;//进来跳转到登陆页
    private int come_type;

    public static void actionStart(Context context) {
        actionStart(context, TYPE_NORMAL);
    }

    public static void actionStart(Context context, int come_type) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("come_type", come_type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvent();
    }

    private void initEvent() {
        rview_province.setOnClickListener(this);
        rview_amount.setOnClickListener(this);
        tv_action_1.setOnClickListener(this);
        tv_action_2.setOnClickListener(this);
        tv_get_mobile_number.setOnClickListener(this);
    }

    Toolbar toolbar;
    private RowView rview_province;
    private RowView rview_amount;

    private View item_order;
    private TextView tv_phone_number;
    private TextView tv_province;
    private TextView tv_amount;
    private TextView tv_id;
    private TextView tv_date;
    private TextView tv_action_1;
    private TextView tv_action_2;
    private ProgressBar progress_bar;

    private TextView tv_get_mobile_number;

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rview_province = findViewById(R.id.rview_province);
        rview_amount = findViewById(R.id.rview_amount);
        toolbar.setTitle(R.string.app_name);

        item_order = findViewById(R.id.item_order);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_province = findViewById(R.id.tv_province);
        tv_amount = findViewById(R.id.tv_amount);
        tv_id = findViewById(R.id.tv_id);
        tv_date = findViewById(R.id.tv_date);
        tv_action_1 = findViewById(R.id.tv_action_1);
        tv_action_2 = findViewById(R.id.tv_action_2);

        progress_bar = findViewById(R.id.progress_bar);
        tv_get_mobile_number = findViewById(R.id.tv_get_mobile_number);
        rview_province.setTitle("省份");
        rview_amount.setTitle("面额");
        setRowViewContent();
    }


    //获取平台上现在的订单量
    public static final String ORDER_LIST_URL = "http://bang.1hengchang.com/bang-front/topuporder/listpage?applCode=mobilefee";

    private RequestCall requestPlatformOrderSizeCall;


    private void requestPlatformOrderSize() {
        if (rush_model == RUSH_MODEL_RUSH) {
            toolbar.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestPlatformOrderSizeCall = OkHttpUtils.get().url(ORDER_LIST_URL).build();
                    requestPlatformOrderSizeCall.execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (!call.isCanceled()) {
                                requestPlatformOrderSize();
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {//
                            parseOrderSizeResponse(response);
                        }
                    });
                }
            }, 500);
        }
    }

    /**
     * 解析网络请求得到的数据
     */
    private void parseOrderSizeResponse(String content) {
        if (!parseListOrderFromesponse(content)) {//如果当前没有历史订单的话 再去执行抢单的逻辑
            Document document = null;
            try {
                document = Jsoup.parse(content);
                Elements elements = document.getElementsByClass("oder-item");
                Element element = elements.select("ul").first();
                Elements liElements = element.select("li");
                for (int i = 0; i < liElements.size(); i++) {
                    Element tempElement = liElements.get(i);
                    String techphoneChargeName = tempElement.attr("data-parval");
                    LogUtils.e(TAG, "parseOrderSizeResponse:techphoneChargeName: " + techphoneChargeName);
                    LogUtils.e(TAG, "parseOrderSizeResponse: mRequestAmount:" + mRequestAmount);
                    if (techphoneChargeName.trim().equals("" + mRequestAmount)) {
                        int orderNum = getTelephoneChargeOrderNum(tempElement.text());
                        LogUtils.e(TAG, techphoneChargeName);
                        LogUtils.e(TAG, "数量:" + orderNum);

                        if (orderNum > 0) {
                            //如果该选项还有剩余订单的话,那这个时候应该先发起抢订单的操作
                            LogUtils.e(TAG, techphoneChargeName + "话费单有库存,请及时去抢单");
                            requestGetTask("" + mRequestAmount, mRequestProvince, "1");
                        } else {
                            //如果没有数量 那就应该执行刷新操作了
                            requestPlatformOrderSize();
                        }
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 解析网络请求得到的数据 得到现在已经存在的订单
     */
    private boolean parseListOrderFromesponse(String content) {
        Document document = null;
        try {
            document = Jsoup.parse(content);
            Element element = document.getElementById("sendOrderListPanel");
            Elements trElements = element.select("tr");
            if (null != trElements && trElements.size() > 0) {//大于0说明有历史订单
                Element tempElement = trElements.get(0);

                orderDataBean = new GetTaskResponse.DataBean();

                String id = tempElement.attr("data-orderseq");
                String amount = tempElement.attr("data-parval");
                String mobile = tempElement.attr("data-mobile");
                String provinceName = tempElement.attr("data-provincename");
                String endTime = null;
                Elements endTimeElements = tempElement.getElementsByClass("endTime");
                if (null != endTimeElements && endTimeElements.size() > 0) {
                    endTime = endTimeElements.get(0).text();
                }
                orderDataBean.setId(id);
                orderDataBean.setAmount(Double.parseDouble(amount));
                orderDataBean.setMobile(mobile);
                orderDataBean.setProvinceName(provinceName);
                orderDataBean.setExpireTime(endTime);
                setItemViewByModel();
                return true;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private String mRequestProvince = Constants.PROVINCE_ALL;
    private int mRequestAmount = 50;
    private RequestCall requestGetTaskCall;
    //真正请求获取任务

    private void requestGetTask(String amount, String province, String count) {
        requestGetTaskCall = OkHttpUtils.post().url(API.GET_TASK_URL)
                .addParams("parval", amount)
                .addParams("appCode", "mobilefee")
                .addParams("province", province)
                .addParams("count", count)
                .build();
        requestGetTaskCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                requestPlatformOrderSize();
            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.e(TAG, "requestGetTask onResponse: " + response);
                parseGetTaskResponse(response);
            }
        });
    }

    GetTaskResponse.DataBean orderDataBean;//获取到的订单信息

    private void parseGetTaskResponse(String response) {
        BaseResponse baseResponse = GsonUtil.GsonToBean(response, BaseResponse.class);
        if (baseResponse.getStatus().equals(Constants.REQUEST_STATUS.SUCCESS)) {
            GetTaskResponse getTaskResponse = GsonUtil.GsonToBean(response, GetTaskResponse.class);
            List<GetTaskResponse.DataBean> orderList = getTaskResponse.getData();
            if (null != orderList && orderList.size() > 0) {
                orderDataBean = orderList.get(0);
                setItemViewByModel();
                showGetOrderSuccessDialog();
            }

        } else {
            if (baseResponse.getMsg().contains("下单太频繁,休息会儿再来")) {
                ToastUtils.showToastShort(mContext, baseResponse.getMsg());
            } else {
                requestPlatformOrderSize();
            }
        }
    }

    //通过model设置item的view
    private void setItemViewByModel() {
        rush_model = RUSH_MODEL_NOT_RUSH;
        setBottomViewStatus();
        item_order.setVisibility(View.VISIBLE);

        tv_phone_number.setText(orderDataBean.getMobile());
        tv_province.setText(orderDataBean.getProvinceName());
        tv_amount.setText("" + orderDataBean.getParval());
        tv_id.setText("订单号:" + orderDataBean.getId());
        if (!TextUtils.isEmpty(orderDataBean.getExpireTime())) {
            if (orderDataBean.getExpireTime().contains("-") || orderDataBean.getExpireTime().contains(":")) {
                tv_date.setText("截止时间:" + orderDataBean.getExpireTime());
            } else {
                tv_date.setText("截止时间:" + DateUtils.reFormat(orderDataBean.getExpireTime(), DateUtils.FORMAT_DATE_TIME_ALL_NUMBER, DateUtils.FORMAT_DATE_TIME));
            }
        }
        tv_action_1.setText("我已充值");
        tv_action_2.setText("我没充值");
        tv_action_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToastShort(mContext, "选择凭证");
                choosePhotoWithPermissionCheck();
            }
        });

        tv_action_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestReportTaskFailed(orderDataBean.getId());
            }
        });
    }


    private AlertDialog getOrderSuccessDialog;

    private void showGetOrderSuccessDialog() {
        if (null == getOrderSuccessDialog) {
            getOrderSuccessDialog = new AlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setMessage("成功获取到一条订单,请及时充值")
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getOrderSuccessDialog.dismiss();
                        }
                    })
                    .create();

            getOrderSuccessDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    MediaPlayUtils.stopPlay();
                }
            });
        }

        if (!getOrderSuccessDialog.isShowing()) {
            getOrderSuccessDialog.show();
            if (SPUtils.getInstance().getBoolean(SettingActivity.SP_MUSIC, true)) {
                MediaPlayUtils.playSound(mContext, "memeda.wav");
            }
        }
    }


    //获取话费单还剩多少
    private int getTelephoneChargeOrderNum(String text) {
        LogUtils.e(TAG, "getTelephoneChargeOrderNum: text:" + text);
        if (!TextUtils.isEmpty(text)) {
            //10元面值话费 9.5折 剩余0单 获取10元订单
            int startPosition = text.indexOf("剩余") + 2;
            int endPosition = text.indexOf("单");
            return Integer.parseInt(text.substring(startPosition, endPosition));
        }
        return 0;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayUtils.stopPlay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                SettingActivity.actionStart(mContext);
//                choosePhotoWithPermissionCheck();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private RequestCall requestReportTaskSuccessFirstCheckUserStatusCall;

    //上报充值结果成功前的校验身份
    public void requestReportTaskSuccessFirstCheckUserStatus() {
        requestReportTaskSuccessFirstCheckUserStatusCall = OkHttpUtils.post().url(API.REPORT_SUCCESS_CHECK_USER_STATUS)
                .build();
        requestReportTaskSuccessFirstCheckUserStatusCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToastShort(mContext, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                BaseResponse baseResponse = GsonUtil.GsonToBean(response, BaseResponse.class);
                if (Constants.REQUEST_STATUS.SUCCESS.equals(baseResponse.getStatus())) {
                    //如果用户校验结果成功的话 那才去执行真正报单的逻辑
                    requestReportTaskSuccess(orderDataBean.getId(), mFileName, mFile);
                } else {
                    ToastUtils.showToastShort(mContext, baseResponse.getMsg());
                }
            }
        });
    }


    private RequestCall requestReportTaskSuccessCall;

    //上报充值结果成功前的校验身份
    public void requestReportTaskSuccess(String id, String fileName, File file) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("orderSeq", id);
        hashMap.put("submitStatus", "00");
        requestReportTaskSuccessCall = OkHttpUtils.post().url(API.REPORT_SUCCESS_API)
                .addFile("file", fileName, file)
                .params(hashMap)
                .build();
        requestReportTaskSuccessCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToastShort(mContext, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                BaseResponse baseResponse = GsonUtil.GsonToBean(response, BaseResponse.class);
                if (Constants.REQUEST_STATUS.SUCCESS.equals(baseResponse.getStatus())) {
                    item_order.setVisibility(View.GONE);
                    ToastUtils.showToastShort(mContext, "报单成功");
                } else {
                    ToastUtils.showToastShort(mContext, baseResponse.getMsg());
                }
            }
        });
    }


    private RequestCall requestReportCall;

    //上报充值结果 我没充
    public void requestReportTaskFailed(String id) {
        requestReportCall = OkHttpUtils.post().url(API.REPORT_FAILED_API)
                .addParams("orderSeq", id)
                .build();
        requestReportCall.execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToastShort(mContext, e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                BaseResponse baseResponse = GsonUtil.GsonToBean(response, BaseResponse.class);
                if (Constants.REQUEST_STATUS.SUCCESS.equals(baseResponse.getStatus())) {
                    item_order.setVisibility(View.GONE);
                    ToastUtils.showToastShort(mContext, "订单取消成功");
                } else {
                    ToastUtils.showToastShort(mContext, baseResponse.getMsg());
                }
            }
        });
    }


    private void choosePhotoWithPermissionCheck() {
        //第二个参数是需要申请的权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE_WRITE_STORAGE);
        } else {
            //权限已经被授予，在这里直接写要执行的相应方法即可
            choosePhoto();
        }
    }

    private static final int REQUEST_PERMISSION_CODE_WRITE_STORAGE = 0x01;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE_WRITE_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto();
            } else {
                // Permission Denied
                ToastUtils.showToastShort(mContext, "选择照片的权限被拒绝.无法选择");
            }
        }
    }

    private void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    private static final int REQUEST_CODE_PICK_IMAGE = 0x00;

    private String mFileName;
    private File mFile;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == RESULT_OK && null != data) {
                Uri uri = data.getData();
                try {
                    Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    mFile = new File(mContext.getCacheDir(), DateUtils.getFormatDateTimeFrom(new Date()) + ".jpg");
                    BitmapUtils.compressBitmapToFile(bit, mFile, 20);
                    mFileName = StringUtils.getFileNameFromPath(mFile.getAbsolutePath());
                    requestReportTaskSuccessFirstCheckUserStatus();
                } catch (FileNotFoundException e) {
                    ToastUtils.showToastShort(mContext, "压缩图片时发生异常");
                    e.printStackTrace();
                }
            } else {
                ToastUtils.showToastShort(mContext, "取消选择图片");
            }
        }
    }


    private BottomSheetDialog amountDialog;
    private View amountDialogView;
    private TextView tv_10;
    private TextView tv_20;
    private TextView tv_30;
    private TextView tv_50;
    private TextView tv_100;
    private TextView tv_200;
    private TextView tv_300;
    private TextView tv_500;

    //选择话费面额的对话框
    private void showSelectAmountDialog() {
        if (null == amountDialog) {
            amountDialog = new BottomSheetDialog(mContext);
            amountDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_amount, null, false);
            tv_10 = amountDialogView.findViewById(R.id.tv_10);
            tv_20 = amountDialogView.findViewById(R.id.tv_20);
            tv_30 = amountDialogView.findViewById(R.id.tv_30);
            tv_50 = amountDialogView.findViewById(R.id.tv_50);
            tv_100 = amountDialogView.findViewById(R.id.tv_100);
            tv_200 = amountDialogView.findViewById(R.id.tv_200);
            tv_300 = amountDialogView.findViewById(R.id.tv_300);
            tv_500 = amountDialogView.findViewById(R.id.tv_500);


            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String) v.getTag();
                    if (!TextUtils.isEmpty(tag)) {
                        mRequestAmount = Integer.parseInt(tag);
                        amountDialog.dismiss();
                        setRowViewContent();
                    }
                }
            };

            tv_10.setOnClickListener(onClickListener);
            tv_20.setOnClickListener(onClickListener);
            tv_30.setOnClickListener(onClickListener);
            tv_50.setOnClickListener(onClickListener);
            tv_100.setOnClickListener(onClickListener);
            tv_200.setOnClickListener(onClickListener);
            tv_300.setOnClickListener(onClickListener);
            tv_500.setOnClickListener(onClickListener);

            amountDialog.setContentView(amountDialogView);
        }
        if (null != amountDialog && !amountDialog.isShowing()) {
            amountDialog.show();
        }
    }


    private BottomSheetDialog provinceDialog;
    private View provinceDialogView;
    private RecyclerView dialog_recylerview;

    //选择省份的对话框
    private void showSelectProvinceDialog() {
        if (null == provinceDialog) {
            provinceDialog = new BottomSheetDialog(mContext);
            provinceDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_list, null, false);
            dialog_recylerview = provinceDialogView.findViewById(R.id.dialog_recylerview);
            dialog_recylerview.setLayoutManager(new LinearLayoutManager(mContext));
            CommonAdapter<String> commonAdapter = new CommonAdapter<String>(mContext, R.layout.item_province, Arrays.asList(Constants.PROVINCE_ARRAY)) {
                @Override
                protected void convert(ViewHolder holder, String s, int position) {
                    holder.setText(R.id.tv_title, s);
                }
            };
            commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    String result = Constants.PROVINCE_ARRAY[position];
                    if (result.equals("不限")) {
                        mRequestProvince = Constants.PROVINCE_ALL;
                    } else {
                        mRequestProvince = result;
                    }
                    provinceDialog.dismiss();
                    setRowViewContent();
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            dialog_recylerview.setAdapter(commonAdapter);

            provinceDialog.setContentView(provinceDialogView);

            //设置对话框的高度
            View parent = (View) provinceDialogView.getParent();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
            params.height = ScreenUtils.getScreenHeight(mContext) / 2;
            parent.setLayoutParams(params);

        }
        if (null != provinceDialog && !provinceDialog.isShowing()) {
            provinceDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rview_province:
                showSelectProvinceDialog();
                break;
            case R.id.rview_amount:
                showSelectAmountDialog();
                break;
            case R.id.tv_get_mobile_number:
                if (rush_model == RUSH_MODEL_RUSH) {
                    rush_model = RUSH_MODEL_NOT_RUSH;
                } else {
                    rush_model = RUSH_MODEL_RUSH;
                }
                setBottomViewStatus();
                requestPlatformOrderSize();
                break;
        }
    }

    private void setBottomViewStatus() {
        if (rush_model == RUSH_MODEL_RUSH) {
            tv_get_mobile_number.setText("正在获取号码...");
            progress_bar.setVisibility(View.VISIBLE);
        } else {
            tv_get_mobile_number.setText("获取号码");
            progress_bar.setVisibility(View.GONE);
        }
    }


    private void setRowViewContent() {
        if (mRequestProvince.equals(Constants.PROVINCE_ALL)) {
            rview_province.setContent("不限");
        } else {
            rview_province.setContent(mRequestProvince);
        }
        rview_amount.setContent("" + mRequestAmount);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int come_type = intent.getIntExtra("come_type", TYPE_NORMAL);
        if (come_type == TYPE_RELOGIN) {
            LoginActivity.actionStart(mContext);
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        exit();
    }

    private long exitTime;

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showToastShort(mContext, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
