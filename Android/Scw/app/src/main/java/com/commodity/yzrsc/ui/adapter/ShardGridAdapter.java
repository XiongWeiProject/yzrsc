package com.commodity.yzrsc.ui.adapter;

import android.content.Context;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.store.ShardGridEntity;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by yangxuqiang on 2017/5/8.
 */

public class ShardGridAdapter extends CommonAdapter<ShardGridEntity> {
    public ShardGridAdapter(Context context, List<ShardGridEntity> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ShardGridEntity shardGridEntity) {
        holder.setImageResource(R.id.item_proview_id,shardGridEntity.getId());
        holder.setText(R.id.item_proview_title,shardGridEntity.getTitle());
    }
}
