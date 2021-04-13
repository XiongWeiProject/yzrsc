package com.commodity.yzrsc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.manager.SPManager;
import com.commodity.yzrsc.model.Banner;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseFragment;
import com.commodity.yzrsc.ui.activity.commodity.CommodityDetailActivity;
import com.commodity.yzrsc.ui.activity.general.GeneralWebViewActivity;
import com.commodity.yzrsc.ui.adapter.DynamicAdapter;
import com.commodity.yzrsc.ui.adapter.DynamicListAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.widget.customview.PageFlipperLayout;
import com.commodity.yzrsc.ui.widget.textview.CenterDrawableTextView;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;
import com.commodity.yzrsc.utils.GsonUtils;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DynamicFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.xlist_dynamic)
    XListView xlistDynamic;
    @Bind(R.id.tv_nodata)
    CenterDrawableTextView tvNodata;
    RecyclerView recyclerView;

    PageFlipperLayout pageFlipperLayout;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    List<DynamicAllListModel> listModels = new ArrayList<>();
    DynamicAllListModel data;

    public DynamicFragment() {
        // Required empty public constructor
    }

    private int pageIndex = 1;
    private int totalPage = 1;

    private String memberId = "0";
    private String minId = "0";//页码的最小id

    List<Banner> banners = new ArrayList<>();

    DynamicAdapter dynamicAdapter ;
    DynamicListAdapter dynamicListAdapter;

    public static DynamicFragment newInstance(int param1, String param2) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initView() {
        View view_header = View.inflate(mContext, R.layout.view_header_market, null);
        xlistDynamic.addHeaderView(view_header);
        xlistDynamic.setPullLoadEnable(true);
        xlistDynamic.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
        pageFlipperLayout = (PageFlipperLayout) view_header.findViewById(R.id.slideshowView);
        pageFlipperLayout.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view_header.findViewById(R.id.cusom_swipe_view);
        xlistDynamic.setPullLoadEnable(true);
        dynamicListAdapter = new DynamicListAdapter(getActivity(), listModels);
        xlistDynamic.setAdapter(dynamicListAdapter);

        sendRequest(1, "");

    }

    @Override
    protected void initListeners() {
        xlistDynamic.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        minId = "0";
                        sendRequest(1, "");
                        xlistDynamic.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
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
                        minId = listModels.get(listModels.size() - 1).getId() + "";
                        sendRequest(1, "");

                    }
                }, SPKeyManager.delay_time);
            }
        });
    }

    //初始化banner数据
    private void initBannar(final List<Banner> bannerList) {
        if (bannerList != null && bannerList.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            dynamicAdapter = new DynamicAdapter(getActivity(),bannerList);
            recyclerView.setAdapter(dynamicAdapter);
            dynamicAdapter.setOnItemClickListener(new BaseRecycleAdapter.ItemClickListener() {
                @Override
                public void itemClick(View v, int position) {
                    Intent intent=new Intent(getActivity(), GeneralWebViewActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("title","");//http://www.soucangwang.com:8090/web/StaticPage/GuideDetail?id=
                    if (bannerList.get(position).getLinkType().contains("消息")){
                        bundle.putString("content_url","http://www.soucangwang.com:8090/web/StaticPage/GuideDetail?id="+bannerList.get(position).getLink());
                        intent.putExtras(bundle);
                        if (!bannerList.get(position).getLink().isEmpty()){
                            getActivity().startActivity(intent);
                        }
                    }else if (bannerList.get(position).getLinkType().contains("商品")){
                        Intent intent1 = new Intent(getActivity(), CommodityDetailActivity.class);
                        Bundle bundle1= new Bundle();
                        bundle1.putString("goodsSaleId",bannerList.get(position).getLink());
                        intent1.putExtras(bundle1);
                        getActivity().startActivity(intent1);
                    }else {
                        bundle.putString("content_url",bannerList.get(position).getLink());
                        intent.putExtras(bundle);
                        if (!bannerList.get(position).getLink().isEmpty()){
                            getActivity().startActivity(intent);
                        }
                    }
                }
            });

        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    //获取传递过来信息进行相关操作
    @Subscribe
    public void NotifyChangedView(Event.NotifyChangedView event) {
        if (event.getDataObject().equals("DynamicFragment")) {
            sendRequest(1, "");
//            sendRequest(2, "");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            customLoadding.show();
            if (pageIndex == 1) {
                minId = "0";
            }
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("likeCount", 6 + "");
            parmMap.put("commentCount", 6 + "");
            parmMap.put("memberId", memberId);
            parmMap.put("catalogId", mParam1 + "");
            parmMap.put("minId", minId);
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
            parmMap.put("scw-token", ConfigManager.instance().getUser().getDeviceToken());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetDynamicList, parmMap, this);
            httpManager.request();
        } else if (tag == 2) {//获取广告数据 广告对象:引导页=1,加载页=2,首页轮播=3,商品分类页=4 ,
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("adverSource", 5 + "");
            parmMap.put("num", 10 + "");
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetAdvers, parmMap, this);
            httpManager.request();

        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                if (pageIndex == 1) {
                    listModels.clear();
                }
//                totalPage = resultJson.optJSONObject("pageInfo").optInt("totalPage");
                JSONArray dataArray = resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), DynamicAllListModel.class);
                            if (data != null) {
                                listModels.add(data);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (listModels.size() < SPKeyManager.pageSize) {
                        xlistDynamic.setPullLoadEnable(false);
                    } else {
                        xlistDynamic.setPullLoadEnable(true);
                    }
                }
                dynamicListAdapter.notifyDataSetChanged();

                if (listModels.size() > 0) {
                    tvNodata.setVisibility(View.GONE);
                } else {
                    tvNodata.setVisibility(View.VISIBLE);
                }

            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
            xlistDynamic.stopLoadMore();
            xlistDynamic.stopRefresh();
            sendRequest(2, "");
        } else if (tag == 2) {
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

                } else {
                    tvTips.setVisibility(View.GONE);
                }
                initBannar(banners);
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                    tvTips.setVisibility(View.GONE);
                }
            }
        }


    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
