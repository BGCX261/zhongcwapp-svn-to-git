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
import android.widget.Toast;

import com.ufida.gkb.entity.AccountEntity;
import com.ufida.gkb.entity.StoreEntity;
import com.ufida.gkb.entity.StoreEntity.Model;
import com.ufida.gkb.util.Constants;
import com.ufida.gkb.util.DateTime;
import com.ufida.gkb.util.JSONUtil;
import com.ufida.gkb.util.Network;
import com.ufida.gkb.util.Result;

public class StoreService {
	private Activity activity;
	private static final String TAG = "StoreService";

	public StoreService(Activity activity) {
		this.activity = activity;
	}

	public List getDataFromServer(String para) {
		List list = new ArrayList();
		Map map = new HashMap();
		map.put("xml", "<query type=\"product\">" + "<booth_id>"
				+ Constants.boothId + "</booth_id>"
				+ "<cond name=\""+ para +"\" code=\"\" id=\"\" />" + "</query>");
		try {
			String response = Network.getInstance().post("/chanjet/booth.do",
					map);
			JSONObject jsonObj = JSONUtil.toObj(response);
			String success = jsonObj.get("success").toString();
			if ("true".equals(success)) {
				JSONArray storeArray = jsonObj.getJSONArray("items");
				for (int i = 0; i < storeArray.length(); i++) {
					JSONObject json = (JSONObject) storeArray.get(i);
					StoreEntity store = new StoreEntity();
					store.setId(i + 1);
					store.setNumber(json.get("inventoryCode").toString());
					store.setName(json.get("name").toString());
					store.setSpec(json.get("specification").toString());
					store.setUnit(json.get("unit").toString());
					store.setAmount(json.get("count").toString());
					store.setCost(json.get("cost").toString());
					store.setMoney(json.get("amount").toString());
					list.add(store);
				}
			} else {
				Toast.makeText(activity, "同步失败", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void saveDataToDB(List list) {
		int num = activity.getContentResolver().delete(Model.CONTENT_URI, null, null);
		
		for (int i = 0; i < list.size(); i++) {
			StoreEntity entity = (StoreEntity) list.get(i);
			activity.getContentResolver().insert(Model.CONTENT_URI,
					entity.toContentValues());
		}
	}
}
