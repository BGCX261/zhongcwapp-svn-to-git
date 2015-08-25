package com.ufida.gkb.activity;

import com.ufida.gkb.R;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class ReportListActivity extends ExpandableListActivity {
	ExpandableListAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.report_list);

		mAdapter = new MyExpandableListAdapter();
		setListAdapter(mAdapter);

		ExpandableListView elv = getExpandableListView();
		elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			public boolean onChildClick(ExpandableListView expandableListView,
					View view, int groupPosition, int childPosition, long id) {
				String selected = groupPosition + "_" + childPosition;
				Intent intent = new Intent();
				intent.putExtra("selectedID", selected);
				intent.setClass(ReportListActivity.this,
						ReportActivity.class);
				startActivity(intent);
				return false;
			}
		});
	}

	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
		private String[] groups = { "销售分析","客户分析" };
		private String[][] children = { { "销售金额分析", "毛利分析" },{ "客户销售额贡献", "客户毛利贡献" } };

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getChild(groupPosition, childPosition).toString());
			return textView;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public Object getGroup(int groupPosition) {
			return groups[groupPosition];
		}

		public int getGroupCount() {
			return groups.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());
			return textView;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int arg0, int arg1) {
			return true;
		}
	}

	public TextView getGenericView() {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 64);

		TextView textView = new TextView(ReportListActivity.this);
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		textView.setPadding(36, 0, 0, 0);
		textView.setTextColor(Color.BLACK);
		return textView;
	}
}
