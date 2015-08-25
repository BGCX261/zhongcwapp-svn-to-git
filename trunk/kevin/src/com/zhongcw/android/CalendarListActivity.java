package com.zhongcw.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class CalendarListActivity extends ListActivity {
	ListView listView;
	List list;
	private DbAdapterCalendar mDbHelper;
	private Bundle bundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = new DbAdapterCalendar(this);
		mDbHelper.open();
		
		Intent intent = this.getIntent();
		bundle = intent.getExtras();
		if(bundle == null){
			fillData(null);
		}else{
			String startTime = bundle.getString("startTime");
			fillData("startTime like'"+startTime+"%'");
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		String msg = data.getExtras().getString("msg");
	}
	
    private void fillData(String selection) {
        Cursor c = mDbHelper.fetchAll(selection);
        startManagingCursor(c);

        String[] from = new String[] { DbAdapterCalendar.KEY_SUBJECT};
        int[] to = new int[] { R.id.subject};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.calendar_list, c, from, to);
        setListAdapter(adapter);
        
        listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent();
						intent.setClass(CalendarListActivity.this,CalendarFormActivity.class);
						Bundle bundle = new Bundle();
						bundle.putLong("rowid", id);
						intent.putExtras(bundle);
						int requestCode = 2;
						startActivityForResult(intent, requestCode);
					}
				});
    }

}