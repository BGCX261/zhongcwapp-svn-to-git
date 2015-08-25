package com.zhongcw.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.zhongcw.R;
import com.zhongcw.util.bitmap.BitmapUtil;

public class TestImageActivity extends Activity {
	private static final String TAG = "TestImageActivity";
	String imageUrl = "http://i.pbase.com/o6/92/229792/1/80199697.uAs58yHk.50pxCross_of_the_Knights_Templar_svg.png";
	Bitmap bmImg;
	ImageView imView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		imView = (ImageView) findViewById(R.id.imview);
		// imView.setImageBitmap(returnBitMap(imageUrl));
		Bitmap bitmap = returnBitMap(imageUrl);
		imView.setImageDrawable(BitmapUtil.resizeImage(bitmap, 20, 10));
	}

	public Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
