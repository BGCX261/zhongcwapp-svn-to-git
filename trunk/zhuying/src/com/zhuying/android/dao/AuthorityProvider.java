package com.zhuying.android.dao;

import java.util.HashMap;

import com.zhuying.android.entity.AuthorityEntity;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;


/**
 * 分类
 */
public class AuthorityProvider extends ContentProvider {

	private static final int Category = 1;
	private static final int Category_ID = 2;

	private static UriMatcher sUriMatcher;
	private static HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(AuthorityEntity.AUTHORITY, AuthorityEntity.PATH, Category);
		sUriMatcher.addURI(AuthorityEntity.AUTHORITY, AuthorityEntity.PATH +"/#", Category_ID);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(AuthorityEntity._ID, AuthorityEntity._ID);
		sProjectionMap.put(AuthorityEntity.KEY_TENANTID, AuthorityEntity.KEY_TENANTID);
		sProjectionMap.put(AuthorityEntity.KEY_DATAID, AuthorityEntity.KEY_DATAID);
		sProjectionMap.put(AuthorityEntity.KEY_OWNERS, AuthorityEntity.KEY_OWNERS);
		sProjectionMap.put(AuthorityEntity.KEY_VISIBLETO, AuthorityEntity.KEY_VISIBLETO);
		sProjectionMap.put(AuthorityEntity.KEY_AUTHID, AuthorityEntity.KEY_AUTHID);
	}
	
	private static final String[] DEFAULT_PROJECTION = new String[] {
		AuthorityEntity._ID, 
		AuthorityEntity.KEY_TENANTID,
		AuthorityEntity.KEY_DATAID,
		AuthorityEntity.KEY_OWNERS,
		AuthorityEntity.KEY_VISIBLETO,
		AuthorityEntity.KEY_AUTHID,
	};


	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case Category:
			return AuthorityEntity.CONTENT_TYPE;
		case Category_ID:
			return AuthorityEntity.CONTENT_ITEM_TYPE;
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
		long rowId = db.replace(DatabaseHelper.TABLE_AUTHORITY, null, initialValues);
		if (rowId > 0) {
			Uri knowleageUri = ContentUris.withAppendedId(
					AuthorityEntity.CONTENT_URI, rowId);
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
		int count = db.update(DatabaseHelper.TABLE_AUTHORITY, values, where, whereArgs);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_AUTHORITY, (!TextUtils.isEmpty(where) ? where : ""), whereArgs);
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
		qb.setTables(DatabaseHelper.TABLE_AUTHORITY);
		qb.setProjectionMap(sProjectionMap);
		switch (sUriMatcher.match(uri)) {
		case Category:
			break;
		case Category_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = AuthorityEntity.DEFAULT_SORT_ORDER;
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