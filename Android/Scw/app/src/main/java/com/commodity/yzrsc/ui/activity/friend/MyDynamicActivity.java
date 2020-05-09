package com.commodity.yzrsc.ui.activity.friend;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.SPKeyManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.adapter.DynamicListAdapter;
import com.commodity.yzrsc.ui.adapter.MyDynamicListAdapter;
import com.commodity.yzrsc.ui.fragment.HomeFriendFragment;
import com.commodity.yzrsc.ui.widget.textview.CenterDrawableTextView;
import com.commodity.yzrsc.ui.widget.xlistView.XListView;
import com.commodity.yzrsc.utils.GsonUtils;
import com.commodity.yzrsc.view.PopWinShare;
import com.squareup.otto.Subscribe;
import com.yixia.camera.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.utilities.RongUtils;

public class MyDynamicActivity extends BaseActivity {
    List<DynamicAllListModel> listModels = new ArrayList<>();
    DynamicAllListModel data;
    MyDynamicListAdapter dynamicListAdapter;
    @Bind(R.id.head_back)
    ImageView headBack;
    @Bind(R.id.iv_release_dynamic)
    AppCompatImageView ivReleaseDynamic;
    @Bind(R.id.tv_nodata)
    CenterDrawableTextView tvNodata;
    @Bind(R.id.xlist_dynamic)
    XListView xlistDynamic;

    private int pageIndex = 1;
    private int totalPage = 1;
    private String memberId = "0";
    private String minId = "0";//页码的最小id
    PopWinShare popWinShare;
    private String catalogId;

    @Override
    protected int getContentView() {
        return R.layout.activity_my_dynamic2;
    }

    @Override
    protected void initView() {
        Bundle extras = getIntent().getExtras();
        memberId = extras.getString("dynamicId");
        catalogId = extras.getString("TypeId");
        xlistDynamic.setPullLoadEnable(true);
        dynamicListAdapter = new MyDynamicListAdapter(this, listModels);
        xlistDynamic.setAdapter(dynamicListAdapter);
        sendRequest(1, "");
    }

    @Override
    protected void initListeners() {
        xlistDynamic.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        minId = "0";
                        sendRequest(1, "");
                        xlistDynamic.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
                    }
                }, SPKeyManager.delay_time);
            }

            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex++;
                        minId = listModels.get(listModels.size() - 1).getId() + "";
                        sendRequest(1, "");

                    }
                }, SPKeyManager.delay_time);
            }
        });
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {
            customLoadding.show();
            if (  pageIndex ==1){
                minId = "0";
            }
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId", memberId);
            parmMap.put("catalogId", catalogId);
            parmMap.put("minId", minId);
            parmMap.put("pageSize", "" + SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetDynamicList, parmMap, this);
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
                    listModels.clear();
                }
//                totalPage = resultJson.optJSONObject("pageInfo").optInt("totalPage");
                JSONArray dataArray = resultJson.optJSONArray("data");
                if (dataArray != null) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        try {
                            data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), DynamicAllListModel.class);
                            if (data!=null){
                                listModels.add(data);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (listModels.size() < SPKeyManager.pageSize) {
                        xlistDynamic.setPullLoadEnable(false);
                    } else {
                        xlistDynamic.setPullLoadEnable(true);
                    }
                }
                dynamicListAdapter.notifyDataSetChanged();

                if (listModels.size() > 0) {
                    tvNodata.setVisibility(View.GONE);
                } else {
                    tvNodata.setVisibility(View.VISIBLE);
                }

            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
            xlistDynamic.stopLoadMore();
            xlistDynamic.stopRefresh();
        }

    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }
    //获取传递过来信息进行相关操作
    @Subscribe
    public void NotifyChangedView(Event.NotifyChangedView event) {
        if (event.getDataObject().equals("MyDynamicActivity")){
            Log.d("MyDynamicActivity","传递过来的数据"+event.getDataObject());
            sendRequest(1, "");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
        BusProvider.disposeInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.head_back, R.id.iv_release_dynamic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_back:
                finish();
                break;
            case R.id.iv_release_dynamic:
                if (popWinShare == null) {
                    //自定义的单击事件
                    MyDynamicActivity.OnClickLintener paramOnClickListener = new MyDynamicActivity.OnClickLintener();
                    popWinShare = new PopWinShare(MyDynamicActivity.this, paramOnClickListener, RongUtils.dip2px(110), RongUtils.dip2px(84), 1);
                    //监听窗口的焦点事件，点击窗口外面则取消显示
                    popWinShare.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                popWinShare.dismiss();
                            }
                        }
                    });
                }
//设置默认获取焦点
                popWinShare.setFocusable(true);
//以某个控件的x和y的偏移量位置开始显示窗口
                popWinShare.showAsDropDown(ivReleaseDynamic, -175, 0);
//如果窗口存在，则更新
                popWinShare.update();
                break;
        }
    }

    class OnClickLintener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            popWinShare.dismiss();

            switch (v.getId()) {

                case R.id.pic:
//                    Bundle bundle=new Bundle();
//                    bundle.putString("userDynamicCatalog_Id",typeModel.get(i).getId());

                    jumpActivity(PicDynamicActivity.class);
                    break;
                case R.id.video:
                    jumpActivity(VideoDynamicActivity.class);
                    break;

                default:
                    break;
            }

        }
    }

}
