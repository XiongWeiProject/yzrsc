package com.commodity.scw.ui.activity.personalcenter.money;

import android.view.View;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.ui.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的钱包
 * Created by yangxuqiang on 2017/3/28.
 */

public class MyMoneyActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Override
    protected int getContentView() {
        return R.layout.activity_mymoney;
    }

    @Override
    protected void initView() {
        title.setText("我的钱包");

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.mymoney_mingxi,R.id.mymoney_tiqian})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.mymoney_tiqian://体现管理
                jumpActivity(TixianManagerActivity.class);
                break;
            case R.id.mymoney_mingxi://钱包明细
                jumpActivity(MoneyDetailActivity.class);
                break;
            case R.id.head_back://返回
                finish();
                break;
            default:
                break;
        }
    }
}
