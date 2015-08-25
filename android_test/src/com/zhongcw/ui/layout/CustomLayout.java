package com.zhongcw.ui.layout;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CustomLayout extends ViewGroup {
	private static final String TAG = "CustomLayout";

	public CustomLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		addChild(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onLayout(), left = " + l + ",top = " + t + ",right = " + r
				+ ",bottom=" + b);
		
		int width = 160;
		int height = 50;

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View v = getChildAt(i);
//			int width = v.getWidth();
//			int height = v.getHeight();
//			int mwidth = v.getMeasuredWidth();
			v.layout(l, t, l + width, t + height);
		}
	}

	private void addChild(Context context) {
		Log.d(TAG, "addChild()");
		int width = 100;
		int height = 30;

		Button btn = new Button(context);
		btn.setText("Layout Button");
//		btn.setWidth(width);
//		btn.setHeight(height);
		addView(btn);
//		addView(btn, new LayoutParams(width, height));
		// addView(btn, width, height);
	}

}
