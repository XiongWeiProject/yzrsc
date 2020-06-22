
package com.commodity.scw.model;

import java.io.Serializable;

/**
 * Function: 上传文件实体
 *
 * Date: 2016年3月30日 下午12:33:27
 * 
 * @author liyushen
 */
public class UploadFile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 */
	private String mType;
	/**
	 * 文件路径
	 */
	private String mFilePath;
	
	/**
	 * tag路径
	 */
	private String mTagPath;
	
	/**
	 * 是否是gif路径
	 */
	private boolean isFilterPath;
	
	private String mFile ;
	
	private int progress;

	/**
	 * 是否是发布页面的上传
	 */
	public boolean isPublish;
	
	/**
	 * 保存tag标签是否成功
	 * @return
	 */
	public boolean isSaveTagPicSuccess;
	

	public boolean isSaveTagPicSuccess() {
		return isSaveTagPicSuccess;
	}

	public void setSaveTagPicSuccess(boolean isSaveTagPicSuccess) {
		this.isSaveTagPicSuccess = isSaveTagPicSuccess;
	}

	public String getmTagPath() {
		return mTagPath;
	}

	public void setmTagPath(String mTagPath) {
		this.mTagPath = mTagPath;
	}

	public boolean isFilterPath() {
		return isFilterPath;
	}

	public void setFilterPath(boolean isFilterPath) {
		this.isFilterPath = isFilterPath;
	}

	public boolean isPublish() {
		return isPublish;
	}

	public void setPublish(boolean isPublish) {
		this.isPublish = isPublish;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * 获取文件类型
	 * @return
	 */
	public String getType() {
		return mType;
	}

	/**
	 * 设置文件类型
	 * @param type
	 */
	public void setType(String type) {
		this.mType = type;
	}

	/**
	 * 获取文件路径
	 * @return
	 */
	public String getFilePath() {
		return mFilePath;
	}

	/**
	 * 设置文件路径
	 * @param filePath
	 */
	public void setFilePath(String filePath) {
		this.mFilePath = filePath;
	}
	
	public String getmFile() {
		return mFile;
	}
	
	public void setmFile(String mFile) {
		this.mFile = mFile;
	}

}
