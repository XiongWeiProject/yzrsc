package com.commodity.yzrsc.ui.pay;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.MyWallModel;
import com.commodity.yzrsc.model.NewOrderModel;
import com.commodity.yzrsc.model.PayModel;
import com.commodity.yzrsc.model.ZhifubaoModel;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.personalcenter.money.MyMoneyActivity;
import com.commodity.yzrsc.ui.activity.personalcenter.orde.DaiSendActivity;
import com.commodity.yzrsc.ui.pay.wx.WXUtils;
import com.commodity.yzrsc.ui.pay.zfb.PayResult;
import com.commodity.yzrsc.ui.pay.zfb.ZFBHandler;
import com.commodity.yzrsc.ui.pay.zfb.ZFBUtils;
import com.commodity.yzrsc.ui.pay.zfb.ZFCallBack;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 支付页面
 * Created by yangxuqiang on 2017/4/5.
 */

public class PayActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.pay_money)
    TextView pay_money;
    @Bind(R.id.pay_time)
    TextView pay_time;
    @Bind(R.id.pay_weixin_img)
    ImageView pay_weixin_img;
    @Bind(R.id.pay_zfb_img)
    ImageView pay_zfb_img;
    @Bind(R.id.pay_pay_button)
    Button pay_pay_button;
    @Bind(R.id.pay_qianbao_img)
    ImageView pay_qianbao_img;
    @Bind(R.id.pay_qianbao_line)
    LinearLayout pay_qianbao_line;

    List<Integer> oderList = new ArrayList<>();
    NewOrderModel newOrderModel;
    @Bind(R.id.head_back)
    LinearLayout headBack;
    @Bind(R.id.head_text_right)
    TextView headTextRight;
    @Bind(R.id.pay_weixin_button)
    LinearLayout payWeixinButton;
    @Bind(R.id.pay_zfb_button)
    LinearLayout payZfbButton;
    @Bind(R.id.momey_pay)
    TextView momeyPay;
    private int payFlag;//默认是微信0
    private String total;
    private String OneordeId;
    private int type = 1;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ;
    private SimpleDateFormat format2;
    List<Integer> list = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initView() {
        title.setText("支付订单");
        Gson gson = new Gson();
        Bundle extras = getIntent().getExtras();
        newOrderModel = (NewOrderModel) extras.getSerializable("orderId");
        total = extras.getString("total");
        OneordeId = extras.getString("ordeId");
        type = extras.getInt("type", 1);
        if (newOrderModel != null) {
            oderList = newOrderModel.getData();
        }else {
            oderList.clear();
            oderList.add(Integer.valueOf(OneordeId));
        }
        Log.e("orderId", oderList + "");
        String format = String.format("%.2f", Double.valueOf(total));
        pay_money.setText(format);
        sendRequest(0, "");
        if (type == 3) {
            pay_qianbao_line.setVisibility(View.GONE);
        } else {
            //獲取钱包金额
            sendRequest(1, "");
        }
    }

    @Override
    protected void initListeners() {

    }

    @OnClick({R.id.head_back, R.id.pay_weixin_button, R.id.pay_zfb_button, R.id.pay_pay_button, R.id.pay_qianbao_line})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.pay_weixin_button:
                switchWeixnPay();
                break;
            case R.id.pay_zfb_button:
                switchZFBPay();
                break;
            case R.id.pay_qianbao_line:
                switchQianaoPay();
                break;
            case R.id.pay_pay_button://支付
//                customLoadding.setTip("支付中...");
//                customLoadding.show();

                if (payFlag == 1) {//支付宝
                    if (type == 0) {
                        PayModel payModel = new PayModel();
                        payModel.setOrderIds(oderList);
                        payModel.setPayment("alipay");
                        new ZFBUtils(this, null, payModel, type, handler).startPay();
                    } else {
                        ZhifubaoModel payModel = new ZhifubaoModel();
                        payModel.setOrderId(OneordeId);
                        payModel.setPayment("alipay");
                        new ZFBUtils(this, null, payModel, type, handler).startPay();
                    }

//                    if(ZFBUtils.isZfbAvilible(this)){
//                        new ZFBUtils(this,null,ordeId,total,handler);
//                    }else {
//                        tip("请安装支付宝");
//                    }
                } else if (payFlag == 0) {//微信 wxpay
                    if (type == 0) {
                        new WXUtils(this, oderList, type).startPay();
                    } else {
                        new WXUtils(this, OneordeId, type).startPay();
                    }

                } else if (payFlag == 2) {//钱包余额
                    //TODO
                    if (oderList.size() == 0) {
                        getPrepayId(OneordeId);
                    } else {
                        getPrepayId(oderList.get(0) + "");
                    }

                }
                break;
        }
    }

    public void getPrepayId(final String oneordeId) {
        customLoadding.setTip("正在支付。。");
        customLoadding.show();
        Gson gson = new Gson();
        String Json = "";
        String url = "";
        String iorderIds = "";
        Json = gson.toJson(oneordeId);
        iorderIds = "{\n" +
                "  \"orderId\": " + Json + ",\n" +
                "  \"payment\": \"walletpay\"\n" +
                "}";
        url = IRequestConst.RequestMethod.POSTWALLORDERPAY;
        Log.e("Json", Json + url);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), iorderIds);
        UpLoadUtils.instance().requestPayId(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure:", e.getMessage());
                customLoadding.dismiss();
                Toast.makeText(PayActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("onResponse:", string);

                if (string != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        if (jsonObject.optBoolean("success")) {
                            customLoadding.dismiss();
                            String data = jsonObject.optString("data");
                            JSONObject jsonData = new JSONObject(data);
                            //消费支出接口
                            postmoney(jsonData.optString("out_trade_no"), total);

                        } else {
                            customLoadding.dismiss();
                            Looper.prepare();
                            Toast.makeText(PayActivity.this, jsonObject.optString("msg"), Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PayActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void postmoney(final String outTradeNo, String totalPaid) {
        Gson gson = new Gson();
        MyWallModel myWallModel = new MyWallModel();
        myWallModel.setOutTradeNo(outTradeNo);
        myWallModel.setTotalPaid(totalPaid);
        String url = "";
        url = IRequestConst.RequestMethod.POSTISPAYMoney;
        Log.e("Json",gson.toJson(myWallModel)+ url);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(myWallModel));
        UpLoadUtils.instance().requestPayId(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure:", e.getMessage());
                Toast.makeText(PayActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.e("onResponse:", string);

                if (string != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        if (jsonObject.optBoolean("success")) {
                            SPKeyManager.curDetailMyOrdeEntity.setState(Constanct.orderPay);
                            Bundle bundle = new Bundle();
                            if (type == 0) {
                                bundle.putString("orderId", oderList.get(0) + "");
                                bundle.putInt("type", payFlag);
                            } else {
                                bundle.putString("orderId", OneordeId);
                                bundle.putInt("type", payFlag);
                            }
                            jumpActivity(DaiSendActivity.class, bundle);
                            finish();
                            Looper.prepare();
                            tip(jsonObject.getString("msg"));
                            Looper.loop();
                        } else {
                            Looper.prepare();
                            Toast.makeText(PayActivity.this, jsonObject.optString("msg"), Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(PayActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private Handler handler = new ZFBHandler(new ZFCallBack() {

        @Override
        public void success(Object obj) {
//            tip((String)obj);
//            PayResult payResult = new PayResult((String) obj);
            PayResult payResult = new PayResult((Map<String, String>) obj);
            Log.e("payResult", payResult.getResultStatus());
            switch (payResult.getResultStatus()) {
                case "9000"://成功
                case "8000":
                    String oderid = "";
                    if (type == 0) {
                        oderid = oderList.get(0) + "";
                    } else {
                        oderid = OneordeId;
                    }
                    UpLoadUtils.instance().confirmOrder(oderid, "alipay", new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            tip("支付失败");
                            Log.e("failure:", e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String string = response.body().string();
                            if (string != null) {
                                try {
                                    JSONObject result = new JSONObject(string);
                                    Boolean data = result.optBoolean("data");
                                    if (data != null && data) {
                                        Bundle bundle = new Bundle();
                                        if (type == 3) {
                                            jumpActivity(MyMoneyActivity.class, bundle);
                                        } else {
                                            SPKeyManager.curDetailMyOrdeEntity.setState(Constanct.orderPay);
                                            if (type == 0) {
                                                bundle.putString("orderId", oderList.get(0) + "");
                                                bundle.putInt("type", payFlag);
                                            } else {
                                                bundle.putString("orderId", OneordeId);
                                                bundle.putInt("type", payFlag);
                                            }
                                            jumpActivity(DaiSendActivity.class, bundle);
                                            finish();
                                        }

                                    } else {
                                        tip(result.optString("msg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    break;
                default:
                    tip("支付失败");
                    break;
            }
        }

        @Override
        public void failder() {

        }
    });

    private void switchZFBPay() {
        payFlag = 1;
        pay_zfb_img.setBackgroundResource(R.drawable.icon_xzoff);
        pay_weixin_img.setBackgroundResource(R.drawable.icon_xzon);
        pay_qianbao_img.setBackgroundResource(R.drawable.icon_xzon);
    }

    private void switchWeixnPay() {
        payFlag = 0;
        pay_zfb_img.setBackgroundResource(R.drawable.icon_xzon);
        pay_weixin_img.setBackgroundResource(R.drawable.icon_xzoff);
        pay_qianbao_img.setBackgroundResource(R.drawable.icon_xzon);
    }

    private void switchQianaoPay() {
        payFlag = 2;
        pay_zfb_img.setBackgroundResource(R.drawable.icon_xzon);
        pay_weixin_img.setBackgroundResource(R.drawable.icon_xzon);
        pay_qianbao_img.setBackgroundResource(R.drawable.icon_xzoff);
    }

    @Override
    protected void onDestroy() {
        if (customLoadding.isShowing()) {
            customLoadding.dismiss();
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        handler = null;
        super.onDestroy();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public String getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        return version;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(!payFlag){
//            finish();
//        }

    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        String onderid = "";
        if (tag == 0) {
            if (type == 0) {
                onderid = oderList.get(0) + "";
            } else {
                onderid = OneordeId;
            }
            if (tag == 0) {
                HashMap<String, String> map = new HashMap<>();
                map.put("orderId", onderid);
                HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                        IRequestConst.RequestMethod.GetOrderExpireTime, map, this);
                httpManager.request();
            }
        } else {
            HashMap<String, String> map = new HashMap<>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetWalletAmount, map, this);
            httpManager.request();
        }

    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if (response.optBoolean("success")) {
            String data = response.optString("data");
            if (tag == 0) {
                try {
                    long nowTime = System.currentTimeMillis();
                    Date parse = format.parse(data);
                    time = (parse.getTime() - nowTime) / 1000;
                    handler.postDelayed(runnable, 1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                JSONObject datas = response.optJSONObject("data");
                momeyPay.setText("钱包余额（￥" + datas.optString("avialableAmount") + "）元");
            }
        } else {
            tip(response.optString("msg"));
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    Handler timehandler = new Handler();
    long time = 0;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if (time > 0) {
                pay_time.setText(getTime(time));
                timehandler.postDelayed(runnable, 1000);
            } else {
                pay_pay_button.setVisibility(View.GONE);
                pay_time.setText("订单超时");
                timehandler.removeCallbacks(runnable);
                time = 0;
            }
        }
    };

    public String getTime(long data) {
        int hour = (int) (data / 3600);
        long h = data % 3600;
        int minute = (int) (h / 60);
        int second = (int) (h % 60);
        return "剩余支付时间" + hour + "小时" + minute + "分" + second + "秒，超时将取消订单";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
