package com.commodity.yzrsc.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.HomeFragmentActivity;
import com.commodity.yzrsc.wxapi.WXEntryActivity;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/3/17 22:37
 * 登录页面
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.tv_regist)
    TextView tv_regist;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_pwd)
    EditText et_pwd;
    @Bind(R.id.tv_changepwd)
    TextView tv_changepwd;
    @Bind(R.id.view_wxlogin_btn)
    View view_wxlogin_btn;
    private String openId = "";
    private String otherLoginInfoJson = "";
    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        et_phone.setText(SPManager.instance().getString(SPKeyManager.USERINFO_mobile));
    }

    @Override
    protected void initListeners() {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_phone.getText().toString().isEmpty() || et_phone.getText().toString().length() < 11) {
                    tip("请输入正确的手机号！");
                    return;
                }
                if (et_pwd.getText().toString().isEmpty()) {
                    tip("请输入密码！");
                    return;
                }
                sendRequest(1, "");
            }
        });
        tv_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(RegistActivity.class);
            }
        });
        tv_changepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(ForgetPwdActivity.class);
            }
        });
        view_wxlogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WXEntryActivity.loginWeixin(MainApplication.mContext, MainApplication.sApi);
            }
        });

    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {//登录
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("loginName", et_phone.getText().toString());
            parmMap.put("password", et_pwd.getText().toString());
            parmMap.put("platformType", "2");//2 android
            parmMap.put("deviceId", MainApplication.deviceId);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.Login, parmMap, this);
            httpManager.request();
        } else if (tag == 2) {//第三方登录
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("openType", "4");//Sys = 1,QQ = 2,Sina = 3,Wechat = 4
            parmMap.put("openId", openId);
            parmMap.put("platformType", "2");//2 android
            parmMap.put("deviceId", MainApplication.deviceId);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.LoginOpen, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject jsonObject = (JSONObject) resultInfo.getResponse();
            if (jsonObject != null) {
                if (jsonObject.optBoolean("success")) {
                    tip("登录成功");
                    SPManager.instance().setString(SPKeyManager.USERINFO_pwd, et_pwd.getText().toString());
                    ConfigManager.instance().writeInSP(jsonObject);
                    jumpActivity(HomeFragmentActivity.class);
                    finish();
                } else {
                    tip(jsonObject.optString("msg"));
                }
            } else {
                tip("登录异常");
            }
        } else if (tag == 2) {
            JSONObject jsonObject = (JSONObject) resultInfo.getResponse();
            if (jsonObject != null) {
                if (jsonObject.optBoolean("success")) {
                    if (jsonObject.optJSONObject("data")==null||(jsonObject.optJSONObject("data")!=null&&jsonObject.optJSONObject("data").optString("mobile").isEmpty())){
                        Bundle bundle = new Bundle();
                        bundle.putString("otherLoginInfo", otherLoginInfoJson);
                        jumpActivityForResult(1001,RegistActivity.class, bundle);
                    }else {
                        tip("登录成功");
                        SPManager.instance().setString(SPKeyManager.USERINFO_pwd, et_pwd.getText().toString());
                        ConfigManager.instance().writeInSP(jsonObject);
                        jumpActivity(HomeFragmentActivity.class);
                        finish();
                    }

                } else {
                    tip(jsonObject.optString("msg"));
                }
            } else {
                tip("登录异常");
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
    }

    //获取微信登录信息
    @Subscribe
    public void getWeChatInfo(Event.GetWeChatInfo event) {
        Log.e("getWeChatInfo: ", event.getStr());
        //{"openid":"oadnS0gg37E8GWtnsxWNYlvQEhyE","nickname":"啦啦","sex":1,"language":"zh_CN","city":"Nanjing","province":"Jiangsu","country":"CN","headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/jhsVmxuX64wsd7icczb60vnNIZAjs4xvaNmzfmrb94gzqzMTXQ6EuTmEDS2zVExjoGjYnfgicfrT26F6E1SicH2WhhK6tnH4Kb6\/0","privilege":[],"unionid":"o1EBrxOc3xM8gATLg7pJeujAUOMk"}
        try {
            JSONObject json = new JSONObject(event.getStr());
            openId = json.optString("openid");
            otherLoginInfoJson=event.getStr();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!openId.isEmpty()) {
            sendRequest(2, "");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==1001){
                sendRequest(2,"");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }
}
