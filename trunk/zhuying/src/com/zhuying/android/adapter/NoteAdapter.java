package com.zhuying.android.adapter;

import com.zhuying.android.R;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * 记录适配器
 */
public class NoteAdapter extends SimpleCursorAdapter{

	public NoteAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		String update = cursor.getString(6);
		if(! TextUtils.isEmpty(update)){
			update = update.substring(0, 10);
		}
		String owner = cursor.getString(10);
		
		
		TextView dateView = (TextView) view.findViewById(R.id.record_date);
		dateView.setText(update);
		TextView personView =(TextView) view.findViewById(R.id.record_person);
		personView.setText("记录人："+owner);
	}
}
