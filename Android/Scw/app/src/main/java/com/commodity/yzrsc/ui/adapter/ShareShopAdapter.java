package com.commodity.yzrsc.ui.adapter;

import android.content.Context;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * Created by 328789 on 2017/6/6.
 */

public class ShareShopAdapter extends CommonAdapter<Object> {

    private final int[] drawable = new int[]{ R.drawable.selector_button_weixin_friend, R.drawable.selector_button_weixin_link,R.drawable.selector_button_qq
            ,R.drawable.selector_button_qq_spance,R.drawable.selector_button_sine,R.drawable.selector_button_save,R.drawable.selector_button_copy};
    private final String[] title = new String[]{"微信好友","朋友圈连接","QQ","QQ空间","新浪微博","保存图片","复制连接"};

    public ShareShopAdapter(Context context, List<Object> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(final ViewHolder holder, Object t) {
        holder.setBackgroundRes(R.id.item_proview_id,drawable[holder.getPosition()]);
        holder.setText(R.id.item_proview_title,title[holder.getPosition()]);
    }
}
