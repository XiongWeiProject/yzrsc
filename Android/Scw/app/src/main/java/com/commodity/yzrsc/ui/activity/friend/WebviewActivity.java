package com.commodity.yzrsc.ui.activity.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebviewActivity extends BaseActivity {


    @Bind(R.id.head_back)
    LinearLayout headBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_text_right)
    TextView headTextRight;
    @Bind(R.id.webview)
    WebView webview;

    public static void startAction(Context activity, String link, String title) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        String link = getIntent().getStringExtra("link");
        String title = getIntent().getStringExtra("title");
        headTitle.setText(title);
        WebSettings settings = webview.getSettings();
        settings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setGeolocationDatabasePath(dir);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBuiltInZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }
        });
        webview.loadUrl(link);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.head_back)
    public void onViewClicked() {
        finish();
    }
}
