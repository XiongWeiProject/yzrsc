package com.commodity.scw.ui.activity.classify;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.model.ImgType;
import com.commodity.scw.model.MarketGoods;
import com.commodity.scw.model.store.TypeChildrenEntity;
import com.commodity.scw.model.store.TypeContextEntity;
import com.commodity.scw.model.store.UploadTypeEntity;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.commodity.CommodityDetailActivity;
import com.commodity.scw.ui.adapter.ItemResultAdapter;
import com.commodity.scw.ui.adapter.MarketGoodsAdapter;
import com.commodity.scw.ui.adapter.PriceGridAdapter;
import com.commodity.scw.ui.adapter.TypeGuideAdapter;
import com.commodity.scw.ui.adapter.TypePaixuAdapter;
import com.commodity.scw.ui.dialog.SearchDialog;
import com.commodity.scw.ui.widget.xlistView.PLA_AdapterView;
import com.commodity.scw.ui.widget.xlistView.XListView;
import com.commodity.scw.utils.DateUtil;
import com.commodity.scw.utils.GsonUtils;
import com.ta.utdid2.android.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 商品列表
 * Created by yangxuqiang on 2017/3/21.
 */

public class TypeActivity extends BaseActivity {
    @Bind(R.id.type_back)
    LinearLayout back;
    @Bind(R.id.activity_linear1)
    LinearLayout linearLayout1;
    @Bind(R.id.activity_linear2)
    LinearLayout linearLayout2;
    @Bind(R.id.activity_linear3)
    LinearLayout linearLayout3;
    @Bind(R.id.activity_image1)
    ImageView button1;
    @Bind(R.id.activity_image2)
    ImageView button2;
    @Bind(R.id.activity_image3)
    ImageView button3;
    @Bind(R.id.activity_titale1)
    TextView titale1;
    @Bind(R.id.activity_titale2)
    TextView titale2;
    @Bind(R.id.activity_titale3)
    TextView titale3;
    @Bind(R.id.activity_xlistview)
    XListView xlistView;

    private boolean clickbutton1;
    private boolean clickbutton2;
    private boolean clickbutton3;
    //用于显示条目
    @Bind(R.id.type_linearlayouy)
    LinearLayout itemLinear;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    /**
     * 分类控制
     */
    @Bind(R.id.linear_fenli)
    LinearLayout fenlei;
    @Bind(R.id.item_type_type)
    ListView item_type_type;

    @Bind(R.id.list_item_paixu)
    ListView list_item_paixu;
    /**
     * 排序控制
     */
    @Bind(R.id.linear_paixu)
    LinearLayout paixu;
    @Bind(R.id.item_type_result)
    ListView item_type_result;
    /**
     * 价格控制
     */
    @Bind(R.id.linear_jiage)
    LinearLayout jiage;
    @Bind(R.id.type_item_price)
    GridView type_item_price;

    @Bind(R.id.type_popup_zhong)
    FrameLayout type_popup_zhong;
    /**
     * 分类
     */
    private List<TypeContextEntity> guidData = new ArrayList();
    /**
     * 价格
     */
    private List<String> priceData = new ArrayList<>();
    /**
     * 分离右侧的
     */
    private List<TypeChildrenEntity> resultData = new ArrayList<>();
    /**
     * 排序
     */
    private List<String> paixuData = new ArrayList<>();
    /**
     * 回现表示
     */
    private int pricePostion;
    private int paixuPostion;
    private int guidPostion;
    private int resultPostion;
    private TypePaixuAdapter typePaixuAdapter;
    private ItemResultAdapter itemResultAdapter;
    private TypeGuideAdapter itemTypeAdapter;
    private PriceGridAdapter priceGridAdapter;

    private int kindId;
    private String priceRange = "0";
    private int sortOrder = 0;

    MarketGoodsAdapter marketGoodsAdapter;
    List<MarketGoods> marketGoodsList;
    SearchDialog searchDialog;
    private String keywords = "";
    private int pageIndex = 1;
    private int totalPage = 1;

    @Override
    protected int getContentView() {
        return R.layout.activity_type;
    }

    @Override
    protected void initView() {
        kindId = getIntent().getExtras().getInt(Constanct.KINDID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        priceData.add("不限");
        priceData.add("1000以下");
        priceData.add("1001-5000");
        priceData.add("5001-1万");
        priceData.add("1万-5万");
        priceData.add("5万-10万");
        priceData.add("10万以上");

        TypeContextEntity typeContextEntity = new TypeContextEntity();
        typeContextEntity.setName("全部");
        guidData.add(typeContextEntity);


        paixuData.add("智能排序");
        paixuData.add("最新");
        paixuData.add("价格从低到高");
        paixuData.add("价格从高到底");

        sendRequest(0, "");
        sendRequest(1, "");
    }

    @Override
    protected void initListeners() {

        //分类显示内容
        marketGoodsList=new ArrayList<>();
        marketGoodsAdapter = new MarketGoodsAdapter(this, marketGoodsList, R.layout.item_market_goods);
        xlistView.setAdapter(marketGoodsAdapter);
        searchDialog=new SearchDialog(this,marketGoodsList,marketGoodsAdapter);
        xlistView.setPullLoadEnable(true);
        xlistView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        sendRequest(0, "");
                        // sendRequest(3,"");
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
                        sendRequest(0, "");
                    }
                },SPKeyManager.delay_time);
            }
        });
        xlistView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("goodsSaleId", String.valueOf(marketGoodsList.get(position - 1).getId()));
                jumpActivity(CommodityDetailActivity.class, bundle);
            }
        });

        searchDialog.setOnSearchListener(new  SearchDialog.OnSearchListener() {
            @Override
            public void doSearch(String search) {
                keywords=search;
                pageIndex = 1;
                sendRequest(0, "");
            }
        });
        searchDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pageIndex = 1;
                sendRequest(0, "");
                xlistView.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
            }
        });

        //分类
        itemTypeAdapter = new TypeGuideAdapter(mContext, guidData, R.layout.item_type_guide);
        item_type_type.setAdapter(itemTypeAdapter);
        item_type_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemTypeAdapter.setClickIndex(i);
                guidPostion = i;
                resultData.clear();
                TypeChildrenEntity en=new TypeChildrenEntity();
                en.setName("全部");
                if (guidPostion == 0) {
                    en.setId(0);
                    resultData.add(en);
                    for (int j = 1; j < guidData.size(); j++) {
                        List<TypeChildrenEntity> children = guidData.get(j).getChildren();
                        if (children != null && children.size() != 0)
                            resultData.addAll(children);
                    }
                } else {
                    en.setId(guidData.get(i).getId());
                    resultData.add(en);
                    resultData.addAll(guidData.get(i).getChildren());
                }
                itemResultAdapter.notifyDataSetChanged();

            }
        });
        itemTypeAdapter.setClickIndex(guidPostion);
        itemResultAdapter = new ItemResultAdapter(mContext, resultData, R.layout.item_type_result);
        item_type_result.setAdapter(itemResultAdapter);
        item_type_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemResultAdapter.setClickPosition(i, null);
                resultPostion = i;
                type_popup_zhong.setVisibility(View.GONE);

                kindId = resultData.get(i).getId();

                pageIndex = 1;
//                sendRequest(1, "");
                initButton();
                goneView();
                type_popup_zhong.setVisibility(View.GONE);
                titale1.setText(resultData.get(i).getName());
                refreshView();
            }
        });
        itemResultAdapter.setClickPosition(resultPostion, null);

        //排序
        typePaixuAdapter = new TypePaixuAdapter(this, paixuData, R.layout.item_type_paixu);
        list_item_paixu.setAdapter(typePaixuAdapter);
        typePaixuAdapter.setClickPosition(paixuPostion);
        list_item_paixu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typePaixuAdapter.setClickPosition(i);
                paixuPostion = i;
                type_popup_zhong.setVisibility(View.GONE);

                sortOrder = i;
                pageIndex = 1;
                sendRequest(0, "");
                titale2.setText(paixuData.get(i));
                refreshView();
            }
        });
        //价格
        priceGridAdapter = new PriceGridAdapter(this, priceData, R.layout.item_gird_price);
        priceGridAdapter.setClickPosition(pricePostion);
        type_item_price.setAdapter(priceGridAdapter);

        //阴影
        itemLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initButton();
                goneView();
                type_popup_zhong.setVisibility(View.GONE);

            }
        });

    }

    @OnClick({R.id.activity_linear1, R.id.activity_linear2, R.id.activity_linear3, R.id.type_back, R.id.type_serach})
    public void onClick(View v) {
        selectButton(v.getId());
        switch (v.getId()) {
            case R.id.activity_linear1://分类
                break;
            case R.id.activity_linear2://智能排序
                break;
            case R.id.activity_linear3://筛选
                break;
            case R.id.type_serach://搜索
//                searchDialog.show();
                Bundle bundle = new Bundle();
                bundle.putInt("a",0);
                jumpActivity(MSerachActivity.class,bundle);
                break;
            case R.id.type_back://返回
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 条目状态切换
     */
    private void selectButton(int id) {
        switch (id) {
            case R.id.activity_linear1://分类
//                sendRequest(1, "");
                goneView();
                if (clickbutton1) {
                    clickbutton1 = false;
                    button1.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                    titale1.setTextColor(Color.parseColor("#3D3737"));

                    type_popup_zhong.setVisibility(View.GONE);
                    break;
                }
                clickbutton1 = true;
                clickbutton2 = false;
                clickbutton3 = false;
                type_popup_zhong.setVisibility(View.VISIBLE);
                fenlei.setVisibility(View.VISIBLE);
                button1.setImageDrawable(getResources().getDrawable(R.drawable.jtup_red));
                button2.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                button3.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                titale1.setTextColor(Color.parseColor("#BA020A"));
                titale2.setTextColor(Color.parseColor("#3D3737"));
                titale3.setTextColor(Color.parseColor("#3D3737"));
                //添加布局


                break;
            case R.id.activity_linear2://智能排序
                goneView();
                if (clickbutton2) {
                    clickbutton2 = false;
                    button2.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                    titale2.setTextColor(Color.parseColor("#3D3737"));

                    type_popup_zhong.setVisibility(View.GONE);
                    break;
                }
                clickbutton2 = true;
                clickbutton1 = false;
                clickbutton3 = false;
                type_popup_zhong.setVisibility(View.VISIBLE);
                paixu.setVisibility(View.VISIBLE);
                button2.setImageDrawable(getResources().getDrawable(R.drawable.jtup_red));
                button1.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                button3.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                titale2.setTextColor(Color.parseColor("#BA020A"));
                titale1.setTextColor(Color.parseColor("#3D3737"));
                titale3.setTextColor(Color.parseColor("#3D3737"));


                break;
            case R.id.activity_linear3://筛选
                goneView();
                if (clickbutton3) {
                    clickbutton3 = false;
                    button3.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                    titale3.setTextColor(Color.parseColor("#3D3737"));

                    type_popup_zhong.setVisibility(View.GONE);
                    break;
                }

                clickbutton3 = true;
                clickbutton1 = false;
                clickbutton2 = false;
                type_popup_zhong.setVisibility(View.VISIBLE);
                jiage.setVisibility(View.VISIBLE);
                button3.setImageDrawable(getResources().getDrawable(R.drawable.jtup_red));
                button2.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                button1.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
                titale3.setTextColor(Color.parseColor("#BA020A"));
                titale2.setTextColor(Color.parseColor("#3D3737"));
                titale1.setTextColor(Color.parseColor("#3D3737"));


                break;
        }
    }

    //价格
    public void priceClik(int postion) {
        this.pricePostion = postion;
        type_popup_zhong.setVisibility(View.GONE);

        priceRange = String.valueOf(postion);
        pageIndex = 1;
//        sendRequest(0, "");
        refreshView();
    }

    private void initButton() {
        clickbutton3 = false;
        clickbutton1 = false;
        clickbutton2 = false;
        type_popup_zhong.setVisibility(View.VISIBLE);
        button3.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
        button2.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
        button1.setImageDrawable(getResources().getDrawable(R.drawable.jtdown_gray));
        titale3.setTextColor(Color.parseColor("#3D3737"));
        titale2.setTextColor(Color.parseColor("#3D3737"));
        titale1.setTextColor(Color.parseColor("#3D3737"));
    }

    private void goneView() {
        fenlei.setVisibility(View.GONE);
        paixu.setVisibility(View.GONE);
        jiage.setVisibility(View.GONE);
    }

    /**
     * 当点击排序分类筛选后刷新界面
     */
    private void refreshView() {
        pageIndex = 1;
        sendRequest(0, "");
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 0) {
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("kindId", String.valueOf(kindId));
            parmMap.put("priceRange", priceRange);
            parmMap.put("sortOrder", String.valueOf(sortOrder));
            parmMap.put("pageIndex", String.valueOf(pageIndex));
            parmMap.put("pageIndex",pageIndex+"");
            parmMap.put("pageSize",""+ SPKeyManager.pageSize);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,
                    IRequestConst.RequestMethod.GetGoodsSaleList, parmMap, this);
            httpManager.request();
        } else if (tag == 1) {
            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET, IRequestConst.RequestMethod.GetGoodsKindList, null, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 0) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                if (searchDialog != null && searchDialog.isShowing()) {//当处于搜索时
                    if (searchDialog.getPage() == 1) {
                        marketGoodsList.clear();
                    }
                    searchDialog.setTotalPage(resultJson.optJSONObject("pageInfo").optInt("totalPage"));
                    JSONArray dataArray = resultJson.optJSONArray("data");
                    if (dataArray != null) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            MarketGoods marketGoods = new MarketGoods();
                            try {
                                initResultData(marketGoodsList, marketGoods, dataArray.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        marketGoodsAdapter.notifyDataSetChanged();
                        if (searchDialog.getPage() >= searchDialog.getTotalPage()) {
                            searchDialog.getListView().setPullLoadEnable(false);
                        } else {
                            searchDialog.getListView().setPullLoadEnable(true);
                        }
                        if (marketGoodsList.size() == 0) {
                            searchDialog.showNodata("暂无搜索结果");
                        } else {
                            searchDialog.showResultList();
                        }
                    }
                    searchDialog.getListView().stopLoadMore();
                    searchDialog.getListView().stopRefresh();
                } else {//当不处于搜索时
                    if (pageIndex == 1) {
                        marketGoodsList.clear();
                    }
                    totalPage = resultJson.optJSONObject("pageInfo").optInt("totalPage");
                    JSONArray dataArray = resultJson.optJSONArray("data");
                    if (dataArray != null) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            MarketGoods marketGoods = new MarketGoods();
                            try {
                                initResultData(marketGoodsList, marketGoods, dataArray.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        marketGoodsAdapter.notifyDataSetChanged();
                        if (pageIndex >= totalPage) {
                            xlistView.setPullLoadEnable(false);
                        } else {
                            xlistView.setPullLoadEnable(true);
                        }
                        if (marketGoodsList.size()>0){
                            tv_nodata.setVisibility(View.GONE);
                        }else {
                            tv_nodata.setVisibility(View.VISIBLE);
                        }
                    }
                    xlistView.stopLoadMore();
                    xlistView.stopRefresh();
                }
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        } else if (tag == 1) {//分类
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            JSONObject data = resultJson.optJSONObject("data");
            String s = resultJson.toString();
            if (!StringUtils.isEmpty(s)) {
                UploadTypeEntity uploadTypeEntity = GsonUtils.jsonToObject(s, UploadTypeEntity.class);
                guidData.clear();
                TypeContextEntity typeContextEntity = new TypeContextEntity();
                typeContextEntity.setName("全部");
                guidData.add(typeContextEntity);
                guidData.addAll(uploadTypeEntity.getData());
                resultData.clear();
                if (guidPostion == 0) {
                    TypeChildrenEntity typeChildrenEntity = new TypeChildrenEntity();
                    typeChildrenEntity.setId(0);
                    typeChildrenEntity.setName("全部");
                    resultData.add(typeChildrenEntity);
                    for (int j = 1; j < guidData.size(); j++) {
                        List<TypeChildrenEntity> children = guidData.get(j).getChildren();
                        if (children != null && children.size() != 0)

                            resultData.addAll(children);
                    }
                }
                itemTypeAdapter.notifyDataSetChanged();
                //导航
            }
        }
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        tip(msg);
    }



    //绑数据
    private void initResultData(List<MarketGoods> marketGoodsList, MarketGoods marketGoods, JSONObject jsonObject) {
        marketGoods.setId(jsonObject.optString("id"));
        marketGoods.setDescribe(jsonObject.optString("description"));
        marketGoods.setPrice(new BigDecimal(new java.text.DecimalFormat("0.00").format(jsonObject.optDouble("price")))+"");
        marketGoods.setSuggestedPrice(new BigDecimal(new java.text.DecimalFormat("0.00").format(jsonObject.optDouble("suggestedPrice")))+"");
        marketGoods.setVideo(jsonObject.optString("video"));
        marketGoods.setVideoSnap(jsonObject.optString("videoSnap"));
        marketGoods.setReselled(jsonObject.optBoolean("isReselled"));
        marketGoods.setShopId(jsonObject.optString("shopId"));
        JSONArray images=jsonObject.optJSONArray("images");
        if (images!=null&&images.length()>0){
            int count=images.length();
            if (!marketGoods.getVideo().isEmpty()){
                count=count+1;
            }
            ImgType[] types=new ImgType[count];
            if (marketGoods.getVideo().isEmpty()){//如果没有视频
                for (int j = 0; j < count; j++) {
                    ImgType imgType=new ImgType();
                    try {
                        imgType.setImgpath(images.getString(j));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    types[j]=imgType;
                }
            }else {
                for (int j = 0; j < count; j++) {//如果有视频
                    ImgType imgType=new ImgType();
                    if (j==0){
                        imgType.setVideo(true);
                        imgType.setVideopath(marketGoods.getVideo());
                        imgType.setImgpath(marketGoods.getVideoSnap());
                        types[j]=imgType;
                    }else {
                        try {
                            imgType.setImgpath(images.getString(j-1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        types[j]=imgType;
                    }
                }
            }
            marketGoods.setImgTypes(types);
        }
        String  ddd= DateUtil.getBetweenTime(jsonObject.optString("updateTime"),"" );
        marketGoods.setDateTime(ddd);
        marketGoods.setFavorite(jsonObject.optBoolean("isFavored"));
        marketGoodsList.add(marketGoods);
    }
    public void initButtonView(){
        initButton();
        goneView();
        type_popup_zhong.setVisibility(View.GONE);
    }
}
