package com.commodity.scw.ui.widget.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.R;


/**
 * 作者：liyushen on 2016/4/11 10:25
 * 功能：主页TabItem单个布局
 */
public class TabMenuItem extends LinearLayout {
    Context context;
    TextView tv_tip, tv_text;
    ImageView iv_icon;
    View view;
    int iconDrawaleID,iconClickDrawableID,textID,tipnum,tabIndex;

    public TabMenuItem(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        this.context = context;
        initView();
        initData();
    }
    /**
     * @param iconDrawaleID       默认图标
     * @param iconClickDrawableID 选中图标
     * @param textID              默认文本
     * @param tipnum              消息数目
     * @param tabIndex                  坐标
     * @return
     */
    public TabMenuItem(Context context, int iconDrawaleID, int iconClickDrawableID, int textID, final int tipnum, final int tabIndex) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        this.context = context;
        this.iconDrawaleID = iconDrawaleID;
        this.iconClickDrawableID = iconClickDrawableID;
        this.textID = textID;
        this.tipnum = tipnum;
        this.tabIndex = tabIndex;
        initView();
        initData();
    }

    public TabMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabMenuItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void initData() {

    }

    @SuppressLint("InflateParams")
	private void initView() {
        view= LayoutInflater.from(context).inflate(R.layout.item_tab, null);
        tv_tip = (TextView) view.findViewById(R.id.tv_tip);
        tv_text = (TextView) view.findViewById(R.id.tv_text);
        iv_icon = (ImageView) view.findViewById(R.id.iv_tab_icon);
        tv_tip.setVisibility(View.GONE);
        addView(view);
    }

    @SuppressWarnings("deprecation")
	public void setIcon(int drawableID) {
        iv_icon.setImageDrawable(getResources().getDrawable(drawableID));
    }

    public String getText() {
        return tv_text.getText().toString();
    }

    public void setText(int textID) {
        tv_text.setText(context.getString(textID));
    }

    public int getTipNum() {
        if (tv_tip.getVisibility() == View.GONE) {
            return 0;
        } else {
            return Integer.parseInt(tv_tip.getText().toString());
        }
    }

    public void setTipNum(int num) {
        if (num == 0) {
            tv_tip.setVisibility(View.GONE);
            return;
        }
        tv_tip.setVisibility(View.VISIBLE);
        tv_tip.setText("" + num);
    }

    public TextView getTextView(){
        return  tv_text;
    }

    public ImageView getIconImageView(){
        return  iv_icon;
    }

    public void setSelect(boolean select){
        if (select){
            iv_icon.setImageDrawable(getResources().getDrawable(iconClickDrawableID));
            tv_text.setText(context.getResources().getString(textID));
            if (tipnum == 0) {
                tv_tip.setVisibility(View.GONE);
            }else {
                tv_tip.setVisibility(View.VISIBLE);
            }
            tv_tip.setText("" + tipnum);
            tv_text.setTextColor(getResources().getColor(R.color.co_F95F3D));
        }else {
            iv_icon.setImageDrawable(context.getResources().getDrawable(iconDrawaleID));
            tv_text.setText(context.getResources().getString(textID));
            if (tipnum == 0) {
                tv_tip.setVisibility(View.GONE);
            }else {
                tv_tip.setVisibility(View.VISIBLE);
            }
            tv_tip.setText("" + tipnum);
            tv_text.setTextColor(getResources().getColor(R.color.second_black));
        }
    }
}
