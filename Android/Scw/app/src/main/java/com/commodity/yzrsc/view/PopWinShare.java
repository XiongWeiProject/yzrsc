package com.commodity.yzrsc.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.commodity.yzrsc.R;

public class PopWinShare extends PopupWindow {
    private View mainView;
    private View mainView1;
    private TextView layoutShare, layoutCopy;
    private TextView layoutShare1, layoutCopy1;

    public PopWinShare(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2,int type){
        super(paramActivity);
        if (type==1){
            //窗口布局
            mainView = LayoutInflater.from(paramActivity).inflate(R.layout.layout_popwin, null);
            //分享布局
            layoutShare = ((TextView)mainView.findViewById(R.id.pic));
            //复制布局
            layoutCopy = (TextView)mainView.findViewById(R.id.video);
            //设置每个子布局的事件监听器
            if (paramOnClickListener != null){
                layoutShare.setOnClickListener(paramOnClickListener);
                layoutCopy.setOnClickListener(paramOnClickListener);
            }
            setContentView(mainView);
        }else {
            //窗口布局
            mainView1 = LayoutInflater.from(paramActivity).inflate(R.layout.layout_popwin1, null);
            //分享布局
            layoutShare1 = ((TextView)mainView1.findViewById(R.id.tv_like));
            //复制布局
            layoutCopy1 = (TextView)mainView1.findViewById(R.id.tv_evalution);
            //设置每个子布局的事件监听器
            if (paramOnClickListener != null){
                layoutShare1.setOnClickListener(paramOnClickListener);
                layoutCopy1.setOnClickListener(paramOnClickListener);
            }
            setContentView(mainView1);
        }

        //设置宽度
        setWidth(paramInt1);
        //设置高度
        setHeight(paramInt2);
        //设置显示隐藏动画
        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
        setBackgroundDrawable(new ColorDrawable(0));
    }
}
