package com.commodity.yzrsc.ui.pay;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.personalcenter.orde.DaiSendActivity;
import com.commodity.yzrsc.ui.pay.wx.WXUtils;
import com.commodity.yzrsc.ui.pay.zfb.PayResult;
import com.commodity.yzrsc.ui.pay.zfb.ZFBHandler;
import com.commodity.yzrsc.ui.pay.zfb.ZFBUtils;
import com.commodity.yzrsc.ui.pay.zfb.ZFCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
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

    private boolean payFlag;//默认是微信
    private String ordeId;
    private String total;
    private SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;
    private SimpleDateFormat format2;

    @Override
    protected int getContentView() {
        return R.layout.activity_pay;
    }

    @Override
    protected void initView() {
        title.setText("支付订单");
        Bundle extras = getIntent().getExtras();
        ordeId = extras.getString("ordeId");
        total=extras.getString("total");
        String format = String.format("%.2f", Double.valueOf(total));
        pay_money.setText(format);
        sendRequest(0,"");
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.pay_weixin_button,R.id.pay_zfb_button,R.id.pay_pay_button})
    public void click(View v){
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.pay_weixin_button:
                switchWeixnPay();
                break;
            case R.id.pay_zfb_button:
                switchZFBPay();
                break;
            case R.id.pay_pay_button://支付
                customLoadding.setTip("支付中...");
                customLoadding.show();
                if(payFlag){//支付宝
                    new ZFBUtils(this,null,ordeId,"alipay",handler).startPay();
//                    if(ZFBUtils.isZfbAvilible(this)){
//                        new ZFBUtils(this,null,ordeId,total,handler);
//                    }else {
//                        tip("请安装支付宝");
//                    }
                }else {//微信 wxpay
                    new WXUtils(this,ordeId).startPay();
                }
                break;
        }
    }
    private Handler handler=new ZFBHandler(new ZFCallBack() {

        @Override
        public void success(Object obj) {
//            tip((String)obj);
//            PayResult payResult = new PayResult((String) obj);
            PayResult payResult = new PayResult((Map<String, String>) obj);
            switch (payResult.getResultStatus()){
                case "9000"://成功
                case "8000":
                        UpLoadUtils.instance().confirmOrder(ordeId, "alipay", new Callback() {@Override
                        public void onFailure(Call call, IOException e) {
                            tip("支付失败");
                                Log.e("failure:",e.getMessage());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String string = response.body().string();
                                if(string!=null){
                                    try {
                                        JSONObject result = new JSONObject(string);
                                        Boolean data = result.optBoolean("data");
                                        if(data!=null&&data){
                                            SPKeyManager.curDetailMyOrdeEntity.setState(Constanct.orderPay);
                                            Bundle bundle = new Bundle();
                                            bundle.putString("orderId",ordeId);
                                            jumpActivity(DaiSendActivity.class,bundle);
                                            finish();
                                        }else {
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
        payFlag=true;
        pay_zfb_img.setBackgroundResource(R.drawable.icon_xzoff);
        pay_weixin_img.setBackgroundResource(R.drawable.icon_xzon);
    }

    private void switchWeixnPay() {
            payFlag=false;
            pay_zfb_img.setBackgroundResource(R.drawable.icon_xzon);
            pay_weixin_img.setBackgroundResource(R.drawable.icon_xzoff);
    }

    @Override
    protected void onDestroy() {
        if(customLoadding.isShowing()){
            customLoadding.dismiss();
        }
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
        handler=null;
        super.onDestroy();
    }
    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public String getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        return version;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!payFlag){
            finish();
        }

    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",ordeId);
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetOrderExpireTime,map,this).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            String data = response.optString("data");
            try {
                long nowTime = System.currentTimeMillis();
                Date parse = format.parse(data);
                time=(parse.getTime()-nowTime)/1000;
                handler.postDelayed(runnable,1000);
            } catch (ParseException e) {
                e.printStackTrace();
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
    Handler timehandler=new Handler();
    long time=0;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            time--;
            if(time>0){
                pay_time.setText(getTime(time));
                timehandler.postDelayed(runnable,1000);
            }else {
                pay_pay_button.setVisibility(View.GONE);
                pay_time.setText("订单超时");
                timehandler.removeCallbacks(runnable);
                time=0;
            }
        }
    };
    public String getTime(long data){
        int hour = (int) (data / 3600);
        long h=data%3600;
        int minute= (int) (h/60);
        int second= (int) (h%60);
        return "剩余支付时间"+hour+"小时"+minute+"分"+second+"秒，超时将取消订单";
    }
}
