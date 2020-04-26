package com.commodity.scw.ui.activity.personalcenter;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.ui.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by yangxuqiang on 2017/5/21.
 */

public class AlertPhone extends BaseActivity {
    @Bind(R.id.alert_y)
    EditText alert_y;
    @Bind(R.id.alert_phone)
    TextView phone;
    @Bind(R.id.head_title)
    TextView title;
    private String vaildCode;
    @Bind(R.id.alert_button_y)
    Button alert_button_y;

    @Override
    protected int getContentView() {
        return R.layout.activity_alertphone;
    }

    @Override
    protected void initView() {
        title.setText("修改绑定手机");
        phone.setText(ConfigManager.instance().getUser().getMobile());

    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.alert_button_y,R.id.head_back,R.id.alert_button_sumbert})
    public void click(View v){
        switch (v.getId()){
            case R.id.alert_button_sumbert:
                String trim = alert_y.getText().toString().trim();
                if(vaildCode==null){
                    tip("请获取手机验证码");
                    initTime();
                }else if(trim==null||trim.length()==0) {
                    tip("请获输入验证码");
                    initTime();
                }else if(!trim.equals(vaildCode)){
                    tip("输入的验证码不正确");
                    initTime();
                }else {
                    jumpActivity(InputPhoneActivity.class);
                    finish();
                }
                break;
            case R.id.alert_button_y:
                sendRequest(0,"");
                alert_button_y.setClickable(false);
                handler.postDelayed(runnable,1000);
                break;
            case R.id.head_back:
                finish();
                break;
        }
    }

    private void initTime() {
        handler.removeCallbacks(runnable);
        time=60;
        alert_button_y.setClickable(true);
        alert_button_y.setText("请重新获取验证码");
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if(tag==0){
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile",ConfigManager.instance().getUser().getMobile());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.GetVaildCodeRegisted, parmMap, this);
            httpManager.request();
        }else if(tag==1){

        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(tag==0){
                JSONObject data = response.optJSONObject("data");
                //验证码
                vaildCode = data.optString("vaildCode");
            }else if(tag==1){

            }
        }else {
            tip(response.optString("msg"));
            initTime();
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
    Handler handler=new Handler();
    int time=60;
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            time--;
            if(time>0){
                alert_button_y.setText(time+"秒");
                handler.postDelayed(runnable,1000);
            }else {
                alert_button_y.setClickable(true);
                alert_button_y.setText("请重新获取验证码");
                time=60;
            }
        }
    };
}
