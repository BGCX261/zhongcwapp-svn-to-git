package com.zhongcw.view;

import com.zhongcw.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 自定义视图
 */
public class CustomView extends View implements View.OnClickListener {
	private static final String TAG = "CustomView";
	private Canvas mCanvas;

	public CustomView(Context context) {
		super(context);
		// new Thread(this).start();

		setOnClickListener(this);
	}

	public CustomView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mCanvas = canvas;

		// drawTextTest(canvas);
		drawBitmapTest(canvas);
		// translateTest(canvas);

		// Paint mPaint = new Paint();
		// mPaint.setColor(Color.GREEN);
		// canvas.drawRect(100, 200, 200, 300, mPaint);
		//
		// canvas.save();
		// canvas.rotate(45);
		// mPaint.setColor(Color.RED);
		// canvas.drawRect(150, 10, 200, 60, mPaint);
		// canvas.restore();
		
		
//		drawSomething();

	}

	private void translateTest(Canvas canvas) {
		float dx = 230;
		float dy = 0;
		canvas.translate(dx, dy);
	}

	/**
	 * 画文本
	 * 
	 * @param canvas
	 */
	private void drawTextTest(Canvas canvas) {
		String text = "绘图文本";
		float x = 30, y = 100;
		float textSize = 20f;
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(textSize);
		// paint.setStyle(style);
		canvas.drawText(text, x, y, paint);
	}

	/**
	 * 画图片
	 * 
	 * @param canvas
	 */
	private void drawBitmapTest(Canvas canvas) {
		float left = 0, top = 0;
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg);
//		int w = bitmap.getWidth();
//		int h = bitmap.getHeight();
//		Log.d(TAG, "width=" + w + ",height=" + h);

//		int x = 100;
//		int y = 10;
//		int width = 50;
//		int height = 20;
//		Bitmap subBitmap = Bitmap.createBitmap(bitmap, x, y, width, height);
		canvas.drawBitmap(bitmap, left, top, null);

		//

		// int sWidth = 20;
		// int sHeight = 20;
		//
		// // Rect src = null;
		// int sLeft = 0;
		// int sTop = 0;
		// int sRight = sLeft + sWidth;
		// int sBottom = sTop + sHeight;
		// Rect src = new Rect(sLeft, sTop, sRight, sBottom);
		//
		// int dWidth = 150;
		// int dHeight = 150;
		//
		// int dLeft = 50;
		// int dTop = 50;
		// int dRight = dLeft + dWidth;
		// int dBottom = dTop + dHeight;
		// Rect dst = new Rect(dLeft, dTop, dRight, dBottom);
		//
		// canvas.drawBitmap(bitmap, src, dst, null);

		// float left2 = left + width;
		// float top2 = top + height;;
		// bitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.tel);
		// canvas.drawBitmap(bitmap, left2, top, paint);

		// canvas.save();

		// float dx = 230;
		// float dy = 100;
		// canvas.translate(dx, dy);

		// float degrees = 40;
		// canvas.rotate(degrees);

		// canvas.restore();

		/*
		 * Bitmap bitmap_tel = BitmapFactory.decodeResource(getResources(),
		 * R.drawable.tel); // Rect src = new Rect(0, 0, 160, 180); Rect dst =
		 * new Rect(40, 60, 100, 200); canvas.drawBitmap(bitmap_tel, null, dst,
		 * paint);
		 */
	}

	@Override
	public void onClick(View v) {
		// v.setBackgroundDrawable(getResources().getDrawable(R.drawable.tel));
	}

	// @Override
	// public void run() {
	// while (!Thread.currentThread().isInterrupted()) {
	// try {
	// Thread.sleep(100);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// postInvalidate();
	// }
	// }
	
	public void drawSomething(){
		Paint p = new Paint();
		p.setColor(Color.RED);
		mCanvas.drawLine(40, 180, 400, 180, p);
	}
}
