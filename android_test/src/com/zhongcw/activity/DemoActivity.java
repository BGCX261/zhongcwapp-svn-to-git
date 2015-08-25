package com.zhongcw.activity;

import com.zhongcw.ActivityB;
import com.zhongcw.R;
import com.zhongcw.util.NotificationUtil;
import com.zhongcw.view.CustomView;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class DemoActivity extends Activity {
	private static final String TAG = "DemoActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		NotificationUtil n = new NotificationUtil(getApplicationContext());
//		PendingIntent pIntent = PendingIntent.getActivity(this, 0, new Intent(
//				this, ActivityB.class), 0);
//		n.startNotification(pIntent, R.drawable.icon, "ticker", "title",
//				"content", 0);

		Log.d(TAG, "onCreate()...");

		// 获取设备屏幕分辨率
//		Display display = getWindowManager().getDefaultDisplay();
//		int width = display.getWidth();
//		int height = display.getHeight();
//		Log.d(TAG, "width = " + width + " ,height = " + height);
//
//		DisplayMetrics dm = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(dm);
//		Log.d(TAG, "w = " + dm.widthPixels + " ,h = " + dm.heightPixels);
//		Log.d(TAG, dm.toString());

		
		startActivity(new Intent(getApplicationContext(), ActivityB.class));
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()...");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()...");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause()...");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop()...");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()...");
	}

	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demo_menu, menu);
		return true;

	}

	/**
	 * 选择菜单
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_quit:
			quitApp();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 退出应用程序
	 */
	private void quitApp() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		am.restartPackage(getPackageName());
		// am.killBackgroundProcesses(getPackageName());

		// Process.killProcess(Process.myPid());
		// System.exit(0);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}