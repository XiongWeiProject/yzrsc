package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.model.mine.DetailMyOrdeEntity;
import com.commodity.scw.model.mine.MyOrdeGoodsEntity;
import com.commodity.scw.ui.activity.personalcenter.SeeWuliu;
import com.commodity.scw.ui.activity.personalcenter.orde.MyOrdeActivity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 我的订单
 * Created by yangxuqiang on 2017/4/3.
 */

public class MyordeAdapter extends CommonAdapter<DetailMyOrdeEntity> implements View.OnClickListener {
    private int index;
    private OnitemListener onitemListener;
    private int position;

    public MyordeAdapter(Context context, List<DetailMyOrdeEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final ViewHolder holder, final DetailMyOrdeEntity detailMyOrdeEntity) {
        switch (detailMyOrdeEntity.getState()) {
            case "订单已提交"://待付款
                index = 1;
                break;
            case "买家已付款,等待卖家发货"://待发货
                index = 2;
                break;
            case "卖家已发货"://已发货
                index = 3;
                break;
            case "买家已收货,交易成功"://交易成功
                index = 4;
                break;
            case "仲裁中"://退货
            case "买家已申请退款":
            case "卖家拒绝退款":
                index = 5;
                break;
            case "订单已取消"://交易关闭
            case "订单已关闭":
            case "已退款":
                index = 6;
                break;
        }
        Button item_button_seewuliu = holder.getView(R.id.item_button_seewuliu);
        LinearLayout item_linera1 = holder.getView(R.id.item_linera1);
        LinearLayout item_linear2 = holder.getView(R.id.item_linear2);
        LinearLayout item_content = holder.getView(R.id.item_content);
        TextView zhaunshoumanager_yuji = holder.getView(R.id.zhaunshoumanager_yuji2);
        zhaunshoumanager_yuji.setVisibility(View.INVISIBLE);
//        zhaunshoumanager_yuji.setText("¥ "+detailMyOrdeEntity.);//预计利润
        TextView zhaunshoumanager_yuanjia = holder.getView(R.id.zhaunshoumanager_yuanjia2);//原价
        zhaunshoumanager_yuanjia.setVisibility(View.INVISIBLE);
//        zhaunshoumanager_yuanjia.setText(detailMyOrdeEntity.);
        TextView zhaunshoumanager_price = holder.getView(R.id.zhaunshoumanager_price2);//合计
        zhaunshoumanager_price.setText("¥"+detailMyOrdeEntity.getTotal());

        holder.getView(R.id.yuan).setVisibility(View.INVISIBLE);
        holder.getView(R.id.li).setVisibility(View.INVISIBLE);

        TextView zhaunshoumanager_content = holder.getView(R.id.zhaunshoumanager_content2);//描述
        List<MyOrdeGoodsEntity> orderGoods = detailMyOrdeEntity.getOrderGoods();
        if(orderGoods!=null&&orderGoods.size()!=0){
            zhaunshoumanager_content.setText(orderGoods.get(0).getDescription());
            ImageView zhaunshoumanager_imag = holder.getView(R.id.zhaunshoumanager_imag2);//图片
            ImageLoaderManager.getInstance().displayImage(orderGoods.get(0).getImage(),zhaunshoumanager_imag);
        }

        TextView zhaunshoumanager_state = holder.getView(R.id.zhaunshoumanager_state2);//状态
        zhaunshoumanager_state.setText(detailMyOrdeEntity.getState());
        TextView zhaunshoumanager_time = holder.getView(R.id.zhaunshoumanager_time2);//时间
        zhaunshoumanager_time.setText(detailMyOrdeEntity.getCreateTime());


        position = holder.getPosition();
        if(index==3||index==4){//显示物流按钮
            item_linear2.setVisibility(View.GONE);
            item_linera1.setVisibility(View.GONE);
            item_button_seewuliu.setVisibility(View.VISIBLE);
        }else {
            item_linear2.setVisibility(View.VISIBLE);
            item_linera1.setVisibility(View.VISIBLE);
            item_button_seewuliu.setVisibility(View.GONE);
        }

        item_button_seewuliu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //物流
                MyOrdeActivity myorde=(MyOrdeActivity)mContext;
                Bundle bundle1 = new Bundle();
                bundle1.putString("orderId",String.valueOf(detailMyOrdeEntity.getId()));
                myorde.jumpActivity(SeeWuliu.class,bundle1);
            }
        });
        item_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onitemListener!=null){
                    onitemListener.itemClick(v,holder.getPosition());
                }
            }
        });
    }
    public void setIndex(int index){
//        this.index=index;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_button_seewuliu:

                break;
//            case R.id.item_content:
//                if(onitemListener!=null){
//                    onitemListener.itemClick(view,mHolder.getPosition());
//                }
//                break;
        }
    }
    public void setOnitemListener(OnitemListener onitemListener){
        this.onitemListener=onitemListener;
    }
    public interface OnitemListener{
        void itemClick(View v, int position);
    }
}
