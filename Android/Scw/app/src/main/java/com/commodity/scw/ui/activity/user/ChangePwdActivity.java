package com.commodity.scw.ui.activity.user;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.model.UserInfo;
import com.commodity.scw.ui.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/4/3 11:24
 * 修改密码
 */
public class ChangePwdActivity  extends BaseActivity {
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_old_pwd)
    EditText et_old_pwd;
    @Bind(R.id.et_pwd)
    EditText et_pwd;
    @Bind(R.id.et_pwd2)
    EditText et_pwd2;

    @Bind(R.id.ll_regist1)
    LinearLayout ll_regist1;
    @Bind(R.id.ll_regist2)
    LinearLayout ll_regist2;
    private String vaildCode="";
    @Override
    protected int getContentView() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void initView() {
        setShowStatus(2);
    }

    @Override
    protected void initListeners() {

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_next.getText().equals("下一步")){
                    if (et_phone.getText().toString().isEmpty()){
                        tip("请输入你的手机号码！");
                        return;
                    }
                    sendRequest(1,"");
                }else {
                    if (et_old_pwd.getText().toString().isEmpty()){
                        tip("请输入你的旧密码！");
                        return;
                    } if (!et_old_pwd.getText().toString().equals(SPManager.instance().getString(SPKeyManager.USERINFO_pwd))){
                        tip("旧密码不正确！");
                        return;
                    }else if (et_pwd.getText().toString().isEmpty()){
                        tip("请输入新密码！");
                        return;
                    }else if (et_pwd2.getText().toString().isEmpty()){
                        tip("请再次输入新密码！");
                        return;
                    }else if (!et_pwd2.getText().toString().equals(et_pwd.getText().toString())){
                        tip("密码不一致！");
                        return;
                    }
                    sendRequest(2,"");
                }
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {//获取验证码
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile",et_phone.getText().toString());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.GetVaildCodeRegisted, parmMap, this);
            httpManager.request();
        }else if (tag == 2) {//修改密码
            Map<String, String> parmMap = new HashMap<String, String>();
//            parmMap.put("mobile",et_phone.getText().toString());
//            parmMap.put("vaildCode",vaildCode);
//            parmMap.put("passWord",et_pwd.getText().toString());
            parmMap.put("oldPassword",et_old_pwd.getText().toString());
            parmMap.put("newPassword",et_pwd.getText().toString());
            parmMap.put("confirmPassword",et_pwd2.getText().toString());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.ChangeInfoPassword, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject jsonObject= (JSONObject) resultInfo.getResponse();
            if (jsonObject!=null){
                if (jsonObject.optBoolean("success")){

                }
                if(jsonObject.optJSONObject("data")==null){
                    tip(jsonObject.optString("msg"));
                }else {
                    vaildCode=jsonObject.optJSONObject("data").optString("vaildCode");
                    setShowStatus(2);
                }

            }

        }else if (tag==2){
            JSONObject jsonObject= (JSONObject) resultInfo.getResponse();
            if (jsonObject!=null){
                if (jsonObject.optString("success").equals("true")&&jsonObject.optString("data").equals("true")){
                    tip("密码修改成功");
                    SPManager.instance().setString(SPKeyManager.USERINFO_pwd, et_pwd.getText().toString());
                    finish();
                }else {
                    if (jsonObject!=null){
                        tip(jsonObject.optString("msg"));
                    }
                }
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
    }


    //显示注册的几个状态
    private void setShowStatus(int status){
        if (status==1){
            ll_regist1.setVisibility(View.VISIBLE);
            ll_regist2.setVisibility(View.GONE);
            btn_next.setText("下一步");
        }if (status==2){
            ll_regist1.setVisibility(View.GONE);
            ll_regist2.setVisibility(View.VISIBLE);
            btn_next.setText("完成");
        }
    }
}
