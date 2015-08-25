package com.ufida.gkb.dao;

import com.ufida.gkb.entity.AccountEntity;
import com.ufida.gkb.entity.StoreEntity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";
	private static final String DATABASE_NAME = "shop.db";
	private static final int DATABASE_VERSION = 1;

	private Context context;

	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists account ("
				+ AccountEntity.Model.KEY_ROWID + " integer primary key,"
				+ AccountEntity.Model.KEY_CONTACTNAME + " text null,"
				+ AccountEntity.Model.KEY_ACCOUNTNAME + " text null,"
				+ AccountEntity.Model.KEY_NOTE + " text null,"
				+ AccountEntity.Model.KEY_PHONE + " text null,"
				+ AccountEntity.Model.KEY_MOBILE + " text null,"
				+ AccountEntity.Model.KEY_EMAIL + " text null,"
				+ AccountEntity.Model.KEY_WEBSITE + " text null,"
				+ AccountEntity.Model.KEY_ISDEL + " integer null,"
				+ AccountEntity.Model.KEY_CREATETIME + " text null,"
				+ AccountEntity.Model.KEY_UPDATETIME + " text null,"
				+ AccountEntity.Model.KEY_OWNERUSERID + " integer null,"
				+ AccountEntity.Model.KEY_ORGID + " integer  null,"
				+ AccountEntity.Model.KEY_ADDRESS + " text  null);");

		db.execSQL("create table if not exists store ("
				+ StoreEntity.Model.KEY_ROWID + " integer primary key,"
				+ StoreEntity.Model.KEY_NUMBER + " text null,"
				+ StoreEntity.Model.KEY_NAME + " text null,"
				+ StoreEntity.Model.KEY_SPEC + " text null,"
				+ StoreEntity.Model.KEY_UNIT + " text null,"
				+ StoreEntity.Model.KEY_AMOUNT + " text null,"
				+ StoreEntity.Model.KEY_COST + " text null,"
				+ StoreEntity.Model.KEY_MONEY + " text  null);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS account");
		db.execSQL("DROP TABLE IF EXISTS store");

		onCreate(db);
	}
}
