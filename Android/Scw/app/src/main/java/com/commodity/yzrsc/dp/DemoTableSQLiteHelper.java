package com.commodity.yzrsc.dp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 实现对表的创建、更新、变更列名操作
 * @author ytm0220@163.com
 *
 */
public class DemoTableSQLiteHelper extends SQLiteOpenHelper {
	public static String TB_NAME = "demoTable";
	public static String Column_id = "_id";
	public static String Column_name = "name";
	public static String Column_code = "code";
	public static int DB_VERSION = 2;

	public DemoTableSQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		this.TB_NAME=name;
	}

	/**
	 * 创建新表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + "(" + Column_id + " integer primary key," + Column_name + " varchar," + Column_code+ " integer"+
				")");
	}

	/**
	 * 当检测与前一次创建数据库版本不一样时，先删除表再创建新表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		onCreate(db);
	}

	/**
	 * 变更列名
	 * @param db
	 * @param oldColumn
	 * @param newColumn
	 * @param typeColumn
	 */
	public void updateColumn(SQLiteDatabase db, String oldColumn, String newColumn, String typeColumn){
		try{
			db.execSQL("ALTER TABLE " +
					TB_NAME + " CHANGE " +
					oldColumn + " "+ newColumn +
					" " + typeColumn
			);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
