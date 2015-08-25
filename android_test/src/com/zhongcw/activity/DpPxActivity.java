package com.zhongcw.activity;

import com.zhongcw.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * dp、px转换类，用于多屏幕适配
 */
public class DpPxActivity extends Activity{
	private static final String TAG = "DpPxActivity";
	public static float scale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		scale = getApplicationContext().getResources().getDisplayMetrics().density;
		Log.d(TAG, "scale = "+scale);
		
		setContentView(R.layout.dp_px);
	}
}
