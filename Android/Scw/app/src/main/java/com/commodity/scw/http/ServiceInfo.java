/*
 * Created by liyushen
 * 
 * Date:2016年3月30日
 * 
 * Copyright (c) 2014, Show(R). All rights reserved.
 * 
 */
package com.commodity.scw.http;

/**
 * Function: 通信返回结果类
 * 
 * Date: 2016年3月30日
 * 
 * @author liyushen
 */
public class ServiceInfo {
	
	public static final String STATUS_SUCCESS = "0"; // 成功
	public static final String STATUS_FAIL = "1";  // 失败
	
	private String mStatus;// 状态码
	private String mCode;// 错误码
	private String mMsg;// 错误信息
	private Object mResponse;// data对应返回结果（如访问外部链接则该变量保存整个通信返回json数据）
	private int totalCount;//总数
	private int totalPage;//总页数
	
	/**
	 * 获取status
	 * @return the status
	 */
	public String getStatus() {
		return mStatus;
	}

	/**
	 * 设置status
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.mStatus = status;
	}

	/**
	 * 获取code
	 * @return the code
	 */
	public String getCode() {
		return mCode;
	}

	/**
	 * 设置code
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.mCode = code;
	}

	/**
	 * 获取msg
	 * @return the msg
	 */
	public String getMsg() {
		return mMsg;
	}

	/**
	 * 设置msg
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.mMsg = msg;
	}

	/**
	 * 获取data数据（访问外部链接时返回整个json数据）
	 * @return the dataResponse
	 */
	public Object getResponse() {
		return mResponse;
	}

	/**
	 * 设置data数据
	 * @param
	 */
	public void setResponse(Object response) {
		this.mResponse = response;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}