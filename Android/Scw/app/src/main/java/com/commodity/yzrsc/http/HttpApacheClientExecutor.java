package com.commodity.yzrsc.http;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import com.commodity.yzrsc.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.PublishMultipartEntity.ProgressListener;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.model.UploadFile;
import com.commodity.yzrsc.model.UserInfo;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import cn.yohoutils.StringUtil;

/**
 * Function: HttpApacheClient通信框架
 * <p>
 * Date: 2016年3月30日 下午5:29:42
 *
 * @author liyushen
 */
@SuppressLint("SimpleDateFormat")
public class HttpApacheClientExecutor implements com.commodity.yzrsc.http.HttpClient {

    // 下载临时文件后缀
    private static final String DOWNLOAD_TEMP = ".temp";
    private static final String TAG = "HttpExecutor";
    private long mTotalSize;// 上传文件总大小

    /**
     * 下载监听按钮
     */
    private HttpClientListener listener;

    private String pageIndex = "1";// 当前页数
    private String useragent = MainApplication.USER_AGENT;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private UserInfo userInfo = ConfigManager.instance().getUser();

    @Override
    public void post(HttpParam param, com.commodity.yzrsc.http.HttpResponse response) {
        post(param, response, true);
    }

    /**
     * 基本post请求
     *
     * @param param        请求参数
     * @param httpResponse 请求响应
     * @param isGzip       是否压缩
     */
    @SuppressWarnings("unused")
    @SuppressLint("SimpleDateFormat")
    @Override
    public void post(HttpParam param,
                     com.commodity.yzrsc.http.HttpResponse httpResponse, boolean isGzip) {
        HttpPost request = null;
        String url = param.getmUrl();
        List<NameValuePair> params = null;
        HttpEntity entity = null;
        String content = null;
        int status = 0;
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            request = new HttpPost(url);
            //头部信息 及clinent配置
            Map<String, String> headerMap = getHeaderMap(client);
            for (String key : headerMap.keySet()) {
                request.addHeader(key, headerMap.get(key));
            }

            if (isGzip) {
                client.addRequestInterceptor(new HttpRequestInterceptor() {
                    @Override
                    public void process(HttpRequest request, HttpContext context)
                            throws HttpException, IOException {
                        if (!request.containsHeader("Accept-Encoding")) {
                            request.addHeader("Accept-Encoding", "gzip");
                        }
                    }
                });
                client.addResponseInterceptor(new HttpResponseInterceptor() {
                    @Override
                    public void process(HttpResponse response,
                                        HttpContext context) throws HttpException,
                            IOException {
                        HttpEntity entity = response.getEntity();
                        Header header = entity.getContentEncoding();
                        if (header != null) {
                            HeaderElement[] elements = header.getElements();
                            for (int i = 0; i < elements.length; i++) {
                                if (elements[i].getName().equalsIgnoreCase(
                                        "gzip")) {
                                    response.setEntity(new GzipDecompressingEntity(
                                            response.getEntity()));
                                    return;
                                }
                            }
                        }
                    }
                });
            }
            if (!param.ismIsExternalLink()) {
                params = param.getmParamsList();
                String requestStr = param.getmUrl() + "?"
                        + getNameValuePairString(params);
                Log.e("HttpLog"+"post url", "Post to:  " + requestStr);
                // 设置Http post请求参数
                UrlEncodedFormEntity urlEncodeEntity = new UrlEncodedFormEntity(
                        params, HTTP.UTF_8);
                StringEntity jsonEntity = new StringEntity(
                        getNameValuePairJson(params), "UTF-8");
                request.setEntity(jsonEntity);
            } else {
                Log.e("HttpLog"+"post url", "Post to:  " + url);
            }
            HttpResponse response = client.execute(request);
            //统一处理返回结果
            DealResponse(response, param, httpResponse, entity, content, status, HttpMothed.POST);

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
            handleCallback(param, httpResponse, content, status,
                    MainApplication.getContext().getString(R.string.TIP_NODATA));
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (request != null) {
                request.abort();
            }
        }
    }

    /*
     * 上传文件
     *
     * @param param
     *            请求参数
     * @param httpResponse
     *            请求响应
     */
    @SuppressLint("SimpleDateFormat")
    @Override
    public void uploadFile(HttpParam param,
                           final com.commodity.yzrsc.http.HttpResponse httpResponse) {
        String url = param.getmUrl();
        HttpPost post = null;
        HttpEntity entity = null;
        String content = null;
        int status = 0;
        // StringBody strBody = null;
        final List<UploadFile> uploadFiles = param.getmFileList();
        PublishMultipartEntity reqEntity = new PublishMultipartEntity(
                new ProgressListener() {
                    @Override
                    public void transferred(long num) {
                        int progress = (int) (((float) num / mTotalSize) * 100);
                        try {
                            if (null != uploadFiles
                                    && null != uploadFiles.get(0)
                                    && uploadFiles.get(0).isPublish) {
                                if (progress > 0) {
                                    httpResponse
                                            .handleProgressResponse(progress);
                                    // Thread.sleep(1500);
                                }
                            } else {
                                httpResponse.handleProgressResponse(progress);
                            }

                        } catch (Exception e) {
                            Log.e("HttpLog"+TAG, "transfer file error");
                        }
                    }
                });
        // MultipartEntity reqEntity = null;
        try {
            post = new HttpPost(url);
            DefaultHttpClient client = new DefaultHttpClient();
            post.addHeader("User-Agent", useragent);
            post.addHeader("X-CompanyId", "427");
            List<NameValuePair> params = param.getmParamsList();
            // String requestStr = param.getmUrl() + "?"
            // + getNameValuePairString(params);
            // Log.e("HttpLog"+TAG, "post file:" + requestStr);

            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT,
                    HttpParam.TIMEOUT_TIME);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    600000);
            for (int i = 0; i < params.size(); i++) {
                NameValuePair info = params.get(i);
                if (info != null) {
                    StringBody strBody = new StringBody(info.getValue(),
                            Charset.forName("utf-8"));
                    reqEntity.addPart(info.getName(), strBody);
                }
            }

            if (uploadFiles != null) {
                for (UploadFile uploadFile : uploadFiles) {

                    File file = new File(uploadFile.getFilePath());
                    if ("img".equals(uploadFile.getType())) {
                        // 图片
                        FileBody imageFileBody = null;
                        // String base64Str = Bitmap2StrByBase64(file);
                        // imageFileBody = new
                        // StringBody(base64Str,Charset.forName("utf-8"));
                        imageFileBody = new FileBody(file, "image.jpg",
                                "image/jpg", "utf-8");
                        reqEntity.addPart("img", imageFileBody);

                    } else if ("vioce".equals(uploadFile.getType())) {
                        // 音频
                        FileBody voiceFileBody = new FileBody(file,
                                "voice.amr", "audio/amr", "utf-8");
                        reqEntity.addPart("voice", voiceFileBody);

                    } else if ("video".equals(uploadFile.getType())) {
                        // 视频
                        FileBody voiceFileBody = new FileBody(file,
                                "video.mp4", "video/mp4", "utf-8");
                        reqEntity.addPart("video", voiceFileBody);
                    }
                }
                mTotalSize = reqEntity.getContentLength();
            }

            post.setEntity(reqEntity);

            HttpResponse response = client.execute(post);
            status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_CREATED || status == HttpStatus.SC_OK) {
                entity = response.getEntity();
                content = EntityUtils.toString(entity, HTTP.UTF_8).trim();
            }
            handleCallback(param, httpResponse, content, status,
                    MainApplication.getContext().getString(R.string.TIP_NONET));
        } catch (SocketTimeoutException e) {
            handleCallback(
                    param,
                    httpResponse,
                    content,
                    status,
                    MainApplication.getContext().getString(R.string.TIP_NETOUTTIME));
        } catch (ConnectTimeoutException e) {
            handleCallback(
                    param,
                    httpResponse,
                    content,
                    status,
                    MainApplication.getContext().getString(
                            R.string.TIP_NETOUTTIME));
        } catch (Exception e) {
            handleCallback(param, httpResponse, content, status,
                    MainApplication.getContext().getString(R.string.TIP_NODATA));
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (post != null) {
                post.abort();
            }
        }
    }

    public String Bitmap2StrByBase64(File file) {
        try {
            FileInputStream inputFile;
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 基本get请求
     *
     * @param param        请求参数
     * @param httpResponse 请求响应
     */
    @Override
    public void get(HttpParam param, com.commodity.yzrsc.http.HttpResponse httpResponse) {
        HttpGet request = null;
        int status = 0;
        HttpEntity entity = null;
        String content = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            request = new HttpGet();
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            //头部信息 及clinent配置
            Map<String, String> headerMap = getHeaderMap(client);
            for (String key : headerMap.keySet()) {
                request.addHeader(key, headerMap.get(key));
            }
            request.setParams(params);
            //配置其他信息
            List<NameValuePair> nameValue = param.getmParamsList();
            String url="";
            if (nameValue==null||nameValue.size()==0){
                url = param.getmUrl();
            }else {
                url = param.getmUrl() + "?"
                        + getNameValuePairString(nameValue);
            }

            request.addHeader("X-PageIndex", pageIndex);
            request.setURI(new URI(url));
            HttpResponse response = null;
            response = client.execute(request);
            Log.e("HttpLog"+"get url", "get to:  " + url);
            //统一处理返回结果
            DealResponse(response, param, httpResponse, entity, content, status, HttpMothed.GET);
        } catch (ConnectTimeoutException e) {
            handleCallback(
                    param,
                    httpResponse,
                    content,
                    status,
                    MainApplication.getContext().getString(
                            R.string.TIP_NETOUTTIME));
        } catch (SocketTimeoutException e) {
            handleCallback(
                    param,
                    httpResponse,
                    content,
                    status,
                    MainApplication.getContext().getString(
                            R.string.TIP_NETOUTTIME));
        } catch (Exception e) {
            handleCallback(param, httpResponse, content, status,
                    MainApplication.getContext().getString(R.string.TIP_NODATA));
            Log.e("HttpLog"+TAG, "get: " + e.toString());
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (request != null) {
                request.abort();
            }
        }
    }


    /**
     * 基本put请求
     *
     * @param param        请求参数
     * @param httpResponse 请求响应
     * @param isGzip       是否压缩
     */
    @SuppressWarnings("unused")
    public void update(HttpParam param,
                       com.commodity.yzrsc.http.HttpResponse httpResponse, boolean isGzip) {
        HttpPut request = null;
        String url = param.getmUrl();
        List<NameValuePair> params = null;
        HttpEntity entity = null;
        String content = null;
        int status = 0;
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            request = new HttpPut(url);
            //头部信息 及clinent配置
            Map<String, String> headerMap = getHeaderMap(client);
            for (String key : headerMap.keySet()) {
                request.addHeader(key, headerMap.get(key));
            }
            if (isGzip) {
                client.addRequestInterceptor(new HttpRequestInterceptor() {
                    @Override
                    public void process(HttpRequest request, HttpContext context)
                            throws HttpException, IOException {
                        if (!request.containsHeader("Accept-Encoding")) {
                            request.addHeader("Accept-Encoding", "gzip");
                        }
                    }
                });
                client.addResponseInterceptor(new HttpResponseInterceptor() {
                    @Override
                    public void process(HttpResponse response,
                                        HttpContext context) throws HttpException,
                            IOException {
                        HttpEntity entity = response.getEntity();
                        Header header = entity.getContentEncoding();
                        if (header != null) {
                            HeaderElement[] elements = header.getElements();
                            for (int i = 0; i < elements.length; i++) {
                                if (elements[i].getName().equalsIgnoreCase(
                                        "gzip")) {
                                    response.setEntity(new GzipDecompressingEntity(
                                            response.getEntity()));
                                    return;
                                }
                            }
                        }
                    }
                });
            }
            if (!param.ismIsExternalLink()) {
                params = param.getmParamsList();
                String requestStr = param.getmUrl() + "?"
                        + getNameValuePairString(params);
                Log.e("HttpLog"+"update url", "update to:  " + requestStr);
                // 设置Http post请求参数
                UrlEncodedFormEntity urlEncodeEntity = new UrlEncodedFormEntity(
                        params, HTTP.UTF_8);

                StringEntity jsonEntity = new StringEntity(
                        getNameValuePairJson(params), "UTF-8");
                // HttpEntity httpEntity = new url;
                // String temp = urlEncodeEntity.toString();
                request.setEntity(jsonEntity);
            } else {
                Log.e("HttpLog"+"update url", "update to:  " + url);
            }
            HttpResponse response = client.execute(request);
            //统一处理返回结果
            DealResponse(response, param, httpResponse, entity, content, status, HttpMothed.UPDATE);
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
            handleCallback(param, httpResponse, content, status,
                    MainApplication.getContext().getString(R.string.TIP_NODATA));
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (request != null) {
                request.abort();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public void delete(HttpParam param,
                       com.commodity.yzrsc.http.HttpResponse httpResponse) {
        HttpDelete request = null;
        HttpEntity entity = null;
        int status = 0;
        String content = null;
        try {
            DefaultHttpClient client = new DefaultHttpClient();
            request = new HttpDelete();
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            //头部信息 及clinent配置
            Map<String, String> headerMap = getHeaderMap(client);
            for (String key : headerMap.keySet()) {
                request.addHeader(key, headerMap.get(key));
            }

            request.setParams(params);
            List<NameValuePair> nameValue = param.getmParamsList();
            String url = param.getmUrl() + "?"
                    + getNameValuePairString(nameValue);
            request.addHeader("X-PageIndex", pageIndex);
            // String uri = URLEncoder.encode(url, "UTF-8");
            request.setURI(new URI(url));
            Log.e("HttpLog"+TAG, "delete url:" + url);
            HttpResponse response = null;
            response = client.execute(request);
            //统一处理返回结果
            DealResponse(response, param, httpResponse, entity, content, status, HttpMothed.DELETE);
        } catch (ConnectTimeoutException e) {
            handleCallback(
                    param,
                    httpResponse,
                    content,
                    status,
                    MainApplication.getContext().getString(
                            R.string.TIP_NETOUTTIME));
        } catch (SocketTimeoutException e) {
            handleCallback(
                    param,
                    httpResponse,
                    content,
                    status,
                    MainApplication.getContext().getString(
                            R.string.TIP_NETOUTTIME));
        } catch (Exception e) {
            handleCallback(param, httpResponse, content, status,
                    "请求异常！");
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (request != null) {
                request.abort();
            }
        }
    }

    private String getNameValuePairString(List<NameValuePair> nameValue) {
        if (nameValue == null || nameValue.size() == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        String pairStr = null;
        for (int i = 0; i < nameValue.size(); i++) {
            NameValuePair info = nameValue.get(i);
            if (info == null) {
                continue;
            }
            String name = info.getName();
            if ("X-PageIndex".equals(name)) {
                pageIndex = info.getValue();
            }
            sb.append("&").append(info.toString());
        }
        pairStr = sb.toString();
        pairStr = pairStr.substring(1);
        return pairStr;
    }

    private String getNameValuePairJson(List<NameValuePair> nameValue) {
        if (nameValue == null || nameValue.size() == 0) {
            return "";
        }
        JSONObject jo = new JSONObject();
        for (int i = 0; i < nameValue.size(); i++) {
            NameValuePair info = nameValue.get(i);
            if (info == null) {
                continue;
            }
            try {
                jo.putOpt(info.getName(), info.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jo.toString();
    }

    private String getNameValuePairJson(List<Map<String, Object>> nameValue,
                                        boolean isNew) {
        if (nameValue == null || nameValue.size() == 0) {
            return "";
        }
        JSONObject jo = new JSONObject();
        for (int i = 0; i < nameValue.size(); i++) {
            for (String k : nameValue.get(i).keySet()) {
                if (nameValue.get(i).get(k) == null) {
                    continue;
                }
                try {
                    jo.putOpt(k, nameValue.get(i).get(k));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jo.toString();
    }

    /**
     * 拦截压缩事件处理
     */
    private static final class GzipDecompressingEntity extends
            HttpEntityWrapper {

        public GzipDecompressingEntity(HttpEntity entity) {
            super(entity);
        }

        @Override
        public InputStream getContent() throws IOException {
            return new GZIPInputStream(wrappedEntity.getContent());
        }

        @Override
        public long getContentLength() {
            return -1;
        }
    }

    /**
     * 下载文件
     */
    @Override
    public void save(String url, File file) throws IOException {
        FileOutputStream fos = null;

        try {
            // 下载
            get(url, file, HttpParam.TIMEOUT_TIME, -1);
        } finally {
            closeQuietly(fos);
        }
    }

    @Override
    public void setListener(HttpClientListener listener) {
        this.listener = listener;
    }

    @Override
    public HttpClientListener getListener() {
        return listener;
    }

    /**
     * 获取网络文件流
     *
     * @param url         URL
     * @param timeout     输出流
     * @param timeout     超时时间
     * @param rangeLength
     * @throws IOException
     */
    private void get(String url, File file, int timeout, int rangeLength)
            throws IOException {
        // 修改temp
        File tempFile = new File(file.getAbsoluteFile() + DOWNLOAD_TEMP);

        URL u = new URL(url);
        URLConnection conn = u.openConnection();

        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);

        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).setInstanceFollowRedirects(true);
        }

        InputStream in = conn.getInputStream();
        if ("gzip".equals(conn.getContentEncoding())) { // 是否于压缩的形式返回
            in = new GZIPInputStream(in);
        }

        int httpResponseCode = getResponseCode(conn);

        if (httpResponseCode != HttpURLConnection.HTTP_OK
                && httpResponseCode != HttpURLConnection.HTTP_PARTIAL) {
            throw new IOException(httpResponseCode + "");
        }

        onHeaders(conn.getHeaderFields());
        FileOutputStream out = new FileOutputStream(tempFile, false);
        try {
            byte[] b = new byte[4096];
            int n = 0;
            while ((n = in.read(b, 0, b.length)) != -1) {
                out.write(b, 0, n);
                onData(b, 0, n);
            }

            closeQuietly(out);

            tempFile.renameTo(file);
            onComplete();
        } catch (Exception e) {
            onError(e);
        } finally {
            closeQuietly(in);
            closeQuietly(out);
        }
    }

    /**
     * 关闭输出流
     *
     * @param closeable
     */
    private void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            onError(e);
        }
    }

    private void onHeaders(Map<String, List<String>> headerFields) {
        if (getListener() != null) {
            getListener().onHeaders(this, headerFields);
        }
    }

    private void onError(Exception e) {
        if (getListener() != null) {
            getListener().onError(this, e);
        }
    }

    private void onComplete() {
        if (getListener() != null) {
            getListener().onComplete(this);
        }
    }

    private void onData(byte[] b, int i, int n) {
        if (getListener() != null) {
            getListener().onData(this, b, 0, n);
        }
    }

    /**
     * 取得ResponseCode
     *
     * @param conn
     * @return
     */
    private int getResponseCode(URLConnection conn) {
        try {
            return ((HttpURLConnection) conn).getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 修改请求方式可以传递对象
     *
     * @param param
     * @param httpResponse
     */
    @SuppressLint("SimpleDateFormat")
    @Override
    public void post(HttpParamNew param,
                     com.commodity.yzrsc.http.HttpResponse httpResponse) {
        HttpPost request = null;
        String url = param.getmUrl();
        List<Map<String, Object>> params = null;
        HttpEntity entity = null;
        String content = null;
        int status = 0;
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            request = new HttpPost(url);
            //头部信息 及clinent配置
            Map<String, String> headerMap = getHeaderMap(client);
            for (String key : headerMap.keySet()) {
                request.addHeader(key, headerMap.get(key));
            }
            if (!param.ismIsExternalLink()) {
                params = param.getmParamsList();
                String requestStr = param.getmUrl() + "?"
                        + getNameValuePairJson(params, true);
                Log.e("HttpLog"+"post url", "NewPost to:  " + requestStr);
                StringEntity jsonEntity = new StringEntity(getNameValuePairJson(params, true), "UTF-8");
                request.setEntity(jsonEntity);
            } else {
                Log.e("HttpLog"+"post url", "NewPost to:  " + url);
            }
            HttpResponse response = client.execute(request);
            //统一处理返回结果
            DealResponse(response, param, httpResponse, entity, content, status, HttpMothed.POST);
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
            handleCallback(param, httpResponse, content, status,
                    MainApplication.getContext().getString(R.string.TIP_NODATA));
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (request != null) {
                request.abort();
            }
        }

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void update(HttpParamNew param,
                       com.commodity.yzrsc.http.HttpResponse httpResponse, boolean isGzip) {
        HttpPut request = null;
        String url = param.getmUrl();
        List<Map<String, Object>> params = null;
        HttpEntity entity = null;
        String content = null;
        int status = 0;
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            request = new HttpPut(url);
            //头部信息 及clinent配置
            Map<String, String> headerMap = getHeaderMap(client);
            for (String key : headerMap.keySet()) {
                request.addHeader(key, headerMap.get(key));
            }
            if (!param.ismIsExternalLink()) {
                params = param.getmParamsList();
                String requestStr = param.getmUrl() + "?"
                        + getNameValuePairJson(params, true);
                Log.e("HttpLog"+"updateNew url", "updateNew to:  " + requestStr);
                StringEntity jsonEntity = new StringEntity(
                        getNameValuePairJson(params, true), "UTF-8");
                request.setEntity(jsonEntity);
            } else {
                Log.e("HttpLog"+"updateNew url", "updateNew to:  " + url);
            }
            HttpResponse response = client.execute(request);
            //统一处理返回结果
            DealResponse(response, param, httpResponse, entity, content, status, HttpMothed.UPDATE);
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
                            R.string.TIP_NODATA));
        } finally {
            if (entity != null) {
                try {
                    entity.consumeContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (request != null) {
                request.abort();
            }
        }

    }

    //统一请求头部信息
    private Map<String, String> getHeaderMap(DefaultHttpClient client) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json; charset=UTF-8");
        headerMap.put("User-Agent", useragent);
        if (ConfigManager.instance().isLogin()&&userInfo!=null){
            headerMap.put("scw-token", userInfo.getDeviceToken());
            Log.e("scw-token",userInfo.getDeviceToken());
        }
        if (client != null) {
            client.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT,
                    HttpParam.TIMEOUT_TIME);
            client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    HttpParam.TIMEOUT_TIME);
        }

        return headerMap;
    }

    //处理返回的值
    private void DealResponse(HttpResponse response, HttpParam param, com.commodity.yzrsc.http.HttpResponse httpResponse, HttpEntity entity, String content, int status, HttpMothed mothed) {
        status = response.getStatusLine().getStatusCode();
        entity = response.getEntity();
        Header[] headArray = response.getAllHeaders();
        String UId = "";
        String Token = "";
        if (headArray != null) {
            int size = headArray.length;
            for (int i = 0; i < size; i++) {
                Header header = headArray[i];
                if (header != null) {
                    String name = header.getName();
                    if ("X-ContanctId".endsWith(name)) {
                        UId = header.getValue();
                    }
                    if ("X-Token".endsWith(name)) {
                        Token = header.getValue();
                    }
                    if ("X-Count".endsWith(name)) {
                        httpResponse.setTotalCount(StringUtil.valueOfInt(header.getValue(), 0));
                    }
                    if ("X-PageCount".endsWith(name)) {
                        httpResponse.setTotalPage(StringUtil.valueOfInt(header.getValue(), 0));
                    }
                }
            }
        }
        try {
            content = EntityUtils.toString(entity, HTTP.UTF_8).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mothed == HttpMothed.GET) {

        }
        String msg = "";
        if (status != HttpStatus.SC_CREATED && status != HttpStatus.SC_OK) {
            msg = "";//此处为访问失败
        } else {
            if (StringUtil.isEmpty(content)) {
                content = "";
            }
            status = HttpStatus.SC_OK;
        }
        handleCallback(param, httpResponse, content, status, msg);
    }

    //处理返回的值
    private void DealResponse(HttpResponse response, HttpParamNew param, com.commodity.yzrsc.http.HttpResponse httpResponse, HttpEntity entity, String content, int status, HttpMothed mothed) {
        status = response.getStatusLine().getStatusCode();
        entity = response.getEntity();
        Header[] headArray = response.getAllHeaders();
        String UId = "";
        String Token = "";
        if (headArray != null) {
            int size = headArray.length;
            for (int i = 0; i < size; i++) {
                Header header = headArray[i];
                if (header != null) {
                    String name = header.getName();
                    if ("X-ContanctId".endsWith(name)) {
                        UId = header.getValue();
                    }
                    if ("X-Token".endsWith(name)) {
                        Token = header.getValue();
                    }
                    if ("X-Count".endsWith(name)) {
                        httpResponse.setTotalCount(StringUtil.valueOfInt(header.getValue(), 0));
                    }
                    if ("X-PageCount".endsWith(name)) {
                        httpResponse.setTotalPage(StringUtil.valueOfInt(header.getValue(), 0));
                    }
                }
            }
        }
        try {
            content = EntityUtils.toString(entity, HTTP.UTF_8).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String msg = "";
        if (status != HttpStatus.SC_CREATED && status != HttpStatus.SC_OK) {
            msg = "";//此处为访问失败
        } else {
            if (StringUtil.isEmpty(content)) {
                content = "";
            }
            status = HttpStatus.SC_OK;
        }
        handleCallback(param, httpResponse, content, status, msg);
    }

    /**
     * 处理回调信息（通信成功或失败调用该方法）
     *
     * @param response http响应对象
     * @param content  返回内容
     * @param httpCode http code码
     * @param httpMsg  http信息（如针对http超时错误信息）
     */
    private void handleCallback(HttpParam param,
                                com.commodity.yzrsc.http.HttpResponse response, String content,
                                int httpCode, String httpMsg) {
        if (response != null) {
            try {
                response.handleResponse(param, content, httpCode, httpMsg);
            } catch (Throwable e) {
                Log.e("HttpLog"+TAG, "异常信息"+e.toString());
            }
        }
    }

    /**
     * 处理回调信息（通信成功或失败调用该方法）
     *
     * @param response http响应对象
     * @param content  返回内容
     * @param httpCode http code码
     * @param httpMsg  http信息（如针对http超时错误信息）
     */
    private void handleCallback(HttpParamNew param,
                                com.commodity.yzrsc.http.HttpResponse response, String content,
                                int httpCode, String httpMsg) {
        if (response != null) {
            try {
                response.handleResponse(param, content, httpCode, httpMsg);
            } catch (Throwable e) {
                Log.e("HttpLog"+TAG, "异常信息"+e.toString());
            }
        }
    }
}
