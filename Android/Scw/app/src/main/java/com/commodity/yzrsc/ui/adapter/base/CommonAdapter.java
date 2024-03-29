package com.commodity.yzrsc.ui.adapter.base;

import android.content.Context;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.commodity.yzrsc.utils.CustomToast;

import java.util.List;


public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);


    /**
     * 方法名称：tip
     */
    protected void tip(String tipStr) {
        CustomToast.showToast(tipStr);
    }
    /**
     * 方法名称：tip
     */
    protected void tip(int tip) {
        tip(tip+"");
    }

    /**
     * 方法名称：tip
     */
    protected void tips(String tipStr) {
        Looper.prepare();
        Toast.makeText(mContext.getApplicationContext(),tipStr,Toast.LENGTH_LONG).show();
        Looper.loop();
    }
}
