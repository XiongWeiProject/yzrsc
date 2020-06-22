package com.commodity.scw.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.commodity.scw.http.BaseHttpManager;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.AppManager;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.ui.dialog.CustomLoadding;
import com.commodity.scw.utils.CustomToast;
import com.commodity.scw.utils.FileUtil;
import com.umeng.socialize.UMShareAPI;

import butterknife.ButterKnife;

/**
 * 作者：liyushen on 2016/4/5 15:29
 * 功能：Activity基类
 */
@SuppressLint("HandlerLeak")
public abstract class BaseActivity extends SwipeBackBaseActivity implements BaseHttpManager.IRequestListener {
    public Context mContext;
    public CustomLoadding customLoadding;
    public Class<? extends Activity> clazz;
    public Bundle bundle;

    /**
     * 返回布局显示ID
     *
     * @return 布局id
     */
    protected abstract int getContentView();

    /**
     * 初始化View（findView替代findViewById）
     */
    protected abstract void initView();

    /**
     * 初始化监听
     */
    protected abstract void initListeners();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        customLoadding = new CustomLoadding(mContext);
        customLoadding.setCanceledOnTouchOutside(false);
        AppManager.getAppManager().addActivity(this);
        if (getContentView() != 0) {
            setContentView(getContentView());
            ButterKnife.bind(this);
            initView();
            initListeners();
        }
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T findView(int id) {
        return (T) super.findViewById(id);
    }

    /**
     * 方法名称：jumpActivity
     * 方法描述：启动相关的界面. 不带bundle
     */
    public void jumpActivity(Class<? extends Activity> toActivity) {
        this.jumpActivity(toActivity, null);

    }
    /**
     * 方法名称：jumpActivity
     * 方法描述：启动相关的界面. 带bundle
     */
    public void jumpActivity(Class<? extends Activity> toActivity, Bundle bundle) {
        Intent intent = new Intent(mContext, toActivity);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    /**
     * 跳转回调，用onActivityResultWithoutLogin接收,不带bundle
     * @param reqcode
     * @param toActivity
     */
    public void jumpActivityForResult(int reqcode, Class<? extends Activity> toActivity) {
        this.jumpActivityForResult(reqcode, toActivity, null);

    }

    /**
     * 跳转回调，用onActivityResultWithoutLogin接收,带bundle
     * @param reqcode
     * @param toActivity
     * @param bundle
     */
    public void jumpActivityForResult(int reqcode, Class<? extends Activity> toActivity, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, toActivity);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, reqcode);
    }

    /**
     * 方法名称：tip
     * 方法描述：toast显示
     */
    protected void tip(String tipStr) {
        CustomToast.showToast(tipStr);
    }

    /**
     * 方法名称：tip
     * 方法描述：toast显示
     */
    protected void tip(int resId) {
        CustomToast.showToast(resId);
    }


    /**
     * 无动画结束activity
     */
    public void noAnimFinish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 发送带参数请求方法，由用户选择时机调用
     *
     * @param ：要传入请求的某类对象
     */
    public void sendRequest(int tag, Object... params) {
        if (params == null || params.length == 0) {
            this.sendRequest(tag);
        }
    }

    /**
     * 发送请求方法，由用户选择时机调用
     */
    public void sendRequest(int tag) {
    }

    @Override
    public void onSuccess(int tag, ServiceInfo result) {
        if (result != null && result.getResponse() != null) {
            OnSuccessResponse(tag, result);
        } else {
            OnSuccessResponse(tag, null);
        }
    }

    @Override
    public void onError(int tag, String code, String msg) {
        OnFailedResponse(tag, code, msg);
    }

    /**
     * 预请求抽象类
     */
    @Override
    public void onPreExecute(int tag) {

    }

    @Override
    public void OnTimeOut(int tag, boolean isShowTip) {
        OnNetTimeOutResponse(tag, isShowTip);
    }

    @Override
    public void OnNetError(int tag, boolean isShowTip) {
        OnNetWorkErrorResponse(tag, isShowTip);
    }

    /**
     * 请求成功抽象类
     *
     */
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo)  {
        if (customLoadding != null && customLoadding.isShowing()) {
            customLoadding.dismiss();
        }
    }

    /**
     * 请求失败抽象类
     *
     * @param code
     * @param msg
     */
    public void OnFailedResponse(int tag, String code, String msg) {
        if (customLoadding != null && customLoadding.isShowing()) {
            customLoadding.dismiss();
        }
    }

    /**
     * 无网络
     */
    public void OnNetWorkErrorResponse(int tag, boolean isShowTip) {
        if (customLoadding != null && customLoadding.isShowing()) {
            customLoadding.dismiss();
        }
        if (isShowTip) {
            CustomToast.showToast("网络异常");
        }
    }

    ;

    /**
     * 超时
     */
    public void OnNetTimeOutResponse(int tag, boolean isShowTip) {
        if (customLoadding != null && customLoadding.isShowing()) {
            customLoadding.dismiss();
        }
        if (isShowTip) {
            CustomToast.showToast("网络超时");
        }
    }

    public void back(View v) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            FileUtil.deleteFiles(ConfigManager.DOWNLOADIMAGE);
        }
    }
}
