/*
 * Created by daihui
 * 
 * Date:2014年9月21日下午8:39:30 
 * 
 * Copyright (c) 2014, Show(R). All rights reserved.
 * 
 */
package com.commodity.scw.http;

/**
 * Function: 通信框架
 * 
 * Date: 2014年9月21日 下午8:39:30
 * 
 * @author liyushen
 */
public enum HttpChannel {
	APACHE_CLIENT, // Apache Http Client（原始Show项目使用该通信方式，Android 2.2及以下版本建议使用）
	HTTP_URL_CONNECTION, // HttpUrlConnection（Android 2.3及以上版本建议使用）
	VOLLEY// 第三方通信框架（内部支持通信方式随版本更改）
}
