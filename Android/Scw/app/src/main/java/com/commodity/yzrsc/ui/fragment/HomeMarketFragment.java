package com.commodity.yzrsc.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.model.Banner;
import com.commodity.yzrsc.model.HomeTypeModel;
import com.commodity.yzrsc.model.ImgType;
import com.commodity.yzrsc.model.MarketGoods;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseFragment;
import com.commodity.yzrsc.ui.activity.classify.TypeActivity;
import com.commodity.yzrsc.ui.activity.user.MessageManagerActivity;
import com.commodity.yzrsc.ui.adapter.HomeTypeAdapter;
import com.commodity.yzrsc.ui.adapter.MarketGoodsAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.dialog.BankDialog;
import com.commodity.yzrsc.ui.dialog.SearchDialog;
import com.commodity.yzrsc.ui.widget.customview.PageFlipperLayout;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;
import com.commodity.yzrsc.utils.DateUtil;
import com.commodity.yzrsc.utils.GsonUtils;
import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.squareup.otto.Subscribe;

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

/**
 * 作者：liyushen on 2017/03/13 16:30
 * 功能：逛市场
 */
public class HomeMarketFragment extends BaseFragment {
    @Bind(R.id.xlistView)
    XListView xlistView;
    @Bind(R.id.ll_search)
    LinearLayout ll_search;
    @Bind(R.id.tv_tips)
    TextView tv_tips;
    @Bind(R.id.iv_tip_btn)
    ImageView iv_tip_btn;

    private int pageIndex = 1;
    private int totalPage = 1;

    MarketGoodsAdapter marketGoodsAdapter;
    List<MarketGoods> marketGoodsList;
    SearchDialog searchDialog;
    private String keywords = "";

    PageFlipperLayout pageFlipperLayout;
    RecyclerView recyclerView;
    List<Banner> banners = new ArrayList<>();
    private HomeTypeAdapter  homeTypeAdapter;
    private List<HomeTypeModel> homeTypeModels = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_market;
    }

    @Override
    protected void initView() {
        View view_header = View.inflate(mContext, R.layout.view_header_market, null);
        xlistView.addHeaderView(view_header);
        xlistView.setPullLoadEnable(true);
        xlistView.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
        pageFlipperLayout = (PageFlipperLayout) view_header.findViewById(R.id.slideshowView);
        recyclerView = (RecyclerView) view_header.findViewById(R.id.cusom_swipe_view);
//取控件当前的布局参数
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
//设置宽度值
//设置高度值
        params.height = 500;
//使设置好的布局参数应用到控件
        recyclerView.setLayoutParams(params);
        initRecView();

        marketGoodsList = new ArrayList<>();
        marketGoodsAdapter = new MarketGoodsAdapter(getActivity(), marketGoodsList, R.layout.item_market_goods);
        xlistView.setAdapter(marketGoodsAdapter);
        initCacheData();
        sendRequest(1, "");
        sendRequest(2, "");
        sendRequest(3, "");
        sendRequest(4, "");
        marketGoodsAdapter.notifyDataSetChanged();
        searchDialog = new SearchDialog(getActivity(), marketGoodsList, marketGoodsAdapter);
        SPKeyManager.messageTipTextViewList.clear();
        SPKeyManager.messageTipTextViewList.add(tv_tips);
    }

    private void initRecView() {
// 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(
                2, 5, PagerGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

// 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(recyclerView);
        homeTypeAdapter = new HomeTypeAdapter(getActivity(),homeTypeModels,this);
        recyclerView.setAdapter(homeTypeAdapter);

    }

    @Override
    protected void initListeners() {
        xlistView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        sendRequest(1, "");
                        sendRequest(3, "");
                        xlistView.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
                    }
                }, SPKeyManager.delay_time);
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
                }, SPKeyManager.delay_time);
            }
        });
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDialog.show();
            }
        });

        searchDialog.setOnSearchListener(new SearchDialog.OnSearchListener() {
            @Override
            public void doSearch(String search) {
                keywords = search;
                sendRequest(1, "");
            }
        });
        searchDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pageIndex = 1;
                sendRequest(1, "");
                xlistView.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
            }
        });
        iv_tip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(MessageManagerActivity.class);
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            Map<String, String> parmMap = new HashMap<String, String>();
            if (searchDialog != null && searchDialog.isShowing()) {
                parmMap.put("keywords", keywords);
                parmMap.put("sortOrder", "1");
                parmMap.put("pageIndex", searchDialog.getPage() + "");
                parmMap.put("pageSize", "" + SPKeyManager.pageSize);
                HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                        IRequestConst.RequestMethod.SearchGoodsSale, parmMap, this);
                httpManager.request();
            } else {
                customLoadding.show();
                parmMap.put("kindId", "0");
                parmMap.put("priceRange", "0");
                parmMap.put("sortOrder", "1");
                parmMap.put("pageIndex", pageIndex + "");
                parmMap.put("pageSize", "" + SPKeyManager.pageSize);
                HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                        IRequestConst.RequestMethod.GetMainGoodsSaleList, parmMap, this);
                httpManager.request();
            }
        } else if (tag == 2) {//未读通知数量
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetUnreadNotificationCounts, parmMap, this);
            httpManager.request();

        } else if (tag == 3) {//获取广告数据 广告对象:引导页=1,加载页=2,首页轮播=3,商品分类页=4 ,
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("adverSource", 3 + "");
            parmMap.put("num", 10 + "");
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetAdvers, parmMap, this);
            httpManager.request();

        } else if (tag == 4) {//获取广告数据 广告对象:引导页=1,加载页=2,首页轮播=3,商品分类页=4 ,
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GethomeType, parmMap, this);
            httpManager.request();

        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                if (searchDialog != null && searchDialog.isShowing()) {//当处于搜索时
                    if (searchDialog.getPage() == 1) {
                        marketGoodsList.clear();
                    }
                    searchDialog.setTotalPage(resultJson.optJSONObject("pageInfo").optInt("totalPage"));
                    JSONArray dataArray = resultJson.optJSONArray("data");
                    if (dataArray != null) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            MarketGoods marketGoods = new MarketGoods();
                            try {
                                initResultData(marketGoodsList, marketGoods, dataArray.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        marketGoodsAdapter.notifyDataSetChanged();
                        if (searchDialog.getPage() >= searchDialog.getTotalPage()) {
                            searchDialog.getListView().setPullLoadEnable(false);
                        } else {
                            searchDialog.getListView().setPullLoadEnable(true);
                        }
                        if (marketGoodsList.size() == 0) {
                            searchDialog.showNodata("暂无搜索结果");
                        } else {
                            searchDialog.showResultList();
                        }
                    }
                    searchDialog.getListView().stopLoadMore();
                    searchDialog.getListView().stopRefresh();
                } else {//当不处于搜索时
                    if (pageIndex == 1) {
                        marketGoodsList.clear();
                        SPManager.instance().setString(SPKeyManager.GetGoodsSaleListCacheStr, resultJson.toString());
                    }
                    totalPage = resultJson.optJSONObject("pageInfo").optInt("totalPage");
                    JSONArray dataArray = resultJson.optJSONArray("data");
                    if (dataArray != null) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            MarketGoods marketGoods = new MarketGoods();
                            try {
                                initResultData(marketGoodsList, marketGoods, dataArray.getJSONObject(i));
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
                    }
                    xlistView.stopLoadMore();
                    xlistView.stopRefresh();
                }
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        } else if (tag == 2) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                JSONObject dataJson = resultJson.optJSONObject("data");
                int count = dataJson.optInt("systemCount") + dataJson.optInt("evaluationCount") + dataJson.optInt("followCount");
                if (count > 0) {
                    tv_tips.setText(count + "");
                    tv_tips.setVisibility(View.VISIBLE);
                } else {
                    tv_tips.setVisibility(View.GONE);
                }

            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                    tv_tips.setVisibility(View.GONE);
                }
            }
        } else if (tag == 3) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            SPManager.instance().setString(SPKeyManager.GetHomeBanerAdversCacheStr, resultJson.toString());
            if (resultJson != null && resultJson.optBoolean("success")) {
                JSONArray dataArray = resultJson.optJSONArray("data");
                banners.clear();
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            Banner data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), Banner.class);
                            banners.add(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                initBannar(banners);
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                    tv_tips.setVisibility(View.GONE);
                }
            }
        }else if (tag == 4) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            SPManager.instance().setString(SPKeyManager.GetHomeBanerAdversCacheStr, resultJson.toString());
            if (resultJson != null && resultJson.optBoolean("success")) {
                JSONArray dataArray = resultJson.optJSONArray("data");
                homeTypeModels.clear();
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            HomeTypeModel data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), HomeTypeModel.class);
                            homeTypeModels.add(data);
                            homeTypeAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
//                initBannar(banners);
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                    tv_tips.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    //首页加载缓存
    private void initCacheData() {
        //banner
        try {
            JSONObject resultJson = new JSONObject(SPManager.instance().getString(SPKeyManager.GetHomeBanerAdversCacheStr));
            if (resultJson != null && resultJson.optBoolean("success")) {
                JSONArray dataArray = resultJson.optJSONArray("data");
                banners.clear();
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            Banner data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), Banner.class);
                            banners.add(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            initBannar(banners);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //列表
        try {
            JSONObject resultJson = new JSONObject(SPManager.instance().getString(SPKeyManager.GetGoodsSaleListCacheStr));
            if (resultJson != null && resultJson.optBoolean("success")) {
                JSONArray dataArray = resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        MarketGoods marketGoods = new MarketGoods();
                        try {
                            initResultData(marketGoodsList, marketGoods, dataArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    marketGoodsAdapter.notifyDataSetChanged();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //初始化banner数据
    private void initBannar(List<Banner> bannerList) {
        if (bannerList != null && bannerList.size() > 0) {
            pageFlipperLayout.setBannerList(bannerList);
            pageFlipperLayout.setVisibility(View.VISIBLE);
        } else {
            pageFlipperLayout.setBannerList(bannerList);
            pageFlipperLayout.setVisibility(View.VISIBLE);
        }
    }

    //绑数据
    private void initResultData(List<MarketGoods> marketGoodsList, MarketGoods marketGoods, JSONObject jsonObject) {
        marketGoods.setId(jsonObject.optString("id"));
        marketGoods.setDescribe(jsonObject.optString("description"));
        marketGoods.setPrice(new BigDecimal(new java.text.DecimalFormat("0.00").format(jsonObject.optDouble("price"))) + "");
        marketGoods.setSuggestedPrice(new BigDecimal(new java.text.DecimalFormat("0.00").format(jsonObject.optDouble("suggestedPrice"))) + "");
        marketGoods.setVideo(jsonObject.optString("video"));
        marketGoods.setVideoSnap(jsonObject.optString("videoSnap"));
        marketGoods.setReselled(jsonObject.optBoolean("isReselled"));
        marketGoods.setShopId(jsonObject.optString("shopId"));
        JSONArray images = jsonObject.optJSONArray("images");
        if (images != null && images.length() > 0) {
            int count = images.length();
            if (!marketGoods.getVideo().isEmpty()) {
                count = count + 1;
            }
            ImgType[] types = new ImgType[count];
            if (marketGoods.getVideo().isEmpty()) {//如果没有视频
                for (int j = 0; j < count; j++) {
                    ImgType imgType = new ImgType();
                    try {
                        imgType.setImgpath(images.getString(j));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    types[j] = imgType;
                }
            } else {
                for (int j = 0; j < count; j++) {//如果有视频
                    ImgType imgType = new ImgType();
                    if (j == 0) {
                        imgType.setVideo(true);
                        imgType.setVideopath(marketGoods.getVideo());
                        imgType.setImgpath(marketGoods.getVideoSnap());
                        types[j] = imgType;
                    } else {
                        try {
                            imgType.setImgpath(images.getString(j - 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        types[j] = imgType;
                    }
                }
            }
            marketGoods.setImgTypes(types);
        }
        String ddd = DateUtil.getBetweenTime(jsonObject.optString("updateTime"), "");
        marketGoods.setDateTime(ddd);
        marketGoods.setFavorite(jsonObject.optBoolean("isFavored"));
        marketGoodsList.add(marketGoods);
    }

    //获取传递过来信息进行相关操作
    @Subscribe
    public void NotifyChangedView(Event.NotifyChangedView event) {
        if (event.getDataObject().equals("HomeMarketFragment")) {
            pageIndex = 1;
            sendRequest(1, "");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }

    /**
     * 写入历史记录
     */
    public void writeInsp() {
        if (searchDialog != null && searchDialog.isShowing()) {
            searchDialog.writeInSP(searchDialog.getSearchString());
        }
    }
}