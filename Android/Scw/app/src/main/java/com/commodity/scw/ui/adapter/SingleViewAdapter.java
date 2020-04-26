package com.commodity.scw.ui.adapter;

import android.content.Context;

import com.commodity.scw.R;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 选择银行卡
 * Created by yangxuqiang on 2017/4/3.
 */

public class SingleViewAdapter extends CommonAdapter<String> {

    private int position;

    public SingleViewAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.single_bank,s);
        if(holder.getPosition()==position){
            holder.setBackgroundRes(R.id.single_select_bg,R.drawable.icon_xzoff);
        }else {
            holder.setBackgroundRes(R.id.single_select_bg,R.drawable.icon_xzon);
        }
    }
    public void setClickPosition(int position){
        this.position=position;
        notifyDataSetChanged();
    }
}
