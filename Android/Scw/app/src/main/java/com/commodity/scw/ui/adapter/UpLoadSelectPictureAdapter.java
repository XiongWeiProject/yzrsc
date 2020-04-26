package com.commodity.scw.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commodity.scw.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.ui.activity.general.BigPictureActivity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by liyushen on 2017/6/3 23:15
 * 上传图片选择适配器
 */
public class UpLoadSelectPictureAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;
    private int layoutId;
    private View.OnClickListener listener;

    public UpLoadSelectPictureAdapter(Context context, List<String> datas, int layoutId) {
        this.context=context;
        this.datas=datas;
        this.layoutId=layoutId;
    }

    @Override
    public int getCount() {
        return datas.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView=View.inflate(context,layoutId,null);
        ImageView upload_grid_imag= (ImageView) convertView.findViewById(R.id.upload_grid_imag);
        ImageView item_delete= (ImageView) convertView.findViewById(R.id.item_delete);

        if(position==datas.size()){//添加按钮
            upload_grid_imag.setBackgroundColor(Color.parseColor("#ffffffff"));
            upload_grid_imag.setBackgroundResource(R.drawable.icon_add_imag);
            item_delete.setVisibility(View.GONE);
            upload_grid_imag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position ==datas.size()){
                        //添加图片
                        if(listener!=null){
                            listener.onClick(v);
                        }
                    }
                }
            });
            if(datas.size()==12){
                upload_grid_imag.setVisibility(View.GONE);
            }
        }else {
            ImageLoaderManager.getInstance().displayImage("file://"+datas.get(position),upload_grid_imag,R.drawable.ico_pic_fail_defalt);
            item_delete.setVisibility(View.VISIBLE);
        }

        item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.remove(position);
                notifyDataSetChanged();
            }
        });
        LinearLayout shop_linearlaout= (LinearLayout) convertView.findViewById(R.id.shop_linearlaout);
        shop_linearlaout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datas.size()!=position){
                    Intent intent = new Intent(context, BigPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("urls", (Serializable) datas);
                    bundle.putInt("index",position-1);
                    intent.putExtras(bundle);
                    ((Activity)context).startActivity(intent);
                }
            }
        });

        return convertView;
    }

    public void addPictureListener(View.OnClickListener listener){
        this.listener=listener;
    }
}
