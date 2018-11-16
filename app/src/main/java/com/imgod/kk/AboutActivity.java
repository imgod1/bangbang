package com.imgod.kk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imgod.kk.utils.AppUtils;
import com.imgod.kk.utils.QQUtils;
import com.imgod.kk.utils.ToastUtils;

/**
 * 项目名称：KnowGirl
 * 类描述：关于界面
 * 创建人：imgod
 * 创建时间：2016/4/24 16:20
 * 修改人：imgod
 * 修改时间：2016/4/24 16:20
 * 修改备注：
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {
    Toolbar toolbar;
    LinearLayout llayout_about;
    TextView txt_about_app;
    TextView txt_qq;
    TextView versionName;

    private Toast toast;

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, AboutActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initEvent();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    private void goQQ2Chat() {
        try {
            QQUtils.goQQ2Chat(AboutActivity.this, getString(R.string.my_qq));
        } catch (Exception e) {
            ToastUtils.showToastShort(AboutActivity.this, "发起QQ聊天发生异常");
        }
    }

    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        llayout_about = findViewById(R.id.llayout_about);
        txt_about_app = findViewById(R.id.txt_about_app);
        txt_qq = findViewById(R.id.txt_qq);
        versionName = findViewById(R.id.versionName);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        String qqTitle = "QQ:" + getString(R.string.my_qq);
        txt_qq.setText(qqTitle);
        String versionTitle = "版本:" + AppUtils.getVersionName(getApplicationContext());
        versionName.setText(versionTitle);
    }


    public void initEvent() {
        llayout_about.setOnClickListener(this);
        txt_about_app.setOnClickListener(this);
        txt_qq.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_qq:
                goQQ2Chat();
                break;
        }
    }
}
