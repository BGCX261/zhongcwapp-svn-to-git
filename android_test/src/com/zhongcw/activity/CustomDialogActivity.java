package com.zhongcw.activity;

import com.zhongcw.ui.widget.dialog.CustomDialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class CustomDialogActivity extends Activity {
	CustomDialog customDialog;
	public Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		customDialog = new CustomDialog(this);
		customDialog.show();

		handler.postDelayed(new Runnable() {
			public void run() {
				customDialog.dismiss();
			}
		}, 5000);
	}
}
