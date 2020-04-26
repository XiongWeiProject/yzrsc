package com.commodity.scw.manager;

import android.app.Application;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.commodity.scw.R;
import com.commodity.scw.http.excutor.ImagePoolExecutor;
import com.commodity.scw.utils.BlurBitmapDisplayer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 
 */
public final class ImageLoaderManager {

	private final Application context;
	private static ImageLoaderManager instance;

	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	public static ImageLoaderManager getInstance() {
		return instance;
	}

	public static void create(Application context) {
		if (instance == null) {
			instance = new ImageLoaderManager(context);
		}
	}

	private ImageLoaderManager(Application context) {
		this.context = context;
		initUILImageLoader();
	}

	private void initUILImageLoader() {
		File cacheDir = StorageUtils.getCacheDirectory(context);
		options = new DisplayImageOptions.Builder().showImageOnLoading(0)
				.showImageOnFail(0).cacheInMemory(true).cacheOnDisk(true)
				.displayer(new SimpleBitmapDisplayer())
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		com.nostra13.universalimageloader.core.ImageLoaderConfiguration config = new com.nostra13.universalimageloader.core.ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				// 以MD5的方式命名缓�?
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.threadPoolSize(5).defaultDisplayImageOptions(options)
				// 图片显示参数设置
				.diskCache(new UnlimitedDiskCache(cacheDir))
				// 不限制的缓存，速度最快
				.imageDownloader(new BaseImageDownloader(context))
				// 缓存图片的方式
				.taskExecutor(ImagePoolExecutor.getInstance().getExecutor())
				.build();

		mImageLoader = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();

		mImageLoader.init(config);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	/**
	 * 获取ImageLoad�?��的options
	 * 
	 * @param loadingResID
	 * @param failResID
	 * @return
	 */
	public DisplayImageOptions getImageOptions(int loadingResID, int failResID) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(loadingResID).showImageOnFail(failResID)
				.cacheInMemory(true).cacheOnDisk(true)
				.displayer(new SimpleBitmapDisplayer())
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		return options;
	}

	/**
	 * String imageUri = "http://site.com/image.png"; // from Web String
	 * imageUri = "file:///mnt/sdcard/image.png"; // from SD card String
	 * imageUri = "content://media/external/audio/albumart/13"; // from content
	 * provider String imageUri = "assets://image.png"; // from assets String
	 * imageUri = "drawable://" + R.drawable.image; // from drawables (only
	 * images, non-9patch)
	 * 
	 * @param uri
	 * @param imageView
	 */
	public void displayImage(final String uri, final ImageView imageView) {
		displayImage(uri, imageView, options);
	}

	public void displayImage(final String uri, final ImageView imageView,
							 int defaultResId) {
		displayImage(uri, imageView, defaultResId, false);
	}

	// public void displayImage(final String uri, final ImageView imageView,
	// boolean rounded) {
	// displayImage(uri, imageView, 0, rounded);
	// }

	/**
	 * 显示模糊图片
	 * 
	 * @param uri
	 *            网络路径
	 * @param imageView
	 *            显示控件
	 * @param defaultId
	 *            默认图片
	 */
	public void displayBlurImage(String uri, ImageView imageView, int defaultId) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(defaultId).showImageOnFail(defaultId)
				.showImageForEmptyUri(defaultId).cacheInMemory(false)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new BlurBitmapDisplayer()).build();
		com.nostra13.universalimageloader.core.ImageLoader.getInstance()
				.displayImage(uri, imageView, options, null);
	}

	public void displayImage(final String uri, final ImageView imageView,
							 boolean rounded, int width) {
		displayImage(uri, imageView, rounded, width, 0);
	}

	public void displayImage(final String uri, final ImageView imageView,
							 boolean rounded, int width, int defaultId) {
		DisplayImageOptions options;
		if (rounded) {
			BitmapDisplayer diaplayer = new RoundedBitmapDisplayer(width / 2);
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(defaultId).showImageOnFail(defaultId)
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565).displayer(diaplayer)
					.build();
		} else {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(defaultId).showImageOnFail(defaultId)
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(6)).build();
		}
		displayImage(uri, imageView, options);
	}

	public void displayImage(final String uri, final ImageView imageView,
							 int defaultResId, boolean rounded) {
		DisplayImageOptions options;
		if (rounded) {
			int width = imageView.getWidth();
			if (width <= 0) {
				width = 60;
			}
			BitmapDisplayer diaplayer = new RoundedBitmapDisplayer(width);
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(defaultResId)
					.showImageForEmptyUri(defaultResId)
					.showImageOnFail(defaultResId).cacheInMemory(true)
					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(diaplayer).build();
		} else {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(defaultResId)
					.showImageForEmptyUri(defaultResId)
					.showImageOnFail(defaultResId).cacheInMemory(true)
					.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
					.build();
		}
		displayImage(uri, imageView,defaultResId, options);//111
	}
	public void displayImage(String uri, ImageView imageView,
							 DisplayImageOptions options) {
		if (TextUtils.isEmpty(uri)) {
			imageView.setImageResource(R.drawable.ic_launcher);
		} else {
			imageView.setImageDrawable(null);
			mImageLoader.displayImage(uri, imageView, options);
		}
	}
	public void displayImage(String uri, ImageView imageView,
							 int defaultResId, DisplayImageOptions options) {
		if (TextUtils.isEmpty(uri)) {
			imageView.setImageResource(defaultResId);
		} else {
			imageView.setImageDrawable(null);
			mImageLoader.displayImage(uri, imageView, options);
		}
	}

	public void displayImage(final String uri, final ImageView imageView,
							 ImageLoadingListener listener) {
		displayImage(uri, imageView, options, listener);
	}

	/**
	 * 异步显示�?��图片
	 * 
	 * @param uri
	 * @param imageView
	 * @param options
	 */
	private void displayImage(String uri, ImageView imageView,
							  DisplayImageOptions options, ImageLoadingListener listener) {
		if (TextUtils.isEmpty(uri)) {
			imageView.setImageResource(0);
		} else {
			com.nostra13.universalimageloader.core.ImageLoader.getInstance()
					.displayImage(uri, imageView, options, listener, listener);
		}
	}

	/**
	 * 异步显示图片，图片可以带圆角与默认图片
	 * 
	 * @param uri
	 *            图片uri
	 * @param imageView
	 *            显示控件
	 * @param roundWidth
	 *            控件宽度，必须为px
	 * @param defaultId
	 *            默认图片
	 */
	public void displayImage(final String uri, final ImageView imageView,
							 int roundWidth, int defaultId) {
		displayImage(uri, imageView, roundWidth, defaultId, null);
	}

	/**
	 * 异步显示图片，监听回调方法 通过imageLoadinglistener可以监听当前的进度
	 * 
	 * @param uri
	 *            图片uri
	 * @param imageView
	 *            显示控件
	 * @param roundWidth
	 *            控件宽度，必须为px
	 * @param defaultId
	 *            默认图片
	 * @param listener
	 *            通过imageLoadinglistener可以监听当前的进度
	 */
	public void displayImage(final String uri, final ImageView imageView,
							 int roundWidth, int defaultId, ImageLoadingListener listener) {
		DisplayImageOptions options;
		if (roundWidth > 0) {
			BitmapDisplayer diaplayer = new RoundedBitmapDisplayer(
					roundWidth / 2);
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(defaultId).showImageOnFail(defaultId)
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565).displayer(diaplayer)
					.build();
		} else {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(defaultId).showImageOnFail(defaultId)
					.cacheInMemory(true).cacheOnDisk(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new SimpleBitmapDisplayer()).build();
		}
		displayImage(uri, imageView, options, listener);
	}

	/**
	 * 通过URI同步获取图片的bitmap
	 * 
	 * @param uri
	 * @return
	 */
	public Bitmap loadImageSync(String uri) {
		return com.nostra13.universalimageloader.core.ImageLoader.getInstance()
				.loadImageSync(uri);
	}
	public void clearMemoryCache() {
		mImageLoader.clearMemoryCache();
	}

	public void clearDiskCache() {
		mImageLoader.clearDiskCache();

	}
}
