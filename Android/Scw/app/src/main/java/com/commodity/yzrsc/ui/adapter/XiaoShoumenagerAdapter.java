package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.mine.DetailMyOrdeEntity;
import com.commodity.yzrsc.model.mine.MyOrdeGoodsEntity;
import com.commodity.yzrsc.ui.activity.personalcenter.SeeWuliu;
import com.commodity.yzrsc.ui.activity.personalcenter.XiaoShouActivity;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 销售管理
 * Created by yangxuqiang on 2017/3/26.
 */

public class XiaoShoumenagerAdapter extends CommonAdapter<DetailMyOrdeEntity>  {

    private OnitemListener onitemListener;
    private int index;
    OrderListAdapter orderListAdapter;
    private Context contexts;
    public XiaoShoumenagerAdapter(Context context, List<DetailMyOrdeEntity> datas, int layoutId) {
        super(context, datas, layoutId);
        contexts = context;
    }

    @Override
    public void convert(final ViewHolder holder, final DetailMyOrdeEntity xiaoShoumenagerEntity) {
        switch (xiaoShoumenagerEntity.getState()) {
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
        holder.setText(R.id.xiaoshou_time,xiaoShoumenagerEntity.getCreateTime());//时间
        holder.setText(R.id.xiaoshou_state,xiaoShoumenagerEntity.getState());//状态
        holder.setText(R.id.xiaoshou_price,"¥"+xiaoShoumenagerEntity.getTotal());//价格
        holder.setText(R.id.zhaunshoumanager_yuanjia2,"¥"+xiaoShoumenagerEntity.getOriginalAmount());
        holder.setText(R.id.zhaunshoumanager_yuji2,"¥"+xiaoShoumenagerEntity.getTotalProfit());

        TextView price = holder.getView(R.id.xiaoshou_price);
        RecyclerView item_rcv = holder.getView(R.id.rcv_oder_list);
        Button xiaoshou_button_seewuliu = holder.getView(R.id.xiaoshou_button_seewuliu);
        LinearLayout xiaoshou_content = holder.getView(R.id.xiaoshou_content);

        LinearLayout item_linera3 = holder.getView(R.id.item_linera3);
        LinearLayout item_linear4 = holder.getView(R.id.item_linear4);

        List<MyOrdeGoodsEntity> orderGoods = xiaoShoumenagerEntity.getOrderGoods();
        if (orderGoods != null && orderGoods.size() != 0) {
            item_rcv.setLayoutManager(new LinearLayoutManager(contexts));
            orderListAdapter = new OrderListAdapter(contexts, orderGoods, R.layout.item_order_list);
            item_rcv.setAdapter(orderListAdapter);
        }
        xiaoshou_button_seewuliu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //物流
                XiaoShouActivity myorde=(XiaoShouActivity)mContext;
                Bundle bundle = new Bundle();
                bundle.putString("orderId",String.valueOf(xiaoShoumenagerEntity.getId()));
                bundle.putString("sourceType", "xiaoshou");
                myorde.jumpActivity(SeeWuliu.class,bundle);
            }
        });
        xiaoshou_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onitemListener!=null){
                    onitemListener.itemClick(v,holder.getPosition());
                }
            }
        });
        item_linear4.setVisibility(View.INVISIBLE);
        item_linera3.setVisibility(View.INVISIBLE);
        if(index==3||index==4){
            item_linear4.setVisibility(View.GONE);
            item_linera3.setVisibility(View.GONE);
            price.setTextColor(Color.parseColor("#BA0008"));
            xiaoshou_button_seewuliu.setVisibility(View.VISIBLE);
        }else {
            price.setTextColor(Color.parseColor("#BA0008"));
            xiaoshou_button_seewuliu.setVisibility(View.GONE);
        }


    }

    public void setIndex(int index){
        this.index=index;
        notifyDataSetChanged();
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.xiaoshou_button_seewuliu:
//                //物流
//                XiaoShouActivity myorde=(XiaoShouActivity)mContext;
//                myorde.jumpActivity(SeeWuliu.class);
//                break;
//            case R.id.xiaoshou_content:
//                if(onitemListener!=null){
//                    onitemListener.itemClick(view,position);
//                }
//                break;
//        }
//    }
    public void setOnitemListener(OnitemListener onitemListener){
        this.onitemListener=onitemListener;
    }
    public interface OnitemListener{
        void itemClick(View v, int position);
    }
}
