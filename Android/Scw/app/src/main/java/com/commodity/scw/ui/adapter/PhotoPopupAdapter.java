package com.commodity.scw.ui.adapter;

import android.content.Context;

import com.commodity.scw.R;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by yangxuqiang on 2017/3/25.
 */

public class PhotoPopupAdapter extends CommonAdapter<String> {
    public PhotoPopupAdapter(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String s) {
        holder.setText(R.id.phone_button,s);
    }
}
