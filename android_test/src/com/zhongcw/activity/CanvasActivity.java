package com.zhongcw.activity;

import com.zhongcw.view.CanvasView;

import android.app.Activity;
import android.os.Bundle;

/**
 * Canvas学习
 */
public class CanvasActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CanvasView v = new CanvasView(this);
		setContentView(v);
	}
}
