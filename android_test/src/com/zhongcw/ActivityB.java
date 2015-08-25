package com.zhongcw;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ActivityB extends Activity {
	private static final String TAG = "ActivityB";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()...");
		
		setContentView(R.layout.tele);
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
}