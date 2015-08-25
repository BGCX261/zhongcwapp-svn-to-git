package com.zhuying.android.entity;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhuying.android.util.HanziToPinyin;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 分类实体类
 */
public class CategoryEntity implements BaseColumns {
	public static final String AUTHORITY = "com.zhuying.provider.CategoryProvider";
	public static final String PATH = "categorys";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhuying." + PATH;
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhuying." + PATH;
	
	// 数据库字段
	public static final String KEY_TENANTID = "tenantId";
	public static final String KEY_CATEGORYNAME = "categoryname"; // 分类名
	public static final String KEY_CATEGORYTYPE = "categorytype"; // 分类类型
	public static final String KEY_CATEGORYCOLOR = "categorycolor"; // 分类颜色
	public static final String KEY_CATEGORYID = "categoryid"; // 分类主键
	public static final String KEY_CREATEDAT = "createdat"; // 创建时间
	public static final String KEY_UPDATEDAT = "updatedat"; // 修改时间
	
	public static final String DEFAULT_SORT_ORDER = KEY_CATEGORYNAME + " COLLATE LOCALIZED ASC";
	
	//实体类属性
	private String tenantId = "";
	private String categoryname = "";
	private String categorytype = "";
	private String categorycolor = "";
	private String categoryid = "";
	private String createdat = "";
	private String updatedat = "";
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getCategoryname() {
		return categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}

	public String getCategorytype() {
		return categorytype;
	}

	public void setCategorytype(String categorytype) {
		this.categorytype = categorytype;
	}

	public String getCategorycolor() {
		return categorycolor;
	}

	public void setCategorycolor(String categorycolor) {
		this.categorycolor = categorycolor;
	}
	
	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
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
	public CategoryEntity() {
	}
	
	/**
	 * 构造器
	 * @param cursor
	 */
	public CategoryEntity(Cursor cursor) {
		tenantId = cursor.getString(1);
		categoryname = cursor.getString(2);
		categorytype = cursor.getString(3);
		categorycolor = cursor.getString(4);
		categoryid = cursor.getString(5);
		createdat = cursor.getString(6);
		updatedat = cursor.getString(7);
	}
	
	/**
	 * 构造器：用JSON对象构造实体类 
	 * @param jsonObj
	 */
	public CategoryEntity(JSONObject jsonObj){
		try {
			categoryname = jsonObj.getString("categoryname");
			categorytype = jsonObj.getString("categorytype");
			categorycolor = jsonObj.getString("categorycolor");
			categoryid = jsonObj.getString("categoryid");
			createdat = jsonObj.getString("createdat");
			updatedat = jsonObj.getString("updatedat");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TENANTID, tenantId);
		initialValues.put(KEY_CATEGORYNAME, categoryname);
		initialValues.put(KEY_CATEGORYTYPE, categorytype);
		initialValues.put(KEY_CATEGORYCOLOR, categorycolor);
		initialValues.put(KEY_CATEGORYID, categoryid);
		initialValues.put(KEY_CREATEDAT, createdat);
		initialValues.put(KEY_UPDATEDAT, updatedat);
		return initialValues;
	}
}
