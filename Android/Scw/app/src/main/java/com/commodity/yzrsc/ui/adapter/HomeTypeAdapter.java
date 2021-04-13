package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.HomeTypeModel;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;

import java.util.List;

public class HomeTypeAdapter extends BaseRecycleAdapter<HomeTypeModel> {
    private Context contexts;

    public HomeTypeAdapter(Context context, List<HomeTypeModel> data) {
        super(context, data, R.layout.item_home_type);
        contexts = context;
    }

    @Override
    protected void convert(Context context, BaseViewHolder holder, HomeTypeModel homeTypeModel) {
        ImageView imageView = holder.getView(R.id.iv_head);
        holder.setText(R.id.tv_title,homeTypeModel.getName());
        Glide.with(contexts).load(homeTypeModel.getImage()).error(R.drawable.rc_grid_image_default).into(imageView);

    }
}
