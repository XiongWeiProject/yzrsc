package com.commodity.yzrsc.utils;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.commodity.yzrsc.MainApplication;

/**
 * 作者：liyushen on 2016/4/15 11:01
 * 功能：自定义Toast 功能：居中显示toast，多次连续点击只弹出最后一次，不会重复显示多次toast
 */
public class CustomToast {
    private static Toast mToast;

    private static Handler mhandler = new Handler();

    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        };
    };

    public static void showToast(Context context, String text, int duration) {
        mhandler.removeCallbacks(r);
        if (null != mToast) {
            mToast.setText(text);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        mhandler.postDelayed(r, 5000);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void showToast(Context context, int strId, int duration) {
        showToast(context, context.getString(strId), duration);
    }
    public static void showToast(String error_text) {
        showToast(MainApplication.getContext(), error_text,0) ;
    }
    public static void showToast(int error_text) {
        showToast(MainApplication.getContext(), error_text+"", 0) ;
    }
}
