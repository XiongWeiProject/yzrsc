package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.commodity.scw.R;
import com.commodity.scw.model.store.TypeChildrenEntity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by yangxuqiang on 2017/3/24.
 */

public class ItemResultAdapter extends CommonAdapter<TypeChildrenEntity> {
    private int position;

    public ItemResultAdapter(Context context, List<TypeChildrenEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, TypeChildrenEntity s) {
        holder.setText(R.id.item_result_text,s.getName());
        ImageView image = holder.getView(R.id.item_result_image);
        if(position==holder.getPosition()){
            holder.setTextColor(R.id.item_result_text, Color.parseColor("#BA0008"));
            image.setVisibility(View.VISIBLE);
        }else {
            image.setVisibility(View.INVISIBLE);
            holder.setTextColorRes(R.id.item_result_text, R.color.co_111A3B);
        }
    }
    public void setClickPosition(int position,List<TypeChildrenEntity> datas){
        this.position=position;
//        if(datas!=null){
//            mDatas.clear();
//            mDatas.addAll(datas);
//        }

        notifyDataSetChanged();
    }
}
