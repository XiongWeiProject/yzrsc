package com.commodity.scw.ui.fragment;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.manager.SPManager;
import com.commodity.scw.model.ZhuanshouItemEntity;
import com.commodity.scw.model.ZhuansouDataEntity;
import com.commodity.scw.ottobus.BusProvider;
import com.commodity.scw.ottobus.Event;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.BaseFragment;
import com.commodity.scw.ui.activity.AuthenticationActivity;
import com.commodity.scw.ui.activity.HomeFragmentActivity;
import com.commodity.scw.ui.activity.classify.MSerachActivity;
import com.commodity.scw.ui.activity.commodity.CommodityDetailActivity;
import com.commodity.scw.ui.activity.store.AlertUploadGoodsActivity;
import com.commodity.scw.ui.activity.store.AlterOrderActivity;
import com.commodity.scw.ui.activity.store.StoreSettingActivity;
import com.commodity.scw.ui.activity.store.UploadGoodsActivity;
import com.commodity.scw.ui.activity.store.YiTijiaoActivity;
import com.commodity.scw.ui.adapter.PhotoPopupAdapter;
import com.commodity.scw.ui.adapter.ZhuanshouAdapter;
import com.commodity.scw.ui.dialog.CommonDialog;
import com.commodity.scw.ui.widget.imageview.CircleImageView;
import com.commodity.scw.ui.widget.layout.StoreTable;
import com.commodity.scw.ui.widget.layout.TakePopupWin;
import com.commodity.scw.ui.widget.xlistView.PLA_AbsListView;
import com.commodity.scw.ui.widget.xlistView.PLA_AdapterView;
import com.commodity.scw.ui.widget.xlistView.XListView;
import com.commodity.scw.utils.GsonUtils;
import com.commodity.scw.utils.ScreenUtils;
import com.squareup.otto.Subscribe;
import com.yixia.camera.util.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 作者：liyushen on 2017/03/13 16:31
 * 功能：我的店Fragment
 */
public class HomeShopFragment extends BaseFragment {
    @Bind(R.id.shop_bg)
    View backage;

    //申请开店
    @Bind(R.id.my_button_kaidian)
    TextView my_button_kaidian;
    //上传宝贝
    @Bind(R.id.my_button_upload)
    TextView my_button_upload;
    //搜索
    @Bind(R.id.my_button_serach)
    LinearLayout my_button_serach;
    @Bind(R.id.popup_all)
    ImageView popup_all;
    @Bind(R.id.tv_nodata)
    TextView tv_nodata;
    //店铺信息
//    @Bind(R.id.my_dian_head_button)
    LinearLayout my_dian_head_button;

    //头像
//    @Bind(R.id.my_image_head)
    CircleImageView my_image_head;
    //店名
//    @Bind(R.id.my_dian_name)
    TextView my_dian_name;
    //店铺邀请码
//    @Bind(R.id.my_dian_code)
    TextView my_dian_code;
    //店铺等级
//    @Bind(R.id.my_dian_class)
    TextView my_dian_class;
//    @Bind(R.id.my_table_menu)
    LinearLayout my_table_menu;

    @Bind(R.id.shop_listview)
    XListView listView;
    @Bind(R.id.zhuanshou_item_delete)
    LinearLayout zhuanshou_item_delete;
    @Bind(R.id.shop_button_piliang)
    ImageView shop_button_piliang;

    boolean flag=false;

    boolean zhenzhegnFlag=false;



    private List<ZhuanshouItemEntity> data=new ArrayList();

    private List<ZhuanshouItemEntity> deleteList=new ArrayList<>();
    private List<String> moreData=new ArrayList();
    //开店材料提交标识
    private boolean isSubmmit=false;
    private ZhuanshouAdapter zhuanshouAdapter;
    private boolean popupFlag;
    private boolean idAllselect;
    private LinearLayout uploadHead;
    private Button shop_shaohou;
    private Button shop_lijirenzheng;

    private int pageIndex = 1;
    private int totalPage = 1;
    private int pageState=0;
    private String propagationUrl;
    private String avatar;
    private String promotionCode;
    private String shopName;
    private int sellAuthenticatedStatus;
    private TextView textView;
    private boolean loadMoreFlag;
    private CommonDialog commonDialog;
    private int visibleIndex;
    private int curPostion=-1;


    @Override
    protected int getContentView() {
        return R.layout.fragment_home_shop;
    }


    @Override
    protected void initView() {
        //头部view
//        View headView = View.inflate(mContext, R.layout.shop_head, null);
//        my_dian_head_button= (LinearLayout) headView.findViewById(R.id.my_dian_head_button);
//        my_image_head= (CircleImageView) headView.findViewById(R.id.my_image_head);
//        my_dian_code= (TextView) headView.findViewById(R.id.my_dian_code);
//        my_dian_name= (TextView) headView.findViewById(R.id.my_dian_name);
//        my_dian_class= (TextView) headView.findViewById(R.id.my_dian_class);
//        my_table_menu= (LinearLayout) headView.findViewById(R.id.my_table_menu);

        my_dian_head_button= (LinearLayout) findView(R.id.my_dian_head_button);
        my_image_head= (CircleImageView) findView(R.id.my_image_head);
        my_dian_code= (TextView) findView(R.id.my_dian_code);
        my_dian_name= (TextView) findView(R.id.my_dian_name);
        my_dian_class= (TextView) findView(R.id.my_dian_class);
        my_table_menu= (LinearLayout) findView(R.id.my_table_menu);

        try {
            setShopInfo(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        customLoadding.show();
        sendRequest(0, ConfigManager.instance().getUser().getId());//获取用户信息

        uploadHead = (LinearLayout) findView(R.id.shop_head);//我的上传头部
        shop_shaohou = (Button) findView(R.id.shop_shaohou);//取消
        shop_shaohou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zhenzhegnFlag=true;
                uploadHead.setVisibility(View.GONE);
            }
        });
        shop_lijirenzheng = (Button) findView(R.id.shop_lijirenzheng);//认证
        shop_lijirenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(AuthenticationActivity.class);
            }
        });
        my_dian_head_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("url",propagationUrl);
                bundle.putString("avatar",avatar);
                bundle.putString("code",promotionCode);
                bundle.putString("shopName",shopName);
                jumpActivity(StoreSettingActivity.class,bundle);
            }
        });



//        listView.addHeaderView(headView);
        zhuanshouAdapter = new ZhuanshouAdapter(getActivity(), data, R.layout.item_zhuanshou);
        listView.setAdapter(zhuanshouAdapter);

        my_table_menu.addView(addMenu("我的转售",false,0));
        my_table_menu.addView(addMenu("我的上传",false,1));
        my_table_menu.addView(addMenu("已下架的",true,2));

        moreData.add("查看原宝贝");
        moreData.add("修改");
        moreData.add("删除");
        moreData.add("取消");

        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pageIndex = 1;
                        sendRequest(pageState+1,"1",SPKeyManager.pageSize+"","1");
                        customLoadding.show();
                        sendRequest(0,"");
                        listView.setRefreshTime(SPKeyManager.dateFormat.format(new Date()));
                    }
                },SPKeyManager.delay_time);
            }

            @Override
            public void onLoadMore() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(loadMoreFlag){
//                            pageIndex++;
                            customLoadding.show();
                            sendRequest(pageState+1,String.valueOf(pageIndex),SPKeyManager.pageSize+"","1");
                        }

                    }
                },SPKeyManager.delay_time);
            }
        });


//        doOnclickItem(0);
        refreshTable(0);
//        sendRequest(1,"1","10","1");
//        customLoadding.show();
//        sendRequest(0,"");
        refreshList();
        listView.setOnTouchListener(new View.OnTouchListener() {

            public int moveY=0;
            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        break;

                    case MotionEvent.ACTION_UP:
                        moveY=0;
                        if(startY>event.getY()){
                            if(shop_button_piliang.getVisibility()==View.VISIBLE){
                                ObjectAnimator alpha = ObjectAnimator.ofFloat(shop_button_piliang, "alpha", 1, 0);
                                alpha.start();
                                alpha.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        shop_button_piliang.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }else {
                            if(shop_button_piliang.getVisibility()==View.GONE){
                                shop_button_piliang.setVisibility(View.VISIBLE);
                                final ObjectAnimator alpha = ObjectAnimator.ofFloat(shop_button_piliang, "alpha", 0, 1);
                                alpha.start();
                                alpha.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                    }
                                });
                            }

                        }
                        break;
                }
                return false;
            }
        });

//        listView.setOnItemClickListener(new PLA_AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
//                if(pageState!=0){
//                    Bundle bundle = new Bundle();
//                    bundle.putString("goodsSaleId",String.valueOf(data.get(position).getId()));
//                    jumpActivity(CommodityDetailActivity.class,bundle);
//                }else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("goodsSaleId",String.valueOf(data.get(position).getId()));
//                    bundle.putString("url",IRequestConst.RequestMethod.GetParentGoodsSaleDetail);
//                    jumpActivity(CommodityDetailActivity.class,bundle);
//                }
//            }
//        });

    }

    private void doOnclickItem(int i) {
        refreshTable(i);
        zhuanshouAdapter.setPageState(i);
        zhuanshouAdapter.hindButton(i==2);
        zhuanshouAdapter.notifyDataSetChanged();
        if(i==1){
            //我的上传加加头布局
            if(isSubmmit||zhenzhegnFlag){
                uploadHead.setVisibility(View.GONE);
            }else {
                uploadHead.setVisibility(View.VISIBLE);
            }
        }else {
            uploadHead.setVisibility(View.GONE);
        }

        data.clear();
        pageIndex=1;
        customLoadding.show();
        zhuanshouAdapter.setState(i);
        switch (i){
            case 0:
                pageState = 0;
                pageIndex = 1;

                sendRequest(pageState+1,String.valueOf(pageIndex),SPKeyManager.pageSize+"","1");
                moreData.clear();
                moreData.add("查看原宝贝");
                moreData.add("修改");
                moreData.add("删除");
                moreData.add("取消");
                break;
            case 1:
                pageState = 1;
                pageIndex = 1;
                sendRequest(pageState+1,String.valueOf(pageIndex),SPKeyManager.pageSize+"","1");
                moreData.clear();
                moreData.add("设置已出售");
                moreData.add("修改");
                moreData.add("删除");
                moreData.add("取消");
                break;
            case 2:
                pageState = 2;
                pageIndex = 1;
                sendRequest(pageState+1,String.valueOf(pageIndex),SPKeyManager.pageSize+"","1");
                moreData.clear();
                moreData.add("设置为出售中");
                moreData.add("修改");
                moreData.add("删除");
                moreData.add("取消");
                break;
        }
    }

    private View addMenu(String text, boolean invisableV, final int index) {
        StoreTable storeTable = new StoreTable(mContext, text);
        storeTable.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtils.getScreenWidth(mContext) / 3, LinearLayout.LayoutParams.MATCH_PARENT));
        if(invisableV){
            storeTable.invisibleV();
        }
        storeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doOnclickItem(index);
            }
        });
        return storeTable;
    }

    @Override
    protected void initListeners() {
        //点击删除条目里面的
        zhuanshouAdapter.setOncheckClick(new ZhuanshouAdapter.OncheckClick() {
            @Override
            public void click(View v, int position) {
                if(deleteList.contains(data.get(position))){
                    deleteList.remove(data.get(position));
                }else {
                    deleteList.add(data.get(position));
                }
                zhuanshouAdapter.setDeleteList(deleteList);
            }
        });
        //点击更多
        zhuanshouAdapter.setMoreClick(new ZhuanshouAdapter.OncheckClick() {
            @Override
            public void click(View v, int position) {
                setHeadPictrue(position);
            }
        });
    }
    private void refreshTable(int index){
        for(int i=0;i<3;i++){
            ((StoreTable)(my_table_menu.getChildAt(i))).select(i==index);
        }
    }
    @OnClick({R.id.my_button_kaidian,R.id.my_button_upload,R.id.my_button_serach,R.id.shop_button_piliang,R.id.popup_all,R.id.popup_delete})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.my_button_kaidian://开店
                if(isSubmmit){//已经提交材料
                    Bundle bundle = new Bundle();
                    bundle.putString("avatar",avatar);
                    bundle.putString("shopName",shopName);
                    bundle.putInt("sellAuthenticatedStatus",sellAuthenticatedStatus);
                    jumpActivity(YiTijiaoActivity.class,bundle);
                }else {//没有提交材料
                    jumpActivity(AuthenticationActivity.class);
                }
                break;
            case R.id.my_button_upload://上传
                if(sellAuthenticatedStatus==1){
                    jumpActivity(UploadGoodsActivity.class);
                }else {
                    tip("认证通过后，才可上传");
                }

                break;
            case R.id.my_button_serach://搜索
                Bundle bundle = new Bundle();
                bundle.putInt("a",1);
                bundle.putString("shopId","111");
                jumpActivity(MSerachActivity.class,bundle);
                break;
            case R.id.shop_button_piliang://批量
                if(!popupFlag){
                    showPopup();
                }else {
                    hindPopup();
                }

                break;
            case R.id.popup_delete://删除
                for (ZhuanshouItemEntity e:deleteList){
                    customLoadding.show();
                    sendRequest(4,String.valueOf(e.getId()));
                    data.remove(e);
                }
                zhuanshouAdapter.setDeleteList(deleteList);
                zhuanshouAdapter.notifyDataSetChanged();
                hindPopup();
                break;
            case R.id.popup_all://全选
                zhuanshouAdapter.setDeleteList(deleteList);
                if(!idAllselect){//全选
                    idAllselect=true;
                    deleteList.clear();
                    deleteList.addAll(data);
                    zhuanshouAdapter.notifyDataSetChanged();
                    popup_all.setBackgroundResource(R.drawable.icon_xzkonn);
                }else {//非全选
                    idAllselect=false;
                    deleteList.clear();
                    zhuanshouAdapter.notifyDataSetChanged();
                    popup_all.setBackgroundResource(R.drawable.icon_xzk);
                }

                break;
            default:
                break;
        }


    }

    /**
     * 隐藏
     */
    private void hindPopup() {
        popupFlag=false;
        deleteList.clear();
        zhuanshouAdapter.hindCheckbox(true);
        zhuanshou_item_delete.setVisibility(View.GONE);
        shop_button_piliang.setBackgroundResource(R.drawable.icon_button_shop);
    }

    /**
     * 显示删除天目
     */
    private void showPopup() {
        popupFlag=true;
        popup_all.setBackgroundResource(R.drawable.icon_xzk);
        zhuanshouAdapter.hindCheckbox(false);
        zhuanshou_item_delete.setVisibility(View.VISIBLE);
        shop_button_piliang.setBackgroundResource(R.drawable.icon_button_shop_x);
    }

    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);

        switch(tag){
            case 0://个人信息
                Map<String,String> map=new HashMap<>();
                map.put("sellerId",ConfigManager.instance().getUser().getId());
                HttpManager httpManager = new HttpManager(tag,HttpMothed.GET,IRequestConst.RequestMethod.StoreInfo,map,this );
                httpManager.request();
                break;
            case 1://获取转售
                Map<String,String> zhuanMap=new HashMap<>();
                zhuanMap.put("pageIndex",String.valueOf(params[0]) );//页码
                zhuanMap.put("pageSize",(String) params[1]);//分页大小
                zhuanMap.put("sortOrder",(String) params[2]);//排序值
                HttpManager zhuanHttpM = new HttpManager(tag,HttpMothed.GET,IRequestConst.RequestMethod.GetMyResellGoods,zhuanMap,this );
                zhuanHttpM.request();
                break;
            case 2://我的上传
                Map<String,String> uploadMap=new HashMap<>();
                uploadMap.put("pageIndex",(String) params[0]);//页码
                uploadMap.put("pageSize",(String) params[1]);//分页大小
                uploadMap.put("sortOrder",(String) params[2]);//排序值
                HttpManager uploadHttpM = new HttpManager(tag,HttpMothed.GET,IRequestConst.RequestMethod.GetMyGoods,uploadMap,this );
                uploadHttpM.request();
                break;
            case 3://已下架
                Map<String,String> downMap=new HashMap<>();
                downMap.put("pageIndex",(String) params[0]);//页码
                downMap.put("pageSize",(String) params[1]);//分页大小
                downMap.put("sortOrder",(String) params[2]);//排序值
                HttpManager downHttpM = new HttpManager(tag,HttpMothed.GET,IRequestConst.RequestMethod.GetMyOffShelvesGoods,downMap,this );
                downHttpM.request();
                break;
            case 4://删除商品
                Map<String,String> deleteMap=new HashMap<>();
                deleteMap.put("goodsSaleId",(String) params[0]);//id
                HttpManager DeleteM = new HttpManager(tag,HttpMothed.POST,IRequestConst.RequestMethod.DeleteGoodsSaleInfo,deleteMap,this );
                DeleteM.request();
                break;
            case 5://brann

                break;
            case 6://设置商品已经售罄
                Map<String,String> overM=new HashMap<>();
                overM.put("goodsSaleId",(String) params[0]);//id
                HttpManager orerH = new HttpManager(tag,HttpMothed.POST,IRequestConst.RequestMethod.SetGoodsSoldout,overM,this );
                orerH.request();
                break;
            case 7:
                Map<String,String> zM=new HashMap<>();
                zM.put("goodsSaleId",(String) params[0]);//id
                HttpManager zH = new HttpManager(tag,HttpMothed.POST,IRequestConst.RequestMethod.SetGoodsOnSale,zM,this );
                zH.request();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        JSONObject jsonObject= (JSONObject) resultInfo.getResponse();
        if(jsonObject.optBoolean("success")){
            switch(tag){
                case 0://个人信息
                    try {
                        setShopInfo(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1://转售商品
                case 2://转售商品
                case 3://转售商品
                    try {
                        parseZhuan(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4://删除
                    if (curPostion!=-1){
                        data.remove(curPostion);
                        zhuanshouAdapter.notifyDataSetChanged();
                    }
                    curPostion=-1;
                    BusProvider.getInstance().post(new Event.NotifyChangedView("HomeMarketFragment"));
                    break;
                case 6:
                case 7:
                    if(jsonObject.optBoolean("data")){
                        tip("设置成功");
                        doOnclickItem(pageState);
                    }else {
                        tip(jsonObject.optString("msg"));
                    }
                default:
                    break;
            }
        }else {
            tip(jsonObject.optString("msg"));
        }
        listView.stopLoadMore();
    }


    /**
     * 解析转售的json
     * @param resultJson
     */
    private void parseZhuan(JSONObject resultJson) throws JSONException {
        if (resultJson!=null&&resultJson.optBoolean("success")){
            if (pageIndex == 1) {
                data.clear();
            }
            totalPage=resultJson.optJSONObject("pageInfo").optInt("totalPage");
            int index=resultJson.optJSONObject("pageInfo").optInt("index");
            ZhuansouDataEntity zhuansouDataEntity = GsonUtils.jsonToObject(resultJson.toString(), ZhuansouDataEntity.class);
            if(zhuansouDataEntity!=null&&zhuansouDataEntity.getPageInfo().getCount()>0){
                //刷新转售数据
                data.addAll(zhuansouDataEntity.getData());
                zhuanshouAdapter.notifyDataSetChanged();
                pageIndex++;
            }
            zhuanshouAdapter.notifyDataSetChanged();
            if (index >= totalPage) {
                listView.setPullLoadEnable(false);
            } else {
                listView.setPullLoadEnable(true);
            }
            if (data.size()>0){
                tv_nodata.setVisibility(View.GONE);
            }else {
                tv_nodata.setVisibility(View.VISIBLE);
                pageIndex=1;
            }
            if(data.size()==0){
                listView.setPullLoadEnable(true);
                loadMoreFlag=false;
            }else {
                loadMoreFlag=true;
            }

        }else {
            if (resultJson!=null&&!resultJson.optBoolean("success")){
                tip(resultJson.optString("msg"));
            }
        }
        listView.stopLoadMore();
        listView.stopRefresh();

//        String s = jsonObject.toString();
//        if(!StringUtils.isEmpty(s)){
//            listView.stopLoadMore();
//            ZhuansouDataEntity zhuansouDataEntity = GsonUtils.jsonToObject(s, ZhuansouDataEntity.class);
//            if(zhuansouDataEntity!=null&&zhuansouDataEntity.getPageInfo().getTotalCount()>0){
//                //刷新转售数据
//                data.addAll(zhuansouDataEntity.getData());
//                zhuanshouAdapter.notifyDataSetChanged();
//                pageIndex++;
//            }else {
//                if(pageIndex>=1){
//                    listView.setPullLoadEnable(false);
//                }else {
//                    listView.setPullLoadEnable(true);
//                }
//            }
//            JSONObject pageInfo = jsonObject.optJSONObject("pageInfo");
//            if(pageInfo.optInt("totalCount")==0){
//                linear_bg.setVisibility(View.VISIBLE);
//                listView.setPullLoadEnable(false);
//            }else {
//                listView.setPullLoadEnable(true);
//            }
//            if(pageInfo.optInt("count")<10){
//                listView.setPullLoadEnable(false);
//                textView = new TextView(mContext);
//                textView.setText("");
//                PLA_AbsListView.LayoutParams layoutParams = new PLA_AbsListView.LayoutParams(ScreenUtils.dp2px(mContext,25), ScreenUtils.dp2px(mContext,25));
//                textView.setLayoutParams(layoutParams);
//                listView.addFooterView(textView);
//            }else {
//                listView.setPullLoadEnable(true);
//                if(textView!=null){
//                    listView.removeFooterView(textView);
//                }
//            }
//        }

    }

    /**
     * 设置店铺信息
     * @param jsonObject
     * @throws JSONException
     */
    private void setShopInfo(JSONObject jsonObject) throws JSONException {
        if (jsonObject==null){
//            my_image_head.setImageBitmap();
            my_dian_code.setText("店铺邀请码:"+ConfigManager.instance().getUser().getId());
            my_dian_name.setText("店铺名称:"+ConfigManager.instance().getUser().getNickname());
            my_dian_class.setText("lv01");
        }else {
            JSONObject data = jsonObject.getJSONObject("data");
            my_dian_class.setText(data.optString("level"));
            shopName = data.optString("name");
            if(StringUtils.isEmpty(shopName)){
                shopName=ConfigManager.instance().getUser().getNickname();
            }
            my_dian_name.setText("店铺名称:"+shopName);
//            isSubmmit=data.optBoolean("isAuthenticated");
            SPManager.instance().setBoolean(Constanct.RENZHENG,isSubmmit);
            propagationUrl=data.optString("propagationUrl");
            SPManager.instance().setString(Constanct.propagationUrl,propagationUrl);
            avatar = data.optString("avatar");
            ImageLoaderManager.getInstance().displayImage(avatar,my_image_head);
            promotionCode = data.optString("promotionCode");
            my_dian_code.setText("店铺邀请码:"+promotionCode);
            sellAuthenticatedStatus = data.optInt("sellAuthenticatedStatus");
            if(sellAuthenticatedStatus==3||sellAuthenticatedStatus==2){
                isSubmmit=false;
            }else {
                isSubmmit=true;
            }
        }

    }


    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
        switch(tag){
            case 0://个人信息
                tip(msg);
                break;
            default:
                break;
        }
        listView.stopLoadMore();
    }

    @Override
    public void OnNetWorkErrorResponse(int tag, boolean isShowTip) {
        super.OnNetWorkErrorResponse(tag, isShowTip);
        listView.stopLoadMore();
    }

    /**
     * popupwindow
     */
    private void setHeadPictrue(int position) {
        curPostion=position;
        PhotoPopupAdapter photoPopupAdapter = new PhotoPopupAdapter(mContext, moreData, R.layout.item_photo_button);
        final TakePopupWin takePopupWin = new TakePopupWin(mContext, R.layout.item_photo, R.id.photo_contorl, R.id.photo_listview, photoPopupAdapter);
        backage.setVisibility(View.VISIBLE);
        final WindowManager.LayoutParams attributes = ((Activity)mContext).getWindow().getAttributes();
        takePopupWin.showAtLocation(((Activity)mContext).findViewById(R.id.home_bg), Gravity.BOTTOM,0,attributes.height-takePopupWin.getHeight());
        //点击时间
        takePopupWin.setOnItemClickListener(new TakePopupWin.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int click, long id) {
                if(commonDialog==null){
                    commonDialog = new CommonDialog(mContext);
                }
                switch (click){
                    case 0://查看原宝贝
                        if(pageState==0){
                            Bundle bundle = new Bundle();
                            bundle.putString("goodsSaleId",String.valueOf(data.get(curPostion).getId()));
                            bundle.putString("url",IRequestConst.RequestMethod.GetParentGoodsSaleDetail);
                            jumpActivity(CommodityDetailActivity.class,bundle);
                        }else {
                            commonDialog.show();
                            commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                                @Override
                                public void clickSubmit() {
                                    customLoadding.show();
                                    if(pageState==1){
                                        sendRequest(6,String.valueOf(data.get(curPostion).getId()));
                                    }else if(pageState==2){
                                        sendRequest(7,String.valueOf(data.get(curPostion).getId()));
                                    }
                                }
                            });
                            if(pageState==1){
                                commonDialog.setContext("设置已出售");
                            }else if(pageState==2){
                                commonDialog.setContext("设置为出售中");
                            }

                        }

                        break;
                    case 1://修改
                        CommonDialog dialog = new CommonDialog(mContext);
                        dialog.show();
                        dialog.setContext("确认修改商品？");
                        dialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                            @Override
                            public void clickSubmit() {
                                Bundle alterBundle = new Bundle();
                                alterBundle.putString("orderId",String.valueOf(data.get(curPostion).getId()));
                                if(pageState==0){
                                    alterBundle.putString("state","zhuanshou");
                                    jumpActivityForResult(1010,AlterOrderActivity.class,alterBundle);
                                }else if(pageState==1){
                                    alterBundle.putString("state","upload");
                                    jumpActivityForResult(1010,AlertUploadGoodsActivity.class,alterBundle);
                                }else {
                                    alterBundle.putString("state","xiajia");
                                    jumpActivityForResult(1010,AlertUploadGoodsActivity.class,alterBundle);
                                }

                            }
                        });

                        break;
                    case 2://删除
                        final CommonDialog commonDialog = new CommonDialog(mContext);
                        commonDialog.show();
                        if(pageState==0){
                            commonDialog.setContext("删除后将无法恢复，并且客户转售\n" +
                                    "后的链接都将失效，确定删除？");
                        }else if(pageState==1){
                            commonDialog.setContext("删除后将无法恢复,确定删除？");
                        }else if(pageState==2){
                            commonDialog.setContext("删除后将无法恢复，确定删除？");
                        }

                        commonDialog.setClickCancelListener(new CommonDialog.OnClickCancelListener() {
                            @Override
                            public void clickCance() {
                                commonDialog.dismiss();
                            }
                        });
                        commonDialog.setClickSubmitListener(new CommonDialog.OnClickSubmitListener() {
                            @Override
                            public void clickSubmit() {
                                customLoadding.show();
                                sendRequest(4,String.valueOf(data.get(curPostion).getId()));
                            }
                        });

                        break;
                    case 3://取消

                        break;
                }

            }
        });
        takePopupWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isSubmmit=(boolean) SPManager.get(mContext, Constanct.RENZHENG,false);
        zhenzhegnFlag= (boolean) SPManager.get(mContext,Constanct.RENZHENG,false);
        sendRequest(0,"");

    }
    public void refreshList(){
        data.clear();
        pageIndex=1;
        sendRequest(pageState+1,"1",SPKeyManager.pageSize+"","1");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==getActivity().RESULT_OK){
            if(requestCode==1010){
                doOnclickItem(pageState);
            }
        }
    }

    //获取传递过来信息进行相关操作
    @Subscribe
    public void NotifyChangedView(Event.NotifyChangedView event) {
        if (event.getDataObject().equals("HomeShopFragment")){
            refreshList();
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
}
