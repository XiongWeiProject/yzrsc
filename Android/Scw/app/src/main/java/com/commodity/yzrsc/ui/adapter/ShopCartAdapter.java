package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.commodity.yzrsc.model.CartBean;

import java.util.List;

import com.bumptech.glide.Glide;
import com.commodity.yzrsc.ui.activity.user.MyCartActivity;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.utils.DateUtil;
import com.commodity.yzrsc.view.RoundAngleImageView;

/**
 * Created by Administrator on 2018/1/6.
 */
public class ShopCartAdapter extends CommonAdapter<CartBean> {

    //构造方法
    public ShopCartAdapter(Context context, List<CartBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final ViewHolder holder, final CartBean cartBean) {
        ListView recyclerView = holder.getView(R.id.rcv_shop_car);
        ImageView iv_shopImag = holder.getView(R.id.iv_shopImag);
        holder.setText(R.id.tv_shopName, cartBean.getShopName());
        Glide.with(mContext).load(cartBean.getSellerAvatar()).error(R.drawable.rc_image_error).into(iv_shopImag);
        recyclerView.setAdapter(new ShopItemAdapter(mContext, cartBean.getShoppingCartGoods(), R.layout.item_shop_card));
        DateUtil.setListViewHeightBasedOnChildren(recyclerView);
    }

}
