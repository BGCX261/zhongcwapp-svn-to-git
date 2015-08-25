package com.zhuying.android.adapter;

import java.util.Date;

import com.zhuying.android.R;
import com.zhuying.android.adapter.TaskListAdapter.TaskViewHolder;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.util.DateTimeUtil;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ActionListAdapterNew extends SimpleCursorAdapter {
	private static final String TAG = "ActionListAdapterNew";
	
	private String currentYear = DateTimeUtil.format_ymd(new Date()).substring(0, 4);

	private Context context;
	private int layout;

	public ActionListAdapterNew(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);

		this.context = context;
		this.layout = layout;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ACtionViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layout, parent, false);
			
			holder = new ACtionViewHolder();
			holder.nameView = (TextView) convertView.findViewById(R.id.name);
			holder.subjectView = (TextView) convertView.findViewById(R.id.subjectName);
			holder.dateView = (TextView) convertView.findViewById(R.id.date);
			holder.imgView = (ImageView) convertView.findViewById(R.id.action_type_img);
			holder.rightArrowView = (ImageView) convertView.findViewById(R.id.right_arrow);
			holder.recordPersonView = (TextView) convertView.findViewById(R.id.record_person);
			convertView.setTag(holder);
		} else {
			holder = (ACtionViewHolder) convertView.getTag();
		}
		
		//get data
		Cursor cursor =  (Cursor) getItem(position);
		String content = cursor.getString(2);
		String subjectName = cursor.getString(8);
		
		String date = cursor.getString(14);
		String display ="";
		if(! TextUtils.isEmpty(date)){
			String year = date.substring(0, 4);
			display = date.substring(0, 10);
			if(currentYear.equals(year)){
				display =  date.substring(5, 10);
			}
		}
		String activityType = cursor.getString(1);
		String activityStatus = cursor.getString(3);
		String subjectType = cursor.getString(7);
		String subjectFace = cursor.getString(9);
		String ownerName = cursor.getString(11);
		
		//associate data to view
		holder.nameView.setText(content);
		
		holder.subjectView.getPaint().setFakeBoldText(true); //粗体
		holder.subjectView.setText(subjectName);
		
		holder.dateView.setText(display);
		
		if("note".equals(activityType)){
			 //记录
			if("contact".equals(subjectType)){
				if(!TextUtils.isEmpty(subjectFace)){
					Cursor photoCursor = context.getContentResolver().query(PhotoEntity.CONTENT_URI, null, PhotoEntity.KEY_NAME + " = '" + subjectFace +"'", null, null);
					if(photoCursor.moveToFirst()){
						String face = photoCursor.getString(4);
						byte[] data = Base64.decode(face, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
						holder.imgView.setImageBitmap(icon);
					}
					photoCursor.close();
				}else{
					Drawable d = context.getResources().getDrawable(R.drawable.contact_icon);
					Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
					holder.imgView.setImageBitmap(bitmap);
				}
			}else if("company".equals(subjectType)){
				if(!TextUtils.isEmpty(subjectFace)){
					Cursor photoCursor = context.getContentResolver().query(PhotoEntity.CONTENT_URI, null, PhotoEntity.KEY_NAME + " = '" + subjectFace +"'", null, null);
					if(photoCursor.moveToFirst()){
						String face = photoCursor.getString(4);
						byte[] data = Base64.decode(face, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
						holder.imgView.setImageBitmap(icon);
					}
					photoCursor.close();
				}else{
					Drawable d = context.getResources().getDrawable(R.drawable.company_icon);
					Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
					holder.imgView.setImageBitmap(bitmap);
				}
			}else if("cases".equals(subjectType)){
				Drawable d = context.getResources().getDrawable(R.drawable.action_type_case);
				Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
				holder.imgView.setImageBitmap(bitmap);
			}else if("deal".equals(subjectType)){
				Drawable d = context.getResources().getDrawable(R.drawable.action_type_deal);
				Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
				holder.imgView.setImageBitmap(bitmap);
			}
			
			holder.rightArrowView.setVisibility(View.VISIBLE);
			holder.recordPersonView.setVisibility(View.VISIBLE);
			holder.recordPersonView.setText("记录人：" + ownerName);
		}else if("comment".equals(activityType)){ //批注
			
			holder.rightArrowView.setVisibility(View.VISIBLE);
			holder.recordPersonView.setText("批注人：" + ownerName);
			
			if("contact".equals(subjectType)){
				if(!TextUtils.isEmpty(subjectFace)){
					Cursor photoCursor = context.getContentResolver().query(PhotoEntity.CONTENT_URI, null, PhotoEntity.KEY_NAME + " = '" + subjectFace +"'", null, null);
					if(photoCursor.moveToFirst()){
						String face = photoCursor.getString(4);
						byte[] data = Base64.decode(face, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
						holder.imgView.setImageBitmap(icon);
					}
					photoCursor.close();
				}else{
					Drawable d = context.getResources().getDrawable(R.drawable.contact_icon);
					Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
					holder.imgView.setImageBitmap(bitmap);
				}
			}else if("company".equals(subjectType)){
				if(!TextUtils.isEmpty(subjectFace)){
					Cursor photoCursor = context.getContentResolver().query(PhotoEntity.CONTENT_URI, null, PhotoEntity.KEY_NAME + " = '" + subjectFace +"'", null, null);
					if(photoCursor.moveToFirst()){
						String face = photoCursor.getString(4);
						byte[] data = Base64.decode(face, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
						holder.imgView.setImageBitmap(icon);
					}
					photoCursor.close();
				}else{
					Drawable d = context.getResources().getDrawable(R.drawable.company_icon);
					Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
					holder.imgView.setImageBitmap(bitmap);
				}
			}else{
				
			}
		}
		
		return convertView;
	}

	class ACtionViewHolder {
		TextView nameView;
		TextView subjectView;
		TextView dateView;
		ImageView imgView;
		ImageView rightArrowView;
		TextView recordPersonView;
	}
}
