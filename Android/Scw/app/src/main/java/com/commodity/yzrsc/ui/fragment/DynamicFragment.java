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
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.ui.BaseFragment;
import com.commodity.yzrsc.ui.adapter.TypeAdapter;
import com.commodity.yzrsc.ui.widget.textview.CenterDrawableTextView;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;
import com.commodity.yzrsc.utils.GsonUtils;

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
    TypeAdapter adapter;
    List<DynamicAllListModel> listModels = new ArrayList<>();
    @Bind(R.id.tv_nodata)
    CenterDrawableTextView tvNodata;
    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    public DynamicFragment() {
        // Required empty public constructor
    }

    private int pageIndex = 1;
    private int totalPage = 1;

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
//        adapter=new TypeModel(getActivity(),listModels);
//        xlistDynamic.setAdapter(adapter);
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
                        sendRequest(1, "");
                    }
                }, SPKeyManager.delay_time);
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId", "1");
            parmMap.put("catalogId", "1");
            parmMap.put("minId", "memberId");
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetEvaluationNotificationList, parmMap, this);
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
                totalPage = resultJson.optJSONObject("pageInfo").optInt("totalPage");
                JSONArray dataArray = resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            DynamicAllListModel data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), DynamicAllListModel.class);
                            listModels.add(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (pageIndex >= totalPage) {
                        xlistDynamic.setPullLoadEnable(false);
                    } else {
                        xlistDynamic.setPullLoadEnable(true);
                    }
                }
                adapter.notifyDataSetChanged();

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
}
