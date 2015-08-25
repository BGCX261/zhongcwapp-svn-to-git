package com.ufida.gkb.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.ufida.gkb.activity.LoginActivity;
import com.ufida.gkb.activity.ShopListActivity;
import com.ufida.gkb.util.JSONUtil;
import com.ufida.gkb.util.Network;

public class LoginService {
	private Activity activity;
	private static final String TAG = "LoginService";

	public static String[] BOOTHNAMEARRAY;
	public static String[] BOOTHIDARRAY;

	public LoginService(Activity activity) {
		this.activity = activity;
	}

	public void doLogin(String phoneNumber) {
		Map map = new HashMap();
		map.put("xml", "<query type=\"bind\">" + "<item phone=\"" + phoneNumber
				+ "\" />" + "</query>");
		try {
			String response = Network.getInstance().post("/chanjet/booth.do",
					map);
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
				Toast.makeText(this.activity, "登录成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent();
				// i.setClass(LoginActivity.this, MainActivity.class);
				intent.setClass(this.activity, ShopListActivity.class);
				this.activity.startActivity(intent);
			} else {
				Toast.makeText(this.activity, "登录失败", Toast.LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this.activity, "网络错误", Toast.LENGTH_SHORT).show();
		}

	}

}
