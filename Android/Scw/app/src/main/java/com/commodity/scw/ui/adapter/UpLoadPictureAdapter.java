package com.commodity.scw.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.commodity.yzrsc.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.ui.activity.general.BigPictureActivity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * 上传宝贝
 * Created by 328789 on 2017/4/13.
 */

public class UpLoadPictureAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;
    private int layoutId;
    private View.OnClickListener listener;

    public UpLoadPictureAdapter(Context context, List<String> datas, int layoutId) {
        this.context=context;
        this.datas=datas;
        this.layoutId=layoutId;
    }

    @Override
    public int getCount() {
        return datas.size();
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
        Log.e("position*********",position+"");
        convertView=View.inflate(context,layoutId,null);
        ImageView upload_grid_imag= (ImageView) convertView.findViewById(R.id.upload_grid_imag);
        ImageView item_delete= (ImageView) convertView.findViewById(R.id.item_delete);

        if(position <datas.size()-1){
            ImageLoaderManager.getInstance().displayImage("file://"+datas.get(position),upload_grid_imag);
            item_delete.setVisibility(View.VISIBLE);
        }else {//添加按钮
            upload_grid_imag.setBackgroundColor(Color.parseColor("#ffffffff"));
            upload_grid_imag.setBackgroundResource(R.drawable.icon_add_imag);
            item_delete.setVisibility(View.GONE);
            upload_grid_imag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position ==datas.size()-1){
                        //添加图片
                        if(listener!=null){
                            listener.onClick(v);
                        }
                    }
                }
            });
            if(datas.size()== SPKeyManager.uploadmax){
                upload_grid_imag.setVisibility(View.GONE);
            }
        }

        item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(datas.get(position));
                if(file.exists()){
                    file.delete();
                }
                datas.remove(position);
                notifyDataSetChanged();
            }
        });

        LinearLayout shop_linearlaout= (LinearLayout) convertView.findViewById(R.id.shop_linearlaout);
        shop_linearlaout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(datas.size()-1!=position){
                    Intent intent = new Intent(context, BigPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("urls", (Serializable) datas);
                    bundle.putInt("index",position);
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
