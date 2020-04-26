package com.commodity.scw.ui.activity.user;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.HomeFragmentActivity;
import com.commodity.scw.ui.widget.imageview.CircleImageView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/3/30 20:37
 * 找回密码
 */
public class ForgetPwdActivity  extends BaseActivity {
    @Bind(R.id.btn_next)
    Button btn_next;

    @Bind(R.id.ll_regist1)
    LinearLayout ll_regist1;
    @Bind(R.id.ll_regist2)
    LinearLayout ll_regist2;
    @Bind(R.id.et_phone)
    EditText et_phone;

    @Bind(R.id.et_yanzhengma)
    EditText et_yanzhengma;
    @Bind(R.id.tv_yanzhengma)
    TextView tv_yanzhengma;
    @Bind(R.id.et_pwd)
    EditText et_pwd;

    @Override
    protected int getContentView() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView() {
        setShowStatus(1);
    }

    @Override
    protected void initListeners() {

        tv_yanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(1,"");
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_next.getText().toString().equals("获取验证码")){
                    if (et_phone.getText().toString().isEmpty()||et_phone.getText().toString().length()<11){
                        tip("请输入正确的手机号！");
                        return;
                    }
                    sendRequest(1,"");
                }else if (btn_next.getText().toString().equals("完成")){
                    if (et_yanzhengma.getText().toString().isEmpty()){
                        tip("请输入验证码！");
                        return;
                    }
                    if (et_pwd.getText().toString().isEmpty()){
                        tip("请输入密码！");
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
        if (customLoadding!=null&&!customLoadding.isShowing()){
            customLoadding.show();
        }
        if (tag == 1) {//获取验证码
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile",et_phone.getText().toString());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.GetVaildCodeRegisted, parmMap, this);
            httpManager.request();
        }else if (tag == 2) {//修改密码
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile",et_phone.getText().toString());
            parmMap.put("vaildCode",et_yanzhengma.getText().toString());
            parmMap.put("passWord",et_pwd.getText().toString());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.ChangePassword, parmMap, this);
            httpManager.request();
        }
        else if (tag == 3) {//账号是否已注册
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("mobile",et_phone.getText().toString());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.MobieIsRegister, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            setShowStatus(2);
        }else if (tag==2){
            JSONObject jsonObject= (JSONObject) resultInfo.getResponse();
            if (jsonObject!=null){
                if (jsonObject.optString("success").equals("true")){
                    tip("密码修改成功");
                    finish();
                }
            }
        }else if (tag==3){
            JSONObject jsonObject= (JSONObject) resultInfo.getResponse();
            if (jsonObject!=null){
                if (jsonObject.optString("success").equals("true")){
                    tip("ddd");
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
            btn_next.setText("获取验证码");
        }if (status==2){
            ll_regist1.setVisibility(View.GONE);
            ll_regist2.setVisibility(View.VISIBLE);
            btn_next.setText("完成");
        }
    }
}
