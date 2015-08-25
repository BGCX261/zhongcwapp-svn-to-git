package com.zhongcw.activity;

import com.zhongcw.view.TalkTimeView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class TalkTimeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		TalkTimeView view = new TalkTimeView(getApplicationContext());
		setContentView(view);
	}

	private void updateUI() {

	}

}
