package com.zhongcw.activity;

import com.zhongcw.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class ColorActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//方式1：通过xml布局文件设置背景色
//		setContentView(R.layout.color);
		
		//方式2：通过API设置背景色
		/*LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		layout.setBackgroundResource(R.drawable.list_bg_blue);
		layout.setBackgroundColor(0xffe8f1f8);
		layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.list_bg_blue));*/
		
		//方式3：通过自定义View设置背景色
		setContentView(new ColorView(this));
	}
}
