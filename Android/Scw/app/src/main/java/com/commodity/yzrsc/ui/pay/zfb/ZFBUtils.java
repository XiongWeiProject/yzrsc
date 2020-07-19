package com.commodity.yzrsc.ui.pay.zfb;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.commodity.yzrsc.AppConst;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.model.OrderInfo;
import com.commodity.yzrsc.model.PayModel;
import com.commodity.yzrsc.model.PayParemsModel;
import com.commodity.yzrsc.model.ZhifubaoModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 支付宝
 * Created by 328789 on 2017/4/11.
 */

public class ZFBUtils {
//    public static final String APPID = "wxbc265d171933e862";
    public static final String APPID = AppConst.ZFB_ID;
    //PID
    public static final String PARTNER = "";
    //私钥
    public static final String PRIVATE_KEY = "";
    // 商户收款账号
    public static final String SELLER = "";
    public static final int SDK_PAY_FLAG = 1;
    private final Activity activity;
    private final OrderInfo ordeInfo;
    private final Handler handler;
    private Object signType;
    //加密方式
    private boolean rsa2=false;
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";
    PayModel payModels;
    ZhifubaoModel zhifubaoModel;
    int types ;
    public ZFBUtils(Activity activity, OrderInfo orderInfo,PayModel payModel,int type, Handler handler) {
        this.activity=activity;
        this.ordeInfo=orderInfo;
        this.handler=handler;
        this.types = type;
        this.payModels=payModel;
    }
    public ZFBUtils(Activity activity, OrderInfo orderInfo,ZhifubaoModel zhifubaoModel, int type, Handler handler) {
        this.activity=activity;
        this.ordeInfo=orderInfo;
        this.handler=handler;
        this.types = type;
        this.zhifubaoModel=zhifubaoModel;
    }
    public void startPay() {
        singlData();
    }

    private void singlData() {
        String josn = "";
        String url = "";
        final Gson gson = new Gson();
        if (types==0){
            josn = gson.toJson(payModels);
            url = IRequestConst.RequestMethod.POSTPAY;
        }else if (types==3){
            josn = gson.toJson(zhifubaoModel);
            url = IRequestConst.RequestMethod.POSTWALLORDERPAY;
        }else {
            josn = gson.toJson(zhifubaoModel);
            url = IRequestConst.RequestMethod.POSTPAY;
        }
        Log.e("Json",josn + url);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),josn);
        String deviceToken = ConfigManager.instance().getUser().getDeviceToken();
        UpLoadUtils.instance().jsonRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure:",e.getMessage());
                Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    final String orderInfo=jsonObject.optString("data");
//                    final String orderInfo=URLEncoder.encode(orderInfos,"UTF-8");
//                    final String orderInfo= URLDecoder.decode(orderInfos,"UTF-8");
                    if(orderInfo!=null){
                        Log.e("jsonObject",orderInfo);
                        Runnable payRunnable = new Runnable() {

                            @Override
                            public void run() {
                                PayParemsModel payParemsModel = gson.fromJson(orderInfo,PayParemsModel.class);
//                                String orderInfo = "app_id="+payParemsModel.getApp_id()+"&notify_url="+payParemsModel.getNotify_url()
//                                        +"&timestamp="+payParemsModel.getTimestamp()+"&biz_content="+payParemsModel.getBiz_content()
//                                        +"&method="+payParemsModel.getMethod()+"&charset="+payParemsModel.getCharset()
//                                        +"&sign_type="+payParemsModel.getSign_type()+"@version="+payParemsModel.getVersion()
//                                        +"&sign="+payParemsModel.getSign()+"&out_trade_no="+payParemsModel.getOut_trade_no()
//                                        +"&payment_link="+payParemsModel.getPayment_link();
                                PayTask alipay = new PayTask(activity);
                                Map<String, String> result = alipay.payV2(payParemsModel.getPayment_link(), true);
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                handler.sendMessage(msg);
                            }
                        };

                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
//                String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
//
//                String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
//                String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
//                final String orderInfo = orderParam + "&" + sign;

            }
        });
    }

    /**
     * 获取签名
     * @return
     */
    public String getSign() {
        return "22222";
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }
    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo(OrderInfo order) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + "" + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" +"" + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + "" + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + "" + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + "" + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" +  "" + "\"";

        // 服务器异步通知页面路径
        orderInfo += "¬ify_url=" + "\"" + "" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
    /**
     * 检查支付包是否存在
     * @param context
     * @return
     */
    public static boolean isZfbAvilible(Context context) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                System.out.println(pinfo.get(i).packageName);
                if (pn.equals("com.alipay.android.app")) {
                    return true;
                }
            }
        }
        return false;
    }

}
