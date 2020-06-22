package com.commodity.scw.services;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.commodity.yzrsc.R;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.ui.activity.HomeFragmentActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.Call;

/**
 * 作者：liyushen on 2016/6/3 14:11
 * 功能：版本更新
 */
@SuppressLint("HandlerLeak")
public class UpdateService extends Service {
    int numbermessage;
    String apkName;
    NotificationManager manager;
    Notification notif;
    int progress;
    int t = 0;

    @Override
    public IBinder onBind(Intent intent) {
        apkName = SPManager.instance().getString(SPKeyManager.UPDATE_URL);
        System.out.println("=====onbind=========>" + apkName);
        return null;
    }

    @SuppressWarnings("deprecation")
	@Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        OkHttpUtils//
                .get()//
                .url(apkName)//
                .build()//
                .execute(new FileCallBack(getExternalCacheDir().getPath(), "hotel.apk")//
                {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        manager.cancel(0);
                        if(Build.VERSION.SDK_INT >= 23&&(e instanceof FileNotFoundException)){
                            Toast.makeText(UpdateService.this,"更新失败,请打开APP存储权限!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(UpdateService.this, "更新失败,请检查网络!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onResponse(File file, int i) {
                        Log.e("onResponse", "onResponse :" + file.getAbsolutePath());
                        installApk(UpdateService.this, file);
                    }

                    @Override
                    public void inProgress(float v, long l, int id) {
                        progress = (int) (100 * v);
                        if (t != progress) {
//                            notif.contentView.setTextViewText(R.id.content_view_text1,
//                                    progress + "%");
//                            notif.contentView.setProgressBar(R.id.content_view_progress,
//                                    100, progress, false);
                            manager.notify(0, notif);
                        }
                        t = progress;
                        // handler.sendEmptyMessage(0);
                    }
                });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        apkName = SPManager.instance().getString(SPKeyManager.UPDATE_URL);
        System.out.println("=====command=========>" + apkName);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(UpdateService.this, HomeFragmentActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(UpdateService.this,
                0, intent, 0);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif = new Notification();
        notif.icon = R.drawable.ic_launcher;
        notif.tickerText = "酒店档期更新通知";
        //   notif.flags |= Notification.FLAG_NO_CLEAR;//
        // 通知栏显示所用到的布局文件
//        notif.contentView = new RemoteViews(getPackageName(),
//                R.layout.layout_notification);
        notif.contentIntent = pIntent;
        manager.notify(0, notif);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.cancel(0);
        System.out.println("======已销毁========>");
    }

    // 安装apk
    public void installApk(Context context, File filePath) {
        System.out.println("======成功========>");
//        Intent intent = new Intent();
//        // 执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        // 执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file),
//                "application/vnd.android.package-archive");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT > 23) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, "com.commodity.scw.fileprovider", filePath);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(filePath), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

        manager.cancel(0);
        onDestroy();
    }




}
