package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.view.View;

import com.commodity.scw.R;
import com.commodity.scw.model.AdressDetail;
import com.commodity.scw.ui.activity.user.UserAdressListActivity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by liyushen on 2017/4/23 15:34
 * 地址列表
 */
public class AdressListAdaper extends CommonAdapter<AdressDetail> {
    private Context context;

    public AdressListAdaper(Context context, List<AdressDetail> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context = context;
    }

    @Override
    public void convert(final ViewHolder holder, final AdressDetail data) {
        holder.setText(R.id.tv_text1,data.getReceiver()+"      "+data.getMobile());
        holder.setText(R.id.tv_text2,data.getProvince()+data.getCity()+data.getDistrict()+data.getAddressDetail());
        if (data.isSelect()){
            holder.setImageDrawable(R.id.iv_isselect,context.getResources().getDrawable(R.drawable.icon_xzoff));
        }else {
            holder.setImageDrawable(R.id.iv_isselect,context.getResources().getDrawable(R.drawable.icon_xzon));
        }
        holder.setOnClickListener(R.id.iv_isselect, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserAdressListActivity)context).selectArdress(holder.getPosition());
            }
        });
        holder.setOnClickListener(R.id.tv_text3, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserAdressListActivity)context).editArdress(holder.getPosition());
            }
        });
        holder.setOnClickListener(R.id.tv_text4, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((UserAdressListActivity)context).deleteArdress(holder.getPosition());
            }
        });
    }
}
