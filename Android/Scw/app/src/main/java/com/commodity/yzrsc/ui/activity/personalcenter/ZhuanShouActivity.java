package com.commodity.yzrsc.ui.activity.personalcenter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.SellerZhuanshouEntity;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.personalcenter.orde.ZhuanShouMDetailActivity;
import com.commodity.yzrsc.ui.adapter.ZhuanShouManagerAdapter;
import com.commodity.yzrsc.ui.widget.layout.StoreTable2;
import com.commodity.yzrsc.ui.widget.xlistView.PLA_AdapterView;
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
import butterknife.OnClick;

/**
 * 转售订单管理
 * Created by yangxuqiang on 2017/3/28.
 */

public class ZhuanShouActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.zhuanshou_guid)
    LinearLayout zhuanshou_guid;
    @Bind(R.id.zhuanshou_listview)
    XListView zhuanshou_listview;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    private List<SellerZhuanshouEntity> data=new ArrayList<>();
    private ZhuanShouManagerAdapter zhuanShouManagerAdapter;
    private int pageIndex = 1;
    private int totalPage = 1;
    private int orderState;

    @Override
    protected int getContentView() {
        return R.layout.actibity_zhuanshou;
    }

    @Override
    protected void initView() {
        title.setText("转售订单");

        zhuanshou_guid.addView(addMenu("全部",true,0));
        zhuanshou_guid.addView(addMenu("待付款",true,1));
        zhuanshou_guid.addView(addMenu("待发货",true,2));
        zhuanshou_guid.addView(addMenu("已发货",true,3));
        zhuanshou_guid.addView(addMenu("交易完成",true,4));
        zhuanshou_guid.addView(addMenu("退款",true,5));
        zhuanshou_guid.addView(addMenu("交易关闭",true,6));

        zhuanShouManagerAdapter = new ZhuanShouManagerAdapter(mContext, data, R.layout.item_zhuanshoumanager);
        zhuanshou_listview.setAdapter(zhuanShouManagerAdapter);

        zhuanshou_listview.setPullRefreshEnable(true);
        zhuanshou_listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        sendRequest(0, "");
                        zhuanshou_listview.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
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
                        sendRequest(0,"");
                    }
                },SPKeyManager.delay_time);
            }
        });
        doOnclickItem(0);
    }

    @Override
    protected void initListeners() {
        zhuanshou_listview.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderId",String.valueOf(data.get(position-1).getId()));
                jumpActivity(ZhuanShouMDetailActivity.class,bundle);
            }
        });
    }
    private View addMenu(String text, boolean invisableV, final int index) {
        StoreTable2 storeTable = new StoreTable2(mContext, text);
        storeTable.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        storeTable.invisibleV();
        storeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOnclickItem(index);
            }
        });

        return storeTable;
    }
    private void doOnclickItem(int index) {
        refreshTable(index);
        //TODO
        data.clear();
        orderState=index;
        pageIndex=1;
        sendRequest(0,"");
    }
    private void refreshTable(int index){
        for(int i=0;i<7;i++){
            ((StoreTable2)(zhuanshou_guid.getChildAt(i))).select(i==index);
        }
    }
    @OnClick({R.id.head_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back://返回
                finish();
                break;
        }
    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        switch (tag){
            case 0://订单
                Map<String, String> ordeMap = new HashMap<>();
                ordeMap.put("pageIndex",pageIndex+"");
                ordeMap.put("pageSize",""+ SPKeyManager.pageSize);
                ordeMap.put("sortOrder","1");
                ordeMap.put("soldOrderState",String.valueOf(orderState));
                new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetResellOrderList,ordeMap,this ).request();
                break;
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==0) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                if (pageIndex == 1) {
                    data.clear();
                }
                totalPage = resultJson.optJSONObject("pageInfo").optInt("totalPage");
                JSONArray dataArray = resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            SellerZhuanshouEntity sellerZhuanshouEntity = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), SellerZhuanshouEntity.class);
                            data.add(sellerZhuanshouEntity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    zhuanShouManagerAdapter.notifyDataSetChanged();
                    if (pageIndex >= totalPage) {
                        zhuanshou_listview.setPullLoadEnable(false);
                    } else {
                        zhuanshou_listview.setPullLoadEnable(true);
                    }
                    if (data.size() > 0) {
                        tv_nodata.setVisibility(View.GONE);
                    } else {
                        tv_nodata.setVisibility(View.VISIBLE);
                    }

                } else {
                    if (resultJson != null && !resultJson.optBoolean("success")) {
                        tip(resultJson.optString("msg"));
                    }
                }
                zhuanshou_listview.stopLoadMore();
                zhuanshou_listview.stopRefresh();
            }
        }
    }


    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
