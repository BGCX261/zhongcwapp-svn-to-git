package com.zhongcw.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

public class ColorView extends View{

	public ColorView(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		
		Typeface fontd = Typeface.create(Typeface.DEFAULT, Typeface.BOLD);
		paint = new Paint(3);
		paint.setTypeface(fontd);
		paint.setAntiAlias(true);
		paint.setTypeface(fontd);
		paint.setStyle(Paint.Style.FILL);
		
		paint.setColor(0xffe8f1f8);
		canvas.drawRect(new RectF(0, 0, 300, 300), paint);
	}

}
