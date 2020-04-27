package com.commodity.yzrsc.ui.activity.personalcenter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.model.SeeWuliuEntity;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.widget.webview.AdvancedWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 查看物流
 * Created by yangxuqiang on 2017/4/3.
 */

public class SeeWuliu extends BaseActivity implements AdvancedWebView.Listener {
    @Bind(R.id.head_title)
    TextView title;
//    @Bind(R.id.see_listview)
//    ListView listView;
    @Bind(R.id.express_webview)
    AdvancedWebView mWebView;
    private static final String URL = "https://m.kuaidi100.com/";

    private List<SeeWuliuEntity> data=new ArrayList<>();
    private String orderId;
    private String url="";

    @Override
    protected int getContentView() {
        return R.layout.activity_seewuliu;
    }

    @Override
    protected void initView() {
        title.setText("查看物流");
        orderId = getIntent().getExtras().getString("orderId");
        if (getIntent().getExtras()!=null&&getIntent().getExtras().containsKey("sourceType")){
            if (getIntent().getExtras().getString("sourceType").equals("xiaoshou")){
                url= IRequestConst.RequestMethod.GetOrderLogisticInfo_SellerOrder;
            }else {
                url= IRequestConst.RequestMethod.GetOrderLogisticInfo_MemberOrder;
            }
        }else {
            url= IRequestConst.RequestMethod.GetOrderLogisticInfo_MemberOrder;
        }
//        for (int i = 0; i < 10; i++) {
//            data.add(new SeeWuliuEntity());
//        }
//        SeeWuliuAdapter seeWuliuAdapter = new SeeWuliuAdapter(mContext, data, R.layout.item_wuliu);
//        listView.setAdapter(seeWuliuAdapter);
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
//        mWebView.loadUrl(URL);
        sendRequest(0,"");
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_back://返回
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {

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

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag);
        customLoadding.show();
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",orderId);
            new HttpManager(tag, HttpMothed.GET,url,map,this).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){
                try {
                    JSONObject data = response.getJSONObject("data");
                    String logisticsCode = data.optString("logisticsCode");
                    String logisticsSN = data.optString("logisticsSN");
                    mWebView.loadUrl("https://m.kuaidi100.com/index_all.html?type="+logisticsCode+"&postid="+logisticsSN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            tip(response.optString("msg"));
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
}
