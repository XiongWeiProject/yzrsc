package com.commodity.scw.ui.adapter;

import android.content.Context;

import com.commodity.scw.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.scw.ui.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 商品列表
 * Created by yangxuqiang on 2017/3/26.
 */

public class TypeLsitRecycleAdapter extends BaseRecycleAdapter<String> {
    public TypeLsitRecycleAdapter(Context context, List<String> data, int myLayoutId) {
        super(context, data, myLayoutId);
    }

    @Override
    protected void convert(Context context, BaseViewHolder holder, String s) {

    }
}
