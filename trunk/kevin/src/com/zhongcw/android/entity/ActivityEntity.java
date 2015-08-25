package com.zhongcw.android.entity;

import org.json.JSONObject;

import com.zhongcw.android.DbAdapterCalendar;

public class ActivityEntity implements IJSONEntity {
	String _id;
	String repeatType;
	String accountId;
	String actvtType;
	String ownerUserId;
	String actvtState;
	String actvtEffe;
	String effeAccountPhase;
	String startTime;
	String endTime;
	String subject;
	String contactName;
	String note;
	String actvtId;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getRepeatType() {
		return repeatType;
	}

	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getActvtType() {
		return actvtType;
	}

	public void setActvtType(String actvtType) {
		this.actvtType = actvtType;
	}

	public String getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public String getActvtState() {
		return actvtState;
	}

	public void setActvtState(String actvtState) {
		this.actvtState = actvtState;
	}

	public String getActvtEffe() {
		return actvtEffe;
	}

	public void setActvtEffe(String actvtEffe) {
		this.actvtEffe = actvtEffe;
	}

	public String getEffeAccountPhase() {
		return effeAccountPhase;
	}

	public void setEffeAccountPhase(String effeAccountPhase) {
		this.effeAccountPhase = effeAccountPhase;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getActvtId() {
		return actvtId;
	}

	public void setActvtId(String actvtId) {
		this.actvtId = actvtId;
	}

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put(DbAdapterCalendar.KEY_SUBJECT, this.subject);
			obj.put(DbAdapterCalendar.KEY_NOTE, this.note);
			obj.put(DbAdapterCalendar.KEY_STARTTIME, this.startTime);
			obj.put(DbAdapterCalendar.KEY_ENDTIME, this.endTime);
			return obj;
		} catch (Exception e) {
			return null;
		}
	}
}
