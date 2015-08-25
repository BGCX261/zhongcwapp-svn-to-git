package com.zhuying.android.dao;

import java.util.HashMap;

import com.zhuying.android.entity.TaskEntity;

import android.R.plurals;
import android.app.SearchManager;
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
 * 计划任务
 */
public class TaskProvider extends ContentProvider {
	private static final String TAG = "PlanProvider";

	private static final int Plan = 1;
	private static final int Plan_ID = 2;

	private static UriMatcher sUriMatcher;
	private static HashMap<String, String> sProjectionMap;
	private DatabaseHelper mOpenHelper;
	
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(TaskEntity.AUTHORITY, TaskEntity.PATH, Plan);
		sUriMatcher.addURI(TaskEntity.AUTHORITY, TaskEntity.PATH +"/#", Plan_ID);
		
		sProjectionMap = new HashMap<String, String>();
		sProjectionMap.put(TaskEntity._ID, TaskEntity._ID);
		sProjectionMap.put(TaskEntity.KEY_BODY, TaskEntity.KEY_BODY);
		sProjectionMap.put(TaskEntity.KEY_SUBJECTID, TaskEntity.KEY_SUBJECTID);
		sProjectionMap.put(TaskEntity.KEY_SUBJECTTYPE, TaskEntity.KEY_SUBJECTTYPE);
		sProjectionMap.put(TaskEntity.KEY_SUBJECTNAME, TaskEntity.KEY_SUBJECTNAME);
		sProjectionMap.put(TaskEntity.KEY_TASKTYPEID, TaskEntity.KEY_TASKTYPEID);
		sProjectionMap.put(TaskEntity.KEY_TASKTYPE, TaskEntity.KEY_TASKTYPE);
		sProjectionMap.put(TaskEntity.KEY_TENANTID, TaskEntity.KEY_TENANTID);
		sProjectionMap.put(TaskEntity.KEY_DUEAT, TaskEntity.KEY_DUEAT);
		sProjectionMap.put(TaskEntity.KEY_OWNERID, TaskEntity.KEY_OWNERID);
		sProjectionMap.put(TaskEntity.KEY_OWNERNAME, TaskEntity.KEY_OWNERNAME);
		sProjectionMap.put(TaskEntity.KEY_STATUS, TaskEntity.KEY_STATUS);
		sProjectionMap.put(TaskEntity.KEY_DUEATTYPE, TaskEntity.KEY_DUEATTYPE);
		sProjectionMap.put(TaskEntity.KEY_FINISHAT, TaskEntity.KEY_FINISHAT);
		sProjectionMap.put(TaskEntity.KEY_CREATEUSERID, TaskEntity.KEY_CREATEUSERID);
		sProjectionMap.put(TaskEntity.KEY_VISIBLETO, TaskEntity.KEY_VISIBLETO);
		sProjectionMap.put(TaskEntity.KEY_UPDATEDAT, TaskEntity.KEY_UPDATEDAT);
		sProjectionMap.put(TaskEntity.KEY_CREATEDAT, TaskEntity.KEY_CREATEDAT);
		sProjectionMap.put(TaskEntity.KEY_TASKID, TaskEntity.KEY_TASKID);
	}
	
	private static final String[] DEFAULT_PROJECTION = new String[] {
		TaskEntity._ID, 
		TaskEntity.KEY_BODY,
		TaskEntity.KEY_SUBJECTID,
		TaskEntity.KEY_SUBJECTTYPE,
		TaskEntity.KEY_SUBJECTNAME,
		TaskEntity.KEY_TASKTYPEID,
		TaskEntity.KEY_TASKTYPE,
		TaskEntity.KEY_TENANTID,
		TaskEntity.KEY_DUEAT,
		TaskEntity.KEY_OWNERID,
		TaskEntity.KEY_OWNERNAME,
		TaskEntity.KEY_STATUS,
		TaskEntity.KEY_DUEATTYPE,
		TaskEntity.KEY_FINISHAT,
		TaskEntity.KEY_CREATEUSERID,
		TaskEntity.KEY_VISIBLETO,
		TaskEntity.KEY_UPDATEDAT,
		TaskEntity.KEY_CREATEDAT,
		TaskEntity.KEY_TASKID,
	};

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case Plan:
			return TaskEntity.CONTENT_TYPE;
		case Plan_ID:
			return TaskEntity.CONTENT_ITEM_TYPE;
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
		long rowId = db.replace(DatabaseHelper.TABLE_TASK, null, initialValues);
//		Log.d(TAG, "rowId = " + rowId);
		if (rowId > 0) {
			Uri knowleageUri = ContentUris.withAppendedId(
					TaskEntity.CONTENT_URI, rowId);
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
		int count = db.update(DatabaseHelper.TABLE_TASK, values, where, whereArgs);
		return count;
	}

	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db.delete(DatabaseHelper.TABLE_TASK, (!TextUtils.isEmpty(where) ? where : ""), whereArgs);
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
		qb.setTables(DatabaseHelper.TABLE_TASK);
		qb.setProjectionMap(sProjectionMap);
		switch (sUriMatcher.match(uri)) {
		case Plan:
			break;
		case Plan_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = TaskEntity.DEFAULT_SORT_ORDER;
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