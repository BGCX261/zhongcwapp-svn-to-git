package com.ufida.gkb.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ufida.gkb.R;

public class StoreActivity extends Activity {
	private Button queryBtn;
	private EditText keyword;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store);
		
		keyword = (EditText) findViewById(R.id.keyword);
		
		queryBtn = (Button) findViewById(R.id.btn_store_query);
		queryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String key_word = keyword.getText().toString();
				Intent intent = new Intent();
				
				intent.putExtra("keyword", key_word);
				
				intent.setClass(StoreActivity.this, StoreListActivity.class);
				startActivity(intent);
			}
		});
	}

}
