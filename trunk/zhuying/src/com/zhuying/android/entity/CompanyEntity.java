package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhuying.android.util.HanziToPinyin;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * 公司实体类
 */
public class CompanyEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.CompanyProvider";
	public static final String PATH = "companys";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying."+PATH;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying."+PATH;
	
	// 数据库字段
	public static final String KEY_PARTYID = "partyid";
	public static final String KEY_NAME = "name"; // 名字
	public static final String KEY_TITLE = "title"; // 职务
	public static final String KEY_BACKGROUND = "background"; // 背景信息
	public static final String KEY_TENANTID = "tenantId"; // 租户id
	public static final String KEY_EMAIL = "email"; // 邮件
	public static final String KEY_PHONE = "phone"; // 电话
	public static final String KEY_WEBSITES = "websites"; // 网址
	public static final String KEY_ADDRESS = "address"; // 地址
	public static final String KEY_IM = "im"; // 即时通讯
	public static final String KEY_PARTYTYPE = "partytype"; // 类型（公司、联系人）
	public static final String KEY_COMPANYID = "companyid"; // 联系人所属公司id
	public static final String KEY_COMPANYNAME = "companyname"; // 联系人所属公司名称
	public static final String KEY_PARTYFACE = "partyface"; // 头像url
	public static final String KEY_OWNERID = "ownerid"; // 负责人id
	public static final String KEY_VISIBLETO = "visibleto"; // 权限范围
	public static final String KEY_UPDATEDAT = "updatedat"; // 最后修改时间
	public static final String KEY_CREATEDAT = "createdat"; // 创建时间
	public static final String KEY_SORT_KEY = "sort_key"; // 拼音排序
	
	public static final String DEFAULT_SORT_ORDER = KEY_NAME + " COLLATE LOCALIZED ASC";
	
	public static final String TYPE_CONTACT = "contact";  // 类型（联系人）
	public static final String TYPE_COMPANY = "company"; // 类型（公司）

	//实体类属性
	private String partyId = "";
	private String name = "";
	private String title = "";
	private String background = "";
	private String tenantId = "";
	private String email = "";
	private String phone = "";
	private String websites = "";
	private String address = "";
	private String im = "";
	private String partytype = "";
	private String companyid = "";
	private String companyname = "";
	private String partyface = "";
	private String ownerid = "";
	private String visibleto = "";
	private String updatedat = "";
	private String createdat = "";
	private String sortKey = "";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsites() {
		return websites;
	}

	public void setWebsites(String websites) {
		this.websites = websites;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIm() {
		return im;
	}

	public void setIm(String im) {
		this.im = im;
	}

	public String getPartytype() {
		return partytype;
	}

	public void setPartytype(String partytype) {
		this.partytype = partytype;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getPartyface() {
		return partyface;
	}

	public void setPartyface(String partyface) {
		this.partyface = partyface;
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

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	
	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	
	/**
	 * 构造器
	 */
	public CompanyEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public CompanyEntity(Cursor cursor) {
		name = cursor.getString(1);
		title = cursor.getString(2);
		background = cursor.getString(3);
		tenantId = cursor.getString(4);
		email = cursor.getString(5);
		phone = cursor.getString(6);
		websites = cursor.getString(7);
		address = cursor.getString(8);
		im = cursor.getString(9);
		partytype = cursor.getString(10);
		companyid = cursor.getString(11);
		companyname = cursor.getString(12);
		partyface = cursor.getString(13);
		ownerid = cursor.getString(14);
		visibleto = cursor.getString(15);
		updatedat = cursor.getString(16);
		createdat = cursor.getString(17);
		sortKey = cursor.getString(18);
		partyId = cursor.getString(19);
	}
	
	/**
	 * 构造器：用JSON对象构造实体类 
	 * @param jsonObj
	 */
	public CompanyEntity(JSONObject jsonObj){
		try {
			name = jsonObj.getString("name");
			title = jsonObj.getString("title");
			background = jsonObj.getString("background");
			tenantId = "";
			email = jsonObj.getString("email");
			phone = jsonObj.getString("phone");
			websites = jsonObj.getString("websites");
			address = jsonObj.getString("address");
			im = jsonObj.getString("im");
			partytype = jsonObj.getString("partytype");
			companyid = jsonObj.getString("companyid");
			companyname = jsonObj.getString("companyname");
			partyface = jsonObj.getString("partyface");
			ownerid = jsonObj.getString("ownerid");
			visibleto = jsonObj.getString("visibleTo");
			updatedat = jsonObj.getString("updatedat");
			createdat = jsonObj.getString("createdat");
			sortKey = HanziToPinyin.getPinYin(jsonObj.getString("name"));
			partyId = jsonObj.getString("partyid");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 实体类构造ContentValues对象
	 * @return
	 */
	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BACKGROUND, background);
		initialValues.put(KEY_TENANTID, tenantId);
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_PHONE, phone);
		initialValues.put(KEY_WEBSITES, websites);
		initialValues.put(KEY_ADDRESS, address);
		initialValues.put(KEY_IM, im);
		initialValues.put(KEY_PARTYTYPE, partytype);
		initialValues.put(KEY_COMPANYID, companyid);
		initialValues.put(KEY_COMPANYNAME, companyname);
		initialValues.put(KEY_PARTYFACE, partyface);
		initialValues.put(KEY_OWNERID, ownerid);
		initialValues.put(KEY_VISIBLETO, visibleto);
		initialValues.put(KEY_UPDATEDAT, updatedat);
		initialValues.put(KEY_CREATEDAT, createdat);
		initialValues.put(KEY_SORT_KEY, sortKey);
		initialValues.put(KEY_PARTYID, partyId);
		return initialValues;
	}
	
	/**
	 * 带主键的实体类构造ContentValues对象
	 * @return
	 */
//	public ContentValues toContentValuesWithId() {
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(KEY_NAME, name);
//		initialValues.put(KEY_TITLE, title);
//		initialValues.put(KEY_BACKGROUND, background);
//		initialValues.put(KEY_TENANTID, tenantId);
//		initialValues.put(KEY_EMAIL, email);
//		initialValues.put(KEY_PHONE, phone);
//		initialValues.put(KEY_WEBSITES, websites);
//		initialValues.put(KEY_ADDRESS, address);
//		initialValues.put(KEY_IM, im);
//		initialValues.put(KEY_PARTYTYPE, partytype);
//		initialValues.put(KEY_COMPANYID, companyid);
//		initialValues.put(KEY_COMPANYNAME, companyname);
//		initialValues.put(KEY_PARTYFACE, partyface);
//		initialValues.put(KEY_OWNERID, ownerid);
//		initialValues.put(KEY_VISIBLETO, visibleto);
//		initialValues.put(KEY_UPDATEDAT, updatedat);
//		initialValues.put(KEY_CREATEDAT, createdat);
//		initialValues.put(KEY_SORT_KEY, sortKey);
//		initialValues.put(KEY_PARTYID, partyId);
//		initialValues.put(CompanyEntity._ID, partyId); //partyId作为主键
//		return initialValues;
//	}
}
