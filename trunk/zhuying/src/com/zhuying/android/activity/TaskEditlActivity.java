package com.zhuying.android.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.AuthorityEntity;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.GroupEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.service.CompanyContactSyncService;
import com.zhuying.android.service.NoteSyncService;
import com.zhuying.android.service.TaskSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.StringUtil;
import com.zhuying.android.util.UUIDUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 *计划任务新增/编辑
 */
public class TaskEditlActivity extends Activity {
	private String subjectId; //关联对象id
	private String subjectType; //关联对象类型
	private String subjectName;
	
	private String companyName = "";
	private int dateSpinnerId = 0;
	private String selectedDateSpinner = "";
	private int categorySpinnerId = 0;
	private String selectedCategorySpinner = "";
	
	private int mState; //新增/编辑标识
	private static final int STATE_EDIT = 0;
	private static final int STATE_INSERT = 1;
	
	private EditText bodyEditText;
	private TextView dateButton;
//	private Spinner dateSpinner;
	private TextView categorySpinner;
	private TextView userSpinner;
	private RelativeLayout selectedUserLayout; //我，和所选人员
	private TextView selectedUserView;
	private RelativeLayout selectedGroupLayout;
	private TextView selectedGroupView;
	
	private TaskEntity entity;
	
	private Cursor mCursor;
	private Uri uri;
	private String id; //主键
	Bundle bundle;
	
	//Dialog
	private static final int DIALOG_PICK_DATE = 1;
	private static final int DIALOG_SINGLE_CHOICE = 5;
	private static final int DIALOG_MULTIPLE_CHOICE_CURSOR = 8; //我，和所选人员
	private static final int DIALOG_MULTIPLE_CHOICE_GROUP = 9;
	
	private static final int DIALOG_CHOICE_USER = 2; //负责人
	private static final int DIALOG_CHOICE_CATEGORY = 3; //任务分类
	private int start_Year;
	private int start_Month;
	private int start_Day;
	
	private String[] province;
	private String[] ids;
	private List selectIds = new ArrayList();
	private boolean[] checkAry;
	private ListView lv;
	
	private RelativeLayout allLayout;
	private RelativeLayout myselfLayout;
	
	private ImageView all;
	private ImageView myself;
	private ImageView group;
	private ImageView user;
	
	private String realName = "";
	private String userid = "";
	
	private String visibleTo = "OWNER";
	
	private String categoryId = "";
	private String categoryType = "";
	
	private SharedPreferences sharedPrefs;
	private String userName; //当前登录人
	
	private String groupID;
	
	private String[] user_from = new String[] { UserEntity._ID,UserEntity.KEY_USERID,UserEntity.KEY_REALNAME };
	private Cursor user_cursor;
	private LinearLayout userLayout; //负责人布局
	private LinearLayout whoLayout; //谁可以看到布局
	
	private static final String TAG = "TaskEditlActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.task_edit);
		
		user_cursor = managedQuery(UserEntity.CONTENT_URI, user_from, null, null,null);
		
		initUI();

		Intent intent = getIntent();
		bundle = intent.getExtras();
		subjectId = bundle.get("subjectid").toString();
		subjectType = bundle.get("subjecttype").toString();
		subjectName = bundle.get("subjectname").toString();
		uri = intent.getData();
		String action = intent.getAction();
		
		if(Intent.ACTION_EDIT.equals(action)){
			mState = STATE_EDIT;
		}else if(Intent.ACTION_INSERT.equals(action)){
			mState = STATE_INSERT;
		}
		
		Calendar c = null;
		c = Calendar.getInstance();
		start_Year = c.get(Calendar.YEAR);
		start_Month = c.get(Calendar.MONTH); // 一月
		start_Day = c.get(Calendar.DATE); // 一日
		// set data and time
		updateDisplay();
		
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		switch (mState) {
		case STATE_EDIT:
//			id = ContentUris.parseId(uri);
			id = bundle.get("id").toString();
			mCursor = managedQuery(TaskEntity.CONTENT_URI, null, TaskEntity.KEY_TASKID + " = '" + id + "'", null, null);
			mCursor.moveToFirst();
			entity = new TaskEntity(mCursor);
			bodyEditText.setText(entity.getBody());
			
			String dueAtType = entity.getDueAtType();
//			Log.d(TAG, "dueAtType = "+dueAtType);
			if("today".equals(dueAtType)){
				dateButton.setText("今天");
			}if("tomorrow".equals(dueAtType)){
				dateButton.setText("明天");
			}if("this_week".equals(dueAtType)){
				dateButton.setText("本周");
			}if("next_week".equals(dueAtType)){
				dateButton.setText("下周");
			}if("specify".equals(dueAtType)){
				dateButton.setText(entity.getDueat());
			}
//			dateSpinner.setSelection(selectId);
			//分类
//			String taskTypeId = entity.getTaskTypeId();
//			for (int i = 0; i < categorySpinner.getCount(); i++) {
//				Cursor value = (Cursor) categorySpinner.getItemAtPosition(i);
//				String typeid = value.getString(1);
//				if (typeid.equals(taskTypeId)) {
//					categorySpinner.setSelection(i);
//					break;
//				}
//			}
			//负责人
			String ownerId = entity.getOwnerId();
//			for (int i = 0; i < userSpinner.getCount(); i++) {
//				Cursor value = (Cursor) userSpinner.getItemAtPosition(i);
//				String typeid = value.getString(1);
//				if (typeid.equals(ownerId)) {
//					userSpinner.setSelection(i);
//					break;
//				}
//			}
			
			String visible = entity.getVisibleTo();
			if("OWNER".equals(visible)){
				myself.setVisibility(View.VISIBLE);
				
				all.setVisibility(View.GONE);
				group.setVisibility(View.GONE);
				user.setVisibility(View.GONE);
			}else if("EVERYONE".equals(visible)){
				all.setVisibility(View.VISIBLE);
				
				myself.setVisibility(View.GONE);
				group.setVisibility(View.GONE);
				user.setVisibility(View.GONE);
			}else if("NAMEDGROUP".equals(visible)){
				group.setVisibility(View.VISIBLE);
				
				myself.setVisibility(View.GONE);
				all.setVisibility(View.GONE);
				user.setVisibility(View.GONE);
				
				Cursor taskUserCursor = getContentResolver().query(AuthorityEntity.CONTENT_URI, null, "dataId = '" + entity.getTaskid() + "'",null, null);
				String owners="";
				while (taskUserCursor.moveToFirst()) {
					owners = taskUserCursor.getString(3);
					break;
				}
				taskUserCursor.close();
				
				Cursor groupCursor = getContentResolver().query(GroupEntity.CONTENT_URI, null, "groupid = '" + owners + "'",null, null);
				String group="";
				while (groupCursor.moveToFirst()) {
					group = groupCursor.getString(1);
					break;
				}
				groupCursor.close();
				selectedGroupView.setText(group);
			}else if("ADHOCGROUP".equals(visible)){
				user.setVisibility(View.VISIBLE);
				
				myself.setVisibility(View.GONE);
				group.setVisibility(View.GONE);
				all.setVisibility(View.GONE);
				
				Cursor taskUserCursor = getContentResolver().query(AuthorityEntity.CONTENT_URI, null, "dataId = ? and owners <> ?",new String[]{entity.getTaskid(),userid}, null);
				String owners="";
				String user="";
				for (taskUserCursor.moveToFirst(); !taskUserCursor
						.isAfterLast(); taskUserCursor.moveToNext()) {
					owners = taskUserCursor.getString(3);
					Cursor userCursor = getContentResolver().query(UserEntity.CONTENT_URI, null, "userid = '" + owners + "'",null, null);
					while (userCursor.moveToFirst()) {
						user += userCursor.getString(4) + ",";
						break;
					}
				}
				if(!StringUtil.isEmpty(user)){
					user = user.substring(0, user.length() - 1); //去掉尾部逗号
				}
				selectedUserView.setText(user);
			}
			
			break;
		case STATE_INSERT: 
			entity = new TaskEntity();
			break;
		default:
			break;
		}
	}

	private void initUI() {
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("计划任务新增");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setText("保存");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				companyName = bodyEditText.getText().toString();
				if(TextUtils.isEmpty(companyName)){
					Toast.makeText(getApplicationContext(), "计划任务内容不能为空", Toast.LENGTH_SHORT).show();
					return; 
				}
//				Cursor categoryCursor = (Cursor) categorySpinner.getSelectedItem();
				
				entity.setSubjectType(subjectType);
				entity.setSubjectId(subjectId);
				entity.setSubjectName(subjectName);
				entity.setBody(companyName);
				String dueAtType = dateButton.getText().toString();
				String duetype;
				if("今天".equals(dueAtType)){
					duetype = "today";
				}else if("明天".equals(dueAtType)){
					duetype = "tomorrow";
				}else if("本周".equals(dueAtType)){
					duetype = "this_week";
				}else if("下周".equals(dueAtType)){
					duetype = "next_week";
				}else{
					duetype = "specific";
				}
				entity.setDueAtType(duetype);
				entity.setDueat(getDueDate(duetype));
				entity.setTaskTypeId(categoryId);
				entity.setTaskType(categoryType);
				entity.setCreateAt(DateTimeUtil.format(new Date()));
				entity.setOwnerId(userid);
				entity.setOwnerName(realName);
				if(visibleTo.equals("NAMEDGROUP")){
					if(StringUtil.isEmpty(groupID)){
						Toast.makeText(getApplicationContext(), "请选择组", Toast.LENGTH_SHORT).show();
						return; 
					}
				}else if(visibleTo.equals("ADHOCGROUP")){
					if(selectIds.isEmpty()){
						Toast.makeText(getApplicationContext(), "请选择人员", Toast.LENGTH_SHORT).show();
						return; 
					}
				}
				entity.setVisibleTo(visibleTo); //默认自己
				entity.setCreateUserId(userid);
				
				switch (mState) {
				case STATE_EDIT:
					String updateDate = DateTimeUtil.format(new Date());
					entity.setUpdateAt(updateDate);
					getContentResolver().update(TaskEntity.CONTENT_URI, entity.toContentValues(), TaskEntity.KEY_TASKID + " = '" + id + "'", null);
					
					saveTaskUser();
					wifiAutoSync();
//					finish();
					
					//更新task_user
					/*Cursor taskUserCursor = getContentResolver().query(AuthorityEntity.CONTENT_URI, null, "dataId = '" + entity.getTaskid() + "'",null, null);
					int count = taskUserCursor.getCount();
					if(count > 0){
						taskUserCursor.moveToFirst();
						AuthorityEntity ae = new AuthorityEntity(taskUserCursor);
						String visib = entity.getVisibleTo();
						if("EVERYONE".equals(visib)){
							ae.setOwners("ALL");
							getContentResolver().update(AuthorityEntity.CONTENT_URI, ae.toContentValues(), "dataId = ?", new String[]{entity.getTaskid()});
						}else if("OWNER".equals(visib)){
							ae.setOwners(entity.getOwnerId());
							getContentResolver().update(AuthorityEntity.CONTENT_URI, ae.toContentValues(), "dataId = ?", new String[]{entity.getTaskid()});
						}else if("NAMEDGROUP".equals(visib)){
							ae.setOwners(groupID);
							getContentResolver().update(AuthorityEntity.CONTENT_URI, ae.toContentValues(), "dataId = ?", new String[]{entity.getTaskid()});
						}
						
						taskUserCursor.close();
					}else{
						saveTaskUser();
					}*/
//					saveTaskUser();
					
					break;
				case STATE_INSERT: 
					entity.setTaskid(UUIDUtil.getUUID());
					String date = DateTimeUtil.format(new Date());
					entity.setCreateAt(date);
					entity.setUpdateAt(date);
					
					getContentResolver().insert(TaskEntity.CONTENT_URI, entity.toContentValues());
					
					//插入记录到task_user
					saveTaskUser();
					
					wifiAutoSync();
//					finish();
					
					break;
				default:
					break;
				}
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
		
		//初始化界面元素
		bodyEditText =  (EditText) findViewById(R.id.plan_edit_body);
		
//		dateSpinner = (Spinner) findViewById(R.id.plan_date_spinner);
		dateButton = (TextView) findViewById(R.id.plan_date);
		dateButton.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                showDialog(DIALOG_SINGLE_CHOICE);
	            }
	        });
		dateButton.setText("今天"); //设置默认值为今天
		
		
		all = (ImageView) findViewById(R.id.all_selected_img);
		myself = (ImageView) findViewById(R.id.myself_selected_img);
		group = (ImageView) findViewById(R.id.group_selected_img);
		user = (ImageView) findViewById(R.id.user_selected_img);
		
		allLayout = (RelativeLayout) findViewById(R.id.all_layout);
		allLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				all.setVisibility(View.VISIBLE);
				
				myself.setVisibility(View.GONE);
				group.setVisibility(View.GONE);
				user.setVisibility(View.GONE);
				
				visibleTo = "EVERYONE";
				
				selectedGroupView.setText("");
				selectedUserView.setText(""); //清空
			}
		});
		
		myselfLayout = (RelativeLayout) findViewById(R.id.myself_layout);
		myselfLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myself.setVisibility(View.VISIBLE);
				
				all.setVisibility(View.GONE);
				group.setVisibility(View.GONE);
				user.setVisibility(View.GONE);
				
				visibleTo = "OWNER";
				
				selectedGroupView.setText("");
				selectedUserView.setText(""); //清空
			}
		});
		
		selectedGroupLayout = (RelativeLayout) findViewById(R.id.plan_edit_selected_group);
		selectedGroupLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_MULTIPLE_CHOICE_GROUP);
				group.setVisibility(View.VISIBLE);
				
				all.setVisibility(View.GONE);
				myself.setVisibility(View.GONE);
				user.setVisibility(View.GONE);
				
				visibleTo = "NAMEDGROUP";
				
				selectedUserView.setText(""); //清空
			}
		});
		
		selectedUserLayout = (RelativeLayout) findViewById(R.id.plan_edit_selected_user);
		selectedUserLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_MULTIPLE_CHOICE_CURSOR);
				user.setVisibility(View.VISIBLE);
				
				all.setVisibility(View.GONE);
				myself.setVisibility(View.GONE);
				group.setVisibility(View.GONE);
				
				visibleTo = "ADHOCGROUP";
				
				selectedGroupView.setText("");
			}
		});
		
		selectedUserView = (TextView) findViewById(R.id.plan_edit_selected_user_value);
		selectedGroupView = (TextView) findViewById(R.id.plan_edit_selected_group_value);
		
		ArrayAdapter<CharSequence> adapter_state = ArrayAdapter.createFromResource(
				this, R.array.plan_date, android.R.layout.simple_spinner_item);
		adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	/*	dateSpinner.setAdapter(adapter_state);
		dateSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
//				String item = (String) dateSpinner.getItemAtPosition(position);
//				Toast.makeText(getApplicationContext(), "position="+position+",id="+id+",item="+item, Toast.LENGTH_SHORT).show();
				if(position == 4){ //更迟
					showDialog(DIALOG_PICK_DATE);
				}
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});*/
		
		categorySpinner = (TextView) findViewById(R.id.plan_category_spinner);
		categorySpinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_CHOICE_CATEGORY);
			}
		});
		categorySpinner.setText("无");
//		ArrayAdapter<CharSequence> adapter_category = ArrayAdapter.createFromResource(
//				this, R.array.plan_category, android.R.layout.simple_spinner_item);
//		adapter_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		/*String[] from = new String[] { CategoryEntity._ID,CategoryEntity.KEY_CATEGORYID,CategoryEntity.KEY_CATEGORYNAME };
		Cursor cursor = managedQuery(CategoryEntity.CONTENT_URI, from, null, null,null);
		MatrixCursor mc = new MatrixCursor(from);
//		mc.addRow(new Object[] { null,"", "" });
		cursor.moveToFirst();
		while (cursor.isAfterLast() == false) {
			mc.addRow(new Object[] {cursor.getInt(0),cursor.getString(1), cursor.getString(2)});
			cursor.moveToNext();
		}
		SimpleCursorAdapter adapter_category = new SimpleCursorAdapter(this,
				R.layout.category_spinner_item, mc,
				new String[] { CategoryEntity.KEY_CATEGORYNAME },
				new int[] { R.id.categoryName });
		adapter_category.setDropDownViewResource(R.layout.category_spinner_dropdown_item);
		categorySpinner.setAdapter(adapter_category);*/
		
		View second_divider = findViewById(R.id.second_divider);
		//负责人布局
		userLayout =  (LinearLayout) findViewById(R.id.user_layout);
		whoLayout =  (LinearLayout) findViewById(R.id.who_layout);
		if(user_cursor.getCount() == 1 ){
			second_divider.setVisibility(View.GONE);
			userLayout.setVisibility(View.GONE); 
			whoLayout.setVisibility(View.GONE);
			
			visibleTo = "EVERYONE";
		}else{
			
		}
		
		
		//负责人下拉菜单
		userSpinner = (TextView) findViewById(R.id.plan_user_spinner);
		userSpinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_CHOICE_USER);
			}
		});
		
		//设置负责人默认值
		sharedPrefs = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		userName = sharedPrefs.getString(Constants.PREF_USERNAME, "");
		String[] user_from = new String[] { UserEntity.KEY_REALNAME,UserEntity.KEY_USERID };
    	Cursor user_cursor = managedQuery(UserEntity.CONTENT_URI, user_from, "name = ?", new String[] { userName },null);
    	user_cursor.moveToFirst();
    	realName = user_cursor.getString(0);
    	userid = user_cursor.getString(1);
    	userSpinner.setText(realName);
    	user_cursor.close();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		 case DIALOG_SINGLE_CHOICE:
	            return new AlertDialog.Builder(TaskEditlActivity.this)
	                .setTitle("请选择计划日期")
	                .setSingleChoiceItems(R.array.plan_date, 0, new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int whichButton) {
//	                    	Toast.makeText(getApplicationContext(), "whichButton="+whichButton, Toast.LENGTH_SHORT).show();
	                    	dismissDialog(DIALOG_SINGLE_CHOICE);
	                    	
	                    	switch (whichButton) {
							case 0:
								dateButton.setText("今天");
								break;
							case 1:
								dateButton.setText("明天");
								break;
							case 2:
								dateButton.setText("本周");
								break;
							case 3:
								dateButton.setText("下周");
								break;
							case 4: //更迟
								showDialog(DIALOG_PICK_DATE);
								break;
							default:
								break;
							}
	                    }
	                })
	               .create();
		case DIALOG_PICK_DATE:
			return new DatePickerDialog(this, sDateSetListener, start_Year,
					start_Month, start_Day);
        case DIALOG_MULTIPLE_CHOICE_CURSOR:
            String[] projection = new String[] {
            		UserEntity.KEY_USERID,
            		UserEntity.KEY_REALNAME
            };
            Cursor cursor = managedQuery(UserEntity.CONTENT_URI,projection, "name <> ?", new String[]{userName}, null);
            
            //直接从数据库中取值，多选列表有问题。改为从数组中取值
            province = new String[cursor.getCount()];
            ids = new String[cursor.getCount()];
            checkAry = new boolean[cursor.getCount()];
			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				String label = cursor.getString(1);
				province[i] = label;
//				ids[i] = cursor.getString(0);
				String userid = cursor.getString(0);
				ids[i] = userid;
			}
			cursor.close();
			
        	AlertDialog builder = new AlertDialog.Builder(this)
			.setTitle("请选择人员：")
			.setMultiChoiceItems(province,
//					new boolean[] { false, false, false, false, false },
					checkAry,
					new OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which, boolean isChecked) {
						}
					})
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String s = "";
					// 扫描所有的列表项，如果当前列表项被选中，将列表项的文本追加到s变量中。
					for (int i = 0; i < province.length; i++) {
						if (lv.getCheckedItemPositions().get(i)) {
//							s += i + ":" + lv.getAdapter().getItem(i) + " ";
							selectIds.add((String) lv.getAdapter().getItem(i));
							s += lv.getAdapter().getItem(i) + ",";
						}
					}
					s = s.substring(0, s.length()-1); //去掉结尾逗号
					selectedUserView.setText(s);
					
//					String sid;
//					for (int i = 0; i < ids.length; i++) {
//						if (lv.getCheckedItemPositions().get(i)) {
//							sid = (String) lv.getAdapter().getItem(i);
//							selectIds.add(sid);
//						}
//					}
				}
			}).setNegativeButton("取消", null).create();
	//
        	lv = builder.getListView();
        	return builder;
        case DIALOG_MULTIPLE_CHOICE_GROUP:
        	String[] group_from = new String[] { GroupEntity._ID,GroupEntity.KEY_GROUPID,GroupEntity.KEY_NAME };
        	final Cursor group_cursor = managedQuery(GroupEntity.CONTENT_URI, group_from, null, null,null);
            AlertDialog groupAlert =  new AlertDialog.Builder(TaskEditlActivity.this)
            .setTitle("请选择组：")
            .setSingleChoiceItems(group_cursor, 0, "name",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	dismissDialog(DIALOG_MULTIPLE_CHOICE_GROUP);
                	group_cursor.moveToPosition(whichButton);
                	String groupName = group_cursor.getString(2);
                	groupID = group_cursor.getString(1);
                	selectedGroupView.setText(groupName);
                }
            }).create();
            return groupAlert;
        case DIALOG_CHOICE_USER:
//        	String[] user_from = new String[] { UserEntity._ID,UserEntity.KEY_USERID,UserEntity.KEY_REALNAME };
//        	final Cursor user_cursor = managedQuery(UserEntity.CONTENT_URI, user_from, null, null,null);
            AlertDialog alert =  new AlertDialog.Builder(TaskEditlActivity.this)
            .setTitle("请选择负责人")
            .setSingleChoiceItems(user_cursor, 0, "realname",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	dismissDialog(DIALOG_CHOICE_USER);
                	user_cursor.moveToPosition(whichButton);
                	userid = user_cursor.getString(1);
                	realName = user_cursor.getString(2);
                	userSpinner.setText(realName);
                }
            }).create();
            return alert;
        case DIALOG_CHOICE_CATEGORY:
        	String[] category_from = new String[] { CategoryEntity._ID,CategoryEntity.KEY_CATEGORYID,CategoryEntity.KEY_CATEGORYNAME };
        	final Cursor category_cursor = managedQuery(CategoryEntity.CONTENT_URI, category_from, null, null,null);
        	//添加默认值
        	final MatrixCursor mc = new MatrixCursor(category_from);
    		mc.addRow(new Object[] { null, "00", "无" });
    		
			for (category_cursor.moveToFirst(); !category_cursor.isAfterLast(); category_cursor
					.moveToNext()) { // 遍历
				mc.addRow(new Object[] { category_cursor.getInt(0), category_cursor.getString(1),
						category_cursor.getString(2) });
			}
        	
            AlertDialog category_alert =  new AlertDialog.Builder(TaskEditlActivity.this)
            .setTitle("请选择分类：")
            .setSingleChoiceItems(mc, 0, "categoryname",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	dismissDialog(DIALOG_CHOICE_CATEGORY);
                	mc.moveToPosition(whichButton);
                	categoryId = mc.getString(1);
                	categoryType = mc.getString(2);
                	categorySpinner.setText(categoryType);
                }
            }).create();
            return category_alert;
		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	private DatePickerDialog.OnDateSetListener sDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			start_Year = year;
			start_Month = monthOfYear;
			start_Day = dayOfMonth;
			String date = updateDisplay();
			
			//将日期显示到Button
			dateButton.setText(date);
		}
	};
	
	private String updateDisplay() {
		String sMonth;
		if (start_Month + 1 < 10) {
			sMonth = "0" + String.valueOf(start_Month + 1);
		} else {
			sMonth = String.valueOf(start_Month + 1);
		}
		
		return new StringBuffer().append(start_Year).append("-").append(sMonth).append("-").append(DateTimeUtil.pad(start_Day)).toString();
	}
	
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
		}else{
			dueDate = dateButton.getText().toString();
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
			pDialog = ProgressDialog.show(TaskEditlActivity.this, "", "计划任务同步中...", true, true);
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
			return result;
		}

		@Override
		protected void onPostExecute(Result result) {
			pDialog.dismiss();
			
			finish();
		}
	}
	
	private void saveTaskUser(){
		//删除当前计划任务对应的所有权限数据
		getContentResolver().delete(AuthorityEntity.CONTENT_URI, "dataId = ?", new String[]{entity.getTaskid()});
		
		AuthorityEntity taskUser = new AuthorityEntity();
		String visb = entity.getVisibleTo();
		if("EVERYONE".equals(visb)){
			taskUser.setAuthId(UUIDUtil.getUUID());
			taskUser.setDataId(entity.getTaskid());
			taskUser.setOwners("ALL");
			taskUser.setVisibleto("EVERYONE");
			
			getContentResolver().insert(AuthorityEntity.CONTENT_URI, taskUser.toContentValues());
		}else if("OWNER".equals(visb)){
			taskUser.setAuthId(UUIDUtil.getUUID());
			taskUser.setDataId(entity.getTaskid());
			taskUser.setOwners(entity.getOwnerId());
			taskUser.setVisibleto("OWNER");
			
			getContentResolver().insert(AuthorityEntity.CONTENT_URI, taskUser.toContentValues());
		}else if("NAMEDGROUP".equals(visb)){
			taskUser.setAuthId(UUIDUtil.getUUID());
			taskUser.setDataId(entity.getTaskid());
			taskUser.setOwners(groupID);
			taskUser.setVisibleto("NAMEDGROUP");
			
			getContentResolver().insert(AuthorityEntity.CONTENT_URI, taskUser.toContentValues());
		}else if("ADHOCGROUP".equals(visb)){
			for (int i = 0; i < selectIds.size(); i++) {
				taskUser.setAuthId(UUIDUtil.getUUID());
				taskUser.setDataId(entity.getTaskid());
				taskUser.setVisibleto("ADHOCGROUP");
				
				String id = (String) selectIds.get(i);//从dialog里获取的realname
				Log.d(TAG, "dialog选中人员："+id);
				String onwerid = "";
				Cursor userCursor = getContentResolver().query(UserEntity.CONTENT_URI, null, "realname = '" + id + "'",null, null);
				while (userCursor.moveToFirst()) {
					onwerid = userCursor.getString(6);
					break;
				}
				userCursor.close();
				//TODO dialog是否能获取id？ 不希望从数据库中通过用户名查询对应id
				taskUser.setOwners(onwerid);
				getContentResolver().insert(AuthorityEntity.CONTENT_URI,taskUser.toContentValues());
			}
			//单独插入当前登录人
			AuthorityEntity singletaskUser = new AuthorityEntity();
			singletaskUser.setAuthId(UUIDUtil.getUUID());
			singletaskUser.setDataId(entity.getTaskid());
			singletaskUser.setOwners(userid);
			singletaskUser.setVisibleto("ADHOCGROUP");
			getContentResolver().insert(AuthorityEntity.CONTENT_URI, singletaskUser.toContentValues());
		}
	}
}
