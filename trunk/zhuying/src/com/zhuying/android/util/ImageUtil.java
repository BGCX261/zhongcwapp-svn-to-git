package com.zhuying.android.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageUtil {

	/**
	 * 图片缩放
	 * 
	 * @param bitmap
	 *            对象
	 * @param w
	 *            要缩放的宽度
	 * @param h
	 *            要缩放的高度
	 * @return newBmp 新 Bitmap对象
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newBmp;
	}

	/**
	 * Bitmap转Byte
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}
}
