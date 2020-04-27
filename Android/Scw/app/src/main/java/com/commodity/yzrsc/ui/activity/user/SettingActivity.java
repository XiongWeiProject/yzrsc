package com.commodity.yzrsc.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.general.GeneralWebViewActivity;
import com.commodity.yzrsc.ui.dialog.CommonDialog;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/3/30 21:10
 * 系统设置
 */
public class SettingActivity extends BaseActivity{
    @Bind(R.id.btn_outlogin)
    Button btn_outlogin;
    @Bind(R.id.tv_text1)
    TextView tv_text1;
    @Bind(R.id.ll_btn_about)
    LinearLayout ll_btn_about;
    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        tv_text1.setText("版本"+MainApplication.mContext.getVersionName());
    }

    @Override
    protected void initListeners() {
        btn_outlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonDialog commonDialog=new CommonDialog(SettingActivity.this);
                commonDialog.show();
                commonDialog.setContext("确认退出登录？");
                commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                    @Override
                    public void clickSubmit() {
                        SPKeyManager.ListenTextView.setText("关闭程序");
                        ConfigManager.instance().clearUser();
                        jumpActivity(LoginActivity.class);
                        finish();
                    }
                });
            }
        });
        ll_btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title","用户协议");
                bundle.putString("content_url", SPManager.instance().getString(SPKeyManager.APP_about_URL));
                jumpActivity(GeneralWebViewActivity.class,bundle);
            }
        });
    }
}
