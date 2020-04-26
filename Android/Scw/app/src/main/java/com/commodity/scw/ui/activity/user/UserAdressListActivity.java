package com.commodity.scw.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManageNew;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.AdressDetail;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.commodity.AddUserAdressActivity;
import com.commodity.scw.ui.adapter.AdressListAdaper;
import com.commodity.scw.ui.dialog.CommonDialog;
import com.commodity.scw.ui.widget.xlistView.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/4/23 15:08
 * 收货地址列表
 */
public class UserAdressListActivity extends BaseActivity {
    @Bind(R.id.tv_rightbtn)
    TextView tv_rightbtn;
    @Bind(R.id.xlistView)
    XListView xlistView;
    private int pageIndex = 1;
    private int totalPage = 1;
    AdressListAdaper adaper;
    List<AdressDetail> dataList;
    private  int curpostion=-1;
    @Override
    protected int getContentView() {
        return R.layout.activity_adress_list;
    }

    @Override
    protected void initView() {
        xlistView.setPullLoadEnable(true);
        dataList = new ArrayList<>();
        adaper = new AdressListAdaper(this, dataList, R.layout.item_address_detail);
        xlistView.setAdapter(adaper);
        sendRequest(1, "");
    }

    @Override
    protected void initListeners() {
        tv_rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivityForResult(1001,AddUserAdressActivity.class);
            }
        });
        xlistView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        sendRequest(1, "");
                        xlistView.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex++;
                        sendRequest(1, "");
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        customLoadding.show();
        if (tag == 1) {//获取用户收货地址列表
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId", ConfigManager.instance().getUser().getId());
            parmMap.put("pageIndex", "1");
            parmMap.put("pageSize", "100");
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetAddressInfoList, parmMap, this);
            httpManager.request();
        }else if (tag==2){//删除
            Map<String, Object> parmMap = new HashMap<String, Object>();
            JSONArray array=new JSONArray();
            array.put(dataList.get(curpostion).getId());
            parmMap.put("addressInfoIds",array);
            parmMap.put("memberId", ConfigManager.instance().getUser().getId());
            HttpManageNew httpManager = new HttpManageNew(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.CancelAddressInfos, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                if (pageIndex == 1) {
                    dataList.clear();
                }
                totalPage = resultJson.optJSONObject("pageInfo").optInt("totalPage");
                JSONArray dataArray = resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        AdressDetail data = new AdressDetail();
                        try {
                            data.setId(dataArray.getJSONObject(i).optString("id"));
                            data.setAddressDetail(dataArray.getJSONObject(i).optString("addressDetail"));
                            data.setCity(dataArray.getJSONObject(i).optString("city"));
                            data.setDistrict(dataArray.getJSONObject(i).optString("district"));
                            data.setMobile(dataArray.getJSONObject(i).optString("mobile"));
                            data.setProvince(dataArray.getJSONObject(i).optString("province"));
                            data.setReceiver(dataArray.getJSONObject(i).optString("receiver"));
                            data.setProvinceId(dataArray.getJSONObject(i).optString("provinceId"));
                            data.setCityId(dataArray.getJSONObject(i).optString("cityId"));
                            data.setDistrictId(dataArray.getJSONObject(i).optString("districtId"));
                            data.setSelect(false);
                            dataList.add(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (dataList.size()>0){
                        curpostion=0;
                        dataList.get(0).setSelect(true);
                    }
                    adaper.notifyDataSetChanged();
                    if (pageIndex >= totalPage) {
                        xlistView.setPullLoadEnable(false);
                    } else {
                        xlistView.setPullLoadEnable(true);
                    }
                }
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
            xlistView.stopLoadMore();
            xlistView.stopRefresh();
        }else if (tag==2){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                 tip("删除成功");
                dataList.remove(curpostion);
                adaper.notifyDataSetChanged();
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
        }

    }
    public void selectArdress(int postion){
        curpostion=postion;
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setSelect(false);
        }
        dataList.get(postion).setSelect(true);
        adaper.notifyDataSetChanged();
    }
    public void editArdress(int postion){
        curpostion=postion;
        Bundle bundle=new Bundle();
        bundle.putSerializable("AdressDetail",dataList.get(curpostion));
        jumpActivityForResult(1001,AddUserAdressActivity.class,bundle);
    }

    public void deleteArdress(int postion){
        curpostion=postion;
        CommonDialog comm=new CommonDialog(this);
        comm.show();
        comm.setContext("确认删除？");
        comm.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
            @Override
            public void clickSubmit() {
                sendRequest(2,"");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==1001){
                pageIndex = 1;
                sendRequest(1, "");
            }
        }
    }

    @Override
    public void finish() {
        Intent intent =new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable("AdressDetail", dataList.get(curpostion));
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        super.finish();
    }
}

