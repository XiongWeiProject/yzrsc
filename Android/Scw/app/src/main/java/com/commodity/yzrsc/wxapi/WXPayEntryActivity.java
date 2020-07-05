package com.commodity.yzrsc.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.ui.activity.personalcenter.orde.DaiSendActivity;
import com.commodity.yzrsc.ui.pay.wx.WXUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 微信
 * Created by 328789 on 2017/4/12.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, WXUtils.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent,this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int errCode = baseResp.errCode;
            switch (errCode){
                case BaseResp.ErrCode.ERR_OK://支付成功
                    UpLoadUtils.instance().confirmOrder(WXUtils.ordeId, WXUtils.wxpay, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Toast.makeText(WXPayEntryActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String string = response.body().string();
                            if(string!=null){
                                try {
                                    JSONObject result = new JSONObject(string);
                                    Boolean data = result.optBoolean("success");
                                    if(data!=null&&data){
                                        SPKeyManager.curDetailMyOrdeEntity.setState(Constanct.orderPay);
                                        Bundle bundle = new Bundle();
                                        bundle.putIntegerArrayList("orderId", (ArrayList<Integer>) WXUtils.ordeId);
                                        Intent intent = new Intent(WXPayEntryActivity.this, DaiSendActivity.class);
                                        intent.putExtras(bundle);
                                        WXPayEntryActivity.this.startActivity(intent);
                                        WXPayEntryActivity.this.finish();
                                    }else {
                                        Toast.makeText(WXPayEntryActivity.this,result.optString("msg"),Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
//                            Toast.makeText(WXPayEntryActivity.this,"支付成功"+string,Toast.LENGTH_SHORT).show();
                        }
                    });

                    break;
                case BaseResp.ErrCode.ERR_COMM://失败
                    Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://cancel
                    Toast.makeText(this,"支付取消",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
                    break;
            }
            finish();
        }
    }
}
