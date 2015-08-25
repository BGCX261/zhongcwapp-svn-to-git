package com.ufida.gkb.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ufida.gkb.R;
import com.ufida.gkb.util.Constants;
import com.ufida.gkb.util.JSONUtil;
import com.ufida.gkb.util.Network;
import com.ufida.gkb.util.StringUtil;

public class IndexActivity extends Activity {
	private static final String TAG = "IndexActivity";

	private TextView todayAmount;
	private TextView monthAmount;
	private TextView todayProfit;
	private TextView monthProfit;
	private TextView todayOrder;
	private TextView monthOrder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "URI is " + getIntent().getDataString());
		getWindow().addFlags(4194304);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		initViewComponent();
		getDataFromServer();
	}

	private void initViewComponent() {
		todayAmount = (TextView) findViewById(R.id.todayAmount);
		monthAmount = (TextView) findViewById(R.id.monthAmount);
		todayProfit = (TextView) findViewById(R.id.todayProfit);
		monthProfit = (TextView) findViewById(R.id.monthProfit);
		todayOrder = (TextView) findViewById(R.id.todayOrder);
		monthOrder = (TextView) findViewById(R.id.monthOrder);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.index_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			getDataFromServer();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void getDataFromServer() {
		Map map = new HashMap();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String today = df.format(new Date());
		map.put("xml", "<query type=\"stat\">" + "<booth_id>"
				+ Constants.boothId + "</booth_id>" + "<date>" + today
				+ "</date>" + "</query>");
		try {
			String response = Network.getInstance().post("/chanjet/booth.do",
					map);
			JSONObject jsonObj = JSONUtil.toObj(response);
			String success = jsonObj.get("success").toString();
			if ("true".equals(success)) {
				JSONObject indexJSON = (JSONObject) jsonObj.getJSONArray(
						"items").get(0);

				String today_amount;
				if (indexJSON.has("today_amount")) {
					today_amount = indexJSON.get("today_amount").toString();
					today_amount = numberFormat(today_amount);
				} else {
					today_amount = "0.00";
				}

				String monthly_amount;
				if (indexJSON.has("monthly_amount")) {
					monthly_amount = indexJSON.get("monthly_amount").toString();
					monthly_amount = numberFormat(monthly_amount);
				} else {
					monthly_amount = "0.00";
				}

				String today_profit;
				if (indexJSON.has("today_profit")) {
					today_profit = indexJSON.get("today_profit").toString();
					today_profit = numberFormat(today_profit);
				} else {
					today_profit = "0.00";
				}
				String monthly_profit;
				if (indexJSON.has("monthly_profit")) {
					monthly_profit = indexJSON.get("monthly_profit").toString();
					monthly_profit = numberFormat(monthly_profit);
				} else {
					monthly_profit = "0.00";
				}
				// monthly_profit = monthly_profit.substring(0, 5);
				String today_order;
				if (indexJSON.has("today_order")) {
					today_order = indexJSON.get("today_order").toString();
				} else {
					today_order = "0";
				}
				String monthly_order;
				if (indexJSON.has("monthly_order")) {
					monthly_order = indexJSON.get("monthly_order").toString();
				} else {
					monthly_order = "0";
				}
				todayAmount.setText(today_amount);
				monthAmount.setText(monthly_amount);
				todayProfit.setText(today_profit);
				monthProfit.setText(monthly_profit);
				todayOrder.setText(today_order);
				monthOrder.setText(monthly_order);
			} else {
				Toast.makeText(IndexActivity.this, "同步失败", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(IndexActivity.this, "网络错误", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private String numberFormat(String number) {
		if (StringUtil.isEmpty(number)) {
			return "0.00";
		} else {
			DecimalFormat df = new DecimalFormat("#.00");
			df.setGroupingUsed(false);
			return df.format(Double.parseDouble(number));
		}

	}
}
