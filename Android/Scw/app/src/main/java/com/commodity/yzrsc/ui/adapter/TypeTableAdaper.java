package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.http.BaseHttpManager;
import com.commodity.yzrsc.http.HttpManager;
import com.commodity.yzrsc.http.HttpMothed;
import com.commodity.yzrsc.http.IRequestConst;
import com.commodity.yzrsc.http.ServiceInfo;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.MarketGoods;
import com.commodity.yzrsc.model.TypeDataEntity;
import com.commodity.yzrsc.ottobus.BusProvider;
import com.commodity.yzrsc.ottobus.Event;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.ui.dialog.ResellGoodsDialog;
import com.commodity.yzrsc.ui.dialog.ResellSuccessDialog;
import com.commodity.yzrsc.utils.SharetUtil;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类列表
 * Created by yangxuqiang on 2017/3/25.
 */

public class TypeTableAdaper extends CommonAdapter<TypeDataEntity> {
    private Context context;
    private ResellGoodsDialog resellGoodsDialog;
    private List<TypeDataEntity> dataEntities;

    public TypeTableAdaper(Context context, List<TypeDataEntity> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=context;
        this.dataEntities=datas;
    }

    @Override
    public void convert(final ViewHolder holder, final TypeDataEntity typeTableEntity) {
        if(typeTableEntity.isReselled()){
            holder.setText(R.id.tv_zhuanhsou,"已转售");
        }else {
            holder.setText(R.id.tv_zhuanhsou,"转售");
        }
        holder.setOnClickListener(R.id.tv_zhuanhsou, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(typeTableEntity.isReselled()){
                    tip("已转售");
                    return;
                }
                if(resellGoodsDialog==null){
                    resellGoodsDialog=new ResellGoodsDialog(context);
                    resellGoodsDialog.setClickSubmitListener(new ResellGoodsDialog.OnClickSubmitListener() {
                        @Override
                        public void clickSubmit(String goodsSaleId, final String description, String resellPrice) {
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
                                            dataEntities.get(holder.getPosition()).setReselled(true);
                                            notifyDataSetChanged();
                                            BusProvider.getInstance().post(new Event.NotifyChangedView("HomeShopFragment"));
                                            final String goodsSaleurl=jsonObject.optString("data");
                                            ResellSuccessDialog resellSuccessDialog=new ResellSuccessDialog(mContext);
                                            resellSuccessDialog.show();
                                            resellSuccessDialog.setClickSubmitListener(new ResellSuccessDialog.OnClickSubmitListener() {
                                                @Override
                                                public void clickSubmit() {
//                                                    SharetUtil.toShare(mContext,typeTableEntity.getImages()[0],
//                                                            goodsSaleurl,"易州人商城",description, Arrays.asList(typeTableEntity.getImages()));
                                                    SharetUtil.shareMoreImage(Arrays.asList(typeTableEntity.getImages()),mContext,typeTableEntity.getDescription()+"\n"+"【 批发价格 】"+typeTableEntity.getPrice()+"元"+"\n"+"【 购买地址 】"+goodsSaleurl);

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
                resellGoodsDialog.setDataView(intitEntity(typeTableEntity));
            }
        });
        holder.setText(R.id.type_desc,typeTableEntity.getDescription());
        holder.setText(R.id.item_table_xianjia,"￥"+typeTableEntity.getPrice());
        holder.setText(R.id.item_table_yuanjia,"￥"+typeTableEntity.getSuggestedPrice());
        ((TextView) holder.getView(R.id.item_table_yuanjia)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//  中间加横线
        ImageView image = holder.getView(R.id.type_image_v);
        ImageLoaderManager.getInstance().displayImage(typeTableEntity.getImages()[0],image);
    }

    private MarketGoods intitEntity(TypeDataEntity typeTableEntity) {
        MarketGoods marketGoods = new MarketGoods();
        marketGoods.setDateTime(typeTableEntity.getUpdateTime());
        marketGoods.setDescribe(typeTableEntity.getDescription());
        marketGoods.setFavorite(typeTableEntity.isFavored());
        marketGoods.setId(String.valueOf(typeTableEntity.getId()));
        marketGoods.setPrice(String.valueOf(typeTableEntity.getPrice()));
        marketGoods.setSuggestedPrice(String.valueOf(typeTableEntity.getPrice().floatValue()));
        marketGoods.setVideo(typeTableEntity.getVideo());
        return marketGoods;
    }
}
