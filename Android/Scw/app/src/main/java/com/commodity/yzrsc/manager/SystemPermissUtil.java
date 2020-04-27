package com.commodity.yzrsc.manager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;

import cn.yohoutils.Model.VersionBaseInfo;
import cn.yohoutils.SafetyUtil;
import cn.yohoutils.SystemUtil;

/**
 * 作者：liyushen on 2017/2/14 16:14
 * 功能：针对Android7.0增加权限判断
 */
public class SystemPermissUtil {
    private static SystemPermissUtil instance;
    private String USER_AGENT="";
    public static SystemPermissUtil instance() {
        if (instance == null) {
            instance = new SystemPermissUtil();
        }
        return instance;
    }

    public SystemPermissUtil() {
    }

    /**
     * 判断存储权限
     * @param context
     */
    public boolean IsHasStoragePermiss(Context context){
        if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)&&
                (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 判断拨打电话权限
     * @param context
     */
    public boolean IsHasCallPermiss(Context context){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else {
            return true;
        }
    }


    /**
     * 是否打开存储权限界面
     * @param context
     */
    public void MakeStorage(Context context) {
        if (IsHasStoragePermiss(context)){
        }else {
            showTipsDialog(context,"存储");
            return;
        }
    }

    /**
     * 拨打电话
     * @param context
     * @param callStr
     */
    public void MakeCall(Context context, String callStr) {
        //用intent启动拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + callStr));
        if (IsHasCallPermiss(context)){
            context.startActivity(intent);
        }else {
            showTipsDialog(context,"电话");
            return;
        }
    }
    /**
     * 获取手机信息
     */
    public String getUserAgent(Context context) {
        if (USER_AGENT.isEmpty()){
            String VERNAME="";
            VersionBaseInfo versionInfo = SystemUtil.getAppVerInfo(context.getApplicationContext());
            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String deviceId="";
            if (IsHasCallPermiss(context)){
                deviceId= mTelephonyManager.getDeviceId();
            }else {
                deviceId = null ;
            }
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (deviceId == null) {
                deviceId = androidId;
            } else if (androidId != null) {
                deviceId = deviceId + androidId;
            }
            MainApplication.deviceId=deviceId;
            String appNameEn = context.getResources().getString(R.string.app_name_en);
            if (versionInfo != null) {
                VERNAME = versionInfo.mVerName;
            }
            long curTime = System.currentTimeMillis();
            curTime = curTime / 1000;
            String MD5 = deviceId + "_" + curTime;
            String checkSum = SafetyUtil.encryptStringToMd5(MD5, "32");

            StringBuilder mUserAgent = new StringBuilder();
            mUserAgent.append(deviceId).append(",").append(curTime).append(",")
                    .append(checkSum).append(",").append(android.os.Build.MODEL)
                    .append(",").append(MainApplication.SCREEN_W + "x" + MainApplication.SCREEN_H)
                    .append(",")
                    .append("Android " + android.os.Build.VERSION.RELEASE)
                    .append(",").append(appNameEn).append(" " + VERNAME)
                    .append(",").append("+8");

            USER_AGENT = mUserAgent.toString();
            Log.e(  "getUserAgent: ",USER_AGENT+"||==" );
        }
        return USER_AGENT;
    }

    /**
     * 显示提示对话框
     */
    public void showTipsDialog(final Context context, String str) {
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要"+str+"权限，请单击【确定】前往设置中心进行权限授权。")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings(context);
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     */
    public void startAppSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" +context. getPackageName()));
        context.startActivity(intent);
    }
}
