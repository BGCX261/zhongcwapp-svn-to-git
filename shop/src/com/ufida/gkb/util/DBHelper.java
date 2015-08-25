//package com.zhongcw.android.util;
//
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DBHelper extends SQLiteOpenHelper {
//	private static final String DATABASE_NAME = "crm";
//	private static final int DATABASE_VERSION = 1;
//	
//	private String sql;
//
//	public String getSql() {
//		return sql;
//	}
//	public void setSql(String sql) {
//		this.sql = sql;
//	}
//
//	/** Create a helper object for the Events database */
//	public DBHelper(Context ctx) {
//		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
//	}
//
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		db.execSQL(getSql());
//	}
//
//	@Override
//	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
////		db.execSQL(getSql());
////		onCreate(db);
//	}
//}
//
