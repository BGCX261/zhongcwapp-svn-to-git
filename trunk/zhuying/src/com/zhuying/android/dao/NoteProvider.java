package com.zhuying.android.dao;

import java.util.HashMap;

import com.zhuying.android.entity.NoteEntity;

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
 * 记录
 */
public class NoteProvider extends ContentProvider {
	private static final int RECORD = 1;
	private static final int RECORD_ID = 2;

	private static UriMatcher sUriMatcher;
	private static HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(NoteEntity.AUTHORITY, NoteEntity.PATH, RECORD);
		sUriMatcher.addURI(NoteEntity.AUTHORITY, NoteEntity.PATH +"/#", RECORD_ID);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(NoteEntity._ID, NoteEntity._ID);
		sProjectionMap.put(NoteEntity.KEY_BODY, NoteEntity.KEY_BODY);
		sProjectionMap.put(NoteEntity.KEY_SUBJECTID, NoteEntity.KEY_SUBJECTID);
		sProjectionMap.put(NoteEntity.KEY_SUBJECTTYPE, NoteEntity.KEY_SUBJECTTYPE);
		sProjectionMap.put(NoteEntity.KEY_SUBJECTNAME, NoteEntity.KEY_SUBJECTNAME);
		sProjectionMap.put(NoteEntity.KEY_DUEAT, NoteEntity.KEY_DUEAT);
		sProjectionMap.put(NoteEntity.KEY_UPDATEDAT, NoteEntity.KEY_UPDATEDAT);
		sProjectionMap.put(NoteEntity.KEY_CREATEDAT, NoteEntity.KEY_CREATEDAT);
		sProjectionMap.put(NoteEntity.KEY_OWNERID, NoteEntity.KEY_OWNERID);
		sProjectionMap.put(NoteEntity.KEY_VISIBLETO, NoteEntity.KEY_VISIBLETO);
		sProjectionMap.put(NoteEntity.KEY_OWNERNAME, NoteEntity.KEY_OWNERNAME);
		sProjectionMap.put(NoteEntity.KEY_SUBJECTFACE, NoteEntity.KEY_SUBJECTFACE);
		sProjectionMap.put(NoteEntity.KEY_NOTEID, NoteEntity.KEY_NOTEID);
	}

	private static final String[] DEFAULT_PROJECTION = new String[] { 
		NoteEntity._ID,
		NoteEntity.KEY_BODY,
		NoteEntity.KEY_SUBJECTID ,
		NoteEntity.KEY_SUBJECTTYPE,
		NoteEntity.KEY_SUBJECTNAME,
		NoteEntity.KEY_DUEAT,
		NoteEntity.KEY_UPDATEDAT,
		NoteEntity.KEY_CREATEDAT,
		NoteEntity.KEY_OWNERID,
		NoteEntity.KEY_VISIBLETO,
		NoteEntity.KEY_OWNERNAME,
		NoteEntity.KEY_SUBJECTFACE,
		NoteEntity.KEY_NOTEID
		};
	
	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case RECORD:
			return NoteEntity.CONTENT_TYPE;
		case RECORD_ID:
			return NoteEntity.CONTENT_ITEM_TYPE;
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
		long rowId = db.replace(DatabaseHelper.TABLE_NOTE, null, initialValues);
		if (rowId > 0) {
			Uri knowleageUri = ContentUris.withAppendedId(
					NoteEntity.CONTENT_URI, rowId);
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
		int count = db.update(DatabaseHelper.TABLE_NOTE, values, where, whereArgs);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_NOTE, (!TextUtils.isEmpty(where) ? where : ""), whereArgs);
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
		qb.setTables(DatabaseHelper.TABLE_NOTE);
		qb.setProjectionMap(sProjectionMap);
		switch (sUriMatcher.match(uri)) {
		case RECORD:
			break;
		case RECORD_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = NoteEntity.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection == null ? DEFAULT_PROJECTION : projection,selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
}