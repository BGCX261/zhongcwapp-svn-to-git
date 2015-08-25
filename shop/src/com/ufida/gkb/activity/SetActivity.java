package com.ufida.gkb.activity;

import com.ufida.gkb.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class SetActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set);
	}

	/**
	 * 关于事件处理
	 */
	public void openAbout(View view) {
		Intent intent = new Intent(SetActivity.this, AboutActivity.class);
		startActivity(intent);
	}

	/**
	 * 登录店铺事件处理
	 */
	public void login(View view) {
		Intent intent = new Intent(SetActivity.this, ShopListActivity.class);
		startActivity(intent);
	}
}
