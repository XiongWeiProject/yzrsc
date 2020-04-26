package com.commodity.scw.utils;

import android.content.Context;
import android.net.Uri;


import com.commodity.scw.manager.SPManager;
import com.commodity.scw.rongyun.SealConst;
import com.commodity.scw.rongyun.SealUserInfoManager;
import com.commodity.scw.rongyun.server.utils.NLog;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by liyushen on 2017/5/6 09:01
 * 聊天工具类
 */
public class RongIMUtil {
    //链接融云服务器
    public static  void connectRongIM(final String token ){
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                NLog.e("融云connect", "onTokenIncorrect");
            }

            @Override
            public void onSuccess(String s) {
                NLog.e("融云connect", "onSuccess userid:" + s);
                SPManager.instance().setString(SealConst.SEALTALK_LOGIN_ID, s);
                SealUserInfoManager.getInstance().openDB();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                NLog.e("connect", "onError errorcode:" + errorCode.getValue());
            }
        });
    }

    //打开聊天界面
    public static  void startConversation(Context context, String targetId, String title){
        RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE,targetId,title);
    }

    //更新用户信息
    public static  void updateUserInfo(String id, String name, String avator){
        io.rong.imlib.model.UserInfo  userInfo =  new io.rong.imlib.model.UserInfo(id,name, Uri.parse(avator));
        RongIM.getInstance().refreshUserInfoCache(userInfo);
    }
}
