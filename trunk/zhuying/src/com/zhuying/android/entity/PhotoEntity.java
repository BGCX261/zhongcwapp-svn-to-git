package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhuying.android.util.HanziToPinyin;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 头像实体类
 */
public class PhotoEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.PhotoProvider";
	public static final String PATH = "photos";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying." + PATH;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying." + PATH;
	
	// 数据库字段
	public static final String KEY_PARTYID = "partyid";
	public static final String KEY_NAME = "name";
	public static final String KEY_EXT = "ext";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_PHOTOUPDATEDATE = "photoUpdateDate";
	
	public static final String DEFAULT_SORT_ORDER = KEY_NAME + " COLLATE LOCALIZED ASC";
	
	//实体类属性
	private String partyId = "";
	private String name = "";
	private String ext = "";
	private String content = "";
	private String photoUpdateDate = "";
	
	public String getPartyId() {
		return partyId;
	}
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getPhotoUpdateDate() {
		return photoUpdateDate;
	}
	public void setPhotoUpdateDate(String photoUpdateDate) {
		this.photoUpdateDate = photoUpdateDate;
	}
	/**
	 * 构造器
	 */
	public PhotoEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public PhotoEntity(Cursor cursor) {
		partyId = cursor.getString(1);
		name = cursor.getString(2);
		ext = cursor.getString(3);
		content = cursor.getString(4);
		photoUpdateDate = cursor.getString(5);
	}
	
	/**
	 * 构造器 
	 * @param jsonObj
	 */
	public PhotoEntity(JSONObject jsonObj){
		try {
			partyId = jsonObj.getString("partyId");
			name = jsonObj.getString("name");
			ext = jsonObj.getString("ext");
			content = jsonObj.getString("content");
			photoUpdateDate = jsonObj.getString("photoUpdateDate");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_PARTYID, partyId);
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_EXT, ext);
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_PHOTOUPDATEDATE, photoUpdateDate);
		return initialValues;
	}
	
	/**
	 * 转为 JSON对象
	 * @return JSONObject
	 */
	public JSONObject toJSONObject(){
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("partyId", partyId);
			jsonObj.put("name", name);
			jsonObj.put("ext", ext);
			jsonObj.put("content", content);
			jsonObj.put("photoUpdateDate", photoUpdateDate);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}
