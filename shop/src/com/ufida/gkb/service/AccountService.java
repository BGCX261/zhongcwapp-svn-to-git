package com.ufida.gkb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.ufida.gkb.activity.LoginActivity;
import com.ufida.gkb.entity.AccountEntity;
import com.ufida.gkb.entity.AccountEntity.Model;
import com.ufida.gkb.util.Constants;
import com.ufida.gkb.util.DateTime;
import com.ufida.gkb.util.JSONUtil;
import com.ufida.gkb.util.Network;
import com.ufida.gkb.util.Result;

public class AccountService {
	private Activity activity;
	private static final String TAG = "AccountService";

	public AccountService(Activity activity) {
		this.activity = activity;
	}

	public List getDataFromServer() {
		List list = new ArrayList();
		Map map = new HashMap();
		map.put("xml", "<query type=\"company\">" + "<booth_id>"
				+ Constants.boothId + "</booth_id>"
				+ "<after>20110214 12:50:01:11</after>" + "</query>");
		try {
			String response = Network.getInstance().post("/chanjet/booth.do",
					map);
			JSONObject jsonObj = JSONUtil.toObj(response);
			String success = jsonObj.get("success").toString();
			if ("true".equals(success)) {
				JSONArray accountArray = jsonObj.getJSONArray("items");
				for (int i = 0; i < accountArray.length(); i++) {
					JSONObject accountJSON = (JSONObject) accountArray.get(i);
					AccountEntity entity = new AccountEntity();
					entity.setId(i + 1);
					entity.setContactName(accountJSON.get("linkman").toString());
					entity.setAccountName(accountJSON.get("name").toString());
					entity.setPhone(accountJSON.get("tel1").toString());
					entity.setMobile(accountJSON.get("mobile1").toString());

					list.add(entity);
				}
			} else {
				Toast.makeText(activity, "同步失败", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public void saveToDB(List list) {
		try {
			int num = activity.getContentResolver().delete(Model.CONTENT_URI, null, null);
			
			for (int j = 0; j < list.size(); j++) {
				AccountEntity entity = (AccountEntity) list.get(j);
				activity.getContentResolver().insert(Model.CONTENT_URI,
						entity.toContentValues());
			}
		} catch (Exception e) {
		}
	}

}
