package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 组实体类
 */
public class GroupEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.GroupProvider";
	public static final String PATH = "groups";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying." + PATH;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying." + PATH;
	
	// 数据库字段
	public static final String KEY_NAME = "name"; // 组名
	public static final String KEY_GROUPID = "groupid";
	public static final String KEY_CREATEDAT = "createdat"; 
	public static final String KEY_UPDATEDAT = "updatedat"; 
	
	public static final String DEFAULT_SORT_ORDER = KEY_NAME + " COLLATE LOCALIZED ASC";
	
	//实体类属性
	private String name = "";
	private String groupid = "";
	private String createdat = "";
	private String updatedat = "";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getCreatedat() {
		return createdat;
	}
	public void setCreatedat(String createdat) {
		this.createdat = createdat;
	}
	public String getUpdatedat() {
		return updatedat;
	}
	public void setUpdatedat(String updatedat) {
		this.updatedat = updatedat;
	}
	/**
	 * 构造器
	 */
	public GroupEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public GroupEntity(Cursor cursor) {
		name = cursor.getString(1);
		groupid = cursor.getString(2);
		createdat = cursor.getString(3);
		updatedat = cursor.getString(4);
	}
	
	/**
	 * 构造器：用JSON对象构造实体类 
	 * @param jsonObj
	 */
	public GroupEntity(JSONObject jsonObj){
		try {
			name = jsonObj.getString("name");
			groupid = jsonObj.getString("groupid");
			createdat = jsonObj.getString("createdat");
			updatedat = jsonObj.getString("updatedat");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_GROUPID, groupid);
		initialValues.put(KEY_CREATEDAT, createdat);
		initialValues.put(KEY_UPDATEDAT, updatedat);
		return initialValues;
	}
}
