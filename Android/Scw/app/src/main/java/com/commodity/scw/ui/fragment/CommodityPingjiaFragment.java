package com.commodity.scw.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.ui.BaseFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/4/8 11:30
 * 商品评价
 */
public class CommodityPingjiaFragment  extends BaseFragment {
    @Bind(R.id.ll_content)
    LinearLayout ll_content;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;

    private int pageIndex = 1;
    private int totalPage = 1;
    String goodsSaleId="";
    @Override
    protected int getContentView() {
        return R.layout.view_commodity_pingjia;
    }

    @Override
    protected void initView() {
        goodsSaleId = getActivity().getIntent().getStringExtra("goodsSaleId");
        ll_content.setMinimumHeight(MainApplication.SCREEN_H-(MainApplication.SCREEN_W/3));
        sendRequest(1,"");
    }

    @Override
    protected void initListeners() {

    }


    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("goodsId",goodsSaleId);
            parmMap.put("sortOrder","1");
            parmMap.put("pageIndex",pageIndex+"");
            parmMap.put("pageSize",""+ SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetEvaluationList, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                JSONArray dataArray=resultJson.optJSONArray("data");
                totalPage=resultJson.optJSONObject("pageInfo").optInt("totalPage");
                if (dataArray != null&&dataArray.length()>0) {
                    ll_content.removeAllViews();
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            ll_content.addView(getItemView(dataArray.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }
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

    //每一个子View工具方法
    private View getItemView(JSONObject jsonObject) {
        View view = View.inflate(mContext, R.layout.item_commodity_pingjia, null);
        TextView tv_text1= (TextView) view.findViewById(R.id.tv_text1);
        TextView tv_text2= (TextView) view.findViewById(R.id.tv_text2);
        TextView tv_text3= (TextView) view.findViewById(R.id.tv_text3);
        tv_text1.setText(jsonObject.optString("buyer"));
        tv_text2.setText(jsonObject.optString("createDate").length()>9?jsonObject.optString("createDate").substring(0,10):"");
        tv_text3.setText(jsonObject.optString("description"));
        return view;
    }
}
