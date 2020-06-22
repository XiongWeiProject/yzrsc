package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.commodity.yzrsc.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.model.PersonInfo;
import com.commodity.scw.ui.activity.user.ContactPersonActivity;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;
import com.commodity.scw.ui.dialog.CommonDialog;
import com.commodity.scw.ui.widget.xlistView.PLA_AdapterView;
import com.commodity.scw.utils.RongIMUtil;

import java.util.List;

/**
 * Created by liyushen on 2017/4/3 22:10
 * 联系人适配器
 */
public class ContactPersonAdapter extends CommonAdapter<PersonInfo> {
    private Context context;
    public ContactPersonAdapter(Context context, List<PersonInfo> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=context;
    }

    @Override
    public void convert(final ViewHolder holder, final PersonInfo data) {
        holder.setText(R.id.tv_text1,data.getName());
        ImageLoaderManager.getInstance().displayImage(data.getAvatar(), (ImageView) holder.getView(R.id.view_ico1),R.drawable.ico_defalut_header);
        holder.setText(R.id.tv_text2,data.getReceivedTime().substring(0,16));
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RongIMUtil.startConversation(context, data.getImId(),data.getName());
            }
        });
        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ((ContactPersonActivity)mContext).deletePerson(holder.getPosition());
                return false;
            }
        });
    }
}