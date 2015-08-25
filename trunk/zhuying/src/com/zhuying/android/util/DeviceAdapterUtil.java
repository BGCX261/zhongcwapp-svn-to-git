package com.zhuying.android.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * 机型适配工具类
 */
public class DeviceAdapterUtil {

	/**
	 * DP 转换为 PX
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dip2px(Context context, float dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		int px = (int) (dip * scale);
		return px;
	}

	/**
	 * PX 转换为 DP
	 * 
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dip(Context context, float px) {
		float scale = context.getResources().getDisplayMetrics().density;
		int dip = (int) (px / scale);
		return dip;
	}

	public static float dp2Px(Activity activity, float dps) {
		DisplayMetrics metrics = new DisplayMetrics();
		float density = metrics.density;
		Log.i("dp2Pix", "<density>" + density);
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		density = metrics.density;
		Log.i("dp2Pix", "<density>" + density);
		float pixels = dps * density;
		return pixels;
	}
}
