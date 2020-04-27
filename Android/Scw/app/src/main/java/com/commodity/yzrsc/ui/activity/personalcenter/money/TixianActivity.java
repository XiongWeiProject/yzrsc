package com.commodity.yzrsc.ui.activity.personalcenter.money;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.model.mine.BlankDataEntity;
import com.commodity.yzrsc.model.mine.BlankEntity;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.utils.GsonUtils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 * 提现
 * Created by yangxuqiang on 2017/3/28.
 */

public class TixianActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.tixian_jine)
    TextView tixian_jine;//提现金额
    @Bind(R.id.tixian_input)
    EditText input;//输入的金额
    @Bind(R.id.tixian_name)
    TextView tixian_name;//姓名
    @Bind(R.id.tixian_bank)
    TextView tixian_bank;//银行
    @Bind(R.id.tixian_card_code)
    TextView tixian_card_code;//卡号
    @Bind(R.id.tixian_edit)
    LinearLayout tixian_edit;//编辑
    @Bind(R.id.tixian_confir)
    Button tixian_confir;//确认提现

    @Bind(R.id.tixian_card_control)
    LinearLayout tixian_card_control;
    @Bind(R.id.tixian_button_bang)
    Button tixian_button_bang;

    public static String viewFlag="tixian";
    private List<BlankDataEntity> data;
    private String avialableAmount;

    @Override
    protected int getContentView() {
        return R.layout.activity_tixianmanager;
    }

    @Override
    protected void initView() {
        title.setText("提现");
        sendRequest(0,"");
        sendRequest(1,"");

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.tixian_confir,R.id.tixian_edit,R.id.tixian_button_bang})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back://返回
                finish();
                break;
            case R.id.tixian_confir://确认提现
                String price = input.getText().toString().trim();
                if(StringUtil.isEmpty(price)){
                    tip("请输入金额");
                    return;
                }
                if(avialableAmount!=null){
                    BigDecimal bigDecimal = new BigDecimal(avialableAmount);
                    BigDecimal bigDecimal1 = new BigDecimal(price);
                    int i = bigDecimal.compareTo(bigDecimal1);
                    if(i==-1){
                        tip("提现金额不可大于可提现金额");
                        return;
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString("view",viewFlag);
                bundle.putString("price",price);
                bundle.putString("card",String.valueOf(data.get(data.size()-1).getId()));
                jumpActivity(ConfirmTixianActivity.class,bundle);
                finish();
                break;
            case R.id.tixian_edit://编辑
//                jumpActivity(EditCard.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("name",data.get(data.size()-1).getCardHolder());
                bundle1.putString("cardNumber",data.get(data.size()-1).getCardNumber());
                bundle1.putString("brank",data.get(data.size()-1).getBankName());
                jumpActivity(AddCard.class,bundle1);
                break;
            case R.id.tixian_button_bang://添加银行卡
                jumpActivity(AddCard.class);
                break;
        }
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetBankCardList,map,this );
            httpManager.request();
        }else if (tag==1){
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
                BlankEntity blankEntity = GsonUtils.jsonToObject(response.toString(), BlankEntity.class);
                data = blankEntity.getData();
                if (data!=null&&data.size()>0){
                    tixian_card_code.setText(data.get(data.size()-1).getCardNumber());
                    tixian_bank.setText(data.get(data.size()-1).getBankName());
                    tixian_name.setText(data.get(data.size()-1).getCardHolder());
                }
            }else if(tag==1){
                JSONObject data = response.optJSONObject("data");
                tixian_jine.setText("账户可提现金额"+data.optString("avialableAmount")+"元");
                avialableAmount = data.optString("avialableAmount");
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
