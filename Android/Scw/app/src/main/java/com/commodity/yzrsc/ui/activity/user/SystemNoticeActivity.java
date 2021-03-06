package com.commodity.yzrsc.ui.activity.user;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.Notice;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.adapter.SystemNoticeAdapter;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;
import com.commodity.yzrsc.utils.GsonUtils;

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
 * Created by liyushen on 2017/4/3 22:18
 * 系统消息
 */
public class SystemNoticeActivity  extends BaseActivity {
    @Bind(R.id.xlistView)
    XListView xlistView;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    SystemNoticeAdapter adapter;
    List<Notice> dataList;
    private int pageIndex = 1;
    private int totalPage = 1;
    @Override
    protected int getContentView() {
        return R.layout.activity_system_notice;
    }

    @Override
    protected void initView() {
        xlistView.setPullLoadEnable(true);
        dataList=new ArrayList<>();
        adapter=new SystemNoticeAdapter(this,dataList,R.layout.item_system_notice);
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
                        sendRequest(1, "");
                    }
                },SPKeyManager.delay_time);
            }
        });
    }
    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("sortOrder","1");
            parmMap.put("pageIndex",pageIndex+"");
            parmMap.put("pageSize",""+ SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetSystemNotificationList, parmMap, this);
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
                            Notice data= GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(),Notice.class);
                            dataList.add(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    adapter.notifyDataSetChanged();
                    if (pageIndex >= totalPage) {
                        xlistView.setPullLoadEnable(false);
                    } else {
                        xlistView.setPullLoadEnable(true);
                    }
                }
                if (dataList.size()>0){
                    tv_nodata.setVisibility(View.GONE);
                }else {
                    tv_nodata.setVisibility(View.VISIBLE);
                }

            }else {
                if (resultJson!=null&&!resultJson.optBoolean("success")){
                    tip(resultJson.optString("msg"));
                }
            }
            xlistView.stopLoadMore();
            xlistView.stopRefresh();
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

}
