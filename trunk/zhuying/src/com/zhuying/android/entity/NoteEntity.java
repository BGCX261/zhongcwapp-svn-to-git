package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhuying.android.util.HanziToPinyin;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 记录实体类
 */
public class NoteEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.RecordProvider";
	public static final String PATH = "records";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying.records";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying.records";
	
	// 数据库字段
	public static final String KEY_BODY = "body"; // 内容
	public static final String KEY_SUBJECTID = "subjectid"; // 关联对象id
	public static final String KEY_SUBJECTTYPE = "subjecttype"; // 关联对象类型(company、person)
	public static final String KEY_SUBJECTNAME = "subjectname"; // 关联对象名称
	public static final String KEY_DUEAT = "dueat"; // 发生日期
	public static final String KEY_UPDATEDAT = "updatedat"; // 最后修改时间
	public static final String KEY_CREATEDAT = "createdat"; // 创建时间
	public static final String KEY_OWNERID = "ownerid"; // 负责人id
	public static final String KEY_VISIBLETO = "visibleto"; // 权限范围
	public static final String KEY_OWNERNAME = "ownerName"; // 负责人姓名
	public static final String KEY_SUBJECTFACE = "subjectface"; // 关联对象logo
	public static final String KEY_NOTEID = "noteid"; // 主键
	
	public static final String DEFAULT_SORT_ORDER = KEY_BODY + " ASC";

	//实体类属性
	private String body = "";
	private String subjectId = "";
	private String subjectType = "";
	private String subjectName = "";
	private String dueat = "";
	private String updatedat = "";
	private String createdat = "";
	private String ownerid = "";
	private String visibleto = "";
	private String ownerName = "";
	private String subjectface = "";
	private String noteid = "";
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getDueat() {
		return dueat;
	}
	public void setDueat(String dueat) {
		this.dueat = dueat;
	}
	public String getUpdatedat() {
		return updatedat;
	}
	public void setUpdatedat(String updatedat) {
		this.updatedat = updatedat;
	}
	public String getCreatedat() {
		return createdat;
	}
	public void setCreatedat(String createdat) {
		this.createdat = createdat;
	}
	public String getOwnerid() {
		return ownerid;
	}
	public void setOwnerid(String ownerid) {
		this.ownerid = ownerid;
	}
	public String getVisibleto() {
		return visibleto;
	}
	public void setVisibleto(String visibleto) {
		this.visibleto = visibleto;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getSubjectface() {
		return subjectface;
	}
	public void setSubjectface(String subjectface) {
		this.subjectface = subjectface;
	}
	public String getNoteid() {
		return noteid;
	}
	public void setNoteid(String noteid) {
		this.noteid = noteid;
	}
	public NoteEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public NoteEntity(Cursor cursor) {
		body = cursor.getString(1);
		subjectId = cursor.getString(2);
		subjectType = cursor.getString(3);
		subjectName = cursor.getString(4);
		dueat = cursor.getString(5);
		updatedat = cursor.getString(6);
		createdat = cursor.getString(7);
		ownerid = cursor.getString(8);
		visibleto = cursor.getString(9);
		ownerName = cursor.getString(10);
		subjectface = cursor.getString(11);
		noteid = cursor.getString(12);
	}
	
	/**
	 * 转为 JSON对象
	 * @return JSONObject
	 */
	public JSONObject toJSONObject(){
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("noteid", noteid);
			jsonObj.put("body", body);
			jsonObj.put("subjectid", subjectId);
			jsonObj.put("subjecttype", subjectType);
			jsonObj.put("subjectname", subjectName);
			jsonObj.put("dueat", dueat);
			jsonObj.put("ownerid", ownerid);
			jsonObj.put("ownerName", ownerName);
			jsonObj.put("subjectface", subjectface);
			jsonObj.put("visibleto", visibleto);
			jsonObj.put("createdat", createdat);
			jsonObj.put("updatedat", updatedat);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
	
	/**
	 * 构造器：用JSON对象构造实体类 
	 * @param jsonObj
	 */
	public NoteEntity(JSONObject jsonObj){
		try {
			body = jsonObj.getString("body");
			subjectId = jsonObj.getString("subjectid");
			subjectType = jsonObj.getString("subjecttype");
			subjectName = jsonObj.getString("subjectname");
			dueat = jsonObj.getString("dueat");
			ownerid = jsonObj.getString("ownerid");
			ownerName = jsonObj.getString("ownerName");
			subjectface = jsonObj.getString("subjectface");
			visibleto = jsonObj.getString("visibleto");
			createdat = jsonObj.getString("createdat");
			updatedat = jsonObj.getString("updatedat");
			noteid = jsonObj.getString("noteid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_BODY, body);
		initialValues.put(KEY_SUBJECTID, subjectId);
		initialValues.put(KEY_SUBJECTTYPE, subjectType);
		initialValues.put(KEY_SUBJECTNAME, subjectName);
		initialValues.put(KEY_DUEAT, dueat);
		initialValues.put(KEY_UPDATEDAT, updatedat);
		initialValues.put(KEY_CREATEDAT, createdat);
		initialValues.put(KEY_OWNERID, ownerid);
		initialValues.put(KEY_VISIBLETO, visibleto);
		initialValues.put(KEY_OWNERNAME, ownerName);
		initialValues.put(KEY_SUBJECTFACE, subjectface);
		initialValues.put(KEY_NOTEID, noteid);
		return initialValues;
	}
}
