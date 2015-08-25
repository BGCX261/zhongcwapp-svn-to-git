package com.zhuying.android.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.zhuying.android.R;
import com.zhuying.android.adapter.RecordAdapter.RecordViewHolder;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.StringUtil;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TaskListAdapter extends SimpleAdapter {
	private Context context;
	private String currentUser; // 当前登录用户
	private SharedPreferences sharedPrefs;
//
	private static final String TAG = "TaskListAdapter";
//	String current;
//	private List list = new ArrayList();
	private List<String> listTag = new ArrayList<String>();
//	String previous = "";
	
	int res;

	public TaskListAdapter(Context context, int layout, List list,String[] from,int[] to,List tags) {
		super(context, list, layout, from,to);
		
//		this.listTag = tags;
		this.context = context;
		this.res = layout;

		sharedPrefs = this.context.getSharedPreferences(Constants.PREF,
				Context.MODE_PRIVATE);
		
		//取得当前用户
		String userName = sharedPrefs.getString(Constants.PREF_USERNAME, "");
		Cursor userCursor = context.getContentResolver().query(
				UserEntity.CONTENT_URI, null, "name = '" + userName + "'",
				null, null);
		while (userCursor.moveToFirst()) {
			currentUser = userCursor.getString(4);
			break;
		}
		userCursor.close();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*View view = convertView;
//		View view;
		if(view == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(res, parent, false);
			
			TextView groupView = (TextView) view.findViewById(R.id.group);
			TextView titleView = (TextView) view.findViewById(R.id.name);
			
			Map item = (Map) getItem(position);
			String tag = (String) item.get("tag");
			String name = (String) item.get("body");
			Log.d(TAG, "previous = "+previous+",tag = "+tag);
			if(!previous.equals(tag)){
				groupView.setVisibility(View.VISIBLE);
				groupView.setText(tag);
				previous = tag;
			}else {
				groupView.setVisibility(View.GONE);
			}
			titleView.setText(name);
		}else{
//			view = convertView;
		}
		
		TextView groupView = (TextView) view.findViewById(R.id.group);
		TextView titleView = (TextView) view.findViewById(R.id.name);
		
		Map item = (Map) getItem(position);
		String tag = (String) item.get("tag");
		String name = (String) item.get("body");
		Log.d(TAG, "previous = "+previous+",tag = "+tag);
		if(!previous.equals(tag)){
			groupView.setVisibility(View.VISIBLE);
			groupView.setText(tag);
			previous = tag;
		}else {
			groupView.setVisibility(View.GONE);
		}
		titleView.setText(name);
		
		return view;*/
		

		TaskViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)context.getSystemService
		      (Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(res, parent, false);
			holder = new TaskViewHolder();
			holder.contentView = (TextView) convertView.findViewById(R.id.name);
			holder.groupView = (TextView) convertView.findViewById(R.id.group);
			holder.ownerView = (TextView) convertView.findViewById(R.id.owner);
			holder.subjectView = (TextView) convertView.findViewById(R.id.subject_name);
			holder.typeView = (TextView) convertView.findViewById(R.id.type);
			convertView.setTag(holder);
		} else {
			holder = (TaskViewHolder) convertView.getTag();
		}
		
		Map item = (Map) getItem(position);
		String current = (String) item.get("tag");
		String name = (String) item.get("body");
		String owner = (String) item.get("owner");
		String subjectname = (String) item.get("subjectname");
		String type = (String) item.get("type");
		String taskTypeId = (String) item.get("taskTypeId");
		
		String previous;
		try{
			Map pre = (Map) getItem(position-1);
			previous = (String) pre.get("tag");
		}catch (Exception e) {
			previous = "";
		}
		
		if(!previous.equals(current)){
			holder.groupView.setVisibility(View.VISIBLE);
			holder.groupView.setText(current);
			
			//设置分组背景色
			if("已过期".equals(current)){
				holder.groupView.setBackgroundColor(context.getResources().getColor(R.color.task_outdate));
			}else if("今天".equals(current)){
				holder.groupView.setBackgroundColor(context.getResources().getColor(R.color.task_today));
			}else if("明天".equals(current)){
				holder.groupView.setBackgroundColor(context.getResources().getColor(R.color.task_tomorrow));
			}else if("本周".equals(current)){
				holder.groupView.setBackgroundColor(context.getResources().getColor(R.color.task_tomorrow));
			}else if("下周".equals(current)){
				holder.groupView.setBackgroundColor(context.getResources().getColor(R.color.task_tomorrow));
			}else if("已完成".equals(current)){
				holder.groupView.setBackgroundColor(context.getResources().getColor(R.color.task_finish));
			}else{
				
			}
			
//			previous = current;
		}else {
			holder.groupView.setVisibility(View.GONE);
		}
		
		
//		holder.contentView.getPaint().setFakeBoldText(true);
		holder.contentView.setText(name);
//		Log.d(TAG, "currentUser = "+currentUser+" , owner = "+owner);
		if (currentUser.equals(owner)) {
			holder.ownerView.setText("");
		} else {
//			holder.ownerView.getPaint().setFakeBoldText(true);
			holder.ownerView.setText(owner + "：");
		}
		
		if(subjectname == null || subjectname.equals("null")){
			holder.subjectView.setVisibility(View.INVISIBLE);
		}else{
			holder.subjectView.setVisibility(View.VISIBLE);
			holder.subjectView.setText(subjectname);
		}
		
//		Cursor c = this.context.getContentResolver().query(CategoryEntity.CONTENT_URI, null,"categoryid ='"+taskTypeId+"'", null, null);
//		while (c.moveToFirst()) {
//			String categoryColor = c.getString(4);
//			holder.typeView.setBackgroundColor(Color.parseColor(categoryColor));
//			break;
//		}
//		c.close();
		
		if(TextUtils.isEmpty(type) || "无".equals(type)){
			holder.typeView.setVisibility(View.GONE);
		}else{
			holder.typeView.setVisibility(View.VISIBLE);
			
			Cursor c = this.context.getContentResolver().query(CategoryEntity.CONTENT_URI, null,"categoryid ='"+taskTypeId+"'", null, null);
			while (c.moveToFirst()) {
				String categoryColor = c.getString(4);
//				Log.d(TAG, "color = " + categoryColor);
				if(!StringUtil.isEmpty(categoryColor)){
					holder.typeView.setText(type);
					holder.typeView.setBackgroundColor(Color.parseColor(categoryColor));
					break;
				}
			}
			c.close();
		}
		
		convertView.setBackgroundResource(R.drawable.listview_item_selector);
		return convertView;
	}

//	@Override
//	public void bindView(View view, Context context, Cursor cursor) {
//		super.bindView(view, context, cursor);
//
//		TextView typeView = (TextView) view.findViewById(R.id.type);
//		TextView titleView = (TextView) view.findViewById(R.id.name);
//		TextView subjectNameView = (TextView) view
//				.findViewById(R.id.subject_name);
//		TextView ownerView = (TextView) view.findViewById(R.id.owner);
//		TextView groupView = (TextView) view.findViewById(R.id.group);
//
//		String type = cursor.getString(6);
//		String taskTypeId = cursor.getString(5);
//		Cursor c = this.context.getContentResolver().query(
//				CategoryEntity.CONTENT_URI, null,
//				"categoryid ='" + taskTypeId + "'", null, null);
//		while (c.moveToFirst()) {
//			String categoryColor = c.getString(4);
//			typeView.setBackgroundColor(Color.parseColor(categoryColor));
//			break;
//		}
//		c.close();
//
//		String subjectName = cursor.getString(4);
//		String owner = cursor.getString(10);
//
//		String userName = sharedPrefs.getString(Constants.PREF_USERNAME, "");
//		Cursor userCursor = context.getContentResolver().query(
//				UserEntity.CONTENT_URI, null, "name = '" + userName + "'",
//				null, null);
//		while (userCursor.moveToFirst()) {
//			currentUser = userCursor.getString(4);
//			break;
//		}
//		userCursor.close();
//
//		setViewText(titleView, cursor.getString(1));
//		setViewText(typeView, type);
//		setViewText(subjectNameView, subjectName);
//		if (currentUser.equals(owner)) {
//			setViewText(ownerView, "");
//		} else {
//			setViewText(ownerView, owner + "：");
//		}
//
//		/**
//		 * ====================================================
//		 */
//		String dueat = cursor.getString(8);
//		// Log.d(TAG, "dueat = "+dueat);
//		current = getGroup(dueat);
//		String previous = " ";
//		if (cursor.moveToPrevious()) {
//			previous = getGroup(cursor.getString(8));
//		}
//		Log.d(TAG, "current = " + current + ",preview = " + previous);
//
//		if (!previous.equals(current)) { // 当前联系人的sortKey！=上一个联系人的sortKey，说明当前联系人是新组。
//			groupView.setVisibility(View.VISIBLE);
//			groupView.setText(current);
//		} else {
//			groupView.setVisibility(View.GONE);
//		}
//	}

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
//	}
	
	class TaskViewHolder {
		TextView contentView;
		TextView groupView;
		TextView ownerView;
		TextView subjectView;
		TextView typeView;
	}

}
