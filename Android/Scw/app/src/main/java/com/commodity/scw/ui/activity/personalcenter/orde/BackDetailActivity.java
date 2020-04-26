package com.commodity.scw.ui.activity.personalcenter.orde;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.Liuyan;
import com.commodity.scw.model.LiuyanEntity;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.BrackAdapter;
import com.commodity.scw.ui.dialog.CommonDialog;
import com.commodity.scw.utils.GsonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 退款详情
 * Created by 328789 on 2017/5/25.
 */

public class BackDetailActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.back_serson)
    TextView back_serson;
    @Bind(R.id.back_listview)
    ListView back_listview;
    @Bind(R.id.break_confrim)
    Button break_confrim;
    @Bind(R.id.break_cancle)
    Button break_cancle;
    private String orderId;
    private List<LiuyanEntity> serasonData;
    private BrackAdapter brackAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_backdetail;
    }

    @Override
    protected void initView() {
        title.setText("退款详情");
        Bundle extras = getIntent().getExtras();
        orderId = extras.getString("orderId");
        String state = extras.getString("state");
        switch (state){
            case "仲裁中"://退货
                break;
            case "买家已申请退款":

                break;
            case "卖家拒绝退款":
            case "已退款":
                break_confrim.setVisibility(View.GONE);
                break_cancle.setVisibility(View.GONE);
                break;
        }

        sendRequest(0,"");
        sendRequest(3,"");
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.break_dialog,R.id.break_cancle,R.id.break_confrim})
    public void click(View v){
        switch (v.getId()){
            case R.id.break_confrim://确认退款
                CommonDialog commonDialog = new CommonDialog(this);
                commonDialog.show();
                commonDialog.setContext("确认退款？");
                commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                    @Override
                    public void clickSubmit() {
                        sendRequest(1,"");
                    }
                });
                break;
            case R.id.break_cancle://拒绝退款
                CommonDialog commonDialog2 = new CommonDialog(this);
                commonDialog2.show();
                commonDialog2.setContext("确认拒绝退款？");
                commonDialog2.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                    @Override
                    public void clickSubmit() {
                        sendRequest(2,"");
                    }
                });
                break;
            case R.id.break_dialog://留言
//                Bundle bundle = new Bundle();
//                bundle.putString("orderId",orderId);
//                bundle.putBoolean("seller",true);
//                jumpActivityForResult(1,SellerPActivity.class,bundle);
                Bundle bundle = new Bundle();
                bundle.putString("ordeId",orderId);
                bundle.putInt("flag",1);
                jumpActivity(BackMoneyActivity.class,bundle);
                break;
            case R.id.head_back:
                finish();
                break;
        }
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if(tag==0){
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",orderId);
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetOrderRefundmentInfo,map,this ).request();
        }else if(tag==1){
            HashMap<String, String> map = new HashMap<>();
            map.put("soldOrderId",orderId);
            new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.AgreeRefundOrder,map,this ).request();
        }else if(tag==2){
            HashMap<String, String> map = new HashMap<>();
            map.put("soldOrderId",orderId);
            new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.DisagreeRefundOrder,map,this ).request();
        }else if(tag==3){
            HashMap<String, String> map = new HashMap<>();
            map.put("orderId",orderId);
            map.put("pageIndex","1");
            map.put("pageSize","100");
            map.put("sortOrder","1");
            new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetOrderArbitrationMessageList,map,this ).request();

        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){
                JSONObject data = response.optJSONObject("data");
                back_serson.setText(data.optString("reason"));
            }else if(tag==1){
                Boolean data = response.optBoolean("success");
                if(data!=null&&data){
                    SPKeyManager.XiaoShouDetailMyOrdeEntity.setState(Constanct.backMoney);
                    finish();
                    tip("已同意退款");
                }
            }else if(tag==2){
                Boolean data = response.optBoolean("data");
                if(data!=null&&data){
                    SPKeyManager.XiaoShouDetailMyOrdeEntity.setState(Constanct.refuseRefund);
                    finish();
                    tip("已拒绝退款");
                }
            }else if (tag==3){
//                JSONObject data = response.optJSONObject("data");
                Liuyan liuyan = GsonUtils.jsonToObject(response.toString(), Liuyan.class);
                serasonData = liuyan.getData();
                brackAdapter = new BrackAdapter(this, serasonData, R.layout.item_brack);
                back_listview.setAdapter(brackAdapter);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            sendRequest(3,"");
        }
    }
}
