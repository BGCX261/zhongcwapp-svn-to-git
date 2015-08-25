package com.ufida.gkb.dao;

import static android.provider.BaseColumns._ID;
import static com.ufida.gkb.entity.AccountEntity.Model.*;

import java.util.HashMap;

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

import com.ufida.gkb.entity.AccountEntity.Model;

public class AccountProvider extends ContentProvider {

	private static final String TAG = "AccountProvider";

	private static HashMap<String, String> sAccountsProjectionMap;
	private static String[] sAccounts = new String[] { KEY_ROWID,
			KEY_CONTACTNAME, KEY_ACCOUNTNAME, KEY_NOTE, KEY_PHONE, KEY_MOBILE,
			KEY_WEBSITE, KEY_EMAIL, KEY_ISDEL, KEY_CREATETIME, KEY_UPDATETIME,
			KEY_OWNERUSERID, KEY_ORGID ,KEY_ADDRESS };

	// for CRUD operation
	private static final int ACCOUNTS = 1;
	private static final int ACCOUNT_ID = 2;

	// for Search Provider
	private static final int SEARCH_SUGGEST = 3;
	private static final int SHORTCUT_REFRESH = 4;

	public static final UriMatcher sUriMatcher;

	private static final String[] COLUMNS = {
			"_id", // must include this column
			SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_TEXT_2,
			SearchManager.SUGGEST_COLUMN_INTENT_DATA,
			SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
			SearchManager.SUGGEST_COLUMN_SHORTCUT_ID };

	private DatabaseHelper mOpenHelper;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = db
				.delete("account", (!TextUtils.isEmpty(selection) ? selection
						: ""), selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
		case ACCOUNTS:
			return CONTENT_TYPE;
		case ACCOUNT_ID:
			return CONTENT_ITEM_TYPE;
		case SEARCH_SUGGEST:
			return SearchManager.SUGGEST_MIME_TYPE;
		case SHORTCUT_REFRESH:
			return SearchManager.SHORTCUT_MIME_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "[insert]" + uri);
		if (sUriMatcher.match(uri) != ACCOUNTS) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		
		long rowId = db.replace("account", null, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}

		throw new SQLException("Failed to insert row into " + uri);

	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "[query]" + uri);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables("account");
		qb.setProjectionMap(sAccountsProjectionMap);

		switch (sUriMatcher.match(uri)) {
		case ACCOUNTS:
			qb.appendWhere("isDel = 0");
			break;
		case ACCOUNT_ID:
			qb.appendWhere("_id =" + uri.getPathSegments().get(1));
			break;

		case SEARCH_SUGGEST:
			qb.appendWhere("isDel = 0");
			String query = selectionArgs[0];

			selection = "contactName like '%" + query
					+ "%' or accountName like '%" + query + "%' ";

			selectionArgs = null;
			break;

		case SHORTCUT_REFRESH:
			Log.d(TAG, "shortcut refresh called");
			return null;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		// Get the database and run the query
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection == null ? sAccounts : projection,
				selection, selectionArgs, null, null, orderBy);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		if (sUriMatcher.match(uri) == SEARCH_SUGGEST) {
			MatrixCursor cursor = new MatrixCursor(COLUMNS);
			if (c.moveToFirst())
				do {
					cursor.addRow(new Object[] {
							c.getInt(0),
							c.getString(IDX_CONTACTNAME),
							c.getString(IDX_ACCOUNTNAME),
							ContentUris.withAppendedId(CONTENT_URI, c.getInt(0)),
							Intent.ACTION_VIEW,
							SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT });
					Log.i(TAG,
							ContentUris
									.withAppendedId(CONTENT_URI, c.getInt(0))
									.toString());

				} while (c.moveToNext());
			c.close();
			return cursor;
		}
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		Log.d(TAG, "[update]" + uri);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count;
		switch (sUriMatcher.match(uri)) {
		case ACCOUNTS:
			count = db.update("account", values, selection, selectionArgs);
			break;
		case ACCOUNT_ID:
			String id = uri.getPathSegments().get(1);
			count = db.update("account", values,
					_ID
							+ "="
							+ id
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Model.AUTHORITY, "list", ACCOUNTS);
		sUriMatcher.addURI(Model.AUTHORITY, "list/#", ACCOUNT_ID);

		sUriMatcher.addURI(Model.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
		sUriMatcher.addURI(Model.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
		sUriMatcher.addURI(Model.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_SHORTCUT, SHORTCUT_REFRESH);
		sUriMatcher.addURI(Model.AUTHORITY,
				SearchManager.SUGGEST_URI_PATH_SHORTCUT + "/*",
				SHORTCUT_REFRESH);

		sAccountsProjectionMap = new HashMap<String, String>();
		sAccountsProjectionMap.put(_ID, _ID);
		sAccountsProjectionMap.put(KEY_CONTACTNAME, KEY_CONTACTNAME);
		sAccountsProjectionMap.put(KEY_ACCOUNTNAME, KEY_ACCOUNTNAME);
		sAccountsProjectionMap.put(KEY_NOTE, KEY_NOTE);
		sAccountsProjectionMap.put(KEY_PHONE, KEY_PHONE);
		sAccountsProjectionMap.put(KEY_MOBILE, KEY_MOBILE);
		sAccountsProjectionMap.put(KEY_WEBSITE, KEY_WEBSITE);
		sAccountsProjectionMap.put(KEY_EMAIL, KEY_EMAIL);
		sAccountsProjectionMap.put(KEY_ISDEL, KEY_ISDEL);
		sAccountsProjectionMap.put(KEY_CREATETIME, KEY_CREATETIME);
		sAccountsProjectionMap.put(KEY_UPDATETIME, KEY_UPDATETIME);
		sAccountsProjectionMap.put(KEY_OWNERUSERID, KEY_OWNERUSERID);
		sAccountsProjectionMap.put(KEY_ORGID, KEY_ORGID);
		sAccountsProjectionMap.put(KEY_ADDRESS, KEY_ADDRESS);

	}
}
