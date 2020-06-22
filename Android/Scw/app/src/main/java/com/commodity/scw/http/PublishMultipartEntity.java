/*
 * Created by DaiHui
 * 
 * Date:2014年9月22日下午12:34:42 
 * 
 * Copyright (c) 2014, Show(R). All rights reserved.
 * 
 */
package com.commodity.scw.http;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Function: 文件上传监听器
 *
 * Date: 2014年9月22日 下午12:34:42
 * 
 * @author DaiHui
 */
public class PublishMultipartEntity extends MultipartEntity {
	/**
	 * 进度监听接口
	 */
	private final ProgressListener listener;

	public PublishMultipartEntity(final ProgressListener listener) {
		super();
		this.listener = listener;
	}

	public PublishMultipartEntity(final HttpMultipartMode mode, final ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	public PublishMultipartEntity(HttpMultipartMode mode, final String boundary, final Charset charset, final ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static interface ProgressListener {
		void transferred(long num);
	}

	public static class CountingOutputStream extends FilterOutputStream {

		private final ProgressListener listener;
		private long transferred;

		public CountingOutputStream(final OutputStream out, final ProgressListener listener) {
			super(out);
			this.listener = listener;
			this.transferred = 0;
		}

		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
			this.transferred += len;
			this.listener.transferred(this.transferred);
		}

		public void write(int b) throws IOException {
			out.write(b);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}

}
