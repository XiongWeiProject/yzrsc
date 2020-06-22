package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.commodity.yzrsc.R;
import com.commodity.scw.ui.activity.classify.TypeActivity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 分类筛选价格
 * Created by yangxuqiang on 2017/3/23.
 */

public class PriceGridAdapter extends CommonAdapter<String> {
    private TypeActivity context;
    private int position;

    public PriceGridAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=(TypeActivity)context;
    }

    @Override
    public void convert(final ViewHolder holder, String s) {
        Button button = holder.getView(R.id.grid_button_price);
        button.setText(s);
        if(holder.getPosition()==position){
            button.setTextColor(Color.parseColor("#FFFFFF"));
            button.setBackgroundResource(R.drawable.shap_button_red);
        }else {
            button.setTextColor(Color.parseColor("#3C3636"));
            button.setBackgroundResource(R.drawable.shap_button_namal);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClickPosition(holder.getPosition());
                context.priceClik(holder.getPosition());
            }
        });
    }
    public void setClickPosition(int position){
        this.position=position;
        notifyDataSetChanged();
        context.initButtonView();
    }

}
