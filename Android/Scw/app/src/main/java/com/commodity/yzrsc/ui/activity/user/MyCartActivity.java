package com.commodity.yzrsc.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.CartBean;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.commodity.OrderActivity;
import com.commodity.yzrsc.ui.adapter.ShopCartAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liyushen on 2017/4/2 11:27
 * 我的收藏
 */
public class MyCartActivity extends BaseActivity {
    @Bind(R.id.btn_commit)
    Button bt_commit;
    @Bind(R.id.recycleview)
    ListView rl_shopproducts;

    ShopCartAdapter marketGoodsAdapter;
    List<CartBean> marketGoodsList = new ArrayList<>();
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.tv_allPrice)
    TextView tvAllPrice;
    @Bind(R.id.ll_item)
    LinearLayout llItem;
    private int curpostion = -1;
    private int current_quantity = 0;
    private double total  = 0;
    private double yunfei  =  0;
    BigDecimal totalsss;
    BigDecimal yunfeiddd;
    ArrayList<Integer> ids = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_cart;
    }

    @Override
    protected void initView() {
        sendRequest(1, "");
    }

    @Override
    protected void initListeners() {
        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < marketGoodsList.size(); i++) {
                    for (int j = 0; j < marketGoodsList.get(i).getShoppingCartGoods().size(); j++) {
                        ids.add(marketGoodsList.get(i).getShoppingCartGoods().get(j).getShoppingCartId());
                    }
                }
                if (ids.size()==0){
                    tip("购物车不能为空");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putDouble("total", totalsss.doubleValue());
                bundle.putDouble("postage", yunfeiddd.doubleValue());
                bundle.putIntegerArrayList("ids", ids);
                jumpActivity(OrderActivity.class, bundle);
            }
        });

    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
//            parmMap.put("sortOrder","1");
            //parmMap.put("pageIndex",pageIndex+"");
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetCartList, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                JSONArray dataArray = resultJson.optJSONArray("data");
                if (dataArray != null) {
                    marketGoodsList = JSON.parseArray(dataArray.toString(), CartBean.class);
                    marketGoodsAdapter = new ShopCartAdapter(this, marketGoodsList, R.layout.item_shopcart);
                    rl_shopproducts.setAdapter(marketGoodsAdapter);
                    BigDecimal b1 = new BigDecimal(Double.toString(total));
                    BigDecimal b2 = new BigDecimal(Double.toString(yunfei) );
                    for (int i = 0; i < marketGoodsList.size(); i++) {
                        for (int j = 0; j < marketGoodsList.get(i).getShoppingCartGoods().size(); j++) {
                            b1 = b1 .add(new BigDecimal(Double.toString(marketGoodsList.get(i).getShoppingCartGoods().get(j).getGoodsPrice())).multiply(new BigDecimal(Double.toString( marketGoodsList.get(i).getShoppingCartGoods().get(j).getQuantity()))));
                            b2 =b2.add(new BigDecimal(Double.toString(marketGoodsList.get(i).getShoppingCartGoods().get(j).getPostage())));
                        }

                    }
                    totalsss  = b1;
                    yunfeiddd = b2;
                    tvAllPrice.setText("总价："+(b1.add(b2).doubleValue()));
                }
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

    public void deleteCart() {
        sendRequest(1, "");
        return;
    }
    public void ShowTotal(double totals,double Yun) {
        total = total - totals;
        yunfei = yunfei - Yun ;
        double zongjia  = total+yunfei;
        tvAllPrice.setText("总价："+zongjia);
        return;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
