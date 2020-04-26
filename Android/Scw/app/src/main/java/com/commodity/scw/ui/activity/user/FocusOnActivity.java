package com.commodity.scw.ui.activity.user;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.model.Focuser;
import com.commodity.scw.model.Notice;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.adapter.FocusOnAdapter;
import com.commodity.scw.ui.adapter.SystemNoticeAdapter;
import com.commodity.scw.ui.widget.xlistView.XListView;
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
 * Created by liyushen on 2017/4/3 22:31
 * 关注
 */
public class FocusOnActivity  extends BaseActivity {
    @Bind(R.id.xlistView)
    XListView xlistView;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    FocusOnAdapter adapter;
    List<Focuser> dataList;
    private int pageIndex = 1;
    private int totalPage = 1;
    @Override
    protected int getContentView() {
        return R.layout.activity_focus_on;
    }

    @Override
    protected void initView() {
        xlistView.setPullLoadEnable(true);
        dataList=new ArrayList<>();
        adapter=new FocusOnAdapter(this,dataList,R.layout.item_focus_on);
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
                    IRequestConst.RequestMethod.GetFollowNotificationList, parmMap, this);
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
                            Focuser data= GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(),Focuser.class);
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
