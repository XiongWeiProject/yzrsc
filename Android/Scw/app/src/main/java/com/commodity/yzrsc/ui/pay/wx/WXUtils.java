package com.commodity.yzrsc.ui.pay.wx;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.commodity.yzrsc.AppConst;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 微信
 * Created by 328789 on 2017/4/11.
 */

public class WXUtils {
    public static final String WX_APP_ID = AppConst.WEIXIN_APP_ID;
    public static final String WX_API_KEY = AppConst.WEIXIN_APP_SECRET;
    private final Context context;
    private IWXAPI wxapi;
    private PayReq req;
    public static List<Integer> ordeId;
    public static String oneordeId;
    public static String wxpay="wxpay";
    public static int types;
    public WXUtils(Context context, List<Integer> ordeId,int type) {
        this.context=context;
        this.ordeId=ordeId;
        this.types=type;
    }

    public WXUtils(Context context, String ordeId,int type) {
        this.context=context;
        this.oneordeId=ordeId;
        this.types=type;
    }
    public void startPay(){
        wxapi = WXAPIFactory.createWXAPI(context, WX_APP_ID);
        wxapi.registerApp(WX_APP_ID);
        if(!wxapi.isWXAppInstalled()&& !wxapi.isWXAppSupportAPI()){
            Toast.makeText(context,"请安装微信",Toast.LENGTH_SHORT).show();
        }
        //生成prepay_id
        getPrepayId();
    }

    public void getPrepayId() {
        String Json = "";
        String url = "";
        String iorderIds = "";
        Gson gson = new Gson();
        if (types==0){
            Json = gson.toJson(ordeId);
            iorderIds ="{\n" +
                    "  \"orderIds\": "+Json+",\n" +
                    "  \"payment\": \""+wxpay+"\"\n" +
                    "}";
            url = IRequestConst.RequestMethod.POSTPAY;
        }else if (types==3){
            Json = gson.toJson(oneordeId);
            iorderIds = "{\n" +
                    "  \"orderId\": "+Json+",\n" +
                    "  \"payment\": \""+wxpay+"\"\n" +
                    "}";
            url = IRequestConst.RequestMethod.POSTWALLORDERPAY;
        }else {
            Json = gson.toJson(oneordeId);
            iorderIds ="{\n" +
                    "  \"orderId\": "+Json+",\n" +
                    "  \"payment\": \""+wxpay+"\"\n" +
                    "}";
            url = IRequestConst.RequestMethod.POSTPAY;
        }
        Log.e("Json",Json + url);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), iorderIds);
        UpLoadUtils.instance().requestPayId(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure:",e.getMessage());
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("onResponse:",string);
                if(string!=null){
                    try {
                        req = new PayReq();
                        JSONObject jsonObject = new JSONObject(string);
                        if(jsonObject.optBoolean("success")){
                            String data = jsonObject.optString("data");
                            JSONObject jsonData = new JSONObject(data);
                            req.appId=WX_APP_ID;
                            req.partnerId = jsonData.optString("partnerid");// 微信支付分配的商户号
                            req.prepayId = jsonData.optString("prepayid");// 预支付订单号，app服务器调用“统一下单”接口获取
                            String prepayid = jsonData.optString("prepayid");
                            if(prepayid==null||prepayid.length()==0){
                                Toast.makeText(context,"没有此订单",Toast.LENGTH_LONG).show();
                            }
                            req.nonceStr = jsonData.optString("noncestr");// 随机字符串，不长于32位
                            req.timeStamp = jsonData.optString("timestamp");// 时间戳
                            req.packageValue = jsonData.optString("package");// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
                            req.sign = jsonData.optString("sign");// 签名，
                            wxapi.sendReq(req);
                        }else {
                            Looper.prepare();
                            Toast.makeText(context,jsonObject.optString("msg"),Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
