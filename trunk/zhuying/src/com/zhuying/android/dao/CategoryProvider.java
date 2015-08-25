package com.zhuying.android.dao;

import java.util.HashMap;

import com.zhuying.android.entity.CategoryEntity;

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
public class CategoryProvider extends ContentProvider {

	private static final int Category = 1;
	private static final int Category_ID = 2;

	private static UriMatcher sUriMatcher;
	private static HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(CategoryEntity.AUTHORITY, CategoryEntity.PATH, Category);
		sUriMatcher.addURI(CategoryEntity.AUTHORITY, CategoryEntity.PATH +"/#", Category_ID);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(CategoryEntity._ID, CategoryEntity._ID);
		sProjectionMap.put(CategoryEntity.KEY_TENANTID, CategoryEntity.KEY_TENANTID);
		sProjectionMap.put(CategoryEntity.KEY_CATEGORYNAME, CategoryEntity.KEY_CATEGORYNAME);
		sProjectionMap.put(CategoryEntity.KEY_CATEGORYTYPE, CategoryEntity.KEY_CATEGORYTYPE);
		sProjectionMap.put(CategoryEntity.KEY_CATEGORYCOLOR, CategoryEntity.KEY_CATEGORYCOLOR);
		sProjectionMap.put(CategoryEntity.KEY_CATEGORYID, CategoryEntity.KEY_CATEGORYID);
		sProjectionMap.put(CategoryEntity.KEY_CREATEDAT, CategoryEntity.KEY_CREATEDAT);
		sProjectionMap.put(CategoryEntity.KEY_UPDATEDAT, CategoryEntity.KEY_UPDATEDAT);
	}
	
	private static final String[] DEFAULT_PROJECTION = new String[] {
		CategoryEntity._ID, 
		CategoryEntity.KEY_TENANTID,
		CategoryEntity.KEY_CATEGORYNAME,
		CategoryEntity.KEY_CATEGORYTYPE,
		CategoryEntity.KEY_CATEGORYCOLOR,
		CategoryEntity.KEY_CATEGORYID,
		CategoryEntity.KEY_CREATEDAT,
		CategoryEntity.KEY_UPDATEDAT,
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
			return CategoryEntity.CONTENT_TYPE;
		case Category_ID:
			return CategoryEntity.CONTENT_ITEM_TYPE;
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
		long rowId = db.replace(DatabaseHelper.TABLE_CATEGORY, null, initialValues);
		if (rowId > 0) {
			Uri knowleageUri = ContentUris.withAppendedId(
					CategoryEntity.CONTENT_URI, rowId);
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
		int count = db.update(DatabaseHelper.TABLE_CATEGORY, values, where, whereArgs);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_CATEGORY, (!TextUtils.isEmpty(where) ? where : ""), whereArgs);
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
		qb.setTables(DatabaseHelper.TABLE_CATEGORY);
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
			orderBy = CategoryEntity.DEFAULT_SORT_ORDER;
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