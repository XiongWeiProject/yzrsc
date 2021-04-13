package com.commodity.yzrsc.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.boardcast.NetBroadcastReceiver;
import com.commodity.yzrsc.manager.SystemPermissUtil;
import com.commodity.yzrsc.ui.dialog.NetErrorTipDialog;
import com.commodity.yzrsc.ui.widget.swipbackhelper.SwipeBackHelper;
import com.commodity.yzrsc.ui.widget.swipbackhelper.SwipeListener;
import com.commodity.yzrsc.utils.LogUtil;


/**
 * 作者：liyushen on 2016/9/5 16:59
 * 功能　滑动返回
 */
public class SwipeBackBaseActivity extends FragmentActivity implements NetBroadcastReceiver.NetEvevt {
    public static NetBroadcastReceiver.NetEvevt evevt;
    boolean isNeedRemove=true;
    boolean booNetErrorFinish=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
      //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)//获取当前页面
                .setSwipeBackEnable(getNeedRemove())//设置是否可滑动
                .setSwipeEdge(200)//可滑动的范围。px。200表示为左边200px的屏幕
                .setSwipeEdgePercent(0.1f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                .setSwipeSensitivity(1)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
                .setScrimColor(Color.TRANSPARENT)//底层阴影颜色
                .setClosePercent(0.2f)//触发关闭Activity百分比
                .setSwipeRelateEnable(true)//是否与下一级activity联动(微信效果)。默认关
                .setSwipeRelateOffset(500)//activity联动时的偏移量。默认500px。
                .setDisallowInterceptTouchEvent(false)//不抢占事件，默认关（事件将先由子View处理再由滑动关闭处理）
                .addListener(new SwipeListener() {//滑动监听
                    @Override
                    public void onScroll(float percent, int px) {//滑动的百分比与距离
                    }

                    @Override
                    public void onEdgeTouch() {//当开始滑动
                    }

                    @Override
                    public void onScrollToClose() {//当滑动关闭
                    }
                });
//        MainApplication.USER_AGENT= SystemPermissUtil.instance().getUserAgent(this);
        evevt = this;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);

    }

    protected void onResume() {
        super.onResume();
    }

    //是否需要滑动finish
    public boolean getNeedRemove() {
        return isNeedRemove;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // Press the back button in mobile phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_right);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_right);
        if (netErrorTipDialog!=null&&netErrorTipDialog.isShowing()){
            netErrorTipDialog.dismiss();
        }
    }


    /**
     * 打印日志
     */
    public void log(Class<?> clazz, Object object){
        LogUtil.instance().e(clazz, object.toString());
    }

    // 无网络是否需要finish
    public boolean booNetErrorFinish() {
        return booNetErrorFinish;
    }

    NetErrorTipDialog netErrorTipDialog;
    @Override
    public void onNetChange(boolean isNetworkConnected) {
        if (!isNetworkConnected){//网络异常
            if (netErrorTipDialog==null){
                netErrorTipDialog=new NetErrorTipDialog(this);
                netErrorTipDialog.setClickSubmitListener(new NetErrorTipDialog.OnClickSubmitListener() {
                    @Override
                    public void clickSubmit() {
                        netErrorTipDialog.dismiss();
                        if (booNetErrorFinish()){
                            finish();
                        }
                    }
                });
            }
            try{
                if (netErrorTipDialog!=null&&!netErrorTipDialog.isShowing()){
                    netErrorTipDialog.show();
                }
            }catch (Exception e){

            }
        }else {
            if (netErrorTipDialog!=null&&netErrorTipDialog.isShowing()){
                netErrorTipDialog.dismiss();
            }
        }
    }
}
