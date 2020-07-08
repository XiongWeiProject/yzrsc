package com.commodity.yzrsc.ui.activity.friend;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.UpLoadUtils;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.user.MyCartActivity;
import com.commodity.yzrsc.ui.pay.PayActivity;
import com.commodity.yzrsc.utils.LogUtil;
import com.commodity.yzrsc.view.flowLayout.FlowLayout;
import com.commodity.yzrsc.view.flowLayout.TagAdapter;
import com.commodity.yzrsc.view.flowLayout.TagFlowLayout;
import com.yixia.camera.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayMoneNumActivity extends BaseActivity {


    @Bind(R.id.head_back)
    LinearLayout headBack;
    @Bind(R.id.head_title)
    TextView headTitle;
    @Bind(R.id.head_text_right)
    TextView headTextRight;

    @Bind(R.id.et_money)
    EditText etMoney;
    @Bind(R.id.btn_pay)
    TextView btnPay;
    List<String> tagList = new ArrayList<>();
    TagAdapter tagAdapter;
    @Bind(R.id.tag_money)
    TagFlowLayout tagMoney;
    TextView tv;
    int money = 0;
    @Override
    protected int getContentView() {
        return R.layout.activity_pay_mone_num;
    }

    @Override
    protected void initView() {
        headTitle.setText("充值");
        tagList.add("50");
        tagList.add("100");
        tagList.add("200");
        tagList.add("500");
        tagList.add("1000");
        tagList.add("2000");
        //类型列表
        tagAdapter = new TagAdapter<String>(tagList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                tv = (TextView) LayoutInflater.from(PayMoneNumActivity.this)
                        .inflate(R.layout.item_tag_money, tagMoney, false);
                tv.setText(s);
                return tv;
            }
        };
        tagMoney.setAdapter(tagAdapter);
//        tagMoney.setMaxSelectCount(1);
        tagMoney.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                etMoney.setText(tagAdapter.getItem(position)+"");
                return false;
            }
        });


    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_pay)
    public void onViewClicked() {
        //生成钱包顶顶那
        String momey = etMoney.getText().toString().trim();
        if (TextUtils.isEmpty(momey)){
            tip("请输入充值金额");
            return;
        }
        postWall(momey);
    }

    private void postWall(final String momey) {
        Map<String, Object> parmMap = new HashMap<String, Object>();
        parmMap.put("amount",momey);
        JSONObject jsonObject = new JSONObject(parmMap);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),jsonObject.toString());
        UpLoadUtils.instance().jsonRequest(IRequestConst.RequestMethod.POSTWALLORDER, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(mContext, "生成订单失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject resultJson= null;
                try {
                    resultJson = new JSONObject(response.body().string());
                    if (resultJson!=null&&resultJson.optBoolean("success")){
                        //跳到支付界面
                        Bundle bundle = new Bundle();
                        bundle.putString("total",momey);
                        bundle.putInt("type",3);
                        bundle.putString("ordeId",resultJson.optString("data"));
                        jumpActivity(PayActivity.class,bundle);
//                tip("确认下单成功");
                        finish();
                    }else {
                        if (resultJson!=null&&!resultJson.optBoolean("success")){
                            tip(resultJson.optString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
