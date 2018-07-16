package com.imgod.kk.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imgod.kk.R;

public class RowView extends LinearLayout {
    public RowView(Context context) {
        super(context);
        initViews();
    }

    public RowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public RowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private TextView tv_title;
    private TextView tv_content;

    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_row, this);
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
    }

    public void setTitle(String title) {
        if (null != tv_title) {
            tv_title.setText(title);
        }
    }

    public void setContent(String content) {
        if (null != tv_title) {
            tv_content.setText(content);
        }
    }

    public void setTitleAndContent(String title, String content) {
        setTitle(title);
        setContent(content);
    }

}
