package com.commodity.scw.ui.activity.personalcenter;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.mine.DetailMyOrdeEntity;
import com.commodity.scw.model.mine.MyOrdeEntity;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.personalcenter.orde.BackActivity;
import com.commodity.scw.ui.activity.personalcenter.orde.BackDetailActivity;
import com.commodity.scw.ui.activity.personalcenter.orde.BackGoodsActivity;
import com.commodity.scw.ui.activity.personalcenter.orde.JiaoYiSuccessActivity;
import com.commodity.scw.ui.activity.personalcenter.orde.XiaoDaiSendActivity;
import com.commodity.scw.ui.activity.personalcenter.orde.ZhuanShouMDetailActivity;
import com.commodity.scw.ui.activity.personalcenter.orde.sellerTijiaoActivity;
import com.commodity.scw.ui.adapter.XiaoShoumenagerAdapter;
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
 * 销售管理
 * Created by yangxuqiang on 2017/3/26.
 */

public class XiaoShouActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.xiaoshou_guid)
    LinearLayout xiaoshou_guid;
    @Bind(R.id.xiaoshou_listview)
    XListView xiaoshou_listview;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    //订单状态
    private boolean isBuy=false;
    private int flag=0;

    private List<DetailMyOrdeEntity> data=new ArrayList<>();
    private XiaoShoumenagerAdapter xiaoShoumenagerAdapter;
    private int pageIndex = 1;
    private int totalPage = 1;
    private int orderState=0;
    private String state;

    @Override
    protected int getContentView() {
        return R.layout.activity_xiaoshou;
    }

    @Override
    protected void initView() {
        title.setText("销售管理");

        xiaoshou_guid.addView(addMenu("全部",true,0));
        xiaoshou_guid.addView(addMenu("待付款",true,1));
        xiaoshou_guid.addView(addMenu("待发货",true,2));
        xiaoshou_guid.addView(addMenu("已发货",true,3));
        xiaoshou_guid.addView(addMenu("交易完成",true,4));
        xiaoshou_guid.addView(addMenu("退款",true,5));
        xiaoshou_guid.addView(addMenu("交易关闭",true,6));

        xiaoShoumenagerAdapter = new XiaoShoumenagerAdapter(this, data, R.layout.item_xiaoshoumemanager);
        xiaoshou_listview.setAdapter(xiaoShoumenagerAdapter);

        xiaoshou_listview.setPullRefreshEnable(true);
        xiaoshou_listview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        sendRequest(0, "");
                        xiaoshou_listview.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
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
        xiaoShoumenagerAdapter.setOnitemListener(new XiaoShoumenagerAdapter.OnitemListener() {
            @Override
            public void itemClick(View v, int position) {
                flag=position;
                Bundle bundle = new Bundle();
                bundle.putString("orderId",String.valueOf(data.get(position).getId()));
                SPKeyManager.XiaoShouDetailMyOrdeEntity=data.get(position);
                state=SPKeyManager.XiaoShouDetailMyOrdeEntity.getState();
                switch (data.get(position).getState()){
                    case "订单已提交"://待付款
                        bundle.putString("flag","tijiao");
                        bundle.putString("activity","xiaoshou");
                        jumpActivity(sellerTijiaoActivity.class,bundle);
//                        jumpActivity(BackActivity.class,bundle);
                        break;
                    case "买家已付款,等待卖家发货"://待发货
                        jumpActivity(XiaoDaiSendActivity.class,bundle);
                        break;
                    case "卖家已发货"://已发货
                        bundle.putString("activity","xiaoshou");
                        jumpActivity(XiaosouSendActivity.class,bundle);
                        break;
                    case "买家已收货,交易成功"://交易成功
                        bundle.putString("activity","xiaoshou");
                        jumpActivity(sellerTijiaoActivity.class,bundle);
                        break;
                    case "仲裁中"://退货
                    case "买家已申请退款":
                    case "卖家拒绝退款":
                        jumpActivity(BackActivity.class,bundle);
                        break;
                    case "订单已取消"://交易关闭
                    case "订单已关闭":
                    case "已退款":
                        bundle.putString("flag","close");
                        bundle.putString("activity","xiaoshou");
                        jumpActivity(sellerTijiaoActivity.class,bundle);
                        break;
                }
            }
        });

    }
    @OnClick({R.id.head_back})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
        }
    }
    private void refreshTable(int index){
        for(int i=0;i<7;i++){
            ((StoreTable2)(xiaoshou_guid.getChildAt(i))).select(i==index);
        }
    }
    private View addMenu(String text, boolean invisableV, final int index) {
        StoreTable2 storeTable = new StoreTable2(mContext, text);
        storeTable.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        storeTable.invisibleV();
//        storeTable.setTestSize(ScreenUtils.sp2px(mContext,8));
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
        orderState=index;
        xiaoShoumenagerAdapter.setIndex(index);
        pageIndex=1;
        sendRequest(0,"");
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
                new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetOrderList,ordeMap,this ).request();
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
                xiaoShoumenagerAdapter.notifyDataSetChanged();
                if (pageIndex >= totalPage) {
                    xiaoshou_listview.setPullLoadEnable(false);
                } else {
                    xiaoshou_listview.setPullLoadEnable(true);
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
            xiaoshou_listview.stopLoadMore();
            xiaoshou_listview.stopRefresh();
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
        if(!StringUtil.isEmpty(state)&&!state.equals(SPKeyManager.XiaoShouDetailMyOrdeEntity.getState())){
            switch (SPKeyManager.XiaoShouDetailMyOrdeEntity.getState()){
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
