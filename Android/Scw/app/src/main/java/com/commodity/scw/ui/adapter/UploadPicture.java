package com.commodity.scw.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.commodity.scw.R;
import com.commodity.scw.ui.adapter.base.CommonAdapter;
import com.commodity.scw.ui.adapter.base.ViewHolder;

import java.util.List;

/**
 * 上传图片
 * Created by yangxuqiang on 2017/4/4.
 */

public class UploadPicture extends CommonAdapter<String> {
    private View.OnClickListener listener;
    private int position;

    public UploadPicture(Context context, List<String> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, String bitmap) {
        position = holder.getPosition();
        ImageView delete = holder.getView(R.id.item_delete);
        if(position ==mDatas.size()-1){//添加按钮
            holder.setBackgroundRes(R.id.upload_grid_imag,R.drawable.icon_add_imag);
            delete.setVisibility(View.GONE);
            holder.setOnClickListener(R.id.upload_grid_imag, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position ==mDatas.size()-1){
                        //添加图片
                        if(listener!=null){
                            listener.onClick(v);
                        }
                    }
                }
            });
        }else {
            ImageView imageView = holder.getView(R.id.upload_grid_imag);
            imageView.setImageBitmap(BitmapFactory.decodeFile(bitmap));
            delete.setVisibility(View.VISIBLE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(position);
                notifyDataSetChanged();
            }
        });

    }
    public void addPictureListener(View.OnClickListener listener){
        this.listener=listener;
    }

}
