package com.ufida.gkb.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ufida.gkb.R;
import com.ufida.gkb.util.Constants;
import com.ufida.gkb.util.JSONUtil;
import com.ufida.gkb.util.Network;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private Button login;
	private EditText localMob;

	public static String[] BOOTHNAMEARRAY;
	public static String[] BOOTHIDARRAY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		localMob = (EditText) findViewById(R.id.localMob);

		login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Map map = new HashMap();
				map.put("xml", "<query type=\"bind\">" + "<item phone=\""
						+ localMob.getText() + "\" />" + "</query>");
				try {
					String response = Network.getInstance().post(
							"/chanjet/booth.do", map);
					JSONObject jsonObj = JSONUtil.toObj(response);
					String success = jsonObj.get("success").toString();
					if ("true".equals(success)) {
						JSONArray ary = jsonObj.getJSONArray("items");
						BOOTHNAMEARRAY = new String[ary.length()];
						BOOTHIDARRAY = new String[ary.length()];
						for (int i = 0; i < ary.length(); i++) {
							JSONObject json = (JSONObject) ary.get(i);
							String boothId = json.get("boothId").toString();
							// Constants.boothId = boothId;
							BOOTHIDARRAY[i] = boothId;
							String boothName = json.get("boothName").toString();
							BOOTHNAMEARRAY[i] = boothName;
						}

						// Toast.makeText(LoginActivity.this, "登录成功",
						// Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						// i.setClass(LoginActivity.this, MainActivity.class);
						intent.setClass(LoginActivity.this,
								ShopListActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(LoginActivity.this, "登录失败",
								Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(LoginActivity.this, "网络错误",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}