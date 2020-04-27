package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.ImgType;
import com.commodity.yzrsc.model.MarketGoods;
import com.commodity.yzrsc.ui.BaseActivity;
import com.commodity.yzrsc.ui.activity.commodity.CommodityDetailActivity;
import com.commodity.yzrsc.ui.activity.user.MyFavoriteCommodActivity;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.ui.widget.customview.GoodsLinearLayout;
import com.commodity.yzrsc.utils.ImageUtil;

import java.util.List;

/**
 * Created by liyushen on 2017/4/2 11:35
 * 收藏列表adapter
 */
public class FavoriteGoodsAdapter extends CommonAdapter<MarketGoods> {
    private Context context;
    public FavoriteGoodsAdapter(Context context, List<MarketGoods> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=context;
    }

    @Override
    public void convert(final ViewHolder holder, final MarketGoods data) {
        holder.getView(R.id.hsc_scrollview).scrollTo(0, 0);
        holder.setText(R.id.tv_text1, data.getDescribe());
        holder.setText(R.id.tv_text2, "￥" + data.getPrice());
        holder.setText(R.id.tv_text3, "￥" + data.getSuggestedPrice());
        ((TextView) holder.getView(R.id.tv_text3)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//  中间加横线

        if (data.isOnsale()){
            holder.setText(R.id.btn_lijigoumai, "立即购买");
        }else {
            holder.setText(R.id.btn_lijigoumai, "已失效"  );
        }
        GoodsLinearLayout goodsLinearLayout=holder.getView(R.id.ll_childview);
        goodsLinearLayout.setImgs(data.getImgTypes());
        goodsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.isOnsale()){
                    return;
                }
                Bundle bundle=new Bundle();
                bundle.putString("goodsSaleId",data.getId());
                ((MyFavoriteCommodActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
            }
        });
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!data.isOnsale()){
                    return;
                }
                Bundle bundle=new Bundle();
                bundle.putString("goodsSaleId",data.getId());
                ((MyFavoriteCommodActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
            }
        });
        goodsLinearLayout.setOnItemCickListen(new GoodsLinearLayout.OnItemCickListen() {
            @Override
            public void onClick(ImgType imgType, int postion) {
                if (!data.isOnsale()){
                    return;
                }
                if (imgType.isVideo()){
                    ImageUtil.jumbVideoPlayer(context, data.getVideo(), data.getVideoSnap(), data.getDescribe());
                }else {
                    Bundle bundle=new Bundle();
                    bundle.putString("goodsSaleId",data.getId());
                    if(context instanceof BaseActivity){
                        ((BaseActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
                    }else {
                        ((MyFavoriteCommodActivity) context).jumpActivity(CommodityDetailActivity.class,bundle);
                    }
                }
            }
        });
       holder.setOnClickListener(R.id.btn_lijigoumai, new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (data.isOnsale()){
                   ((MyFavoriteCommodActivity) context).lijiGoumai(holder.getPosition());
               }
           }
       });
        holder.setOnClickListener(R.id.btn_quxiaoshoucang, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyFavoriteCommodActivity) context).quxiaoShoucang(holder.getPosition());
            }
        });

    }
}