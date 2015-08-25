package com.zhuying.android.activity;

import java.util.Date;

import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.async.UITask;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.service.ActionSyncService;
import com.zhuying.android.service.CommonSyncService;
import com.zhuying.android.service.CompanyContactSyncService;
import com.zhuying.android.service.LoginService;
import com.zhuying.android.service.NoteSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.UUIDUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 记录新增/编辑
 */
public class NoteEditActivity extends Activity {
	private String subjectId; // 关联对象id
	private String subjectType; // 关联对象类型
	private String subjectName; // 关联对象名称
	private String partyFace = "";

	private int mState; // 新增/编辑标识
	private static final int STATE_EDIT = 0;
	private static final int STATE_INSERT = 1;

	private NoteEntity entity;
	private Cursor mCursor;
	private Uri uri;
	private String id; // 主键
	Bundle bundle;

	// 界面元素
	private EditText bodyEditText;

	private String recordBody = "";

	private ActionEntity action = new ActionEntity();
	
	private ProgressDialog progressDialog;
	private TextView title;
	
	private SharedPreferences sharedPrefs;
	private String realName;
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.note_edit);
		initUI();

		Intent intent = getIntent();
		bundle = intent.getExtras();
		subjectId = bundle.get("subjectid").toString();
		subjectType = bundle.get("subjecttype").toString();
		subjectName = bundle.get("subjectname").toString();
		if(bundle.get("subjectface") != null){
			partyFace = bundle.get("subjectface").toString();
		}
		// id = b.get("id").toString();

		uri = intent.getData();
		String action = intent.getAction();
		if (Intent.ACTION_EDIT.equals(action)) {
			mState = STATE_EDIT;
		} else if (Intent.ACTION_INSERT.equals(action)) {
			mState = STATE_INSERT;
		}
		
		
		//获取当前登录人
		sharedPrefs = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		String userName = sharedPrefs.getString(Constants.PREF_USERNAME, "");
		Cursor userCursor = getContentResolver().query(
				UserEntity.CONTENT_URI, null, "name = '" + userName + "'",
				null, null);
		while (userCursor.moveToFirst()) {
			realName = userCursor.getString(4);
			userid = userCursor.getString(6);
			break;
		}
		userCursor.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		switch (mState) {
		case STATE_EDIT:
			title.setText("修改记录");
			// id = ContentUris.parseId(uri);
			id = bundle.get("id").toString();
			mCursor = managedQuery(NoteEntity.CONTENT_URI, null,
					NoteEntity.KEY_NOTEID + " = '" + id + "'", null, null);
			mCursor.moveToFirst();
			entity = new NoteEntity(mCursor);
			bodyEditText.setText(entity.getBody());
			bodyEditText.setSelection(entity.getBody().length()); // 把光标移动到文本最后
			break;
		case STATE_INSERT:
			title.setText("新增记录");
			entity = new NoteEntity();
			break;
		default:
			break;
		}
	}

	private void initUI() {
		// init Header
		title = (TextView) findViewById(R.id.header_title);
		
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setText("保存");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				recordBody = bodyEditText.getText().toString();
				if(TextUtils.isEmpty(recordBody)){
					Toast.makeText(getApplicationContext(), "记录内容不能为空", Toast.LENGTH_SHORT).show();
					return; 
				}

				String noteDueat = DateTimeUtil.format(new Date());
				entity.setBody(recordBody);
				entity.setSubjectId(subjectId);
				entity.setSubjectType(subjectType);
				entity.setSubjectName(subjectName);
				entity.setDueat(noteDueat);

				// 设置最近行动表字段值
				String actionDate = DateTimeUtil.format(new Date());
				action.setActivitytype(ActionEntity.TYPE_NOTE);
				action.setActivitycontent(recordBody);
				action.setSubjectid(subjectId);
				action.setSubjecttype(subjectType);
				action.setSubjectname(subjectName);
//				action.setSubjectface(entity.getSubjectface());
				action.setOwnerid(userid);
				action.setOwnername(realName);
				action.setUpdatedat(actionDate);
				action.setCreatedat(actionDate);
				action.setSubjectface(partyFace);
				action.setActivitydate(noteDueat);

				switch (mState) {
				case STATE_EDIT:
					String updateDate = DateTimeUtil.format(new Date());
					entity.setUpdatedat(updateDate);

					getContentResolver().update(NoteEntity.CONTENT_URI,
							entity.toContentValues(),
							NoteEntity.KEY_NOTEID + " = '" + id + "'", null);

					// 更新最近行动表
					Cursor actionCursor = managedQuery(ActionEntity.CONTENT_URI,null,ActionEntity.KEY_PARENTID + " = '"+ entity.getNoteid() + "'", null, null);
					while(actionCursor.moveToNext()){
						actionCursor.moveToFirst();
						ActionEntity actioncur = new ActionEntity(actionCursor);
						actionCursor.close();
						actioncur.setActivitycontent(entity.getBody());
						actioncur.setUpdatedat(DateTimeUtil.format(new Date()));
						actioncur.setActivitydate(DateTimeUtil.format(new Date()));
						getContentResolver().update(ActionEntity.CONTENT_URI,actioncur.toContentValues(),ActionEntity.KEY_PARENTID + " = '"+ entity.getNoteid() + "'", null);
					}

					wifiAutoSync();
//					finish();
					break;
				case STATE_INSERT:
					entity.setNoteid(UUIDUtil.getUUID());
					String date = DateTimeUtil.format(new Date());
					entity.setCreatedat(date);
					entity.setUpdatedat(date);
					entity.setVisibleto("OWNER");

					SharedPreferences sharedPrefs = getSharedPreferences(
							Constants.PREF, Context.MODE_PRIVATE);
					String name = sharedPrefs.getString(
							Constants.PREF_USERNAME, "");
					Cursor userCursor = getContentResolver().query(
							UserEntity.CONTENT_URI, null,
							"name = '" + name + "'", null, null);
					userCursor.moveToFirst();
					String realname = userCursor.getString(4);
					String userid = userCursor.getString(6);
					userCursor.close();

					entity.setOwnerName(realname);
					entity.setOwnerid(userid);

					Uri recordUri = getContentResolver().insert(
							NoteEntity.CONTENT_URI, entity.toContentValues());
//					long recordId = ContentUris.parseId(recordUri);
//					action.setParentid(String.valueOf(recordId));
					action.setParentid(entity.getNoteid());
					action.setLatestactivityid(UUIDUtil.getUUID());

					getContentResolver().insert(ActionEntity.CONTENT_URI,
							action.toContentValues());
					wifiAutoSync();
//					finish();
					break;
				default:
					break;
				}
			}
		});
		Button left = (Button) findViewById(R.id.header_left_btn);
		left.setText("返回");
		left.setVisibility(View.VISIBLE);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 初始化界面元素
		bodyEditText = (EditText) findViewById(R.id.record_body);
		bodyEditText.requestFocus();
	}

	/**
	 * Wifi下自动同步
	 */
	private void wifiAutoSync() {
		if (Constants.WIFI_AUTO_SYNC) { // wifi下自动同步打开
//			NetworkStateUtil net = new NetworkStateUtil(getApplicationContext());
			if (NetworkStateUtil.checkWifi(getApplicationContext())) { // wifi可用
				new WifiSyncTask().execute();
			}else{
				finish();
			}
		}else{
			finish();
		}
	}

	private class WifiSyncTask extends AsyncTask<Void, Void, Result> {
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			pDialog = ProgressDialog.show(NoteEditActivity.this, "", "记录同步中...", true, true);
		}

		@Override
		protected Result doInBackground(Void... params) {
			NoteSyncService service = new NoteSyncService(
					getApplicationContext());
			SharedPreferences pref = getApplicationContext()
					.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
			String ticketId = pref.getString(Constants.PREF_TICKETID, null);
			Result result = service.syncNote(ticketId);
			
			ActionSyncService actionS = new ActionSyncService(
					getApplicationContext());
			result = actionS.syncAction(ticketId);
			return result;
		}

		@Override
		protected void onPostExecute(Result result) {
			pDialog.dismiss();
			
			finish();
		}
	}

}
