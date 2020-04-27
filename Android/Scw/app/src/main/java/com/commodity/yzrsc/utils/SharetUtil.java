package com.commodity.yzrsc.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.model.ShareEntity;
import com.commodity.yzrsc.ui.dialog.CustomLoadding;
import com.commodity.yzrsc.ui.dialog.ShareDialog;
import com.commodity.yzrsc.ui.pay.wx.WXUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxuqiang on 2017/4/1.
 */

public class SharetUtil {

    private static List<String> imageURLs;
    private static String downloadimagepath=ConfigManager.DOWNLOADIMAGE;
    private static Context context;
    private static CustomLoadding customLoadding;
    private static String weixindesc;

    public static void show(final Context mContext, final String url, final Bitmap bitmap, final String title, final String content, final ShareCallBack callBack, final List urls){
        context=mContext;
        ShareAction shareAction = new ShareAction((Activity) mContext)
                .setDisplayList(
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                        ,SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                        ,SHARE_MEDIA.SMS
                )
                .addButton("umeng_button_saveimage", "umeng_button_saveimage", "icon_download", "icon_download")
                .addButton("umeng_sharebutton_copyurl", "umeng_sharebutton_copyurl", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            ClipboardManager clipboardManager = (ClipboardManager) MainApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipurl = ClipData.newPlainText("url", url);
                            clipboardManager.setPrimaryClip(clipurl);
                            Toast.makeText((Activity) mContext, "复制链接成功", Toast.LENGTH_LONG).show();

                        } else if (snsPlatform.mShowWord.equals("umeng_button_saveimage")){
                            ImageUtil.saveImageToGallery(mContext,bitmap);
                        }else if (snsPlatform.mShowWord.equals("umeng_socialize_text_weixin_circle_key")){
                            shareMoreImage(urls, mContext,content);

                        }else {
                            UMImage umImage = new UMImage((Activity) mContext, bitmap);
                            new ShareAction((Activity) mContext).withMedia(umImage)
                                    .setPlatform(share_media)
                                    .setCallback(new CustomShareListener((Activity) mContext,callBack))
                                    .share();
                        }
                    }
                });
        shareAction.open();

    }

    public static void shareMoreImage(List urls, Context mContext,String d) {
        context=mContext;
        weixindesc=d;
        FileUtil.deleteFiles(ConfigManager.DOWNLOADIMAGE);
        imageURLs = urls;
        customLoadding = new CustomLoadding(mContext);
        customLoadding.setTip("准备分享");
        customLoadding.show();
        new Thread(runnable).start();
    }

    public static void showShop(final Context mContext, final String url, final Bitmap bitmap, final String title, final String content, final ShareCallBack callBack){
        ShareAction shareAction = new ShareAction((Activity) mContext)
                .setDisplayList(
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE
                        ,SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                        ,SHARE_MEDIA.SMS
                )
                .addButton("umeng_button_saveimage", "umeng_button_saveimage", "icon_download", "icon_download")
                .addButton("umeng_sharebutton_copyurl", "umeng_sharebutton_copyurl", "umeng_socialize_copyurl", "umeng_socialize_copyurl")
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                        if (snsPlatform.mShowWord.equals("umeng_sharebutton_copyurl")) {
                            ClipboardManager clipboardManager = (ClipboardManager) MainApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipurl = ClipData.newPlainText("url", url);
                            clipboardManager.setPrimaryClip(clipurl);

                            Toast.makeText((Activity) mContext, "复制链接成功", Toast.LENGTH_LONG).show();

                        } else if (snsPlatform.mShowWord.equals("umeng_button_saveimage")){
                            ImageUtil.saveImageToGallery(mContext,bitmap);

                        }else {
                            UMWeb web = new UMWeb(url);
                            web.setTitle(title);
                            web.setDescription(content);
//                            web.setThumb(new UMImage((Activity) mContext, bitmap));
                            new ShareAction((Activity) mContext).withMedia(web)
                                    .setPlatform(share_media)
                                    .setCallback(new CustomShareListener((Activity) mContext,callBack))
                                    .share();
                        }
                    }
                });
        shareAction.open();

    }
    private static class CustomShareListener implements UMShareListener {

        private ShareCallBack callBack;
        private WeakReference<Activity> mActivity;

        private CustomShareListener(Activity activity,ShareCallBack callBack) {
            mActivity = new WeakReference(activity);
            this.callBack=callBack;
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            callBack.onResult();

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                        && platform != SHARE_MEDIA.EMAIL
                        && platform != SHARE_MEDIA.FLICKR
                        && platform != SHARE_MEDIA.FOURSQUARE
                        && platform != SHARE_MEDIA.TUMBLR
                        && platform != SHARE_MEDIA.POCKET
                        && platform != SHARE_MEDIA.PINTEREST

                        && platform != SHARE_MEDIA.INSTAGRAM
                        && platform != SHARE_MEDIA.GOOGLEPLUS
                        && platform != SHARE_MEDIA.YNOTE
                        && platform != SHARE_MEDIA.EVERNOTE) {
                    Toast.makeText(mActivity.get(), " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
                    && platform != SHARE_MEDIA.EMAIL
                    && platform != SHARE_MEDIA.FLICKR
                    && platform != SHARE_MEDIA.FOURSQUARE
                    && platform != SHARE_MEDIA.TUMBLR
                    && platform != SHARE_MEDIA.POCKET
                    && platform != SHARE_MEDIA.PINTEREST

                    && platform != SHARE_MEDIA.INSTAGRAM
                    && platform != SHARE_MEDIA.GOOGLEPLUS
                    && platform != SHARE_MEDIA.YNOTE
                    && platform != SHARE_MEDIA.EVERNOTE) {
                Toast.makeText(mActivity.get(), " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
                callBack.onError();
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            callBack.onCancel();
//            Toast.makeText(mActivity.get(),   " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }
    public static void share(final Activity activity, SHARE_MEDIA media, final String content,UMImage UMImage,String url){
//        UMWeb web = new UMWeb(url);
//        web.setTitle("文巴掌商城");
//        web.setDescription(content);
//        web.setThumb(UMImage);
        new ShareAction(activity).setPlatform(media)
                .withMedia(UMImage)
                .setCallback(new CustomShareListener(activity, new ShareCallBack() {
                    @Override
                    public void onResult() {

                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }))
                .share();
    }
    public static void shareUrl(final Activity activity, SHARE_MEDIA media,String url,String desc,UMImage umimage){
        UMWeb web = new UMWeb(url);
        web.setThumb(umimage);
        web.setDescription(desc);
        web.setTitle("文巴掌商城");
        new ShareAction(activity).setPlatform(media)
                .withMedia(web)
                .setCallback(new CustomShareListener(activity, new ShareCallBack() {
                    @Override
                    public void onResult() {

                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }))
                .share();
    }
    public static void shareShopUrl(final Activity activity, SHARE_MEDIA media,String url,String desc,UMImage umimage){
        UMWeb web = new UMWeb(url);
        web.setThumb(umimage);
        web.setDescription(desc);
        web.setTitle("文巴掌商城");
        new ShareAction(activity).setPlatform(media)
                .withMedia(web)
                .setCallback(new CustomShareListener(activity, new ShareCallBack() {
                    @Override
                    public void onResult() {

                    }

                    @Override
                    public void onError() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }))
                .share();
    }

    public static void toShare(final Context mContext, String icopath, final String shareUrl, final String title, final String description, final List urls){
        ShareEntity shareEntity = new ShareEntity();
        shareEntity.desc=description;
        shareEntity.images=urls;
        shareEntity.imageUrl=icopath;
        shareEntity.shareURL= shareUrl;
        ShareDialog shareDialog = new ShareDialog(mContext,shareEntity);
        shareDialog.show();
    }
    private static Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(customLoadding!=null&&customLoadding.isShowing()){
                        customLoadding.dismiss();
                    }
                    break;
                case 1:
                    if(customLoadding!=null&&customLoadding.isShowing()){
                        customLoadding.dismiss();
                    }
                    Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private static Runnable runnable=new Runnable(){
        @Override
        public void run() {
            try {
                uploadImage(imageURLs);
                File file = new File(downloadimagepath);
                File[] files = file.listFiles();
                shareMultiplePictureToTimeLine(context,files);
                handler.sendEmptyMessage(0);
            } catch (IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(0);
                Log.e("-->","图片下载失败");
                FileUtil.deleteFiles(downloadimagepath);
            }
        }
    };

    public static void uploadImage(List<String> urls) throws IOException {
        for (int i=0;i<urls.size()&&i<9;i++) {
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(urls.get(i));
            saveFile(bitmap,System.currentTimeMillis()+".png");
        }
    }

    public static void shareMultiplePictureToTimeLine(Context context,File[] files) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");

        ArrayList<Uri> imageUris = new ArrayList<Uri>();
        for (File f : files) {
            imageUris.add(Uri.fromFile(f));
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.putExtra("Kdescription",weixindesc);
//        context.startActivity(intent);
//        ((Activity)context).startActivityForResult(intent,1000);
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, WXUtils.WX_APP_ID);
        wxapi.registerApp(WXUtils.WX_APP_ID);
        if(!wxapi.isWXAppInstalled()&& !wxapi.isWXAppSupportAPI()){
            Toast.makeText(context,"请安装微信",Toast.LENGTH_SHORT).show();
        }
        if(context instanceof  FragmentActivity){
            ((FragmentActivity)context).startActivityForResult(intent,1000);
        }else {
            ((Activity)context).startActivityForResult(intent,1000);
        }
    }
    public static void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(downloadimagepath);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File myCaptureFile = new File(dirFile ,File.separator+fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    public static void saveImages(final Context context2, final List<String> images){
        context=context2;
        customLoadding = new CustomLoadding(context);
        customLoadding.setTip("正在下载");
        customLoadding.show();
        new Thread(){
            @Override
            public void run() {
                super.run();
                for (String s:images){
                    Bitmap bitmap = ImageLoader.getInstance().loadImageSync(s);
                    ImageUtil.saveImageToGallery2(context,bitmap);
                }
                handler.sendEmptyMessage(1);
            }
        }.start();
    }

}
