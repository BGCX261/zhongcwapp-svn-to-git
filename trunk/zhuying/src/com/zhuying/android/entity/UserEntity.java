package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 用户实体类
 */
public class UserEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.UserProvider";
	public static final String PATH = "users";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying." + PATH;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying." + PATH;
	
	// 数据库字段
	public static final String KEY_NAME = "name"; // 登录名
	public static final String KEY_EMAIL = "email"; // email
	public static final String KEY_LASTLOGIN = "lastlogin"; // 最后登录时间
	public static final String KEY_REALNAME = "realname"; // 真实姓名
	public static final String KEY_ISLOGIN = "islogin"; // 是否是当前登录用户
	public static final String KEY_USERID = "userid"; 
	public static final String KEY_CREATEDAT = "createdat"; 
	public static final String KEY_UPDATEDAT = "updatedat"; 
	
	public static final String DEFAULT_SORT_ORDER = KEY_NAME + " COLLATE LOCALIZED ASC";
	
	//实体类属性
	private String name = "";
	private String email = "";
	private String lastLogin = "";
	private String realName = "";
	private String isLogin = "";
	private String userid = "";
	private String createdat = "";
	private String updatedat = "";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
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
	public UserEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public UserEntity(Cursor cursor) {
		name = cursor.getString(1);
		email = cursor.getString(2);
		lastLogin = cursor.getString(3);
		realName = cursor.getString(4);
		isLogin = cursor.getString(5);
		userid = cursor.getString(6);
		createdat = cursor.getString(7);
		updatedat = cursor.getString(8);
	}
	
	/**
	 * 构造器：用JSON对象构造实体类 
	 * @param jsonObj
	 */
	public UserEntity(JSONObject jsonObj){
		try {
			name = jsonObj.getString("name");
			email = jsonObj.getString("email");
			realName = jsonObj.getString("realname");
			isLogin = jsonObj.getString("islogin");
			createdat = jsonObj.getString("createdat");
			updatedat = jsonObj.getString("updatedat");
			userid = jsonObj.getString("userid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_LASTLOGIN, lastLogin);
		initialValues.put(KEY_REALNAME, realName);
		initialValues.put(KEY_ISLOGIN, isLogin);
		initialValues.put(KEY_USERID, userid);
		initialValues.put(KEY_CREATEDAT, createdat);
		initialValues.put(KEY_UPDATEDAT, updatedat);
		return initialValues;
	}
}
