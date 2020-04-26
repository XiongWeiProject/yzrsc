package com.commodity.scw.ui.activity.personalcenter.money;

import android.view.View;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.ui.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 编辑银行卡
 * Created by yangxuqiang on 2017/4/3.
 */

public class EditCard extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Override
    protected int getContentView() {
        return R.layout.activity_editcard;
    }

    @Override
    protected void initView() {
        title.setText("修改银行卡");

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.add_card,R.id.head_back})
    public void onClik(View v){
        switch (v.getId()){
            case R.id.add_card://添加银行卡
                jumpActivity(AddCard.class);
                break;
            case R.id.head_back:
                finish();
                break;
        }
    }
}
