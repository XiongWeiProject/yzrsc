package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.mine.MyOrdeGoodsEntity;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * created by  xiongwei
 * on 订单多个列表
 * role:
 */
public class OrderListAdapter extends BaseRecycleAdapter<MyOrdeGoodsEntity> {

    public OrderListAdapter(Context context, List<MyOrdeGoodsEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    protected void convert(Context context, BaseViewHolder holder, MyOrdeGoodsEntity myOrdeGoodsEntity) {
        TextView zhaunshoumanager_content = holder.getView(R.id.zhaunshoumanager_content2);//描述
        zhaunshoumanager_content.setText(myOrdeGoodsEntity.getDescription());
        ImageView zhaunshoumanager_imag = holder.getView(R.id.zhaunshoumanager_imag2);//图片
        ImageLoaderManager.getInstance().displayImage(myOrdeGoodsEntity.getImage(),zhaunshoumanager_imag);
    }

}
