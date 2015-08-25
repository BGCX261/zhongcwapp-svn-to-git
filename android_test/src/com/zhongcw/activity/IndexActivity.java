package com.zhongcw.activity;

import com.zhongcw.R;
import com.zhongcw.view.HeaderBar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class IndexActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		setContentView(R.layout.index);
		
		Dialog dialog = new Dialog(IndexActivity.this,android.R.style.Theme_Panel);
		dialog.setContentView(R.layout.dialog);
		dialog.show();

//		LinearLayout layout = (LinearLayout) findViewById(R.id.index_layout);
//
//		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
//				LayoutParams.WRAP_CONTENT);
//		layout.addView(new HeaderBar(getApplicationContext()), lp);
	}
}
