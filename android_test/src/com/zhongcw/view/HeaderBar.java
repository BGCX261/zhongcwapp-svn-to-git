package com.zhongcw.view;

import com.zhongcw.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class HeaderBar extends View {

	public HeaderBar(Context context) {
		super(context);
	}

	public HeaderBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public HeaderBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = getWidth();
		int height = getHeight();
		
//		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
//				R.drawable.headerbar);
//		float left = 0;
//		float top = 0;
//		Paint paint = new Paint();
//		canvas.drawBitmap(bitmap, left, top, paint);
		
//		setBackgroundColor(Color.BLUE);
		
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawRect(10, 20, width, height, paint);
	}

}
