package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.model.LiuyanEntity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;
import com.commodity.scw.utils.ScreenUtils;

import java.util.List;

/**
 * Created by 328789 on 2017/5/25.
 */

public class BrackAdapter extends CommonAdapter<LiuyanEntity> {

    public BrackAdapter(Context context, List<LiuyanEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final ViewHolder holder, LiuyanEntity liuyanEntity) {
        TextView buy = holder.getView(R.id.buy);
        LinearLayout t_l = holder.getView(R.id.t_l);
        TextView t_s = holder.getView(R.id.t_s);
        TextView t_c = holder.getView(R.id.t_c);
        TextView seller = holder.getView(R.id.seller);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) t_l.getLayoutParams();

        if(!liuyanEntity.isSeller()){//买家
            seller.setVisibility(View.INVISIBLE);
            t_s.setText("退款说明：");
            layoutParams.rightMargin= (int) ScreenUtils.px2dp(mContext,74);
            layoutParams.leftMargin= (int) ScreenUtils.px2dp(mContext,10);
        }else {
            buy.setVisibility(View.INVISIBLE);
            t_s.setText("拒绝退款：");
            layoutParams.rightMargin= (int) ScreenUtils.px2dp(mContext,10);
            layoutParams.leftMargin= (int) ScreenUtils.px2dp(mContext,74);
        }
        t_c.setText(liuyanEntity.getMessage());
        t_l.setLayoutParams(layoutParams);

        LinearLayout linear_picture = holder.getView(R.id.linear_picture);
        int v = (int) ScreenUtils.dp2px(mContext, 60);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(v,v);
        List<String> images = liuyanEntity.getImages();
        if(images!=null&&images.size()>0){
            for (String s:images){
                ImageView imageView = new ImageView(mContext);
                ImageLoaderManager.getInstance().displayImage(s,imageView);
                linear_picture.addView(imageView,params);
            }
        }

    }
}
