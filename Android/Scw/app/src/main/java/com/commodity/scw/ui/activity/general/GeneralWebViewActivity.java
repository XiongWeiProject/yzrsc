package com.commodity.scw.ui.activity.general;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.widget.webview.CustomWebView;

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
                if(url.startsWith("http:") || url.startsWith("https:") ) {
                    view.loadUrl(url);
                    return false;
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
