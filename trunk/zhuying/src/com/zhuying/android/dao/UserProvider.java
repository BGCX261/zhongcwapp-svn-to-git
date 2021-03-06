package com.zhuying.android.dao;

import java.util.HashMap;

import com.zhuying.android.entity.UserEntity;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 用户
 */
public class UserProvider extends ContentProvider {

	private static final int User = 1;
	private static final int User_ID = 2;

	private static UriMatcher sUriMatcher;
	private static HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(UserEntity.AUTHORITY, UserEntity.PATH, User);
		sUriMatcher.addURI(UserEntity.AUTHORITY, UserEntity.PATH +"/#", User_ID);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(UserEntity._ID, UserEntity._ID);
		sProjectionMap.put(UserEntity.KEY_NAME, UserEntity.KEY_NAME);
		sProjectionMap.put(UserEntity.KEY_EMAIL, UserEntity.KEY_EMAIL);
		sProjectionMap.put(UserEntity.KEY_LASTLOGIN, UserEntity.KEY_LASTLOGIN);
		sProjectionMap.put(UserEntity.KEY_REALNAME, UserEntity.KEY_REALNAME);
		sProjectionMap.put(UserEntity.KEY_ISLOGIN, UserEntity.KEY_ISLOGIN);
		sProjectionMap.put(UserEntity.KEY_USERID, UserEntity.KEY_USERID);
		sProjectionMap.put(UserEntity.KEY_CREATEDAT, UserEntity.KEY_CREATEDAT);
		sProjectionMap.put(UserEntity.KEY_UPDATEDAT, UserEntity.KEY_UPDATEDAT);
	}
	
	private static final String[] DEFAULT_PROJECTION = new String[] {
		UserEntity._ID, 
		UserEntity.KEY_NAME,
		UserEntity.KEY_EMAIL,
		UserEntity.KEY_LASTLOGIN,
		UserEntity.KEY_REALNAME,
		UserEntity.KEY_ISLOGIN,
		UserEntity.KEY_USERID,
		UserEntity.KEY_CREATEDAT,
		UserEntity.KEY_UPDATEDAT
	};


	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case User:
			return UserEntity.CONTENT_TYPE;
		case User_ID:
			return UserEntity.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}
	
	/**
	 * 新增
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.replace(DatabaseHelper.TABLE_USER, null, initialValues);
		if (rowId > 0) {
			Uri knowleageUri = ContentUris.withAppendedId(
					UserEntity.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(knowleageUri, null);
			return knowleageUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}
	
	/**
	 * 更新
	 */
	@Override
	public int update(Uri uri, ContentValues values, String where,
			String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.update(DatabaseHelper.TABLE_USER, values, where, whereArgs);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_USER, (!TextUtils.isEmpty(where) ? where : ""), whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/**
	 * 查询
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(DatabaseHelper.TABLE_USER);
		qb.setProjectionMap(sProjectionMap);
		switch (sUriMatcher.match(uri)) {
		case User:
			break;
		case User_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = UserEntity.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection == null ? DEFAULT_PROJECTION : projection,
				selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

}