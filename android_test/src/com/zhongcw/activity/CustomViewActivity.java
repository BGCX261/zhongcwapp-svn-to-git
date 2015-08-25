package com.zhongcw.activity;

import com.zhongcw.R;
import com.zhongcw.view.CustomView;

import android.app.Activity;
import android.os.Bundle;

public class CustomViewActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.custom_view);

		CustomView customView = new CustomView(getApplicationContext());
		setContentView(customView);
		
		customView.drawSomething();
		customView.invalidate();

	}
}
