package com.zhongcw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class TalkTimeView extends View {

	public TalkTimeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		int hour = 0;
		int minute = 0;
		int second = 0;

		while (true) {
			if (second == 3) {
				break;
			}
			second++;
			String text = hour + ":" + minute + ":" + second;
			float x = 30, y = 100;
			float textSize = 20f;
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setTextSize(textSize);
			canvas.drawText(text, x, y, paint);
		}

	}

}
