package com.ufida.gkb.activity;

import com.ufida.gkb.R;
import com.ufida.gkb.util.Constants;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * 店铺选择界面
 */
public class ShopListActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_list);
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				R.layout.shop_list_item, LoginActivity.BOOTHNAMEARRAY);
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				i.putExtra("boothId", LoginActivity.BOOTHIDARRAY[position]);
				startActivity(i);
			}
		});
	}

}