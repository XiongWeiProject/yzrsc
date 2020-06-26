package com.qiansheng.commnityestate.ui.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qiansheng.commnityestate.R;
import com.qiansheng.commnityestate.base.BaseActivity;
import com.qiansheng.commnityestate.view.TitleView;

import butterknife.BindView;

public class WebviewActivity extends BaseActivity {

    @BindView(R.id.title_view)
    TitleView titleView;
    @BindView(R.id.webview)
    WebView   webview;

    public static void startAction(Context activity, String link, String title) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra("link", link);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        String link = getIntent().getStringExtra("link");
        String title = getIntent().getStringExtra("title");
        titleView.setTitleText(title);
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

}
