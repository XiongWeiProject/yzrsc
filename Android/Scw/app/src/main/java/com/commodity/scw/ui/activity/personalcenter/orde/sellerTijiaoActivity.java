package com.commodity.scw.ui.activity.personalcenter.orde;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.personalcenter.BuyBackDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 退货
 * Created by yangxuqiang on 2017/4/4.
 */

public class sellerTijiaoActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.send_image)
    ImageView send_image;//图片
    @Bind(R.id.send_text)
    TextView send_text;//简介
    @Bind(R.id.send_jiage)
    TextView send_jiage;//价格
    @Bind(R.id.send_item_value)
    TextView send_item_value;//订单时间
    @Bind(R.id.send_item_waybill)
    TextView send_item_waybill;//订单号
    @Bind(R.id.send_item_state)
    TextView send_item_state;//状态
    @Bind(R.id.send_item_mai)
    TextView send_item_mai;//卖家
    @Bind(R.id.send_class)
    TextView send_class;//等级
    @Bind(R.id.send_item_price)
    TextView send_item_price;//宝贝价格
    @Bind(R.id.send_item_comprin)
    TextView send_item_comprin;//快递公司
    @Bind(R.id.send_item_recivice)
    TextView send_item_recivice;//收货人
    @Bind(R.id.send_item_phone)
    TextView send_item_phone;//联系方式
    @Bind(R.id.send_item_address)
    TextView send_item_address;//地址
    @Bind(R.id.send_item_express)
    TextView send_item_express;//运费
    @Bind(R.id.send_item_total)
    TextView send_item_total;//总价
    private String id;
    private String url;
    @Bind(R.id.back_button_shouhou)
    Button back_button_shouhou;
    @Bind(R.id.seller_view)
    TextView seller_view;
    private String activity;

    @Override
    protected int getContentView() {
        return R.layout.activity_backgoods;
    }

    @Override
    protected void initView() {
        title.setText("订单详情");
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("orderId");
        activity = bundle.getString("activity");
        if("myorde".equals(activity)){
            url = IRequestConst.RequestMethod.BuyGetOrderDetail;
        }else {
            url=IRequestConst.RequestMethod.GetSoldOrderDetail;
            seller_view.setText("买家");
        }
        sendRequest(0,"");
        back_button_shouhou.setVisibility(View.VISIBLE);

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.back_button_shouhou})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back://
                finish();
                break;
            case R.id.back_button_shouhou://退款详情
                Bundle bundle = new Bundle();
                bundle.putString("orderId",id);
                jumpActivity(BuyBackDetailActivity.class,bundle);
                break;
        }
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",String.valueOf(id));
            new HttpManager(tag, HttpMothed.GET, url,map,this).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){
                JSONObject data = response.optJSONObject("data");
                if("myorde".equals(activity)){
                    send_item_mai.setText(data.optString("seller"));//卖家
                }else {
                    send_item_mai.setText(data.optString("buyer"));//卖家
                }
                send_item_mai.setText(data.optString("seller"));//卖家
                send_item_comprin.setText(data.optString("logisticsName"));//快递公司
                send_item_recivice.setText(data.optString("receiver"));//收件人名字
                send_item_phone.setText(data.optString("receiverMobile"));//收件人手机
                send_item_address.setText(data.optString("receiverAddress"));//收件人地址
                send_item_waybill.setText(data.optString("code"));//订单号
                send_item_value.setText(data.optString("createTime"));//时间
                send_jiage.setText("¥"+data.optString("goodsAmount"));//价格
                send_item_price.setText("¥"+data.optString("goodsAmount"));//价格
                send_item_express.setText("¥"+data.optString("postage"));//运费
                send_item_total.setText("¥"+data.optString("total"));//总价

                send_item_state.setText(data.optString("state"));//订单状态

                if(data.optString("state").contains("拒绝退款")){
                    back_button_shouhou.setVisibility(View.GONE);
                }else if(data.optString("state").contains("交易成功")){
                    back_button_shouhou.setVisibility(View.GONE);
                }else if(data.optString("state").contains("订单已取消")){
                    back_button_shouhou.setVisibility(View.GONE);
                }else if(data.optString("state").contains("订单已提交")){
                    back_button_shouhou.setVisibility(View.GONE);
                }else {
                    back_button_shouhou.setVisibility(View.VISIBLE);
                }


                JSONArray orderGoods = data.optJSONArray("orderGoods");
                try {
                    JSONObject order = (JSONObject) orderGoods.get(0);
                    ImageLoaderManager.getInstance().displayImage(order.optString("image"),send_image);

                    send_text.setText(order.optString("description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
}
