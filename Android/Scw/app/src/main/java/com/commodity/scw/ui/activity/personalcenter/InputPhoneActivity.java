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

public class InputPhoneActivity extends BaseActivity {
    @Bind(R.id.input_y)
    EditText alert_y;
    @Bind(R.id.input_phone)
    EditText phone;
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.input_button_y)
    Button input_button_y;
    private String vaildCode;
    private String phoneN;
    private String trim;

    @Override
    protected int getContentView() {
        return R.layout.activity_inputphone;
    }

    @Override
    protected void initView() {
        title.setText("修改绑定手机");
    }

    @Override
    protected void initListeners() {

    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if(tag==0){
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile", phoneN);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.ChangeInfoNewMobileGetVaild, parmMap, this);
            httpManager.request();
        }else if(tag==1){
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile", phoneN);
            parmMap.put("vaildCode", trim);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.ChangeInfoNewMobileSubmit, parmMap, this);
            httpManager.request();
        }
    }
    @OnClick({R.id.input_button_y,R.id.head_back,R.id.input_button_sumbert})
    public void click(View v){
        switch (v.getId()){
            case R.id.input_button_sumbert:
                trim = alert_y.getText().toString().trim();
                if(vaildCode==null){
                    tip("请获取手机验证码");
                    initTime();
                }else if(trim ==null|| trim.length()==0) {
                    tip("请获输入验证码");
                    initTime();
                }else if(!trim.equals(vaildCode)){
                    tip("输入的验证码不正确");
                    initTime();
                }else {
                    sendRequest(1,"");

                }
                break;
            case R.id.input_button_y:
                phoneN = phone.getText().toString().trim();
                if(phoneN ==null|| phoneN.length()==0){
                    tip("请输入手机号");
                }else {
                    handler.postDelayed(runnable,1000);
                    input_button_y.setClickable(false);
                    sendRequest(0,"");
                }
                break;
            case R.id.head_back:
                finish();
                break;
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
                if(response.optBoolean("data")){
                    tip("修改成功");
                    ConfigManager.instance().getUser().setMobile(phoneN);
                    finish();
                }else {
                    tip(response.optString("msg"));
                    initTime();
                }
            }
        }else {
            tip(response.optString("msg"));
            time=0;
        }
    }

    private void initTime() {
        time=60;
        handler.removeCallbacks(runnable);
        input_button_y.setClickable(true);
        input_button_y.setText("请重新获取验证码");
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
                input_button_y.setText(time+"秒");
                handler.postDelayed(runnable,1000);
            }else {
                input_button_y.setClickable(true);
                input_button_y.setText("请重新获取验证码");
                time=60;
            }
        }
    };
}
