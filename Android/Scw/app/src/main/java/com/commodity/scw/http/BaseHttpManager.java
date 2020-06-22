package com.commodity.scw.http;


/**
 * Function: 通信基类
 * 
 * Date: 2014年9月19日 下午3:28:57
 * 
 * @author DaiHui
 */
public abstract class BaseHttpManager {

	/**
	 * get请求
	 */
	protected abstract void get();

	/**
	 * post请求
	 */
	protected abstract void post();

	protected abstract void update();

	protected abstract void delete();

	/**
	 * 上传请求
	 */
	protected abstract void upload();

	/**
	 * 通信请求回调接口
	 */
	public interface IRequestListener {
		/**
		 * 预置操作
		 */
		public void onPreExecute(int Tag);

		/**
		 * 成功
		 */
		public void onSuccess(int Tag, ServiceInfo result);

		/**
		 * 失败
		 * 
		 * @param code
		 *            错误码
		 * @param msg
		 *            错误信息
		 */
		public void onError(int Tag, String code, String msg);

		/**
		 * 超时
		 */
		public void OnTimeOut(int Tag, boolean isShowTip);

		/**
		 * 无网络
		 */
		public void OnNetError(int Tag, boolean isShowTip);

	}

	/**
	 * 上传文件回调接口
	 */
	public interface IUploadFileListener extends IRequestListener {
		/**
		 * 上传进度
		 */
		public void OnUploadProgress(int progress);
	}

}
