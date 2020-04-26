package com.commodity.scw.ui.activity.personalcenter.money;

import android.view.View;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.model.mine.TixianjiluDataEntity;
import com.commodity.scw.ui.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yangxuqiang on 2017/6/7.
 */

public class TixianDetaillActivity extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.detaill_price)
    TextView detaill_price;
    @Bind(R.id.detaill_state)
    TextView detaill_state;
    @Bind(R.id.detaill_name)
    TextView detaill_name;
    @Bind(R.id.detaill_number)
    TextView detaill_number;
    @Bind(R.id.detaill_time)
    TextView detaill_time;
    private TixianjiluDataEntity entity;

    @Override
    protected int getContentView() {
        return R.layout.activity_tixiandetaill;
    }

    @Override
    protected void initView() {
//        title.setText("提现详情");
        entity = (TixianjiluDataEntity) getIntent().getExtras().getSerializable("entity");
        title.setText(entity.getTransactionType()+"详情");
        sendRequest(0,"");
    }

    @Override
    protected void initListeners() {

    }
    @OnClick(R.id.head_back)
    public void click(View v){
        finish();
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        HashMap<String, String> map = new HashMap<>();
        map.put("id",entity.getId()+"");
        new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetWithdrawDetail,map,this).request();
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);

        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            JSONObject data = response.optJSONObject("data");
            detaill_price.setText("金额 ¥ "+ data.optString("amount"));
            detaill_state.setText(data.optString("status"));
            detaill_name.setText(data.optString("cardHolder"));
            detaill_number.setText(data.optString("cardNumber"));
            detaill_time.setText(data.optString("createTime"));
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
