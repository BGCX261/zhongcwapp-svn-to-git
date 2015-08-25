package com.zhongcw.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhongcw.android.entity.ActivityEntity;
import com.zhongcw.android.service.ActivityService;
import com.zhongcw.android.service.LoginService;
import com.zhongcw.android.util.DateTime;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class CalendarActivity extends Activity {
	private static final String TAG = "CalendarActivity";

	private CalendarView view;
	private DbAdapterCalendar dbAdapterCalendar  = new DbAdapterCalendar(CalendarActivity.this);;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new CalendarView(this);
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.calendar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.calendar_add: {
			Intent intent = new Intent();
			intent.setClass(CalendarActivity.this, CalendarFormActivity.class);
			int requestCode = 1;
			startActivityForResult(intent, requestCode);
			return true;
		}
		case R.id.calendar_sync: {
			new LoginService().login(null, null);
			String startTime = DateTime.getDateStrDiff(-7);
			String endTime = DateTime.getDateStrDiff(7);
			List<ActivityEntity> newlyAddedActivities = loadActiviyFromDB(
					startTime, endTime);
			ActivityService service = new ActivityService();
			JSONArray activities = service.sync(startTime, endTime,
					newlyAddedActivities);
			deleteMobileDB(startTime, endTime);
			storeServerToDB(activities);
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void deleteMobileDB(String startTime, String endTime) {
			dbAdapterCalendar.open();
			String where = String.format("%s > %s and %s < %s",
					DbAdapterCalendar.KEY_STARTTIME, "'"+startTime+"'",
					DbAdapterCalendar.KEY_ENDTIME, "'"+endTime+"'");
			dbAdapterCalendar.deleteWithWhere(where);
			dbAdapterCalendar.close();
	}
	
	private void storeServerToDB(JSONArray activities) {
		try {
			dbAdapterCalendar.open();
			for (int j = 0; j < activities.length(); j++) {
				JSONObject jsonobj = activities.getJSONObject(j);
				ActivityEntity entity = new ActivityEntity();
				entity.setActvtId(jsonobj.getString("actvtId"));
				entity.setSubject(jsonobj.getString("subject"));
				entity.setStartTime(jsonobj.getString("startTime"));
				entity.setEndTime(jsonobj.getString("endTime"));
				dbAdapterCalendar.insert(entity.getRepeatType(), entity.getAccountId(), entity.getActvtType(), entity.getOwnerUserId(), entity.getActvtState(), entity.getActvtEffe(), entity.getEffeAccountPhase(), entity.getStartTime(), entity.getEndTime(), entity.getSubject(), entity.getContactName(), entity.getNote(), entity.getActvtId());
			}
			dbAdapterCalendar.close();
		} catch (JSONException e) {
		}
	}

	private List<ActivityEntity> loadActiviyFromDB(String startTime,
			String endTime) {
		List<ActivityEntity> list = new ArrayList<ActivityEntity>();
		dbAdapterCalendar.open();
		String where = String.format("%s > %s and %s < %s and %s is null",
				DbAdapterCalendar.KEY_STARTTIME, "'"+startTime+"'",
				DbAdapterCalendar.KEY_ENDTIME, "'"+endTime+"'",
				DbAdapterCalendar.KEY_ACTVTID);
		Cursor cursor = dbAdapterCalendar.fetchAll(where);
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			ActivityEntity entity = new ActivityEntity();
			entity.setStartTime(cursor.getString(8));
			entity.setEndTime(cursor.getString(9));
			entity.setSubject(cursor.getString(10));
			entity.setNote(cursor.getString(12));
			list.add(entity);
			cursor.moveToNext();
		}
		dbAdapterCalendar.close();
		return list;
	}

	public void queryEvent(String date) {
		Intent intent = new Intent();
		intent.setClass(CalendarActivity.this, CalendarListActivity.class);

		Bundle bundle = new Bundle();
		bundle.putString("startTime", date);
		intent.putExtras(bundle);

		int requestCode = 1;
		startActivityForResult(intent, requestCode);
		// selection
	}

	// 获取yyyy-MM-dd是星期几。周日为0，周一为1
	public static int getWeekdayOfDateTime(String datetime) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(df.parse(datetime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
		return weekday;
	}

	static int getNumberOfDaysInMonth(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			return isLeapYear(year) ? 29 : 28;
		default:
			return 0;

		}
	}

	static boolean isLeapYear(int year) {
		return year % 400 == 0 || (year % 4 == 0 && year % 400 != 0);
	}

}
