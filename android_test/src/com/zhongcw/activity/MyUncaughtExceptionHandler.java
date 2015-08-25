package com.zhongcw.activity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MyUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {
	private Thread.UncaughtExceptionHandler a;
	private Context context;

	MyUncaughtExceptionHandler(Context ctx) {
		this.context = ctx;
		this.a = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.i("huilurry", "ppppppppppppp=" + ex.getMessage());
		Toast.makeText(context, "uncaughtException...", Toast.LENGTH_LONG)
				.show();
		// 是否抛出异常
		// if(a!=null)
		// a.uncaughtException(thread, ex);
	}
}
