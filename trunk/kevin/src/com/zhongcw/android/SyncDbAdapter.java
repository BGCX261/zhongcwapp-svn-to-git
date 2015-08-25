package com.zhongcw.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SyncDbAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_STARTTIME = "startTime";
	public static final String KEY_USERID = "userId";
	public static final String KEY_ORGID = "orgId";

	private static final String TAG = "SyncDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_NAME = "ufcrm";
	private static final String DATABASE_TABLE = "accountSync";
	private static final int DATABASE_VERSION = 3;

	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ROWID
			+ " integer primary key autoincrement, " + KEY_STARTTIME
			+ " text  null, " + KEY_USERID + " integer  null, " + KEY_ORGID
			+ " integer  null);";

	private static final String[] columns = new String[] { KEY_ROWID,
			KEY_STARTTIME, KEY_USERID, KEY_ORGID, };

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public SyncDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public SyncDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long insert(String startTime, Integer userId, Integer orgId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_STARTTIME, startTime);
		initialValues.put(KEY_USERID, userId);
		initialValues.put(KEY_ORGID, orgId);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean delete(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor fetchAll(String selection) {
		return mDb.query(DATABASE_TABLE, columns, selection, null, null, null,
				null);
	}

	public Cursor fetch(long rowId) throws SQLException {
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, columns, KEY_ROWID
				+ "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public boolean update(long rowId, String startTime, Integer userId,
			Integer orgId) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_STARTTIME, startTime);
		initialValues.put(KEY_USERID, userId);
		initialValues.put(KEY_ORGID, orgId);
		return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}
}
