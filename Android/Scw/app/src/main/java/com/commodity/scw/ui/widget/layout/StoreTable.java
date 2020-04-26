package com.commodity.scw.ui.widget.layout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.R;

/**
 * Created by yangxuqiang on 2017/3/25.
 */

public class StoreTable extends LinearLayout {
    private String text;
    private Context context;
    private View h;
    private View v;
    private TextView textView;

    public StoreTable(Context context,String text) {
        super(context);
        this.context=context;
        this.text=text;
        initView();
    }

    private void initView() {
        View button = LayoutInflater.from(context).inflate(R.layout.table_store, null);
        h = button.findViewById(R.id.store_h);
        v = button.findViewById(R.id.store_v);
        textView = (TextView) button.findViewById(R.id.store_text);
        textView.setText(text);
        addView(button);
    }
    public void select(boolean flag){
        if(flag){
            textView.setTextColor(Color.parseColor("#BA0008"));
            h.setVisibility(View.VISIBLE);
        }else {
            textView.setTextColor(Color.parseColor("#3C3636"));
            h.setVisibility(View.INVISIBLE);
        }
    }
    public void invisibleV(){
        v.setVisibility(INVISIBLE);
    }
    public void invisibleH(){
        h.setVisibility(INVISIBLE);
    }
    public void setTestSize(int size){
        textView.setTextSize(size);
    }
    public void setVWidth(int width){
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.width=width;
        v.setLayoutParams(layoutParams);
    }
}
