package com.zhuying.android.activity;

import java.util.Date;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.zhuying.android.R;
import com.zhuying.android.adapter.ActionListAdapter;
import com.zhuying.android.adapter.ActionListAdapterNew;
import com.zhuying.android.async.Result;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.service.ActionSyncService;
import com.zhuying.android.service.CommonSyncService;
import com.zhuying.android.service.CompanyContactSyncService;
import com.zhuying.android.service.NoticeSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.StringUtil;

/**
 * 最近行动
 */
public class ActionListActivity extends Activity implements IXListViewListener {
//	public PullToRefreshListView listView;
	private XListView mListView;
	
	private final static int EXIT_APP = 1;
	
	private String syncTime = "最后同步：";
	
	private static final String[] DEFAULT_PROJECTION = new String[] {
		ActionEntity._ID, 
		ActionEntity.KEY_ACTIVITYTYPE,
		ActionEntity.KEY_ACTIVITYCONTENT,
		ActionEntity.KEY_ACTIVITYSTATUS,
		ActionEntity.KEY_ACTIVITYCREATE,
		ActionEntity.KEY_PARENTID,
		ActionEntity.KEY_SUBJECTID,
		ActionEntity.KEY_SUBJECTTYPE,
		ActionEntity.KEY_SUBJECTNAME,
		ActionEntity.KEY_SUBJECTFACE,
		ActionEntity.KEY_OWNERID,
		ActionEntity.KEY_OWNERNAME,
		ActionEntity.KEY_ACTIVITYDATE,
		ActionEntity.KEY_UPDATEDAT,
		ActionEntity.KEY_CREATEDAT,
		" max(updatedat,createdat) as sortdat"
	};
	
	String sortOrder = " sortdat desc";
	
	private SharedPreferences sharedPrefs;
	String pref_syncTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.action_list);
		
		sharedPrefs = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		pref_syncTime = sharedPrefs.getString(Constants.PREF_ACTION_SYNCTIME, "2010-10-01 00:00:00");
		
		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(pref_syncTime);
		
//		LinearLayout emptyView = (LinearLayout) getLayoutInflater().inflate(R.layout.empty_action, null);
/*		LinearLayout emptyView = new LinearLayout(getApplicationContext());
		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		emptyView.setOrientation(LinearLayout.VERTICAL);
		
		TextView tv = new TextView(getApplicationContext());
		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		tv.setText("无数据...");
		
		((AdapterView<ListAdapter>) mListView.getParent()).addView(emptyView);
		mListView.setEmptyView(emptyView);*/
		
		View emptyView = findViewById(R.id.empty_list_view);
		mListView.setEmptyView(emptyView);
		
		
		initUI();

		doList();
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getSysNotice();
	}

	private void initUI() {
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("最近行动");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setVisibility(View.INVISIBLE);
	}

	private void doList() {
//		listView = (PullToRefreshListView) getListView();
//		listView.setOnRefreshListener(new OnRefreshListener() {
//			@Override
//			public void onRefresh() {
//				new GetDataTask().execute();
//			}
//		});
//		listView.setLastUpdated(syncTime + DateTimeUtil.format(new Date()));

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ActionListAdapter ada = (ActionListAdapter) getListAdapter();
				// ContentResolver cr = (ContentResolver) ada.getItem(position);
				// Cursor c = ada.getCursor();

				Cursor c = managedQuery(ActionEntity.CONTENT_URI, null,
						"_id = " + id, null, null);
				while (c.moveToFirst()) {
					String type = c.getString(1);
					// String name = c.getString(2);
					String parentId = c.getString(5);
					String subjectid = c.getString(6);
					String subjecttype = c.getString(7);
					String subjectname = c.getString(8);

					if (ActionEntity.TYPE_NOTE.equals(type)) {
						Uri uri = NoteEntity.CONTENT_URI;
						Intent i = new Intent();
						i.setAction(Intent.ACTION_VIEW);
						i.setData(uri);
						i.setType("vnd.android.cursor.item/vnd.zhuying.records");
						i.putExtra("id", parentId); // 主键（noteId）
						i.putExtra("subjectid", subjectid);
						i.putExtra("subjecttype", subjecttype);
						i.putExtra("subjectname", subjectname);
						i.putExtra("from", "actionlist");
						startActivity(i);
					} else if (ActionEntity.TYPE_COMMENT.equals(type)) { // 批注
						Intent i = new Intent();
						i.putExtra("id", parentId); // 主键（noteId）
						i.putExtra("subjectid", subjectid);
						i.putExtra("subjecttype", subjecttype);
						i.putExtra("subjectname", subjectname);
						i.setClass(ActionListActivity.this,
								CommentDetailActivity.class);
						startActivity(i);
					}
					// else if(ActionEntity.TYPE_TASK.equals(type)){
					// Intent i = new Intent();
					// i.setAction(Intent.ACTION_VIEW);
					// i.setData(PlanEntity.CONTENT_URI);
					// i.setType("vnd.android.cursor.item/vnd.zhuying.plans");
					// i.putExtra("id", parentId); //主键（taskid）
					// i.putExtra("subjectid", subjectid);
					// i.putExtra("subjecttype", subjecttype);
					// i.putExtra("subjectname", subjectname);
					// startActivity(i);
					// }else {
					//
					// }
					break;
				}
				c.close();
			}
		});
		Cursor cursor = managedQuery(ActionEntity.CONTENT_URI, DEFAULT_PROJECTION, null,
				null, sortOrder);
		String[] from = { ActionEntity.KEY_ACTIVITYCONTENT,
				ActionEntity.KEY_ACTIVITYTYPE };
		int[] to = { R.id.name, R.id.type };
		SimpleCursorAdapter a = new ActionListAdapter(this,
				R.layout.action_list_item, cursor, from, to);
//		setListAdapter(a);
		mListView.setAdapter(a);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			quitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void quitDialog() {
		AlertDialog ad = new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("确认退出")
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface Dialog,
									int whichButton) {
								// android.os.Process.killProcess(android.os.Process.myPid());
								forceStopPackage("com.zhuying.android");
							}
						}).create();
		ad.show();
	}

	private void forceStopPackage(String pkgName) {
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {
		@Override
		protected String[] doInBackground(Void... params) {
			CommonSyncService common = new CommonSyncService(getApplicationContext());
			Result result =  common.sync();
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
//			listView.onRefreshComplete();
//			listView.setLastUpdated(syncTime + DateTimeUtil.format(new Date()));
			
			onLoad();
			doList();
			
			super.onPostExecute(result);
		}
	}

	@Override
	public void onRefresh() {
		if(!NetworkStateUtil.checkNetworkInfo(ActionListActivity.this)){
			mListView.stopRefresh();
		}else{
			new GetDataTask().execute();
		}
	}

	@Override
	public void onLoadMore() {
		
	}
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.setRefreshTime(DateTimeUtil.format(new Date()));
	}
	
	private void getSysNotice() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String ticketId = sharedPrefs.getString(Constants.PREF_TICKETID, null);
				NoticeSyncService service = new NoticeSyncService(ActionListActivity.this);
				String r = service.syncNotice(ticketId);
				return r;
			}
			
			protected void onPostExecute(String result) {
				if(!StringUtil.isEmpty(result)){
					showNoticeDialog(ActionListActivity.this,result);
				}
			};
		}.execute();
	
	}
	
	/**
	 * 通知对话框
	 * @param ctx
	 */
	private void showNoticeDialog(Context ctx,String content){
		new AlertDialog.Builder(ctx).setTitle("提示").setMessage("系统通知：\n"+content)
				.setCancelable(false).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
							}
						}).create().show();
	}

}
