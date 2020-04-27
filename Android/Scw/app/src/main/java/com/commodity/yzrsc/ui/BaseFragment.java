package com.commodity.yzrsc.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.commodity.yzrsc.http.BaseHttpManager;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.ui.dialog.CustomLoadding;
import com.commodity.yzrsc.utils.CustomToast;

import butterknife.ButterKnife;

/**
 * 作者：liyushen on 2016/4/5 15:29
 * 功能：Fragment基类
 */
public abstract class BaseFragment extends Fragment implements BaseHttpManager.IRequestListener {

    public Context mContext;
    public View view;
    public CustomLoadding customLoadding;
    /**
     * 返回布局显示ID
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        customLoadding = new CustomLoadding(mContext);
        customLoadding.setCanceledOnTouchOutside(false);
        view = inflater.inflate(getContentView(), null);
        ButterKnife.bind(this, view);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        initView();
        initListeners();
        return view;
    }

    @SuppressWarnings("unchecked")
	protected final <T extends View> T findView(int id) {
        return (T) view.findViewById(id);
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
}
