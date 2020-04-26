package com.commodity.scw.ui.activity.store;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.widget.webview.AdvancedWebView;
import com.ta.utdid2.android.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 328789 on 2017/5/26.
 */

public class StoreProviewActivity extends BaseActivity implements AdvancedWebView.Listener {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.store_proview)
    AdvancedWebView mWebView;
    @Override
    protected int getContentView() {
        return R.layout.activity_storeproview;
    }

    @Override
    protected void initView() {
        mWebView.setListener(this, this);
        mWebView.setGeolocationEnabled(false);
        mWebView.setMixedContentAllowed(true);
        mWebView.setCookiesEnabled(true);
        mWebView.setThirdPartyCookiesEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
//                tip("");
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
//                Toast.makeText(SeeWuliu.this, title, Toast.LENGTH_SHORT).show();
            }

        });
        mWebView.addHttpHeader("X-Requested-With", "");
        Bundle extras = getIntent().getExtras();
        String url = extras.getString("url");
        String falg = extras.getString("falg");
        if("stroe".equals(falg)){
            title.setText("店铺预览");
        }else if("goods".equals(falg)){
            title.setText("宝贝预览");
        }
        if(!StringUtils.isEmpty(url)){
            mWebView.loadUrl(url);
            customLoadding.setTip("加载中...");
            customLoadding.show();
        }
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {
        customLoadding.dismiss();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        customLoadding.dismiss();
        tip("加载失败");
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }
        // ...
        super.onBackPressed();
    }
    @OnClick({R.id.head_back})
    public void click(View v){
        if(mWebView.onBackPressed()){
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            if(mWebView.onBackPressed()){
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
