package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 计划任务权限实体类
 */
public class AuthorityEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.AuthorityProvider";
	public static final String PATH = "authoritys";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying." + PATH;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying." + PATH;
	
	// 数据库字段
	public static final String KEY_TENANTID = "tenantId";
	public static final String KEY_DATAID = "dataId";
	public static final String KEY_OWNERS = "owners";
	public static final String KEY_VISIBLETO = "visibleto";
	public static final String KEY_AUTHID = "authId";
	
	public static final String DEFAULT_SORT_ORDER = KEY_DATAID + " COLLATE LOCALIZED ASC";
	
	//实体类属性
	private String tenantId = "";
	private String dataId = "";
	private String owners = "";
	private String visibleto = "";
	private String authId = "";
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public String getOwners() {
		return owners;
	}

	public void setOwners(String owners) {
		this.owners = owners;
	}

	public String getVisibleto() {
		return visibleto;
	}

	public void setVisibleto(String visibleto) {
		this.visibleto = visibleto;
	}
	
	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	/**
	 * 构造器
	 */
	public AuthorityEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public AuthorityEntity(Cursor cursor) {
		tenantId = cursor.getString(1);
		dataId = cursor.getString(2);
		owners = cursor.getString(3);
		visibleto = cursor.getString(4);
		authId = cursor.getString(5);
	}
	
	public AuthorityEntity(JSONObject jsonObj){
		try {
			dataId = jsonObj.getString("dataId");
			owners = jsonObj.getString("owners");
			authId = jsonObj.getString("authId");
			visibleto = jsonObj.getString("visibleto");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TENANTID, tenantId);
		initialValues.put(KEY_DATAID, dataId);
		initialValues.put(KEY_OWNERS, owners);
		initialValues.put(KEY_VISIBLETO, visibleto);
		initialValues.put(KEY_AUTHID, authId);
		return initialValues;
	}
	
	public JSONObject toJSONObject(){
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("authId", authId);
			jsonObj.put("dataId", dataId);
			jsonObj.put("owners", owners);
			jsonObj.put("visibleto", visibleto);
//			jsonObj.put("subjectName", subjectName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}
