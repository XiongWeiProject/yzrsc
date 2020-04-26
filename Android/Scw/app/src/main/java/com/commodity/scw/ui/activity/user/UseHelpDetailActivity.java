package com.commodity.scw.ui.activity.user;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.widget.webview.CustomWebView;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/4/2 15:44
 * 交易流程 如何购买商品  提现规则  买家等级  卖家等级
 */
public class UseHelpDetailActivity extends BaseActivity{
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.tv_text1)
    TextView tv_text1;
    @Bind(R.id.tv_textinfo)
    TextView tv_textinfo;
    @Bind(R.id.customwebview)
    CustomWebView customwebview;

    @Override
    protected int getContentView() {
        return R.layout.activity_use_help_detail;
    }

    @Override
    protected void initView() {
        if (getIntent().getExtras().containsKey("title")){
            tv_title.setText(getIntent().getExtras().getString("title"));
            tv_text1.setText(getIntent().getExtras().getString("title"));
        }
        if (getIntent().getExtras().containsKey("content")){
            tv_textinfo.setText(getIntent().getExtras().getString("content"));
            customwebview.loadHtmlStrData(getIntent().getExtras().getString("content"));
        }
    }

    @Override
    protected void initListeners() {

    }
}
