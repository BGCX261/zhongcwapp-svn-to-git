package com.zhuying.android.dao;

import java.util.HashMap;

import com.zhuying.android.entity.ActionEntity;

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
import android.util.Log;

/**
 * 最近行动
 */
public class ActionProvider extends ContentProvider {
	private static final String TAG = "ActionProvider";

	private static final int ACTIVITY = 1;
	private static final int ACTIVITY_ID = 2;

	private static UriMatcher sUriMatcher;
	private static HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(ActionEntity.AUTHORITY, ActionEntity.PATH, ACTIVITY);
		sUriMatcher.addURI(ActionEntity.AUTHORITY, ActionEntity.PATH +"/#", ACTIVITY_ID);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(ActionEntity._ID, ActionEntity._ID);
		sProjectionMap.put(ActionEntity.KEY_ACTIVITYTYPE, ActionEntity.KEY_ACTIVITYTYPE);
		sProjectionMap.put(ActionEntity.KEY_ACTIVITYCONTENT, ActionEntity.KEY_ACTIVITYCONTENT);
		sProjectionMap.put(ActionEntity.KEY_ACTIVITYSTATUS, ActionEntity.KEY_ACTIVITYSTATUS);
		sProjectionMap.put(ActionEntity.KEY_ACTIVITYCREATE, ActionEntity.KEY_ACTIVITYCREATE);
		sProjectionMap.put(ActionEntity.KEY_PARENTID, ActionEntity.KEY_PARENTID);
		sProjectionMap.put(ActionEntity.KEY_SUBJECTID, ActionEntity.KEY_SUBJECTID);
		sProjectionMap.put(ActionEntity.KEY_SUBJECTTYPE, ActionEntity.KEY_SUBJECTTYPE);
		sProjectionMap.put(ActionEntity.KEY_SUBJECTNAME, ActionEntity.KEY_SUBJECTNAME);
		sProjectionMap.put(ActionEntity.KEY_SUBJECTFACE, ActionEntity.KEY_SUBJECTFACE);
		sProjectionMap.put(ActionEntity.KEY_OWNERID, ActionEntity.KEY_OWNERID);
		sProjectionMap.put(ActionEntity.KEY_OWNERNAME, ActionEntity.KEY_OWNERNAME);
		sProjectionMap.put(ActionEntity.KEY_ACTIVITYDATE, ActionEntity.KEY_ACTIVITYDATE);
		sProjectionMap.put(ActionEntity.KEY_UPDATEDAT, ActionEntity.KEY_UPDATEDAT);
		sProjectionMap.put(ActionEntity.KEY_CREATEDAT, ActionEntity.KEY_CREATEDAT);
		sProjectionMap.put(ActionEntity.KEY_LATESTACTIVITYID, ActionEntity.KEY_LATESTACTIVITYID);
	}
	
	private static final String[] DEFAULT_PROJECTION = new String[] {
		ActionEntity._ID, 
		ActionEntity.KEY_ACTIVITYTYPE,
		ActionEntity.KEY_ACTIVITYCONTENT,
		ActionEntity.KEY_ACTIVITYSTATUS,
		ActionEntity.KEY_ACTIVITYCREATE,
		ActionEntity.KEY_PARENTID,
		ActionEntity.KEY_SUBJECTID,
		ActionEntity.KEY_SUBJECTTYPE,
		ActionEntity.KEY_SUBJECTNAME,
		ActionEntity.KEY_SUBJECTFACE,
		ActionEntity.KEY_OWNERID,
		ActionEntity.KEY_OWNERNAME,
		ActionEntity.KEY_ACTIVITYDATE,
		ActionEntity.KEY_UPDATEDAT,
		ActionEntity.KEY_CREATEDAT,
		ActionEntity.KEY_LATESTACTIVITYID,
	};

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case ACTIVITY:
			return ActionEntity.CONTENT_TYPE;
		case ACTIVITY_ID:
			return ActionEntity.CONTENT_ITEM_TYPE;
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
		long rowId = db.replace(DatabaseHelper.TABLE_LATEST_ACTIVITY, null, initialValues);
//		Log.d(TAG, "rowId = " + rowId);
		if (rowId > 0) {
			Uri knowleageUri = ContentUris.withAppendedId(
					ActionEntity.CONTENT_URI, rowId);
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
		int count = db.update(DatabaseHelper.TABLE_LATEST_ACTIVITY, values, where, whereArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_LATEST_ACTIVITY, (!TextUtils.isEmpty(where) ? where : ""), whereArgs);
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
		qb.setTables(DatabaseHelper.TABLE_LATEST_ACTIVITY);
		qb.setProjectionMap(sProjectionMap);
		switch (sUriMatcher.match(uri)) {
		case ACTIVITY:
			break;
		case ACTIVITY_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = ActionEntity.DEFAULT_SORT_ORDER;
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