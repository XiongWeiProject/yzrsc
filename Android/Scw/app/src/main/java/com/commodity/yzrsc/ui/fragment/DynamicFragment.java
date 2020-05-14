package com.commodity.yzrsc.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseFragment;
import com.commodity.yzrsc.ui.adapter.DynamicListAdapter;
import com.commodity.yzrsc.ui.adapter.TypeAdapter;
import com.commodity.yzrsc.ui.widget.textview.CenterDrawableTextView;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;
import com.commodity.yzrsc.utils.GsonUtils;
import com.squareup.otto.Subscribe;
import com.yixia.camera.util.Log;

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
        xlistDynamic.setPullLoadEnable(true);
        dynamicListAdapter = new DynamicListAdapter(getActivity(),listModels);
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
                        minId = listModels.get(listModels.size()-1).getId()+"";
                        sendRequest(1, "");

                    }
                }, SPKeyManager.delay_time);
            }
        });
    }
    //获取传递过来信息进行相关操作
    @Subscribe
    public void NotifyChangedView(Event.NotifyChangedView event) {
        if (event.getDataObject().equals("DynamicFragment")) {
            sendRequest(1, "");
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
            if (  pageIndex ==1){
                minId = "0";
            }
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("likeCount", 6+"");
            parmMap.put("commentCount", 6+"");
            parmMap.put("memberId", memberId);
            parmMap.put("catalogId", mParam1+"");
            parmMap.put("minId", minId);
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
            parmMap.put("scw-token", ConfigManager.instance().getUser().getDeviceToken());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetDynamicList, parmMap, this);
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
                            if (data!=null){
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
}
