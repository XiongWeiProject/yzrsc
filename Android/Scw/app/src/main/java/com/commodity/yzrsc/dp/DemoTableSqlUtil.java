package com.commodity.yzrsc.dp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：liyushen on 2017/03/10 14:58
 * 功能：无
 */
public class DemoTableSqlUtil {
    private DemoTableSQLiteHelper dbHelper;
    private Context context;
    private SQLiteDatabase db;
    private Cursor cursor;
    private List<DemoBean> cityList = new ArrayList<>();
    public DemoTableSqlUtil(Context context) {
        super();
        this.context = context;
        initTable();
    }

    /* 初始化并创建数据库 */
    private void initTable() {
        try {
            /* 初始化并创建数据库 */
            dbHelper = new DemoTableSQLiteHelper(context, DemoTableSQLiteHelper.TB_NAME, null, DemoTableSQLiteHelper.DB_VERSION);
    		/* 创建表 */
            db = dbHelper.getWritableDatabase();    //调用SQLiteHelper.OnCreate()
        	queryAll();
        } catch (IllegalArgumentException e) {
            //当用SimpleCursorAdapter装载数据时，表ID列必须是_id，否则报错column '_id' does not exist
            e.printStackTrace();
            //当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表 注：表以前数据将丢失
            ++DemoTableSQLiteHelper.DB_VERSION;
            dbHelper.onUpgrade(db, --DemoTableSQLiteHelper.DB_VERSION, DemoTableSQLiteHelper.DB_VERSION);
//    		dbHelper.updateColumn(db, SQLiteHelper.ID, "_"+SQLiteHelper.ID, "integer");
        }
    }
    //插入一条数据
    public void insert(String... args){
        ContentValues values = new ContentValues();
        for (int i = 0; i < args.length; i=i+2) {
            values.put(args[i],args[i+1]);
        }
        //插入数据 用ContentValues对象也即HashMap操作,并返回ID号
         db.insert(DemoTableSQLiteHelper.TB_NAME, DemoTableSQLiteHelper.Column_id, values);
    }

    //删除一条数据
    public void delete(String id){
        db.delete(DemoTableSQLiteHelper.TB_NAME, DemoTableSQLiteHelper.Column_id + "=" + id, null);
    }

    //修改一条数据
    public void update(String id, String... args){
        ContentValues values = new ContentValues();
        for (int i = 0; i < args.length; i=i+2) {
            values.put(args[i],args[i+1]);
        }
        db.update(DemoTableSQLiteHelper.TB_NAME, values, DemoTableSQLiteHelper.Column_id + "=" + id, null);
    }

    //查询所有结果
    public void queryAll(){
        cityList.clear();
        /* 查询表，得到cursor对象 */
        cursor = db.query(DemoTableSQLiteHelper.TB_NAME, null, null, null, null, null,DemoTableSQLiteHelper.Column_code + " DESC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && (cursor.getString(1) != null)) {
            DemoBean city = new DemoBean();
            city.setId(cursor.getString(0));
            city.setCity(cursor.getString(1));
            city.setCode(cursor.getString(2));
            cityList.add(city);
            cursor.moveToNext();
        }
    }

    //查询所有结果
    public void query(String... args){
        cityList.clear();
        String key = "";
        if (args!=null){
            for (int i = 0; i <args.length ; i++) {
                key= args[i]+ " and" +key;
            }
        }
        cursor = db.query(true, DemoTableSQLiteHelper.TB_NAME,
                new String[]{DemoTableSQLiteHelper.Column_id, DemoTableSQLiteHelper.Column_name, DemoTableSQLiteHelper.Column_code},
                key, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast() && (cursor.getString(1) != null)){
            DemoBean city = new DemoBean();
            city.setId(cursor.getString(0));
            city.setCity(cursor.getString(1));
            city.setCode(cursor.getString(2));
            cityList.add(city);
            cursor.moveToNext();
        }
    }
    public List<DemoBean> getResutList(){
        return cityList;
    }

    public class DemoBean {
        private String id;
        private String city;
        private String code;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
