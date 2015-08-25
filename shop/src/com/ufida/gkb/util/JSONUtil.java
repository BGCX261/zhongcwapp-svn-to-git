package com.ufida.gkb.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.ufida.gkb.entity.IJSONEntity;

public class JSONUtil {
	public static String toJSON(List<? extends IJSONEntity> obj) {
		JSONArray array = new JSONArray();
		for (IJSONEntity entity : obj)
			array.put(entity.toJSON());
		return array.toString();
	}

	public static JSONObject toObj(String json) {
		if (json == null)
			return null;
		try {
			return new JSONObject(json);
		} catch (Exception e) {
			Log.e("JSONUtil", e.getMessage());
			return null;
		}
	}
}
