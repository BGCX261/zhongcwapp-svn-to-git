package com.zhuying.android.dao;

import java.util.HashMap;

import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * 联系人
 */
public class ContactProvider extends ContentProvider {
	private static final String TAG = "ContactProvider";

	private static final int Contact = 1;
	private static final int Contact_ID = 2;
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
		sUriMatcher.addURI(ContactEntity.AUTHORITY, ContactEntity.PATH, Contact);
		sUriMatcher.addURI(ContactEntity.AUTHORITY, ContactEntity.PATH +"/#", Contact_ID);
		
		sUriMatcher.addURI(ContactEntity.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
		sUriMatcher.addURI(ContactEntity.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(ContactEntity._ID, ContactEntity._ID);
		sProjectionMap.put(ContactEntity.KEY_NAME, ContactEntity.KEY_NAME);
		sProjectionMap.put(ContactEntity.KEY_TITLE, ContactEntity.KEY_TITLE);
		sProjectionMap.put(ContactEntity.KEY_BACKGROUND, ContactEntity.KEY_BACKGROUND);
		sProjectionMap.put(ContactEntity.KEY_TENANTID, ContactEntity.KEY_TENANTID);
		sProjectionMap.put(ContactEntity.KEY_EMAIL, ContactEntity.KEY_EMAIL);
		sProjectionMap.put(ContactEntity.KEY_PHONE, ContactEntity.KEY_PHONE);
		sProjectionMap.put(ContactEntity.KEY_WEBSITES, ContactEntity.KEY_WEBSITES);
		sProjectionMap.put(ContactEntity.KEY_ADDRESS, ContactEntity.KEY_ADDRESS);
		sProjectionMap.put(ContactEntity.KEY_IM, ContactEntity.KEY_IM);
		sProjectionMap.put(ContactEntity.KEY_PARTYTYPE, ContactEntity.KEY_PARTYTYPE);
		sProjectionMap.put(ContactEntity.KEY_COMPANYID, ContactEntity.KEY_COMPANYID);
		sProjectionMap.put(ContactEntity.KEY_COMPANYNAME, ContactEntity.KEY_COMPANYNAME);
		sProjectionMap.put(ContactEntity.KEY_PARTYFACE, ContactEntity.KEY_PARTYFACE);
		sProjectionMap.put(ContactEntity.KEY_OWNERID, ContactEntity.KEY_OWNERID);
		sProjectionMap.put(ContactEntity.KEY_VISIBLETO, ContactEntity.KEY_VISIBLETO);
		sProjectionMap.put(ContactEntity.KEY_UPDATEDAT, ContactEntity.KEY_UPDATEDAT);
		sProjectionMap.put(ContactEntity.KEY_CREATEDAT, ContactEntity.KEY_CREATEDAT);
		sProjectionMap.put(ContactEntity.KEY_SORT_KEY, ContactEntity.KEY_SORT_KEY);
		sProjectionMap.put(ContactEntity.KEY_PARTYID, ContactEntity.KEY_PARTYID);
	}
	
	private static final String[] DEFAULT_PROJECTION = new String[] { 
		ContactEntity._ID,
		ContactEntity.KEY_NAME,
		ContactEntity.KEY_TITLE,
		ContactEntity.KEY_BACKGROUND,
		ContactEntity.KEY_TENANTID,
		ContactEntity.KEY_EMAIL,
		ContactEntity.KEY_PHONE,
		ContactEntity.KEY_WEBSITES,
		ContactEntity.KEY_ADDRESS,
		ContactEntity.KEY_IM,
		ContactEntity.KEY_PARTYTYPE,
		ContactEntity.KEY_COMPANYID,
		ContactEntity.KEY_COMPANYNAME,
		ContactEntity.KEY_PARTYFACE,
		ContactEntity.KEY_OWNERID,
		ContactEntity.KEY_VISIBLETO,
		ContactEntity.KEY_UPDATEDAT,
		ContactEntity.KEY_CREATEDAT,
		ContactEntity.KEY_SORT_KEY,
		ContactEntity.KEY_PARTYID 
	};

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case Contact:
			return ContactEntity.CONTENT_TYPE;
		case Contact_ID:
			return ContactEntity.CONTENT_ITEM_TYPE;
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
					ContactEntity.CONTENT_URI, rowId);
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
		int count;
		/*switch (sUriMatcher.match(uri)) {
		case NOTE:
			count = db.update(TABLE_NAME, values, where, whereArgs);
			break;
		case NOTE_ID:
			String knowleageId = uri.getPathSegments().get(1);
			count = db.update(
					TABLE_NAME,
					values,
					ContactEntity._ID
							+ "="
							+ knowleageId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}*/
		
		count = db.update(DatabaseHelper.TABLE_PARTY, values, where, whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		/*SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case Contact:
			count = db.delete(DatabaseHelper.TABLE_PARTY, where, whereArgs);
			break;
		case Contact_ID:
			String knowleageId = uri.getPathSegments().get(1);
			count = db.delete(
					DatabaseHelper.TABLE_PARTY,
					ContactEntity._ID
							+ "="
							+ knowleageId
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;*/
		
		
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
		/*SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_NAME);
		qb.setProjectionMap(sProjectionMap);
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		
		String orderBy = ContactEntity.DEFAULT_SORT_ORDER;
		if(sortOrder != null){
			orderBy = sortOrder;
		}
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		Log.d(TAG, "query count " + c.getCount());
		return c;*/
		

		
//		Log.d(TAG, "[query]" + uri);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(DatabaseHelper.TABLE_PARTY);
		qb.setProjectionMap(sProjectionMap);

		switch (sUriMatcher.match(uri)) {
		case Contact:
			break;
		case Contact_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;
		case SEARCH_SUGGEST:
			String query = selectionArgs[0];
			selection = "partytype = '"+CompanyEntity.TYPE_CONTACT+"' and name like '%" + query + "%'";
			selectionArgs = null;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = ContactEntity.DEFAULT_SORT_ORDER;
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
							c.getString(1), //姓名
							c.getString(12), //公司
							ContentUris.withAppendedId(ContactEntity.CONTENT_URI, c.getInt(0)),
							Intent.ACTION_VIEW,
							SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT });
				} while (c.moveToNext());
			c.close();
			return cursor;
		}
		return c;
	
	}

}