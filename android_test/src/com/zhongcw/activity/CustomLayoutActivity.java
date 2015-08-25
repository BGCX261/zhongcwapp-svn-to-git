package com.zhongcw.activity;

import com.zhongcw.ui.layout.CustomLayout;

import android.app.Activity;
import android.os.Bundle;

public class CustomLayoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		CustomLayout layout = new CustomLayout(this);
		setContentView(layout);
	}
}
