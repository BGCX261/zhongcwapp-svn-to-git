package com.zhongcw.view;

import com.zhongcw.activity.DpPxActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义View。用于Canvas绘图时，px/dp转换
 */
public class DpPxView extends View {

	/**
	 * 构造器。用于Code构造
	 * 
	 * @param context
	 */
	public DpPxView(Context context) {
		super(context);
	}

	/**
	 * 构造器。用于XML构造
	 * 
	 * @param context
	 * @param attrs
	 */
	public DpPxView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int left_dp = 0;
		int top_dp = 0;
		int right_dp = 100;
		int bottom_dp = 100;

		int left_px = (int) (left_dp * DpPxActivity.scale);
		int top_px = (int) (top_dp * DpPxActivity.scale);
		int right_px = (int) (right_dp * DpPxActivity.scale);
		int bottom_px = (int) (bottom_dp * DpPxActivity.scale);
		
		Rect rect = new Rect(0, 0, 100, 100);
		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		canvas.drawRect(rect, paint);
	}

}
