package com.zhongcw.activity;

import java.util.ArrayList;
import java.util.List;

import com.zhongcw.R;
import com.zhongcw.adapter.MyAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CustomListActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.custom_list);

		doCustomList();
	}

	private void doList() {
		List list = new ArrayList();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("d");

		ListAdapter adapter = new ArrayAdapter(getApplicationContext(),
				R.layout.custom_list_item, list);
		setListAdapter(adapter);
	}

	private void doCustomList() {
		ListView listView = getListView();
		
		MyAdapter adapter = new MyAdapter(getApplicationContext());
		listView.setAdapter(adapter);
//		adapter.notifyDataSetChanged();
		

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(), position + "_" + id,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
