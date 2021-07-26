package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.Constanct;
import com.commodity.yzrsc.model.HomeTypeModel;
import com.commodity.yzrsc.ui.BaseFragment;
import com.commodity.yzrsc.ui.activity.classify.TypeActivity;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;

import java.util.List;

public class HomeTypeAdapter extends BaseRecycleAdapter<HomeTypeModel> {
    private Context contexts;
    private BaseFragment baseFragment;
    public HomeTypeAdapter(Context context, List<HomeTypeModel> data, BaseFragment fragment) {
        super(context, data, R.layout.item_home_type);
        contexts = context;
        baseFragment =fragment;
    }

    @Override
    protected void convert(Context context, BaseViewHolder holder, final HomeTypeModel homeTypeModel) {
        ImageView imageView = holder.getView(R.id.iv_head);
        holder.setText(R.id.tv_title,homeTypeModel.getName());
        Glide.with(contexts).load(homeTypeModel.getImage()).error(R.drawable.rc_grid_image_default).into(imageView);
        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //TODO 跳转到详情
                Bundle bundle = new Bundle();
                bundle.putInt(Constanct.KINDID,homeTypeModel.getId());
                baseFragment.jumpActivity(TypeActivity.class,bundle);
            }
        });
    }

}
