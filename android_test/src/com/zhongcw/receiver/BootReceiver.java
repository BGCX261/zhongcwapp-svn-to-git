package com.zhongcw.receiver;

import com.zhongcw.service.MyService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	private static final String TAG = "BootReceiver";
	
	public void onReceive(Context ctx, Intent intent) {
		Log.d(TAG, "onReceive()...");
		// // start activity
		// String action = "android.intent.action.MAIN";
		// String category = "android.intent.category.LAUNCHER";
		// Intent myi = new Intent(ctx, CustomDialog.class);
		// myi.setAction(action);
		// myi.addCategory(category);
		// myi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// ctx.startActivity(myi);

		// start service
		Intent s = new Intent(ctx, MyService.class);
		ctx.startService(s);
	}
}
