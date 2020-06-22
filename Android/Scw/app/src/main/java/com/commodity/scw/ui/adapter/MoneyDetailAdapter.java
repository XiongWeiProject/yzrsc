package com.commodity.scw.ui.adapter;

import android.content.Context;

import com.commodity.yzrsc.R;
import com.commodity.scw.model.MoneyDetailEntity;
import com.commodity.scw.model.mine.TixianjiluDataEntity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 钱包明细
 * Created by yangxuqiang on 2017/3/29.
 */

public class MoneyDetailAdapter extends CommonAdapter<TixianjiluDataEntity> {
    public MoneyDetailAdapter(Context context, List<TixianjiluDataEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, TixianjiluDataEntity moneyDetailEntity) {
//        holder.setImageBitmap(R.id.maingxi_image,"");//图片
        holder.setText(R.id.minxi_desc,moneyDetailEntity.getTransactionType());
        holder.setText(R.id.minxin_time,moneyDetailEntity.getCreateTime());
        holder.setText(R.id.minxin_money,"¥ "+moneyDetailEntity.getAmount());
    }
}
