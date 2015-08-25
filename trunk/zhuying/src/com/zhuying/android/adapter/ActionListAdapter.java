package com.zhuying.android.adapter;

import java.util.Date;

import com.zhuying.android.R;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.StringUtil;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ActionListAdapter extends SimpleCursorAdapter {
	private static final String TAG = "ActionListAdapter";
	private String currentYear = DateTimeUtil.format_ymd(new Date()).substring(
			0, 4);
	// private String display = "";
	int mlayout;
	private LayoutInflater inflater;
	private Context context;

	public ActionListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);

		mlayout = layout;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);

		// view.setBackgroundResource(R.drawable.listview_item_selector);

		TextView dateView = (TextView) view.findViewById(R.id.date);
		TextView contentView = (TextView) view.findViewById(R.id.name);
		TextView subjectNameView = (TextView) view
				.findViewById(R.id.subjectName);
		subjectNameView.getPaint().setFakeBoldText(true); // 粗体
		ImageView typeImage = (ImageView) view
				.findViewById(R.id.action_type_img);
		ImageView rightArrowImage = (ImageView) view
				.findViewById(R.id.right_arrow);
		TextView recordPersonView = (TextView) view
				.findViewById(R.id.record_person);
		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.action_layout);
		// String date = cursor.getString(14);
		String date = cursor.getString(12);
		String subjectName = cursor.getString(8);
		String display = "";
		if (!TextUtils.isEmpty(date)) {
			String year = date.substring(0, 4);
			display = date.substring(0, 10);
			if (currentYear.equals(year)) {
				display = date.substring(5, 10);
			}
		}

		String content = cursor.getString(2);
		setViewText(dateView, display);
		setViewText(contentView, content);
		setViewText(subjectNameView, subjectName);

		String activityType = cursor.getString(1);
		String activityStatus = cursor.getString(3);
		String subjectType = cursor.getString(7);
		String subjectFace = cursor.getString(9);
		String ownerName = cursor.getString(11);
		
//		Log.d(TAG, "subjectName = "+subjectName+",subjectFace = "+subjectFace);
		
		if (ActionEntity.TYPE_NOTE.equals(activityType)) { // 记录
			layout.setBackgroundResource(R.drawable.white);
			subjectNameView.setVisibility(View.VISIBLE);
			if ("contact".equals(subjectType)) {
				if (!StringUtil.isEmpty(subjectFace)) {
					Cursor photoCursor = context.getContentResolver().query(
							PhotoEntity.CONTENT_URI, null,
							PhotoEntity.KEY_NAME + " = '" + subjectFace + "'",
							null, null);
					if (photoCursor.moveToFirst()) {
						String face = photoCursor.getString(4);
						byte[] data = Base64.decode(face, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						typeImage.setImageBitmap(scaleImg(icon));
					}else{
						Drawable d = context.getResources().getDrawable(
								R.drawable.contact_icon);
						Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
						typeImage.setImageBitmap(scaleImg(bitmap));
					}
					photoCursor.close();
				} else {
					Drawable d = context.getResources().getDrawable(
							R.drawable.contact_icon);
					Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
					typeImage.setImageBitmap(scaleImg(bitmap));
				}
			} else if ("company".equals(subjectType)) {
				if (!StringUtil.isEmpty(subjectFace)) {
					Cursor photoCursor = context.getContentResolver().query(
							PhotoEntity.CONTENT_URI, null,
							PhotoEntity.KEY_NAME + " = '" + subjectFace + "'",
							null, null);
					if (photoCursor.moveToFirst()) {
						String face = photoCursor.getString(4);
						byte[] data = Base64.decode(face, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						typeImage.setImageBitmap(scaleImg(icon));
					}else {
						Drawable d = context.getResources().getDrawable(
								R.drawable.company_icon);
						Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
						typeImage.setImageBitmap(scaleImg(bitmap));
					}
					photoCursor.close();
				} else {
					Drawable d = context.getResources().getDrawable(
							R.drawable.company_icon);
					Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
					typeImage.setImageBitmap(scaleImg(bitmap));
				}
			} else if ("cases".equals(subjectType)) {
				Drawable d = context.getResources().getDrawable(
						R.drawable.action_type_case);
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				typeImage.setImageBitmap(scaleImg(bitmap));
			} else if ("deal".equals(subjectType)) {
				Drawable d = context.getResources().getDrawable(
						R.drawable.action_type_deal);
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				typeImage.setImageBitmap(scaleImg(bitmap));
			}

			rightArrowImage.setVisibility(View.VISIBLE);
			recordPersonView.setVisibility(View.VISIBLE);
			setViewText(recordPersonView, "记录人：" + cursor.getString(11));
		} else if ("comment".equals(activityType)) { // 批注
			layout.setBackgroundResource(R.drawable.white);
			subjectNameView.setVisibility(View.VISIBLE);

			// layout.setBackgroundResource(R.drawable.action_bg_comment);
			// Drawable d =
			// context.getResources().getDrawable(R.drawable.action_type_comment);
			// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
			// typeImage.setImageBitmap(bitmap);
			rightArrowImage.setVisibility(View.VISIBLE);
			recordPersonView.setVisibility(View.VISIBLE);
			setViewText(recordPersonView, "批注人：" + cursor.getString(11));

			if ("contact".equals(subjectType)) {
				if (!TextUtils.isEmpty(subjectFace)) {
					Cursor photoCursor = context.getContentResolver().query(
							PhotoEntity.CONTENT_URI, null,
							PhotoEntity.KEY_NAME + " = '" + subjectFace + "'",
							null, null);
					if (photoCursor.moveToFirst()) {
						String face = photoCursor.getString(4);
						byte[] data = Base64.decode(face, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						typeImage.setImageBitmap(scaleImg(icon));
					} else {
						Drawable d = context.getResources().getDrawable(
								R.drawable.contact_icon);
						Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
						typeImage.setImageBitmap(scaleImg(bitmap));
					}
					photoCursor.close();
				} else {
					Drawable d = context.getResources().getDrawable(
							R.drawable.contact_icon);
					Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
					typeImage.setImageBitmap(scaleImg(bitmap));
				}
			} else if ("company".equals(subjectType)) {
				if (!TextUtils.isEmpty(subjectFace)) {
					Cursor photoCursor = context.getContentResolver().query(
							PhotoEntity.CONTENT_URI, null,
							PhotoEntity.KEY_NAME + " = '" + subjectFace + "'",
							null, null);
					if (photoCursor.moveToFirst()) {
						String face = photoCursor.getString(4);
						byte[] data = Base64.decode(face, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0,
								data.length);
						typeImage.setImageBitmap(scaleImg(icon));
					}else {
						Drawable d = context.getResources().getDrawable(
								R.drawable.company_icon);
						Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
						typeImage.setImageBitmap(scaleImg(bitmap));
					}
					photoCursor.close();
				} else {
					Drawable d = context.getResources().getDrawable(
							R.drawable.company_icon);
					Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
					typeImage.setImageBitmap(scaleImg(bitmap));
				}
			}else if ("cases".equals(subjectType)) {
				Drawable d = context.getResources().getDrawable(
						R.drawable.action_type_case);
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				typeImage.setImageBitmap(scaleImg(bitmap));
			} else if ("deal".equals(subjectType)) {
				Drawable d = context.getResources().getDrawable(
						R.drawable.action_type_deal);
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				typeImage.setImageBitmap(scaleImg(bitmap));
			}
		} else if (ActionEntity.TYPE_TASK.equals(activityType)) { // 计划任务
			layout.setBackgroundResource(R.drawable.white);
			subjectNameView.setVisibility(View.GONE);

			Drawable d = context.getResources().getDrawable(
					R.drawable.action_type_plan);
			Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
			typeImage.setImageBitmap(scaleImg(bitmap));
			rightArrowImage.setVisibility(View.GONE);

			if ("finish".equals(activityStatus)) {
				// Log.d(TAG, "完成的计划任务："+content);
				CharSequence origin = contentView.getText();
				SpannableString span = new SpannableString(origin);
				span.setSpan(new StrikethroughSpan(), 0, origin.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				contentView.setText(span);
			}
			setViewText(recordPersonView, "负责人：" + cursor.getString(11));
		} else if ("deal".equals(activityType)) { // 销售机会
		// holder.layout.setBackgroundResource(R.drawable.action_bg);
			rightArrowImage.setVisibility(View.GONE);

			subjectNameView.setVisibility(View.GONE);

			if ("create".equals(activityStatus)) {
				Drawable d = context.getResources().getDrawable(
						R.drawable.action_type_deal_create);
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				typeImage.setImageBitmap(scaleImg(bitmap));

				setViewText(contentView, "销售机会：" + content);
				recordPersonView.setVisibility(View.VISIBLE);
				setViewText(recordPersonView, "创建人：" + ownerName);
			} else if ("pending".equals(activityStatus)) {
				Drawable d = context.getResources().getDrawable(
						R.drawable.action_type_deal_ing);
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				typeImage.setImageBitmap(scaleImg(bitmap));

				setViewText(contentView, "销售机会：" + content);
				recordPersonView.setVisibility(View.VISIBLE);
				setViewText(recordPersonView, "标识人：" + ownerName);
			} else if ("won".equals(activityStatus)) {
				layout.setBackgroundResource(R.drawable.action_bg_deal_win);

				Drawable d = context.getResources().getDrawable(
						R.drawable.action_type_deal_won);
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				typeImage.setImageBitmap(scaleImg(bitmap));

				setViewText(contentView, "销售机会：" + content);
				recordPersonView.setVisibility(View.VISIBLE);
				setViewText(recordPersonView, "标识人：" + ownerName);
			} else if ("lost".equals(activityStatus)) {
				layout.setBackgroundResource(R.drawable.action_bg_deal_lost);
				Drawable d = context.getResources().getDrawable(
						R.drawable.action_type_deal_lost);
				Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
				typeImage.setImageBitmap(scaleImg(bitmap));

				setViewText(contentView, "销售机会：" + content);
				recordPersonView.setVisibility(View.VISIBLE);
				setViewText(recordPersonView, "标识人：" + ownerName);
			}

		}
	}

	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder holder;
	// if (convertView == null) {
	// convertView = inflater.inflate(mlayout, null);
	// holder = new ViewHolder();
	// holder.date = (TextView) convertView.findViewById(R.id.date);
	// holder.name = (TextView) convertView.findViewById(R.id.name);
	// holder.subjectName = (TextView)
	// convertView.findViewById(R.id.subjectName);
	// holder.typeView = (ImageView)
	// convertView.findViewById(R.id.action_type_img);
	// holder.rightView = (ImageView)
	// convertView.findViewById(R.id.right_arrow);
	// holder.recordPerson = (TextView)
	// convertView.findViewById(R.id.record_person);
	// holder.actionLayout = (RelativeLayout)
	// convertView.findViewById(R.id.action_layout);
	//
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// convertView.setBackgroundResource(R.drawable.listview_item_selector);
	//
	// holder.subjectName.getPaint().setFakeBoldText(true); //粗体
	//
	// Cursor cursor = (Cursor) getItem(position);
	//
	// // String date = cursor.getString(14);
	// String date = cursor.getString(12);
	// String display ="";
	// if(! TextUtils.isEmpty(date)){
	// String year = date.substring(0, 4);
	// display = date.substring(0, 10);
	// if(currentYear.equals(year)){
	// display = date.substring(5, 10);
	// }
	// }
	//
	// String content = cursor.getString(2);
	// // setViewText(holder.date, display);
	// // setViewText(holder.name, content);
	// // setViewText(holder.subjectName, cursor.getString(8));
	// holder.date.setText(display);
	// holder.name.setText(content);
	// holder.subjectName.setText(cursor.getString(8));
	//
	// String activityType = cursor.getString(1);
	// String activityStatus = cursor.getString(3);
	// String subjectType = cursor.getString(7);
	// String subjectFace = cursor.getString(9);
	// String ownerName = cursor.getString(11);
	// if(ActionEntity.TYPE_NOTE.equals(activityType)){ //记录
	// holder.actionLayout.setBackgroundResource(R.drawable.white);
	// holder.name.setVisibility(View.VISIBLE);
	// if("contact".equals(subjectType)){
	// if(!StringUtil.isEmpty(subjectFace)){
	// Cursor photoCursor =
	// context.getContentResolver().query(PhotoEntity.CONTENT_URI, null,
	// PhotoEntity.KEY_NAME + " = '" + subjectFace +"'", null, null);
	// if(photoCursor.moveToFirst()){
	// String face = photoCursor.getString(4);
	// byte[] data = Base64.decode(face, Base64.DEFAULT);
	// Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
	// holder.typeView.setImageBitmap(scaleImg(icon));
	// }
	// photoCursor.close();
	// }else{
	// Drawable d = context.getResources().getDrawable(R.drawable.contact_icon);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	// }
	// }else if("company".equals(subjectType)){
	// if(!StringUtil.isEmpty(subjectFace)){
	// Cursor photoCursor =
	// context.getContentResolver().query(PhotoEntity.CONTENT_URI, null,
	// PhotoEntity.KEY_NAME + " = '" + subjectFace +"'", null, null);
	// if(photoCursor.moveToFirst()){
	// String face = photoCursor.getString(4);
	// byte[] data = Base64.decode(face, Base64.DEFAULT);
	// Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
	// holder.typeView.setImageBitmap(scaleImg(icon));
	// }
	// photoCursor.close();
	// }else{
	// Drawable d = context.getResources().getDrawable(R.drawable.company_icon);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	// }
	// }else if("cases".equals(subjectType)){
	// Drawable d =
	// context.getResources().getDrawable(R.drawable.action_type_case);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	// }else if("deal".equals(subjectType)){
	// Drawable d =
	// context.getResources().getDrawable(R.drawable.action_type_deal);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	// }
	//
	// holder.rightView.setVisibility(View.VISIBLE);
	// holder.recordPerson.setVisibility(View.VISIBLE);
	// // setViewText(holder.recordPerson, "记录人："+cursor.getString(11));
	// holder.recordPerson.setText("记录人："+cursor.getString(11));
	// }else if("comment".equals(activityType)){ //批注
	// holder.actionLayout.setBackgroundResource(R.drawable.white);
	// holder.subjectName.setVisibility(View.VISIBLE);
	//
	// // layout.setBackgroundResource(R.drawable.action_bg_comment);
	// // Drawable d =
	// context.getResources().getDrawable(R.drawable.action_type_comment);
	// // Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// // typeImage.setImageBitmap(bitmap);
	// holder.rightView.setVisibility(View.VISIBLE);
	// holder.recordPerson.setVisibility(View.VISIBLE);
	// // setViewText(holder.recordPerson, "批注人：" + cursor.getString(11));
	// holder.recordPerson.setText("批注人："+cursor.getString(11));
	//
	// if("contact".equals(subjectType)){
	// if(!TextUtils.isEmpty(subjectFace)){
	// Cursor photoCursor =
	// context.getContentResolver().query(PhotoEntity.CONTENT_URI, null,
	// PhotoEntity.KEY_NAME + " = '" + subjectFace +"'", null, null);
	// if(photoCursor.moveToFirst()){
	// String face = photoCursor.getString(4);
	// byte[] data = Base64.decode(face, Base64.DEFAULT);
	// Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
	// holder.typeView.setImageBitmap(scaleImg(icon));
	// }
	// photoCursor.close();
	// }else{
	// Drawable d = context.getResources().getDrawable(R.drawable.contact_icon);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	// }
	// }else if("company".equals(subjectType)){
	// if(!TextUtils.isEmpty(subjectFace)){
	// Cursor photoCursor =
	// context.getContentResolver().query(PhotoEntity.CONTENT_URI, null,
	// PhotoEntity.KEY_NAME + " = '" + subjectFace +"'", null, null);
	// if(photoCursor.moveToFirst()){
	// String face = photoCursor.getString(4);
	// byte[] data = Base64.decode(face, Base64.DEFAULT);
	// Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
	// holder.typeView.setImageBitmap(scaleImg(icon));
	// }
	// photoCursor.close();
	// }else{
	// Drawable d = context.getResources().getDrawable(R.drawable.company_icon);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	// }
	// }else{
	//
	// }
	// }else if(ActionEntity.TYPE_TASK.equals(activityType)){ //计划任务
	// holder.actionLayout.setBackgroundResource(R.drawable.white);
	// holder.subjectName.setVisibility(View.GONE);
	//
	// Drawable d =
	// context.getResources().getDrawable(R.drawable.action_type_plan);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	// holder.rightView.setVisibility(View.GONE);
	//
	// if("finish".equals(activityStatus)){
	// // Log.d(TAG, "完成的计划任务："+content);
	// CharSequence origin = holder.name.getText();
	// SpannableString span = new SpannableString(origin);
	// span.setSpan(new StrikethroughSpan(), 0,
	// origin.length(),
	// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	// holder.name.setText(span);
	// }
	// // setViewText(holder.recordPerson, "负责人：" + cursor.getString(11));
	// holder.recordPerson.setText("负责人："+cursor.getString(11));
	// }else if("deal".equals(activityType)){ //销售机会
	// // holder.layout.setBackgroundResource(R.drawable.action_bg);
	// holder.rightView.setVisibility(View.GONE);
	//
	// holder.subjectName.setVisibility(View.GONE);
	//
	// if("create".equals(activityStatus)){
	// Drawable d =
	// context.getResources().getDrawable(R.drawable.action_type_deal_create);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	//
	// // setViewText(holder.name, "销售机会：" + content);
	// holder.name.setText("销售机会：" + content);
	// holder.recordPerson.setVisibility(View.VISIBLE);
	// // setViewText(holder.recordPerson, "创建人：" + ownerName);
	// holder.recordPerson.setText("创建人："+cursor.getString(11));
	// }else if("pending".equals(activityStatus)){
	// Drawable d =
	// context.getResources().getDrawable(R.drawable.action_type_deal_ing);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	//
	// // setViewText(holder.name, "销售机会：" + content);
	// holder.name.setText("销售机会：" + content);
	// holder.recordPerson.setVisibility(View.VISIBLE);
	// // setViewText(holder.recordPerson, "标识人：" + ownerName);
	// holder.recordPerson.setText("标识人："+cursor.getString(11));
	// }else if("won".equals(activityStatus)){
	// holder.actionLayout.setBackgroundResource(R.drawable.action_bg_deal_win);
	//
	// Drawable d =
	// context.getResources().getDrawable(R.drawable.action_type_deal_won);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	//
	// // setViewText(holder.name, "销售机会：" + content);
	// holder.name.setText("销售机会：" + content);
	// holder.recordPerson.setVisibility(View.VISIBLE);
	// // setViewText(holder.recordPerson, "标识人：" + ownerName);
	// holder.recordPerson.setText("标识人："+cursor.getString(11));
	// }else if("lost".equals(activityStatus)){
	// holder.actionLayout.setBackgroundResource(R.drawable.action_bg_deal_lost);
	// Drawable d =
	// context.getResources().getDrawable(R.drawable.action_type_deal_lost);
	// Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
	// holder.typeView.setImageBitmap(scaleImg(bitmap));
	//
	// // setViewText(holder.name, "销售机会：" + content);
	// holder.name.setText("销售机会：" + content);
	// holder.recordPerson.setVisibility(View.VISIBLE);
	// // setViewText(holder.recordPerson, "标识人：" + ownerName);
	// holder.recordPerson.setText("标识人："+cursor.getString(11));
	// }
	//
	// }
	//
	// return convertView;
	// }

	private class ViewHolder {
		TextView date;
		TextView name;
		TextView subjectName;
		ImageView typeView;
		ImageView rightView;
		TextView recordPerson;
		RelativeLayout actionLayout;
	}

	/**
	 * 缩放图片
	 */
	private Bitmap scaleImg(Bitmap bm) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth = 64;
		int newHeight = 64;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);

		return newbm;
	}

	/**
	 * 自定义Adapter，通过复写getView方法，实现ListView中item背景颜色变化
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View localView = super.getView(position, convertView, parent);
		localView.setBackgroundResource(R.drawable.listview_item_selector);
		return localView;
	}

}
