package com.commodity.scw.ui.adapter;

import android.content.Context;

import com.commodity.scw.R;
import com.commodity.scw.model.TixianJiluEntity;
import com.commodity.scw.model.mine.TixianjiluDataEntity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 提心记录
 * Created by yangxuqiang on 2017/3/29.
 */

public class TixianjiluAdapter extends CommonAdapter<TixianjiluDataEntity> {
    public TixianjiluAdapter(Context context, List<TixianjiluDataEntity> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, TixianjiluDataEntity tixianJiluEntity) {
        holder.setText(R.id.jilu_money,"¥ "+tixianJiluEntity.getAmount());
        holder.setText(R.id.jilu_request,"¥ "+tixianJiluEntity.getStatus());
        holder.setText(R.id.jilu_date,tixianJiluEntity.getCreateTime());
    }
}
