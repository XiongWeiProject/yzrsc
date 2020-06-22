package com.commodity.scw.utils;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.yohoutils.StringUtil;

public class DownloadUtils {
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
		return donwLoadFile(urlString, path,null);
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
		if(filePath.exists()){
			if(listener != null){
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
			int totalSize  = 1;
			if(urlConnection != null){
				totalSize = urlConnection.getContentLength();
				Log.e( "totalSize: ", totalSize+"===");
			}

			FileOutputStream fio = new FileOutputStream(file);
			out = new BufferedOutputStream(fio);
			int b;
			byte[] arry = new byte[1024];
			int downLoadSize = 0;
			while (in != null && (b = in.read(arry)) != -1) {
				out.write(arry, 0, b);
				downLoadSize += b;
				if(listener != null){
					listener.loading(downLoadSize, totalSize);
				}
			}
			out.flush();
			if(listener != null){
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
}
