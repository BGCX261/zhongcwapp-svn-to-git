package com.ufida.gkb.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ufida.gkb.R;
import com.ufida.gkb.entity.AccountEntity;
import com.ufida.gkb.service.AccountService;
import com.ufida.gkb.ui.UITask;
import com.ufida.gkb.util.LoginStatus;
import com.ufida.gkb.util.Result;
import com.ufida.gkb.util.StringUtil;

public class AccountViewActivity extends Activity {
	private TextView contactNameEditText;
	private TextView accountNameEditText;
	private TextView phoneEditText;
	private TextView mobileEditText;
	private TextView websiteEditText;
	private TextView emailEditText;
	private TextView noteEditText;
	private TextView addressText;
	private AccountEntity entity;
	private AccountService service;

	private final String TAG = "AccountViewActivity";
	private Cursor mCursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "URI is " + getIntent().getDataString());

		if (LoginStatus.loginFlag) {
			getWindow().addFlags(4194304);// WindowManager.LayoutParams.FLAG_ROCKET_MENU_NOTIFY
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_card);

		mCursor = managedQuery(getIntent().getData(), null, null, null, null);
		service = new AccountService(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mCursor != null) {
			// Make sure we are at the one and only row in the cursor.
			mCursor.moveToFirst();
			entity = new AccountEntity(mCursor);
			contactNameEditText = (TextView) findViewById(R.id.contactName);
			contactNameEditText.setText(entity.getContactName());

			accountNameEditText = (TextView) findViewById(R.id.accountName);
			accountNameEditText.setText(entity.getAccountName());

			View phone_row = findViewById(R.id.phone_row);
			if (!StringUtil.isEmpty(entity.getPhone())) {
				phoneEditText = (TextView) findViewById(R.id.phone);
				phoneEditText.setText(entity.getPhone());
				phone_row.setVisibility(View.VISIBLE);
			} else {
				phone_row.setVisibility(View.GONE);
			}

			View mobile_row = findViewById(R.id.mobile_row);
			if (!StringUtil.isEmpty(entity.getMobile())) {
				mobileEditText = (TextView) findViewById(R.id.mobile);
				mobileEditText.setText(entity.getMobile());

				mobile_row.setVisibility(View.VISIBLE);
			} else {
				mobile_row.setVisibility(View.GONE);
			}

//			View email_row = findViewById(R.id.email_row);
//			if (!StringUtil.isEmpty(entity.getEmail())) {
//				emailEditText = (TextView) findViewById(R.id.email);
//				emailEditText.setText(entity.getEmail());
//				email_row.setVisibility(View.VISIBLE);
//			} else {
//				email_row.setVisibility(View.GONE);
//			}

//			View website_row = findViewById(R.id.website_row);
//			if (!StringUtil.isEmpty(entity.getWebsite())) {
//				websiteEditText = (TextView) findViewById(R.id.website);
//				websiteEditText.setText(entity.getWebsite());
//				website_row.setVisibility(View.VISIBLE);
//			} else {
//				website_row.setVisibility(View.GONE);
//			}

//			View note_row = findViewById(R.id.note_row);
//			if (!StringUtil.isEmpty(entity.getNote())) {
//				noteEditText = (TextView) note_row.findViewById(R.id.note);
//				noteEditText.setText(entity.getNote());
//				note_row.setVisibility(View.VISIBLE);
//			} else {
//				note_row.setVisibility(View.GONE);
//			}
			
//			View address_row = findViewById(R.id.address_row);
//			if (!StringUtil.isEmpty(entity.getAddress())) {
//				addressText = (TextView) address_row.findViewById(R.id.address);
//				addressText.setText(entity.getAddress());
////				addressText.setOnClickListener(new OnClickListener() {
////					@Override
////					public void onClick(View view) {
////						viewMap(view);
////					}
////				});
//				address_row.setVisibility(View.VISIBLE);
//			} else {
//				address_row.setVisibility(View.GONE);
//			}
			
		
		}
	}

//	/**
//	 * 添加menu
//	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		if (LoginStatus.loginFlag) {
//			MenuInflater inflater = getMenuInflater();
//			inflater.inflate(R.menu.account_view_menu, menu);
//		}
//		return true;
//	}

//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.menu_delete: {
//			new AlertDialog.Builder(this)
//					.setTitle("确认删除?")
//					.setCancelable(false)
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//									new UITask(AccountViewActivity.this) {
//
//										@Override
//										protected Result doInBackground(
//												Void... params) {
//											publishProgress(1);
//											Result ret = service.delete(entity
//													.getId());
//											if (!ret.isSuccess())
//												return ret;
//											publishProgress(2);
//											service.sync();
//											finish();
//											return ret;
//										}
//									}.execute();
//								}
//							})
//					.setNegativeButton("取消",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//									dialog.cancel();
//								}
//							}).create().show();
//			return true;
//		}
//		case R.id.menu_edit: {
//			Uri uri = getIntent().getData();
//			startActivity(new Intent(Intent.ACTION_EDIT, uri));
//			return true;
//		}
//		}
//		return super.onOptionsItemSelected(item);
//	}

	public void callPhone(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + entity.getPhone()));
		startActivity(intent);
	}

	public void callMobile(View view) {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + entity.getMobile()));
		startActivity(intent);
	}

	public void sendSMS(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.putExtra("address", entity.getMobile());
		intent.setType("vnd.android-dir/mms-sms");
		startActivity(intent);
	}

	public void sendEmail(View view) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] { entity.getEmail() });
		intent.setType("text/plain");
		// intent.setType("message/rfc822");
		startActivity(intent);
	}

	public void visitSite(View view) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(entity.getWebsite()));
		startActivity(intent);
	}

	public void viewActivity(View view) {
//		Intent intent = new Intent();
//		intent.setClass(AccountViewActivity.this, CalendarListActivity.class);
//		intent.setData(Uri.parse(Model.BASE_URI + "account/" + entity.getId()));
//		startActivity(intent);
	}

	public void viewContract(View view) {
//		Intent intent = new Intent();
//		intent.setClass(AccountViewActivity.this, ContractListActivity.class);
//		intent.setData(Uri
//				.parse(com.ufida.gkb.entity.ContractEntity.Model.BASE_URI
//						+ "account/" + entity.getId()));
//		startActivity(intent);
	}
	
	public void viewMap(View view) {
//		Uri uri = Uri.parse("http://maps.google.com/maps?f=d&daddr=beijing national library&hl=zh-CN");
		Uri uri = Uri.parse("http://maps.google.com/maps?f=d&daddr="+addressText.getText()+"&hl=zh-CN");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
}
