/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.zhongcw.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class DbAdapter {
	public static final String KEY_ROWID = "_id"; //0
    public static final String KEY_CONTACTNAME = "contactName"; //1
    public static final String KEY_ACCOUNTNAME = "accountName";
    public static final String KEY_NOTE = "note";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_MOBILE = "mobile"; //5
    public static final String KEY_WEBSITE = "website";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_OWNERUSERID = "ownerUserId";
    public static final String KEY_UPDATETIME = "updateTime";
    public static final String KEY_ISDEL = "isDel"; //10
    public static final String KEY_CREATETIME = "createTime";
    public static final String KEY_ORGID = "orgId";
    public static final String KEY_ACCOUNTID = "accountId"; //13
    
    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_NAME = "ufcrm";
    private static final String DATABASE_TABLE = "account";
    private static final int DATABASE_VERSION = 8;
    
    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table "+DATABASE_TABLE+
            " ("+KEY_ROWID+" integer primary key autoincrement, "+ 
            KEY_CONTACTNAME+" text  null, "+
            KEY_ACCOUNTNAME+" text  null, "+
            KEY_NOTE+" text  null, "+
            KEY_PHONE+" text  null, "+
            KEY_MOBILE+" text  null, "+
            KEY_WEBSITE+" text  null, "+
            KEY_EMAIL+" text  null, "+
            
            KEY_OWNERUSERID+" integer  null, "+
            KEY_UPDATETIME+" text  null, "+
            KEY_ISDEL+" integer  null, "+
            KEY_CREATETIME+" text  null, "+
            KEY_ORGID+" integer  null, "+
            KEY_ACCOUNTID+" integer  null);";
    
    private static final String[] columns =  new String[] {
			KEY_ROWID,
			KEY_CONTACTNAME, 
			KEY_ACCOUNTNAME,
			KEY_NOTE,
			KEY_PHONE,
			KEY_MOBILE,
			KEY_WEBSITE,
			KEY_EMAIL, 
			KEY_OWNERUSERID,
		    KEY_UPDATETIME,
		    KEY_ISDEL,
		    KEY_CREATETIME,
		    KEY_ORGID,
		    KEY_ACCOUNTID,
			};

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
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);
        }
    }

    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }

    
    /**
     * 新增（insert）
     */
    public long insert(String contactName, String accountName,String note,String phone,String mobile,String website,String email,Integer ownerUserId,String updateTime,Integer isDel,String createTime,Integer ordId,Integer accountId) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CONTACTNAME, contactName);
        initialValues.put(KEY_ACCOUNTNAME, accountName);
        initialValues.put(KEY_NOTE, note);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_MOBILE, mobile);
        initialValues.put(KEY_WEBSITE, website);
        initialValues.put(KEY_EMAIL, email);

        initialValues.put(KEY_OWNERUSERID, ownerUserId);
        initialValues.put(KEY_UPDATETIME, updateTime);
        initialValues.put(KEY_ISDEL, isDel);
        initialValues.put(KEY_CREATETIME, createTime);
        initialValues.put(KEY_ORGID, ordId);
        initialValues.put(KEY_ACCOUNTID, accountId);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * 删除（delete）
     */
    public boolean delete(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteWithWhere(String where) {
        return mDb.delete(DATABASE_TABLE, where, null) > 0;
    }

    public Cursor fetchAll(String selection) {
        return mDb.query(DATABASE_TABLE, columns, selection, null, null, null, null);
    }

    /**
     * 查询（query）
     */
    public Cursor fetch(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE,columns , KEY_ROWID + "=" + rowId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * 更新（update）
     */
    public boolean update(long rowId, String contactName, String accountName,String note,String phone,String mobile,String website,String email,Integer ownerUserId,String updateTime,Integer isDel,String createTime,Integer ordId,Integer accountId) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CONTACTNAME, contactName);
        initialValues.put(KEY_ACCOUNTNAME, accountName);
        initialValues.put(KEY_NOTE, note);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_MOBILE, mobile);
        initialValues.put(KEY_WEBSITE, website);
        initialValues.put(KEY_EMAIL, email);
        
        initialValues.put(KEY_OWNERUSERID, ownerUserId);
        initialValues.put(KEY_UPDATETIME, updateTime);
        initialValues.put(KEY_ISDEL, isDel);
        initialValues.put(KEY_CREATETIME, createTime);
        initialValues.put(KEY_ORGID, ordId);
//        initialValues.put(KEY_ACCOUNTID, accountId);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean update_2(String contactName, String accountName,String note,String phone,String mobile,String website,String email,Integer ownerUserId,String updateTime,Integer isDel,String createTime,Integer ordId,Integer accountId) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CONTACTNAME, contactName);
        initialValues.put(KEY_ACCOUNTNAME, accountName);
        initialValues.put(KEY_NOTE, note);
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_MOBILE, mobile);
        initialValues.put(KEY_WEBSITE, website);
        initialValues.put(KEY_EMAIL, email);
        
        initialValues.put(KEY_OWNERUSERID, ownerUserId);
        initialValues.put(KEY_UPDATETIME, updateTime);
        initialValues.put(KEY_ISDEL, isDel);
        initialValues.put(KEY_CREATETIME, createTime);
        initialValues.put(KEY_ORGID, ordId);
//        initialValues.put(KEY_ACCOUNTID, accountId);
        return mDb.update(DATABASE_TABLE, initialValues, KEY_ACCOUNTID + "=" + accountId, null) > 0;
    }
}
