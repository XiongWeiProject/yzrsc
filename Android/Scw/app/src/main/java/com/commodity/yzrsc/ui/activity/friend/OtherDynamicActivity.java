package com.commodity.yzrsc.ui.activity.friend;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.adapter.DynamicListAdapter;
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
import butterknife.OnClick;

public class OtherDynamicActivity extends BaseActivity {


    @Bind(R.id.tv_nodata)
    CenterDrawableTextView tvNodata;
    @Bind(R.id.xlist_dynamic)
    XListView xlistDynamic;
    @Bind(R.id.head_back)
    ImageView headBack;

    List<DynamicAllListModel> listModels = new ArrayList<>();
    DynamicAllListModel data;
    DynamicListAdapter dynamicListAdapter;

    private int pageIndex = 1;
    private int totalPage = 1;
    private String memberId = "0";
    private String minId = "0";//页码的最小id

    @Override
    protected int getContentView() {
        return R.layout.activity_my_dynamic;
    }

    @Override
    protected void initView() {
        Bundle extras = getIntent().getExtras();
        memberId = extras.getString("dynamicId");
        xlistDynamic.setPullLoadEnable(true);
        dynamicListAdapter = new DynamicListAdapter(this, listModels);
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

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId", memberId);
            parmMap.put("catalogId", "");
            parmMap.put("minId", minId);
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
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
                            listModels.add(data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (listModels.size() == SPKeyManager.delay_time) {
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
