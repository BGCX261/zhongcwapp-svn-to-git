package com.zhongcw.android;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhongcw.android.entity.AccountEntity;
import com.zhongcw.android.entity.ActivityEntity;
import com.zhongcw.android.service.AccountService;
import com.zhongcw.android.service.ActivityService;
import com.zhongcw.android.service.LoginService;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class MyListActivity extends ListActivity {
	ListView listView;
	List list;

	private static DbAdapter mDbHelper;
	private static SyncDbAdapter SyncmDbHelper;
	private static String STARTTIME="1970-01-01 00:00:00";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new DbAdapter(this);
		SyncmDbHelper = new SyncDbAdapter(this);
		// mDbHelper.open();
		fillData();
		// TODO 发送请求
		// new LoginService().login(null, null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// String msg = data.getExtras().getString("msg");
		this.fillData();
	}

	private void fillData() {
		mDbHelper.open();
		Cursor c = mDbHelper.fetchAll(DbAdapter.KEY_ISDEL+" = 0");
		int num = c.getCount();
		startManagingCursor(c);
		String[] from = new String[] { DbAdapter.KEY_CONTACTNAME,
				DbAdapter.KEY_ACCOUNTNAME };
		int[] to = new int[] { R.id.contactName, R.id.accountName };
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.list, c, from, to);
		setListAdapter(notes);
		listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(MyListActivity.this, FormActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("rowid", id); // rowid
				intent.putExtras(bundle);
				int requestCode = 2;
				startActivityForResult(intent, requestCode);
			}
		});

		mDbHelper.close();

		// long-press menu
		registerForContextMenu(listView);
	}

	public static JSONArray getJSONList(String jsonString) {
		// List arylist = new ArrayList();
		JSONArray jsonArray = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString); // getHttpResponse("http://10.5.30.151:6060/","http://10.5.30.151:6060/account/page.do",null)

			// /** 取得服务器上数据库结果集 */
			jsonArray = jsonObject.getJSONArray("list");

			// for (int j = 0; j < jsonArray.length(); j++) {
			// JSONObject jsonobj = jsonArray.getJSONObject(j);
			// // Object obj = jsonobj.get("accountName");
			// // arylist.add(obj.toString());
			// arylist.add(jsonobj);
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonArray;
	}

	/**
	 * 添加menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_add: {
			Intent intent = new Intent();
			intent.setClass(MyListActivity.this, FormActivity.class);
			int requestCode = 1;
			startActivityForResult(intent, requestCode);
			return true;
		}
			// case R.id.menu_search: {
			// Intent intent = new Intent();
			// intent.setClass(MyListActivity.this, Grid2.class);
			// int requestCode = 1;
			// startActivityForResult(intent, requestCode);
			// return true;
			// }
		case R.id.menu_calendar: {
			Intent intent = new Intent();
			intent.setClass(MyListActivity.this, CalendarActivity.class);
			int requestCode = 1;
			startActivityForResult(intent, requestCode);
			return true;
		}
		case R.id.menu_calendar_list: {
			Intent intent = new Intent();
			intent.setClass(MyListActivity.this, CalendarListActivity.class);
			int requestCode = 1;
			startActivityForResult(intent, requestCode);
			return true;
		}
		case R.id.account_sync: {
//			String startTime = getStartTime();

			new LoginService().login(null, null);
			List<AccountEntity> accountEntity = loadActiviyFromDB(STARTTIME);
			AccountService service = new AccountService();
			JSONObject jsonObj = service.sync(STARTTIME, accountEntity);
			JSONArray insert = null;
			JSONArray update = null;
			try {
				insert = jsonObj.getJSONObject("synClientDatas").getJSONArray(
						"insertAccountList");
				update = jsonObj.getJSONObject("synClientDatas").getJSONArray(
						"updateAccountList");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			deleteMobileDB(STARTTIME);
			storeServerToDB(insert);
			STARTTIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			if (update != null) {
				updateServerToDB(update);
			}

			
			this.fillData();
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.context_menu_edit:
			long id = info.id;
			return true;
		case R.id.context_menu_delete:
			id = info.id;
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	private List<AccountEntity> loadActiviyFromDB(String startTime) {
		mDbHelper.open();
		List<AccountEntity> list = new ArrayList<AccountEntity>();
		Cursor cursor = mDbHelper.fetchAll(DbAdapter.KEY_UPDATETIME + " > '" + STARTTIME+" '");
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			AccountEntity entity = new AccountEntity();
			entity.setAccountId(cursor.getInt(13));
			entity.setAccountName(cursor.getString(2));
			entity.setContactName(cursor.getString(1));
			entity.setCreateTime(cursor.getString(11));
			entity.setIsDel(cursor.getInt(10));
			entity.setOrdId(cursor.getInt(12));
			entity.setOwnerUserId(cursor.getInt(8));
			entity.setUpdateTime(cursor.getString(9));
			entity.setWebsite(cursor.getString(6));
			list.add(entity);
			cursor.moveToNext();
		}
		mDbHelper.close();

		return list;
	}

	

	private void deleteMobileDB(String startTime) {
		mDbHelper.open();
		String where = String.format("%s >= %s ", DbAdapter.KEY_UPDATETIME, "'"
				+ startTime + "'");
		mDbHelper.deleteWithWhere(where);
		mDbHelper.close();
	}

	private void storeServerToDB(JSONArray activities) {
		try {
			mDbHelper.open();
			for (int j = 0; j < activities.length(); j++) {
				JSONObject jsonobj = activities.getJSONObject(j);
				AccountEntity entity = new AccountEntity();
				entity.setContactName(jsonobj.getString("contactName"));
				entity.setAccountName(jsonobj.getString("accountName"));
				entity.setAccountId(jsonobj.getInt("accountId"));
				entity.setCreateTime(jsonobj.getString("createTime"));
				entity.setIsDel(jsonobj.getInt("isDel"));
				entity.setOrdId(jsonobj.getInt("orgId"));
				entity.setOwnerUserId(jsonobj.getInt("ownerUserId"));
				entity.setUpdateTime(jsonobj.getString("updateTime"));
				entity.setWebsite(jsonobj.getString("website"));
				mDbHelper.insert(entity.getContactName(), entity
						.getAccountName(), entity.getNote(), entity.getPhone(),
						entity.getMobile(), entity.getWebsite(), entity
								.getEmail(), entity.getOwnerUserId(), entity
								.getUpdateTime(), entity.getIsDel(), entity
								.getCreateTime(), entity.getOrdId(), entity
								.getAccountId());
			}
			mDbHelper.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void updateServerToDB(JSONArray activities) {
		try {
			mDbHelper.open();
			for (int j = 0; j < activities.length(); j++) {
				JSONObject jsonobj = activities.getJSONObject(j);
				AccountEntity entity = new AccountEntity();
				entity.setContactName(jsonobj.getString("contactName"));
				entity.setAccountName(jsonobj.getString("accountName"));
				entity.setAccountId(jsonobj.getInt("accountId"));
				entity.setCreateTime(jsonobj.getString("createTime"));
				entity.setIsDel(jsonobj.getInt("isDel"));
				entity.setOrdId(jsonobj.getInt("orgId"));
				entity.setOwnerUserId(jsonobj.getInt("ownerUserId"));
				entity.setUpdateTime(jsonobj.getString("updateTime"));
				entity.setWebsite(jsonobj.getString("website"));
				mDbHelper.update_2(entity.getContactName(), entity.getAccountName(), entity
						.getNote(), entity.getPhone(), entity.getMobile(),
						entity.getWebsite(), entity.getEmail(), entity
								.getOwnerUserId(), entity.getUpdateTime(),
						entity.getIsDel(), entity.getCreateTime(), entity
								.getOrdId(), entity.getAccountId());
			}
			mDbHelper.close();
		} catch (JSONException e) {
		}
	}
	
	private String getStartTime(){
		String startTime = null;
		SyncmDbHelper.open();
		Cursor cursor = SyncmDbHelper.fetchAll(null);
		int rows = cursor.getCount();
		if(rows > 0){
			startTime = cursor.getString(1);
		}else{
			SyncmDbHelper.insert("1970-03-22 00:00:00", 1, 1);
		}
		
		SyncmDbHelper.close();
		return startTime;
	}

}