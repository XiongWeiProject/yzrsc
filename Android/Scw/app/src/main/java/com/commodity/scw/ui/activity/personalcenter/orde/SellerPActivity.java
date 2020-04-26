package com.commodity.scw.ui.activity.personalcenter.orde;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.model.CommodityBean;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.personalcenter.XiaoShouActivity;
import com.commodity.scw.ui.adapter.AlterOrderAdapter;
import com.commodity.scw.utils.GsonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 * 评价
 * Created by yangxuqiang on 2017/4/4.
 */

public class SellerPActivity extends BaseActivity {

    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.pingjia_image)
    ImageView pingjia_image;
    @Bind(R.id.pjngjai_text)
    TextView pjngjai_text;
    @Bind(R.id.pingjia_jiage)
    TextView pingjia_jiage;//价格
    @Bind(R.id.pingjia_edit)
    EditText pingjia_edit;
    private String orderId;
    private String trim;
    private CommodityBean commodityBean;
    private String url;
    private String url2;

    @Override
    protected int getContentView() {
        return R.layout.activity_pingjia;
    }

    @Override
    protected void initView() {
        title.setText("订单详情");
        Bundle extras = getIntent().getExtras();
        orderId = extras.getString("orderId");
        boolean seller = extras.getBoolean("seller");
        if(seller){//卖jia
            url = IRequestConst.RequestMethod.LeaveArbitrationMessage;
            url2=IRequestConst.RequestMethod.GetSoldOrderDetail;

        }else {
            url = IRequestConst.RequestMethod.MemberorderLeaveArbitrationMessage;
            url2=IRequestConst.RequestMethod.BuyGetOrderDetail;
        }
        sendRequest(0,"");
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.pingjia_button_send})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_back://返回
                finish();
                break;
            case R.id.pingjia_button_send://评价发布
                trim = pingjia_edit.getText().toString().trim();
                if(StringUtil.isEmpty(trim)){
                    tip("请输入评价");
                }else {
                    sendRequest(1,"");

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
            map.put("orderId",String.valueOf(orderId));
            new HttpManager(tag, HttpMothed.GET,url2 ,map,this).request();
        }else if(tag==1){
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",String.valueOf(orderId));
            map.put("message",trim);
            new HttpManager(tag, HttpMothed.POST, url,map,this).request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){
                JSONObject data = response.optJSONObject("data");

                pingjia_jiage.setText("¥"+ data.optString("total"));//价格
                JSONArray orderGoods = data.optJSONArray("orderGoods");

                try {
                    JSONObject jsonObject = orderGoods.getJSONObject(0);
                    pjngjai_text.setText(jsonObject.optString("description"));
                    ImageLoaderManager.getInstance().displayImage(jsonObject.optString("image"),pingjia_image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (tag==1){
                if (response.optBoolean("data")){
                    jumpActivity(XiaoShouActivity.class);
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
