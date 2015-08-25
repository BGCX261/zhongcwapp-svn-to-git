package com.zhongcw.activity;

import com.zhongcw.ActivityB;
import com.zhongcw.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MyActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
//		String t = android.provider.Settings.System.getString(
//				getContentResolver(), "android_id");
//		Log.i("huilurry", "android_id=" + t);
		huilurry();
		 
		Button btn = (Button) findViewById(R.id.btn_hello);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(getApplicationContext(),
						ActivityB.class));
			}
		});
		
		
//		throw new NullPointerException("is null");

	}

	HandlerThread localHandlerThread;
	Handler handler;

	private void huilurry() {
		localHandlerThread = new HandlerThread("huilurry");
		localHandlerThread.start();
		handler = new Handler(localHandlerThread.getLooper());
		Thread
				.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(getApplicationContext()));
	}
}
