package com.zhuying.android.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.dao.DatabaseHelper;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.service.ActionSyncService;
import com.zhuying.android.service.TaskSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.StringUtil;
import com.zhuying.android.util.UUIDUtil;

/**
 * 计划任务明细
 */
public class TaskDetailActivity extends Activity {
	private String subjectid; //关联对象id
	private String subjecttype; //关联对象类型
	private String subjectname; //关联对象类型
	
	private TextView bodyTextView;
	private TextView dateTextView;
	private TextView typeTextView;
	private Button done;
	
	private TaskEntity entity;
	private Cursor mCursor;
	private Uri uri;
	private String id; //主键
	private String from;
	
	private ActionEntity action = new ActionEntity();
	
	private RelativeLayout subjectLayout; //关联对象布局
	
	private String status;
	private LinearLayout moveto_layout;
	private TextView todayView;
	private TextView tomorrowView;
	private TextView thisweekView;
	private TextView nextweekView;
	private TextView specifyView;
	private ImageView rightArrow;
	
	private TextView moveView;
	private Button right;
	
	private SharedPreferences sharedPrefs;
	String ownerName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.task_detail);
		
		sharedPrefs = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		ownerName = sharedPrefs.getString(Constants.PREF_USERNAME, "");
		
		initUI();
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		subjectid = b.get("subjectid").toString();
		subjecttype = b.get("subjecttype").toString();
		subjectname = b.get("subjectname").toString();
		id = b.get("id").toString();
		from = b.get("from").toString();
		
		mCursor = managedQuery(TaskEntity.CONTENT_URI, null, TaskEntity.KEY_TASKID+" = '"+id+"'", null, null);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mCursor != null){
			mCursor.moveToFirst();
			entity = new TaskEntity(mCursor);
			status = entity.getStatus();
			if("finish".equals(status)){
				done.setVisibility(View.GONE);
				moveto_layout.setVisibility(View.GONE);
				moveView.setVisibility(View.GONE);
				right.setVisibility(View.GONE);
			}else{
				
			}
			bodyTextView.setText(entity.getBody());
//			String createAt = entity.getCreateAt();
			String dueat = entity.getDueat();
			dateTextView.setText(dueat.subSequence(0, 10));
			
			String type = entity.getTaskType();
			Cursor c = managedQuery(CategoryEntity.CONTENT_URI, null,"categoryid ='"+entity.getTaskTypeId()+"'", null, null);
			while (c.moveToFirst()) {
				String categoryColor = c.getString(4);
				typeTextView.setBackgroundColor(Color.parseColor(categoryColor));
				break;
			}
			c.close();
			typeTextView.setText(type);
			
			if(StringUtil.isEmpty(subjectid)){
				rightArrow.setVisibility(View.GONE);
			}else{
				if(subjecttype.equals("contact") || subjecttype.equals("company")){
					rightArrow.setVisibility(View.VISIBLE);
				}else{
					rightArrow.setVisibility(View.GONE);	
				}
			}
		}
	}

	private void initUI() {
		//init Header
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("计划任务");
		right = (Button) findViewById(R.id.header_right_btn);
		right.setText("修改");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_EDIT);
				i.setData(TaskEntity.CONTENT_URI);
				i.setType("vnd.android.cursor.item/vnd.zhuying.plans");
				i.putExtra("id", id);
				i.putExtra("subjectid", subjectid);
				i.putExtra("subjecttype", subjecttype);
				i.putExtra("subjectname", subjectname);
				startActivity(i);
			}
		});
		Button left = (Button) findViewById(R.id.header_left_btn);
		left.setText("返回");
		left.setVisibility(View.VISIBLE);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		//初始化界面
		rightArrow = (ImageView) findViewById(R.id.record_arrow);
		
		bodyTextView = (TextView) findViewById(R.id.plan_detail_body);
		dateTextView = (TextView) findViewById(R.id.plan_detail_date);
		typeTextView = (TextView) findViewById(R.id.plan_detail_type);
		
		done = (Button) findViewById(R.id.plan_detail_done);
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//设置最近行动表字段值
				String actionDate = DateTimeUtil.format(new Date());
				action.setLatestactivityid(UUIDUtil.getUUID());
				action.setActivitystatus("finish");
				action.setActivitytype(ActionEntity.TYPE_TASK);
				action.setActivitycontent(bodyTextView.getText().toString());
				action.setSubjectid(subjectid);
				action.setSubjecttype(subjecttype);
				action.setSubjectname(subjectname);
				action.setSubjectface("");
				action.setOwnerid("");
				action.setOwnername(ownerName);
				action.setUpdatedat(actionDate);
				action.setCreatedat(actionDate);
				action.setParentid(id);
				action.setActivitydate(actionDate);
				action.setActivitycreate("");
				
				getContentResolver().insert(ActionEntity.CONTENT_URI, action.toContentValues());
				
				//设置计划任务完成状态机完成时间
				entity.setStatus("finish");
				String date = DateTimeUtil.format(new Date());
				entity.setFinishAt(date);
				entity.setUpdateAt(date);
				getContentResolver().update(TaskEntity.CONTENT_URI, entity.toContentValues(), TaskEntity.KEY_TASKID+" = '"+id+"'", null);
				
//				finish();
				wifiAutoSync();
			}
		});
		
		moveView = (TextView) findViewById(R.id.moveto_text);
		
		moveto_layout = (LinearLayout) findViewById(R.id.moveto_layout);
		todayView = (TextView) findViewById(R.id.moveto_today);
		todayView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String duetype = "today";
				entity.setDueAtType(duetype);
				entity.setDueat(getDueDate(duetype));
				entity.setUpdateAt(DateTimeUtil.format(new Date()));
				getContentResolver().update(TaskEntity.CONTENT_URI, entity.toContentValues(), TaskEntity.KEY_TASKID+" = '"+id+"'", null);
//				finish();
				
				wifiAutoSync();
			}
		});
		tomorrowView = (TextView) findViewById(R.id.moveto_tomorrow);
		tomorrowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String duetype = "tomorrow";
				entity.setDueAtType(duetype);
				entity.setDueat(getDueDate(duetype));
				entity.setUpdateAt(DateTimeUtil.format(new Date()));
				getContentResolver().update(TaskEntity.CONTENT_URI, entity.toContentValues(), TaskEntity.KEY_TASKID+" = '"+id+"'", null);
//				finish();
				wifiAutoSync();
			}
		});
		thisweekView = (TextView) findViewById(R.id.moveto_thisweek);
		thisweekView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String duetype = "this_week";
				entity.setDueAtType(duetype);
				entity.setDueat(getDueDate(duetype));
				entity.setUpdateAt(DateTimeUtil.format(new Date()));
				getContentResolver().update(TaskEntity.CONTENT_URI, entity.toContentValues(), TaskEntity.KEY_TASKID+" = '"+id+"'", null);
//				finish();
				wifiAutoSync();
			}
		});
		nextweekView = (TextView) findViewById(R.id.moveto_nextweek);
		nextweekView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String duetype = "next_week";
				entity.setDueAtType(duetype);
				entity.setDueat(getDueDate(duetype));
				entity.setUpdateAt(DateTimeUtil.format(new Date()));
				getContentResolver().update(TaskEntity.CONTENT_URI, entity.toContentValues(), TaskEntity.KEY_TASKID+" = '"+id+"'", null);
//				finish();
				wifiAutoSync();
			}
		});
		specifyView = (TextView) findViewById(R.id.moveto_specific);
		specifyView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String duetype = "specify";
				entity.setDueAtType(duetype);
				entity.setDueat(getDueDate(duetype));
				entity.setUpdateAt(DateTimeUtil.format(new Date()));
				getContentResolver().update(TaskEntity.CONTENT_URI, entity.toContentValues(), TaskEntity.KEY_TASKID+" = '"+id+"'", null);
				wifiAutoSync();
			}
		});
		
		subjectLayout = (RelativeLayout) findViewById(R.id.task_subject_layout);
		subjectLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("task_detail".equals(from)){
					
				}else{
					Uri uri;
					if(subjecttype.equals(CompanyEntity.TYPE_CONTACT)){ //联系人
						uri = ContactEntity.CONTENT_URI;
						Intent i = new Intent();
						i.setAction(Intent.ACTION_VIEW);
						i.setData(uri);
						i.setType("vnd.android.cursor.item/vnd.zhuying.contacts");
						i.putExtra("id", subjectid); //主键
						startActivity(i);
					}else if(subjecttype.equals(CompanyEntity.TYPE_COMPANY)){ //公司
						uri = CompanyEntity.CONTENT_URI;
						Intent i = new Intent();
						i.setAction(Intent.ACTION_VIEW);
						i.setData(uri);
						i.setType("vnd.android.cursor.item/vnd.zhuying.companys");
						i.putExtra("id", subjectid); //主键
						startActivity(i);
					}
				}
			}
		});
	}
	
	/**
	 * Copy from TaskEditlActivity.java
	 * @param dueType
	 * @return
	 */
	private String getDueDate(String dueType){
		String dueDate = "";
		if("today".equals(dueType)){
			dueDate = DateTimeUtil.getToday();
		}else if("tomorrow".equals(dueType)){
			dueDate = DateTimeUtil.getTomorrow();
		}else if("this_week".equals(dueType)){
			dueDate = DateTimeUtil.getCurrentWeekday();
		}else if("next_week".equals(dueType)){
			dueDate = DateTimeUtil.getNextSunday();
		}else if("specify".equals(dueType)){
			dueDate = DateTimeUtil.getNextNextMonday();
		}else{
		}
		return dueDate + " 00:00:00";
	}
	
	/**
	 * Wifi下自动同步
	 */
	private void wifiAutoSync() {
		if (Constants.WIFI_AUTO_SYNC) { // wifi下自动同步打开
//			NetworkStateUtil net = new NetworkStateUtil(getApplicationContext());
			if (NetworkStateUtil.checkWifi(getApplicationContext())) { // wifi可用
				new WifiSyncTask().execute();
			}else{
				finish();
			}
		}else{
			finish();
		}
	}
	
	private class WifiSyncTask extends AsyncTask<Void, Void, Result> {
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			pDialog = ProgressDialog.show(TaskDetailActivity.this, "", "计划任务同步中...", true, true);
		}

		@Override
		protected Result doInBackground(Void... params) {
			TaskSyncService service = new TaskSyncService(
					getApplicationContext());
			SharedPreferences pref = getApplicationContext()
					.getSharedPreferences(Constants.PREF,
							Context.MODE_PRIVATE);
			String ticketId = pref.getString(Constants.PREF_TICKETID, null);
			Result result = service.syncTask(ticketId);
			
			ActionSyncService actionS = new ActionSyncService(
					getApplicationContext());
			result = actionS.syncAction(ticketId);
			return result;
		}

		@Override
		protected void onPostExecute(Result result) {
			pDialog.dismiss();
			
			finish();
		}
	}
	
}
