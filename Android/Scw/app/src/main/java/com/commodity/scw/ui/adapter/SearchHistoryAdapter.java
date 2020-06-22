package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.view.View;


import com.commodity.yzrsc.R;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 作者：yangpeixing on 2016/4/13 18:22
 * 功能：历史搜索记录
 * 产权：南京婚尚
 */
public class SearchHistoryAdapter extends CommonAdapter<String> {
    List<String> datas;
    OnHistorySearchListener listener;

    public SearchHistoryAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
        this.datas = datas;
    }

    /**
     * 设置历史搜索监听
     * @param listener
     */
    public void setOnHistorySearchListener(OnHistorySearchListener listener) {
        this.listener = listener;
    }

    @Override
    public void convert(ViewHolder holder, final String s) {
        holder.setText(R.id.tv_name, s);
        holder.getView(R.id.bt_clear).setVisibility(View.GONE);
        if (holder.getPosition() == datas.size() - 1) {
            holder.getView(R.id.bt_clear).setVisibility(View.VISIBLE);
        }
        holder.getView(R.id.bt_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnClearBt();
                }
            }
        });
        holder.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.OnItemClick(s);
                }
            }
        });

    }

    public interface OnHistorySearchListener {
        /**
         * 清除历史记录按钮回调
         */
        public void OnClearBt();

        /**
         * item点击回调
         * @param search
         */
        public void OnItemClick(String search);
    }

}
