package com.commodity.scw.ui.pay.zfb;

import android.os.Handler;
import android.os.Message;

/**
 * Created by yangxuqiang on 2017/5/11.
 */

public class ZFBHandler extends Handler {
    private final ZFCallBack callBack;

    public ZFBHandler(ZFCallBack callBack) {
        this.callBack=callBack;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case ZFBUtils.SDK_PAY_FLAG:
                callBack.success(msg.obj);
                break;
        }
    }
}
