package com.commodity.scw.ui.widget.layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.commodity.scw.R;


/**
 * 上拉菜单popupwindow
 * Created by yangxuqiang on 2017/3/11.
 */

public class TakePopupWin extends PopupWindow {

    private int linearlayoutId;
    private BaseAdapter adapter;
    private int layout;
    private int listviewId;
    private View popup;
    private OnItemClickListener onItemClickListener;

    public TakePopupWin(Context context, int layout,int lineatlayoutId,int listviewId, BaseAdapter adapter) {
        super(context);
        this.layout=layout;
        this.adapter=adapter;
        this.listviewId=listviewId;
        this.linearlayoutId=lineatlayoutId;
        init(context);
    }

    private void init(Context context) {
        popup = View.inflate(context, layout, null);

        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_bg));

        popup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout linearLayout = (LinearLayout) popup.findViewById(linearlayoutId);
                int top = linearLayout.getTop();
                int y = (int) event.getY();
                if(event.getAction()== MotionEvent.ACTION_UP&&y<top){
                    dismiss();
                }
                return false;
            }
        });
        this.setContentView(popup);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(0xffffff));
        this.setAnimationStyle(R.style.take_popupwin);

        ListView popupLitView = (ListView) popup.findViewById(listviewId);
        popupLitView.setAdapter(adapter);
        popupLitView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(parent,view,position,id);
                    dismiss();
                }
            }
        });
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
}
