package com.zhongcw.android;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FormActivity extends Activity {
	private EditText contactNameEditText;
	private EditText accountNameEditText;
	private EditText noteEditText;
	private EditText phoneEditText;
	private EditText mobileEditText;
	private EditText websiteEditText;
	private EditText emailEditText;
	
	private Button mContactSaveButton;
	private Button cancelButton;
	
	String jsonobj;
	private DbAdapter mDbHelper;
	
	public static final int MENU_DELETE = 1;
	long rowId;
	
	String contactName = "";
	String accountName = "";
	String note = "";
	String phone = "";
	String mobile = "";
	String website = "";
	String email = "";
	
	Integer ownerUserId = 1;
	String updateTime;
	Integer isDel = 0;
	String createTime; 
	
	Integer ordId =1;
	Integer accountId = 0;
	
	Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);

		Intent intent = this.getIntent();
		bundle = intent.getExtras();
//		jsonobj = bundle.getString("jsonData");
		
		mDbHelper = new DbAdapter(this);
		
		if(bundle == null){
			//insert
		}else{
			//update
			rowId = bundle.getLong("rowid");
			mDbHelper.open();
			Cursor cursor = mDbHelper.fetch(rowId);
			mDbHelper.close();
			contactName = cursor.getString(1);
			accountName = cursor.getString(2);
			note = cursor.getString(3);
			phone = cursor.getString(4);
			mobile = cursor.getString(5);
			website = cursor.getString(6);
			email = cursor.getString(7);
		}
		
		
//		try {
//			JSONObject json = new JSONObject(jsonobj);
//			contactName = json.get("contactName").toString();
//			accountName = json.get("accountName").toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}

		contactNameEditText = (EditText) findViewById(R.id.contactNameEditText);
		contactNameEditText.setText(contactName);
		accountNameEditText = (EditText) findViewById(R.id.accountNameEditText);
		accountNameEditText.setText(accountName);
		noteEditText = (EditText) findViewById(R.id.noteEditText);
		noteEditText.setText(note);
		phoneEditText = (EditText) findViewById(R.id.phoneEditText);
		phoneEditText.setText(phone);
		mobileEditText = (EditText) findViewById(R.id.mobileEditText);
		mobileEditText.setText(mobile);
		websiteEditText = (EditText) findViewById(R.id.websiteEditText);
		websiteEditText.setText(website);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		emailEditText.setText(email);
		
		mContactSaveButton = (Button) findViewById(R.id.contactSaveButton);
		mContactSaveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				contactName = contactNameEditText.getText().toString();
				accountName = accountNameEditText.getText().toString();
				note = noteEditText.getText().toString();
				phone = phoneEditText.getText().toString();
				mobile = mobileEditText.getText().toString();
				website = websiteEditText.getText().toString();
				email = emailEditText.getText().toString();
				 
				if(bundle == null){
					createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					//insert
					mDbHelper.open();
					mDbHelper.insert(contactName, accountName, note, phone, mobile, website, email, ownerUserId, updateTime, isDel, createTime, ordId, accountId);
					mDbHelper.close();
				}else{
					updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					//update
					mDbHelper.open();
					mDbHelper.update(rowId, contactName, accountName, note, phone, mobile, website, email, ownerUserId, updateTime, isDel, createTime, ordId, accountId);
					mDbHelper.close();
				}
				
//				postData("jsonObject",jsonobj);
				
				Intent intent2  = new Intent();
//				intent2.putExtra("msg", accountName);
				setResult(RESULT_OK, intent2);
				finish();
			}
		});
		
		cancelButton = (Button) findViewById(R.id.cancelButton);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	/**
	 * 添加menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.form_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_delete: {
				//delete
				mDbHelper.open();
//				mDbHelper.delete(rowId);
				isDel = 1; //标志位，表示非物理删除
				updateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				mDbHelper.update(rowId, contactName, accountName, note, phone, mobile, website, email, ownerUserId, updateTime, isDel, createTime, ordId, accountId);
				mDbHelper.close();
				
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void postData(String key,String value){
		HttpClient httpclient = new DefaultHttpClient();
		String url = "http://10.5.30.151:6060/";
		HttpPost httppost = new HttpPost(url);
		try {
			httpclient.execute(httppost);
			
			String url2 = "http://10.5.30.151:6060/account/save.do";
			httppost = new HttpPost(url2);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair(key,value));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
