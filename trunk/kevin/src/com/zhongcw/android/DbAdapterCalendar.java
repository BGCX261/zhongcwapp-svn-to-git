package com.zhongcw.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DbAdapterCalendar {
	/**
	 * 数据库字段
	 */
	public static final String KEY_ROWID = "_id";
    public static final String KEY_REPEATTYPE = "repeatType";
    public static final String KEY_ACCOUNTID = "accountId";
    public static final String KEY_ACTVTTYPE = "actvtType";
    public static final String KEY_OWNERUSERID = "ownerUserId";
    public static final String KEY_ACTVTSTATE = "actvtState";
    public static final String KEY_ACTVTEFFE = "actvtEffe";
    public static final String KEY_EFFEACCOUNTPHASE = "effeAccountPhase";
    public static final String KEY_STARTTIME = "startTime";
    public static final String KEY_ENDTIME = "endTime";
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_CONTACTNAME = "contactName";
    public static final String KEY_NOTE = "note";
    public static final String KEY_ACTVTID  = "actvtId "; //同步标识
    
    
    private static final String TAG = "DbAdapterCalendar";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_NAME = "ufcrm";
    private static final String DATABASE_TABLE = "t_activity";
    private static final int DATABASE_VERSION = 2;
    
    /**
     * Database creation sql statement
     * 为了快速、方便的搭建可运行应用程序，所有字段暂时都设置为text类型
     */
    private static final String DATABASE_CREATE =
            "create table "+DATABASE_TABLE+
            " ("+KEY_ROWID+" integer primary key autoincrement, "+ 
            KEY_REPEATTYPE+" text null, "+
            KEY_ACCOUNTID+" text null, "+
            KEY_ACTVTTYPE+" text null, "+
            KEY_OWNERUSERID+" text null, "+
            KEY_ACTVTSTATE+" text null, "+
            KEY_ACTVTEFFE+" text null, "+
            KEY_EFFEACCOUNTPHASE+" text null, "+
            KEY_STARTTIME+" text null, "+
            KEY_ENDTIME+" text null, "+
            KEY_SUBJECT+" text null, "+
            KEY_CONTACTNAME+" text null, "+
            KEY_NOTE+" text null, "+
            KEY_ACTVTID+" text null);";
    
    private static final String[] columns =  new String[] {
    	KEY_ROWID,
    	KEY_REPEATTYPE,
    	KEY_ACCOUNTID,
    	KEY_ACTVTTYPE,
    	KEY_OWNERUSERID,
    	KEY_ACTVTSTATE,
    	KEY_ACTVTEFFE,
    	KEY_EFFEACCOUNTPHASE,
    	KEY_STARTTIME,
    	KEY_ENDTIME,
    	KEY_SUBJECT,
    	KEY_CONTACTNAME,
    	KEY_NOTE,
    	KEY_ACTVTID,
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
//            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
//                    + newVersion + ", which will destroy all old data");
//            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
//            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DbAdapterCalendar(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapterCalendar open() throws SQLException {
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
    public long insert(String repeatType, String accountId,String actvtType,String ownerUserId,String actvtState,String actvtEffe,String effeAccountPhase,String startTime,String endTime,String subject,String contactName,String note,String actvtid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_REPEATTYPE, repeatType);
        contentValues.put(KEY_ACCOUNTID, accountId);
        contentValues.put(KEY_ACTVTTYPE, actvtType);
        contentValues.put(KEY_OWNERUSERID, ownerUserId);
        contentValues.put(KEY_ACTVTSTATE, actvtState);
        contentValues.put(KEY_ACTVTEFFE, actvtEffe);
        contentValues.put(KEY_EFFEACCOUNTPHASE, effeAccountPhase);
        contentValues.put(KEY_STARTTIME, startTime);
        contentValues.put(KEY_ENDTIME, endTime);
        contentValues.put(KEY_SUBJECT, subject);
        contentValues.put(KEY_CONTACTNAME, contactName);
        contentValues.put(KEY_NOTE, note);
        contentValues.put(KEY_ACTVTID, actvtid);
        return mDb.insert(DATABASE_TABLE, null, contentValues);
    }

    /**
     * 删除（delete）
     * Delete the note with the given rowId
     * 
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean delete(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteWithWhere(String where) {
        return mDb.delete(DATABASE_TABLE, where, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     * 
     * @return Cursor over all notes
     */
    public Cursor fetchAll(String selection) {
        return mDb.query(DATABASE_TABLE, columns, selection, null, null, null, null);
    }

    /**
     * 查询（query）
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
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
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param body value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean update(long rowId, String repeatType, String accountId,String actvtType,String ownerUserId,String actvtState,String actvtEffe,String effeAccountPhase,String startTime,String endTime,String subject,String contactName,String note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_REPEATTYPE, repeatType);
        contentValues.put(KEY_ACCOUNTID, accountId);
        contentValues.put(KEY_ACTVTTYPE, actvtType);
        contentValues.put(KEY_OWNERUSERID, ownerUserId);
        contentValues.put(KEY_ACTVTSTATE, actvtState);
        contentValues.put(KEY_ACTVTEFFE, actvtEffe);
        contentValues.put(KEY_EFFEACCOUNTPHASE, effeAccountPhase);
        contentValues.put(KEY_STARTTIME, startTime);
        contentValues.put(KEY_ENDTIME, endTime);
        contentValues.put(KEY_SUBJECT, subject);
        contentValues.put(KEY_CONTACTNAME, contactName);
        contentValues.put(KEY_NOTE, note);
        return mDb.update(DATABASE_TABLE, contentValues, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
