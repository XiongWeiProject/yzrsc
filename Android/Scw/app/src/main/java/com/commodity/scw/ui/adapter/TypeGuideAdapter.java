package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.graphics.Color;

import com.commodity.scw.R;
import com.commodity.scw.model.store.TypeContextEntity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 分类引导栏
 * Created by yangxuqiang on 2017/3/20.
 */

public class TypeGuideAdapter extends CommonAdapter<TypeContextEntity> {

    private int clickIndex;

    public TypeGuideAdapter(Context context, List<TypeContextEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, TypeContextEntity s) {
        if(clickIndex==holder.getPosition()){
            holder.setBackgroundRes(R.id.item_type_guide, R.color.type_9F9F9);
//            holder.setTextColor(R.id.item_type_guide, R.color.type_BA0008);
            holder.setTextColor(R.id.item_type_guide, Color.parseColor("#BA0008"));
        }else {
            holder.setBackgroundRes(R.id.item_type_guide, R.color.type_FFFFFF);
            holder.setTextColorRes(R.id.item_type_guide, R.color.co_111A3B);
        }
        holder.setText(R.id.item_type_guide,s.getName());

    }
    public void setClickIndex(int index){
        clickIndex =index;
        notifyDataSetChanged();
    }
}
