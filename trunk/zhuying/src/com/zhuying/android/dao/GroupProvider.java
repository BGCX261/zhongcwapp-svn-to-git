package com.zhuying.android.dao;

import java.util.HashMap;

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

import com.zhuying.android.entity.GroupEntity;

/**
 * 组
 */
public class GroupProvider extends ContentProvider {

	private static final int GROUP = 1;
	private static final int GROUP_ID = 2;

	private static UriMatcher sUriMatcher;
	private static HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(GroupEntity.AUTHORITY, GroupEntity.PATH, GROUP);
		sUriMatcher.addURI(GroupEntity.AUTHORITY, GroupEntity.PATH +"/#", GROUP_ID);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(GroupEntity._ID, GroupEntity._ID);
		sProjectionMap.put(GroupEntity.KEY_NAME, GroupEntity.KEY_NAME);
		sProjectionMap.put(GroupEntity.KEY_GROUPID, GroupEntity.KEY_GROUPID);
		sProjectionMap.put(GroupEntity.KEY_CREATEDAT, GroupEntity.KEY_CREATEDAT);
		sProjectionMap.put(GroupEntity.KEY_UPDATEDAT, GroupEntity.KEY_UPDATEDAT);
	}
	
	private static final String[] DEFAULT_PROJECTION = new String[] {
		GroupEntity._ID, 
		GroupEntity.KEY_NAME,
		GroupEntity.KEY_GROUPID,
		GroupEntity.KEY_CREATEDAT,
		GroupEntity.KEY_UPDATEDAT,
	};


	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case GROUP:
			return GroupEntity.CONTENT_TYPE;
		case GROUP_ID:
			return GroupEntity.CONTENT_ITEM_TYPE;
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
		long rowId = db.replace(DatabaseHelper.TABLE_GROUPS, null, initialValues);
		if (rowId > 0) {
			Uri knowleageUri = ContentUris.withAppendedId(
					GroupEntity.CONTENT_URI, rowId);
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
		int count = db.update(DatabaseHelper.TABLE_GROUPS, values, where, whereArgs);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_GROUPS, (!TextUtils.isEmpty(where) ? where : ""), whereArgs);
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
		qb.setTables(DatabaseHelper.TABLE_GROUPS);
		qb.setProjectionMap(sProjectionMap);
		switch (sUriMatcher.match(uri)) {
		case GROUP:
			break;
		case GROUP_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = GroupEntity.DEFAULT_SORT_ORDER;
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