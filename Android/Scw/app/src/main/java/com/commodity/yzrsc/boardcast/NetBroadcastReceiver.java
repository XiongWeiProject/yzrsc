package com.commodity.yzrsc.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.commodity.yzrsc.ui.SwipeBackBaseActivity;
import com.commodity.yzrsc.utils.NetWorkUtils;

/**
 * Created by liyushen on 2017/8/12 21:34
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    public NetEvevt evevt = SwipeBackBaseActivity.evevt;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            boolean netWorkState = NetWorkUtils.instance().isNetworkConnected(context);
            // 接口回调传过去状态的类型
            if (evevt!=null){
                evevt.onNetChange(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetEvevt {
        public void onNetChange(boolean isNetworkConnected);
    }
}
