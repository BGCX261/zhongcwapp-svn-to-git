package com.zhongcw.activity;

import com.zhongcw.DatabaseHelper;
import com.zhongcw.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DBActivity extends Activity {
	private static final String TAG = "DBActivity";

	public ProgressDialog pBar;

	Handler h = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				pBar.cancel();
				break;
			default:
				break;
			}
		};
	};

	class mThread implements Runnable {
		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message m = new Message();
			m.what = 1;
			h.sendMessage(m);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// dbTest();

		setContentView(R.layout.main);

		pBar = new ProgressDialog(this);
		pBar.setTitle("正在同步客户");
		pBar.setMessage("请稍候...");
		pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pBar.show();

		new Thread(new mThread()).start();

		// try {
		// Thread.sleep(5000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// pBar.cancel();

		/**
		 * 
		 // 初始化一个进度条 ProgressDialog progressDialog = new ProgressDialog(this);
		 * // 设置标题 progressDialog.setTitle("标题"); // 设置内容
		 * progressDialog.setMessage("内容"); //
		 * 进度条分不确定（indeterminate=true）和确定（indeterminate=false）2种
		 * progressDialog.setIndeterminate(false); // 设置样式
		 * progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); //
		 * 最大值 progressDialog.setMax(100); // 第一进度条的位置
		 * progressDialog.incrementProgressBy(30); // 第二进度条的位置
		 * progressDialog.incrementSecondaryProgressBy(70); // 时间计算
		 * progressDialog.setCancelable(false); // 显示 progressDialog.show();
		 */
	}

	private void dbTest() {
		DatabaseHelper helper = new DatabaseHelper(getApplicationContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		long beginTime = System.currentTimeMillis();
		db.beginTransaction();
		try {
			String sql = "insert into zcw_user (_id) values (?)";
			SQLiteStatement insert = db.compileStatement(sql);
			for (int i = 0; i < 6000; i++) {
				insert.bindLong(1, i);
				insert.executeInsert();
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - beginTime;
		Log.d(TAG, "time = " + time);

	}
}
