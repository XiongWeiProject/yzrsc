
package com.commodity.yzrsc.http;

import com.commodity.yzrsc.model.UploadFile;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.yohoutils.Logger;


/**
 * Function: HTTP参数
 * 
 * Date: 2014年9月18日 下午5:59:43
 * 
 * @author DaiHui
 */
public class HttpParam {
	private static final String TAG = "HttpParam";
	/**
	 * 超时时间
	 */
	public static final int TIMEOUT_TIME = 15000;
	/**
	 * Gson实例
	 */
	// private static Gson mGson;
	/**
	 * 方法
	 */
	private HttpMothed mMothed;
	/**
	 * base url
	 */
	private String mUrl;
	/**
	 * 传入的请求参数
	 */
	private Map<String, String> mRequestParams;

	/**
	 * 传入的请求参数
	 */
	private String jsons;
	/**
	 * 参数键值对
	 */
	private JSONObject mRequest;
	/**
	 * 参数列表
	 */
	private List<NameValuePair> mParamsList;
	/**
	 * 是否需要使用Https通信渠道
	 */
	@SuppressWarnings("unused")
	private boolean mHasSecurityCertificate;
	/**
	 * 文件列表
	 */
	private List<UploadFile> mFileList;
	/**
	 * 是否自动开启线程
	 */
	private boolean mIsAutoExecuteThread = true;
	/**
	 * 非访问外部链接
	 */
	private boolean mIsExternalLink = false;
	/**
	 * 是否需要显示网络错误信息
	 */
	private boolean mIsShowNetError = true;

	public HttpParam(HttpMothed mothed, String url,
					 Map<String, String> requestParams, boolean hasSecurityCertificate,
					 List<UploadFile> fileList, boolean isExternalLink) throws Exception {
		mMothed = mothed;
		mRequestParams = requestParams;
		mHasSecurityCertificate = hasSecurityCertificate;
		mFileList = fileList;
		mIsExternalLink = isExternalLink;
		
		if (!isExternalLink) {
			mUrl = url;
			constructParameters();
		} else {
			mUrl = url;
		}
	}
	public HttpParam(HttpMothed mothed, String url,
					 String json, boolean hasSecurityCertificate,
					 List<UploadFile> fileList, boolean isExternalLink,String ss) throws Exception {
		mMothed = mothed;
		jsons = json;
		mHasSecurityCertificate = hasSecurityCertificate;
		mFileList = fileList;
		mIsExternalLink = isExternalLink;

		if (!isExternalLink) {
			mUrl = url;
			constructParameters();
		} else {
			mUrl = url;
		}
	}
	/**
	 * 构造参数列表
	 * 
	 * @throws JSONException
	 */
	private void constructParameters() throws JSONException, Exception {
		mParamsList = new ArrayList<NameValuePair>();
		List<NameValuePair> baseParams = getAuthInfo();
		if (baseParams != null && baseParams.size() > 0) {
			mParamsList.addAll(baseParams);
		}

		if (mRequestParams != null && mRequestParams.size() > 0) {
//			StringBuffer strBuffer = new StringBuffer();
			String key = null;
			String value = null;
			for (Map.Entry<String, String> entry : mRequestParams.entrySet()) {
				key = entry.getKey();
				value = entry.getValue();
				mParamsList.add(new BasicNameValuePair(key, value));
//				strBuffer.append(",\"" + key + "\":\"" + value+"\"");
			}
//			String parameters = strBuffer.toString().substring(1);
//			parameters ="{"+parameters+"}";
//			mParamsList.add(new BasicNameValuePair("parameters", parameters));
		}
	}

	/**
	 * 获取认证信息 包含所有需要的公共信息
	 * 
	 * @return
	 * @throws JSONException
	 */
	private List<NameValuePair> getAuthInfo() throws JSONException {
		List<NameValuePair> baseParams = new ArrayList<NameValuePair>();

		
		//baseParams.add(new BasicNameValuePair("platform", "Android"));
		
		return baseParams;
	}

	/**
	 * 获取请求方式
	 * 
	 * @return
	 */
	public HttpMothed getmMothed() {
		return mMothed;
	}

	/**
	 * 设置请求方式
	 * 
	 * @param mMothed
	 */
	public void setmMothed(HttpMothed mMothed) {
		this.mMothed = mMothed;
	}

	/**
	 * 获取请求URL
	 * 
	 * @return
	 */
	public String getmUrl() {
		return mUrl;
	}

	/**
	 * 设置请求URL
	 * 
	 * @param mUrl
	 */
	public void setmUrl(String mUrl) {
		this.mUrl = mUrl;
	}

	/**
	 * 获取参数列表
	 * 
	 * @return
	 */
	public List<NameValuePair> getmParamsList() {
		return mParamsList;
	}

	/**
	 * 设置参数列表
	 * 
	 * @param mParamsList
	 */
	public void setmParamsList(List<NameValuePair> mParamsList) {
		this.mParamsList = mParamsList;
	}

	/**
	 * 获取请求参数键值对象
	 * 
	 * @return
	 */
	public JSONObject getmRequest() {
		return mRequest;
	}

	/**
	 * 设置请求参数键值对象
	 * 
	 * @param mRequest
	 */
	public void setmRequest(JSONObject mRequest) {
		this.mRequest = mRequest;
	}

	/**
	 * 获取上传文件列表
	 * 
	 * @return the mFileList
	 */
	public List<UploadFile> getmFileList() {
		return mFileList;
	}

	/**
	 * 设置上传文件列表
	 * 
	 * @param mFileList
	 *            the mFileList to set
	 */
	public void setmFileList(List<UploadFile> mFileList) {
		this.mFileList = mFileList;
	}

	/**
	 * 获取是否自动开启线程
	 * 
	 * @return the mIsAutoExecuteThread
	 */

	public boolean ismIsAutoExecuteThread() {
		return mIsAutoExecuteThread;
	}

	/**
	 * 设置是否自动开启线程
	 * 
	 * @param mIsAutoExecuteThread
	 *            the mIsAutoExecuteThread to set
	 */
	public void setmIsAutoExecuteThread(boolean mIsAutoExecuteThread) {
		this.mIsAutoExecuteThread = mIsAutoExecuteThread;
	}

	/**
	 * 设置是否显示网络错误信息
	 * 
	 * @param isShowNetError
	 */
	public void setIsShowNetError(boolean isShowNetError) {
		mIsShowNetError = isShowNetError;
	}

	/**
	 * 获取是否显示网络错误信息
	 * 
	 * @return the mIsShowNetError
	 */
	public boolean isShowNetError() {
		return mIsShowNetError;
	}

	/**
	 * 获取是否访问外部链接
	 * 
	 * @return the mIsExternalLink
	 */
	public boolean ismIsExternalLink() {
		return mIsExternalLink;
	}

	/**
	 * 设置是否访问外部链接
	 * 
	 * @param mIsExternalLink
	 *            the mIsExternalLink to set
	 */
	public void setmIsExternalLink(boolean mIsExternalLink) {
		this.mIsExternalLink = mIsExternalLink;
	}

	/**
	 * 清除"parameters"键（上传文件时内部已带key）
	 */
	public String clearParamKey() {
		String params = null;
		try {
			params = mParamsList.toString();
			String temp = "[parameters=";
			params = params.substring(temp.length(), params.length() - 1);
		} catch (Throwable e) {
			Logger.d(TAG, "clear param key error");
		}
		return params;
	}
}

