package com.commodity.yzrsc.ui.activity.personalcenter.orde;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.SeeWuliuEntity;
import com.commodity.yzrsc.model.WuliuDataEntity;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.dialog.CommonDialog;
import com.commodity.yzrsc.ui.dialog.SendGoodsDialog;
import com.commodity.yzrsc.utils.GsonUtils;
import com.commodity.yzrsc.utils.ScreenUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;
import cn.yohoutils.StringUtil;

/**
 * 代发货
 * Created by yangxuqiang on 2017/4/4.
 */

public class XiaoDaiSendActivity extends BaseActivity {
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
    @Bind(R.id.pa_button_send)
    Button pa_button_send;
    @Bind(R.id.send_item_express)
    TextView send_item_express;//运费
    @Bind(R.id.send_item_total)
    TextView send_item_total;//总价
    @Bind(R.id.seller_view)
    TextView seller_view;
    private SendGoodsDialog sendGoodsDialog;
    private String id;
    private String code;
    private List<String> express=new ArrayList<>();
    private List<WuliuDataEntity> data;

    @Override
    protected int getContentView() {
        return R.layout.activity_daisend;
    }

    @Override
    protected void initView() {
        title.setText("订单详情");
        pa_button_send.setText("发货");
        Bundle bundle = getIntent().getExtras();
        sendGoodsDialog = new SendGoodsDialog(this);


        id = bundle.getString("orderId");
        sendRequest(0,"");
        seller_view.setText("买家");

    }

    @Override
    protected void initListeners() {
        sendGoodsDialog.setCancleOnClickListener(new CommonDialog.OnClickCancelListener() {
            @Override
            public void clickCance() {
                sendGoodsDialog.dismiss();
            }
        });
        sendGoodsDialog.setSubmitListener(new CommonDialog.OnClickCancelListener() {
            @Override
            public void clickCance() {
                if(StringUtil.isEmpty(sendGoodsDialog.getInputCode())||StringUtil.isEmpty(code)){
                    tip("请输入内容");
                }else {
                    sendRequest(1,"");
                }
            }
        });
        sendGoodsDialog.setOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest(2,"");

            }
        });
    }
    @OnClick({R.id.head_back,R.id.pa_button_send})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back://返回
                finish();
                break;
//            case R.id.pa_button_shouhou://售后
//                break;
            case R.id.pa_button_send://发货
                if(!sendGoodsDialog.isShowing()){
                    sendGoodsDialog.show();
                }
                break;
        }
    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",id);
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetSoldOrderDetail,map,this).request();
        }else if(tag==1){
            HashMap<String, String> map = new HashMap<>();
            map.put("soldOrderId",id);
            map.put("logisticsCode",code);
            map.put("logisticsSN",sendGoodsDialog.getInputCode());
            new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.DeliverAnOrder,map,this).request();

        }else if(tag==2){
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetLogisticsList,null,this).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){
                JSONObject data = response.optJSONObject("data");
                send_item_mai.setText(data.optString("buyer"));//卖家
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


            }else if (tag==1){
                tip("发货成功");
                if(sendGoodsDialog.isShowing()){
                    SPKeyManager.XiaoShouDetailMyOrdeEntity.setState(Constanct.sendGoods);
                    sendGoodsDialog.dismiss();
                }
                finish();
            }else if(tag==2){
                SeeWuliuEntity seeWuliuEntity = GsonUtils.jsonToObject(response.toString(), SeeWuliuEntity.class);
                data = seeWuliuEntity.getData();
                for (int i = 0; i< data.size()-1; i++){
                    express.add(i, data.get(i).getValue());
                }
                showSingle();
            }
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
    private void showSingle() {
//        final BankDialog bankDialog = new BankDialog(mContext, items);
//        bankDialog.show();
//        bankDialog.setItemClick(new BankDialog.OnitemClickListener() {
//            @Override
//            public void itemClick(int position) {
//
//                yinhang=items.get(position);
//                addcard_text_yinhang.setText(yinhang);
//                bankDialog.dismiss();
//            }
//        });
        if (express.size()==0){
            return;
        }
        OptionPicker picker = new OptionPicker(this, express);
        picker.setTitleTextSize(ScreenUtils.sp2px(mContext,14));
        picker.setSubmitTextColor(0xFFBA0008);
        picker.setCancelTextColor(0xFF111A3B);
        picker.setCancelTextSize(14);
        picker.setSubmitTextSize(14);
        picker.setTextColor(0xFF111A3B, 0xFF999999);
        WheelView.LineConfig config = new WheelView.LineConfig();
        config.setColor(0xFFF2F2F2);
        picker.setLineConfig(config);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int i, String s) {
                sendGoodsDialog.setCText(s);
                code=data.get(i).getKey();
            }
        });
        picker.show();
    }
}
