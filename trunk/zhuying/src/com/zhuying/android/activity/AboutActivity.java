package com.zhuying.android.activity;

import com.zhuying.android.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		initUI();
	}
	
	private void initUI() {
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("关于");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setVisibility(View.INVISIBLE);
		Button left = (Button) findViewById(R.id.header_left_btn);
		left.setText("返回");
		left.setVisibility(View.VISIBLE);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
