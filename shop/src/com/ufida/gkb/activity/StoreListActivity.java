package com.ufida.gkb.activity;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ufida.gkb.R;
import com.ufida.gkb.entity.StoreEntity;
import com.ufida.gkb.entity.StoreEntity.Model;
import com.ufida.gkb.service.AccountService;
import com.ufida.gkb.service.StoreService;

public class StoreListActivity extends ListActivity {
	private final String TAG = "StoreListActivity";

	private static final String[] PROJECTION = new String[] { Model._ID,
			Model.KEY_NUMBER, Model.KEY_NAME, Model.KEY_SPEC, Model.KEY_UNIT,
			Model.KEY_AMOUNT, Model.KEY_COST, Model.KEY_MONEY };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "URI is " + getIntent().getDataString());
		getWindow().addFlags(4194304);

		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String keyword = intent.getExtras().get("keyword").toString();
		
		fillViewWithData(keyword);

		setContentView(R.layout.store_list);
		this.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);

		

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			this.doSearch(intent);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			// this.doView(intent);
		} else {
			this.doList(intent);
		}

	}

	public void onNewIntent(final Intent intent) {
		super.onNewIntent(intent);
		final String queryAction = intent.getAction();
		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			this.doSearch(intent);
		} else if (Intent.ACTION_VIEW.equals(queryAction)) {
			// this.doView(intent);
		} else {
			this.doList(intent);
		}
		return;
	}

	private void doSearch(Intent intent) {
		String query = intent.getStringExtra(SearchManager.QUERY);
		String[] selectionArgs = new String[] { query };
		Uri uri = Uri.parse("content://" + Model.AUTHORITY + "/"
				+ SearchManager.SUGGEST_URI_PATH_QUERY);
		intent.setData(uri);

		ListView listView = getListView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Uri uri = ContentUris.withAppendedId(Model.CONTENT_URI, id);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});

		Cursor cursor = managedQuery(intent.getData(), PROJECTION, null,
				selectionArgs, Model.DEFAULT_SORT_ORDER);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.account_item, cursor, new String[] {
						SearchManager.SUGGEST_COLUMN_TEXT_1,
						SearchManager.SUGGEST_COLUMN_TEXT_2 }, new int[] {
						R.id.contactName, R.id.accountName });

		setListAdapter(adapter);
	}

	private void doView(Intent intent) {
		Uri uri = intent.getData();
		String action = intent.getAction();
		Intent i = new Intent(action);
		i.setData(uri);
		startActivity(i);
		this.finish();
	}

	private void doList(Intent intent) {
		intent.setData(Model.CONTENT_URI);
		ListView listView = getListView();
		// listView.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Uri uri = ContentUris.withAppendedId(Model.CONTENT_URI, id);
		// startActivity(new Intent(Intent.ACTION_VIEW, uri));
		// }
		// });
		Cursor cursor = managedQuery(intent.getData(), PROJECTION, null, null,
				null);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.store_item, cursor, new String[] { Model.KEY_NUMBER,
						Model.KEY_NAME, Model.KEY_SPEC, Model.KEY_UNIT,
						Model.KEY_AMOUNT, Model.KEY_COST, Model.KEY_MONEY },
				new int[] { R.id.inventoryCode, R.id.name, R.id.specification,
						R.id.unit, R.id.count, R.id.cost, R.id.amount });
		setListAdapter(adapter);
	}

	private void fillViewWithData(String para) {
		StoreService service = new StoreService(StoreListActivity.this);
		List list = service.getDataFromServer(para);
		service.saveDataToDB(list);
	}

}