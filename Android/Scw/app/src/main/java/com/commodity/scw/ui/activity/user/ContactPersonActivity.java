package com.commodity.scw.ui.activity.user;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.PersonInfo;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.ContactPersonAdapter;
import com.commodity.scw.ui.dialog.CommonDialog;
import com.commodity.scw.ui.widget.xlistView.PLA_AdapterView;
import com.commodity.scw.ui.widget.xlistView.XListView;
import com.commodity.scw.utils.CustomToast;
import com.commodity.scw.utils.GsonUtils;

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
 * Created by liyushen on 2017/4/3 21:59
 * 联系人界面
 */
public class ContactPersonActivity  extends BaseActivity {
    @Bind(R.id.xlistView)
    XListView xlistView;
    @Bind(R.id.et_search)
    EditText et_search;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    ContactPersonAdapter adapter;
    List<PersonInfo> dataList;
    private int pageIndex = 1;
    private int totalPage = 1;
    private String keyname="";
    private boolean isSearch=false;
    private int curpostion=-1;
    @Override
    protected int getContentView() {
        return R.layout.activity_contact_person;
    }

    @Override
    protected void initView() {
        dataList=new ArrayList<>();
        adapter=new ContactPersonAdapter(this,dataList,R.layout.item_person_info);
        xlistView.setAdapter(adapter);
        sendRequest(1,"");
    }

    @Override
    protected void initListeners() {
        xlistView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        isSearch=false;
                        sendRequest(1, "");
                        xlistView.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
                    }
                },SPKeyManager.delay_time);
            }

            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex++;
                        isSearch=false;
                        sendRequest(1, "");
                    }
                },SPKeyManager.delay_time);
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                keyname=editable.toString();
                isSearch=true;
                pageIndex = 1;
                sendRequest(1, "");
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            if (!isSearch){
                customLoadding.show();
            }
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("sortOrder","1");
            parmMap.put("pageIndex",pageIndex+"");
            parmMap.put("pageSize",""+ SPKeyManager.pageSize);
            parmMap.put("name",keyname );
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetContactList, parmMap, this);
            httpManager.request();
        }else if (tag == 2) {//删除联系人
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("contactId", dataList.get(curpostion).getImId());
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.DeleteContact, parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag==1){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                if (pageIndex == 1) {
                    dataList.clear();
                }
                totalPage=resultJson.optJSONObject("pageInfo").optInt("totalPage");
                JSONArray dataArray=resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            PersonInfo data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(),PersonInfo.class);
                            data.setReceivedTime(dataArray.getJSONObject(i).optString("createTime"));
                            dataList.add(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (dataList.size()>0){
                        tv_nodata.setVisibility(View.GONE);
                    }else {
                        tv_nodata.setVisibility(View.VISIBLE);
                    }

                    if (pageIndex >= totalPage) {
                        xlistView.setPullLoadEnable(false);
                    } else {
                        xlistView.setPullLoadEnable(true);
                    }
                }
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
            xlistView.stopLoadMore();
            xlistView.stopRefresh();
        }else  if (tag==2){
            JSONObject resultJson= (JSONObject) resultInfo.getResponse();
            if (resultJson!=null&&resultJson.optBoolean("success")){
                CustomToast.showToast("删除成功");
                dataList.remove(curpostion);
                adapter.notifyDataSetChanged();
                if (dataList.size()>0){
                    tv_nodata.setVisibility(View.GONE);
                }else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }
            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    CustomToast.showToast(resultJson.optString("msg"));
                }
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    public void deletePerson(int postion){
        curpostion=postion;
        CommonDialog commonDialog = new CommonDialog(ContactPersonActivity.this);
        commonDialog.show();
        commonDialog.setContext("确定删除？");
        commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
            @Override
            public void clickSubmit() {
                sendRequest(2,"");
            }
        });
    }
}
