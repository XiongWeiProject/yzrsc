package com.commodity.yzrsc.ui.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManageNew;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.CartBean;
import com.commodity.yzrsc.model.ImgType;
import com.commodity.yzrsc.model.MarketGoods;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.commodity.CommodityOrderActivity;
import com.commodity.yzrsc.ui.activity.commodity.OrderActivity;
import com.commodity.yzrsc.ui.adapter.FavoriteGoodsAdapter;
import com.commodity.yzrsc.ui.adapter.ShopCartAdapter;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/4/2 11:27
 * 我的收藏
 */
public class MyCartActivity extends BaseActivity{
    @Bind(R.id.btn_commit)
    Button bt_commit;
    @Bind(R.id.recycleview)
    XListView rl_shopproducts;

    ShopCartAdapter marketGoodsAdapter;
    List<CartBean> marketGoodsList;
    private  int curpostion=-1;
    private int current_quantity = 0;
    private double total = 0;
    private double yunfei = 0;
    List<String> ids = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_cart;
    }

    @Override
    protected void initView() {
        rl_shopproducts.setPullLoadEnable(true);
        marketGoodsList=new ArrayList<>();
        marketGoodsAdapter=new ShopCartAdapter(this,marketGoodsList,R.layout.item_shopcart);
        rl_shopproducts.setAdapter(marketGoodsAdapter);
        sendRequest(1,"");
    }

    @Override
    protected void initListeners() {
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putDouble("total", Double.valueOf(total));
                bundle.putDouble("postage", Double.valueOf(yunfei));
                bundle.putString("ids", ids.toString());
                jumpActivity(OrderActivity.class, bundle);
            }
        });
        rl_shopproducts.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //pageIndex = 1;
                        sendRequest(1, "");
                        rl_shopproducts.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
                    }
                },SPKeyManager.delay_time);
            }

            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //pageIndex++;
                        //sendRequest(1, "");
                    }
                },SPKeyManager.delay_time);
            }
        });
    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("sortOrder","1");
            //parmMap.put("pageIndex",pageIndex+"");
            parmMap.put("pageSize",""+ SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetCartList, parmMap, this);
            httpManager.request();
        }
        else if(tag == 2){
            //删除
            if(curpostion < marketGoodsList.size()) {
                String ids =  "[" + Integer.valueOf(marketGoodsList.get(curpostion).getId()).toString() + "]";
                Map<String, String> parmMap = new HashMap<String, String>();
                parmMap.put("ids", "[" + Integer.valueOf(marketGoodsList.get(curpostion).getId()).toString() + "]");
                HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                        IRequestConst.RequestMethod.DeleteCart, parmMap, this);
                //httpManager.setmRequest(ids);
                httpManager.request();
            }
        }
        else if(tag == 3){
            //修改数量
            if(curpostion < marketGoodsList.size()) {
                if ((params != null) && (params.length > 0)) {
                    Map<String, String> parmMap = new HashMap<String, String>();
                    parmMap.put("id", Integer.valueOf(marketGoodsList.get(curpostion).getId()).toString());
                    parmMap.put("memberId", ConfigManager.instance().getUser().getId());
                    //parmMap.put("goodSaleId", marketGoodsList.get(curpostion).addGoodsSaleId());
                    parmMap.put("quantity", ((Integer) params[0]).toString());
                    HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                            IRequestConst.RequestMethod.ChangeCartQuantity, parmMap, this);
                    httpManager.request();
                }
            }
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                    marketGoodsList.clear();
                JSONArray dataArray=resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        CartBean marketGoods =new CartBean();
                        try {
                            initResultData(marketGoodsList,marketGoods,dataArray.getJSONObject(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    marketGoodsAdapter.notifyDataSetChanged();
                }
//                if (marketGoodsList.size()>0){
//                    tv_nodata.setVisibility(View.GONE);
//                }else {
//                    tv_nodata.setVisibility(View.VISIBLE);
//                }
//
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
            rl_shopproducts.stopLoadMore();
            rl_shopproducts.stopRefresh();
        }
        else if( tag == 2 ){
            //删除
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")) {
                marketGoodsList.remove(curpostion);
                marketGoodsAdapter.notifyDataSetChanged();
            }
        }
        else if( tag == 3 ){
            //修改数量
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")) {
                marketGoodsList.get(curpostion).setQuantity(current_quantity);
                marketGoodsAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }


    private void initResultData(List<CartBean> marketGoodsList, CartBean marketGoods, JSONObject jsonObject) {
        //marketGoods.setId(jsonObject.optInt("id"));
        Integer shopId = (jsonObject.optInt("shopId"));
        String shopName = (jsonObject.optString("shopName"));
        String sellerAvatar = (jsonObject.optString("sellerAvatar"));

        JSONArray goods=jsonObject.optJSONArray("shoppingCartGoods");
        if (goods!=null&&goods.length()>0){
            for (int j = 0; j < goods.length(); j++) {
                    marketGoods.setId(j);
                marketGoods.setShopId(shopId);
                marketGoods.setShopName(shopName);
                marketGoods.setSellerAvatar(sellerAvatar);

                try {
                    JSONObject obj = goods.getJSONObject(j);

                    marketGoods.setGoodsSaleId(obj.optString("goodsSaleId"));
                    marketGoods.setGoodsSaleName(obj.optString("goodsSaleName"));
                    marketGoods.setGoodsImage(obj.optString("goodsImage"));
                    marketGoods.setGoodsPrice(obj.optDouble("goodsPrice"));
                    marketGoods.setPostage(obj.optDouble("postage"));
                    marketGoods.setQuantity(obj.optInt("quantity"));

                    ids.add(obj.optString("goodsSaleId"));
                    yunfei += obj.optDouble("postage");
                    total += obj.optDouble("goodsPrice");
                }
                catch (JSONException e){
                    //
                }
            }
        }
//        marketGoods.setDateTime(SPKeyManager.dateFormat2.format(new Date()).substring(0,10));
//        marketGoods.setFavorite(jsonObject.optBoolean("isFavored"));
//        marketGoods.setOnsale(jsonObject.optBoolean("isOnsale"));
        marketGoodsList.add(marketGoods);
    }

    public void deleteCart(int postion){
        curpostion = postion;
        sendRequest(2, "");
        return;
    }

    public void changeCartQuantity(int postion, int quantity){
        curpostion = postion;
        current_quantity = quantity;
        sendRequest(3, quantity);
        return;
    }
}
