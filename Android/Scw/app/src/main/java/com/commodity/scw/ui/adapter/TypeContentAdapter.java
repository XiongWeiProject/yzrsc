package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.scw.manager.Constanct;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.model.store.TypeChildrenEntity;
import com.commodity.scw.ui.BaseFragment;
import com.commodity.scw.ui.activity.classify.TypeActivity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 分类内容
 * Created by yangxuqiang on 2017/3/20.
 */

public class TypeContentAdapter extends CommonAdapter<TypeChildrenEntity> {

    private BaseFragment baseFragment;

    public TypeContentAdapter(Context context, List<TypeChildrenEntity> datas, int layoutId, BaseFragment fragment) {
        super(context, datas, layoutId);
        baseFragment =fragment;

    }

    @Override
    public void convert(final ViewHolder holder, final TypeChildrenEntity typeContentEntity) {
        ImageView iamge = holder.getView(R.id.type_content_image);
//        if(holder.getPosition()==0){
//            holder.setText(R.id.type_content_text,"");
//            iamge.setVisibility(View.GONE);
//        }else {
//            iamge.setVisibility(View.VISIBLE);
//            TextView all = holder.getView(R.id.type_all);
//            all.setVisibility(View.INVISIBLE);
//            holder.setText(R.id.type_content_text,typeContentEntity.getName());
//            ImageLoaderManager.getInstance().displayImage(typeContentEntity.getImage(), (ImageView) holder.getView(R.id.type_content_image),R.drawable.ico_pic_fail_defalt);
//        }

        iamge.setVisibility(View.VISIBLE);
        TextView all = holder.getView(R.id.type_all);
        all.setVisibility(View.INVISIBLE);
        holder.setText(R.id.type_content_text,typeContentEntity.getName());
        ImageLoaderManager.getInstance().displayImage(typeContentEntity.getImage(), (ImageView) holder.getView(R.id.type_content_image),R.drawable.ico_pic_fail_defalt);


        holder.setOnClickListener(R.id.type_content_linear, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                //TODO 跳转到详情
                Bundle bundle = new Bundle();
                bundle.putInt(Constanct.KINDID,typeContentEntity.getId());
                baseFragment.jumpActivity(TypeActivity.class,bundle);
            }
        });
    }
}
