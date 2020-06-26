package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.model.CartBean;

import java.util.List;

import com.bumptech.glide.Glide;
import com.commodity.yzrsc.ui.activity.user.MyCartActivity;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.view.RoundAngleImageView;

/**
 * Created by Administrator on 2018/1/6.
 */
public class ShopCartAdapter extends CommonAdapter<CartBean> {
    private Context context;
    private int shopId = 0;

    //构造方法
    public ShopCartAdapter(Context context, List<CartBean> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context = context;
    }

    @Override
    public void convert(final ViewHolder holder, final CartBean cartBean) {
        ImageView iv_minus, iv_goodsAdd, iv_delete;
        RoundAngleImageView iv_shopImag, iv_goodsImage;
        final TextView tv_shopName, tv_goodsName, tv_goodsNum, tv_goodsPrice;
        LinearLayout ll_shopcart_header;
        ll_shopcart_header = holder.getView(R.id.ll_shopcart_header);
        iv_shopImag = holder.getView(R.id.iv_shopImag);
        iv_goodsImage = holder.getView(R.id.iv_goodsImage);
        iv_minus = holder.getView(R.id.iv_minus);
        iv_goodsAdd = holder.getView(R.id.iv_goodsAdd);
        tv_shopName = holder.getView(R.id.tv_shopName);
        tv_goodsName = holder.getView(R.id.tv_goodsName);
        tv_goodsNum = holder.getView(R.id.tv_goodsNum);
        tv_goodsPrice = holder.getView(R.id.tv_goodsPrice);
        iv_delete = holder.getView(R.id.iv_delete);
        if (cartBean.getId() > 0) {
            if (cartBean.getShopId() == shopId) {
                //不是第一条数据 && ShopId与上一条数据不一致  ->   GONE
                ll_shopcart_header.setVisibility(View.GONE);
            } else {
                ll_shopcart_header.setVisibility(View.VISIBLE);
            }
        } else {
            shopId = cartBean.getShopId();
            ll_shopcart_header.setVisibility(View.VISIBLE);
        }
        //数据填充
        tv_goodsName.setText(cartBean.getGoodsSaleName());
        tv_shopName.setText(cartBean.getShopName());
        tv_goodsPrice.setText("¥" + Double.valueOf(cartBean.getGoodsPrice()).toString());
        tv_goodsNum.setText(cartBean.getQuantity() + "");
        Glide.with(context).load(cartBean.getGoodsImage()).into(iv_goodsImage);
        Glide.with(mContext).load(cartBean.getSellerAvatar()).error(R.drawable.rc_image_error).into(iv_shopImag);
        //给每个item->GoodsMin绑定点击事件，数量的改变
        iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartBean.getQuantity() > 1) {
                    int count = cartBean.getQuantity() - 1;
                    ((MyCartActivity) context).changeCartQuantity(holder.getPosition(), count);
//                    cartBean.setQuantity(count);
//                    tv_goodsNum.setText(cartBean.getQuantity() + "");
//                    notifyDataSetChanged();
                }
            }
        });
        //给每个item->GoodsAdd绑定点击事件，数量的改变
        iv_goodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = cartBean.getQuantity() + 1;
                ((MyCartActivity) context).changeCartQuantity(holder.getPosition(), count);
//                cartBean.setQuantity(count);
//                tv_goodsNum.setText(cartBean.getQuantity() + "");
//                notifyDataSetChanged();
            }
        });
        //每个item删除的点击事件
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyCartActivity) context).deleteCart(holder.getPosition());
            }
        });
    }

}
