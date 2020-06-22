package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.http.BaseHttpManager;
import com.commodity.scw.http.HttpManageNew;
import com.commodity.scw.http.HttpManager;
import com.commodity.scw.http.HttpMothed;
import com.commodity.scw.http.IRequestConst;
import com.commodity.scw.http.ServiceInfo;
import com.commodity.scw.manager.ConfigManager;
import com.commodity.scw.model.ImgType;
import com.commodity.scw.model.MarketGoods;
import com.commodity.scw.model.ShareEntity;
import com.commodity.scw.ottobus.BusProvider;
import com.commodity.scw.ottobus.Event;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.ui.activity.HomeFragmentActivity;
import com.commodity.scw.ui.activity.commodity.CommodityDetailActivity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;
import com.commodity.scw.ui.dialog.ResellGoodsDialog;
import com.commodity.scw.ui.dialog.ResellSuccessDialog;
import com.commodity.scw.ui.dialog.ShareImageDialog;
import com.commodity.scw.ui.widget.customview.GoodsLinearLayout;
import com.commodity.scw.utils.ImageUtil;
import com.commodity.scw.utils.SharetUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * Created by liyushen on 2017/3/19 15:04
 * 首页市场商品的adapter
 */
public class MarketGoodsAdapter extends CommonAdapter<MarketGoods> {
    private Context context;
    Drawable ico_love;
    Drawable ico_love_on;
    ResellGoodsDialog resellGoodsDialog;
    private List<MarketGoods> dataslist;
    public MarketGoodsAdapter(Context context, List<MarketGoods> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context = context;
        this.dataslist=datas;
        ico_love = context.getResources().getDrawable(R.drawable.ico_love);
        ico_love_on = context.getResources().getDrawable(R.drawable.ico_love_on);
        ico_love.setBounds(0, 0, ico_love.getMinimumWidth(), ico_love.getMinimumHeight());
        ico_love_on.setBounds(0, 0, ico_love_on.getMinimumWidth(), ico_love_on.getMinimumHeight());
    }

    @Override
    public void convert(final ViewHolder holder, final MarketGoods data) {
        holder.getView(R.id.hsc_scrollview).scrollTo(0, 0);
        holder.setText(R.id.tv_text1, data.getDescribe());
        holder.setText(R.id.tv_text2, "￥" + data.getPrice());
        holder.setText(R.id.tv_text3, "￥" + data.getSuggestedPrice());

        if (data.getShopId().equals(ConfigManager.instance().getUser().getId())){
            holder.setVisible(R.id.tv_text5,false);
            holder.setVisible(R.id.tv_text6,false);
        }else {
            holder.setVisible(R.id.tv_text5,true);
            holder.setVisible(R.id.tv_text6,true);
        }

        ((TextView) holder.getView(R.id.tv_text3)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//  中间加横线
        holder.setText(R.id.tv_text4, data.getDateTime());
        TextView textView = holder.getView(R.id.tv_text5);
        if (data.isFavorite()) {
            textView.setText("已收藏");
            textView.setTag("已收藏");
            textView.setCompoundDrawables(null, null, ico_love_on, null);
        } else {
            textView.setText("收藏");
            textView.setTag("收藏");
            textView.setCompoundDrawables(null, null, ico_love, null);
        }

        TextView tv_text6 = holder.getView(R.id.tv_text6);
        if (data.isReselled()) {
            tv_text6.setText("已转售");
            tv_text6.setTag("已转售");
        } else {
            tv_text6.setText("转售");
            tv_text6.setTag("转售");
        }
        GoodsLinearLayout goodsLinearLayout = holder.getView(R.id.ll_childview);
        goodsLinearLayout.setImgs(data.getImgTypes());

        goodsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("goodsSaleId",data.getId());
                if(context instanceof BaseActivity){
                    ((BaseActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
                }else {
                    ((HomeFragmentActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
                }
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("goodsSaleId",data.getId());
                if(context instanceof BaseActivity){
                    ((BaseActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
                }else {
                    ((HomeFragmentActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
                }

            }
        });
        goodsLinearLayout.setOnItemCickListen(new GoodsLinearLayout.OnItemCickListen() {
            @Override
            public void onClick(ImgType imgType, int postion) {
                if (imgType.isVideo()){
                    ImageUtil.jumbVideoPlayer(context, data.getVideo(), data.getVideoSnap(), data.getDescribe());
                }else {
                    Bundle bundle=new Bundle();
                    bundle.putString("goodsSaleId",data.getId());
                    if(context instanceof BaseActivity){
                        ((BaseActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
                    }else {
                        ((HomeFragmentActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
                    }
                }
            }
        });
        holder.setOnClickListener(R.id.tv_text5, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getTag().equals("收藏")) {
                    ((TextView)view).setText("已收藏");
                    ((TextView)view).setTag("已收藏");
                    tip("已收藏");
                    favorGoods(data.getId());
                    ((TextView)view).setCompoundDrawables(null, null, ico_love_on, null);
                }else {
                    ((TextView)view).setText("收藏");
                    ((TextView)view).setTag("收藏");
                    tip("取消收藏");
                    favorGoods(data.getId());
                    ((TextView)view).setCompoundDrawables(null, null, ico_love, null);
                }
            }
        });
        holder.setOnClickListener(R.id.tv_text6, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showResellGoods(holder.getPosition(),data);
            }
        });
    }

   private void favorGoods(String goodsSaleId){
       Map<String, Object> parmMap = new HashMap<String, Object>();
       JSONArray array=new JSONArray();
       array.put(goodsSaleId);
       parmMap.put("goodSaleIds",array);
       parmMap.put("memberId", ConfigManager.instance().getUser().getId());
       HttpManageNew httpManager = new HttpManageNew(0, HttpMothed.POST,
               IRequestConst.RequestMethod.CancelFavorGoods, parmMap, new BaseHttpManager.IRequestListener() {
           @Override
           public void onPreExecute(int Tag) {

           }

           @Override
           public void onSuccess(int Tag, ServiceInfo result) {

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

    //转售
    private void showResellGoods(final int postion, final MarketGoods marketGoods){
        if (dataslist.get(postion).isReselled()){
            tip("已转售");
            return;
        }
        if (resellGoodsDialog==null){
            resellGoodsDialog=new ResellGoodsDialog(context);
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
                            JSONObject jsonObject= (JSONObject) result.getResponse();
                            if (jsonObject!=null){
                                if (jsonObject.optBoolean("success")){
                                    dataslist.get(postion).setReselled(true);
                                    notifyDataSetChanged();
                                    BusProvider.getInstance().post(new Event.NotifyChangedView("HomeShopFragment"));
                                    final String goodsSaleurl=jsonObject.optString("data");
                                    ResellSuccessDialog resellSuccessDialog=new ResellSuccessDialog(mContext);
                                    resellSuccessDialog.show();
                                    resellSuccessDialog.setClickSubmitListener(new ResellSuccessDialog.OnClickSubmitListener() {
                                        @Override
                                        public void clickSubmit() {
                                            ImgType[] imgTypes = dataslist.get(postion).getImgTypes();
                                            List<String> urls=new ArrayList<String>();
                                            for (int i=0;i<imgTypes.length;i++){
                                                if(!imgTypes[i].isVideo()){
                                                    urls.add(imgTypes[i].getImgpath());
                                                }
                                            }
                                            String imgpath="";
                                            ImgType imgType = dataslist.get(postion).getImgTypes()[0];
                                            if(imgType.isVideo()){
                                                imgpath = dataslist.get(postion).getImgTypes()[1].getImgpath();
                                            }else {
                                                imgpath=imgType.getImgpath();
                                            }
//                                            SharetUtil.toShare(mContext,imgpath,
//                                                    goodsSaleurl,"文巴掌商城",description,urls);
//                                            ShareEntity shareEntity = new ShareEntity();
//                                            shareEntity.desc=description+"\n"+"【 批发价格 】"+resellPrice+"元"+"\n"+"【 购买地址 】"+goodsSaleurl;
//                                            shareEntity.images=urls;
//                                            shareEntity.imageUrl=imgpath;
//                                            shareEntity.shareURL= goodsSaleurl;
//                                            ShareImageDialog shareDialog = new ShareImageDialog(mContext,shareEntity);
//                                            shareDialog.show();
                                            SharetUtil.shareMoreImage(urls,mContext,description+"\n"+"【 批发价格 】"+resellPrice+"元"+"\n"+"【 购买地址 】"+goodsSaleurl);

                                        }
                                    });
                                }else {
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
        }
        resellGoodsDialog.show();
        resellGoodsDialog.setDataView(marketGoods);
    }
}