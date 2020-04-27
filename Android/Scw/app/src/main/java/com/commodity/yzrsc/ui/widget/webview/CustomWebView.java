package com.commodity.yzrsc.ui.widget.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 作者：liyushen on 2017/03/21 13:39
 * 功能：自定义通用的webview
 */
public class CustomWebView extends WebView {

    private ProgressBar progressbar;

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3, 0, 0));
        addView(progressbar);
        setWebChromeClient(new WebChromeClient());
        getSettings().setBuiltInZoomControls(false);// 隐藏缩放按钮
        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕
        getSettings().setUseWideViewPort(false);// 可任意比例缩放
        getSettings().setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        getSettings().setSavePassword(true);
        getSettings().setSaveFormData(true);// 保存表单数据
        getSettings().setJavaScriptEnabled(true);
        getSettings().setGeolocationEnabled(true);// 启用地理定位
//        getSettings().setGeolocationDatabasePath("/data/data/com.commodity.scw/databases/");
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setAppCachePath(this.getContext().getCacheDir().getAbsolutePath());
        getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        String dir = context.getDir("database", Context.MODE_PRIVATE).getPath();
        getSettings().setGeolocationEnabled(true);
        getSettings().setGeolocationDatabasePath(dir);
        getSettings().setDomStorageEnabled(true);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    //拼凑html显示
    public void loadHtmlStrData(String htmlString) {
        String resultString = "<!DOCTYPE html><html><meta name='viewport' content='width=device-width, initial-scale=1' /><head><style>img{width:95%; height:auto;}</style></head><body>"
                + htmlString + "</body></html>";
         loadDataWithBaseURL(null, resultString, "text/html","utf-8", null);
    }

}