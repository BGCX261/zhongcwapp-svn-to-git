package com.zhuying.android.activity;

import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.service.NoteSyncService;
import com.zhuying.android.service.SuggestSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.NetworkStateUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SuggestActivity extends Activity{
	EditText contentView;
	
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.suggest_edit);
		
		initUI();
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		SCREEN_WIDTH = metrics.widthPixels;
		SCREEN_HEIGHT = metrics.heightPixels;
	}
	
	private void initUI() {
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("反馈意见");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setVisibility(View.VISIBLE);
		right.setText("提交");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!NetworkStateUtil.checkNetworkInfo(SuggestActivity.this)){
					
				}else{
					SuggestSyncService service = new SuggestSyncService(
							getApplicationContext());
					SharedPreferences pref = getApplicationContext()
							.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
					String ticketId = pref.getString(Constants.PREF_TICKETID, null);
					Result result = service.syncSuggest(ticketId,contentView.getText().toString());
					finish();
				}
			}
		});
		Button left = (Button) findViewById(R.id.header_left_btn);
		left.setText("返回");
		left.setVisibility(View.VISIBLE);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		contentView = (EditText) findViewById(R.id.suggest_content);
	}
}
