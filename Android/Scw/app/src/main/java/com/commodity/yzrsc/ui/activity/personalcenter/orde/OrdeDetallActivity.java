package com.commodity.yzrsc.ui.activity.personalcenter.orde;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 订单详情 -卖家
 * Created by yangxuqiang on 2017/4/2.
 */

public class OrdeDetallActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.orde_content)
    LinearLayout orde_content;
    @Bind(R.id.orde_button_send)
    Button orde_button_send;
//    @Bind(R.id.orde_button_shouhou)
//    Button orde_button_shouhou;
    private int ordeState;


    @Override
    protected int getContentView() {
        return R.layout.activity_ordedetall;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ordeState = bundle.getInt("ordeState");


        switch (ordeState){
            case 0://全部
                break;
            case 1://代付款
                orde_button_send.setVisibility(View.GONE);
                break;
            case 2://代发货
                orde_button_send.setVisibility(View.VISIBLE);
                orde_button_send.setText("发货");
                break;
            case 3://已发货
                orde_button_send.setVisibility(View.VISIBLE);
                orde_button_send.setText("查看物流");
                break;
            case 4://交易完成
                orde_button_send.setVisibility(View.GONE);
//                orde_button_shouhou.setVisibility(View.GONE);
                break;
            case 5://退货

                break;
        }

        title.setText("订单详情");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin= ScreenUtils.dp2px(mContext,10);

        orde_content.addView(getItemMenu("订单时间","2015-12-12 12:12:12",false));
        orde_content.addView(getItemMenu("订单号","1111111111",false));
        orde_content.addView(getItemMenu("订单状态","买家已付款，等待卖",true));
        orde_content.addView(getItemMenu("转售方","小李",false),layoutParams);
        orde_content.addView(getItemMenu("买家","翡翠园",false));
        orde_content.addView(getItemMenu("宝贝价格","¥ 1920",true));

        orde_content.addView(getItemMenu("快递公司","德邦物流",false),layoutParams);
        orde_content.addView(getItemMenu("收货人","李思思",false));
        orde_content.addView(getItemMenu("联系方式","13123454332",false));
        orde_content.addView(getItemMenu("收货地址","海市浦东新区东方路111号",false));

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.orde_button_send})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.head_back://返回
                finish();
                break;
            case R.id.orde_button_send://发货
                if(ordeState==2){//发货

                }else if(ordeState==3){//查看物流
//                    jumpActivity(SeeWuliu.class);
                }
                break;
//            case R.id.orde_button_shouhou://售后服务
//                break;
            default:
                break;
        }
    }

    public View getItemMenu(String nameText,String valueText,boolean read) {
        View inflate = View.inflate(mContext, R.layout.item_orde_content, null);
        TextView name = (TextView) inflate.findViewById(R.id.orde_item_name);
        TextView value = (TextView) inflate.findViewById(R.id.orde_item_value);

        name.setText(nameText);
        value.setText(valueText);
        if(read){
            value.setTextColor(Color.parseColor("#BA0008"));
        }
        return inflate;
    }
}
