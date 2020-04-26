/*
 * Copyright (c) 2015 [1076559197@qq.com | tchen0707@gmail.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License”);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.commodity.scw.ui.activity;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManageNew;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.model.CommodityBean;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.user.LoginActivity;
import com.commodity.scw.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * 作者：liyushen on 2016/11/28 15:08
 * 功能：启动页
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_image)
    ImageView mSplashImage;
    @Bind(R.id.enter)
    Button enter;
    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        sendRequest(1,"");
        if (SPManager.instance().getBoolean(SPKeyManager.ISNOTFIRSTNAPP)){
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_alpha_anim);;
            mSplashImage.setAnimation(animation);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    if (SPManager.instance().getBoolean(SPKeyManager.USERINFO_isLogin)){
                        jumpActivity(HomeFragmentActivity.class);
                    }else {
                        jumpActivity(LoginActivity.class);
                    }
                    finish();
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }else {
            jumpActivity(GuideShowActivity.class);
            SPManager.instance().setBoolean(SPKeyManager.ISNOTFIRSTNAPP,true);
            finish();
        }

    }

    @Override
    protected void initListeners() {
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPManager.instance().getBoolean(SPKeyManager.USERINFO_isLogin)){
                    jumpActivity(HomeFragmentActivity.class);
                }else {
                    jumpActivity(LoginActivity.class);
                }
                finish();
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {//静态界面
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetStaticPageUrl, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            SPManager.instance().setString(SPKeyManager.APP_about_URL,resultJson.optString("about"));
            SPManager.instance().setString(SPKeyManager.APP_license_URL,resultJson.optString("license"));
            SPManager.instance().setString(SPKeyManager.APP_authorization_URL,resultJson.optString("authorization"));
        } else if (tag == 2) {

        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
}
