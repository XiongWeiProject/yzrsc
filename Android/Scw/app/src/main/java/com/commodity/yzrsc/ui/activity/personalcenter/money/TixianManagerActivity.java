package com.commodity.yzrsc.ui.activity.personalcenter.money;

import android.view.View;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.dialog.CommonDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 提现管理
 * Created by yangxuqiang on 2017/3/29.
 */

public class TixianManagerActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.head_text_right)
    TextView head_text_right;
    @Bind(R.id.tixian_kejine)//可提现金额
    TextView tixian_kejine;
    @Bind(R.id.tixian_dong)//冻结金额
    TextView tixian_dong;
    private int size;

    @Override
    protected int getContentView() {
        return R.layout.activity_tixian_manager;
    }

    @Override
    protected void initView() {
        title.setText("提现管理");
        head_text_right.setText("提现记录");


    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        sendRequest(0,"");
    }

    @OnClick({R.id.tixian_button,R.id.head_back,R.id.head_text_right})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tixian_button://提现
                sendRequest(1,"");


                break;
            case R.id.head_text_right://提现记录
                jumpActivity(TixianJiluActivity.class);
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
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetWalletAmount,map,this );
            httpManager.request();
        }else {
            HashMap<String, String> map = new HashMap<>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetBankCardList,map,this );
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
                tixian_kejine.setText("￥"+data.optString("avialableAmount"));
                tixian_dong.setText("￥"+data.optString("frozenAmount"));
            }else {
                JSONArray data = null;
                try {
                    data = response.getJSONArray("data");
                    size = data.length();
                    if(size==0){
                        CommonDialog commonDialog = new CommonDialog(this);
                        commonDialog.show();
                        commonDialog.setContext("添加银行卡");
                        commonDialog.setClickCancelListener(new CommonDialog.OnClickCancelListener() {
                            @Override
                            public void clickCance() {

                            }
                        });
                        commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                            @Override
                            public void clickSubmit() {
                                jumpActivity(AddCard.class);
                            }
                        });
                    }else {
                        jumpActivity(TixianActivity.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
