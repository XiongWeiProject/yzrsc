package com.commodity.yzrsc.ui.activity.personalcenter.money;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.friend.PayMoneNumActivity;
import com.commodity.yzrsc.ui.dialog.CommonDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包
 * Created by yangxuqiang on 2017/3/28.
 */

public class MyMoneyActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.head_back)
    LinearLayout headBack;
    @Bind(R.id.head_text_right)
    TextView headTextRight;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.qianbao_yue_line)
    LinearLayout qianbaoYueLine;
    @Bind(R.id.btn_pay)
    TextView btnPay;
    @Bind(R.id.qianbao_chongzhi_line)
    LinearLayout qianbaoChongzhiLine;
    @Bind(R.id.mymoney_tiqian)
    LinearLayout mymoneyTiqian;
    @Bind(R.id.mymoney_mingxi)
    LinearLayout mymoneyMingxi;

    @Override
    protected int getContentView() {
        return R.layout.activity_mymoney;
    }

    @Override
    protected void initView() {
        title.setText("我的钱包");
        sendRequest(0);
    }

    @Override
    protected void initListeners() {

    }

    @OnClick({R.id.head_back, R.id.mymoney_mingxi, R.id.mymoney_tiqian,R.id.btn_pay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mymoney_tiqian://体现管理
                jumpActivity(TixianManagerActivity.class);
                break;
            case R.id.mymoney_mingxi://钱包明细
                jumpActivity(MoneyDetailActivity.class);
                break;
            case R.id.qianbao_chongzhi_line://充值

            case R.id.head_back://返回
                finish();
                break;
            case R.id.btn_pay://充值
                jumpActivity(PayMoneNumActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void sendRequest(int tag) {
        super.sendRequest(tag);
        if (tag==0){
            HashMap<String, String> map = new HashMap<>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetWalletAmount,map,this );
            httpManager.request();
        }
    }
    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){
                JSONObject data = response.optJSONObject("data");
                tvMoney.setText("￥"+data.optString("avialableAmount")+"元");
//                tixian_dong.setText("￥"+data.optString("frozenAmount"));
            }
        }else {
            tip(response.optString("msg"));
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
}
