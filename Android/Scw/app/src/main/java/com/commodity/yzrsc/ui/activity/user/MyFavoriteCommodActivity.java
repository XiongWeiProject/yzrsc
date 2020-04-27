package com.commodity.yzrsc.ui.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManageNew;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.ImgType;
import com.commodity.yzrsc.model.MarketGoods;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.commodity.CommodityOrderActivity;
import com.commodity.yzrsc.ui.adapter.FavoriteGoodsAdapter;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;

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
 * Created by liyushen on 2017/4/2 11:27
 * 我的收藏
 */
public class MyFavoriteCommodActivity extends BaseActivity{
    @Bind(R.id.xlistView)
    XListView xlistView;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;

    private int pageIndex = 1;
    private int totalPage = 1;
    FavoriteGoodsAdapter marketGoodsAdapter;
    List<MarketGoods> marketGoodsList;
    private  int curpostion=-1;
    @Override
    protected int getContentView() {
        return R.layout.activity_myfavorite_commod;
    }

    @Override
    protected void initView() {
        xlistView.setPullLoadEnable(true);
        marketGoodsList=new ArrayList<>();
        marketGoodsAdapter=new FavoriteGoodsAdapter(this,marketGoodsList,R.layout.item_favorite_goods);
        xlistView.setAdapter(marketGoodsAdapter);
        sendRequest(1,"");
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
    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("sortOrder","1");
            parmMap.put("pageIndex",pageIndex+"");
            parmMap.put("pageSize",""+ SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetMyFavorGoods, parmMap, this);
            httpManager.request();
        }else if (tag==2){//收藏
            Map<String, Object> parmMap = new HashMap<String, Object>();
            JSONArray array=new JSONArray();
            array.put(marketGoodsList.get(curpostion).getId());
            parmMap.put("goodSaleIds",array);
            parmMap.put("memberId", ConfigManager.instance().getUser().getId());
            HttpManageNew httpManager = new HttpManageNew(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.CancelFavorGoods, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                if (pageIndex == 1) {
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
                }
                if (marketGoodsList.size()>0){
                    tv_nodata.setVisibility(View.GONE);
                }else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }

            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
            xlistView.stopLoadMore();
            xlistView.stopRefresh();
        }else if (tag==2) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                tip("取消收藏成功");
                marketGoodsList.remove(curpostion);
                marketGoodsAdapter.notifyDataSetChanged();
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
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


    //获取解析数据源
    private List<MarketGoods> getResultData() {
        List<MarketGoods> marketGoodses=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            MarketGoods marketGoods =new MarketGoods();
            marketGoods.setId(i+"");
            marketGoods.setDescribe("Describe"+i);
            int count=1+(int)(Math.random()*5);
            ImgType[] types=new ImgType[count];
            for (int j = 0; j < count; j++) {
                ImgType imgType=new ImgType();
                types[j]=imgType;
            }
            marketGoods.setImgTypes(types);
            marketGoodses.add(marketGoods);
        }
        return marketGoodses;
    }

    public void lijiGoumai(int postion){
        curpostion=postion;
        Bundle bundle=new Bundle();
        bundle.putString("goodsSaleId",marketGoodsList.get(postion).getId());
        jumpActivity(CommodityOrderActivity.class,bundle);

    }
    public void quxiaoShoucang(int postion){
        curpostion=postion;
        sendRequest(2,"");
    }

    private void initResultData(List<MarketGoods> marketGoodsList, MarketGoods marketGoods, JSONObject jsonObject) {
        marketGoods.setId(jsonObject.optString("id"));
        marketGoods.setDescribe(jsonObject.optString("description"));
        marketGoods.setPrice(new BigDecimal(new java.text.DecimalFormat("0.00").format(jsonObject.optDouble("price")))+"");
        marketGoods.setSuggestedPrice(new BigDecimal(new java.text.DecimalFormat("0.00").format(jsonObject.optDouble("suggestedPrice")))+"");
        marketGoods.setShopId(jsonObject.optString("shopId"));
        JSONArray images=jsonObject.optJSONArray("images");
        if (images!=null&&images.length()>0){
            ImgType[] types=new ImgType[images.length()];
            for (int j = 0; j < images.length(); j++) {
                ImgType imgType=new ImgType();
                try {
                    imgType.setImgpath(images.getString(j));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                types[j]=imgType;
            }
            marketGoods.setImgTypes(types);
        }
        marketGoods.setDateTime(SPKeyManager.dateFormat2.format(new Date()).substring(0,10));
        marketGoods.setFavorite(jsonObject.optBoolean("isFavored"));
        marketGoods.setOnsale(jsonObject.optBoolean("isOnsale"));
        marketGoodsList.add(marketGoods);
    }
}
