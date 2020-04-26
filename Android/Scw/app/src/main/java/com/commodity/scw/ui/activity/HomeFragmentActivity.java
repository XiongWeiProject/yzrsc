package com.commodity.scw.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.commodity.scw.MainApplication;
import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.AppManager;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.ottobus.BusProvider;
import com.commodity.scw.ottobus.Event;
import com.commodity.scw.ui.BaseFragmentActivity;
import com.commodity.scw.ui.SwipeBackBaseActivity;
import com.commodity.scw.ui.activity.user.MessageManagerActivity;
import com.commodity.scw.ui.fragment.HomeMarketFragment;
import com.commodity.scw.ui.fragment.HomeMineFragment;
import com.commodity.scw.ui.fragment.HomeShopFragment;
import com.commodity.scw.ui.fragment.HomeTypeFragment;
import com.commodity.scw.ui.widget.layout.TabMenuItem;
import com.commodity.scw.utils.RongIMUtil;
import com.commodity.scw.utils.ScreenUtils;
import com.squareup.otto.Subscribe;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 作者：liyushen on 2016/4/5 16:15 功能：主页 　
 */
public class HomeFragmentActivity extends BaseFragmentActivity {
    public static boolean isForeground = false;
    private Fragment mCurFragment;
    LinearLayout ll_menu;
    long exitTime = 0;
    int tab = 0;
    int tabNum = 4;
    private Fragment[] fragments;
    @Override
    public boolean getNeedRemove() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_homefragment;
    }

    @Override
    protected void initView() {
        ll_menu = findView(R.id.ll_tabmenu);
        fragments = new Fragment[tabNum];
        //初始化加载首页
        fragments[tab]=new HomeMarketFragment();
        mCurFragment=fragments[tab];
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container_layout, mCurFragment).commit();

        //加载底部tab
        ll_menu.removeAllViews();
        ll_menu.addView(setMenuItem(R.drawable.ico_home_normal,
                R.drawable.ico_home_press, R.string.first_home, 0, 0));
        ll_menu.addView(setMenuItem(R.drawable.ico_type_normal,
                R.drawable.ico_type_press, R.string.second_home, 0, 1));
        ll_menu.addView(setMenuItem(R.drawable.ico_shop_normal,
                R.drawable.ico_shop_press, R.string.third_home, 0, 2));
        ll_menu.addView(setMenuItem(R.drawable.ico_mine_normal,
                R.drawable.ico_mine_press, R.string.fourth_home, 0, 3));
        tabNum=ll_menu.getChildCount();
        //默认选择第几个tab
        doClickTabItem(tab,0);
        //连接融云服务器 4yVNxreUw0x5CzOcVNyOXa+YsUIoF3ojin3K277sfOkWnrAzSqHtxTlwdOPZGuBkqNP0jLAlqcJaDMVsghR0EqtdpZUyLdaH
        RongIMUtil.connectRongIM(ConfigManager.instance().getUser().getImToken());
        initNotice();
    }
    @Override
    protected void initListeners() {
        if (SPKeyManager.ListenTextView==null){
            SPKeyManager.ListenTextView=new TextView(this);
        }
        SPKeyManager.ListenTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("关闭程序")){
                    finish();
                }else if (editable.toString().equals("其他")){

                }
            }
        });
    }

    /**
     * 点击tab事件哈哈
     *
     * @param index
     * @param tipnum
     */
    public void doClickTabItem(int index,int tipnum) {
        refreshTab(index);
        if (fragments[index]==null){
            if (index==0){
                 fragments[index]=new HomeMarketFragment();
            }else  if (index==1){
                fragments[index]=new HomeTypeFragment();
            }else  if (index==2){
                fragments[index]=new HomeShopFragment();
            }else  if (index==3){
                fragments[index]=new HomeMineFragment();
            }else {
                 fragments[index]=new HomeMarketFragment();
            }
        }
        switchFragment(fragments[index]);
    }

    //选择的Fragment
    private void switchFragment(Fragment fragment) {
        if (fragment != mCurFragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFragment).add(R.id.container_layout, fragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFragment).show(fragment).commit();
            }
            mCurFragment = fragment;
//            if(fragment instanceof HomeShopFragment){
//                ((HomeShopFragment) fragment).refreshList();
//            }
        }
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            // System.currentTimeMillis()无论何时调用，肯定大于2000
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(mContext, getString(R.string.app_exit_tip), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                AppManager.getAppManager().AppExit(mContext);
                noAnimFinish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject jsonObject= null;
            try {
                jsonObject = new JSONObject((String) resultInfo.getResponse());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url="";
            url = jsonObject.optString("updateUrl");
            Log.e( "qingje: ", jsonObject.optString("appTypeName"));
            SPManager.instance().setString(SPKeyManager.UPDATE_URL, url);
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            Map<String,String> parmMap=new HashMap<>();
            parmMap.put("upgradeType", "477");
            parmMap.put("v", MainApplication .instance().getVersionCode() + "");
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.MobileUpdate, parmMap , this);
            httpManager.request();
        }
    }

    //获取当前显示第几个tab
    public int getTab() {
        return tab;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10001) {
                if (tab == 0 ) {

                }
            }
        }
    }


    //刷新底部tab显示
    private void refreshTab(int index) {
        for (int i = 0; i < tabNum; i++) {
            if (index==i){
                ((TabMenuItem) ll_menu.getChildAt(i)).setSelect(true);
            }else {
                ((TabMenuItem) ll_menu.getChildAt(i)).setSelect(false);
            }
        }
    }

    /**
     * 设置tab项
     *
     * @param iconDrawaleID       默认图标
     * @param iconClickDrawableID 选中图标
     * @param textID              默认文本
     * @param tipnum              消息数目
     * @param tabIndex            坐标
     * @return
     */
    public TabMenuItem setMenuItem(int iconDrawaleID,  final int iconClickDrawableID, int textID, final int tipnum, final int tabIndex) {
        final TabMenuItem tabMenuItem = new TabMenuItem(mContext,iconDrawaleID,iconClickDrawableID,textID,tipnum,tabIndex);
        tabMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tab = tabIndex;
                doClickTabItem(tabIndex, 0);
            }
        });
        tabMenuItem.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / tabNum, LinearLayout.LayoutParams.MATCH_PARENT));
        return tabMenuItem;
    }


    //获取传递过来信息
    @Subscribe
    public void ReceiverRongyunMessage(Event.ReceiverRongyunMessage event) {
        Log.e( "新的融云消息: ","" + event.getUserid() );
        notif.contentView.setTextViewText(R.id.tv_name, "新的消息，请打开消息管理查看");
        manager.notify(0, notif);//启动notice

    }

    @Override
    protected void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
        SwipeBackBaseActivity.evevt = null;
    }


    //以下显示通知栏消息
    NotificationManager manager;
    Notification notif;

    private void initNotice(){
        Intent intent = new Intent(HomeFragmentActivity.this, MessageManagerActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(HomeFragmentActivity.this,
                0, intent, 0);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif = new Notification();
        notif.icon = R.drawable.ic_launcher;
        notif.tickerText = "新的消息";
        //   notif.flags |= Notification.FLAG_NO_CLEAR;//
        // 通知栏显示所用到的布局文件
        notif.contentView = new RemoteViews(getPackageName(),
                R.layout.layout_notification);
        notif.contentIntent = pIntent;
       // manager.notify(0, notif);//启动notice
      //  manager.cancel(0);//取消notice

        //消息免打扰
//        RongIM.getInstance().setNotificationQuietHours("00:00:00", 1439, new RongIMClient.OperationCallback() {
//            @Override
//            public void onSuccess() {
//                SharedPreferences.Editor editor;
//                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
//                editor = sp.edit();
//                editor.putBoolean("isOpenDisturb", false);
//                editor.apply();
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode errorCode) {
//
//            }
//        });
    }

    @Override
    public boolean booNetErrorFinish() {
        return false;
    }
}
