package com.zhongcw.util;



import com.zhongcw.R;
import com.zhongcw.R.id;
import com.zhongcw.R.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;


public class TelephoneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tele);

		// init component
		TextView v_imei = (TextView) findViewById(R.id.imei);
		TextView v_tel = (TextView) findViewById(R.id.tel);
		TextView v_ICCID = (TextView) findViewById(R.id.ICCID);
		TextView v_imsi = (TextView) findViewById(R.id.imsi);

		// get tele info
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId(); // 取出IMEI
		String tel = tm.getLine1Number(); // 取出MSISDN，很可能为空
		String ICCID = tm.getSimSerialNumber(); // 取出ICCID
		String imsi = tm.getSubscriberId(); // 取出IMSI

		// set value
		v_imei.setText(imei);
		v_tel.setText(tel);
		v_ICCID.setText(ICCID);
		v_imsi.setText(imsi);

	}

}
