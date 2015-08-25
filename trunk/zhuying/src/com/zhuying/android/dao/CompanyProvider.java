package com.zhuying.android.dao;

import java.util.HashMap;

import com.zhuying.android.entity.CompanyEntity;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * 公司
 */
public class CompanyProvider extends ContentProvider {
	private static final String TAG = "CompanyProvider";

	private static final int Company = 1;
	private static final int Company_ID = 2;
	// for Search Provider
	private static final int SEARCH_SUGGEST = 3;

	private static UriMatcher sUriMatcher;
	private static HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	private static final String[] COLUMNS = {
		"_id", // must include this column
		SearchManager.SUGGEST_COLUMN_TEXT_1,
		SearchManager.SUGGEST_COLUMN_TEXT_2,
		SearchManager.SUGGEST_COLUMN_INTENT_DATA,
		SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
		SearchManager.SUGGEST_COLUMN_SHORTCUT_ID };

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(CompanyEntity.AUTHORITY, CompanyEntity.PATH, Company);
		sUriMatcher.addURI(CompanyEntity.AUTHORITY, CompanyEntity.PATH +"/#", Company_ID);
		
		sUriMatcher.addURI(CompanyEntity.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
		sUriMatcher.addURI(CompanyEntity.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(CompanyEntity._ID, CompanyEntity._ID);
		sProjectionMap.put(CompanyEntity.KEY_NAME, CompanyEntity.KEY_NAME);
		sProjectionMap.put(CompanyEntity.KEY_TITLE, CompanyEntity.KEY_TITLE);
		sProjectionMap.put(CompanyEntity.KEY_BACKGROUND, CompanyEntity.KEY_BACKGROUND);
		sProjectionMap.put(CompanyEntity.KEY_TENANTID, CompanyEntity.KEY_TENANTID);
		sProjectionMap.put(CompanyEntity.KEY_EMAIL, CompanyEntity.KEY_EMAIL);
		sProjectionMap.put(CompanyEntity.KEY_PHONE, CompanyEntity.KEY_PHONE);
		sProjectionMap.put(CompanyEntity.KEY_WEBSITES, CompanyEntity.KEY_WEBSITES);
		sProjectionMap.put(CompanyEntity.KEY_ADDRESS, CompanyEntity.KEY_ADDRESS);
		sProjectionMap.put(CompanyEntity.KEY_IM, CompanyEntity.KEY_IM);
		sProjectionMap.put(CompanyEntity.KEY_PARTYTYPE, CompanyEntity.KEY_PARTYTYPE);
		sProjectionMap.put(CompanyEntity.KEY_COMPANYID, CompanyEntity.KEY_COMPANYID);
		sProjectionMap.put(CompanyEntity.KEY_COMPANYNAME, CompanyEntity.KEY_COMPANYNAME);
		sProjectionMap.put(CompanyEntity.KEY_PARTYFACE, CompanyEntity.KEY_PARTYFACE);
		sProjectionMap.put(CompanyEntity.KEY_OWNERID, CompanyEntity.KEY_OWNERID);
		sProjectionMap.put(CompanyEntity.KEY_VISIBLETO, CompanyEntity.KEY_VISIBLETO);
		sProjectionMap.put(CompanyEntity.KEY_UPDATEDAT, CompanyEntity.KEY_UPDATEDAT);
		sProjectionMap.put(CompanyEntity.KEY_CREATEDAT, CompanyEntity.KEY_CREATEDAT);
		sProjectionMap.put(CompanyEntity.KEY_SORT_KEY, CompanyEntity.KEY_SORT_KEY);
		sProjectionMap.put(CompanyEntity.KEY_PARTYID, CompanyEntity.KEY_PARTYID);
	}
	
	private static final String[] DEFAULT_PROJECTION = new String[] {
		CompanyEntity._ID,
		CompanyEntity.KEY_NAME,
		CompanyEntity.KEY_TITLE,
		CompanyEntity.KEY_BACKGROUND,
		CompanyEntity.KEY_TENANTID,
		CompanyEntity.KEY_EMAIL,
		CompanyEntity.KEY_PHONE,
		CompanyEntity.KEY_WEBSITES,
		CompanyEntity.KEY_ADDRESS,
		CompanyEntity.KEY_IM,
		CompanyEntity.KEY_PARTYTYPE,
		CompanyEntity.KEY_COMPANYID,
		CompanyEntity.KEY_COMPANYNAME,
		CompanyEntity.KEY_PARTYFACE,
		CompanyEntity.KEY_OWNERID,
		CompanyEntity.KEY_VISIBLETO,
		CompanyEntity.KEY_UPDATEDAT,
		CompanyEntity.KEY_CREATEDAT,
		CompanyEntity.KEY_SORT_KEY,
		CompanyEntity.KEY_PARTYID
	};


	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case Company:
			return CompanyEntity.CONTENT_TYPE;
		case Company_ID:
			return CompanyEntity.CONTENT_ITEM_TYPE;
		case SEARCH_SUGGEST:
			return SearchManager.SUGGEST_MIME_TYPE;
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
		long rowId = db.replace(DatabaseHelper.TABLE_PARTY, null, initialValues);
//		Log.d(TAG, "rowId = " + rowId);
		if (rowId > 0) {
			Uri knowleageUri = ContentUris.withAppendedId(
					CompanyEntity.CONTENT_URI, rowId);
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
		int count = db.update(DatabaseHelper.TABLE_PARTY, values, where, whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_PARTY, (!TextUtils.isEmpty(where) ? where : ""), whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/**
	 * 查询
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
//		Log.d(TAG, "[query]" + uri);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(DatabaseHelper.TABLE_PARTY);
		qb.setProjectionMap(sProjectionMap);
		switch (sUriMatcher.match(uri)) {
		case Company:
			break;
		case Company_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;
		case SEARCH_SUGGEST:
			String query = selectionArgs[0];
			selection = "partytype = 'company' and name like '%" + query + "%'";
			selectionArgs = null;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = CompanyEntity.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection == null ? DEFAULT_PROJECTION : projection,
				selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		if (sUriMatcher.match(uri) == SEARCH_SUGGEST) {
			MatrixCursor cursor = new MatrixCursor(COLUMNS);
			if (c.moveToFirst())
				do {
					cursor.addRow(new Object[] {
							c.getInt(0),
							c.getString(1),
							c.getString(1),
							ContentUris.withAppendedId(CompanyEntity.CONTENT_URI, c.getInt(0)),
							Intent.ACTION_VIEW,
							SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT });
				} while (c.moveToNext());
			c.close();
			return cursor;
		}
		return c;
	}

}