package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhuying.android.util.HanziToPinyin;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 计划任务实体类
 */
public class TaskEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.PlanProvider";
	public static final String PATH = "plans";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying.plans";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying.plans";
	
	// 数据库字段
	public static final String KEY_BODY = "body"; // 内容
	public static final String KEY_SUBJECTID = "subjectid"; // 关联对象id
	public static final String KEY_SUBJECTTYPE = "subjecttype"; // 关联对象类型
	public static final String KEY_SUBJECTNAME = "subjectname"; // 关联对象名称
	public static final String KEY_TASKTYPEID = "tasktypeid";
	public static final String KEY_TASKTYPE = "tasktype";
	public static final String KEY_TENANTID = "tenantid";
	public static final String KEY_DUEAT = "dueat";
	public static final String KEY_OWNERID = "ownerid"; // 负责人id
	public static final String KEY_OWNERNAME = "ownerName"; // 负责人姓名
	public static final String KEY_STATUS = "status";
	public static final String KEY_DUEATTYPE = "dueattype";
	public static final String KEY_FINISHAT = "finishat";
	public static final String KEY_CREATEUSERID = "createuserid";
	public static final String KEY_VISIBLETO = "visibleto"; // 权限范围
	public static final String KEY_UPDATEDAT = "updatedat"; // 最后修改时间
	public static final String KEY_CREATEDAT = "createdat"; // 创建时间
	public static final String KEY_TASKID = "taskid"; // 主键
	
	public static final String DEFAULT_SORT_ORDER = KEY_BODY + " ASC";
	
	//实体类属性
	private String body = "";
	private String subjectId = "";
	private String subjectType = "";
	private String subjectName = "";
	private String taskTypeId = "";
	private String taskType = "";
	private String tenantId = "";
	private String dueat = "";
	private String ownerId = "";
	private String ownerName = "";
	private String status = "unfinish";
	private String dueAtType = "";
	private String finishAt = "";
	private String createUserId = "";
	private String visibleTo = "";
	private String updateAt = "";
	private String createAt = "";
	private String taskid = "";
	
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
	public String getDueAtType() {
		return dueAtType;
	}
	public void setDueAtType(String dueAtType) {
		this.dueAtType = dueAtType;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTaskTypeId() {
		return taskTypeId;
	}
	public void setTaskTypeId(String taskTypeId) {
		this.taskTypeId = taskTypeId;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getDueat() {
		return dueat;
	}
	public void setDueat(String dueat) {
		this.dueat = dueat;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFinishAt() {
		return finishAt;
	}
	public void setFinishAt(String finishAt) {
		this.finishAt = finishAt;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
	public String getVisibleTo() {
		return visibleTo;
	}
	public void setVisibleTo(String visibleTo) {
		this.visibleTo = visibleTo;
	}
	public String getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(String updateAt) {
		this.updateAt = updateAt;
	}
	public String getCreateAt() {
		return createAt;
	}
	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public TaskEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public TaskEntity(Cursor cursor) {
		body = cursor.getString(1);
		subjectId = cursor.getString(2);
		subjectType = cursor.getString(3);
		subjectName = cursor.getString(4);
		taskTypeId = cursor.getString(5);
		taskType = cursor.getString(6);
		tenantId = cursor.getString(7);
		dueat = cursor.getString(8);
		ownerId = cursor.getString(9);
		ownerName = cursor.getString(10);
		status = cursor.getString(11);
		dueAtType = cursor.getString(12);
		finishAt = cursor.getString(13);
		createUserId = cursor.getString(14);
		visibleTo = cursor.getString(15);
		updateAt = cursor.getString(16);
		createAt = cursor.getString(17);
		taskid = cursor.getString(18);
	}
	
	/**
	 * 构造器：用JSON对象构造实体类 
	 * @param jsonObj
	 */
	public TaskEntity(JSONObject jsonObj){
		try {
			body = jsonObj.getString("body");
			subjectId = jsonObj.getString("subjectid");
			subjectType = jsonObj.getString("subjectType");
			subjectName = jsonObj.getString("subjectName");
			taskTypeId = jsonObj.getString("tasktypeid");
			taskType = jsonObj.getString("tasktype");
//			tenantId = jsonObj.getString("websites");
			dueat = jsonObj.getString("dueat");
			ownerId = jsonObj.getString("ownerid");
			ownerName = jsonObj.getString("ownername");
			status = jsonObj.getString("status");
			dueAtType = jsonObj.getString("dueattype");
			finishAt = jsonObj.getString("finishat");
			createUserId = jsonObj.getString("createUserId");
			visibleTo = jsonObj.getString("visibleto");
			updateAt = jsonObj.getString("updatedat");
			createAt = jsonObj.getString("createdat");
			taskid = jsonObj.getString("taskid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public JSONObject toJSONObject(){
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("taskid", taskid);
			jsonObj.put("body", body);
			jsonObj.put("subjectid", subjectId);
			jsonObj.put("subjectType", subjectType);
			jsonObj.put("subjectName", subjectName);
			jsonObj.put("tasktypeid", taskTypeId);
			jsonObj.put("tasktype", taskType);
			jsonObj.put("dueat", dueat);
			jsonObj.put("ownerid", ownerId);
			jsonObj.put("ownername", ownerName);
			jsonObj.put("status", status);
			jsonObj.put("dueattype", dueAtType);
			jsonObj.put("finishat", finishAt);
			jsonObj.put("createUserId", createUserId);
			jsonObj.put("visibleto", visibleTo);
			jsonObj.put("createdat", createAt);
			jsonObj.put("updatedat", updateAt);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_BODY, body);
		initialValues.put(KEY_SUBJECTID, subjectId);
		initialValues.put(KEY_SUBJECTTYPE, subjectType);
		initialValues.put(KEY_SUBJECTNAME, subjectName);
		initialValues.put(KEY_TASKTYPEID, taskTypeId);
		initialValues.put(KEY_TASKTYPE, taskType);
		initialValues.put(KEY_TENANTID, tenantId);
		initialValues.put(KEY_DUEAT, dueat);
		initialValues.put(KEY_OWNERID, ownerId);
		initialValues.put(KEY_OWNERNAME, ownerName);
		initialValues.put(KEY_STATUS, status);
		initialValues.put(KEY_DUEATTYPE, dueAtType);
		initialValues.put(KEY_FINISHAT, finishAt);
		initialValues.put(KEY_CREATEUSERID, createUserId);
		initialValues.put(KEY_VISIBLETO, visibleTo);
		initialValues.put(KEY_UPDATEDAT, updateAt);
		initialValues.put(KEY_CREATEDAT, createAt);
		initialValues.put(KEY_TASKID, taskid);
		return initialValues;
	}
}
