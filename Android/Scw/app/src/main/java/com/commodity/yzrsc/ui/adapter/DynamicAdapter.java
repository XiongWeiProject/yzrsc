package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.Banner;
import com.commodity.yzrsc.model.HomeTypeModel;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;

import java.util.List;

public class DynamicAdapter extends BaseRecycleAdapter<Banner> {
    private Context contexts;

    public DynamicAdapter(Context context, List<Banner> data) {
        super(context, data, R.layout.item_dnamic);
        contexts = context;
    }

    @Override
    protected void convert(Context context, BaseViewHolder holder, Banner homeTypeModel) {
        ImageView imageView = holder.getView(R.id.iv_pic);
        Glide.with(contexts).load(homeTypeModel.getMediaUrl()).error(R.drawable.ico_pic_fail_defalt).into(imageView);

    }
}
