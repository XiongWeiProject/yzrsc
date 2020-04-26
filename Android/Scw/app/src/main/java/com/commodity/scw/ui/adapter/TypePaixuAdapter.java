package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.ui.activity.classify.TypeActivity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 排序
 * Created by yangxuqiang on 2017/3/24.
 */

public class TypePaixuAdapter extends CommonAdapter<String> {
    private int position;

    public TypePaixuAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        TextView text = holder.getView(R.id.item_paixu_text);
        ImageView image = holder.getView(R.id.item_paixu_image);
        text.setText(s);
        if(position==holder.getPosition()){
            text.setTextColor(Color.parseColor("#C20008"));
            image.setVisibility(View.VISIBLE);
        }else {
            text.setTextColor(mContext.getResources().getColor(R.color.co_111A3B));
            image.setVisibility(View.GONE);
        }
    }
    public void setClickPosition(int position){
        this.position=position;
        notifyDataSetChanged();
        ((TypeActivity)mContext).initButtonView();
    }
}
