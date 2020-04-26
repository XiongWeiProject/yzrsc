/*
 * Created by fanchao
 * 
 * Date:2014年9月25日下午4:44:01 
 * 
 * Copyright (c) 2014, Show(R). All rights reserved.
 * 
 */  
package com.commodity.scw.http;


/** 
 * Function: HttpClient工厂选择  
 *
 * Date: 2014年9月25日  下午4:44:01  
 * 
 * @author fanchao 
 */
public class HttpClientFactory {

	private HttpClientFactory() {
    }

	/**
	 * 选择了ApacheClient这套框架
	 * @return
	 */
    public static HttpClient instance() {
        return new HttpApacheClientExecutor();
    }
	
	
}
