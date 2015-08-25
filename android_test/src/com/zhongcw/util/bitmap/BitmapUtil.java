package com.zhongcw.util.bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

/**
 * 位图工具类
 */
public class BitmapUtil {
	private static final String TAG = "BitmapUtil";

	/**
	 * 图片缩放
	 * 
	 * @param originalBitmap
	 *            原始图片
	 * @param w
	 *            新图片宽度
	 * @param h
	 *            新图片高度
	 * @return 转化后的新图片
	 */
	public static Drawable resizeImage(Bitmap originalBitmap, int w, int h) {
		// 获取原始图片的宽和高
		int width = originalBitmap.getWidth();
		int height = originalBitmap.getHeight();
		Log.d(TAG, "原始图片：宽 = " + width + "，高 = " + height);
		// 定义预转换成的图片的宽度和高度
		int newWidth = w;
		int newHeight = h;
		Log.d(TAG, "新图片：宽 = " + newWidth + "，高 = " + newHeight);
		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Log.d(TAG, "缩放率：宽 = " + scaleWidth + "，高 = " + scaleHeight);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width,
				height, matrix, true);
		// 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
		return new BitmapDrawable(resizedBitmap);
	}

}
