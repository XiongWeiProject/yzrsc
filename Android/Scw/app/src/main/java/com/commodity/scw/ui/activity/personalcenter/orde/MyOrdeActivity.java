package com.commodity.scw.ui.activity.personalcenter.orde;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.mine.DetailMyOrdeEntity;
import com.commodity.scw.model.mine.MyOrdeEntity;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.MyordeAdapter;
import com.commodity.scw.ui.widget.layout.StoreTable2;
import com.commodity.scw.ui.widget.xlistView.XListView;
import com.commodity.scw.utils.GsonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 * 我的订单
 * Created by yangxuqiang on 2017/4/3.
 */

public class MyOrdeActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.zhuanshou_guid)
    LinearLayout zhuanshou_guid;
    @Bind(R.id.zhuanshou_listview)
    XListView zhuanshou_listview;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;

    private List<DetailMyOrdeEntity> data=new ArrayList<>();
    private MyordeAdapter myordeAdapter;
    private int orderState=0;
    private int pageIndex = 1;
    private int totalPage = 1;
    private int flag=0;
    private String state;

    @Override
    protected int getContentView() {
        return R.layout.activity_myorde;
    }

    @Override
    protected void initView() {
        title.setText("我的订单");

        zhuanshou_guid.addView(addMenu("全部",true,0));
        zhuanshou_guid.addView(addMenu("待付款",true,1));
        zhuanshou_guid.addView(addMenu("待发货",true,2));
        zhuanshou_guid.addView(addMenu("待收货",true,3));
        zhuanshou_guid.addView(addMenu("交易完成",true,4));
        zhuanshou_guid.addView(addMenu("退款",true,5));
        zhuanshou_guid.addView(addMenu("交易关闭",true,6));

        myordeAdapter = new MyordeAdapter(this, data, R.layout.item_myorde);
        zhuanshou_listview.setAdapter(myordeAdapter);

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
        myordeAdapter.setOnitemListener(new MyordeAdapter.OnitemListener() {
            @Override
            public void itemClick(View v, int position) {
                flag=position;
                SPKeyManager.curDetailMyOrdeEntity=data.get(position);
                state = SPKeyManager.curDetailMyOrdeEntity.getState();
                Bundle bundle = new Bundle();
                bundle.putString("orderId",String.valueOf(data.get(position).getId()));
                bundle.putString("activity","myorde");
                switch (data.get(position).getState()){
                    case "订单已提交"://待付款
                        bundle.putString("flag","tijiao");
                        jumpActivity(DaiPayActivity.class,bundle);
                        break;
                    case "买家已付款,等待卖家发货"://待发货
                        jumpActivity(DaiSendActivity.class,bundle);
                        break;
                    case "卖家已发货"://已发货
                        jumpActivity(YiSendActivity.class,bundle);
                        break;
                    case "买家已收货,交易成功"://交易成功
                        jumpActivity(JiaoYiSuccessActivity.class,bundle);
                        break;
                    case "仲裁中"://退货
                    case "买家已申请退款":
                    case "卖家拒绝退款":
                        jumpActivity(BackGoodsActivity.class,bundle);
                        break;
                    case "订单已取消"://交易关闭
                    case "订单已关闭":
                    case "已退款":
                        bundle.putString("flag","close");
                        jumpActivity(sellerTijiaoActivity.class,bundle);
                        break;
                }
            }
        });
    }
    private View addMenu(String text, boolean invisableV, final int index) {
        StoreTable2 storeTable = new StoreTable2(mContext, text);
//        ScreenUtils.getScreenWidth(mContext) / 6
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
    }
    private void refreshTable(int index){
        for(int i=0;i<7;i++){
            ((StoreTable2)(zhuanshou_guid.getChildAt(i))).select(i==index);
        }
        pageIndex=1;
        orderState = index;
        sendRequest(0,"");
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
                ordeMap.put("orderState",String.valueOf(orderState));
                new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.BuyGetOrderList,ordeMap,this ).request();
                break;
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==0){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                if (pageIndex == 1) {
                    data.clear();
                }
                totalPage=resultJson.optJSONObject("pageInfo").optInt("totalPage");
                MyOrdeEntity myOrdeEntity = GsonUtils.jsonToObject(resultJson.toString(), MyOrdeEntity.class);
                data.addAll(myOrdeEntity.getData());
                myordeAdapter.notifyDataSetChanged();
                if (pageIndex >= totalPage) {
                    zhuanshou_listview.setPullLoadEnable(false);
                } else {
                    zhuanshou_listview.setPullLoadEnable(true);
                }
                if (data.size()>0){
                    tv_nodata.setVisibility(View.GONE);
                }else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }

            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
            zhuanshou_listview.stopLoadMore();
            zhuanshou_listview.stopRefresh();
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
//        data.clear();
//        pageIndex=1;
//        zhuanshou_listview.setSelection(flag);
//        sendRequest(0,"");

//        if (SPKeyManager.curDetailMyOrdeEntity!=null){
//            SPKeyManager.curDetailMyOrdeEntity.setState("xvsssdfsdf");
//        }
        if(!StringUtil.isEmpty(state)&&!state.equals(SPKeyManager.curDetailMyOrdeEntity.getState())){
            switch (SPKeyManager.curDetailMyOrdeEntity.getState()){
                case "订单已提交"://待付款
                    doOnclickItem(1);
                    break;
                case "买家已付款,等待卖家发货"://待发货
                    doOnclickItem(2);
                    break;
                case "卖家已发货"://已发货
                    doOnclickItem(3);
                    break;
                case "买家已收货,交易成功"://交易成功
                    doOnclickItem(4);
                    break;
                case "仲裁中"://退货
                case "买家已申请退款":
                case "卖家拒绝退款":
                    doOnclickItem(5);
                    break;
                case "订单已取消"://交易关闭
                case "订单已关闭":
                case "已退款":
                    doOnclickItem(6);
                    break;
            }
        }
    }
}
