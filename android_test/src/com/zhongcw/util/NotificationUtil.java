package com.zhongcw.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * 
 * 状态栏通知工具类
 */
public class NotificationUtil {
	private Context context;

	public NotificationUtil(Context ctx) {
		this.context = ctx;
	}

	public void startNotification(PendingIntent pIntent, int icon,
			CharSequence ticker, CharSequence title, CharSequence content,
			int id) {
		NotificationManager nManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(icon, ticker, System
				.currentTimeMillis());
		notification.setLatestEventInfo(context, title, content, pIntent);
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.flags = Notification.FLAG_FOREGROUND_SERVICE;
		// notification.defaults |= Notification.DEFAULT_SOUND;
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		// notification.defaults |= Notification.DEFAULT_LIGHTS;
		nManager.notify(id, notification);
	}

}
