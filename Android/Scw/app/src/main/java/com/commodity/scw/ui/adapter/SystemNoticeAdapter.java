package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.view.View;

import com.commodity.yzrsc.R;
import com.commodity.scw.model.Notice;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by liyushen on 2017/4/3 22:21
 * 系统消息
 */
public class SystemNoticeAdapter extends CommonAdapter<Notice> {
    private Context context;
    public SystemNoticeAdapter(Context context, List<Notice> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=context;
    }

    @Override
    public void convert(final ViewHolder holder, Notice data) {
        holder.setText(R.id.tv_text1,data.getCreateTime());
        holder.setText(R.id.tv_text2,data.getContent());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
