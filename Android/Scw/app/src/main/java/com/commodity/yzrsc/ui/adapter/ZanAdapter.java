package com.commodity.yzrsc.ui.adapter;

import android.content.Context;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;

import java.util.List;

public class ZanAdapter extends BaseRecycleAdapter<DynamicAllListModel.likeList> {

    public ZanAdapter(Context context, List<DynamicAllListModel.likeList> data, int myLayoutId) {
        super(context, data, myLayoutId);
    }
    @Override
    protected void convert(Context context, BaseViewHolder holder, DynamicAllListModel.likeList likeList) {
        holder.setText(R.id.name,likeList.getMemberNickname());
    }
}
