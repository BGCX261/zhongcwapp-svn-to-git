package com.zhongcw.activity;

import com.zhongcw.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class GestureActivity extends Activity implements OnTouchListener,
		OnGestureListener {

	GestureDetector mGestureDetector;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		mGestureDetector = new GestureDetector(this);
		LinearLayout layout = (LinearLayout) findViewById(R.id.main_LinearLayout);
		layout.setOnTouchListener(this);
		layout.setLongClickable(true);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// Fling left
			Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// Fling right
			Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}