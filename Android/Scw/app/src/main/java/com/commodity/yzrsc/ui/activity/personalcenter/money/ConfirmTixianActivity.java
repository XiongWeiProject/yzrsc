package com.commodity.yzrsc.ui.activity.personalcenter.money;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.ui.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 * 接受验证码，确认提现
 * Created by yangxuqiang on 2017/4/3.
 */

public class ConfirmTixianActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.confirm_edit_yzm)
    EditText confirm_edit_yzm;
    @Bind(R.id.confirm_text_phone)
    TextView confirm_text_phone;
    @Bind(R.id.confirm_button_y_z_m)
    TextView confirm_button_y_z_m;

    private String phone;
    private String name;
    private String blank;
    private String number;
    private String vaildCode;
    private String price;
    private String viewFlag;
    private String card;
    private String yzm;

    @Override
    protected int getContentView() {
        return R.layout.activity_confirmtixian;
    }

    @Override
    protected void initView() {

        Bundle bundle = getIntent().getExtras();

        viewFlag = bundle.getString("view");
        if (viewFlag.equals(AddCard.viewFlag)){//添加银行卡
            title.setText("添加银行卡");
            blank = bundle.getString("blank");
            name = bundle.getString("name");
            number = bundle.getString("number");
        }else if(viewFlag.equals(TixianActivity.viewFlag)){//确认提现
            title.setText("确认提现");
            price = bundle.getString("price");
            card = bundle.getString("card");
        }
        phone=ConfigManager.instance().getUser().getMobile();
        confirm_text_phone.setText("为了您的资金安全，本次操作需要短信验证。");

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.confirm_button_y_z_m,R.id.head_back,R.id.confirm_tixian})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.confirm_button_y_z_m://获取验证码
                confirm_text_phone.setText("为了您的资金安全，本次操作需要短信验证。已发送验证码至手机："+ phone);
                confirm_button_y_z_m.setClickable(false);
                handler.postDelayed(runnable,1000);
                sendRequest(3,"");
                break;
            case R.id.confirm_tixian://确认
                yzm = confirm_edit_yzm.getText().toString().trim();
                if(StringUtil.isEmpty(yzm)){
                    tip("请输入验证码");
                }else if(StringUtil.isEmpty(vaildCode)){
                    tip("请发送验证码");
                }else if(yzm.equals(vaildCode)){
                    if (viewFlag.equals(AddCard.viewFlag)){//添加银行卡
                        customLoadding.setTip("正在添加银行卡");
                        customLoadding.show();
                        sendRequest(0,"");
                    }else if(viewFlag.equals(TixianActivity.viewFlag)){//确认提现
                        customLoadding.setTip("正在提现");
                        customLoadding.show();
                        sendRequest(2,"");
                    }

                }else {
                    tip("验证码不正确");
                }

                break;
            case R.id.head_back://返回
                finish();
                break;
        }
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("bankName",blank);
            map.put("cardNumber",number);
            map.put("cardHolder",name);
            new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.AddBankCard,map,this).request();
        }else if(tag==2){
            HashMap<String, String> map = new HashMap<>();
            map.put("amount",price);
            map.put("bankCardId",card);
            map.put("validCode",yzm);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.ApplyForWithdraw,map,this );
            httpManager.request();
        } else{
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile",ConfigManager.instance().getUser().getMobile());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.GetVaildCodeRegisted, parmMap, this);
            httpManager.request();
        }

    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){//添加银行卡
                if(response.optBoolean("data")){
                    jumpActivity(TixianActivity.class);
                    finish();
                }
            }else if (tag==2){//提线
                if (response.optBoolean("data")){
                    jumpActivity(Tixiansuccess.class);
                    finish();
                }
            }else {
                JSONObject data = response.optJSONObject("data");
                //验证码
                vaildCode = data.optString("vaildCode");
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
    Handler handler=new Handler();
    int time=60;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            time--;
            if(time>0){
                confirm_button_y_z_m.setText(time+"秒");
                handler.postDelayed(runnable,1000);
            }else {
                confirm_button_y_z_m.setClickable(true);
                confirm_button_y_z_m.setText("请从新获取验证码");
                time=60;
            }
        }
    };
}
