package com.commodity.yzrsc.ui.activity.friend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.view.flowLayout.FlowLayout;
import com.commodity.yzrsc.view.flowLayout.TagAdapter;
import com.commodity.yzrsc.view.flowLayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayMoneNumActivity extends BaseActivity {


    @Bind(R.id.head_back)
    LinearLayout headBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_text_right)
    TextView headTextRight;

    @Bind(R.id.et_money)
    EditText etMoney;
    @Bind(R.id.btn_pay)
    TextView btnPay;
    List<String> tagList = new ArrayList<>();
    TagAdapter tagAdapter;
    @Bind(R.id.tag_money)
    TagFlowLayout tagMoney;

    @Override
    protected int getContentView() {
        return R.layout.activity_pay_mone_num;
    }

    @Override
    protected void initView() {
        headTitle.setText("充值");
        tagList.add("50");
        tagList.add("100");
        tagList.add("200");
        tagList.add("500");
        tagList.add("1000");
        tagList.add("2000");
        //类型列表
        tagAdapter = new TagAdapter<String>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(PayMoneNumActivity.this)
                        .inflate(R.layout.item_tag_money, tagMoney, false);
                tv.setText(s);
                return tv;
            }
        };
        tagMoney.setAdapter(tagAdapter);

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_pay)
    public void onViewClicked() {
    }
}
