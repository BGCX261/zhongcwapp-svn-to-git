package com.zhongcw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class AutoUpdate extends Activity {
	private final static String tag = "AutoUpdate";
	public ProgressDialog pBar;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.update);
		Dialog dialog = new AlertDialog.Builder(AutoUpdate.this).setTitle("系统更新")
				.setMessage("发现新版本，请更新").setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						pBar = new ProgressDialog(AutoUpdate.this);
						pBar.setTitle("正在下载");
						pBar.setMessage("请稍候...");
						pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						downFile("http://booth.chanjet.com/android_test.apk");
					}
				}).setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				}).create();

		dialog.show();
	}

	void downFile(final String url) {
		Log.d(tag, "downFile()...");
		Log.d(tag, "url = "+url);
//		Toast.makeText(AutoUpdate.this, "downFile()...", Toast.LENGTH_SHORT).show();
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					Log.d(tag, "length = "+length);
//					Toast.makeText(AutoUpdate.this, "length = "+length, Toast.LENGTH_SHORT).show();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						
						File file = new File(Environment
								.getExternalStorageDirectory(), "android_test.apk");
						Log.d(tag, "file = "+file.getPath());
//						Toast.makeText(AutoUpdate.this, "file = "+file.getPath(), Toast.LENGTH_SHORT).show();
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
							}
						}
					}

					fileOutputStream.flush();

					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();

				} catch (ClientProtocolException e) {
					Log.d(tag, "ClientProtocolException = "+e.getMessage());
//					Toast.makeText(AutoUpdate.this, "ClientProtocolException...", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (IOException e) {
					Log.d(tag, "IOException = "+e.getMessage());
//					Toast.makeText(AutoUpdate.this, "IOException...", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		}.start();
	}

	void down() {
		Log.d(tag, "down()...");
//		Toast.makeText(AutoUpdate.this, "down()...", Toast.LENGTH_SHORT).show();
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}

	void update() {
		Log.d(tag, "update()...");
//		Toast.makeText(AutoUpdate.this, "update()...", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File("/sdcard/android_test.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

}