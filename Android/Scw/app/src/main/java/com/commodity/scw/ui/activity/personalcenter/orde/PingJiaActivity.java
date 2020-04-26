package com.commodity.scw.ui.activity.personalcenter.orde;

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
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.ui.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 * 评价
 * Created by yangxuqiang on 2017/4/4.
 */

public class PingJiaActivity extends BaseActivity {

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
    private String goodId;

    @Override
    protected int getContentView() {
        return R.layout.activity_pingjia;
    }

    @Override
    protected void initView() {
        title.setText("订单详情");
        SPKeyManager.uploadmax=4;
        orderId = getIntent().getExtras().getString("orderId");
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
                    if(orderId!=null){
                        sendRequest(1,"");
                    }else {
                        tip("获取商品详情失败");
                    }

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
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.BuyGetOrderDetail,map,this).request();
        }else if(tag==1){
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",String.valueOf(orderId));
            map.put("goodId",goodId);
            map.put("description",trim);
//            map.put("shopId",commodityBean.getSellerImId());
            new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.AddGoodsEvaluation,map,this).request();
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
                    goodId = jsonObject.optString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else if (tag==1){
                if (response.optBoolean("data")){
//                    jumpActivity(XiaoShouActivity.class);
                    tip("评价成功");
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
