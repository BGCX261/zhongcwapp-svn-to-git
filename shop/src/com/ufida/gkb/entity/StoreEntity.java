package com.ufida.gkb.entity;

import com.ufida.gkb.entity.AccountEntity.Model;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 库存
 */
public class StoreEntity {
	private int id;
	private String number; // 编码
	private String name;// 名称
	private String spec;// 规格
	private String unit;// 单位
	private String amount;// 数量
	private String cost;// 成本
	private String money;// 金额
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public static final class Model implements BaseColumns {
		public static final String AUTHORITY = "com.ufida.gkb.stores";

		private Model() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/list");
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.gkb.store";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.gkb.store";
		 public static final String DEFAULT_SORT_ORDER = "number DESC";

		public static final String KEY_ROWID = "_id";
		public static final String KEY_NUMBER = "number";
		public static final String KEY_NAME = "name";
		public static final String KEY_SPEC = "spec";
		public static final String KEY_UNIT = "unit";
		public static final String KEY_AMOUNT = "amount";
		public static final String KEY_COST = "cost";
		public static final String KEY_MONEY = "money";

		public static final int IDX_ROWID = 0;
		public static final int IDX_NUMBER = 1;
		public static final int IDX_NAME = 2;
		public static final int IDX_SPEC = 3;
		public static final int IDX_UNIT = 4;
		public static final int IDX_AMOUNT = 5;
		public static final int IDX_COST = 6;
		public static final int IDX_MONEY = 7;

	}
	
	public ContentValues toContentValues() {
		ContentValues initialValues = new ContentValues();
		initialValues.put(Model.KEY_ROWID, id);
		initialValues.put(Model.KEY_NUMBER, number);
		initialValues.put(Model.KEY_NAME, name);
		initialValues.put(Model.KEY_SPEC, spec);
		initialValues.put(Model.KEY_UNIT, unit);
		initialValues.put(Model.KEY_AMOUNT, amount);
		initialValues.put(Model.KEY_COST, cost);
		initialValues.put(Model.KEY_MONEY, money);
		return initialValues;
	}

}
