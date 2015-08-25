package com.zhuying.android.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.util.Log;

import com.zhuying.android.async.Result;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.HttpProxy;
import com.zhuying.android.util.Md5;
import com.zhuying.android.util.MobiEncrptionUtil;

/**
 * 最近行动同步服务
 */
public class ActionSyncService {
	private static final String TAG = "ActionSyncService";
	private Context ctx;
	private SharedPreferences sharedPrefs;
	
	public ActionSyncService(Context context){
		this.ctx = context;
	}
	
	/**
	 * 最近行动同步接口
	 * @param ticketId 票据id
	 * @return
	 */
	public Result syncAction(String ticketId){
		String opUrl = LoginService.url + "/activitySync";

		Map bodyData = new HashMap();// 发送的业务数据
		sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		String pref_syncTime = sharedPrefs.getString(Constants.PREF_ACTION_SYNCTIME, "2010-10-01 00:00:00");
		bodyData.put("syncTime", pref_syncTime);
		
		//=================
		JSONArray add_ary = new JSONArray();
		Cursor addCursor = ctx.getContentResolver().query(ActionEntity.CONTENT_URI, null, "createdat > '"+ pref_syncTime+"'", null, null);
		for (addCursor.moveToFirst(); !addCursor.isAfterLast(); addCursor
				.moveToNext()) { //遍历
			ActionEntity entity = new ActionEntity(addCursor);
			JSONObject jsonObj = entity.toJSONObject();
			add_ary.put(jsonObj);
		}
		addCursor.close();
		bodyData.put("add", add_ary.toString());
		
		JSONArray update_ary = new JSONArray();
		Cursor updateCursor = ctx.getContentResolver().query(ActionEntity.CONTENT_URI, null, "updatedat > '" + pref_syncTime + "' and createdat < '" + pref_syncTime + "'", null, null);
		for (updateCursor.moveToFirst(); !updateCursor.isAfterLast(); updateCursor
				.moveToNext()) {
			ActionEntity entity = new ActionEntity(updateCursor);
			JSONObject jsonObj = entity.toJSONObject();
			update_ary.put(jsonObj);
		}
		updateCursor.close();
		bodyData.put("update", update_ary.toString());
		
		Cursor allCursor = ctx.getContentResolver().query(ActionEntity.CONTENT_URI, null, null, null, null);
		String allId = "";
		for (allCursor.moveToFirst(); !allCursor.isAfterLast(); allCursor
				.moveToNext()) {
			allId += allCursor.getString(15)+ ",";
		}
		allCursor.close();
		bodyData.put("checkAllId", allId);
		//=================
		
		String sign = "";
		try {
			sign = Md5.md5(LoginService.signKey + LoginService.sid + ticketId + new JSONObject(bodyData).toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Map postData = new HashMap();// 发送数据组装
		postData.put("sendTime", DateTimeUtil.format(new Date()));
		postData.put("sid", LoginService.sid);
		postData.put("ticketId", ticketId);
		postData.put("data", new JSONObject(bodyData).toString());
		postData.put("sign", sign);

		try {
			String data = new JSONObject(postData).toString();
			Log.d(TAG, "最近行动_Send:" + data);
			HttpProxy proxy = new HttpProxy(opUrl);
			proxy.setMethod("POST");
			proxy.setHeader("Content-Length", String
					.valueOf(data.getBytes().length));
			proxy.setBody(data);
			proxy.exec();
			Log.d(TAG,"最近行动_Recv:" + proxy.getResponseString());
			
			String response = proxy.getResponseString();
			JSONObject json = new JSONObject(response);
			String code = json.get("code").toString();
			String msg = json.get("msg").toString();
			
			if("0000".equals(code)){
				//存储同步时间到SharedPreferences
				String syncTime = DateTimeUtil.format(new Date());
				sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putString(Constants.PREF_ACTION_SYNCTIME,syncTime);
				editor.commit();
				
				String jdata=json.get("data").toString();
				JSONObject json_data = new JSONObject(jdata);
				String delId = json_data.get("delId").toString();
				String[] dels = delId.split(",");
				for (int i = 0; i < dels.length; i++) {
					String del = dels[i];
					ctx.getContentResolver().delete(ActionEntity.CONTENT_URI, "latestactivityid = '"+del+"'", null);
				}
				String add = json_data.get("add").toString();
				JSONArray addAry = new JSONArray(add);
				for (int i = 0; i < addAry.length(); i++) {
					JSONObject addItem = (JSONObject) addAry.get(i);
					ActionEntity action = new ActionEntity(addItem);
					ctx.getContentResolver().insert(ActionEntity.CONTENT_URI,
							action.toContentValues());
				}
				
				String update = json_data.get("update").toString();
				JSONArray updateAry = new JSONArray(update);
				for (int i = 0; i < updateAry.length(); i++) {
					JSONObject updateItem = (JSONObject) updateAry.get(i);
					String actionid = updateItem.getString("latestactivityid");
					ActionEntity action = new ActionEntity(updateItem);
					ctx.getContentResolver().update(ActionEntity.CONTENT_URI, action.toContentValues(), ActionEntity.KEY_LATESTACTIVITYID + " = '" + actionid +"'", null);
				}
				return Result.success("登录成功");
			}else{
				return Result.fail(msg);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Result.fail("最近行动同步错误");
	}
}
