package com.zhongcw.activity;

import com.zhongcw.R;
import com.zhongcw.view.HeaderBar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class LinearLayoutActivity extends Activity implements OnClickListener{
	HeaderBar head;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.linear);
		

		head = (HeaderBar) findViewById(R.id.cs);
		
//		head.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), "HeaderBar", Toast.LENGTH_SHORT)
//						.show();
////				head.setBackgroundColor(Color.GREEN);
//				head.setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbar));
//			}
//		});
		head.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.cs){
//			head.setBackgroundDrawable(getResources().getDrawable(R.drawable.headerbar));
			head.setBackgroundResource(R.drawable.headerbar);
		}
	}
}
