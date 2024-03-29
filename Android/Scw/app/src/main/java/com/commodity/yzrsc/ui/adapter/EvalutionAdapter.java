package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.model.Evalution;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;

import java.util.List;

public class EvalutionAdapter extends BaseRecycleAdapter<DynamicAllListModel.commentList> {
    public EvalutionAdapter(Context context, List<DynamicAllListModel.commentList> data, int myLayoutId) {
        super(context, data, myLayoutId);
    }
    @Override
    protected void convert(Context context, BaseViewHolder holder, DynamicAllListModel.commentList commentList) {
        String strMsg = "今天<font color=\"#00ff00\">天气不错</font>";
        TextView textView = holder.getView(R.id.name);
        if (commentList.getCommentType()==0){
            textView.setText(Html.fromHtml("<font color=\"#516490\">"+commentList.getMemberNickname()+":"+"</font>"+commentList.getComment()));
        }else {
            textView.setText(Html.fromHtml("<font color=\"#516490\">"+commentList.getMemberNickname()+":"+"</font>"+"回复"+"<font color=\"#516490\">"+commentList.getReplyiedNickname()+":"+"</font>"+commentList.getComment()));
        }
    }
}
