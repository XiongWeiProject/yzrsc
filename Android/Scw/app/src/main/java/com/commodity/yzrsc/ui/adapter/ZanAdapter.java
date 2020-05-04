package com.commodity.yzrsc.ui.adapter;

import android.content.Context;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;

import java.util.List;

public class ZanAdapter extends BaseRecycleAdapter<String> {

    public ZanAdapter(Context context, List<String> data, int myLayoutId) {
        super(context, data, myLayoutId);
    }

    @Override
    protected void convert(Context context, BaseViewHolder holder, String s) {
        holder.setText(R.id.name,s);
    }
}
