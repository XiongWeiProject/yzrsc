package com.commodity.scw.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.store.RenzhengActivity;
import com.commodity.scw.ui.widget.webview.CustomWebView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 * 认证
 * Created by yangxuqiang on 2017/3/25.
 */

public class AuthenticationActivity extends BaseActivity {
//    @Bind(R.id.authentication_context)
//    TextView textView;
    @Bind(R.id.authertication_head)
    TextView authertication_head;
    @Bind(R.id.customweb)
    CustomWebView customweb;
    @Override
    protected int getContentView() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void initView() {
//        sendRequest(0,"");
        customweb.loadUrl(SPManager.instance().getString(SPKeyManager.APP_authorization_URL));
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.authentication_confirmation,R.id.authentication_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.authentication_back:
                finish();
                break;
            case R.id.authentication_confirmation:
                jumpActivity(RenzhengActivity.class);
                finish();
                break;
        }
    }

    @Override
    public void sendRequest(int tag) {
        super.sendRequest(tag);
        customLoadding.show();
        if(tag==0){
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetStaticPageUrl,null,this).request();
        }
    }


    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        String authorization = response.optString("authorization");
        if(!StringUtil.isEmpty(authorization)){
            customweb.loadUrl(authorization);
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
    }
}
