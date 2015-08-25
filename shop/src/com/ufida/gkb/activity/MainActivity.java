package com.ufida.gkb.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

import com.ufida.gkb.R;
import com.ufida.gkb.util.Constants;
import com.ufida.gkb.util.DateTime;

public class MainActivity extends TabActivity {
	private TabHost tabHost;
	private static final String TAG = "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		String boothId = intent.getExtras().get("boothId").toString();
		Constants.boothId = boothId;
		
		tabHost = this.getTabHost();
		LayoutInflater.from(this).inflate(R.layout.main,
				tabHost.getTabContentView(), true);

		tabHost.addTab(tabHost
				.newTabSpec("index")
				.setIndicator("",
						getResources().getDrawable(R.drawable.tab_shouye))
				.setContent(new Intent(this, IndexActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("account")
				.setIndicator("",
						getResources().getDrawable(R.drawable.tab_kehu))
				.setContent(new Intent(this, AccountListActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("store")
				.setIndicator("",
						getResources().getDrawable(R.drawable.tab_kucun))
				.setContent(new Intent(this, StoreActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("report")
				.setIndicator("",
						getResources().getDrawable(R.drawable.tab_baobiao))
				.setContent(new Intent(this, ReportListActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("setting")
				.setIndicator("",
						getResources().getDrawable(R.drawable.tab_shezhi))
				.setContent(new Intent(this, SetActivity.class)));

		startTask();
	}

	private void startTask() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 19); // 7点
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		Date date = c.getTime();
		if (date.before(new Date())) {
			date = addDay(date, 1);
		}
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startNotification();
			}
		};
		long PERIOD_DAY = 24 * 60 * 60 * 1000;
		timer.schedule(task, date, PERIOD_DAY);
	}

	private Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

	private void startNotification() {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, MainActivity.class), 0);
		Notification notification = new Notification(R.drawable.icon, "旺铺通知",
				System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), "旺铺助手", "日报",
				contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;
		nm.notify(1, notification);
	}
}