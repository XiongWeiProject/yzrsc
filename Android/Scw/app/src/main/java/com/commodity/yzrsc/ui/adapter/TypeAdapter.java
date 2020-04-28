package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.widget.RelativeLayout;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.Comments;
import com.commodity.yzrsc.model.TypeModel;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;

import java.util.List;

public class TypeAdapter extends BaseRecycleAdapter<TypeModel> {

    public TypeAdapter(Context context, List<TypeModel> datas) {
        super(context, datas, R.layout.item_type);
    }


    @Override
    protected void convert(Context context, BaseViewHolder holder, TypeModel typeModel) {
        holder.setText(R.id.tv_title,typeModel.getName());
    }
}
