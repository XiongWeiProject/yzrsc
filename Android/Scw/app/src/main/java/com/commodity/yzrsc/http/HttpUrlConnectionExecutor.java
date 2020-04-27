
package com.commodity.yzrsc.http;

import android.util.Log;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.UploadFile;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HTTP;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;


/**
 * Function: HttpUrlConnection通信框架
 * 
 * Date: 2014年9月22日 下午4:05:09
 * 
 * @author DaiHui
 */
public class HttpUrlConnectionExecutor implements HttpClient {
	private static final String TAG = "HttpExecutor";
	private static final String PARAMETER_SEPARATOR = "&";
	private static final String NAME_VALUE_SEPARATOR = "=";
	private String useragent = MainApplication.USER_AGENT;
	/**
	 * 基本post请求
	 * 
	 * @param param
	 *            请求参数
	 * @param httpResponse
	 *            请求响应
	 */
	@Override
	public void post(HttpParam param, HttpResponse httpResponse) {
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedWriter out = null;
		BufferedReader buff_in = null;
		String content = null;
		int status = 0;
		try {
			url = new URL(param.getmUrl());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(HttpParam.TIMEOUT_TIME);// 设置超时
			urlConnection.setReadTimeout(HttpParam.TIMEOUT_TIME);// 设置超时
			urlConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			urlConnection.addRequestProperty("User-Agent",
					useragent);
			urlConnection.setChunkedStreamingMode(0);// 用于减少内存开支和减少延迟，默认chunk
														// length为1024
			if (!param.ismIsExternalLink()) {
				out = new BufferedWriter(new OutputStreamWriter(
						urlConnection.getOutputStream()));
				List<NameValuePair> temp = param.getmParamsList();
				Log.d(
						TAG,
						"post url:"
								+ url
								+ "?"
								+ temp.toString().substring(1,
										temp.toString().length() - 1));
				String str_param = format(temp, HTTP.UTF_8);
				out.write(str_param);
				out.flush();
			} else {
				Log.d(TAG, "post url:" + url);
			}
			status = urlConnection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				buff_in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				String inputLine;
				StringBuilder response_builder = new StringBuilder();
				// 数据长度读取不要使用getContentLength，此处应一直循环读取输入流行
				while ((inputLine = buff_in.readLine()) != null) {
					response_builder.append(inputLine);
				}
				content = response_builder.toString();
			}
			handleCallback(
					param,
					httpResponse,
					content,
					status,
					MainApplication.getContext().getString(
							R.string.TIP_NONET));
		} catch (SocketTimeoutException e) {
			handleCallback(
					param,
					httpResponse,
					content,
					status,
					MainApplication.getContext().getString(
							R.string.TIP_NETOUTTIME));
		} catch (ConnectTimeoutException e) {
			handleCallback(
					param,
					httpResponse,
					content,
					status,
					MainApplication.getContext().getString(
							R.string.TIP_NETOUTTIME));
		} catch (Exception e) {
			handleCallback(
					param,
					httpResponse,
					content,
					status,
					MainApplication.getContext().getString(
							R.string.TIP_NONET));
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (buff_in != null) {
				try {
					buff_in.close();
				} catch (IOException e) {
				}
			}
			if (urlConnection != null) {
				urlConnection.disconnect();// 如果http.keepAlive设为false，则disconnect后不会将底层socket放入已连接过sockets
											// pool中，且增加了延迟
			}
		}
	}

	/**
	 * 基本get请求
	 * 
	 * @param param
	 *            请求参数
	 * @param httpResponse
	 *            请求响应
	 */
	@Override
	public void get(HttpParam param, HttpResponse httpResponse) {
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedReader buff_in = null;
		String content = null;
		int status = 0;
		try {
			url = new URL(param.getmUrl());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("GET");
			urlConnection.setConnectTimeout(HttpParam.TIMEOUT_TIME);// 设置超时
			urlConnection.setReadTimeout(HttpParam.TIMEOUT_TIME);// 设置超时
			urlConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			urlConnection.addRequestProperty("User-Agent",
					useragent);
			urlConnection.setChunkedStreamingMode(0);// 用于减少内存开支和减少延迟，默认chunk
														// length为1024
			urlConnection.connect();
			Log.e(TAG, "get url:" + url);
			status = urlConnection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				buff_in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				String inputLine;
				StringBuilder response_builder = new StringBuilder();
				// 数据长度读取不要使用getContentLength，此处应一直循环读取输入流行
				while ((inputLine = buff_in.readLine()) != null) {
					response_builder.append(inputLine);
				}
				content = response_builder.toString();
			}
			handleCallback(
					param,
					httpResponse,
					content,
					status,
					MainApplication.getContext().getString(
							R.string.TIP_NONET));
		} catch (SocketTimeoutException e) {
			handleCallback(
					param,
					httpResponse,
					content,
					status,
					MainApplication.getContext().getString(
							R.string.TIP_NETOUTTIME));
		} catch (ConnectTimeoutException e) {
			handleCallback(
					param,
					httpResponse,
					content,
					status,
					MainApplication.getContext().getString(
							R.string.TIP_NETOUTTIME));
		} catch (Exception e) {
			handleCallback(
					param,
					httpResponse,
					content,
					status,
					MainApplication.getContext().getString(
							R.string.TIP_NONET));
		} finally {
			if (buff_in != null) {
				try {
					buff_in.close();
				} catch (IOException e) {
				}
			}
			if (urlConnection != null) {
				urlConnection.disconnect();// 如果http.keepAlive设为false，则disconnect后不会将底层socket放入已连接过sockets
											// pool中，且增加了延迟
			}
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param param
	 *            请求参数
	 * @param httpResponse
	 *            请求响应
	 */
	public void uploadFile(HttpParam param, HttpResponse httpResponse) {
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedReader buff_in = null;
		String content = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		String PREFIX = "--", LINE_END = "\r\n";
		int status = 0;
		try {
			url = new URL(param.getmUrl());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(HttpParam.TIMEOUT_TIME);// 设置超时
			urlConnection.setReadTimeout(HttpParam.TIMEOUT_TIME);// 设置超时
			urlConnection.setRequestProperty("Charset", HTTP.UTF_8);
			urlConnection.setRequestProperty("Content-Type", CONTENT_TYPE
					+ ";boundary=" + BOUNDARY);
			urlConnection.addRequestProperty("User-Agent",
					useragent);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setChunkedStreamingMode(0);// 用于减少内存开支和减少延迟，默认chunk
														// length为1024
			List<UploadFile> fileList = param.getmFileList();
			for (UploadFile uploadFile : fileList) {
				File file = new File(uploadFile.getFilePath());
				out = new BufferedOutputStream(new DataOutputStream(
						urlConnection.getOutputStream()));
				if (file != null) {
					/**
					 * 当文件不为空，把文件包装并且上传
					 */

					StringBuffer sb = new StringBuffer();
					sb.append(PREFIX);
					sb.append(BOUNDARY);
					sb.append(LINE_END);
					/**
					 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
					 * filename是文件的名字，包含后缀名的 比如:abc.png
					 */

					sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
							+ file.getName() + "\"" + LINE_END);
					sb.append("Content-Type: application/octet-stream; charset="
							+ HTTP.UTF_8 + LINE_END);
					sb.append(LINE_END);
					out.write(sb.toString().getBytes());
					InputStream is = new FileInputStream(file);
					byte[] bytes = new byte[1024];
					int len = 0;
					while ((len = is.read(bytes)) != -1) {
						out.write(bytes, 0, len);
					}
					is.close();
					out.write(LINE_END.getBytes());
					byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
							.getBytes();
					out.write(end_data);
					out.flush();
				}
			}
			status = urlConnection.getResponseCode();
			if (status == HttpURLConnection.HTTP_OK) {
				buff_in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				String inputLine;
				StringBuilder response_builder = new StringBuilder();
				// 数据长度读取不要使用getContentLength，此处应一直循环读取输入流行
				while ((inputLine = buff_in.readLine()) != null) {
					response_builder.append(inputLine);
				}
				content = response_builder.toString();
			}
			handleCallback(param, httpResponse, content, status,
					MainApplication.getContext().getString(R.string.TIP_NONET));
		} catch (SocketTimeoutException e) {
			handleCallback(param, httpResponse, content, status,
					MainApplication.getContext().getString(R.string.TIP_NONET));
		} catch (ConnectTimeoutException e) {
			handleCallback(param, httpResponse, content, status,
					MainApplication.getContext().getString(R.string.TIP_NONET));
		} catch (Exception e) {
			handleCallback(param, httpResponse, content, status,
					MainApplication.getContext().getString(R.string.TIP_NONET));
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
			if (buff_in != null) {
				try {
					buff_in.close();
				} catch (IOException e) {
				}
			}
			if (urlConnection != null) {
				urlConnection.disconnect();// 如果http.keepAlive设为false，则disconnect后不会将底层socket放入已连接过sockets
											// pool中，且增加了延迟
			}
		}
	}

	/**
	 * 格式化参数列表
	 * 
	 * @param parameters
	 *            参数列表
	 * @param encoding
	 *            编码
	 * @return
	 */
	public static String format(final List<? extends NameValuePair> parameters,
								final String encoding) {
		final StringBuilder result = new StringBuilder();
		for (final NameValuePair parameter : parameters) {
			final String encodedName = encode(parameter.getName(), encoding);
			final String value = parameter.getValue();
			final String encodedValue = value != null ? encode(value, encoding)
					: "";
			if (result.length() > 0) {
				result.append(PARAMETER_SEPARATOR);
			}
			result.append(encodedName);
			result.append(NAME_VALUE_SEPARATOR);
			result.append(encodedValue);
		}
		return result.toString();
	}

	/**
	 * 编码
	 * 
	 * @param content
	 *            需编码内容
	 * @param encoding
	 *            编码
	 * @return
	 */
	private static String encode(final String content, final String encoding) {
		try {
			return URLEncoder.encode(content, encoding != null ? encoding
					: HTTP.UTF_8);
		} catch (UnsupportedEncodingException problem) {
			throw new IllegalArgumentException(problem);
		}
	}

	/**
	 * 处理回调信息（通信成功或失败调用该方法）
	 * 
	 * @param response
	 *            http响应对象
	 * @param content
	 *            返回内容
	 * @param httpCode
	 *            http code码
	 * @param httpMsg
	 *            http信息（如针对http超时错误信息）
	 */
	private void handleCallback(HttpParam param, HttpResponse response,
								String content, int httpCode, String httpMsg) {
		Log.d(TAG, "response content:" + content);
		if (response != null) {
			try {
				response.handleResponse(param, content, httpCode, httpMsg);
			} catch (Throwable e) {
				Log.d(TAG, "handle callback");
			}
		}
	}

	@Override
	public void post(HttpParam param, HttpResponse httpResponse, boolean isGzip) {
		// TODO Auto-generated method stub

	}

	@Override
	public void save(String url, File file) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener(HttpClientListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public HttpClientListener getListener() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(HttpParam param, HttpResponse httpResponse,
			boolean isGzip) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(HttpParam param, HttpResponse httpResponse) {
		// TODO Auto-generated method stub

	}

	@Override
	public void post(HttpParamNew param, HttpResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(HttpParamNew param, HttpResponse httpResponse,
			boolean isGzip) {
		// TODO Auto-generated method stub

	}

}
