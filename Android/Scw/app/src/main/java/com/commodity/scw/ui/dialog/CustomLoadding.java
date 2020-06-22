package com.commodity.scw.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.commodity.yzrsc.R;


/**
 * 作者：liyushen on 2016/4/15 10:51
 * 功能：加载loadding
 */
public class CustomLoadding extends Dialog {
    TextView tv_tip;

    public CustomLoadding(Context context) {
        super(context, R.style.style_loaddingDialog);
    }

    public CustomLoadding(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_lodding);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
    }

    @Override
    public void show() {
        super.show();
    }

    public void defaultShow() {
        super.show();
        tv_tip.setText("正在加载...");
    }

    public void setTip(String tip) {
        this.show();
        tv_tip.setText(tip);
    }
}