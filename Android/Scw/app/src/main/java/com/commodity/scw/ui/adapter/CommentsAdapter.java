package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.commodity.scw.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.model.Comments;
import com.commodity.scw.model.Notice;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;
import com.commodity.scw.utils.HttpRequestUtil;

import java.util.List;

/**
 * Created by liyushen on 2017/4/3 22:37
 * 评论
 */
public class CommentsAdapter extends CommonAdapter<Comments> {
    private Context context;

    public CommentsAdapter(Context context, List<Comments> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context = context;
    }

    @Override
    public void convert(final ViewHolder holder, final Comments data) {
        holder.setText(R.id.tv_text1,data.getCreateTime());
        holder.setText(R.id.tv_text2,"订单编号："+data.getOrderCode());
        holder.setText(R.id.tv_text3,data.getEvaluationContent());
        ImageLoaderManager.getInstance().displayImage(data.getGoodsImage(), (ImageView) holder.getView(R.id.iv_image),R.drawable.ico_pic_fail_defalt);
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // HttpRequestUtil.ReadNotification(data.getId()+"","3");
            }
        });
    }
}
