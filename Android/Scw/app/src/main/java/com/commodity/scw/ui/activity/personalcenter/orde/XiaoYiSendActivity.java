package com.commodity.scw.ui.activity.personalcenter.orde;

import android.view.View;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.ui.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 已发货
 * Created by yangxuqiang on 2017/4/4.
 */

public class XiaoYiSendActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Override
    protected int getContentView() {
        return R.layout.xiao_yigoods;
    }

    @Override
    protected void initView() {
        title.setText("订单详情");

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
        }
    }
}
