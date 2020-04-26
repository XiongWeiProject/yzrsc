/*
 * Created by DaiHui
 * 
 * Date:2014年9月19日下午5:20:21 
 * 
 * Copyright (c) 2014, Show(R). All rights reserved.
 * 
 */
package com.commodity.scw.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.commodity.scw.MainApplication;
import com.commodity.scw.R;
import com.commodity.scw.http.BaseHttpManager.IRequestListener;
import com.commodity.scw.http.BaseHttpManager.IUploadFileListener;
import com.commodity.scw.ui.SwipeBackBaseActivity;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Function: Http通信响应
 * <p/>
 * Date: 2016年3月30日
 *
 * @author liyushen
 */
@SuppressWarnings("unused")
public class HttpResponse {
    private static final String TAG = "HttpResponse";
    private static final String STATUS = "status";
    private static final String CODE = "code";
    private static final String DATA = "data";
    private static final String MESSAGE = "message";
    private static final String SUC_STATUS = "200";
    private static final String SUC_STATUS_CREATE = "201";
    public static final String FAIL_STATUS = "-1";
    private IRequestListener mListener;
    private String mStatus;// 状态码
    private String mCode;// 错误码
    private String mMsg;// 错误信息
    private String resultContent="";// data对应json
    private Object mResponse;// data对应json
    private int totalCount=0;//总数
    private int totalPage=0;//总页数
    private int tag = 0;
    public boolean isShowTimeOutTip = true;

    /**
     * 构造函数
     */
    public HttpResponse(int tag, IRequestListener listener) {
        mListener = listener;
        this.tag = tag;
    }

    /**
     * 预置事件回调
     */
    public void preExecute() {
        if (mListener != null) {
            if (mListener instanceof IRequestListener) {
                (mListener).onPreExecute(tag);
            }
        }
    }

    /**
     * 超时事件回调
     */
    public void timeOut(final boolean isShowTip) {
        if (mListener != null) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                Handler response = new Handler(Looper.getMainLooper());
                response.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mListener instanceof IRequestListener) {
                            (mListener).OnTimeOut(tag, isShowTip);
                        }
                    }
                });
                return;
            }
        }
    }

    /**
     * 无网络事件回调
     */
    public void NetError(boolean isShowTip) {
        if (mListener != null) {
            if (mListener instanceof IRequestListener) {
                (mListener).OnNetError(tag, isShowTip);
                if (SwipeBackBaseActivity.evevt!=null){
                    SwipeBackBaseActivity.evevt.onNetChange(false);
                }
            }
        }
    }

    /**
     * 处理响应内容
     *
     * @param responseContent
     * @throws JSONException
     */
    public synchronized void handleResponse(HttpParam param, String responseContent, int httpCode, String httpMsg) throws JSONException {
        try {
            resultContent=responseContent;
            if (httpMsg != null && httpMsg.equals(MainApplication.getContext().getString(
                    R.string.TIP_NETOUTTIME))) {
                timeOut(isShowTimeOutTip);//超时操作
                return;
            }
            if (!TextUtils.isEmpty(responseContent)) {
                JSONObject  mResponseObject = null;
                try {
                       mResponseObject = new JSONObject(responseContent);
                } catch (Throwable e) {
                }
                if (HttpStatus.SC_OK <= httpCode && httpCode < HttpStatus.SC_MULTIPLE_CHOICES) {

                    if (param.ismIsExternalLink()) {
                        // 访问外部链接
                        setStatus(SUC_STATUS);
                        setDataResponse(responseContent);
                    } else {
                        setStatus(httpCode + "");
                        setDataResponse(mResponseObject);
                    }
                    Log.e(TAG, "请求成功内容:" + httpCode +"\n"+ responseContent);
                } else if(httpCode==409){
                    setError(httpCode, responseContent);
                }else {
                    if (mResponseObject!=null){
                        httpMsg= mResponseObject.optString("message");
                    }
                    setError(httpCode, httpMsg);
                }
            } else {
                setError(httpCode, httpMsg);
            }
        } catch (Throwable e) {
            setError(httpCode, httpMsg);
            Log.d(TAG, "异常信息"+httpCode+"|||"+httpMsg);
            return;
        }
        callback(responseContent);
    }

    /**
     * 处理响应内容
     *
     * @param responseContent
     * @throws JSONException
     */
    public synchronized void handleResponse(HttpParamNew param, String responseContent, int httpCode, String httpMsg) throws JSONException {
        try {
            resultContent=responseContent;
            if (httpMsg != null && httpMsg.equals(MainApplication.getContext().getString(
                    R.string.TIP_NETOUTTIME))) {
                timeOut(isShowTimeOutTip);//超时操作
                return;
            }
            if (!TextUtils.isEmpty(responseContent)) {
                JSONObject  mResponseObject = null;
                try {
                    mResponseObject = new JSONObject(responseContent);
                } catch (Throwable e) {
                }
                if (HttpStatus.SC_OK <= httpCode && httpCode < HttpStatus.SC_MULTIPLE_CHOICES) {

                    if (param.ismIsExternalLink()) {
                        // 访问外部链接
                        setStatus(SUC_STATUS);
                        setDataResponse(responseContent);
                    } else {
                        setStatus(httpCode + "");
                        setDataResponse(mResponseObject);
                    }
                    Log.e(TAG, "请求成功内容:" + httpCode +"\n"+ responseContent);
                } else if(httpCode==409){
                    setError(httpCode, responseContent);
                }else {
                    if (mResponseObject!=null){
                        httpMsg= mResponseObject.optString("message");
                    }
                    setError(httpCode, httpMsg);
                }
            } else {
                setError(httpCode, httpMsg);
            }
        } catch (Throwable e) {
            setError(httpCode, httpMsg);
            Log.d(TAG, "异常信息"+httpCode+"|||"+httpMsg);
            return;
        }
        callback(responseContent);
    }

    /**
     * 处理进度回调
     *
     * @param progress
     * @throws Exception
     */
    public void handleProgressResponse(int progress) throws Exception {
        callbackProgress(progress);
    }

    /**
     * 设置错误信息
     *
     * @param httpCode 错误码
     * @param httpMsg  错误信息
     */
    private void setError(int httpCode, String httpMsg) {
        setStatus(FAIL_STATUS);
        setCode(String.valueOf(httpCode));
        setMsg(httpMsg);
        Log.e(TAG, "请求失败内容:" + httpCode +";\n"+ httpMsg+"\n"+resultContent);
    }

    /**
     * 执行回调
     */
    private void callback(final String responseContent) {
        if (mListener != null) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                Handler response = new Handler(Looper.getMainLooper());
                response.post(new Runnable() {
                    @Override
                    public void run() {
                        dealWithCallback(responseContent);
                    }
                });
                return;
            }
            dealWithCallback(responseContent);
        }
    }

    /**
     * 处理回调内容
     *
     * @param responseContent 所有json数据
     */
    private void dealWithCallback(String responseContent) {
        if (TextUtils.equals(getStatus(), SUC_STATUS) || TextUtils.equals(getStatus(), SUC_STATUS_CREATE)) {
            ServiceInfo info = new ServiceInfo();
            info.setCode(getCode());
            info.setMsg(getMsg());
            info.setStatus(getStatus());
            info.setResponse(getResponse());
            info.setTotalCount(getTotalCount());
            info.setTotalPage(getTotalPage());
            mListener.onSuccess(tag, info);
        } else {
            mListener.onError(tag, getCode(), getMsg());
        }
    }

    /**
     * 执行进度回调（上传文件）
     *
     * @param progress 进度
     * @throws Exception
     */
    private void callbackProgress(final int progress) throws Exception {
        if (mListener != null) {
            if (mListener instanceof IUploadFileListener) {
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    Handler response = new Handler(Looper.getMainLooper());
                    response.post(new Runnable() {

                        @Override
                        public void run() {
                            ((IUploadFileListener) mListener).OnUploadProgress(progress);
                        }
                    });
                    return;
                }
            }
        }
    }

    /**
     * 获取状态码
     *
     * @return the mStatus
     */
    public String getStatus() {
        if (TextUtils.isEmpty(mStatus)) {
            return FAIL_STATUS;
        }
        return mStatus;
    }

    /**
     * 设置状态码
     *
     * @param mStatus the mStatus to set
     */
    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    /**
     * 获取错误代码
     *
     * @return the mCode
     */
    public String getCode() {
        if (TextUtils.isEmpty(mCode)) {
            return "0";
        }
        return mCode;
    }

    /**
     * 设置错误代码
     *
     * @param mCode the mCode to set
     */
    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    /**
     * 获取错误信息
     *
     * @return the mMsg
     */
    public String getMsg() {
        if (TextUtils.isEmpty(mMsg)) {
            return "";
        }
        return mMsg;
    }

    /**
     * 设置错误信息
     *
     * @param mMsg the mMsg to set
     */
    public void setMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    /**
     * 获取解析数据
     *
     * @return the mResponseJson
     */
    // public JSONObject getResponseJson() {
    // return mResponseJson;
    // }

    /**
     * 获取data对应json数据
     *
     * @return the mDataResponse
     */
    public Object getResponse() {
        return mResponse;
    }

    /**
     * 设置data对应json数据
     *
     * @param
     */
    public void setDataResponse(Object response) {
        this.mResponse = response;
    }

    /**
     * 解析data数据
     *
     * @return the mDataResponse
     */
    private Object parseDataResponse(JSONObject responseJsonObj) {
        if (responseJsonObj != null) {
            mResponse = responseJsonObj.optJSONArray(DATA);
            if (mResponse == null) {
                mResponse = responseJsonObj.optJSONObject(DATA);
            }
        }
        return mResponse;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
