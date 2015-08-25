package com.zhuying.android.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.zhuying.android.R;
import com.zhuying.android.adapter.NoteAdapter;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.StringUtil;

/**
 * 联系人明细
 */
public class ContactDetailActivity extends Activity {
	private String contactName = "";
	public static List<ContentValues> recordList = new ArrayList<ContentValues>();
	
	private boolean recordLayoutExpd = true; //记录展开
	private LinearLayout recordListView;
	LinearLayout record_total_layout;
	private TextView nameTextView;
	private TextView titleView;
	private TextView companyView;
	private LinearLayout detailLayout;
	private Button record_btn; //新增记录按钮
	
	private ContactEntity entity;
	private Cursor mCursor;
//	private Uri uri;
	private String id; //主键
	
	//计划任务
	private Button plan_btn; //新增记录按钮
	private boolean planLayoutExpd = true; //计划任务展开
	private LinearLayout planListView;
	private LinearLayout plan_total_layout;
	
	private TextView title;
	private ImageView iconView;
	
	String currentYear = DateTimeUtil.format_ymd(new Date()).substring(0,4);
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contact_detail);
		initUI();
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		id = b.get("id").toString();
		
//		uri = getIntent().getData();
//		rid = ContentUris.parseId(uri);
		mCursor = managedQuery(ContactEntity.CONTENT_URI, null, ContactEntity.KEY_PARTYID + " = '" + id +"'", null, null);
		
//		loadRecodListData();
//		loadPlanListData();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mCursor != null){
			mCursor.moveToFirst();
			entity = new ContactEntity(mCursor);
			
			String partyFace = entity.getPartyface();
			Cursor photoCursor = managedQuery(PhotoEntity.CONTENT_URI, null, PhotoEntity.KEY_NAME + " = '" + partyFace +"'", null, null);
			if(photoCursor.moveToFirst()){
//				photoCursor.moveToFirst();
				String content = photoCursor.getString(4);
				byte[] data = Base64.decode(content, Base64.DEFAULT);
				Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
				iconView.setImageBitmap(icon);
			}else{
				iconView.setBackgroundResource(R.drawable.contact_icon);
			}
			
			title.setText(entity.getName());
			title.setWidth(120);
			title.setSingleLine(true);
			title.setEllipsize(TextUtils.TruncateAt.END);
			nameTextView.setText(entity.getName());
			titleView.setText(entity.getTitle());
			String companyName = entity.getCompanyname();
			if(!StringUtil.isEmpty(companyName)){
				companyView.setText(companyName);
			}else{
				companyView.setText("");
			}
			
			//防止每次从修改返回时，重复生成View
			detailLayout.removeAllViews();
			//电话
			String phoneJSON = entity.getPhone();
			try {
				JSONArray phoneAry = new JSONArray(phoneJSON);
				for(int i=0;i<phoneAry.length();i++){
					JSONObject json = (JSONObject) phoneAry.get(i);
					if(!TextUtils.isEmpty(json.getString("phoneText"))){
						genMobileLayout(json.getString("phoneType"), json.getString("phoneText"));	
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//Email
			String emailJSON = entity.getEmail();
			try {
				JSONArray emailAry = new JSONArray(emailJSON);
				for(int i=0;i<emailAry.length();i++){
					JSONObject json = (JSONObject) emailAry.get(i);
					if(!TextUtils.isEmpty(json.getString("emailText"))){
						genEmailLayout(json.getString("emailType"), json.getString("emailText"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//地址
			String addressJSON = entity.getAddress();
			try {
				JSONArray addressAry = new JSONArray(addressJSON);
				for(int i=0;i<addressAry.length();i++){
					JSONObject json = (JSONObject) addressAry.get(i);
					if(!TextUtils.isEmpty(json.getString("addressText"))){
						genAddressLayout(json.getString("addressType"), json.getString("addressText"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//网址
			String website = entity.getWebsites();
			try {
				JSONArray webAry = new JSONArray(website);
				for(int i=0;i<webAry.length();i++){
					JSONObject json = (JSONObject) webAry.get(i);
					if(!TextUtils.isEmpty(json.getString("websitesText"))){
						genWebsiteLayout(json.getString("websitesType"), json.getString("websitesText"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//IM
			String imJSON = entity.getIm();
			try {
				JSONArray imAry = new JSONArray(imJSON);
				for(int i=0;i<imAry.length();i++){
					JSONObject json = (JSONObject) imAry.get(i);
					if(!TextUtils.isEmpty(json.getString("imText"))){
						genImLayout(json.getString("imType"), json.getString("imText"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		loadRecodListData();
		loadPlanListData();
	}

	private void initUI() {
		//init Header
		title = (TextView) findViewById(R.id.header_title);
//		title.setText("联系人明细");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setText("修改");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(Intent.ACTION_EDIT,uri));
				
				Intent i = new Intent();
				i.setAction(Intent.ACTION_EDIT);
				i.setData(ContactEntity.CONTENT_URI);
				i.setType("vnd.android.cursor.item/vnd.zhuying.contacts");
				i.putExtra("id", id);
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
		nameTextView = (TextView) findViewById(R.id.contact_name);
		nameTextView.getPaint().setFakeBoldText(true);
		titleView = (TextView) findViewById(R.id.contact_title);
		companyView = (TextView) findViewById(R.id.contact_company);
		iconView = (ImageView) findViewById(R.id.contact_icon);
		
		
		detailLayout = (LinearLayout) findViewById(R.id.detail_layout);
		
		record_btn = (Button) findViewById(R.id.contact_record_btn);
		record_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_INSERT);
				i.setData(NoteEntity.CONTENT_URI);
				i.putExtra("subjectid", entity.getPartyId()); //关联对象id
				i.putExtra("subjecttype", CompanyEntity.TYPE_CONTACT); //关联对象类型
				i.putExtra("subjectname", nameTextView.getText().toString()); //关联对象名称
				i.putExtra("subjectface", entity.getPartyface()); //关联对象id
				startActivity(i);
			}
		});
		
		recordListView = (LinearLayout) findViewById(R.id.contact_record_listview);
		record_total_layout = (LinearLayout) findViewById(R.id.record_total_layout);
		
		final ImageView arrow = (ImageView) findViewById(R.id.record_expand_layout_arrow);
		LinearLayout record_expand_layout = (LinearLayout) findViewById(R.id.record_expand_layout);
		record_expand_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(recordLayoutExpd){
//					recordListView.setVisibility(View.INVISIBLE);
					record_total_layout.removeView(recordListView);
					arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.div_right));
					recordLayoutExpd = false;
				}else{
//					recordListView.setVisibility(View.VISIBLE);
					record_total_layout.addView(recordListView);
					arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.div_down));
					recordLayoutExpd = true;
				}
			}
		});
		
		//计划任务
		plan_btn = (Button) findViewById(R.id.contact_plan_btn);
		plan_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setAction(Intent.ACTION_INSERT);
				i.setData(TaskEntity.CONTENT_URI);
				i.putExtra("subjectid", entity.getPartyId()); //关联对象id
				i.putExtra("subjecttype", CompanyEntity.TYPE_CONTACT);
				i.putExtra("subjectname", nameTextView.getText().toString()); //关联对象名称
				startActivity(i);
			}
		});
		planListView = (LinearLayout) findViewById(R.id.contact_plan_listview);
		plan_total_layout = (LinearLayout) findViewById(R.id.plan_total_layout);
		final ImageView plan_arrow = (ImageView) findViewById(R.id.plan_expand_layout_arrow);
		LinearLayout plan_expand_layout = (LinearLayout) findViewById(R.id.plan_expand_layout);
		plan_expand_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(planLayoutExpd){
					plan_total_layout.removeView(planListView);
					plan_arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.div_right));
					planLayoutExpd = false;
				}else{
					plan_total_layout.addView(planListView);
					plan_arrow.setBackgroundDrawable(getResources().getDrawable(R.drawable.div_down));
					planLayoutExpd = true;
				}
			}
		});
	}
	
	/**
	 * 加载记录列表数据
	 */
	private void loadRecodListData() {
		Cursor cursor = managedQuery(NoteEntity.CONTENT_URI, null, "subjectid = '"+ entity.getPartyId() + "' and subjecttype = '"+CompanyEntity.TYPE_CONTACT+"' ", null,"dueat desc");
//		String[] from = {RecordEntity.KEY_BODY,RecordEntity.KEY_UPDATEDAT,RecordEntity.KEY_OWNERNAME};
//		int[] to = {R.id.record_content,R.id.record_date,R.id.record_person};
//		NoteAdapter recordAdapter = new NoteAdapter(this, R.layout.record_list_item, cursor, from, to);
//		recordListView.setAdapter(recordAdapter);
		if(cursor.getCount()== 0){
			record_total_layout.setVisibility(View.GONE);
		}else{
			recordListView.removeAllViews();
			record_total_layout.setVisibility(View.VISIBLE);
			record_total_layout.removeView(recordListView);

			for (int i = 0; i < cursor.getCount(); i++) {
				cursor.moveToPosition(i);
				String body = cursor.getString(1);
				String dueat = cursor.getString(5);
				if(! TextUtils.isEmpty(dueat)){
					String year = dueat.substring(0, 4);
					if(currentYear.equals(year)){
						dueat =  dueat.substring(5, 10);
					}else{
						dueat = dueat.substring(0, 10);
					}
				}
				
				String owner = cursor.getString(10);
				final String rid = cursor.getString(12);
				
				RelativeLayout itemLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.note_list_item, null);
				itemLayout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Uri uri= NoteEntity.CONTENT_URI;
						Intent i = new Intent();
						i.setAction(Intent.ACTION_VIEW);
						i.setData(uri);
						i.setType("vnd.android.cursor.item/vnd.zhuying.records");
						i.putExtra("id", rid); //主键（noteId）
						i.putExtra("subjectid", entity.getPartyId()); //关联对象id
						i.putExtra("subjecttype", CompanyEntity.TYPE_CONTACT); //关联对象类型
						i.putExtra("subjectname", nameTextView.getText().toString()); //关联对象名称
						i.putExtra("from", "contact_detail");
						startActivity(i);
					}
				});
				TextView nameView = (TextView) itemLayout.findViewById(R.id.record_content);
				nameView.setText(body);
				TextView dateView = (TextView) itemLayout.findViewById(R.id.record_date);
				dateView.setText(dueat);
				TextView personView =(TextView) itemLayout.findViewById(R.id.record_person);
				personView.setText("记录人："+owner);
				
//				View sep = getLayoutInflater().inflate(R.layout.setting_divider, null);
//				itemLayout.addView(sep);
				recordListView.addView(itemLayout);
			}
			record_total_layout.addView(recordListView);
		}
		cursor.close();
	}
	
	/**
	 * 加载计划任务列表数据
	 */
	private void loadPlanListData() {
		Cursor taskCursor = managedQuery(TaskEntity.CONTENT_URI, null, "subjectid = '"+ entity.getPartyId() + "' and subjecttype = '"+CompanyEntity.TYPE_CONTACT+"' ", null,null);
		if(taskCursor.getCount()== 0){
			plan_total_layout.setVisibility(View.GONE);
		}else{
			planListView.removeAllViews();
			plan_total_layout.setVisibility(View.VISIBLE);
			plan_total_layout.removeView(planListView);
			
			for (int i = 0; i < taskCursor.getCount(); i++) {
				taskCursor.moveToPosition(i);
				String body = taskCursor.getString(1);
				final String rid = taskCursor.getString(18);
				String taskType = taskCursor.getString(6);
				String taskTypeId = taskCursor.getString(5);
				String dueat = taskCursor.getString(8);
				String taskid = taskCursor.getString(18);
				
				RelativeLayout layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.record_plan_list_item, null);
				layout.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent();
						i.setAction(Intent.ACTION_VIEW);
						i.setData(TaskEntity.CONTENT_URI);
						i.setType("vnd.android.cursor.item/vnd.zhuying.plans");
						i.putExtra("id", rid); //主键（taskId）
						i.putExtra("subjectid", entity.getPartyId()); //关联对象id
						i.putExtra("subjecttype", CompanyEntity.TYPE_CONTACT); //关联对象类型
						i.putExtra("subjectname", nameTextView.getText().toString()); //关联对象名称
						i.putExtra("from", "task_detail");
						startActivity(i);
					}
				});
				TextView nameView = (TextView) layout.findViewById(R.id.name);
				nameView.setText(body);
				TextView typeView = (TextView) layout.findViewById(R.id.type);
				typeView.setText(taskType);
				Cursor categoryCursor = getContentResolver().query(CategoryEntity.CONTENT_URI, null,"categoryid = ?", new String[]{taskTypeId}, null);
				while (categoryCursor.moveToFirst()) {
					String categoryColor = categoryCursor.getString(4);
					typeView.setBackgroundColor(Color.parseColor(categoryColor));
					break;
				}
				categoryCursor.close();
				
				TextView dateView = (TextView) layout.findViewById(R.id.date);
				dateView.setText(getDueatType(taskid));
				
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 50);
				planListView.addView(layout, lp);
			}
			plan_total_layout.addView(planListView);
		}
		taskCursor.close();
	}
	
	/**
	 * 生成电话
	 */
	private void genMobileLayout(String label,String value){
		final String number = value;
		/*final LinearLayout mobileLayout = new LinearLayout(getApplicationContext());
		mobileLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mobileLayout.setOrientation(LinearLayout.HORIZONTAL);
		mobileLayout.setPadding(70, 5, 0, 0);
		LayoutParams blp = new LayoutParams(60,LayoutParams.WRAP_CONTENT);
		TextView labelView = new TextView(getApplicationContext());
		labelView.setLayoutParams(blp);
		labelView.setText(label);
		labelView.setTextSize(16);
		labelView.setTextColor(Color.GRAY);
		labelView.setGravity(Gravity.RIGHT);
		
		LinearLayout.LayoutParams nlp = new LinearLayout.LayoutParams(260,LayoutParams.WRAP_CONTENT);
		nlp.setMargins(10, 0, 0, 0);
		TextView valueView = new TextView(getApplicationContext());
		valueView.setLayoutParams(nlp);
		valueView.setText(value);
		valueView.setTextSize(16);
		valueView.setTextColor(Color.BLACK);
//		valueView.getPaint().setFakeBoldText(true);
		
		mobileLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pickTelorSMS(number);
			}
		});
		
		mobileLayout.addView(labelView);
		mobileLayout.addView(valueView);*/
		
		
		LinearLayout mobileLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.row_mobilel, null);
		mobileLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pickTelorSMS(number);
			}
		});
		TextView labelView = (TextView) mobileLayout.findViewById(R.id.mobile_label);
		TextView valueView = (TextView) mobileLayout.findViewById(R.id.mobile_value);
		
		labelView.setText(label);
		valueView.setText(value);
		
		mobileLayout.removeAllViews();
		mobileLayout.addView(labelView);
		mobileLayout.addView(valueView);
		detailLayout.addView(mobileLayout);
		detailLayout.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 生成email
	 */
	private void genEmailLayout(String label,String value){
		LinearLayout emailLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.row_email, null);
		TextView labelView = (TextView) emailLayout.findViewById(R.id.email_label);
		TextView valueView = (TextView) emailLayout.findViewById(R.id.email_value);
		
		labelView.setText(label);
		valueView.setText(value);
		
		emailLayout.removeAllViews();
		emailLayout.addView(labelView);
		emailLayout.addView(valueView);
		
		detailLayout.addView(emailLayout);
		detailLayout.setVisibility(View.VISIBLE);
	}
	
	private void genAddressLayout(String label,String value){
		/*final String number = value;
		final LinearLayout addressLayout = new LinearLayout(getApplicationContext());
		addressLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addressLayout.setOrientation(LinearLayout.HORIZONTAL);
		addressLayout.setPadding(70, 5, 0, 0);
		LayoutParams blp = new LayoutParams(60,LayoutParams.WRAP_CONTENT);
		TextView labelView = new TextView(getApplicationContext());
		labelView.setLayoutParams(blp);
		labelView.setText(label);
		labelView.setTextSize(16);
		labelView.setTextColor(Color.GRAY);
		labelView.setGravity(Gravity.RIGHT);
		
		LinearLayout.LayoutParams nlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		nlp.setMargins(10, 0, 0, 0);
		TextView valueView = new TextView(getApplicationContext());
		valueView.setLayoutParams(nlp);
		valueView.setText(value);
		valueView.setTextSize(16);
		valueView.setTextColor(Color.BLACK);
//		valueView.getPaint().setFakeBoldText(true);
		
		addressLayout.addView(labelView);
		addressLayout.addView(valueView);*/
		
		
		LinearLayout addressLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.row_address, null);
		TextView labelView = (TextView) addressLayout.findViewById(R.id.address_label);
		TextView valueView = (TextView) addressLayout.findViewById(R.id.address_value);
		
		labelView.setText(label);
		valueView.setText(value);
		
		addressLayout.removeAllViews();
		addressLayout.addView(labelView);
		addressLayout.addView(valueView);
		
		detailLayout.addView(addressLayout);
		detailLayout.setVisibility(View.VISIBLE);
	}
	
	private void genImLayout(String label,String value){
		/*final LinearLayout imLayout = new LinearLayout(getApplicationContext());
		imLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		imLayout.setOrientation(LinearLayout.HORIZONTAL);
		imLayout.setPadding(70, 5, 0, 0);
		LayoutParams blp = new LayoutParams(60,LayoutParams.WRAP_CONTENT);
		TextView labelView = new TextView(getApplicationContext());
		labelView.setLayoutParams(blp);
		labelView.setText(label);
		labelView.setTextSize(16);
		labelView.setTextColor(Color.GRAY);
		labelView.setGravity(Gravity.RIGHT);
		
		LinearLayout.LayoutParams nlp = new LinearLayout.LayoutParams(260,LayoutParams.WRAP_CONTENT);
		nlp.setMargins(10, 0, 0, 0);
		TextView valueView = new TextView(getApplicationContext());
		valueView.setLayoutParams(nlp);
		valueView.setText(value);
		valueView.setTextSize(16);
		valueView.setTextColor(Color.BLACK);
//		valueView.getPaint().setFakeBoldText(true);
		
		imLayout.addView(labelView);
		imLayout.addView(valueView);*/
		
		LinearLayout imLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.row_im, null);
		TextView labelView = (TextView) imLayout.findViewById(R.id.im_label);
		TextView valueView = (TextView) imLayout.findViewById(R.id.im_value);
		
		labelView.setText(label);
		valueView.setText(value);
		
		imLayout.removeAllViews();
		imLayout.addView(labelView);
		imLayout.addView(valueView);
		
		detailLayout.addView(imLayout);
		detailLayout.setVisibility(View.VISIBLE);
	}
	
	private void genWebsiteLayout(String label,String value){
		/*final LinearLayout websiteLayout = new LinearLayout(getApplicationContext());
		websiteLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		websiteLayout.setOrientation(LinearLayout.HORIZONTAL);
		websiteLayout.setPadding(70, 5, 0, 0);
		LayoutParams blp = new LayoutParams(60,LayoutParams.WRAP_CONTENT);
		TextView labelView = new TextView(getApplicationContext());
		labelView.setLayoutParams(blp);
		labelView.setText(label);
		labelView.setTextSize(16);
		labelView.setTextColor(Color.GRAY);
		labelView.setGravity(Gravity.RIGHT);
		
		LinearLayout.LayoutParams nlp = new LinearLayout.LayoutParams(260,LayoutParams.WRAP_CONTENT);
		nlp.setMargins(10, 0, 0, 0);
		TextView valueView = new TextView(getApplicationContext());
		valueView.setLayoutParams(nlp);
		valueView.setText(value);
		valueView.setTextSize(16);
		valueView.setTextColor(Color.BLACK);
//		valueView.getPaint().setFakeBoldText(true);
		
		
		websiteLayout.addView(labelView);
		websiteLayout.addView(valueView);*/
		
		LinearLayout websiteLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.row_website, null);
		TextView labelView = (TextView) websiteLayout.findViewById(R.id.website_label);
		TextView valueView = (TextView) websiteLayout.findViewById(R.id.website_value);
		
		labelView.setText(label);
		valueView.setText(value);
		
		websiteLayout.removeAllViews();
		websiteLayout.addView(labelView);
		websiteLayout.addView(valueView);
		detailLayout.addView(websiteLayout);
		detailLayout.setVisibility(View.VISIBLE);
	}
	
	private String getDueatType(String taskid){
		String today = DateTimeUtil.getToday();
		String tomorrow = DateTimeUtil.getTomorrow();
		String thieWeek = DateTimeUtil.getCurrentWeekday();
		String nextWeek = DateTimeUtil.getNextSunday();
		
		String str = "";
		
		String selection = "taskid = '"+taskid+"' and status = 'unfinish' and dueat < '" + today + " 00:00:00" + "'";
		Cursor outdate_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, null);
		if(outdate_cursor.moveToFirst()){
			str = "已过期";
			return str;
		}else{
			selection = "taskid = '"+taskid+"' and status = 'unfinish' and dueat >= '"+today + " 00:00:00"+"' and dueat < '"+today+ " 24:00:00"+"'";
			Cursor today_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, null);
			if(today_cursor.moveToFirst()){
				str = "今天";
				return str;
			}else{
				selection = "taskid = '"+taskid+"' and status = 'unfinish' and dueat >= '"+tomorrow + " 00:00:00"+"' and dueat < '"+tomorrow+ " 24:00:00"+"'";
				Cursor tomorrow_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, null);
				if(tomorrow_cursor.moveToFirst()){
					str = "明天";
					return str;
				}else{
					selection = "taskid = '"+taskid+"' and status = 'unfinish' and dueat > '"+tomorrow + " 24:00:00"+"' and dueat <= '"+thieWeek+ " 00:00:00"+"'";
					Cursor thisweek_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, null);
					if(thisweek_cursor.moveToFirst()){
						str = "本周";
						return str;
					}else{
						selection = "taskid = '"+taskid+"' and status = 'unfinish' and dueat > '"+thieWeek + " 24:00:00"+"' and dueat <= '"+nextWeek+ " 00:00:00"+"'";
						Cursor nextweek_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, null);
						if(nextweek_cursor.moveToFirst()){
							str = "下周";
							return str;
						}else{
							selection = "taskid = '"+taskid+"' and status = 'unfinish' and dueat > '"+nextWeek + " 24:00:00"+"'";
							Cursor specify_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, null);
							if(specify_cursor.moveToFirst()){
								str = "更迟的";
								return str;
							}else{
								selection = "taskid = '"+taskid+"' and status = 'finish'";
								Cursor finish_cursor = managedQuery(TaskEntity.CONTENT_URI, null,selection, null, null);
								if(finish_cursor.moveToFirst()){
									str = "已完成";
									return str;
								}
							}
						}
					}
				}
			}
		}
		return str;
	}
	
	
	
	/**
	 * 打电话/发短信 选择对话框
	 */
	private void pickTelorSMS(final String mobileNumber){
		final Context ctx = ContactDetailActivity.this;
		final Context dialogContext = new ContextThemeWrapper(ctx,
				android.R.style.Theme_Light);
		String cancel = "返回";
		String[] choices;
		choices = new String[2];
		choices[0] = "打电话";
		choices[1] = "发短信";
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle("请选择");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0: { //打电话
							callMobile(ctx, mobileNumber);
							break;
						}
						case 1: //发短信
							sendSMS(ctx, mobileNumber);
							break;
						}
					}
				});
		builder.setNegativeButton(cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
	
	
	/**
	 * 发短信
	 * @param cott
	 * @param mobile
	 */
	public void sendSMS(Context cott, String mobile) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.putExtra("address", mobile);
		intent.setType("vnd.android-dir/mms-sms");
		cott.startActivity(intent);
	}
	
	/**
	 * 打电话
	 * @param cott
	 * @param mobile
	 */
	public void callMobile(Context cott, String mobile) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + mobile));
		cott.startActivity(intent);
	}
}
