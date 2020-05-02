package com.commodity.yzrsc.http;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.commodity.yzrsc.manager.ConfigManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 上传
 * Created by 328789 on 2017/4/14.
 */

public class UpLoadUtils {
    private static UpLoadUtils upLoadUtils;
    private OkHttpClient mOkHttpClient;
    /**1分钟*/
    protected static final long CONNECT_TIME_OUT = 1 *60L;
    /**1分钟*/
    protected static final long WRITE_TIME_OUT = 1 *60L;
    /**1分钟*/
    protected static final long READ_TIME_OUT = 1 *60L;

    public UpLoadUtils() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .build();
    }
    public static UpLoadUtils instance(){
        if(upLoadUtils==null){
            synchronized (UpLoadUtils.class){
                if(upLoadUtils==null){
                    upLoadUtils=new UpLoadUtils();
                }
            }
        }
       return upLoadUtils;
    }
    /*
     *上传图片
     */
    public void uploadPicture(String url,MultipartBody.Builder multiparBody, final Callback callback){

        Request build = new Request.Builder().url(url)
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Charset", "UTF-8")
                .addHeader("Accept", "application/json")
                .addHeader("scw-token", ConfigManager.instance().getUser().getDeviceToken())
                .post(multiparBody.build())
                .build();
        mOkHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call,response);
            }
        });
    }
    /*
     *上传图片视频
     */
    public void uploadPicture1(String url,MultipartBody.Builder multiparBody, final Callback callback){

        Request build = new Request.Builder().url(url)
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Charset", "UTF-8")
                .addHeader("Accept", "application/json")
                .addHeader("scw-token", "309AA687789A2FFD47404D063005282D")
                .post(multiparBody.build())
                .build();
        mOkHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call,response);
            }
        });
    }

    /**
     * post请求
     * @param url
     * @param body
     * @param callback
     */
    public void jsonRequest(String url,RequestBody body,final Callback callback){
        Request build = new Request.Builder().url(url)
                .addHeader("Charset", "UTF-8")
                .addHeader("Accept", "application/json")
                .addHeader("scw-token", ConfigManager.instance().getUser().getDeviceToken())
                .post(body)
                .build();
        mOkHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call,response);
            }
        });
    }
    /**
     * 获取微信payid
     */
    public void requestPayId(String url,RequestBody body,final Callback callback){
        Request build = new Request.Builder().url(url)
                .addHeader("Content-type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("scw-token", ConfigManager.instance().getUser().getDeviceToken())
                .post(body)
                .build();
        mOkHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call,response);
            }
        });
    }
    /**
     * 获取微信payid
     */
    public void requesDynamic(String url, FormBody body, final Callback callback){
        Request build = new Request.Builder().url(url)
                .addHeader("Content-type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("scw-token", "309AA687789A2FFD47404D063005282D")
                .post(body)
                .build();
        mOkHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call,response);
            }
        });
    }
    /**
     * 确认订单支付成功
     */
    public void confirmOrder(String ordeId,String payment,final Callback callback){
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\n" +
                "  \"orderId\": " + ordeId + "\n" +
                "}");
        Request build = new Request.Builder().url(IRequestConst.RequestMethod.IsOrderPaid)
                .addHeader("Content-type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("scw-token", ConfigManager.instance().getUser().getDeviceToken())
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callback.onResponse(call,response);
            }
        });
    }
}
