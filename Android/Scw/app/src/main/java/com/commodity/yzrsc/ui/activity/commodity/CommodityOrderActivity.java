package com.commodity.yzrsc.ui.activity.commodity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.AdressDetail;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.user.UserAdressListActivity;
import com.commodity.yzrsc.ui.dialog.CommonDialog;
import com.commodity.yzrsc.ui.pay.PayActivity;
import com.commodity.yzrsc.ui.widget.imageview.XCRoundRectImageView;
import com.commodity.yzrsc.utils.ResultUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/3/29 22:08
 * 确认订单界面
 */
public class CommodityOrderActivity extends BaseActivity{
    @Bind(R.id.ll_adress)
    LinearLayout ll_adress;
    @Bind(R.id.tv_adress)
    TextView tv_adress;
    @Bind(R.id.btn_submit)
    Button btn_submit;
    @Bind(R.id.iv_image)
    XCRoundRectImageView iv_image;
    @Bind(R.id.xioashou_content)
    TextView xioashou_content;
    @Bind(R.id.tv_yuanjia)
    TextView tv_yuanjia;
    @Bind(R.id.tv_yunfei)
    TextView tv_yunfei;
    @Bind(R.id.tv_nowjiage)
    TextView tv_nowjiage;
    @Bind(R.id.tv_zongjia)
    TextView tv_zongjia;
    JSONObject dataJson;
    AdressDetail adressDetail;
    private String goodsSaleId="";
    private double price;
    private double postage;

    @Override
    protected int getContentView() {
        return R.layout.activity_commdity_order;
    }

    @Override
    protected void initView() {
        String goodsDetailStr = "";
        if (getIntent()!=null&&getIntent().getExtras().containsKey("goodsDetailStr")){
            goodsDetailStr=getIntent().getExtras().getString("goodsDetailStr");
        }
        if (getIntent()!=null&&getIntent().getExtras().containsKey("goodsSaleId")){
            goodsSaleId=getIntent().getExtras().getString("goodsSaleId");
        }

        try {
            if (!goodsDetailStr.equals("")){
                dataJson=new JSONObject(goodsDetailStr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_adress.setTag("新增收货地址");
        if (!goodsDetailStr.isEmpty()&&dataJson!=null){
            initDataView(dataJson);
        }
        sendRequest(1,"");
        if (!goodsSaleId.isEmpty()){
            sendRequest(3,"");
        }
    }

    @Override
    protected void initListeners() {
        ll_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_adress.getText().toString().equals("新增收货地址")){
                    jumpActivityForResult(1001,AddUserAdressActivity.class);
                }else {
                    jumpActivityForResult(1002,UserAdressListActivity.class);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_adress.getTag().equals("新增收货地址")){
                    tip("请添加收货地址");
                    return;
                }
                if (adressDetail==null){
                    tip("请添加收货地址");
                    return;
                }
                if (dataJson==null){
                    tip("当前商品状态异常");
                    return;
                }

                CommonDialog comm=new CommonDialog(CommodityOrderActivity.this);
                comm.show();
                comm.setContext("确认下单？");
                comm.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                    @Override
                    public void clickSubmit() {
                        sendRequest(2,"");
                    }
                });
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if (tag == 1) {//获取用户收货地址列表
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId",ConfigManager.instance().getUser().getId());
            parmMap.put("pageIndex","1");
            parmMap.put("pageSize","100");
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetAddressInfoList, parmMap, this);
            httpManager.request();
        }else if (tag==2){//确认订单
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("goodsSaleId",dataJson.optString("id"));
            parmMap.put("addressInfoId",adressDetail.getId());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.OrderAnOrder, parmMap, this);
            httpManager.request();
        }else if (tag == 3) {//商品详情
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("goodsSaleId",goodsSaleId);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetGoodsDetail, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                try {
                    JSONArray dataArray=resultJson.getJSONArray("data");
                    if (dataArray!=null&&dataArray.length()>0){
                        adressDetail=new AdressDetail();
                        adressDetail.setId(dataArray.getJSONObject(0).optString("id"));
                        tv_adress.setText(dataArray.getJSONObject(0).optString("receiver")+"       "+
                                dataArray.getJSONObject(0).optString("mobile")+"\n"+
                                dataArray.getJSONObject(0).optString("province")+
                                dataArray.getJSONObject(0).optString("city")+
                                dataArray.getJSONObject(0).optString("district")+
                                dataArray.getJSONObject(0).optString("addressDetail"));
                        tv_adress.setTag("有收货地址");
                    }else {
                        tv_adress.setText("新增收货地址");
                        tv_adress.setTag("新增收货地址");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
        }else if (tag==2){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                //跳到支付界面
                Bundle bundle = new Bundle();
                bundle.putString("total",String.valueOf(price+postage));
                bundle.putString("ordeId",resultJson.optString("data"));
                jumpActivity(PayActivity.class,bundle);
//                tip("确认下单成功");
                finish();
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
        } else if (tag==3){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                dataJson=resultJson.optJSONObject("data");
                initDataView(dataJson);
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==1001){
                sendRequest(1,"");
            }else if (requestCode==1002){
               if (data!=null){
                   Bundle bundle=data.getExtras();
                   adressDetail= (AdressDetail) bundle.getSerializable("AdressDetail");
                   tv_adress.setText(adressDetail.getReceiver()+"      "+adressDetail.getMobile()+"\n"+
                           adressDetail.getProvince()+adressDetail.getCity()+adressDetail.getDistrict()+adressDetail.getAddressDetail());
               }
            }
        }
    }

    //init商品
    private void initDataView(JSONObject jsonObject){
        ImageLoaderManager.getInstance().displayImage((String) ResultUtil.getFirstDataFromArray(jsonObject.optJSONArray("images")),iv_image,R.drawable.ico_pic_fail_defalt);
        xioashou_content.setText(jsonObject.optString("description"));
        tv_yuanjia.setText("￥"+jsonObject.optString("suggestedPrice"));
        tv_nowjiage.setText("￥"+jsonObject.optDouble("price"));
        tv_yunfei.setText("￥"+jsonObject.optDouble("postage"));
        tv_zongjia.setText("￥"+(String.format("%.2f", jsonObject.optDouble("price")+jsonObject.optDouble("postage"))));
        price=jsonObject.optDouble("price");
        postage=jsonObject.optDouble("postage");
    }
}
