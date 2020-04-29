package com.commodity.yzrsc.ui.adapter;

import android.content.Context;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.rongyun.ui.adapter.BaseAdapter;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;

import java.util.List;

public class DynamicListAdapter extends CommonAdapter<DynamicAllListModel> {
    public DynamicListAdapter(Context context, List<DynamicAllListModel> datas) {
        super(context, datas, R.layout.item_dynamic_list);
    }

    @Override
    public void convert(ViewHolder holder, DynamicAllListModel dynamicAllListModel) {

    }
}
