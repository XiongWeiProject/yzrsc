package com.commodity.yzrsc.ui.adapter;

import android.content.Context;

import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;

import java.util.List;

public class HomeAdapter extends CommonAdapter<String> {

	public HomeAdapter(Context context, List<String> datas, int layoutId) {
		super(context, datas, layoutId);
	}

	@Override
	public void convert(ViewHolder holder, String t) {
	//	holder.setText(R.id.tvCity,t);
		//holder.setImageBitmapFromJar(R.id.item_imageview, t, R.drawable.icons_my_header);
//		holder.setImageBitmapFromQuick(R.id.item_imageview, t, R.drawable.icons_my_header);

	}
}
 