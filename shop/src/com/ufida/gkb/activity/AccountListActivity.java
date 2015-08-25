package com.ufida.gkb.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.ufida.gkb.R;
import com.ufida.gkb.entity.AccountEntity;
import com.ufida.gkb.entity.AccountEntity.Model;
import com.ufida.gkb.service.AccountService;
import com.ufida.gkb.test.ContactTest;
import com.ufida.gkb.test.TelephoneTest;
import com.ufida.gkb.util.LoginStatus;

public class AccountListActivity extends ListActivity {
	private final String TAG = "AccountListActivity";

	private static final String[] PROJECTION = new String[] { Model._ID,
			Model.KEY_CONTACTNAME, Model.KEY_ACCOUNTNAME };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "URI is " + getIntent().getDataString());
		getWindow().addFlags(4194304);// WindowManager.LayoutParams.FLAG_ROCKET_MENU_NOTIFY

		super.onCreate(savedInstanceState);

		fillViewWithData();

		setContentView(R.layout.account_list);
		this.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);

		Intent intent = getIntent();

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			this.doSearch(intent);
		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			this.doView(intent);
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
			this.doView(intent);
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
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Uri uri = ContentUris.withAppendedId(Model.CONTENT_URI, id);
				startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});
		Cursor cursor = managedQuery(intent.getData(), PROJECTION, null, null,
				"contactName DESC");

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.account_item, cursor, new String[] {
						Model.KEY_CONTACTNAME, Model.KEY_ACCOUNTNAME },
				new int[] { R.id.contactName, R.id.accountName });

		setListAdapter(adapter);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.account_list_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			onSearchRequested();
			break;
		case R.id.menu_account_refresh:
			fillViewWithData();
			break;
		case R.id.menu_writeContact:
			getContact();
			break;
		// case R.id.menu_phoneInfo:
		// getPhoneInfo();
		// break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getContact() {
		Cursor cur = managedQuery(getIntent().getData(), null, null, null, null);
		while (cur.moveToNext()) {
			String contactName = cur.getString(cur.getColumnIndex("contactName"));
//			String accountName = cur.getString(cur.getColumnIndex("accountName"));
			String mobile = cur.getString(cur.getColumnIndex("mobile"));
			writeContact(contactName, mobile);
		}
	}

	private void getPhoneInfo() {
		Intent i = new Intent();
		i.setClass(AccountListActivity.this, TelephoneTest.class);
		startActivity(i);
	}

	/**
	 * 写联系人
	 */
	private void writeContact(String name, String phone) {
		// ContentValues values = new ContentValues();
		// values.put(People.NAME, contactName); // 名称
		// values.put(Contacts.People.STARRED, 0);
		// Uri newPerson = Contacts.People.createPersonInMyContactsGroup(
		// getContentResolver(), values);
		//
		// Uri numberUri = null;
		// values.clear();
		// numberUri = Uri.withAppendedPath(newPerson,
		// People.Phones.CONTENT_DIRECTORY);
		// values.put(Contacts.Phones.TYPE, People.Phones.TYPE_MOBILE); // 手机
		// values.put(People.NUMBER, mobile);
		// getContentResolver().insert(numberUri, values);

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				.withValue(ContactsContract.RawContacts.AGGREGATION_MODE,
						ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED)
				.build());

		// add name
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
				.withValue(
						ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
						name).build());

		// add phone
		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)
				.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
						ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
				.build());

		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {

		}
	}

	private void fillViewWithData() {
		AccountService accountService = new AccountService(
				AccountListActivity.this);
		List list = accountService.getDataFromServer();
		accountService.saveToDB(list);

	}

}