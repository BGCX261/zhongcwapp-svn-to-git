package com.zhongcw.android.entity;

import org.json.JSONObject;

import com.zhongcw.android.DbAdapter;

public class AccountEntity implements IJSONEntity {
	private String _id;
	private String contactName;
	private String accountName;
	private String note;
	private String phone;
	private String mobile;
	private String website;
	private Integer ownerUserId;
	private String updateTime;
	private Integer isDel;
	private String createTime;
	private Integer ordId;
	private Integer accountId;
	
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	public Integer getOwnerUserId() {
		return ownerUserId;
	}
	public void setOwnerUserId(Integer ownerUserId) {
		this.ownerUserId = ownerUserId;
	}
	public Integer getIsDel() {
		return isDel;
	}
	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}
	public Integer getOrdId() {
		return ordId;
	}
	public void setOrdId(Integer ordId) {
		this.ordId = ordId;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	String email;

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put(DbAdapter.KEY_ACCOUNTID, this.accountId);
			obj.put(DbAdapter.KEY_ACCOUNTNAME, this.accountName);
			obj.put(DbAdapter.KEY_CONTACTNAME, this.contactName);
			obj.put(DbAdapter.KEY_CREATETIME, this.createTime);
			obj.put(DbAdapter.KEY_ISDEL, this.isDel);
			obj.put(DbAdapter.KEY_ORGID, this.ordId);
			obj.put(DbAdapter.KEY_OWNERUSERID, this.ownerUserId);
			obj.put(DbAdapter.KEY_UPDATETIME, this.updateTime);
			obj.put(DbAdapter.KEY_WEBSITE, this.website);
			return obj;
		} catch (Exception e) {
			return null;
		}
	}
}
