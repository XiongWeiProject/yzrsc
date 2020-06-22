package com.commodity.scw.ui.activity.store;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.ui.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 * 店铺名称
 * Created by yangxuqiang on 2017/4/6.
 */

public class SettingShopName extends BaseActivity {
    @Bind(R.id.head_title)
    TextView title;
    @Bind(R.id.head_text_right)
    TextView head_text_right;
    @Bind(R.id.setting_shop_name)
    EditText setting_shop_name;
    private String name;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting_shop_name;
    }

    @Override
    protected void initView() {
        title.setText("修改店铺名称");
        head_text_right.setText("保存");
        String shopname = SPManager.instance().getString(Constanct.shopDesc);
        if(shopname==null){
            setting_shop_name.setHint("请输入...");
        }else {
            setting_shop_name.setText(shopname);
        }
    }

    @Override
    protected void initListeners() {

    }
    @OnClick({R.id.head_back,R.id.head_text_right})
    public void click(View v){
        switch (v.getId()){
            case R.id.head_back:
                finish();
                break;
            case R.id.head_text_right:
                name = setting_shop_name.getText().toString().trim();
                if(StringUtil.isEmpty(name)){
                    tip("请输入店铺名称");
                }else {
                    sendRequest(0,"");
                }
                break;
        }
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        customLoadding.show();
        super.sendRequest(tag, params);
        HashMap<String, String> map = new HashMap<>();
        map.put("name",name);
//        map.put("description","desc");
        new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.EditShopName,map,this).request();
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(response.optBoolean("data")){
                SPManager.instance().setString(Constanct.shopName,name);
                tip("设置成功");
                finish();
            }else {
                tip(response.optString("msg"));
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
