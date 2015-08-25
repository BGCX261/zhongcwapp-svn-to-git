package com.zhongcw.android.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zhongcw.android.entity.ActivityEntity;
import com.zhongcw.android.util.JSONUtil;
import com.zhongcw.android.util.Network;

public class ActivityService {
	public JSONArray sync(String startTime, String endTime,
			List<ActivityEntity> activities) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		map.put("jsonObjectlist", JSONUtil.toJSON(activities));
		String response = Network.getInstance().post(
				"/activity/synActivity.do", map);
		JSONObject obj = JSONUtil.toObj(response);
		try {
			return obj.getJSONArray("list");
		} catch (Exception e) {
			return null;
		}
	}
}
