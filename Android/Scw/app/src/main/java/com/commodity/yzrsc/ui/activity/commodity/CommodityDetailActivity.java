package com.commodity.yzrsc.ui.activity.commodity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.BaseHttpManager;
import com.commodity.yzrsc.http.HttpManageNew;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ConfigManager;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.CommodityBean;
import com.commodity.yzrsc.model.ImgType;
import com.commodity.yzrsc.model.MarketGoods;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.BaseFragmentActivity;
import com.commodity.yzrsc.ui.activity.general.GeneralWebViewActivity;
import com.commodity.yzrsc.ui.activity.user.MyCartActivity;
import com.commodity.yzrsc.ui.dialog.ResellGoodsDialog;
import com.commodity.yzrsc.ui.dialog.ResellSuccessDialog;
import com.commodity.yzrsc.ui.fragment.CommodityGuigeFragment;
import com.commodity.yzrsc.ui.fragment.CommodityPingjiaFragment;
import com.commodity.yzrsc.ui.fragment.CommodityTuwenFragment;
import com.commodity.yzrsc.ui.widget.customview.PageFlipperLayout;
import com.commodity.yzrsc.ui.widget.imageview.CircleImageView;
import com.commodity.yzrsc.ui.widget.scrollView.StickyScrollView;
import com.commodity.yzrsc.utils.GsonUtils;
import com.commodity.yzrsc.utils.RongIMUtil;
import com.commodity.yzrsc.utils.SharetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyushen on 2017/3/28 20:32
 * 商品详情
 */
public class CommodityDetailActivity extends BaseFragmentActivity implements View.OnClickListener, StickyScrollView.OnScrollChangedListener {
    @Bind(R.id.pageflipperlayout)
    PageFlipperLayout pageflipperlayout;
    @Bind(R.id.btn_lianxihuozhu)
    Button btn_lianxihuozhu;
    @Bind(R.id.btn_lijigoumai)
    Button btn_lijigoumai;
    @Bind(R.id.tv_title1)
    TextView tv_title1;
    @Bind(R.id.tv_title2)
    TextView tv_title2;
    @Bind(R.id.tv_title3)
    TextView tv_title3;
    @Bind(R.id.scrollView)
    StickyScrollView stickyScrollView;
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.tabMainContainer)
    FrameLayout frameLayout;
    @Bind(R.id.ll_good_detail)
    LinearLayout llTitle;
    @Bind(R.id.tv_shangpinmiaoshu)
    TextView tv_shangpinmiaoshu;
    @Bind(R.id.tv_shangpinjiage)
    TextView tv_shangpinjiage;
    @Bind(R.id.tv_shangpinyuanjia)
    TextView tv_shangpinyuanjia;
    @Bind(R.id.tv_isfavored)
    TextView tv_isfavored;
    @Bind(R.id.tv_resell_goods)
    TextView tv_resell_goods;
    @Bind(R.id.tv_cart)
    TextView tv_cart;
    @Bind(R.id.tv_cart_num)
    TextView tv_cart_num;

    @Bind(R.id.img_avator)
    CircleImageView img_avator;
    @Bind(R.id.tv_huiyuanmingcheng)
    TextView tv_huiyuanmingcheng;
    @Bind(R.id.tv_dianpu)
    TextView tv_dianpu;
    @Bind(R.id.linear_item)
    LinearLayout linear_item;

    private int height;
    private List<TextView> titleTextViews = new ArrayList<>();
    private CommodityTuwenFragment commodityTuwenFragment;
    private CommodityGuigeFragment commodityGuigeFragment;
    private CommodityPingjiaFragment commodityPingjiaFragment;
    private Fragment mCurFragment;
    private String goodsSaleId = "";
    private String goodsDetailStr = "";
    private String contactId = "";
    private String sellerImId = "";//融云聊天id
    private CommodityBean commodityBean;
    private String url=IRequestConst.RequestMethod.GetGoodsDetail;
    private int numberInCard = 0;
    @Override
    protected int getContentView() {
        return R.layout.activity_commodity_detail;
    }

    @Override
    protected void initView() {
        if (getIntent().getExtras().containsKey("goodsSaleId")) {
            goodsSaleId = getIntent().getExtras().getString("goodsSaleId");
        }
        if (getIntent().getExtras().containsKey("proview")) {
            if("proview".equals(getIntent().getExtras().getString("proview"))){
                linear_item.setVisibility(View.GONE);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) stickyScrollView.getLayoutParams();
                layoutParams.bottomMargin=0;
            }else {
                linear_item.setVisibility(View.VISIBLE);
            }
        }
        if (getIntent().getExtras().containsKey("url")) {
            url = getIntent().getExtras().getString("url");
        }
        tv_title1.setOnClickListener(this);
        tv_title2.setOnClickListener(this);
        tv_title3.setOnClickListener(this);
        titleTextViews.add(tv_title1);
        titleTextViews.add(tv_title2);
        titleTextViews.add(tv_title3);
        stickyScrollView.setOnScrollListener(this);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) llTitle.getLayoutParams();
        params.setMargins(0, getStatusHeight(), 0, 0);
        llTitle.setLayoutParams(params);
        pageflipperlayout.setLayoutHight(1);

        //默认设置一个Frg
        getIntent().putExtra("goodsSaleId", goodsSaleId);
        commodityTuwenFragment = new CommodityTuwenFragment();
        commodityGuigeFragment = new CommodityGuigeFragment();
        commodityPingjiaFragment = new CommodityPingjiaFragment();
        mCurFragment = commodityTuwenFragment;
        showStats(0);
        getSupportFragmentManager().beginTransaction().add(R.id.tabMainContainer, mCurFragment).commit();
        switchFragment(mCurFragment);
        sendRequest(1, "");
        sendRequest(5, "");
    }

    @Override
    protected void initListeners() {
        btn_lijigoumai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sellerImId.equals(ConfigManager.instance().getUser().getId())){
                    tip("无法购买自己店铺的宝贝！");
                    return;
                }
                sendRequest(4, "");
            }
        });
        initListeners2();
        btn_lianxihuozhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sellerImId.equals(ConfigManager.instance().getUser().getId())){
                    tip("本店铺商品，无法发起会话！");
                    return;
                }
                try{
                    RongIMUtil.startConversation(CommodityDetailActivity.this, sellerImId, "货主");
                }catch (Exception e){
                    tip(e.getMessage());
                }

                RongIMUtil.updateUserInfo(commodityBean.getSellerImId(),commodityBean.getShopName(),commodityBean.getSellerAvatar());
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.getInstance().post(new Event.SendCommodInfo(commodityBean));
                    }
                }, 1000);
            }
        });
        tv_isfavored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest(2, "");
            }
        });
        tv_cart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                //bundle.putString("goodsDetailStr", goodsDetailStr);
                jumpActivity(MyCartActivity.class, bundle);
            }
        });
        tv_resell_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sellerImId.equals(ConfigManager.instance().getUser().getId())){
                    tip("本店铺商品，无法转售！");
                    return;
                }
                if (tv_resell_goods.getText().toString().equals("已转售")){
                    tip("已转售");
                }else {
                    MarketGoods marketGoods=new MarketGoods();
                    marketGoods.setSuggestedPrice(commodityBean.getSuggestedPrice()+"");
                    marketGoods.setId(commodityBean.getId()+"");
                    marketGoods.setDescribe(commodityBean.getDescription());
                    marketGoods.setPrice(commodityBean.getPrice()+"");
                    showResellGoods(marketGoods);
                }
            }
        });
        tv_dianpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("title",commodityBean.getShopName());
                bundle.putString("content_url",commodityBean.getPropagationUrl());
                jumpActivity(GeneralWebViewActivity.class,bundle);
            }
        });
    }


    @Override
    public void sendRequest(int tag, Object... params) {
        super.sendRequest(tag, params);
        if (tag == 1) {//商品详情
            customLoadding.show();
            Map<String, String> parmMap = new HashMap<String, String>();
            if(url.equals(IRequestConst.RequestMethod.GetGoodsDetail)){
                parmMap.put("goodsSaleId", goodsSaleId);
            }else {
                parmMap.put("resellGoodsSaleId", goodsSaleId);
            }

            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,url
                    , parmMap, this);
            httpManager.request();
        } else if (tag == 2) {//收藏
            Map<String, Object> parmMap = new HashMap<String, Object>();
            JSONArray array = new JSONArray();
            array.put(goodsSaleId);
            parmMap.put("goodSaleIds", array);
            parmMap.put("memberId", ConfigManager.instance().getUser().getId());
            HttpManageNew httpManager = new HttpManageNew(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.CancelFavorGoods, parmMap, this);
            httpManager.request();
        } else if (tag == 3) {//添加联系人
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("contactId", contactId);
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.AddContact, parmMap, this);
            httpManager.request();
        } else if (tag == 4) {//加入购物车
            Map<String, String> parmMap = new HashMap<String, String>();
            parmMap.put("memberId",ConfigManager.instance().getUser().getId());
            parmMap.put("goodsSaleId", goodsSaleId);
            parmMap.put("quantity", "1");
            HttpManager httpManager = new HttpManager(tag, HttpMothed.POST,
                    IRequestConst.RequestMethod.AddCart, parmMap, this);
            httpManager.request();
        } else if (tag == 5) {
            Map<String, String> parmMap = new HashMap<String, String>();

            HttpManager httpManager = new HttpManager(tag, HttpMothed.GET,IRequestConst.RequestMethod.GetCartList
                    , parmMap, this);
            httpManager.request();
        }
    }

    @Override
    public void OnSuccessResponse(int tag, ServiceInfo resultInfo) {
        super.OnSuccessResponse(tag, resultInfo);
        if (tag == 1) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                commodityBean = GsonUtils.jsonToObject(resultJson.optJSONObject("data").toString(), CommodityBean.class);
                initDataView(resultJson);
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        } else if (tag == 2) {
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                if (tv_isfavored.getTag().equals("收藏")) {
                    commodityBean.setFavoredCount(commodityBean.getFavoredCount()+1);
                    tv_isfavored.setText("已收藏 "+commodityBean.getFavoredCount());
                    tv_isfavored.setTag("已收藏");
                    tip("已收藏");
                    tv_isfavored.setCompoundDrawables(null, ico_love_on, null, null);
                } else {
                    commodityBean.setFavoredCount(commodityBean.getFavoredCount()-1);
                    tv_isfavored.setText("收藏 "+commodityBean.getFavoredCount());
                    tv_isfavored.setTag("收藏");
                    tip("取消收藏");
                    tv_isfavored.setCompoundDrawables(null, ico_love, null, null);
                }
            } else {
                if (resultJson != null && !resultJson.optBoolean("success")) {
                    tip(resultJson.optString("msg"));
                }
            }
        } else if (tag == 4){//加入购物车
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            if (resultJson != null && resultJson.optBoolean("success")) {
                tip("成功加入购物车");
                numberInCard++;
                tv_cart_num.setText(Integer.valueOf(numberInCard).toString());
            }
            else{
                tip("加入购物车失败");
            }
        } else if (tag == 5) {//购物车数量
            JSONObject resultJson = (JSONObject) resultInfo.getResponse();
            numberInCard = ParseCartNumber(resultJson);
            tv_cart_num.setText(Integer.valueOf(numberInCard).toString());
        }

    }

    public static int ParseCartNumber(JSONObject resultJson){
        if (resultJson != null && resultJson.optBoolean("success")) {
            return 0;
        }
        else
            return 0;
    }

    @Override
    public void OnFailedResponse(int tag, String code, String msg) {
        super.OnFailedResponse(tag, code, msg);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusHeight() {
//        int resourceId = CeshiActivity.this.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        return getResources().getDimensionPixelSize(resourceId);
        //如果没有设置沉浸式状态栏 就返回0
        return 0;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_title1) {
            showStats(0);
            switchFragment(commodityTuwenFragment);
            // getSupportFragmentManager().beginTransaction().replace(R.id.tabMainContainer, commodityTuwenFragment).commit();
        } else if (v.getId() == R.id.tv_title2) {
            showStats(1);
            switchFragment(commodityGuigeFragment);
            // getSupportFragmentManager().beginTransaction().replace(R.id.tabMainContainer, commodityGuigeFragment).commit();
        } else if (v.getId() == R.id.tv_title3) {
            showStats(2);
            switchFragment(commodityPingjiaFragment);
            //sgetSupportFragmentManager().beginTransaction().replace(R.id.tabMainContainer, commodityPingjiaFragment).commit();
        }
    }

    private void showStats(int position) {
        for (int i = 0; i < titleTextViews.size(); i++) {
            if (i == position) {
                titleTextViews.get(i).setTextColor(getResources().getColor(R.color.co_F95F3D));
                titleTextViews.get(i).setBackgroundResource(R.drawable.one_line_bottom_red_bg);
            } else {
                titleTextViews.get(i).setTextColor(getResources().getColor(R.color.second_black));
                titleTextViews.get(i).setBackgroundResource(R.drawable.icon_transparent);
            }
        }
    }

    private void initListeners2() {
        //获取内容总高度
        final ViewTreeObserver vto = llContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = llContent.getHeight();
                //注意要移除
                llContent.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);

            }
        });

        //获取Fragment高度
        ViewTreeObserver viewTreeObserver = frameLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = height - frameLayout.getHeight();
                //注意要移除
                frameLayout.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });

        //获取title高度
        ViewTreeObserver viewTreeObserver1 = llTitle.getViewTreeObserver();
        viewTreeObserver1.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = height - llTitle.getHeight() - getStatusHeight();//计算滑动的总距离
                stickyScrollView.setStickTop(llTitle.getHeight() + getStatusHeight());//设置距离多少悬浮
                //注意要移除
                llTitle.getViewTreeObserver()
                        .removeGlobalOnLayoutListener(this);
            }
        });


    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t <= 0) {
            llTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
        } else if (t > 0 && t <= height) {
            float scale = (float) t / height;
            int alpha = (int) (255 * scale);
            llTitle.setBackgroundColor(Color.argb((int) alpha, 17, 26, 56));//设置标题栏的透明度及颜色
        } else {
            llTitle.setBackgroundColor(Color.argb((int) 255, 17, 26, 56));
        }
    }

    //选择的Fragment
    private void switchFragment(Fragment fragment) {
        if (fragment != mCurFragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFragment).add(R.id.tabMainContainer, fragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(mCurFragment).show(fragment).commit();
            }
            mCurFragment = fragment;
        }
    }

    Drawable ico_love;
    Drawable ico_love_on;

    private void initDataView(JSONObject resultJson) {
        ico_love = getResources().getDrawable(R.drawable.ico_love);
        ico_love_on = getResources().getDrawable(R.drawable.ico_love_on);
        ico_love.setBounds(0, 0, ico_love.getMinimumWidth(), ico_love.getMinimumHeight());
        ico_love_on.setBounds(0, 0, ico_love_on.getMinimumWidth(), ico_love_on.getMinimumHeight());

        JSONObject data = resultJson.optJSONObject("data");
        contactId = data.optString("sellerImId");
        sellerImId = data.optString("sellerImId");
        goodsDetailStr = data.toString();
        tv_shangpinmiaoshu.setText(data.optString("description"));
        tv_shangpinjiage.setText("￥" + new BigDecimal(new java.text.DecimalFormat("0.00").format(data.optDouble("price")))+"" );
        tv_shangpinyuanjia.setText("￥" + new BigDecimal(new java.text.DecimalFormat("0.00").format(data.optDouble("suggestedPrice")))+"");
        tv_shangpinyuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//  中间加横线
        ImageLoaderManager.getInstance().displayImage(data.optString("sellerAvatar"),img_avator,R.drawable.ico_defalut_header);
        tv_huiyuanmingcheng.setText(data.optString("shopName"));
        if (commodityBean.isIsReselled()){
            tv_resell_goods.setText("已转售");
        }else {
            tv_resell_goods.setText("转售");
        }

        if (commodityGuigeFragment == null) {
            commodityGuigeFragment = new CommodityGuigeFragment();
            commodityGuigeFragment.setDataView(data);
        } else {
            commodityGuigeFragment.setDataView(data);
        }

        if (commodityTuwenFragment == null) {
            commodityTuwenFragment = new CommodityTuwenFragment();
            commodityTuwenFragment.setDataView(data);
        } else {
            commodityTuwenFragment.setDataView(data);
        }

        if (data.optBoolean("isFavored")) {
            tv_isfavored.setText("已收藏 "+commodityBean.getFavoredCount());
            tv_isfavored.setTag("已收藏");
            tv_isfavored.setCompoundDrawables(null, ico_love_on, null, null);
        } else {
            tv_isfavored.setText("收藏 "+commodityBean.getFavoredCount());
            tv_isfavored.setTag("收藏");
            tv_isfavored.setCompoundDrawables(null, ico_love, null, null);
        }
        if(data.optString("shopId").equals(ConfigManager.instance().getUser().getId())){
            linear_item.setVisibility(View.GONE);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) stickyScrollView.getLayoutParams();
            layoutParams.bottomMargin=0;
        } else  {
            linear_item.setVisibility(View.VISIBLE);
        }


        List<ImgType> imgTypeList = new ArrayList<>();
        JSONArray imagesArray = data.optJSONArray("images");
        if (imagesArray != null && imagesArray.length() > 0) {
            for (int i = 0; i < imagesArray.length(); i++) {
                try {
                    ImgType imh = new ImgType();
                    imh.setImgpath(imagesArray.getString(i));
                    imh.setVideo(false);
                    imgTypeList.add(imh);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (commodityBean.getVideo()!=null&&!commodityBean.getVideo().isEmpty()){
                ImgType imh = new ImgType();
                imh.setImgpath(commodityBean.getVideoSnap());
                imh.setVideo(true);
                imh.setVideopath(commodityBean.getVideo());
                imgTypeList.add(0,imh);
            }
            pageflipperlayout.setImgTypeList(imgTypeList);
        } else {

        }
    }

    //弹转售
    private void showResellGoods(final MarketGoods marketGoods) {
        ResellGoodsDialog resellGoodsDialog = new ResellGoodsDialog(this);
        resellGoodsDialog.setClickSubmitListener(new ResellGoodsDialog.OnClickSubmitListener() {
            @Override
            public void clickSubmit(String goodsSaleId, final String description, final String resellPrice) {
                Map<String, String> parmMap = new HashMap<String, String>();
                parmMap.put("goodsSaleId", goodsSaleId);
                parmMap.put("description", description);
                parmMap.put("resellPrice", resellPrice);
                HttpManager httpManager = new HttpManager(0, HttpMothed.POST,
                        IRequestConst.RequestMethod.ResellGoods, parmMap, new BaseHttpManager.IRequestListener() {
                    @Override
                    public void onPreExecute(int Tag) {
                    }

                    @Override
                    public void onSuccess(int Tag, ServiceInfo result) {
                        final JSONObject jsonObject = (JSONObject) result.getResponse();
                        if (jsonObject != null) {
                            if (jsonObject.optBoolean("success")) {
                                tv_resell_goods.setText("已转售");
                                BusProvider.getInstance().post(new Event.NotifyChangedView("HomeShopFragment"));
                                final String goodsSaleurl=jsonObject.optString("data");
                                ResellSuccessDialog resellSuccessDialog=new ResellSuccessDialog(mContext);
                                resellSuccessDialog.show();
                                resellSuccessDialog.setClickSubmitListener(new ResellSuccessDialog.OnClickSubmitListener() {
                                    @Override
                                    public void clickSubmit() {
//                                        SharetUtil.toShare(mContext,commodityBean.getImages().get(0),
//                                                goodsSaleurl,"易州人商城",description,commodityBean.getImages());
//                                        ShareEntity shareEntity = new ShareEntity();
//                                        shareEntity.desc=description+"\n"+"【 批发价格 】"+resellPrice+"\n"+"【 购买地址 】"+goodsSaleurl;
//                                        shareEntity.images=commodityBean.getImages();
//                                        shareEntity.imageUrl=commodityBean.getImages().get(0);
//                                        shareEntity.shareURL= commodityBean.getGoodsSaleUrl();
//                                        ShareImageDialog shareDialog = new ShareImageDialog(mContext,shareEntity);
//                                        shareDialog.show();
                                        SharetUtil.shareMoreImage(commodityBean.getImages(),mContext,commodityBean.getDescription()+"\n"+"【 批发价格 】"+commodityBean.getPrice()+"\n"+"【 购买地址 】"+commodityBean.getGoodsSaleUrl());
                                    }
                                });
                            } else {
                                tip(jsonObject.optString("msg"));
                            }
                        }
                    }

                    @Override
                    public void onError(int Tag, String code, String msg) {
                    }

                    @Override
                    public void OnTimeOut(int Tag, boolean isShowTip) {

                    }

                    @Override
                    public void OnNetError(int Tag, boolean isShowTip) {
                    }
                });
                httpManager.request();
            }
        });
        resellGoodsDialog.show();
        resellGoodsDialog.setDataView(marketGoods);
    }
}
