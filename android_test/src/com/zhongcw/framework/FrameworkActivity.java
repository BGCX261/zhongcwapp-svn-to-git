package com.zhongcw.framework;

import android.app.Activity;
import android.os.Bundle;

public abstract class FrameworkActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initView();
	}

	public abstract void initView();
}
