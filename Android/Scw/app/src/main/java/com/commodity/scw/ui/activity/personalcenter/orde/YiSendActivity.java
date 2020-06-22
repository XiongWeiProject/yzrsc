package com.commodity.scw.ui.activity.personalcenter.orde;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.mine.DetailMyOrdeEntity;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.personalcenter.SeeWuliu;
import com.commodity.scw.ui.activity.store.StoreSettingActivity;
import com.commodity.scw.ui.dialog.CommonDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 已发货
 * Created by yangxuqiang on 2017/4/4.
 */

public class YiSendActivity extends BaseActivity {
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
    private String id;
    private String url;

    @Bind(R.id.send_item_express)
    TextView send_item_express;//运费
    @Bind(R.id.send_item_total)
    TextView send_item_total;//总价

    @Override
    protected int getContentView() {
        return R.layout.activity_yisend;
    }

    @Override
    protected void initView() {
        title.setText("订单详情");
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("orderId");
        String activity = bundle.getString("activity");
        if("myorde".equals(activity)){
            url = IRequestConst.RequestMethod.BuyGetOrderDetail;
        }else {
            url=IRequestConst.RequestMethod.GetSoldOrderDetail;
        }
        sendRequest(0,"");
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.yi_button_backpay,R.id.yi_button_confirm,R.id.yi_button_wuliu})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back://
                finish();
                break;
            case R.id.yi_button_backpay://退款
                Bundle bundle = new Bundle();
                bundle.putString("ordeId",id);
                bundle.putInt("flag",0);
                jumpActivity(BackMoneyActivity.class,bundle);
                finish();
                break;
            case R.id.yi_button_wuliu://物流
                Bundle bundle1 = new Bundle();
                bundle1.putString("orderId",id);
                jumpActivity(SeeWuliu.class,bundle1);
                break;
//            case R.id.yi_button_shouhou://售后
//                break;
            case R.id.yi_button_confirm://确认收货
                CommonDialog commonDialog = new CommonDialog(YiSendActivity.this);
                commonDialog.show();
                commonDialog.setContext("确认收货");
                commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                    @Override
                    public void clickSubmit() {
                        sendRequest(1,"");
                    }
                });
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
        }else if(tag==1){//确认收货
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",String.valueOf(id));
            new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.ConfirmOrderReceived,map,this).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){
                JSONObject data = response.optJSONObject("data");
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

                JSONArray orderGoods = data.optJSONArray("orderGoods");
                try {
                    JSONObject order = (JSONObject) orderGoods.get(0);
                    ImageLoaderManager.getInstance().displayImage(order.optString("image"),send_image);

                    send_text.setText(order.optString("description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else if(tag==1){
                if(response.optBoolean("data")){
                    tip("确认收货成功");
                    SPKeyManager.curDetailMyOrdeEntity.setState(Constanct.acceptGoods);
                    finish();
                }else {
                    tip(response.optString("msg"));
                }
            }
        }else {
            tip(response.optString("msg"));
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
}
