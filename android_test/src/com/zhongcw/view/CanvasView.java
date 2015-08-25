package com.zhongcw.view;

import com.zhongcw.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class CanvasView extends View {

	public CanvasView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Bitmap bitmap = null; // 位图
		Rect src = null; // 位图子集矩形
		Rect dst = null; // 目标矩形
		Paint paint = null;

		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
		dst = new Rect(40, 80, 240, 480);
		src = new Rect(0, 0, 20, 30);
		
		// 自动缩放/转换位图填充目标矩形
		canvas.drawBitmap(bitmap, src, dst, paint);
	}

}
