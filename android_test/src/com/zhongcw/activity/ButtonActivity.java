package com.zhongcw.activity;

import com.zhongcw.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ButtonActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.button);

//		final Button b = (Button) findViewById(R.id.day);
//		b.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				b.setBackgroundResource(R.drawable.btn_day_press);
//			}
//		});
	}
}
