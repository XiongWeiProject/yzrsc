package com.commodity.yzrsc;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.manager.NetworkManager;
import com.commodity.yzrsc.rongyun.SealAppContext;
import com.commodity.yzrsc.rongyun.SealUserInfoManager;
import com.commodity.yzrsc.rongyun.message.TestMessage;
import com.commodity.yzrsc.rongyun.message.provider.ContactNotificationMessageProvider;
import com.commodity.yzrsc.rongyun.message.provider.TestMessageProvider;
import com.commodity.yzrsc.rongyun.server.utils.NLog;
import com.commodity.yzrsc.rongyun.stetho.RongDatabaseDriver;
import com.commodity.yzrsc.rongyun.stetho.RongDatabaseFilesProvider;
import com.commodity.yzrsc.rongyun.stetho.RongDbFilesDumperPlugin;
import com.commodity.yzrsc.rongyun.utils.SharedPreferencesContext;
import com.commodity.yzrsc.wxapi.WXEntryActivity;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yixia.camera.VCamera;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.push.RongPushClient;
import io.rong.push.common.RongException;
import okhttp3.OkHttpClient;

/**
 * 作者：liyushen on 2016/4/1 15:29 功能：Application基类
 */
public class MainApplication extends Application {
	private static DisplayImageOptions options;
	/**
	 * tag日志
	 */
	public static final String tag = MainApplication.class.getSimpleName();
	private static PackageInfo packInfo;
	public static MainApplication mContext;
	/**
	 * USER_AGENT
	 */
	public static String USER_AGENT;
	/**
	 * 屏幕高
	 */
	public static int SCREEN_H = 0;
	/**
	 * 屏幕宽
	 */
	public static int SCREEN_W = 0;
	/**
	 * 屏幕密度
	 */
	public static float DENSITY = 0.0f;
	/**
	 * 屏幕密度
	 */
	public static String deviceId = "";

	public static Map<String,Object> mHashMap = new HashMap<String,Object>() ;
	{
		PlatformConfig.setWeixin(AppConst.WEIXIN_APP_ID, AppConst.WEIXIN_APP_SECRET);
		PlatformConfig.setQQZone(AppConst.QQ_APP_ID, AppConst.QQ_APP_KEY);
		PlatformConfig.setSinaWeibo(AppConst.SINA_APP_ID,AppConst.SINA_APP_SECRET, "http://sns.whalecloud.com");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MultiDex.install(this);
		mContext = this;
		//分享
		Config.DEBUG = false;
		UMShareAPI.get(this);
//		VideoUtils.initSmallVideo(this);
		// 初始化网络
		NetworkManager.create(this);
		// ConfigManager内存管理类
		ConfigManager.create(this);
		Config.isJumptoAppStore = true;
		ImageLoaderManager.create(this);
		// 注释掉IDE显示logcat日志，不注释掉异常存储在存储卡中
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				// .addInterceptor(new LoggerInterceptor("TAG"))
				.connectTimeout(100000L, TimeUnit.MILLISECONDS)
				.readTimeout(100000L, TimeUnit.MILLISECONDS)
				// 其他配置
				.build();

		OkHttpUtils.initClient(okHttpClient);
		getAppInfo();

		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		SCREEN_W = dm.widthPixels;
		SCREEN_H = dm.heightPixels;
		DENSITY = dm.density;
		if (SCREEN_W > SCREEN_H) {
			int i = SCREEN_H;
			SCREEN_H = SCREEN_W;
			SCREEN_W = i;
		}
		// 初始化融云组件
		initRongyun();
		// 初始化微信组件
		initWeiXin();
		// 初始化视频录制信息
		 initRecorder();
	}

	public boolean isLogin() {
		return ConfigManager.instance().isLogin();
	}

	public void clearUser() {
		ConfigManager.instance().clearUser();
	}

	private void getAppInfo() {
		// 获取packageManager的实例
		PackageManager packageManager = getPackageManager();
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 方法名称：getVersionName
	 * <p>
	 * 方法描述：获取系统的版本名称 versionName
	 *
	 * @author liyushen
	 * @return versionName 4.2
	 *         <p>
	 *         备注：
	 */
	public String getVersionName() {
		if (packInfo == null) {
			getAppInfo();
		}
		return packInfo != null ? packInfo.versionName : "";
	}

	/**
	 * 方法名称：getDataDir
	 * <p>
	 * 方法描述：获取apk包名路径
	 *
	 * @author liyushen
	 * @return versionName 4.2
	 *         <p>
	 *         备注：
	 */

	public int getVersionCode() {
		if (packInfo == null)
			getAppInfo();
		return packInfo != null ? packInfo.versionCode : 0;
	}

	public static Context getContext() {
		return mContext;
	}
	public static MainApplication instance() {
		return mContext;
	}


	@Override
	public void onTerminate() {// 程序终止的时候执行
		super.onTerminate();
	}

	//集成融云  initRongyun  getOptions  openSealDBIfHasCachedToken  getCurProcessName
	private void initRongyun(){
		Stetho.initialize(new Stetho.Initializer(this) {
			@Override
			protected Iterable<DumperPlugin> getDumperPlugins() {
				return new Stetho.DefaultDumperPluginsBuilder(MainApplication.this)
						.provide(new RongDbFilesDumperPlugin(MainApplication.this, new RongDatabaseFilesProvider(MainApplication.this)))
						.finish();
			}

			@Override
			protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
				Stetho.DefaultInspectorModulesBuilder defaultInspectorModulesBuilder = new Stetho.DefaultInspectorModulesBuilder(MainApplication.this);
				defaultInspectorModulesBuilder.provideDatabaseDriver(new RongDatabaseDriver(MainApplication.this, new RongDatabaseFilesProvider(MainApplication.this), new DefaultDatabaseConnectionProvider()));
				return defaultInspectorModulesBuilder.finish();
			}
		});

		if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

//            LeakCanary.install(this);//内存泄露检测
			RongPushClient.registerHWPush(this);
			RongPushClient.registerMiPush(this, "2882303761517473625", "5451747338625");//PHMgkQXQgSiBIa
			try {
				RongPushClient.registerGCM(this);
			} catch (RongException e) {
				e.printStackTrace();
			}

			/**
			 * 注意：
			 *
			 * IMKit SDK调用第一步 初始化
			 *
			 * context上下文
			 *
			 * 只有两个进程需要初始化，主进程和 push 进程
			 */
			//RongIM.setServerInfo("nav.cn.ronghub.com", "img.cn.ronghub.com");
			RongIM.init(this);
			NLog.setDebug(true);//Seal Module Log 开关
			SealAppContext.init(this);
			SharedPreferencesContext.init(this);
			Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

			try {
				RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
				RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
				RongIM.registerMessageType(TestMessage.class);
				RongIM.registerMessageTemplate(new TestMessageProvider());

			} catch (Exception e) {
				e.printStackTrace();
			}



			openSealDBIfHasCachedToken();

			options = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.ico_defalut_header)
					.showImageOnFail(R.drawable.ico_defalut_header)
					.showImageOnLoading(R.drawable.ico_defalut_header)
					.displayer(new FadeInBitmapDisplayer(300))
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.build();

			//RongExtensionManager.getInstance().registerExtensionModule(new PTTExtensionModule(this, true, 1000 * 60));

//			RongIM.connect("4yVNxreUw0x5CzOcVNyOXa+YsUIoF3ojin3K277sfOkWnrAzSqHtxTlwdOPZGuBkqNP0jLAlqcJaDMVsghR0EqtdpZUyLdaH", new RongIMClient.ConnectCallback() {
//				@Override
//				public void onTokenIncorrect() {
//					NLog.e("融云connect", "onTokenIncorrect");
//				}
//
//				@Override
//				public void onSuccess(String s) {
//					NLog.e("融云connect", "onSuccess userid:" + s);
//					SPManager.instance().setString(SealConst.SEALTALK_LOGIN_ID, s);
//					SealUserInfoManager.getInstance().openDB();
//				}
//
//				@Override
//				public void onError(RongIMClient.ErrorCode errorCode) {
//					NLog.e("connect", "onError errorcode:" + errorCode.getValue());
//				}
//			});
		}
	}

	public static DisplayImageOptions getOptions() {
		return options;
	}

	private void openSealDBIfHasCachedToken() {
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		String cachedToken = sp.getString("loginToken", "");
		if (!TextUtils.isEmpty(cachedToken)) {
			String current = getCurProcessName(this);
			String mainProcessName = getPackageName();
			if (mainProcessName.equals(current)) {
				SealUserInfoManager.getInstance().openDB();
			}
		}
	}

	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

	//微信登录
	public static IWXAPI sApi;
	private void initWeiXin() {
		sApi = WXEntryActivity.initWeiXin(this, AppConst.WEIXIN_APP_ID);//AppConst.WEIXIN_APP_ID
	}

	//初始化视频录制配置
	private void initRecorder(){
		//ConfigManager.VIDEO_PATH_SD += String.valueOf(System.currentTimeMillis());
		File file = new File(ConfigManager.VIDEO_PATH_SD);
		if(!file.exists()) file.mkdirs();

		//设置视频缓存路径
		VCamera.setVideoCachePath(ConfigManager.VIDEO_PATH_SD);

		// 开启log输出,ffmpeg输出到logcat
		VCamera.setDebugMode(true);

		// 初始化拍摄SDK，必须
		VCamera.initialize(this);
	}

}
