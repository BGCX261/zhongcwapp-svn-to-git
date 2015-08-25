package com.zhuying.android.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.async.UITask;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.AuthorityEntity;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.GroupEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.service.CommonSyncService;
import com.zhuying.android.service.NoticeSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.Network;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.StringUtil;

/**
 * 设置
 */
public class SettingActivity extends Activity {
	private RelativeLayout about;
	private RelativeLayout update;
	private RelativeLayout suggest;
	private TextView currentUserView;
	private SharedPreferences sharedPrefs;
	private TextView logoutView;
	private Button syncBtn;
	private CheckBox wifi_sync;
	
	//版本升级
	public ProgressDialog pBar;
	private Handler handler = new Handler();
	private int newVerCode = 0;
	private String newVerName = "";
	Config config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initUI();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		
//		String ticketId = sharedPrefs.getString(Constants.PREF_TICKETID, null);
//		
//		NoticeSyncService service = new NoticeSyncService(SettingActivity.this);
//		service.syncNotice(ticketId);
		getSysNotice();
	}

	private void initUI() {
		//init Header
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("设置");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setVisibility(View.INVISIBLE);
		
		//init Widget
		syncBtn = (Button) findViewById(R.id.sync_btn);
		syncBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				new UITask(SettingActivity.this) {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						progressDialog.setMessage("同步中...");
					}
					
					@Override
					protected Result doInBackground(Void... params) {
						CommonSyncService common = new CommonSyncService(getApplicationContext());
						Result result =  common.sync();
						return result;
					}
					
					@Override
					protected void onPostExecute(Result result) {
						if (result.isSuccess()) {
						}else{
						}
						super.onPostExecute(result);
					}
				}.execute();
			
			}
		});
		
		about =  (RelativeLayout) findViewById(R.id.setting_about);
		about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(SettingActivity.this, AboutActivity.class);
				startActivity(i);
			}
		});
		
		update = (RelativeLayout) findViewById(R.id.setting_update);
		update.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(!NetworkStateUtil.checkNetworkInfo(SettingActivity.this)){
					
				}else{
					autoUpdate();
				}
			}
		});
		
		suggest = (RelativeLayout) findViewById(R.id.setting_suggest);
		suggest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(SettingActivity.this, SuggestActivity.class);
				startActivity(i);
			}
		});
		
		currentUserView = (TextView) findViewById(R.id.current_user);
		sharedPrefs = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		String userName = sharedPrefs.getString(Constants.PREF_USERNAME, "");
		currentUserView.setText("当前登录帐户："+userName);
		
		logoutView = (TextView) findViewById(R.id.logout);
		logoutView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(SettingActivity.this).setMessage("请您在退出登录前确保本地数据已与服务器端保持同步，否则会造成数据丢失\n确定退出登录？")
						.setCancelable(false).setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										//清除Pref
										sharedPrefs.edit().clear().commit(); 
										//删除表
										getContentResolver().delete(ContactEntity.CONTENT_URI, null, null);
										getContentResolver().delete(NoteEntity.CONTENT_URI, null, null);
										getContentResolver().delete(TaskEntity.CONTENT_URI, null, null);
										getContentResolver().delete(UserEntity.CONTENT_URI, null, null);
										getContentResolver().delete(GroupEntity.CONTENT_URI, null, null);
										getContentResolver().delete(ActionEntity.CONTENT_URI, null, null);
										getContentResolver().delete(CategoryEntity.CONTENT_URI, null, null);
										getContentResolver().delete(AuthorityEntity.CONTENT_URI, null, null);
										getContentResolver().delete(PhotoEntity.CONTENT_URI, null, null);
										
										Intent i = new Intent();
										i.setClass(getApplicationContext(), LoginActivity.class);
										startActivity(i);
										finish();
									}
								}).setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								}).create().show();
			}
		});
		
		wifi_sync = (CheckBox) findViewById(R.id.wifi_sync);
		wifi_sync.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					Constants.WIFI_AUTO_SYNC = true;
//					Toast.makeText(getApplicationContext(), ""+Constants.WIFI_AUTO_SYNC, Toast.LENGTH_SHORT).show();
				}else{
					Constants.WIFI_AUTO_SYNC = false;
//					Toast.makeText(getApplicationContext(), ""+Constants.WIFI_AUTO_SYNC, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	/**
	 * 自动升级
	 */
	public void autoUpdate() {
		config = new Config();
		if (getServerVerCode()) {
			int vercode = config.getVerCode();
			if (newVerCode > vercode) {
				doNewVersionUpdate();
			} else {
				notNewVersionShow();
			}
		}
	}
	
	public boolean getServerVerCode() {
		try {
			String verjson = Network.getInstance().getContent(
					Config.UPDATE_SERVER + Config.UPDATE_VERJSON);
			JSONArray array = new JSONArray(verjson);
			if (array.length() > 0) {
				JSONObject obj = array.getJSONObject(0);
				try {
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					newVerName = obj.getString("verName");
				} catch (Exception e) {
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private void doNewVersionUpdate() {
		int verCode = config.getVerCode();
		String verName = config.getVerName();
		StringBuffer sb = new StringBuffer();
		sb.append("发现新版本，是否更新？");
		Dialog dialog = new AlertDialog.Builder(SettingActivity.this).setTitle("软件更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(SettingActivity.this);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar
										.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								downFile(Config.UPDATE_SERVER
										+ Config.UPDATE_APKNAME);
							}
						}).setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								// finish();
							}
						}).create();// 创建
		// 显示对话框
		dialog.show();
	}
	
	void downFile(final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(Environment
								.getExternalStorageDirectory(),
								Config.UPDATE_SAVENAME);
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}

	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), Config.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	private void notNewVersionShow() {
		Toast.makeText(getApplicationContext(), "已经是最新版,不需要升级", Toast.LENGTH_LONG).show();
	}
	
	class Config {
		private static final String TAG = "Config";
		public static final String UPDATE_SERVER = "http://www.yewuben.com/";
		public static final String UPDATE_VERJSON = "yewuben.json";
		public static final String UPDATE_APKNAME = "yewuben.apk";
		public static final String UPDATE_SAVENAME = "yewuben_update.apk";
		public static final String PACKAGE = "com.zhuying.android";

		public int getVerCode() {
			int verCode = -1;
			try {
				verCode = getPackageManager().getPackageInfo(PACKAGE,
						0).versionCode;
			} catch (NameNotFoundException e) {
				Log.e(TAG, e.getMessage());
			}
			return verCode;
		}

		public String getVerName() {
			String verName = "";
			try {
				verName = getPackageManager().getPackageInfo(PACKAGE,
						0).versionName;
			} catch (NameNotFoundException e) {
				Log.e(TAG, e.getMessage());
			}
			return verName;
		}
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
	
//	private void getSysNotice() {
//		String ticketId = sharedPrefs.getString(Constants.PREF_TICKETID, null);
//		NoticeSyncService service = new NoticeSyncService(SettingActivity.this);
//		service.syncNotice(ticketId);
//	}
	
	private void getSysNotice() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String ticketId = sharedPrefs.getString(Constants.PREF_TICKETID, null);
				NoticeSyncService service = new NoticeSyncService(SettingActivity.this);
				String r = service.syncNotice(ticketId);
				return r;
			}
			
			protected void onPostExecute(String result) {
				if(!StringUtil.isEmpty(result)){
					showNoticeDialog(SettingActivity.this,result);
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
