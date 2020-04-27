package com.commodity.yzrsc.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.SystemPermissUtil;
import com.commodity.yzrsc.ui.activity.general.PicShowActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.yohoutils.FileUtil;
import cn.yohoutils.SafetyUtil;
import cn.yohoutils.StringUtil;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * 作者：liyushen on 2016/4/25 15:12
 * 功能：图片下载工具类
 * 产权：南京婚尚
 */
public class ImageUtil {
    /**
     * 下载图片
     *
     * @param url
     * @param path
     */
    public static void downLoadImage(String url, String path) {
        if (StringUtil.isEmpty(url) || StringUtil.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (file.exists()) {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            if (options.outHeight <= 0 || options.outWidth <= 0) {
                file.delete();
            } else {
                return;
            }
        }
        donwLoadFile(url, path);
    }

    /**
     * 下载文件
     *
     * @param urlString
     * @param path
     * @return
     */
    public static boolean donwLoadFile(String urlString, String path) {
        return donwLoadFile(urlString, path, null);
    }


    /**
     * 下载文件
     *
     * @param urlString
     * @param path
     * @return
     */
    public static boolean donwLoadFile(String urlString, String path, DownLoadListener listener) {
        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");
        InputStream in = null;
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        URL url = null;

        File filePath = new File(path);
        if (filePath.exists()) {
            if (listener != null) {
                listener.loadCompletion(1, 1);
                return true;
            }
        }
        try {
            url = new URL(urlString);
            if (url != null)
                urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection != null) {
                in = new BufferedInputStream(urlConnection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        File file = FileUtil.createFile(path);

        try {
            int totalSize = 1;
            if (urlConnection != null) {
                totalSize = urlConnection.getContentLength();
            }

            FileOutputStream fio = new FileOutputStream(file);
            out = new BufferedOutputStream(fio);
            int b;
            byte[] arry = new byte[1024];
            int downLoadSize = 0;
            while (in != null && (b = in.read(arry)) != -1) {
                out.write(arry, 0, b);
                downLoadSize += b;
                if (listener != null) {
                    listener.loading(downLoadSize, totalSize);
                }
            }
            out.flush();
            if (listener != null) {
                listener.loadCompletion(downLoadSize, totalSize);
            }
            if (out != null) {
                try {
                    out.close();
                } catch (final IOException e) {
                    Log.e("", "Error in downloadBitmap - " + e);
                }
            }
            if (file.length() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (final IOException e) {
            if (file.exists()) {
                file.delete();
            }
            Log.e("", "Error in downloadBitmap - " + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return false;
    }

    public interface DownLoadListener {
        // 下载中
        public void loading(int downLoadSize, int totalSize);

        // 下载完成
        public void loadCompletion(int downLoadSize, int totalSize);


    }

    /**
     * 保存一张图片到本地
     *
     * @param bmp
     * @param localPath
     */
    public static void saveBitmapToLocal(Bitmap bmp, String localPath) {

        if (bmp == null || localPath == null || localPath.length() == 0) {
            return;
        }

        FileOutputStream b = null;
        FileUtil.createFile(localPath);
        try {
            b = new FileOutputStream(localPath);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPublishTimeByMillis(long millis) {
        final long second = 1000;// 1秒
        final long secondMax = 60 * second;// 1分
        final long minuteMax = 60 * secondMax;// 1小时
        final long hourMax = 24 * minuteMax;// 1天
        final long dayMax = 5 * hourMax;// 5天
        long curTime = System.currentTimeMillis();
        if (millis <= 0) {
            millis = curTime;
        }
        String time = "";
        // 时间间隔
        long timeInterval = curTime - millis;

        String curDate = getTimeByMillis("yyyy", curTime);
        String millisData = getTimeByMillis("yyyy", millis);
        if (curDate.equals(millisData)) {
            // 同一年
            if (timeInterval < second) {
                time = "1秒前";
            } else if (timeInterval >= second && timeInterval < secondMax) {
                time = (timeInterval / second) + "秒前";
            } else if (timeInterval >= secondMax && timeInterval < minuteMax) {
                time = (timeInterval / secondMax) + "分钟前";
            } else if (timeInterval >= minuteMax && timeInterval < hourMax) {
                time = (timeInterval / minuteMax) + "小时前";
            } else if (timeInterval >= hourMax && timeInterval <= dayMax) {
                time = (timeInterval / hourMax) + "天前";
            } else if (timeInterval > dayMax) {
                time = getTimeByMillis("MM-dd HH:MM", millis);
            }
        } else {
            // 跨年
            time = getTimeByMillis(millis);
        }

        return time;
    }

    /**
     * 格式化时间
     *
     * @param millis
     * @return
     */
    public static String getTimeByMillis(long millis) {
        return getTimeByMillis("yyyy-MM-dd HH:MM", millis);
    }

    /**
     * 格式化时间
     *
     * @param millis
     * @return
     */
    public static String getTimeByMillis(String format, long millis) {

        long curTime = System.currentTimeMillis();
        if (millis <= 0) {
            millis = curTime;
        }

        String time = "";

        SimpleDateFormat dataformat = new SimpleDateFormat(format, Locale.CHINA);
        time = dataformat.format(new Date(millis));

        return time;
    }

    public static long getMillisByStringTime(String format, String time) {
        long millis = 0;
        SimpleDateFormat dataformat = new SimpleDateFormat(format, Locale.CHINA);
        Date date;
        try {
            date = dataformat.parse(time);
            millis = date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return millis;
    }

    public static int MEDIA_TYPE_IMAGE = 0;
    public static int MEDIA_TYPE_VIDEO = 1;

    public static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "You");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param mobile
     */
    public static void call(Context context, String mobile) {
        SystemPermissUtil.instance().MakeCall(context, mobile);

    }


    /**
     * 一段文字多个颜色
     *
     * @param tv
     * @param content
     * @param start
     * @param end
     * @param color   ：颜色，不能直接传R.color.xx,要先getResources
     */
    public static void setTextViewMultiColor(TextView tv, String content,
                                             int start, int end, int color) {
        if (StringUtil.isEmpty(content) || start > end) {
            return;
        }
        if (start < 0) {
            start = 0;
        }
        if (end >= content.length()) {
            end = content.length();
        }
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(style);
    }

    public static SpannableStringBuilder getMultiColor(String content,
                                                       int start, int end, int color, int fontSize) {
        if (StringUtil.isEmpty(content) || start > end) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end >= content.length()) {
            end = content.length();
        }
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        style.setSpan(new ForegroundColorSpan(color), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (fontSize > 0) {
            style.setSpan(new AbsoluteSizeSpan(fontSize, true), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return style;
    }

    public static SpannableStringBuilder getMultiSize(String content,
                                                      int start, int end, int fontSize) {
        if (StringUtil.isEmpty(content) || start > end) {
            return null;
        }
        if (start < 0) {
            start = 0;
        }
        if (end >= content.length()) {
            end = content.length();
        }
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        if (fontSize > 0) {
            style.setSpan(new AbsoluteSizeSpan(fontSize, true), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return style;
    }

    /**
     * 获取圖片角度
     *
     * @param path
     * @return
     */
    public static int getImageDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 根据uri获取绝对路径
     *
     * @param context
     * @param imageUri
     * @return
     */
    @SuppressLint("NewApi")
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }


    /**
     * 水平方向模糊度
     */
    private static float hRadius = 2;

    /**
     * 竖直方向模糊度
     */
    private static float vRadius = 2;

    /**
     * 模糊迭代度
     */
    private static int iterations = 1;

    /**
     * 柔化效果(高斯模糊)(优化后比上面快三倍)
     *
     * @param bmp isFriend 是否邀请好友界面过来
     * @return
     */
    public static Bitmap blurImageAmeliorate(Bitmap bmp) {
        Bitmap mbitmap = null;

        mbitmap = compress(bmp);

        // BitmapUtil.bitmapRecycle(bmp);
        int width = mbitmap.getWidth();
        int height = mbitmap.getHeight();
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        mbitmap.getPixels(inPixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < iterations; i++) {
            blur(inPixels, outPixels, width, height, hRadius);
            blur(outPixels, inPixels, height, width, vRadius);
        }
        blurFractional(inPixels, outPixels, width, height, hRadius);
        blurFractional(outPixels, inPixels, height, width, vRadius);
        bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static void blur(int[] in, int[] out, int width, int height,
                            float radius) {
        int widthMinus1 = width - 1;
        int r = (int) radius;
        int tableSize = 2 * r + 1;
        int divide[] = new int[256 * tableSize];

        for (int i = 0; i < 256 * tableSize; i++)
            divide[i] = i / tableSize;

        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;
            int ta = 0, tr = 0, tg = 0, tb = 0;

            for (int i = -r; i <= r; i++) {
                int rgb = in[inIndex + clamp(i, 0, width - 1)];
                ta += (rgb >> 24) & 0xff;
                tr += (rgb >> 16) & 0xff;
                tg += (rgb >> 8) & 0xff;
                tb += rgb & 0xff;
            }

            for (int x = 0; x < width; x++) {
                out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
                        | (divide[tg] << 8) | divide[tb];

                int i1 = x + r + 1;
                if (i1 > widthMinus1)
                    i1 = widthMinus1;
                int i2 = x - r;
                if (i2 < 0)
                    i2 = 0;
                int rgb1 = in[inIndex + i1];
                int rgb2 = in[inIndex + i2];

                ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
                tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
                tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
                tb += (rgb1 & 0xff) - (rgb2 & 0xff);
                outIndex += height;
            }
            inIndex += width;
        }
    }

    public static int clamp(int x, int a, int b) {
        return (x < a) ? a : (x > b) ? b : x;
    }

    public static void blurFractional(int[] in, int[] out, int width,
                                      int height, float radius) {
        radius -= (int) radius;
        float f = 1.0f / (1 + 2 * radius);
        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;

            out[outIndex] = in[0];
            outIndex += height;
            for (int x = 1; x < width - 1; x++) {
                int i = inIndex + x;
                int rgb1 = in[i - 1];
                int rgb2 = in[i];
                int rgb3 = in[i + 1];

                int a1 = (rgb1 >> 24) & 0xff;
                int r1 = (rgb1 >> 16) & 0xff;
                int g1 = (rgb1 >> 8) & 0xff;
                int b1 = rgb1 & 0xff;
                int a2 = (rgb2 >> 24) & 0xff;
                int r2 = (rgb2 >> 16) & 0xff;
                int g2 = (rgb2 >> 8) & 0xff;
                int b2 = rgb2 & 0xff;
                int a3 = (rgb3 >> 24) & 0xff;
                int r3 = (rgb3 >> 16) & 0xff;
                int g3 = (rgb3 >> 8) & 0xff;
                int b3 = rgb3 & 0xff;
                a1 = a2 + (int) ((a1 + a3) * radius);
                r1 = r2 + (int) ((r1 + r3) * radius);
                g1 = g2 + (int) ((g1 + g3) * radius);
                b1 = b2 + (int) ((b1 + b3) * radius);
                a1 *= f;
                r1 *= f;
                g1 *= f;
                b1 *= f;
                out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                outIndex += height;
            }
            out[outIndex] = in[width - 1];
            inIndex += width;
        }
    }

    /**
     * 个人中心背景图
     *
     * @param bitmap
     * @param
     * @return
     */
    public static Bitmap compress(Bitmap bitmap) {
        Bitmap mBitmap = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] b = baos.toByteArray();
        // bitmapRecycle(bitmap);
        Options options = new Options();
        // options.inJustDecodeBounds = true;
        // BitmapFactory.decodeByteArray(b, 0, b.length, options);
        options.inSampleSize = 2;
        options.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeByteArray(b, 0, b.length, options);
        return mBitmap;
    }


    /**
     * 刷新相册
     *
     * @param filePath
     * @param context
     */
    public static void scanPhotos(String filePath, Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static String zoomPhoto2Local(String filePath) {
        if (StringUtil.isEmpty(filePath)) {
            return null;
        }
        String name = SafetyUtil.encryptStringToMd5(filePath, "32");
        File f = FileUtil.createFile(ConfigManager.IMG_PATH + name + ".jpg");
        Options options = new Options();
        options.inSampleSize = 1;
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        int baseWidth = 1000;
        if (options.outWidth > baseWidth) {
            options.inSampleSize = options.outWidth / baseWidth;
            if ((options.outWidth / options.inSampleSize) >= baseWidth * 1.5) {
                options.inSampleSize += 1;
            }
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(filePath, options);
        int degree = getImageDegree(filePath);
        Bitmap newBitmap = rotaingImageView(degree, bitmap);

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
        if (newBitmap != null) {
            newBitmap.recycle();
            newBitmap = null;
        }
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f.getAbsolutePath();
    }

    //跳转显示大图
    public static void jumbPicShowActivity(Activity activity,int mIndex){
        Intent intent=new Intent(activity, PicShowActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt("mIndex",mIndex);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    //跳转播放视频
    public static void jumbVideoPlayer(Context activity,String videoPath,String videoSnap,String describe){
        JCVideoPlayer.toJCVideoPalyActivity(activity, videoPath,videoSnap,describe);
    }
    //保存图片到相册
    public static void saveImageToGallery(Context context, Bitmap bmp) {
        if (bmp == null){
            Toast.makeText(context, "保存出错了...",Toast.LENGTH_SHORT).show();
            return;
        }
        // 首先保存图片
        File appDir = new File(ConfigManager.ROOT, "saveImaeg");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "保存出错了...",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(context, "保存出错了...",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch (Exception e){
            Toast.makeText(context, "保存出错了...",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // 最后通知图库更新
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        Toast.makeText(context, "保存成功了...",Toast.LENGTH_SHORT).show();
    }
    //保存图片到相册
    public static boolean saveImageToGallery2(Context context, Bitmap bmp) {
        if (bmp == null){
            return false;
        }
        // 首先保存图片
        File appDir = new File(ConfigManager.ROOT, "saveImaeg");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        // 最后通知图库更新
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        return true;
    }
}
