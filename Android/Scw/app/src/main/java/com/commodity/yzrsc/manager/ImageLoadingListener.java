package com.commodity.yzrsc.manager;

 


import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;


public class ImageLoadingListener implements com.nostra13.universalimageloader.core.listener.ImageLoadingListener, ImageLoadingProgressListener {
	/**
	 * 开始加载
	 */
	@Override
	public void onLoadingStarted(String imageUri, View view) {
	}

	/**
	 * 加载失败
	 */
	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
	}

	/**
	 * 加载完成
	 */
	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	}

	/**
	 * 加载取消
	 */
	@Override
	public void onLoadingCancelled(String imageUri, View view) {
	}

	/**
	 * 下载进行中的监听回调
	 * 
	 * @param imageUri 图片地址
	 * @param view 显示view
	 * @param current 当前的size字节大小
	 * @param total 文件总共的字节大小
	 */
	@Override
	public void onProgressUpdate(String imageUri, View view, int current, int total) {
	}
}
