package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 最近行动实体类
 */
public class ActionEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.ActionProvider";
	public static final String PATH = "actions";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying." + PATH;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying."+ PATH;
	
	// 数据库字段
	public static final String KEY_ACTIVITYTYPE = "activitytype"; // 类型（note（记录）、deal（业务机会）、task（计划任务））
	public static final String KEY_ACTIVITYCONTENT = "activitycontent"; // 活动内容
	public static final String KEY_ACTIVITYSTATUS = "activitystatus"; // 状态
	public static final String KEY_ACTIVITYCREATE = "activitycreate"; // 创建信息（建立人及时间）
	public static final String KEY_PARENTID = "parentid"; // 对应note，deal，task的id
	public static final String KEY_SUBJECTID = "subjectid"; // 关联对象id
	public static final String KEY_SUBJECTTYPE = "subjecttype"; // 关联对象类型
	public static final String KEY_SUBJECTNAME = "subjectname"; // 关联对象名称
	public static final String KEY_SUBJECTFACE = "subjectface"; // 关联对象的logo地址
	public static final String KEY_OWNERID = "ownerid"; // 负责人id
	public static final String KEY_OWNERNAME = "ownername"; // 负责人姓名
	public static final String KEY_ACTIVITYDATE = "activitydate"; // 行动时间
	public static final String KEY_UPDATEDAT = "updatedat"; // 最后修改时间
	public static final String KEY_CREATEDAT = "createdat"; // 创建时间
	public static final String KEY_LATESTACTIVITYID = "latestactivityid"; // 主键
	
	public static final String DEFAULT_SORT_ORDER = KEY_UPDATEDAT + " DESC";
	
	public static final String TYPE_NOTE = "note";
	public static final String TYPE_TASK = "task";
	public static final String TYPE_COMMENT = "comment"; //批注

	//实体类属性
	private String activityType = "";
	private String activityContent = "";
	private String activityStatus = "";
	private String activityCreate = "";
	private String parentId = "";
	private String subjectId = "";
	private String subjectType = "";
	private String subjectName = "";
	private String subjectFace = "";
	private String ownerId = "";
	private String ownerName = "";
	private String activityDate = "";
	private String updateDat = "";
	private String createDat = "";
	private String latestactivityid = "";
	
	
	public String getActivitytype() {
		return activityType;
	}

	public void setActivitytype(String activitytype) {
		this.activityType = activitytype;
	}

	public String getActivitycontent() {
		return activityContent;
	}

	public void setActivitycontent(String activitycontent) {
		this.activityContent = activitycontent;
	}

	public String getActivitystatus() {
		return activityStatus;
	}

	public void setActivitystatus(String activitystatus) {
		this.activityStatus = activitystatus;
	}

	public String getActivitycreate() {
		return activityCreate;
	}

	public void setActivitycreate(String activitycreate) {
		this.activityCreate = activitycreate;
	}

	public String getParentid() {
		return parentId;
	}

	public void setParentid(String parentid) {
		this.parentId = parentid;
	}

	public String getSubjectid() {
		return subjectId;
	}

	public void setSubjectid(String subjectid) {
		this.subjectId = subjectid;
	}

	public String getSubjecttype() {
		return subjectType;
	}

	public void setSubjecttype(String subjecttype) {
		this.subjectType = subjecttype;
	}

	public String getSubjectname() {
		return subjectName;
	}

	public void setSubjectname(String subjectname) {
		this.subjectName = subjectname;
	}

	public String getSubjectface() {
		return subjectFace;
	}

	public void setSubjectface(String subjectface) {
		this.subjectFace = subjectface;
	}

	public String getOwnerid() {
		return ownerId;
	}

	public void setOwnerid(String ownerid) {
		this.ownerId = ownerid;
	}

	public String getOwnername() {
		return ownerName;
	}

	public void setOwnername(String ownername) {
		this.ownerName = ownername;
	}

	public String getActivitydate() {
		return activityDate;
	}

	public void setActivitydate(String activitydate) {
		this.activityDate = activitydate;
	}

	public String getUpdatedat() {
		return updateDat;
	}

	public void setUpdatedat(String updatedat) {
		this.updateDat = updatedat;
	}

	public String getCreatedat() {
		return createDat;
	}

	public void setCreatedat(String createdat) {
		this.createDat = createdat;
	}
	
	public String getLatestactivityid() {
		return latestactivityid;
	}

	public void setLatestactivityid(String latestactivityid) {
		this.latestactivityid = latestactivityid;
	}

	public ActionEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public ActionEntity(Cursor cursor) {
		activityType = cursor.getString(1);
		activityContent = cursor.getString(2);
		activityStatus = cursor.getString(3);
		activityCreate = cursor.getString(4);
		parentId = cursor.getString(5);
		subjectId = cursor.getString(6);
		subjectType = cursor.getString(7);
		subjectName = cursor.getString(8);
		subjectFace = cursor.getString(9);
		ownerId = cursor.getString(10);
		ownerName = cursor.getString(11);
		activityDate = cursor.getString(12);
		updateDat = cursor.getString(13);
		createDat = cursor.getString(14);
		latestactivityid = cursor.getString(15);
	}
	
	/**
	 * 构造器：用JSON对象构造实体类 
	 * @param jsonObj
	 */
	public ActionEntity(JSONObject jsonObj){
		try {
			activityType = jsonObj.getString("activitytype");
			activityContent = jsonObj.getString("activitycontent");
			activityStatus = jsonObj.getString("activitystatus");
			activityCreate = jsonObj.getString("activitycreate");
			parentId = jsonObj.getString("parentid");
			subjectId = jsonObj.getString("subjectid");
			subjectType = jsonObj.getString("subjectType");
			subjectName = jsonObj.getString("subjectName");
			subjectFace = jsonObj.getString("subjectface");
			ownerId = jsonObj.getString("ownerid");
			ownerName = jsonObj.getString("ownername");
			activityDate = jsonObj.getString("activitydate");
			updateDat = jsonObj.getString("updatedat");
			createDat = jsonObj.getString("createdat");
			latestactivityid = jsonObj.getString("latestactivityid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ACTIVITYTYPE, activityType);
		initialValues.put(KEY_ACTIVITYCONTENT, activityContent);
		initialValues.put(KEY_ACTIVITYSTATUS, activityStatus);
		initialValues.put(KEY_ACTIVITYCREATE, activityCreate);
		initialValues.put(KEY_PARENTID, parentId);
		initialValues.put(KEY_SUBJECTID, subjectId);
		initialValues.put(KEY_SUBJECTTYPE, subjectType);
		initialValues.put(KEY_SUBJECTNAME, subjectName);
		initialValues.put(KEY_SUBJECTFACE, subjectFace);
		initialValues.put(KEY_OWNERID, ownerId);
		initialValues.put(KEY_OWNERNAME, ownerName);
		initialValues.put(KEY_ACTIVITYDATE, activityDate);
		initialValues.put(KEY_UPDATEDAT, updateDat);
		initialValues.put(KEY_CREATEDAT, createDat);
		initialValues.put(KEY_LATESTACTIVITYID, latestactivityid);
		return initialValues;
	}
	
	/**
	 * 转为 JSON对象
	 * @return JSONObject
	 */
	public JSONObject toJSONObject() {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("activitytype", activityType);
			jsonObj.put("activitycontent", activityContent);
			jsonObj.put("activitystatus", activityStatus);
			jsonObj.put("activitycreate", activityCreate);
			jsonObj.put("parentid", parentId);
			jsonObj.put("subjectid", subjectId);
			jsonObj.put("subjectType", subjectType);
			jsonObj.put("subjectName", subjectName);
			jsonObj.put("subjectface", subjectFace);
			jsonObj.put("ownerid", ownerId);
			jsonObj.put("ownername", ownerName);
			jsonObj.put("activitydate", activityDate);
			jsonObj.put("updatedat", updateDat);
			jsonObj.put("createdat", createDat);
			jsonObj.put("latestactivityid", latestactivityid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}
