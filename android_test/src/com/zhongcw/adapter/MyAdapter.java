package com.zhongcw.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private Context context;
	private String[] datas = { "aaa", "bbb", "ccc" };

	public MyAdapter(Context c) {
		context = c;
	}

	@Override
	public int getCount() {
		return datas.length;
	}

	@Override
	public Object getItem(int position) {
		return datas[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
		if (convertView == null) {
			textView = new TextView(context);
			textView.setText(datas[position]);
			if (position % 2 == 0) {
				textView.setBackgroundColor(Color.RED);
			} else {
				textView.setBackgroundColor(Color.GREEN);
			}
		} else {
			textView = (TextView) convertView;
		}
		return textView;
	}
}
