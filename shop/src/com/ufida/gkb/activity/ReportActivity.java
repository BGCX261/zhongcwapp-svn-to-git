package com.ufida.gkb.activity;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.ufida.gkb.R;
import com.ufida.gkb.util.Constants;
import com.ufida.gkb.util.DateTime;
import com.ufida.gkb.util.Network;

public class ReportActivity extends Activity {
	String selectedID;

	private ImageView chartImg;

	// private TextView statisticsText;
	// private Button startDateBtn;
	// private Button endDateBtn;
	// private Button queryBtn;
	//
	// private int start_Year;
	// private int start_Month;
	// private int start_Day;
	// private int end_Year;
	// private int end_Month;
	// private int end_Day;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.report);

		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		selectedID = bundle.getString("selectedID");

		// startDateBtn = (Button) findViewById(R.id.startDateBtn);
		// startDateBtn.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// showDialog(0);
		// }
		// });
		// endDateBtn = (Button) findViewById(R.id.endDateBtn);
		// endDateBtn.setOnClickListener(new View.OnClickListener() {
		// public void onClick(View v) {
		// showDialog(1);
		// }
		// });

		chartImg = (ImageView) findViewById(R.id.chartImg);
		// statisticsText = (TextView) findViewById(R.id.statisticsText);

		// ========
		// Calendar c = null;
		// c = Calendar.getInstance();
		// start_Year = c.get(Calendar.YEAR);
		// start_Month = 0; //一月
		// start_Day = 1; //一日
		// end_Year = c.get(Calendar.YEAR);
		// end_Month = c.get(Calendar.MONTH);
		// end_Day = c.get(Calendar.DAY_OF_MONTH);
		// set data and time
		// updateDisplay();

		displayChart();
		// ========

		// queryBtn = (Button) findViewById(R.id.queryBtn);
		// queryBtn.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// displayChart();
		// }
		// });
	}

	private void doReport(String chartType, String chartData) {
		try {
			String data = URLEncoder.encode(chartData, "UTF-8");
			String chartURL = "/jfreechart?chartType=" + chartType
					+ "&chartWidth=480&chartHeight=810&json=" + data;
			InputStream input = Network.getInstance().getStream(
					Constants.HOST_CHART, chartURL);
			// InputStream input =
			// Network.getInstance().getStream("http://10.5.31.137:9090" +
			// "/chart_wecoo", chartURL);
			Bitmap bm = BitmapFactory.decodeStream(input);
			chartImg.setImageBitmap(bm);
			// statisticsText.setText("客户类型分布统计建议: \n 您可以按类型对客户市场进行分割，获得相对较小的客户群体，有的放矢的进行市场活动。\n 您需要继续与主要类型客户\"投资商\"保持良好的跟进，以提高其忠诚度 ，同时关注最少的客户类型\"未知\"，以挖掘更大价值。 \n 您可以适当地调整贵公司的产品功能，来扩大对更多类型客户的吸引 ，您还可以进一步分析各客户类型成熟度。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private Map getChartDataFromCRM(String id) throws Exception {
	// Map<String, String> para = new HashMap<String, String>();
	// if (id.equals("0_0")) {
	// para.put("url", "getAccountType.do");
	// para.put("type", "pie");
	// } else if (id.equals("0_1")) {
	// para.put("url", "getAccountPhaseOfAccountType.do");
	// para.put("type", "bar");
	// } else if (id.equals("0_2")) {
	// para.put("url", "getAccountPhaseOfIndustry.do");
	// para.put("type", "bar");
	// } else if (id.equals("0_3")) {
	// para.put("url", "getAccountPhaseOfEmployees.do");
	// para.put("type", "bar");
	// } else if (id.equals("0_4")) {
	// para.put("url", "getAccountPhaseOfProvince.do");
	// para.put("type", "bar");
	// } else if (id.equals("0_5")) {
	// para.put("url", "getAccountCreateNum.do");
	// para.put("type", "line");
	// } else if (id.equals("1_0")) {
	// para.put("url", "getOpportunityNum.do");
	// para.put("type", "line");
	// } else if (id.equals("1_1")) {
	// para.put("url", "getFactAmountOfProduct.do");
	// para.put("type", "bar");
	// } else if (id.equals("1_2")) {
	// para.put("url", "getRationAndFactOfDept.do");
	// para.put("type", "bar");
	// } else if (id.equals("2_0")) {
	// para.put("url", "getActivityType.do");
	// para.put("type", "pie");
	// } else {
	// para.put("url", "getActivityNumOnDept.do");
	// para.put("type", "bar");
	// }
	//
	// return para;
	// }

	// @Override
	// protected Dialog onCreateDialog(int id) {
	// switch (id) {
	// case 0:
	// return new DatePickerDialog(this, sDateSetListener, start_Year,
	// start_Month, start_Day);
	// case 1:
	// return new DatePickerDialog(this, eDateSetListener, end_Year,
	// end_Month, end_Day);
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPrepareDialog(int id, Dialog dialog) {
	// switch (id) {
	// case 0:
	// ((DatePickerDialog) dialog).updateDate(start_Year, start_Month,
	// start_Day);
	// break;
	// case 1:
	// ((DatePickerDialog) dialog).updateDate(end_Year, end_Month,
	// end_Day);
	// break;
	// }
	// }

	// private DatePickerDialog.OnDateSetListener sDateSetListener = new
	// DatePickerDialog.OnDateSetListener() {
	// public void onDateSet(DatePicker view, int year, int monthOfYear,
	// int dayOfMonth) {
	// start_Year = year;
	// start_Month = monthOfYear;
	// start_Day = dayOfMonth;
	// updateDisplay();
	// }
	// };
	//
	// private DatePickerDialog.OnDateSetListener eDateSetListener = new
	// DatePickerDialog.OnDateSetListener() {
	// public void onDateSet(DatePicker view, int year, int monthOfYear,
	// int dayOfMonth) {
	// end_Year = year;
	// end_Month = monthOfYear;
	// end_Day = dayOfMonth;
	// updateDisplay();
	// }
	// };

	// private void updateDisplay() {
	// String sMonth;
	// if (start_Month + 1 < 10) {
	// sMonth = "0" + String.valueOf(start_Month + 1);
	// } else {
	// sMonth = String.valueOf(start_Month + 1);
	// }
	// startDateBtn.setText(new StringBuffer().append(start_Year).append("-")
	// .append(sMonth).append("-").append(DateTime.pad(start_Day)));
	//
	// String eMonth;
	// if (end_Month + 1 < 10) {
	// eMonth = "0" + String.valueOf(end_Month + 1);
	// } else {
	// eMonth = String.valueOf(end_Month + 1);
	// }
	// endDateBtn.setText(new StringBuffer().append(end_Year).append("-")
	// .append(eMonth).append("-").append(DateTime.pad(end_Day)));
	//
	// }

	private void displayChart() {
		try {
			// Map chartMap = getChartDataFromCRM(selectedID);
			// String url = "/report/" + chartMap.get("url").toString();
			// String chartType = chartMap.get("type").toString();
			//
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("startdt",
			// startDateBtn.getText().toString()+" 00:00:00");
			// map.put("enddt", endDateBtn.getText().toString() +" 23:59:59");
			// map.put("key2val", "1");
			// String response = Network.getInstance().post(url, map);

			// doReport(chartType, response);

			String pie = "{list:[{enum_value:'集团客户',num:20},{enum_value:'中小客户',num:70},{enum_value:'个人客户',num:10}]}";
			String bar = "{list:[{jsonData:[{enum_value:'分组A',line_0:11,line_1:22,line_2:33}],yFieldArr:[{label='潜在客户',data:'line_0'},{label='意向客户',data:'line_1'},{label='老客户',data:'line_2'}]}]}";
			
			String bar_single = "{list:[{enum_value:'集团客户',num:20},{enum_value:'中小客户',num:70},{enum_value:'个人客户',num:10}]}";
			String line = "{list:[{enum_value:'集团客户',num:20},{enum_value:'中小客户',num:70},{enum_value:'个人客户',num:10}]}";
			if (selectedID.equals("0_0")) {
				doReport("bar", bar_single);
			} 
			if (selectedID.equals("0_1")) {
				doReport("line", line);
			} 
			if (selectedID.equals("1_0")) {
				doReport("bar", bar);
			} 
			if (selectedID.equals("1_1")) {
				doReport("pie", pie);
			} 

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
