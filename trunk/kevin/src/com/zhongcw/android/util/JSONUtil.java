package com.zhongcw.android.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zhongcw.android.entity.IJSONEntity;

public class JSONUtil {
	public static String toJSON(List<? extends IJSONEntity> obj) {
		JSONArray array = new JSONArray();
		for (IJSONEntity entity : obj)
			array.put(entity.toJSON());
		return array.toString();
	}

	public static JSONObject toObj(String json) {
		try {
			return new JSONObject(json);
		} catch (Exception e) {
			// TODO:add log
			e.printStackTrace();
			return null;
		}
	}
}
