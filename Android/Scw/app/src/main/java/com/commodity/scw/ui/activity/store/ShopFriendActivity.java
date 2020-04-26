package com.commodity.scw.ui.activity.store;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.ui.BaseActivity;
import com.qiniu.android.utils.StringUtils;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yohoutils.StringUtil;

/**
 *店铺合作
 * Created by yangxuqiang on 2017/4/6.
 */

public class ShopFriendActivity extends BaseActivity {
    @Bind(R.id.shop_edit)
    EditText shop_edit;
    @Bind(R.id.head_text_right)
    TextView right;
    @Bind(R.id.head_title)
    TextView title;
    private String trim;

    @Override
    protected int getContentView() {
        return R.layout.activity_shopfriend;
    }

    @Override
    protected void initView() {
        title.setText("店铺招商信息");
        right.setText("保存");
        String shopDesc = SPManager.instance().getString(Constanct.shopDesc);
        if(shopDesc==null){
            shop_edit.setHint("请输入内容");
        }else {
            shop_edit.setText(shopDesc);
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
            case R.id.head_text_right://保存
                trim = shop_edit.getText().toString().trim();
                if(StringUtil.isEmpty(trim)){
                    tip("请输入内容");
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
//        map.put("name",name);
        map.put("description",trim);
        new HttpManager(tag, HttpMothed.POST, IRequestConst.RequestMethod.EditShopInfo,map,this).request();
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if(response.optBoolean("success")){
            if(response.optBoolean("data")){
                SPManager.instance().setString(Constanct.shopDesc,trim);
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
