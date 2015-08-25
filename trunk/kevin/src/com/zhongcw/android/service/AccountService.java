package com.zhongcw.android.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zhongcw.android.entity.AccountEntity;
import com.zhongcw.android.entity.ActivityEntity;
import com.zhongcw.android.util.JSONUtil;
import com.zhongcw.android.util.Network;

public class AccountService {
	public JSONObject sync(String startTime,List<AccountEntity> activities) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("startTime", startTime);
		map.put("jsonObjectlist", JSONUtil.toJSON(activities));
		String response = Network.getInstance().post(
				"/account/synClientAccounts.do", map);
		JSONObject obj = JSONUtil.toObj(response);
		try {
//			return obj.getJSONArray("list");
			return obj;
		} catch (Exception e) {
			return null;
		}
	}
}
