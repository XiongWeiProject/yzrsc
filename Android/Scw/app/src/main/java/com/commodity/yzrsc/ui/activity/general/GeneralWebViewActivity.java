package com.commodity.yzrsc.ui.activity.general;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.widget.webview.CustomWebView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by liyushen on 2017/5/13 22:41
 * 通用显示webview
 */
public class GeneralWebViewActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.customwebview)
    CustomWebView customwebview;

    @Override
    protected int getContentView() {
        return R.layout.activity_gener_webview;
    }

    @Override
    protected void initView() {
        if (getIntent().getExtras()!=null&&getIntent().getExtras().containsKey("title")){
            tv_title.setText(getIntent().getExtras().getString("title"));
        }
        if (getIntent().getExtras()!=null&&getIntent().getExtras().containsKey("content_url")){
            customwebview.loadUrl(getIntent().getExtras().getString("content_url"));
        }
        customwebview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                String referer = "http://yzrsc.83soft.cn";
                if(url.startsWith("http:") || url.startsWith("https:") ) {
                    if (url.contains("https://wx.tenpay.com")) {
                        Map<String, String> extraHeaders = new HashMap<>();
                        extraHeaders.put("Referer", referer);
                        view.loadUrl(url, extraHeaders);
                        referer = url;
                        return true;
                    }
                    view.loadUrl(url);
                    return true;
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
            }
        });
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.web_back})
    public void click(View v){
        switch (v.getId()){
            case R.id.web_back:
                if(customwebview.canGoBack()){
                    customwebview.goBack();
                }else {
                    finish();
                }
                break;
        }
    }
}
