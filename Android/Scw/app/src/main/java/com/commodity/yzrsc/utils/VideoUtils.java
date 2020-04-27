package com.commodity.yzrsc.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.manager.ConfigManager;

import java.io.File;

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
}
