package com.commodity.scw.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.model.Banner;
import com.commodity.scw.model.ImgType;
import com.commodity.scw.model.store.TypeChildrenEntity;
import com.commodity.scw.model.store.TypeContextEntity;
import com.commodity.scw.model.store.UploadTypeEntity;
import com.commodity.scw.ui.BaseFragment;
import com.commodity.scw.ui.activity.classify.MSerachActivity;
import com.commodity.scw.ui.activity.classify.TypeActivity;
import com.commodity.scw.ui.activity.user.MessageManagerActivity;
import com.commodity.scw.ui.adapter.TypeContentAdapter;
import com.commodity.scw.ui.adapter.TypeGuideAdapter;
import com.commodity.scw.ui.widget.customview.PageFlipperLayout;
import com.commodity.scw.utils.GsonUtils;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 作者：liyushen on 2017/03/13 16:31
 * 功能：分类Fragment
 */
public class HomeTypeFragment extends BaseFragment {
    @Bind(R.id.type_slideshowView)
    PageFlipperLayout slideshowView;
    @Bind(R.id.guide_listview)
    ListView guideListview;
    @Bind(R.id.content_grid_v)
    GridView content_grid_v;
    @Bind(R.id.info_)
    TextView info_;
    @Bind(R.id.img_info)
    ImageView img_info;
    private List<TypeContextEntity> guidData = new ArrayList<>();
    private List<TypeChildrenEntity> contentData = new ArrayList<>();
    private TypeGuideAdapter typeGuideAdapter;
    private TypeContentAdapter typeContentAdapter;
    private String kindId = "1";
    List<Banner> banners = new ArrayList<>();
    private int guidId = 0;
    private TypeChildrenEntity typeChildrenEntity = new TypeChildrenEntity();

    @Override
    protected int getContentView() {
        return R.layout.fragment_home_type;
    }

    @Override
    protected void initView() {
        inintData();
        sendRequest(0, "");
        //轮播图
        sendRequest(3, "");
        sendRequest(4, "");
        SPKeyManager.messageTipTextViewList.add(info_);
    }

    private void inintData() {
        //展示
        typeContentAdapter = new TypeContentAdapter(mContext, contentData, R.layout.item_type_content, this);
        content_grid_v.setAdapter(typeContentAdapter);
        initKindList(SPManager.instance().getString(SPKeyManager.GetGoodsKindListCacheStr));
    }

    @Override
    protected void initListeners() {
        //点击轮播图


        //导航栏监听
        guideListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeGuideAdapter.setClickIndex(i);
                guidId = i;
                //TODO 更新内容
                contentData.clear();
                typeChildrenEntity.setId(guidData.get(i).getId());
                typeChildrenEntity.setImage(guidData.get(i).getImage());
                typeChildrenEntity.setName("全部");
                contentData.add(typeChildrenEntity);
                if (i == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constanct.KINDID,0);
                    jumpActivity(TypeActivity.class,bundle);
                    typeGuideAdapter.setClickIndex(1);
                    contentData.addAll(guidData.get(1).getChildren());

                } else {
                    contentData.addAll(guidData.get(i).getChildren());
                }
                typeGuideAdapter.notifyDataSetChanged();
                typeContentAdapter.notifyDataSetChanged();
            }
        });
        //内容栏
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (customLoadding!=null&&!customLoadding.isShowing()){
            customLoadding.show();
        }
        if (tag == 0) {
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetGoodsKindList, null, this);
            httpManager.request();
        } else if (tag == 3) {
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("adverSource", 4 + "");
            parmMap.put("num", 10 + "");
            HttpManager manager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetAdvers, parmMap, this);
            manager.request();
        } else if (tag == 4) {
            Map<String, String> parmMap = new HashMap<String, String>();
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetUnreadNotificationCounts, parmMap, this);
            httpManager.request();
        }

    }


    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject response = (JSONObject) resultInfo.getResponse();
        if (response != null) {
            if (response.optBoolean("success")) {
                if (tag == 0) {
                    String s = response.toString();
                    if (!StringUtils.isEmpty(s)) {
                        SPManager.instance().setString(SPKeyManager.GetGoodsKindListCacheStr, s);
                        initKindList(s);
                    }
                } else if (tag == 3) {
                    JSONArray dataArray = response.optJSONArray("data");
                    banners.clear();
                    if (dataArray != null) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            try {
                                Banner data = GsonUtils.jsonToObject(dataArray.getJSONObject(i).toString(), Banner.class);
                                banners.add(data);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    initBannar(banners);
                } else if (tag == 4) {
                    JSONObject resultJson = (JSONObject) resultInfo.getResponse();
                    if (resultJson != null && resultJson.optBoolean("success")) {
                        JSONObject dataJson = resultJson.optJSONObject("data");
                        int count = dataJson.optInt("systemCount") + dataJson.optInt("evaluationCount") + dataJson.optInt("followCount");
                        if (count > 0) {
                            info_.setText(count + "");
                            info_.setVisibility(View.VISIBLE);
                        } else {
                            info_.setVisibility(View.GONE);
                        }

                    } else {
                        if (resultJson != null && !resultJson.optBoolean("success")) {
                            tip(resultJson.optString("msg"));
                            info_.setVisibility(View.GONE);
                        }
                    }
                }

            }
        } else {
            tip("获取分类失败");
        }
    }

    //加载分类信息
    private void initKindList(String s) {
        guidData.clear();
        contentData.clear();

        TypeContextEntity typeContextEntity = new TypeContextEntity();
        typeContextEntity.setName("全部");
        guidData.add(typeContextEntity);


        if (!StringUtils.isEmpty(s)){
            UploadTypeEntity uploadTypeEntity = GsonUtils.jsonToObject(s, UploadTypeEntity.class);
            TypeChildrenEntity typeChildrenEntity = new TypeChildrenEntity();
            typeChildrenEntity.setId(0);
            typeChildrenEntity.setImage(uploadTypeEntity.getData().get(0).getImage());
            typeChildrenEntity.setName("全部");
            contentData.add(typeChildrenEntity);
            guidData.addAll(uploadTypeEntity.getData());
            contentData.addAll(guidData.get(1).getChildren());
        }
        //导航
        if (typeGuideAdapter == null) {
            typeGuideAdapter = new TypeGuideAdapter(mContext, guidData, R.layout.item_type_guide);
            guideListview.setAdapter(typeGuideAdapter);
        } else {
            typeGuideAdapter.notifyDataSetChanged();
        }
        typeGuideAdapter.setClickIndex(1);
    }

    //初始化banner数据
    private void initBannar(List<Banner> bannerList) {
        if (bannerList != null && bannerList.size() > 0) {
            slideshowView.setBannerList(bannerList);
            slideshowView.setVisibility(View.VISIBLE);
        } else {
            slideshowView.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }

    @OnClick({R.id.serach, R.id.info_, R.id.img_info})
    public void click(View v) {
        switch (v.getId()) {
            case R.id.serach:
                Bundle bundle = new Bundle();
                bundle.putInt("a",0);
                jumpActivity(MSerachActivity.class,bundle);
                break;
            case R.id.info_:
                jumpActivity(MessageManagerActivity.class);
                break;
            case R.id.img_info:
                jumpActivity(MessageManagerActivity.class);
                break;
        }
    }
}
