package com.zhongcw.activity;

import com.zhongcw.R;
import com.zhongcw.util.bitmap.BitmapUtil;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

public class BitmapActivity extends Activity {
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setTitle("eoeAndroid教程: 缩放和旋转图片 -by:IceskYsl");
		LinearLayout linLayout = new LinearLayout(this);
		// 加载需要操作的图片
		Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),
				R.drawable.bitmap_test);

		ImageView imageView = new ImageView(this);
		// 设置ImageView的图片为转换的图片
		imageView.setImageDrawable(BitmapUtil.resizeImage(bitmapOrg, 400, 650));
		// 将图片居中显示
		// imageView.setScaleType(ScaleType.CENTER);
		// 将ImageView添加到布局模板中
		linLayout.addView(imageView, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// 设置为本activity的模板
		setContentView(linLayout);
		
		
	}

}
