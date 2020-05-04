package com.commodity.yzrsc.ui.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ImageLoaderManager;
import com.commodity.yzrsc.model.DynamicAllListModel;
import com.commodity.yzrsc.ui.adapter.base.BaseRecycleAdapter;
import com.commodity.yzrsc.ui.adapter.base.BaseViewHolder;
import com.commodity.yzrsc.ui.adapter.base.CommonAdapter;
import com.commodity.yzrsc.ui.adapter.base.ViewHolder;
import com.commodity.yzrsc.utils.VideoUtils;

import java.util.List;

public class ShowPicAdapter extends BaseRecycleAdapter<String> {
    private int type  = 0;//默认是图片
    private int layouts;
    private Context contexts;
    private View inflate;
    private ItemClickListener listener;
    public ShowPicAdapter(Context context, List<String> datas, int layout,int types) {
        super(context, datas, layout);
        type = types;
        layouts = layout;
        contexts = context;
        type = types;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflate = LayoutInflater.from(contexts).inflate(layouts, null);
        return new BaseViewHolder(inflate);
    }
    @Override
    protected void convert(Context context, final BaseViewHolder holder, String s) {
        if(listener!=null){
            inflate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(v,holder.getLayoutPosition(),type);
                }
            });
        }
        ImageView head = holder.getView(R.id.iv_grid_imag);
        ImageLoaderManager.getInstance().displayImage(s, head,
                R.drawable.ico_pic_fail_defalt);
//        if (type==1){
//            head.setImageBitmap( VideoUtils.getBitmapFormUrl(s));
//        }else {
//            ImageLoaderManager.getInstance().displayImage(s, head,
//                    R.drawable.ico_pic_fail_defalt);
//        }

    }

    public void setOnItemClickListener(ItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public ItemClickListener getOnItemClickListener() {
        return listener;
    }

    public interface ItemClickListener{
        public abstract void itemClick(View v, int position,int type);
    }

}
