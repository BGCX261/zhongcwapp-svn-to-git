package com.zhuying.android.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuying.android.R;
import com.zhuying.android.adapter.GroupAdapter;
import com.zhuying.android.adapter.TaskListAdapter;
import com.zhuying.android.async.Result;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.service.CommonSyncService;
import com.zhuying.android.service.NoticeSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.StringUtil;

/**
 * 计划任务列表
 */
public class TaskListActivity extends Activity implements IXListViewListener {
//	public PullToRefreshListView listView;
	private XListView mListView;
	public TextView title;
	public ImageView arrowView;
	
	List list = new ArrayList();
	List<String> listTag = new ArrayList<String>();
	
	private String syncTime = "最后同步：";
	private SharedPreferences sharedPrefs;
	String pref_syncTime;
	
	//popup window
	private PopupWindow popupWindow;
	private ListView lv_group;
	private View view;
	private List<String> groups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.task_list);
		
		sharedPrefs = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		pref_syncTime = sharedPrefs.getString(Constants.PREF_TASK_SYNCTIME, "2010-10-01 00:00:00");
		
		
		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(pref_syncTime);
		
		View emptyView = findViewById(R.id.empty_list_view);
		mListView.setEmptyView(emptyView);
		
		initUI();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		doList("all");
		
		getSysNotice();
	}

	private void initUI() {
		arrowView = (ImageView) findViewById(R.id.task_down_icon);
		arrowView.setBackgroundResource(R.drawable.task_down_arrow);
		arrowView.setVisibility(View.VISIBLE);
		arrowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopupWindow(v);
			}
		});
		
		title = (TextView) findViewById(R.id.header_title);
		title.setTextSize(16);
		title.setText("计划任务(全部)");
		title.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				FloatWindow.getInstance(TaskListActivity.this).createFloatWindow();
				showPopupWindow(v);
			}
		});
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setVisibility(View.VISIBLE);
		right.setText("新增");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_INSERT);
				i.setData(TaskEntity.CONTENT_URI);
				i.putExtra("subjectid", ""); //关联对象id
				i.putExtra("subjecttype", "");
				i.putExtra("subjectname", ""); //关联对象名称
				startActivity(i);
			}
		});
	}
	
	/**
	 * @param str 分类类型
	 */
	public void doList(String str) {
//		listView = (PullToRefreshListView) getListView();
//		listView.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new GetDataTask().execute();
//            }
//        });
//		listView.setLastUpdated(syncTime + DateTimeUtil.format(new Date()));
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Map item = (Map)mListView.getAdapter().getItem(position);
				String name = (String) item.get("body");
				String taskid = (String) item.get("taskid");
				String subjectid = (String) item.get("subjectid");
				String subjecttype = (String) item.get("subjecttype");
				String subjectname = (String) item.get("subjectname");
				//============
//				String rid="";
//				String subjectid="";
//				String subjecttype="";
//				String subjectname="";
//				Cursor c = managedQuery(PlanEntity.CONTENT_URI, null,"_id = "+id, null, null);
//				while (c.moveToFirst()) {
//					rid = c.getString(18);
//					subjectid = c.getString(2);
//					subjecttype = c.getString(3);
//					subjectname = c.getString(4);
//					break;
//				}
//				c.close();
				
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.setData(TaskEntity.CONTENT_URI);
				i.setType("vnd.android.cursor.item/vnd.zhuying.plans");
//				i.putExtra("id", rid); //主键（taskId）
				i.putExtra("id", taskid); //主键（taskId）
				i.putExtra("subjectid", subjectid); //关联对象id
				i.putExtra("subjecttype", subjecttype); //关联对象类型
				i.putExtra("subjectname", subjectname); //关联对象名称
				i.putExtra("from", "task_list");
				startActivity(i);
			}
		});
		
		String selection = null;
		String sortOrder = "dueat desc";
		String date = "";
		String today = DateTimeUtil.getToday();
		String tomorrow = DateTimeUtil.getTomorrow();
		String thieWeek = DateTimeUtil.getCurrentWeekday();
		String nextWeek = DateTimeUtil.getNextSunday();
		
		if("all".equals(str)){
/*//			list.removeAll(list);
			list.clear();
			List today_list = new ArrayList();
			List finish_list = new ArrayList();
			//today
			selection = "status = 'unfinish' and dueat > '"+today + " 00:00:00"+"' and dueat < '"+today+ " 24:00:00"+"'";
			Cursor today_cursor = managedQuery(PlanEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (today_cursor.moveToFirst(); !today_cursor.isAfterLast(); today_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", today_cursor.getString(1));
				map.put("tag", "today");
				map.put("taskid", today_cursor.getString(18));
				map.put("subjectid", today_cursor.getString(2));
				map.put("subjecttype", today_cursor.getString(3));
				map.put("subjectname", today_cursor.getString(4));
				today_list.add(map);
			}
			//finish
			selection = "status = 'finish'";
			Cursor finish_cursor = managedQuery(PlanEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (finish_cursor.moveToFirst(); !finish_cursor.isAfterLast(); finish_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", finish_cursor.getString(1));
				map.put("tag", "finish");
				map.put("taskid", finish_cursor.getString(18));
				map.put("subjectid", finish_cursor.getString(2));
				map.put("subjecttype", finish_cursor.getString(3));
				map.put("subjectname", finish_cursor.getString(4));
				finish_list.add(map);
			}
			
			list.addAll(today_list);
			list.addAll(finish_list);*/
			
			//outdate
			list.clear();
			
			List outdate_list = new ArrayList();
			selection = "status = 'unfinish' and dueat < '" + today + " 00:00:00" + "'";
			Cursor outdate_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (outdate_cursor.moveToFirst(); !outdate_cursor.isAfterLast(); outdate_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", outdate_cursor.getString(1));
				map.put("tag", "已过期");
				map.put("taskid", outdate_cursor.getString(18));
				map.put("subjectid", outdate_cursor.getString(2));
				map.put("subjecttype", outdate_cursor.getString(3));
				map.put("subjectname", outdate_cursor.getString(4));
				map.put("owner", outdate_cursor.getString(10));
				map.put("taskTypeId", outdate_cursor.getString(5));
				map.put("type", outdate_cursor.getString(6));
				outdate_list.add(map);
			}
			list.addAll(outdate_list);
		
			//today
			List today_list = new ArrayList();
			selection = "status = 'unfinish' and dueat >= '"+today + " 00:00:00"+"' and dueat < '"+today+ " 24:00:00"+"'";
			Cursor today_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (today_cursor.moveToFirst(); !today_cursor.isAfterLast(); today_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", today_cursor.getString(1));
				map.put("tag", "今天");
				map.put("taskid", today_cursor.getString(18));
				map.put("subjectid", today_cursor.getString(2));
				map.put("subjecttype", today_cursor.getString(3));
				map.put("subjectname", today_cursor.getString(4));
				map.put("owner", today_cursor.getString(10));
				map.put("taskTypeId", today_cursor.getString(5));
				map.put("type", today_cursor.getString(6));
				today_list.add(map);
			}
			list.addAll(today_list);
			
			//tomorrow
			List tomorrow_list = new ArrayList();
			selection = "status = 'unfinish' and dueat >= '"+tomorrow + " 00:00:00"+"' and dueat < '"+tomorrow+ " 24:00:00"+"'";
			Cursor tomorrow_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (tomorrow_cursor.moveToFirst(); !tomorrow_cursor.isAfterLast(); tomorrow_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", tomorrow_cursor.getString(1));
				map.put("tag", "明天");
				map.put("taskid", tomorrow_cursor.getString(18));
				map.put("subjectid", tomorrow_cursor.getString(2));
				map.put("subjecttype", tomorrow_cursor.getString(3));
				map.put("subjectname", tomorrow_cursor.getString(4));
				map.put("owner", tomorrow_cursor.getString(10));
				map.put("taskTypeId", tomorrow_cursor.getString(5));
				map.put("type", tomorrow_cursor.getString(6));
				tomorrow_list.add(map);
			}
			list.addAll(tomorrow_list);
			
			//thisweek
			List thisweek_list = new ArrayList();
			selection = "status = 'unfinish' and dueat > '"+tomorrow + " 24:00:00"+"' and dueat <= '"+thieWeek+ " 00:00:00"+"'";
			Cursor thisweek_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (thisweek_cursor.moveToFirst(); !thisweek_cursor.isAfterLast(); thisweek_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", thisweek_cursor.getString(1));
				map.put("tag", "本周");
				map.put("taskid", thisweek_cursor.getString(18));
				map.put("subjectid", thisweek_cursor.getString(2));
				map.put("subjecttype", thisweek_cursor.getString(3));
				map.put("subjectname", thisweek_cursor.getString(4));
				map.put("owner", thisweek_cursor.getString(10));
				map.put("taskTypeId", thisweek_cursor.getString(5));
				map.put("type", thisweek_cursor.getString(6));
				thisweek_list.add(map);
			}
			list.addAll(thisweek_list);
		
			//next
			List nextweek_list = new ArrayList();
			selection = "status = 'unfinish' and dueat > '"+thieWeek + " 24:00:00"+"' and dueat <= '"+nextWeek+ " 00:00:00"+"'";
			Cursor nextweek_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (nextweek_cursor.moveToFirst(); !nextweek_cursor.isAfterLast(); nextweek_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", nextweek_cursor.getString(1));
				map.put("tag", "下周");
				map.put("taskid", nextweek_cursor.getString(18));
				map.put("subjectid", nextweek_cursor.getString(2));
				map.put("subjecttype", nextweek_cursor.getString(3));
				map.put("subjectname", nextweek_cursor.getString(4));
				map.put("owner", nextweek_cursor.getString(10));
				map.put("taskTypeId", nextweek_cursor.getString(5));
				map.put("type", nextweek_cursor.getString(6));
				nextweek_list.add(map);
			}
			list.addAll(nextweek_list);
			
			//specify
			List specify_list = new ArrayList();
			selection = "status = 'unfinish' and dueat > '"+nextWeek + " 24:00:00"+"'";
			Cursor specify_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (specify_cursor.moveToFirst(); !specify_cursor.isAfterLast(); specify_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", specify_cursor.getString(1));
				map.put("tag", "更迟的");
				map.put("taskid", specify_cursor.getString(18));
				map.put("subjectid", specify_cursor.getString(2));
				map.put("subjecttype", specify_cursor.getString(3));
				map.put("subjectname", specify_cursor.getString(4));
				map.put("owner", specify_cursor.getString(10));
				map.put("taskTypeId", specify_cursor.getString(5));
				map.put("type", specify_cursor.getString(6));
				specify_list.add(map);
			}
			list.addAll(specify_list);
		
			//finish
			date = DateTimeUtil.format_ymd(new Date());
			sortOrder = "finishat desc";
			List finish_list = new ArrayList();
			selection = "status = 'finish'";
			Cursor finish_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (finish_cursor.moveToFirst(); !finish_cursor.isAfterLast(); finish_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", finish_cursor.getString(1));
				map.put("tag", "已完成");
				map.put("taskid", finish_cursor.getString(18));
				map.put("subjectid", finish_cursor.getString(2));
				map.put("subjecttype", finish_cursor.getString(3));
				map.put("subjectname", finish_cursor.getString(4));
				map.put("owner", finish_cursor.getString(10));
				map.put("taskTypeId", finish_cursor.getString(5));
				map.put("type", finish_cursor.getString(6));
				finish_list.add(map);
			}
			list.addAll(finish_list);
			
		}else if("outdate".equals(str)){
			list.clear();
			List outdate_list = new ArrayList();
			selection = "status = 'unfinish' and dueat < '" + today + " 00:00:00" + "'";
			Cursor outdate_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (outdate_cursor.moveToFirst(); !outdate_cursor.isAfterLast(); outdate_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", outdate_cursor.getString(1));
				map.put("tag", "已过期");
				map.put("taskid", outdate_cursor.getString(18));
				map.put("subjectid", outdate_cursor.getString(2));
				map.put("subjecttype", outdate_cursor.getString(3));
				map.put("subjectname", outdate_cursor.getString(4));
				map.put("owner", outdate_cursor.getString(10));
				map.put("taskTypeId", outdate_cursor.getString(5));
				map.put("type", outdate_cursor.getString(6));
				outdate_list.add(map);
			}
			list.addAll(outdate_list);
		}else if("today".equals(str)){
			list.clear();
			List today_list = new ArrayList();
			selection = "status = 'unfinish' and dueat >= '"+today + " 00:00:00"+"' and dueat < '"+today+ " 24:00:00"+"'";
			Cursor today_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (today_cursor.moveToFirst(); !today_cursor.isAfterLast(); today_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", today_cursor.getString(1));
				map.put("tag", "今天");
				map.put("taskid", today_cursor.getString(18));
				map.put("subjectid", today_cursor.getString(2));
				map.put("subjecttype", today_cursor.getString(3));
				map.put("subjectname", today_cursor.getString(4));
				map.put("owner", today_cursor.getString(10));
				map.put("taskTypeId", today_cursor.getString(5));
				map.put("type", today_cursor.getString(6));
				today_list.add(map);
			}
			list.addAll(today_list);
		}else if("tomorrow".equals(str)){
			list.clear();
			List tomorrow_list = new ArrayList();
			selection = "status = 'unfinish' and dueat >= '"+tomorrow + " 00:00:00"+"' and dueat < '"+tomorrow+ " 24:00:00"+"'";
			Cursor tomorrow_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (tomorrow_cursor.moveToFirst(); !tomorrow_cursor.isAfterLast(); tomorrow_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", tomorrow_cursor.getString(1));
				map.put("tag", "明天");
				map.put("taskid", tomorrow_cursor.getString(18));
				map.put("subjectid", tomorrow_cursor.getString(2));
				map.put("subjecttype", tomorrow_cursor.getString(3));
				map.put("subjectname", tomorrow_cursor.getString(4));
				map.put("owner", tomorrow_cursor.getString(10));
				map.put("taskTypeId", tomorrow_cursor.getString(5));
				map.put("type", tomorrow_cursor.getString(6));
				tomorrow_list.add(map);
			}
			list.addAll(tomorrow_list);
		}else if("thisweek".equals(str)){
			list.clear();
			List thisweek_list = new ArrayList();
			selection = "status = 'unfinish' and dueat > '"+tomorrow + " 24:00:00"+"' and dueat <= '"+thieWeek+ " 00:00:00"+"'";
			Cursor thisweek_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (thisweek_cursor.moveToFirst(); !thisweek_cursor.isAfterLast(); thisweek_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", thisweek_cursor.getString(1));
				map.put("tag", "本周");
				map.put("taskid", thisweek_cursor.getString(18));
				map.put("subjectid", thisweek_cursor.getString(2));
				map.put("subjecttype", thisweek_cursor.getString(3));
				map.put("subjectname", thisweek_cursor.getString(4));
				map.put("owner", thisweek_cursor.getString(10));
				map.put("taskTypeId", thisweek_cursor.getString(5));
				map.put("type", thisweek_cursor.getString(6));
				thisweek_list.add(map);
			}
			list.addAll(thisweek_list);
		}else if("nextweek".equals(str)){
			list.clear();
			List nextweek_list = new ArrayList();
			selection = "status = 'unfinish' and dueat > '"+thieWeek + " 24:00:00"+"' and dueat <= '"+nextWeek+ " 00:00:00"+"'";
			Cursor nextweek_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (nextweek_cursor.moveToFirst(); !nextweek_cursor.isAfterLast(); nextweek_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", nextweek_cursor.getString(1));
				map.put("tag", "下周");
				map.put("taskid", nextweek_cursor.getString(18));
				map.put("subjectid", nextweek_cursor.getString(2));
				map.put("subjecttype", nextweek_cursor.getString(3));
				map.put("subjectname", nextweek_cursor.getString(4));
				map.put("owner", nextweek_cursor.getString(10));
				map.put("taskTypeId", nextweek_cursor.getString(5));
				map.put("type", nextweek_cursor.getString(6));
				nextweek_list.add(map);
			}
			list.addAll(nextweek_list);
		}else if("specify".equals(str)){
			list.clear();
			List specify_list = new ArrayList();
			selection = "status = 'unfinish' and dueat > '"+nextWeek + " 24:00:00"+"'";
			Cursor specify_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (specify_cursor.moveToFirst(); !specify_cursor.isAfterLast(); specify_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", specify_cursor.getString(1));
				map.put("tag", "更迟的");
				map.put("taskid", specify_cursor.getString(18));
				map.put("subjectid", specify_cursor.getString(2));
				map.put("subjecttype", specify_cursor.getString(3));
				map.put("subjectname", specify_cursor.getString(4));
				map.put("owner", specify_cursor.getString(10));
				map.put("taskTypeId", specify_cursor.getString(5));
				map.put("type", specify_cursor.getString(6));
				specify_list.add(map);
			}
			list.addAll(specify_list);
		}else if("finish".equals(str)){
			date = DateTimeUtil.format_ymd(new Date());
			sortOrder = "finishat desc";

			list.clear();
			List finish_list = new ArrayList();
			selection = "status = 'finish'";
			Cursor finish_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, sortOrder);
			for (finish_cursor.moveToFirst(); !finish_cursor.isAfterLast(); finish_cursor
					.moveToNext()) {
				Map map = new HashMap();
				map.put("body", finish_cursor.getString(1));
				map.put("tag", "已完成");
				map.put("taskid", finish_cursor.getString(18));
				map.put("subjectid", finish_cursor.getString(2));
				map.put("subjecttype", finish_cursor.getString(3));
				map.put("subjectname", finish_cursor.getString(4));
				map.put("owner", finish_cursor.getString(10));
				map.put("taskTypeId", finish_cursor.getString(5));
				map.put("type", finish_cursor.getString(6));
				finish_list.add(map);
			}
			list.addAll(finish_list);
		}
//		Cursor cursor = managedQuery(PlanEntity.CONTENT_URI, null,selection, null, sortOrder);
//		String[] from = { PlanEntity.KEY_BODY };
//		int[] to = { R.id.name };
//		
//		
//			// 遍历Cursor，将记录存储到List
//		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//				.moveToNext()) { // 遍历
//			PlanEntity entity = new PlanEntity(cursor);
//			Map map = new HashMap();
//			map.put("body", entity.getBody());
//			list.add(map);
//		}
		
		String[] from = { TaskEntity.KEY_BODY };
		int[] to = { R.id.name };
		TaskListAdapter a = new TaskListAdapter(this,
				R.layout.task_list_item, list,from,to,listTag);
//		setListAdapter(a);
		mListView.setAdapter(a);
	}
	
//	private String getGroup(String dueat) {
//		String v = "";
//
//		String today = DateTimeUtil.getToday();
//		String str = dueat.substring(0, 10);
//
//		int value = str.compareTo(today);
//		switch (value) {
//		case -1:
//			v = "已过期";
//			break;
//		case 0:
//			v = "今天";
//			break;
//		case 1:
//			v = "更迟";
//			break;
//		default:
//			break;
//		}
//		return v;
//}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			quitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void quitDialog() {
		AlertDialog ad = new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("确认退出")
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface Dialog,
									int whichButton) {
								// android.os.Process.killProcess(android.os.Process.myPid());
								forceStopPackage("com.zhuying.android");
							}
						}).create();
		ad.show();
	}
	
	private void forceStopPackage(String pkgName) {
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		FloatWindow.getInstance(TaskListActivity.this).removeFloatWindow();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	 private class GetDataTask extends AsyncTask<Void, Void, String[]> {
	        @Override
	        protected String[] doInBackground(Void... params) {
	        	CommonSyncService common = new CommonSyncService(getApplicationContext());
				Result result =  common.sync();
				return null;
	        }

	        @Override
	        protected void onPostExecute(String[] result) {
	        	onLoad();
	        	
	        	//添加刷新列表
	        	doList("all");
	            
	            super.onPostExecute(result);
	        }
	    }


	@Override
	public void onRefresh() {
		if(!NetworkStateUtil.checkNetworkInfo(TaskListActivity.this)){
			mListView.stopRefresh();
		}else{
			new GetDataTask().execute();
		}
	}

	@Override
	public void onLoadMore() {
		
	}
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.setRefreshTime(DateTimeUtil.format(new Date()));
	}
	
	
	/**
	 * Popup Window
	 * 
	 * @param parent
	 */
	private void showPopupWindow(View parent) {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.group_list, null);

			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			groups = new ArrayList<String>();
			groups.add("全部");
			groups.add("已过期");
			groups.add("今天");
			groups.add("明天");
			groups.add("本周");
			groups.add("下周");
			groups.add("更迟的");
			groups.add("已完成");

			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(view, 280, LayoutParams.WRAP_CONTENT);
		}
		
		arrowView.setBackgroundResource(R.drawable.task_up_arrow);
		
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				arrowView.setBackgroundResource(R.drawable.task_down_arrow);
			}
		});
		
		/*WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半 - PopupWindow的高度的一半
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;
		
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int popwinWifth = popupWindow.getWidth();*/

//		popupWindow.showAsDropDown(parent, xPos, 0);
//		popupWindow.showAtLocation(parent, Gravity.CENTER_HORIZONTAL, 0, 0);
		
		//得到mView在屏幕中的坐标  
		int [] pos = new int[2];  
		parent.getLocationOnScreen(pos);  
		int offsetY = pos[1] + parent.getHeight();  
		int offsetX = 0;  
		          
		//显示mPopupWindow  
		popupWindow.showAtLocation(parent, Gravity.TOP|Gravity.CENTER_HORIZONTAL, offsetX, offsetY);  

		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				if (popupWindow != null) {
					popupWindow.dismiss();
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
				}
				
				switch (position) {
				case 0:
					doList("all");
					title.setText("计划任务(全部)");
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
					break;
				case 1:
					doList("outdate");
					title.setText("计划任务(过期)");
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
					break;
				case 2:
					doList("today");
					title.setText("计划任务(今天)");
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
					break;
				case 3:
					doList("tomorrow");
					title.setText("计划任务(明天)");
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
					break;
				case 4:
					doList("thisweek");
					title.setText("计划任务(本周)");
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
					break;
				case 5:
					doList("nextweek");
					title.setText("计划任务(下周)");
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
					break;
				case 6:
					doList("specify");
					title.setText("计划任务(更迟)");
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
					break;
				case 7:
					doList("finish");
					title.setText("计划任务(完成)");
					arrowView.setBackgroundResource(R.drawable.task_down_arrow);
					break;
				default:
					break;
				}
			}
		});
	}
	
	
	private void getSysNotice() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String ticketId = sharedPrefs.getString(Constants.PREF_TICKETID, null);
				NoticeSyncService service = new NoticeSyncService(TaskListActivity.this);
				String r = service.syncNotice(ticketId);
				return r;
			}
			
			protected void onPostExecute(String result) {
				if(!StringUtil.isEmpty(result)){
					showNoticeDialog(TaskListActivity.this,result);
				}
			};
		}.execute();
	
	}
	
	/**
	 * 通知对话框
	 * @param ctx
	 */
	private void showNoticeDialog(Context ctx,String content){
		new AlertDialog.Builder(ctx).setTitle("提示").setMessage("系统通知：\n"+content)
				.setCancelable(false).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
							}
						}).create().show();
	}
}
