package com.commodity.scw.ui.activity.personalcenter.money;

import android.view.View;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.ui.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yangxuqiang on 2017/6/13.
 */

public class Tixiansuccess extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.head_text_right)
    TextView head_text_right;
    @Override
    protected int getContentView() {
        return R.layout.activity_tixiansuccess;
    }

    @Override
    protected void initView() {
        title.setText("");

        head_text_right.setText("提现记录");
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_text_right,R.id.tixian_success})
    public void click(View v){
        switch (v.getId()){
            case R.id.head_back:
            case R.id.tixian_success:
                finish();
                break;
            case R.id.head_text_right:
                jumpActivity(TixianJiluActivity.class);
                finish();
                break;
        }
    }
}
