
package com.commodity.scw.http;


/** 
 * Function: 获取内容抽象类（网络数据和缓存数据）
 *
 * Date: 2016年3月30日  上午10:42:41  
 * 
 * @author liyushen
 */
public abstract class ContentListener<T> {
	/**
	 * 预处理
	 */
	public abstract void onPreExecute();

	/**
	 * 成功回调
	 * @param result 
	 */
	public abstract void onSuccess(T result);

	/**
	 * 错误回调
	 * @param code 错误码（如存在通信问题返回http code，如show业务错误返回业务错误码）
	 * @param msg 错误信息
	 */
	public abstract void onError(String code, String msg);
	/**
	 * 超时回调
	 * @param msg 错误信息
	 */
	public abstract void onTimeOut(boolean isShowTip);

	/**
	 * 上传文件进度
	 * @param progress 进度值
	 */
	public void OnUploadProgress(int progress) {};
	/**
	 * 网络异常
	 */
	public abstract void onNetWorkError(boolean isShowTip);
}
