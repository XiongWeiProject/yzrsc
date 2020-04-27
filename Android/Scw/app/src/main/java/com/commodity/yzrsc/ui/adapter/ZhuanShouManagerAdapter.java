package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.SellerZhuanshouEntity;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 转售订单管理
 * Created by yangxuqiang on 2017/3/28.
 */

public class ZhuanShouManagerAdapter extends CommonAdapter<SellerZhuanshouEntity> {
    public ZhuanShouManagerAdapter(Context context, List<SellerZhuanshouEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, SellerZhuanshouEntity zhuanshouManagerEntity) {
        holder.setText(R.id.zhaunshoumanager_time,zhuanshouManagerEntity.getCreateTime());//时间
        holder.setText(R.id.zhaunshoumanager_state,zhuanshouManagerEntity.getState());//状态
        holder.setText(R.id.zhaunshoumanager_price,"¥"+zhuanshouManagerEntity.getTotal());//价格
        holder.setText(R.id.zhaunshoumanager_yuanjia,"¥"+zhuanshouManagerEntity.getOriginalAmount());//原价
        holder.setText(R.id.zhaunshoumanager_yuji,"¥"+ String.format("%.2f", zhuanshouManagerEntity.getTotalProfit()  ));//预计利润
        ImageView imageView = holder.getView(R.id.zhaunshoumanager_imag);//图片
        if (zhuanshouManagerEntity.getOrderGoods().get(0)!=null){
            holder.setText(R.id.zhaunshoumanager_content,zhuanshouManagerEntity.getOrderGoods().get(0).getDescription());//介绍
            ImageLoaderManager.getInstance().displayImage(zhuanshouManagerEntity.getOrderGoods().get(0).getImage(),imageView);
        }
    }
}
