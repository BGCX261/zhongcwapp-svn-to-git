package com.zhongcw.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
	private static final String TAG = "MyService";

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, "onBind()...");
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate()");
		super.onCreate();
	}

}
