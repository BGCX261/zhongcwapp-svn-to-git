package com.ufida.gkb.entity;

import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class AccountEntity implements IJSONEntity {
	private int id;
	private String contactName;
	private String accountName;
	private String note;
	private String phone;
	private String mobile;
	private String website;
	private String email;
	private int isDel = 0;
	private String createTime;
	private String updateTime;
	private int ownerUserId;
	private int orgId;
	private String address;
	

	public AccountEntity() {
	}

	public AccountEntity(JSONObject jsonobj) {
		if (jsonobj != null) {
			try {
				id = jsonobj.getInt("accountId");
				contactName = jsonobj.getString("contactName");
				accountName = jsonobj.getString("accountName");
				note = jsonobj.getString("note");
				phone = jsonobj.getString("phone");
				mobile = jsonobj.getString("mobile");
				email = jsonobj.getString("email");
				website = jsonobj.getString("website");
				isDel = jsonobj.getInt("isDel");
				createTime = jsonobj.getString("createTime");
				updateTime = jsonobj.getString("updateTime");
				ownerUserId = jsonobj.getInt("ownerUserId");
				orgId = jsonobj.getInt("orgId");
				address = jsonobj.getString("address");
			} catch (Exception e) {
				Log.e("AccountEntity", e.getMessage());
			}
		}
	}

	public AccountEntity(Cursor cursor) {
		id = cursor.getInt(Model.IDX_ROWID);
		contactName = cursor.getString(Model.IDX_CONTACTNAME);
		accountName = cursor.getString(Model.IDX_ACCOUNTNAME);
		note = cursor.getString(Model.IDX_NOTE);
		phone = cursor.getString(Model.IDX_PHONE);
		mobile = cursor.getString(Model.IDX_MOBILE);
		website = cursor.getString(Model.IDX_WEBSITE);
		email = cursor.getString(Model.IDX_EMAIL);
		isDel = cursor.getInt(Model.IDX_ISDEL);
		createTime = cursor.getString(Model.IDX_CREATETIME);
		updateTime = cursor.getString(Model.IDX_UPDATETIME);
		ownerUserId = cursor.getInt(Model.IDX_OWNERUSERID);
		orgId = cursor.getInt(Model.IDX_ORGID);
		address = cursor.getString(Model.IDX_ADDRESS);
		
	}

	public Integer getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(Integer ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		if ("null".equals(phone))
			phone = "";
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
		if ("null".equals(website))
			website = "";
		this.website = website;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.put(Model.KEY_ACCOUNTID, this.id);
			obj.put(Model.KEY_CONTACTNAME, this.contactName);
			obj.put(Model.KEY_ACCOUNTNAME, this.accountName);
			obj.put(Model.KEY_NOTE, this.note);
			obj.put(Model.KEY_PHONE, this.phone);
			obj.put(Model.KEY_MOBILE, this.mobile);
			obj.put(Model.KEY_EMAIL, this.email);
			obj.put(Model.KEY_WEBSITE, this.website);
			obj.put(Model.KEY_ISDEL, this.isDel);
			obj.put(Model.KEY_CREATETIME, this.createTime);
			obj.put(Model.KEY_UPDATETIME, this.updateTime);
			obj.put(Model.KEY_OWNERUSERID, this.ownerUserId);
			obj.put(Model.KEY_ORGID, this.orgId);
			obj.put(Model.KEY_ADDRESS, this.address);
			
			return obj;
		} catch (Exception e) {
			Log.e("AccountEntity", e.getMessage());
			return null;
		}
	}

	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(Model.KEY_ROWID, id);
		initialValues.put(Model.KEY_CONTACTNAME, contactName);
		initialValues.put(Model.KEY_ACCOUNTNAME, accountName);
		initialValues.put(Model.KEY_NOTE, note);
		initialValues.put(Model.KEY_PHONE, phone);
		initialValues.put(Model.KEY_MOBILE, mobile);
		initialValues.put(Model.KEY_WEBSITE, website);
		initialValues.put(Model.KEY_EMAIL, email);
		initialValues.put(Model.KEY_ISDEL, isDel);
		initialValues.put(Model.KEY_CREATETIME, createTime);
		initialValues.put(Model.KEY_UPDATETIME, updateTime);
		initialValues.put(Model.KEY_OWNERUSERID, ownerUserId);
		initialValues.put(Model.KEY_ORGID, orgId);
		initialValues.put(Model.KEY_ADDRESS,address);
		return initialValues;
	}

	public static final class Model implements BaseColumns {
		public static final String AUTHORITY = "com.ufida.gkb.accounts";

		// This class cannot be instantiated
		private Model() {
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/list");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.gkb.account";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gkb.account";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "updateTime DESC";

		public static final String KEY_ROWID = "_id"; // 0
		public static final String KEY_CONTACTNAME = "contactName";
		public static final String KEY_ACCOUNTNAME = "accountName";
		public static final String KEY_NOTE = "note";
		public static final String KEY_PHONE = "phone";
		public static final String KEY_MOBILE = "mobile"; // 5
		public static final String KEY_WEBSITE = "website";
		public static final String KEY_EMAIL = "email";
		public static final String KEY_ISDEL = "isDel";
		public static final String KEY_CREATETIME = "createTime";
		public static final String KEY_UPDATETIME = "updateTime"; // 10
		public static final String KEY_OWNERUSERID = "ownerUserId";
		public static final String KEY_ORGID = "orgId";
		public static final String KEY_ACCOUNTID = "accountId"; // 13
		public static final String KEY_ADDRESS = "address";

		public static final int IDX_ROWID = 0; // 0
		public static final int IDX_CONTACTNAME = 1;
		public static final int IDX_ACCOUNTNAME = 2;
		public static final int IDX_NOTE = 3;
		public static final int IDX_PHONE = 4;
		public static final int IDX_MOBILE = 5; // 5
		public static final int IDX_WEBSITE = 6;
		public static final int IDX_EMAIL = 7;
		public static final int IDX_ISDEL = 8;
		public static final int IDX_CREATETIME = 9;
		public static final int IDX_UPDATETIME = 10; // 10
		public static final int IDX_OWNERUSERID = 11;
		public static final int IDX_ORGID = 12;
		public static final int IDX_ADDRESS = 13;
	}
}
