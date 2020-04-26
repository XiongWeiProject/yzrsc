package com.commodity.scw.ui.demo;/**
 * Created by lys on 2017/3/10.
 */

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.commodity.scw.R;
import com.commodity.scw.dp.DemoTableSQLiteHelper;
import com.commodity.scw.dp.DemoTableSqlUtil;
import com.commodity.scw.ui.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：liyushen on 2017/03/10 14:32
 * 功能：无
 */
public class SqlLiteActivity extends BaseActivity {
    private ListView listview;
    private ListAdapter listAdapter;
    private int POSTION=0;
    private EditText etCity;
    private EditText etCode;
    private Button bt_add;
    private Button bt_modify;
    private Button bt_query;
    private List<DemoTableSqlUtil.DemoBean> cityList = new ArrayList<>();
    DemoTableSqlUtil demoTableSqlUtil;
    @Override
    protected int getContentView() {
        return R.layout.activity_kkorm;
    }

    @Override
    protected void initView() {
        etCity = (EditText) findViewById(R.id.etCity);
        etCode = (EditText) findViewById(R.id.etCode);
        bt_add = (Button) findViewById(R.id.bt_add);
        bt_modify = (Button) findViewById(R.id.bt_modify);
        bt_query = (Button) findViewById(R.id.bt_query);
        demoTableSqlUtil=new DemoTableSqlUtil(this);
        restData();
        listview = (ListView)findViewById(R.id.listView);
        listAdapter = new ListAdapter();
        listview.setAdapter(listAdapter);
        listview.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion,
                                    long arg3) {
                setSelectedValues(postion);
            }
        });

    }

    @Override
    protected void initListeners() {
/* 插入表数据并ListView显示更新 */
        bt_add.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                if(etCity.getText().length() > 1 && etCode.getText().length() >1){
                    demoTableSqlUtil.insert(DemoTableSQLiteHelper.Column_name,etCity.getText().toString().trim(),DemoTableSQLiteHelper.Column_code,etCode.getText().toString().trim());
                    demoTableSqlUtil.queryAll();
                    restData();
                    listview.setAdapter(new ListAdapter());
                }
            }
        });

    	/* 查询表，模糊条件查询 */
        bt_query.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                demoTableSqlUtil.queryAll();
                restData();
                listview.setAdapter(new ListAdapter());
            }
        });

    	/* 修改表数据 */
        bt_modify.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                demoTableSqlUtil.update(cityList.get(POSTION).getId(),DemoTableSQLiteHelper.Column_name,etCity.getText().toString().trim(),DemoTableSQLiteHelper.Column_code,etCode.getText().toString().trim());
                demoTableSqlUtil.queryAll();
                restData();
                listview.setAdapter(new ListAdapter());
            }
        });
    }
    /* 设置选中ListView的值 */
    public void setSelectedValues(int postion){
        POSTION = postion;
        etCity.setText(cityList.get(postion).getCity());
        etCode.setText(cityList.get(postion).getCode());
    }

    /* 重值form */
    public void resetForm(){
        etCity.setText("");
        etCode.setText("");
    }
    private class ListAdapter extends BaseAdapter {
        public ListAdapter(){
            super();
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cityList.size();
        }

        @Override
        public Object getItem(int postion) {
            // TODO Auto-generated method stub
            return postion;
        }

        @Override
        public long getItemId(int postion) {
            // TODO Auto-generated method stub
            return postion;
        }

        @Override
        public View getView(final int postion, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.listview, null);
            TextView tv = (TextView) view.findViewById(R.id.tvCity);
            TextView tvCity2 = (TextView) view.findViewById(R.id.tvCity2);

            tv.setText("" + cityList.get(postion).getCity());
            tvCity2.setText("" + cityList.get(postion).getCode());
            TextView bu = (TextView) view.findViewById(R.id.btRemove);
            bu.setText("delete");
            bu.setId(Integer.parseInt(cityList.get(postion).getId()));

			/* 删除表数据 */
            bu.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View view) {
                    try{
                        demoTableSqlUtil.delete(cityList.get(postion).getId());
                        cityList.remove(postion);
                        listview.setAdapter(new ListAdapter());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
            return view;
        }
    }

    private void restData(){
        cityList.clear();
        for (int i = 0; i < demoTableSqlUtil.getResutList().size(); i++) {
            cityList.add(demoTableSqlUtil.getResutList().get(i));
        }
    }
}