package com.commodity.yzrsc.http;

import android.os.Build;

import com.commodity.yzrsc.http.excutor.PoolExecutor;
import com.commodity.yzrsc.manager.NetworkManager;
import com.commodity.yzrsc.model.UploadFile;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

import cn.yohoutils.Logger;

/**
 * Function: 通信管理类（支持Apache Client、HttpUrlConnection和Volley（包含前两种方式）框架）</br> 示例：
 * 
 * <pre>
 * {
 * 	&#064;code
 * 	HttpManager http = new HttpManager(&quot;http://www.xxxx.com&quot;);
 * 	http.setRequestListener(new IPreRequestListener() {
 * 
 * 		&#064;Override
 * 		public void onSuccess(Object httpResponse) {
 * 			// 成功
 * 
 * 		}
 * 
 * 		&#064;Override
 * 		public void onError(String code, String msg) {
 * 			// 错误
 * 
 * 		}
 * 
 * 		&#064;Override
 * 		public void onPreExecute() {
 * 			// 预处理
 * 
 * 		}
 * 	});
 * 	http.request();
 * }
 * </pre>
 * 
 * <b>注意：</b><br>
 * 通常应继承该类，处理各自业务<br>
 * Date: 2014年9月18日 上午11:25:29</br> 此处方法支持传递jsonArray对象
 * 
 * @author DaiHui
 */
public class HttpManageNew extends BaseHttpManager {
	private static final String TAG = "HttpManager";
	// private HttpApacheClientExecutor mHttpApacheClientExecutor;// Apache
	// Client通信框架触发器
	// private HttpUrlConnectionExecutor mHttpUrlConnectionExecutor;//
	// HttpUrlConnnection通信框架触发器
	private HttpChannel mDefHttpFramework = HttpChannel.APACHE_CLIENT;// 默认通信框架
	private HttpParamNew mHttpParam;// 通信参数
	private HttpResponse mHttpResponse;// 通信响应
	private HttpClient mClient;// 通信渠道
	/**
	 * 初始URL（默认用GET方式请求）
	 * 
	 * @param url
	 *            URL
	 */
	public HttpManageNew(int tag,String url) {
		this(tag,HttpMothed.GET, url);
	}

	/**
	 * 初始请求方式及URL
	 * 
	 * @param mothed
	 *            请求方式
	 * @param url
	 *            URL
	 */
	public HttpManageNew(int tag,HttpMothed mothed, String url) {
		this(tag,mothed, url, null);
	}

	/**
	 * 初始请求方式、URL及传入对象
	 * 
	 * @param mothed
	 *            请求方式
	 * @param url
	 *            URL
	 * @param obj
	 *            传入对象
	 */
	public HttpManageNew(int tag, HttpMothed mothed, String url, Map<String, Object> obj) {
		this(tag,mothed, url, obj, null);
	}

	/**
	 * 初始请求方式、URL、传入对象及监听实例
	 * 
	 * @param mothed
	 *            请求方式
	 * @param url
	 *            URL
	 * @param obj
	 *            传入对象
	 * @param listener
	 *            监听实例
	 */
	public HttpManageNew(int tag, HttpMothed mothed, String url,
						 Map<String, Object> obj, IRequestListener listener) {
		this(tag,mothed, url, obj, listener, false);
	}

	/*
	 * public HttpManager(HttpMothed mothed, String url, JSONArray jsonArray,
	 * IRequestListener listener){
	 * 
	 * }
	 */
	/**
	 * 初始请求方式、URL、传入对象、监听实例以及是否使用Https
	 * 
	 * @param mothed
	 *            请求方式
	 * @param url
	 *            URL
	 * @param obj
	 *            传入对象
	 * @param listener
	 *            监听实例
	 * @param hasSecurityCertificate
	 *            使用Https
	 */
	public HttpManageNew(int tag, HttpMothed mothed, String url,
						 Map<String, Object> obj, IRequestListener listener,
						 boolean hasSecurityCertificate) {
		this(tag,mothed, url, obj, listener, hasSecurityCertificate, null);
	}

	/**
	 * 上传文件
	 * 
	 * @param mothed
	 *            请求方式
	 * @param url
	 *            URL
	 * @param obj
	 *            传入对象
	 * @param listener
	 *            监听实例
	 * @param fileList
	 *            文件列表
	 */
	public HttpManageNew(int tag, HttpMothed mothed, String url,
						 Map<String, Object> obj, IRequestListener listener,
						 List<UploadFile> fileList) {
		this(tag,mothed, url, obj, listener, false, fileList);
	}

	/**
	 * 初始请求方式、URL、传入对象、监听实例、是否使用Https以及文件列表
	 * 
	 * @param mothed
	 *            请求方式
	 * @param url
	 *            URL
	 * @param obj
	 *            传入对象
	 * @param listener
	 *            监听实例
	 * @param hasSecurityCertificate
	 *            使用Https
	 * @param fileList
	 *            文件列表
	 */
	public HttpManageNew(int tag, HttpMothed mothed, String url,
						 Map<String, Object> obj, IRequestListener listener,
						 boolean hasSecurityCertificate, List<UploadFile> fileList) {
		try {
			autoChangeOriFramework();
			mClient = HttpClientFactory.instance();
			mHttpParam = new HttpParamNew(mothed, url, obj,
					hasSecurityCertificate, fileList, false);
			initHttpResponse(tag,listener);
		} catch (JSONException e) {
			Logger.d(TAG, "construct http param json error");
		} catch (Exception e) {
			Logger.d(TAG, "construct http param error");
		}
	}

	/**
	 * 该API仅针对外部链接访问（如使用Google Api根据经纬度查询地址）
	 * 
	 * @param url
	 *            URL
	 * @param listener
	 *            监听实例
	 */
	public HttpManageNew(int tag, String url, IRequestListener listener) {
		try {
			mClient = HttpClientFactory.instance();
			mHttpParam = new HttpParamNew(HttpMothed.POST, url, null, false,
					null, true);
			initHttpResponse(tag,listener);
		} catch (JSONException e) {
			Logger.d(TAG, "construct http param json error");
		} catch (Exception e) {
			Logger.d(TAG, "construct http param error");
		}
	}

	/**
	 * 设置是否自动开启线程
	 * 
	 * @param isAuto
	 */
	public void setAutoExecuteThread(boolean isAuto) {
		mHttpParam.setmIsAutoExecuteThread(isAuto);
	}

	/**
	 * 动态设置监听（构造函数内若已设置监听，此处不需再设）
	 * 
	 * @param listener
	 */
	public void setRequestListener(int tag,IRequestListener listener) {
		initHttpResponse(tag,listener);
	}

	public void isShowNetError(boolean isShowNetError) {
		mHttpParam.setIsShowNetError(isShowNetError);
	}

	/**
	 * 初始Http Response对象与回调接口
	 * 
	 * @param listener
	 *            回调接口
	 */
	private void initHttpResponse(int tag,IRequestListener listener) {
		mHttpResponse = new HttpResponse(tag,listener);
	}

	/**
	 * 修改通信框架
	 * 
	 * @param httpFramework
	 *            参考{@link HttpChannel}
	 */
	@SuppressWarnings("unused")
	private void setmDefHttpFramework(HttpChannel httpFramework) {
		this.mDefHttpFramework = httpFramework;
	}

	/**
	 * 根据当前手机OS版本来使用通信框架
	 */
	private void autoChangeOriFramework() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			// 使用Apache Client
			mDefHttpFramework = HttpChannel.APACHE_CLIENT;
		} else {
			// 使用HttpUrlConnection
			mDefHttpFramework = HttpChannel.HTTP_URL_CONNECTION;
		}
	}

	/**
	 * 统一请求
	 */
	public void request() {
		// 判断是否显示网络错误信息
		if (!NetworkManager.instance().isDataUp()) {
			mHttpResponse.NetError(mHttpParam.isShowNetError());
			return;
		}
		mHttpResponse.preExecute();
		// 判断是否自动开启线程
		if (mHttpParam.ismIsAutoExecuteThread()) {
			PoolExecutor.getInstance().getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					execute();
				}
			});
		} else {
			execute();
		}
	}

	/**
	 * 触发请求
	 */
	private void execute() {
		HttpMothed mothed = HttpMothed.POST;
		if (mHttpParam != null) {
			mothed = mHttpParam.getmMothed();
		}
		switch (mothed) {
		case GET:
			get();
			break;
		case POST:
			post();
			break;
		case UPLOAD:
			upload();
			break;
		case UPDATE:
			update();
			break;
		case DELETE:
			delete();
			break;
		default:
			break;
		}
	}

	@Override
	protected void get() {
		switchHttpChannel(mDefHttpFramework, HttpMothed.GET);
	}

	@Override
	protected void post() {
		switchHttpChannel(mDefHttpFramework, HttpMothed.POST);
	}

	@Override
	protected void upload() {
		switchHttpChannel(mDefHttpFramework, HttpMothed.UPLOAD);
	}

	@Override
	protected void update() {
		switchHttpChannel(mDefHttpFramework, HttpMothed.UPDATE);
	}

	@Override
	protected void delete() {
		switchHttpChannel(mDefHttpFramework, HttpMothed.DELETE);
	}

	/**
	 * 选择http通信方式（原来http通信框架和volley）
	 */
	private void switchHttpChannel(HttpChannel channel, HttpMothed mothed) {
		// switch (channel) {
		// case APACHE_CLIENT:
		// mHttpApacheClientExecutor = new HttpApacheClientExecutor();
		switch (mothed) {
		case GET:
			// mClient.get(mHttpParam, mHttpResponse);
			// break;
		case POST:
			mClient.post(mHttpParam, mHttpResponse);
			break;
		// case UPLOAD:
		// mClient.uploadFile(mHttpParam, mHttpResponse);
		// break;
		// case DOWNLOAD:
		// break;
		case UPDATE:
			mClient.update(mHttpParam, mHttpResponse, true);
			break;
		// case DELETE:
		// mClient.delete(mHttpParam, mHttpResponse);
		default:
			break;
		}
	}

}
