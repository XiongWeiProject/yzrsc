package com.commodity.yzrsc.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.manager.ConfigManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

/**
 * 拍摄视频
 * Created by 328789 on 2017/4/10.
 */

public class VideoUtils {
    public static String videoPath="/video.mp4";

    public static void openVideo(Activity activity, int code) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        File file = new File(getVideoPath(), videoPath);
        Uri videoUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10 * 1000);//时长10秒
//        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,1024*1024*10);//大小上线10M
        activity.startActivityForResult(intent, code);
    }
    public static Bitmap getThumbnail(String filePath){
        return ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.MICRO_KIND),
                ScreenUtils.dp2px(MainApplication.getContext(),60),ScreenUtils.dp2px(MainApplication.getContext(),60));
    }

    public static File getVideoPath() {
        File file = new File(ConfigManager.VIDEO_PATH);
        if(!file.exists()){
            file.mkdirs();
        }
        return file;
    }
    public static Bitmap getBitmapFormUrl(String url) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
        /*getFrameAtTime()--->在setDataSource()之后调用此方法。 如果可能，该方法在任何时间位置找到代表性的帧，
         并将其作为位图返回。这对于生成输入数据源的缩略图很有用。**/
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
    public static String bitmapToString(Bitmap bitmap){
        //将Bitmap转换成字符串
        String string=null;
        ByteArrayOutputStream bStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,bStream);
        byte[]bytes=bStream.toByteArray();
        string= Base64.encodeToString(bytes,Base64.DEFAULT);
        return string;
    }
}
