/*
 * Created by fanchao
 * 
 * Date:2014年9月25日下午3:30:08 
 * 
 * Copyright (c) 2014, Show(R). All rights reserved.
 * 
 */
package com.commodity.scw.http;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Function: Http客户端的基类 下载可以使用如下的代码
 * 
 * <pre>
 * try {
 * 	HttpClient client = HttpClientFactory.instance();
 * 	client.setListener(new HttpClientListener() {
 * 		&#064;Override
 * 		public void onHeaders(HttpClient httpClient, Map&lt;String, List&lt;String&gt;&gt; headerFields) {
 * 		}
 * 
 * 		&#064;Override
 * 		public void onError(HttpClient client, Throwable e) {
 * 		}
 * 
 * 		&#064;Override
 * 		public void onData(HttpClient client, byte[] buffer, int offset, int length) {
 * 		}
 * 
 * 		&#064;Override
 * 		public void onComplete(HttpClient client) {
 * 		}
 * 	});
 * 	client.save(&quot;http://xxxx/1.png&quot;, new File(SystemUtil.getMediaDirectory(), &quot;1.png&quot;));
 * } catch (Exception e) {
 * 	// 处理一些无法处理的异常
 * }
 * </pre>
 * 
 * Date: 2014年9月25日 下午3:30:08
 * 
 * @author fanchao
 */
public interface HttpClient {

	/** 与网络请求的动作 */
	public void get(HttpParam param, HttpResponse httpResponse);

	public void post(HttpParam param, HttpResponse response);

	public void post(HttpParam param, HttpResponse httpResponse, boolean isGzip);
	
	public void update(HttpParam param, HttpResponse httpResponse, boolean isGzip);
	
	public void delete(HttpParam param, HttpResponse httpResponse);
	
	//支持jsonarray的重写方法
	public void post(HttpParamNew param, HttpResponse response);
	public void update(HttpParamNew param, HttpResponse httpResponse, boolean isGzip);

	/**
	 * 上传文件
	 * @param param
	 * @param httpResponse
	 */
	public void uploadFile(HttpParam param, final HttpResponse httpResponse);

	/**
	 * 保存文件
	 * @param url
	 * @param file
	 * @throws IOException
	 */
	public void save(String url, File file) throws IOException;

	/**
	 * 设置监听
	 * @param listener
	 */
	public void setListener(HttpClientListener listener);

	public HttpClientListener getListener();

	/**
	 * 获取数据的监听接口
	 */
	public interface HttpClientListener {
		/**
		 * 头部回调
		 * @param httpClient
		 * @param headerFields
		 */
		public void onHeaders(HttpClient httpClient, Map<String, List<String>> headerFields);

		/**
		 * 错误回调
		 * @param client
		 * @param e
		 */
		public void onError(HttpClient client, Throwable e);

		/**
		 * 数据回调
		 * @param client
		 * @param buffer
		 * @param offset
		 * @param length
		 */
		public void onData(HttpClient client, byte[] buffer, int offset, int length);

		/**
		 * 完成回调
		 * @param client
		 */
		public void onComplete(HttpClient client);
	}
}
