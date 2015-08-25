package com.zhuying.android.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.AuthorityEntity;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.GroupEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.entity.UserEntity;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "zhuying.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_PARTY = "party";// 联系人、公司表
	public static final String TABLE_NOTE = "note"; // 记录表
	public static final String TABLE_TASK = "tasks"; // 计划任务表
	public static final String TABLE_USER = "user"; // 用户表
	public static final String TABLE_GROUPS = "groups"; // 组表
	public static final String TABLE_LATEST_ACTIVITY = "latest_activity"; // 最近行动表
	public static final String TABLE_CATEGORY = "category"; //  计划任务分类表
	public static final String TABLE_AUTHORITY = "tasks_users"; //  计划任务权限表
	public static final String TABLE_PHOTO = "photo"; //  头像表
	
	DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 用户表
		db.execSQL("CREATE TABLE " + TABLE_USER + " (" 
				+ UserEntity._ID + " INTEGER PRIMARY KEY," 
				+ UserEntity.KEY_NAME + " TEXT,"
				+ UserEntity.KEY_EMAIL + " TEXT,"
				+ UserEntity.KEY_LASTLOGIN + " TEXT,"
				+ UserEntity.KEY_REALNAME + " TEXT,"
				+ UserEntity.KEY_ISLOGIN + " TEXT,"
				+ UserEntity.KEY_USERID + " TEXT,"
				+ UserEntity.KEY_CREATEDAT + " TEXT,"
				+ UserEntity.KEY_UPDATEDAT + " TEXT"
				+ ");");
		
		// 组表
		db.execSQL("CREATE TABLE " + TABLE_GROUPS + " (" 
				+ GroupEntity._ID + " INTEGER PRIMARY KEY," 
				+ GroupEntity.KEY_NAME + " TEXT,"
				+ GroupEntity.KEY_GROUPID + " TEXT,"
				+ GroupEntity.KEY_CREATEDAT + " TEXT,"
				+ GroupEntity.KEY_UPDATEDAT + " TEXT"
				+ ");");
		
		// 联系人、公司表
		db.execSQL("CREATE TABLE " + TABLE_PARTY + " ("
				+ ContactEntity._ID + " INTEGER PRIMARY KEY," 
				+ ContactEntity.KEY_NAME + " TEXT,"
				+ ContactEntity.KEY_TITLE + " TEXT,"
				+ ContactEntity.KEY_BACKGROUND + " TEXT,"
				+ ContactEntity.KEY_TENANTID + " TEXT,"
				+ ContactEntity.KEY_EMAIL + " TEXT,"
				+ ContactEntity.KEY_PHONE + " TEXT,"
				+ ContactEntity.KEY_WEBSITES + " TEXT,"
				+ ContactEntity.KEY_ADDRESS + " TEXT,"
				+ ContactEntity.KEY_IM + " TEXT,"
				+ ContactEntity.KEY_PARTYTYPE + " TEXT,"
				+ ContactEntity.KEY_COMPANYID + " TEXT,"
				+ ContactEntity.KEY_COMPANYNAME + " TEXT,"
				+ ContactEntity.KEY_PARTYFACE + " TEXT,"
				+ ContactEntity.KEY_OWNERID + " TEXT,"
				+ ContactEntity.KEY_VISIBLETO + " TEXT,"
				+ ContactEntity.KEY_UPDATEDAT + " TEXT,"
				+ ContactEntity.KEY_CREATEDAT + " TEXT,"
				+ ContactEntity.KEY_SORT_KEY + " TEXT,"
				+ ContactEntity.KEY_PARTYID + " TEXT"
//				+ " PRIMARY KEY(_id,partyid)" //联合主键
				+ ");");
		
		// 记录表
		db.execSQL("CREATE TABLE " + TABLE_NOTE + " (" 
				+ NoteEntity._ID + " INTEGER PRIMARY KEY," 
				+ NoteEntity.KEY_BODY + " TEXT,"
				+ NoteEntity.KEY_SUBJECTID + " TEXT,"
				+ NoteEntity.KEY_SUBJECTTYPE + " TEXT,"
				+ NoteEntity.KEY_SUBJECTNAME + " TEXT,"
				+ NoteEntity.KEY_DUEAT + " TEXT,"
				+ NoteEntity.KEY_UPDATEDAT + " TEXT,"
				+ NoteEntity.KEY_CREATEDAT + " TEXT,"
				+ NoteEntity.KEY_OWNERID + " TEXT,"
				+ NoteEntity.KEY_VISIBLETO + " TEXT,"
				+ NoteEntity.KEY_OWNERNAME + " TEXT,"
				+ NoteEntity.KEY_SUBJECTFACE + " TEXT,"
				+ NoteEntity.KEY_NOTEID + " TEXT"
				+ ");");
		
		// 计划任务表
		db.execSQL("CREATE TABLE " + TABLE_TASK + " (" 
				+ TaskEntity._ID + " INTEGER PRIMARY KEY," 
				+ TaskEntity.KEY_BODY + " TEXT,"
				+ TaskEntity.KEY_SUBJECTID + " TEXT,"
				+ TaskEntity.KEY_SUBJECTTYPE + " TEXT,"
				+ TaskEntity.KEY_SUBJECTNAME + " TEXT,"
				+ TaskEntity.KEY_TASKTYPEID + " TEXT,"
				+ TaskEntity.KEY_TASKTYPE + " TEXT,"
				+ TaskEntity.KEY_TENANTID + " TEXT,"
				+ TaskEntity.KEY_DUEAT + " TEXT,"
				+ TaskEntity.KEY_OWNERID + " TEXT,"
				+ TaskEntity.KEY_OWNERNAME + " TEXT,"
				+ TaskEntity.KEY_STATUS + " TEXT,"
				+ TaskEntity.KEY_DUEATTYPE + " TEXT,"
				+ TaskEntity.KEY_FINISHAT + " TEXT,"
				+ TaskEntity.KEY_CREATEUSERID + " TEXT,"
				+ TaskEntity.KEY_VISIBLETO + " TEXT,"
				+ TaskEntity.KEY_UPDATEDAT + " TEXT,"
				+ TaskEntity.KEY_CREATEDAT + " TEXT,"
				+ TaskEntity.KEY_TASKID + " TEXT"
				+ ");");
		
		// 最近行动表
		db.execSQL("CREATE TABLE " + TABLE_LATEST_ACTIVITY + " (" 
				+ ActionEntity._ID + " INTEGER PRIMARY KEY," 
				+ ActionEntity.KEY_ACTIVITYTYPE + " TEXT,"
				+ ActionEntity.KEY_ACTIVITYCONTENT + " TEXT,"
				+ ActionEntity.KEY_ACTIVITYSTATUS + " TEXT,"
				+ ActionEntity.KEY_ACTIVITYCREATE + " TEXT,"
				+ ActionEntity.KEY_PARENTID + " TEXT,"
				+ ActionEntity.KEY_SUBJECTID + " TEXT,"
				+ ActionEntity.KEY_SUBJECTTYPE + " TEXT,"
				+ ActionEntity.KEY_SUBJECTNAME + " TEXT,"
				+ ActionEntity.KEY_SUBJECTFACE + " TEXT,"
				+ ActionEntity.KEY_OWNERID + " TEXT,"
				+ ActionEntity.KEY_OWNERNAME + " TEXT,"
				+ ActionEntity.KEY_ACTIVITYDATE + " TEXT,"
				+ ActionEntity.KEY_UPDATEDAT + " TEXT,"
				+ ActionEntity.KEY_CREATEDAT + " TEXT,"
				+ ActionEntity.KEY_LATESTACTIVITYID + " TEXT"
				+ ");");
		
		// 分类表
		db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" 
				+ CategoryEntity._ID + " INTEGER PRIMARY KEY," 
				+ CategoryEntity.KEY_TENANTID + " TEXT,"
				+ CategoryEntity.KEY_CATEGORYNAME + " TEXT,"
				+ CategoryEntity.KEY_CATEGORYTYPE + " TEXT,"
				+ CategoryEntity.KEY_CATEGORYCOLOR + " TEXT,"
				+ CategoryEntity.KEY_CATEGORYID + " TEXT,"
				+ CategoryEntity.KEY_CREATEDAT + " TEXT,"
				+ CategoryEntity.KEY_UPDATEDAT + " TEXT"
				+ ");");
		
		// 计划任务权限表
		db.execSQL("CREATE TABLE " + TABLE_AUTHORITY + " (" 
				+ AuthorityEntity._ID + " INTEGER PRIMARY KEY," 
				+ AuthorityEntity.KEY_TENANTID + " TEXT,"
				+ AuthorityEntity.KEY_DATAID + " TEXT,"
				+ AuthorityEntity.KEY_OWNERS + " TEXT,"
				+ AuthorityEntity.KEY_VISIBLETO + " TEXT,"
				+ AuthorityEntity.KEY_AUTHID + " TEXT"
				+ ");");
		
		// 头像表
		db.execSQL("CREATE TABLE " + TABLE_PHOTO + " (" 
				+ PhotoEntity._ID + " INTEGER PRIMARY KEY," 
				+ PhotoEntity.KEY_PARTYID + " TEXT,"
				+ PhotoEntity.KEY_NAME + " TEXT,"
				+ PhotoEntity.KEY_EXT + " TEXT,"
				+ PhotoEntity.KEY_CONTENT + " TEXT,"
				+ PhotoEntity.KEY_PHOTOUPDATEDATE + " TEXT"
				+ ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion >= oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTY);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

			onCreate(db);
		}
	}
}
