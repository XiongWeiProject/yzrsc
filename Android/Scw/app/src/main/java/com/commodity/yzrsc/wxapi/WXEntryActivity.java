package com.commodity.yzrsc.wxapi;


//import com.umeng.weixin.callback.WXCallbackActivity;
// 实现IWXAPIEventHandler 接口，以便于微信事件处理的回调

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.commodity.yzrsc.AppConst;
import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.manager.OkHttpRequestUtil;
import com.commodity.yzrsc.model.WXAccessTokenInfo;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.utils.CustomToast;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import java.io.IOException;

public class WXEntryActivity extends WXCallbackActivity {
    private static final String WEIXIN_ACCESS_TOKEN_KEY = "wx_access_token_key";
    private static final String WEIXIN_OPENID_KEY = "wx_openid_key";
    private static final String WEIXIN_REFRESH_TOKEN_KEY = "wx_refresh_token_key";
    private String TAG="WXEntryActivity";
    private Gson mGson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 微信事件回调接口注册
        MainApplication.sApi.handleIntent(getIntent(), this);
        mGson = new Gson();
    }

    /**
     * 微信组件注册初始化
     *
     * @param context       上下文
     * @param weixin_app_id
     * @return 微信组件api对象
     */
    public static IWXAPI initWeiXin(Context context, @NonNull String weixin_app_id) {
        if (TextUtils.isEmpty(weixin_app_id)) {
            Toast.makeText(context.getApplicationContext(), "app_id 不能为空", Toast.LENGTH_SHORT).show();
        }
        IWXAPI api = WXAPIFactory.createWXAPI(context, weixin_app_id, true);
        api.registerApp(weixin_app_id);
        return api;
    }

    /**
     * 登录微信
     *
     * @param api 微信服务api
     */
    public static void loginWeixin(Context context, IWXAPI api) {
        // 判断是否安装了微信客户端
        if (!api.isWXAppInstalled()) {
            Toast.makeText(context.getApplicationContext(), "您还未安装微信客户端！", Toast.LENGTH_SHORT).show();
            return;
        }
        // 发送授权登录信息，来获取code
        SendAuth.Req req = new SendAuth.Req();
        // 应用的作用域，获取个人信息
        req.scope = "snsapi_userinfo";
        /**
         * 用于保持请求和回调的状态，授权请求后原样带回给第三方
         * 为了防止csrf攻击（跨站请求伪造攻击），后期改为随机数加session来校验
         */
        req.state = "app_wechat";
        api.sendReq(req);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            // 发送成功
            case BaseResp.ErrCode.ERR_OK:
                if(resp.getType()==ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
                    Toast.makeText(this,"分享成功了",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // 获取code
                    String code = ((SendAuth.Resp) resp).code;
                    // 通过code获取授权口令access_token
                    Log.e(TAG, "onResp: " +code );
                    getAccessToken(code);
                }

                break;
        }
        finish();
    }
    /**
     * 获取授权口令
     */
    private void getAccessToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid=" + AppConst.WEIXIN_APP_ID +
                "&secret=" + AppConst.WEIXIN_APP_SECRET +
                "&code=" + code +
                "&grant_type=authorization_code";
        // 网络请求获取access_token
        OkHttpRequestUtil.instance().httpRequest(url, new OkHttpRequestUtil.ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                // 判断是否获取成功，成功则去获取用户信息，否则提示失败
                Log.e(TAG, "获取信息成功" + response.toString());
                processGetAccessTokenResult(response);
            }

            @Override
            public void onError(int errorCode, final String errorMsg) {
                CustomToast.showToast("获取微信信息失败" + errorMsg);
                Log.e(TAG, "获取微信信息失败" + errorMsg.toString());
            }

            @Override
            public void onFailure(IOException e) {
                CustomToast.showToast("微信登录失败" + e.getMessage());
                Log.e(TAG, "微信登录失败" + e.getMessage());
            }
        });
    }

    /**
     * 处理获取的授权信息结果
     *
     * @param response 授权信息结果
     */
    private void processGetAccessTokenResult(String response) {
        // 验证获取授权口令返回的信息是否成功
        if (validateSuccess(response)) {
            // 使用Gson解析返回的授权口令信息
            WXAccessTokenInfo tokenInfo = mGson.fromJson(response, WXAccessTokenInfo.class);
            // 保存信息到手机本地
            //saveAccessInfotoLocation(tokenInfo);
            // 获取用户信息
            getUserInfo(tokenInfo.getAccess_token(), tokenInfo.getOpenid());
        } else {
            // 授权口令获取失败，解析返回错误信息
//            WXErrorInfo wxErrorInfo = mGson.fromJson(response, WXErrorInfo.class);
//            Logger.e(wxErrorInfo.toString());
            // 提示错误信息
           // CustomToast.showToast("获取微信登录错误信息");
        }
    }

    /**
     * 验证是否成功
     *
     * @param response 返回消息
     * @return 是否成功
     */
    private boolean validateSuccess(String response) {
        return (response.contains("access_token") && response.contains("openid"));
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/sns/userinfo?" +
                "access_token=" + access_token +
                "&openid=" + openid;
        Log.e(TAG,"用户信息获取: "+ access_token+"=="+openid);
        OkHttpRequestUtil.instance().httpRequest(url, new OkHttpRequestUtil.ApiCallback<String>() {
            @Override
            public void onSuccess(String response) {
                // 解析获取的用户信息
                //WXUserInfo userInfo = mGson.fromJson(response, WXUserInfo.class);
                Log.e(TAG,"用户信息获取结果: "+ response.toString());
               // CustomToast.showToast("获取微信登录信息成功");
                BusProvider.getInstance().post(new Event.GetWeChatInfo(response.toString()));
                finish();
            }

            @Override
            public void onError(int errorCode, String errorMsg) {
                CustomToast.showToast("错误信息: " + errorMsg);
            }

            @Override
            public void onFailure(IOException e) {
                CustomToast.showToast("获取用户信息失败: " );
            }
        });
    }
}
