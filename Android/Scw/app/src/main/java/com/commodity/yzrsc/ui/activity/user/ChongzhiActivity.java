package com.commodity.yzrsc.ui.activity.user;

import android.content.Intent;
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
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.AdressDetail;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.user.UserAdressListActivity;
import com.commodity.yzrsc.ui.dialog.CommonDialog;
import com.commodity.yzrsc.ui.pay.PayActivity;
import com.commodity.yzrsc.ui.widget.imageview.XCRoundRectImageView;
import com.commodity.yzrsc.utils.ResultUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/3/29 22:08
 * 钱包充值界面
 */
public class ChongzhiActivity extends BaseActivity{
    @Bind(R.id.chongzhi_50)
    TextView tv_50;
    @Bind(R.id.chongzhi_100)
    TextView tv_100;
    @Bind(R.id.chongzhi_200)
    TextView tv_200;
    @Bind(R.id.chongzhi_500)
    TextView tv_500;
    @Bind(R.id.chongzhi_1000)
    TextView tv_1000;
    @Bind(R.id.chongzhi_2000)
    TextView tv_2000;
    @Bind(R.id.chongzhi_other)
    EditText et_other;

    @Bind(R.id.btn_submit)
    Button btn_submit;

    JSONObject dataJson;
    AdressDetail adressDetail;
    private int price = 50;
    private int selected = 50;

    @Override
    protected int getContentView() {
        return R.layout.activity_commdity_order;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListeners() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( selected == 0 ){
                    price = Integer.parseInt(et_other.getText().toString());
                    if( (price < 1) || (price > 100000)) {
                        tip("请选择充值金额，或自定义金额：1 ~ 100000");
                        return;
                    }
                }
                sendRequest(1,"");
            }
        });
        tv_50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearColor();
                tv_50.setBackgroundColor(getResources().getColor(R.color.type_BA0008));
            }
        });
        tv_100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearColor();
                tv_100.setBackgroundColor(getResources().getColor(R.color.type_BA0008));
            }
        });
        tv_200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearColor();
                tv_200.setBackgroundColor(getResources().getColor(R.color.type_BA0008));
            }
        });

        tv_500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearColor();
                tv_500.setBackgroundColor(getResources().getColor(R.color.type_BA0008));
            }
        });
        tv_1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearColor();
                tv_1000.setBackgroundColor(getResources().getColor(R.color.type_BA0008));
            }
        });
        tv_2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearColor();
                tv_2000.setBackgroundColor(getResources().getColor(R.color.type_BA0008));
            }
        });
    }

    private void clearColor(){
        tv_50.setBackgroundColor(getResources().getColor(R.color.white_half));
        tv_100.setBackgroundColor(getResources().getColor(R.color.white_half));
        tv_200.setBackgroundColor(getResources().getColor(R.color.white_half));

        tv_500.setBackgroundColor(getResources().getColor(R.color.white_half));
        tv_1000.setBackgroundColor(getResources().getColor(R.color.white_half));
        tv_2000.setBackgroundColor(getResources().getColor(R.color.white_half));
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if (tag == 1) {//充值
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("goodsSaleId",dataJson.optString("id"));
            parmMap.put("addressInfoId",adressDetail.getId());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.OrderAnOrder, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                //跳到支付界面
                Bundle bundle = new Bundle();
                bundle.putString("Amount",Integer.valueOf(price).toString()+".00");
                jumpActivity(PayActivity.class,bundle);
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==1001){
                //sendRequest(1,"");
            }else if (requestCode==1002){
            }
        }
    }

}
