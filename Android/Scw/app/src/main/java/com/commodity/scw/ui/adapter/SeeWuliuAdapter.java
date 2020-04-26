package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.model.SeeWuliuEntity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;
import com.commodity.scw.utils.ScreenUtils;

import java.util.List;

/**
 * 物流
 * Created by yangxuqiang on 2017/4/4.
 */

public class SeeWuliuAdapter extends CommonAdapter<SeeWuliuEntity> {
    public SeeWuliuAdapter(Context context, List<SeeWuliuEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, SeeWuliuEntity seeWuliuEntity) {
        TextView viwuliu_compoinew = holder.getView(R.id.wuliu_compoin);//公司
        TextView wuliu_time = holder.getView(R.id.wuliu_time);//时间
        TextView wuliu_content = holder.getView(R.id.wuliu_content);//描述
        TextView wuliu_waybill = holder.getView(R.id.wuliu_waybill);//运单号
        ImageView imageView = holder.getView(R.id.wuliu_oval);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) wuliu_content.getLayoutParams();




        wuliu_time.setText("2014-12-12 12:12:12");
        if(holder.getPosition()==0){
            layoutParams.width= ScreenUtils.dp2px(mContext,16);
            layoutParams.height=ScreenUtils.dp2px(mContext,16);
            imageView.setLayoutParams(layoutParams);
            params.bottomMargin=ScreenUtils.dp2px(mContext,10);
            holder.setBackgroundRes(R.id.wuliu_oval,R.drawable.shap_wuliu_read);

            wuliu_content.setTextColor(0xFFBA0008);
            wuliu_waybill.setTextColor(0xFFBA0008);
            viwuliu_compoinew.setTextColor(0xFFBA0008);
            viwuliu_compoinew.setVisibility(View.VISIBLE);
            wuliu_waybill.setVisibility(View.VISIBLE);

        }else {
            layoutParams.width= ScreenUtils.dp2px(mContext,10);
            layoutParams.height=ScreenUtils.dp2px(mContext,10);
            wuliu_content.setTextColor(0xFF666666);
            params.bottomMargin=ScreenUtils.dp2px(mContext,40);
            imageView.setLayoutParams(layoutParams);
            holder.setBackgroundRes(R.id.wuliu_oval,R.drawable.icon_transparent);
            viwuliu_compoinew.setVisibility(View.GONE);
            wuliu_waybill.setVisibility(View.GONE);
        }

    }
}
