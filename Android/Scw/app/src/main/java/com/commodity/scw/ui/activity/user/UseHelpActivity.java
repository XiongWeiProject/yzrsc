package com.commodity.scw.ui.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.model.UseHelp;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedHashTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/4/2 11:50
 * 使用帮助
 */
public class UseHelpActivity extends BaseActivity {
    @Bind(R.id.ll_function)
    LinearLayout ll_function;
    List<UseHelp> useHelpList = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.activity_use_help;
    }

    @Override
    protected void initView() {
        sendRequest(1,"");
    }

    @Override
    protected void initListeners() {

    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {//获取使用帮助信息
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetGuides, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                useHelpList.clear();
                JSONArray  dataJson=resultJson.optJSONArray("data");
                for (int i = 0; i < dataJson.length(); i++) {
                    try {
                        UseHelp userInfo=  GsonUtils.jsonToObject(dataJson.getJSONObject(i).toString(),UseHelp.class);
                        useHelpList.add(userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initDataView();
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
        }
    }
    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
    private void initDataView() {
        for (int i = 0; i < useHelpList.size(); i++) {
            ll_function.addView(getItemView(useHelpList.get(i), false, false));
            if (useHelpList.get(i).getGuides()!=null){
                for (int j = 0; j < useHelpList.get(i).getGuides().size(); j++) {
                    if (j==useHelpList.get(i).getGuides().size()-1){
                        ll_function.addView(getItemView(useHelpList.get(i).getGuides().get(j), true, false));
                    }else {
                        ll_function.addView(getItemView(useHelpList.get(i).getGuides().get(j), true, true));
                    }

                }
            }
        }
//        ll_function.addView(getItemView("交易介绍", false, false));
//        ll_function.addView(getItemView("交易流程", true, true));
//        ll_function.addView(getItemView("如何购买商品", true, false));
//        ll_function.addView(getItemView("交易提现", false, false));
//        ll_function.addView(getItemView("提现规则", true, false));
//        ll_function.addView(getItemView("等级规则", false, false));
//        ll_function.addView(getItemView("买家等级", true, true));
//        ll_function.addView(getItemView("卖家等级", true, false));
    }

    //每一个子View工具方法
    private View getItemView(final Object object, final boolean showIco , final boolean showLine) {
        View view = View.inflate(mContext, R.layout.item_use_help, null);
        TextView tv_text1= (TextView) view.findViewById(R.id.tv_text1);
        View view_ico2=view.findViewById(R.id.view_ico2);
        View view_line=view.findViewById(R.id.view_line);
        if (object instanceof UseHelp){
            tv_text1.setText(((UseHelp) object).getName());
        }else if (object instanceof UseHelp.GuidesBean){
            tv_text1.setText(((UseHelp.GuidesBean) object).getTitle());
        }
        if (!showIco){
            view.setBackgroundColor(getResources().getColor(R.color.type_FAFAFA));
            view_ico2.setVisibility(View.GONE);
        }else {
            view_ico2.setVisibility(View.VISIBLE);
        }
        if (showLine){
            view_line.setVisibility(View.VISIBLE);
        }else {
            view_line.setVisibility(View.GONE);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showIco){
                    Bundle bundle =new Bundle();
                    bundle.putString("title",((UseHelp.GuidesBean) object).getTitle());
                    bundle.putString("content",((UseHelp.GuidesBean) object).getDescription());
                    jumpActivity(UseHelpDetailActivity.class,bundle);
                }
            }
        });
        return view;
    }
}
