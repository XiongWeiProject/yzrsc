package com.commodity.scw.utils;

import com.commodity.scw.http.BaseHttpManager;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.SPKeyManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyushen on 2017/5/28 10:59
 * 通用的请求，不想重复写
 */
public class HttpRequestUtil {
    //阅读消息
    public static void ReadNotification(String id,String type){
        Map<String, String> parmMap = new HashMap<String, String>();
        parmMap.put("id",id);
        parmMap.put("type",type);//消息类型 1:系统消息, 2:被关注消息, 3:被评价消息,
        HttpManager httpManager = new HttpManager(0, HttpMothed.POST,
                IRequestConst.RequestMethod.ReadNotification, parmMap, new BaseHttpManager.IRequestListener() {
            @Override
            public void onPreExecute(int Tag) {

            }

            @Override
            public void onSuccess(int Tag, ServiceInfo result) {

            }

            @Override
            public void onError(int Tag, String code, String msg) {

            }

            @Override
            public void OnTimeOut(int Tag, boolean isShowTip) {

            }

            @Override
            public void OnNetError(int Tag, boolean isShowTip) {

            }
        });
        httpManager.request();
    }
}
