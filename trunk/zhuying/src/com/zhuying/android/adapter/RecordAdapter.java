package com.zhuying.android.adapter;

import java.util.List;

import com.zhuying.android.R;
import com.zhuying.android.adapter.ContactListAdapter.ViewHolder;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 联系人明细记录列表适配器
 */
public class RecordAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<ContentValues> list;

	public RecordAdapter(Context context, List<ContentValues> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecordViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.note_list_item, null);
			holder = new RecordViewHolder();
			holder.star = (ImageView) convertView
					.findViewById(R.id.record_star);
			holder.date = (TextView) convertView.findViewById(R.id.record_date);
			holder.person = (TextView) convertView.findViewById(R.id.record_person);
			holder.content = (TextView) convertView.findViewById(R.id.record_content);
			convertView.setTag(holder);
		} else {
			holder = (RecordViewHolder) convertView.getTag();
		}

		final ContentValues cv = list.get(position);
		String date = cv.getAsString("date");
		holder.date.setText(date);
		String person = cv.getAsString("person");
		holder.person.setText(person);
		String content = cv.getAsString("content");
		holder.content.setText(content);

		return convertView;
	}

	class RecordViewHolder {
		ImageView star; // 星星
		TextView date;// 日期
		TextView person;// 记录人
		TextView content;// 内容
	}

}
