package com.commodity.scw.ui.activity.classify;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.model.ImgType;
import com.commodity.scw.model.MarketGoods;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.MarketGoodsAdapter;
import com.commodity.scw.ui.adapter.SearchHistoryAdapter;
import com.commodity.scw.ui.widget.textview.ClearSearchEditText;
import com.commodity.scw.ui.widget.xlistView.XListView;
import com.commodity.scw.utils.DateUtil;
import com.commodity.scw.utils.KeyBoardUtils;
import com.yixia.camera.util.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 328789 on 2017/5/23.
 */

public class MSerachActivity extends BaseActivity {
    private String SEARCHKET=MSerachActivity.class.getSimpleName();
    @Bind(R.id.ed_search)
    ClearSearchEditText ed_search;
    @Bind(R.id.lv_result)
    XListView xlistView;
    @Bind(R.id.lv_history)
    ListView lv_history;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    List<MarketGoods> marketGoodsList=new ArrayList<>();
    private MarketGoodsAdapter marketGoodsAdapter;
    private int pageIndex=1;
    private int totalPage;
    private String keywords="";
    SearchHistoryAdapter historyAdapter;
    List<String> historylist = new ArrayList<>();//历史记录列表
    private String url;
    private String shopId;

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void initView() {
        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("a")){
            int a = extras.getInt("a");
            switch (a){
                case 0:
                    url = IRequestConst.RequestMethod.SearchGoodsSale;
                    break;
                case 1://店铺
                    url =IRequestConst.RequestMethod.mSearchGoodsSale;
                    break;
            }
        }
        if(extras.containsKey("shopId")){
            shopId = extras.getString("shopId");
        }
        historyAdapter = new SearchHistoryAdapter(this, historylist, R.layout.item_history);
        lv_history.setAdapter(historyAdapter);
        marketGoodsAdapter=new MarketGoodsAdapter(this,marketGoodsList,R.layout.item_market_goods);
        xlistView.setAdapter(marketGoodsAdapter);
        showHistroyList();
    }

    @Override
    protected void initListeners() {

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    keywords=editable.toString();
                    pageIndex = 1;
                    sendRequest(1, "");
                }else {
                    showHistroyList();
                }
            }
        });

        xlistView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        sendRequest(1, "");
                        xlistView.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
                    }
                },SPKeyManager.delay_time);
            }

            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex++;
                        sendRequest(1, "");
                    }
                },SPKeyManager.delay_time);
            }
        });

        historyAdapter.setOnHistorySearchListener(new SearchHistoryAdapter.OnHistorySearchListener() {
            @Override
            public void OnClearBt() {
                SPManager.instance().setString(SEARCHKET, "");
                getHistorySearchList();
                historyAdapter.notifyDataSetChanged();
                showNodata("暂无历史搜索记录");
            }

            @Override
            public void OnItemClick(String search) {
                ed_search.setText(search);
                ed_search.setSelection(search.length());
            }
        });
    }
    @OnClick({R.id.tv_cancel})
    public void click(View v){
        switch (v.getId()){
            case R.id.tv_cancel:
                KeyBoardUtils.hindKeyBord(ed_search);
                finish();
                break;
        }
    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("keywords",keywords);
            parmMap.put("sortOrder","1");
            parmMap.put("pageIndex",pageIndex+"");
            parmMap.put("pageSize",""+ SPKeyManager.pageSize);
            if(StringUtils.isNotEmpty(shopId)){
                parmMap.put("token", ConfigManager.instance().getUser().getImToken());
            }
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,url
                    , parmMap, this);
            httpManager.request();
        }
    }
    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                if (pageIndex  == 1) {
                    marketGoodsList.clear();
                }
                totalPage=resultJson.optJSONObject("pageInfo").optInt("totalPage");
                JSONArray dataArray=resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        MarketGoods marketGoods =new MarketGoods();
                        try {
                            initResultData(marketGoodsList,marketGoods,dataArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    marketGoodsAdapter.notifyDataSetChanged();
                    if (pageIndex >= totalPage) {
                        xlistView.setPullLoadEnable(false);
                    } else {
                        xlistView.setPullLoadEnable(true);
                    }
                    if (marketGoodsList.size() == 0) {
                        showNodata("暂无搜索结果");
                    } else {
                        showResultList();
                    }
                }
                xlistView.stopLoadMore();
                xlistView.stopRefresh();
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    //绑数据
    private void initResultData(List<MarketGoods> marketGoodsList, MarketGoods marketGoods, JSONObject jsonObject) {
        marketGoods.setId(jsonObject.optString("id"));
        marketGoods.setDescribe(jsonObject.optString("description"));
        marketGoods.setPrice(new BigDecimal(new java.text.DecimalFormat("0.00").format(jsonObject.optDouble("price")))+"");
        marketGoods.setSuggestedPrice(new BigDecimal(new java.text.DecimalFormat("0.00").format(jsonObject.optDouble("suggestedPrice")))+"" );
        marketGoods.setVideo(jsonObject.optString("video"));
        marketGoods.setVideoSnap(jsonObject.optString("videoSnap"));
        marketGoods.setReselled(jsonObject.optBoolean("isReselled"));
        marketGoods.setShopId(jsonObject.optString("shopId"));
        JSONArray images=jsonObject.optJSONArray("images");
        if (images!=null&&images.length()>0){
            int count=images.length();
            if (!marketGoods.getVideo().isEmpty()){
                count=count+1;
            }
            ImgType[] types=new ImgType[count];
            if (marketGoods.getVideo().isEmpty()){//如果没有视频
                for (int j = 0; j < count; j++) {
                    ImgType imgType=new ImgType();
                    try {
                        imgType.setImgpath(images.getString(j));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    types[j]=imgType;
                }
            }else {
                for (int j = 0; j < count; j++) {//如果有视频
                    ImgType imgType=new ImgType();
                    if (j==0){
                        imgType.setVideo(true);
                        imgType.setVideopath(marketGoods.getVideo());
                        imgType.setImgpath(marketGoods.getVideoSnap());
                        types[j]=imgType;
                    }else {
                        try {
                            imgType.setImgpath(images.getString(j-1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        types[j]=imgType;
                    }
                }
            }
            marketGoods.setImgTypes(types);
        }
//        String  ddd= DateUtil.getBetweenTime(jsonObject.optString("updateTime"),"" );
//        marketGoods.setDateTime(ddd);
        marketGoods.setFavorite(jsonObject.optBoolean("isFavored"));
        marketGoodsList.add(marketGoods);
    }

    public void showNodata(String str) {
        lv_history.setVisibility(View.GONE);
        xlistView.setVisibility(View.GONE);
        tv_nodata.setVisibility(View.VISIBLE);
        tv_nodata.setText(str);
        marketGoodsAdapter.notifyDataSetChanged();
    }

    public void showHistroyList() {
        getHistorySearchList();
        lv_history.setVisibility(View.VISIBLE);//历史搜索显示
        xlistView.setVisibility(View.GONE);//搜索结果隐藏
        tv_nodata.setVisibility(View.GONE);
        if (historylist.size() == 0) {
            showNodata("暂无历史搜索记录");
        } else {
            if (historyAdapter != null) {
                historyAdapter.notifyDataSetChanged();
            }
        }
    }

    public void showResultList() {
        lv_history.setVisibility(View.GONE);//历史搜索隐藏
        xlistView.setVisibility(View.VISIBLE);//搜索结果显示
        tv_nodata.setVisibility(View.GONE);
        if (marketGoodsList.size() == 0) {
            showNodata("暂无搜索结果");
        } else {
            marketGoodsAdapter.notifyDataSetChanged();
        }
    }

    /*
  * 获取缓存历史记录
  */
    private void getHistorySearchList() {
        String sp = SPManager.instance().getString(SEARCHKET);
        Log.e("sp", sp);
        if (sp == null || sp.equals("")) {
            historylist.clear();

        } else {
            if (!sp.contains("/")) {
                historylist.clear();
                historylist.add(sp);
                return;
            }
            String[] arr = sp.split("/");
            historylist.clear();
            for (int i = arr.length - 1; i >= 0; i--) {
                historylist.add(arr[i]);//将缓存中的历史记录倒叙加载
            }
        }
    }
}
