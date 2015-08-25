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
import android.util.Log;

import com.zhuying.android.async.Result;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.GroupEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.HanziToPinyin;
import com.zhuying.android.util.HttpProxy;
import com.zhuying.android.util.Md5;
import com.zhuying.android.util.MobiEncrptionUtil;

/**
 * 组信息同步服务
 */
public class GroupSyncService {
	private static final String TAG = "GroupSyncService";
	private Context ctx;
	private SharedPreferences sharedPrefs;
	
	public GroupSyncService(Context context){
		this.ctx = context;
	}
	
	/**
	 * 基础数据：组信息同步接口
	 * @param ticketId 票据id
	 * @return
	 */
	public Result syncGroup(String ticketId){
		String opUrl = LoginService.url + "/groupItem";

		Map bodyData = new HashMap();// 发送的业务数据
		sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		String pref_syncTime = sharedPrefs.getString(Constants.PREF_GROUP_SYNCTIME, "2010-10-01 00:00:00");
		bodyData.put("syncTime", pref_syncTime);
		bodyData.put("groupId", "");
		
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
			Log.d(TAG, "组信息_Send:" + data);
			HttpProxy proxy = new HttpProxy(opUrl);
			proxy.setMethod("POST");
			proxy.setHeader("Content-Length", String
					.valueOf(data.getBytes().length));
			proxy.setBody(data);
			proxy.exec();
			Log.d(TAG,"组信息_Recv:" + proxy.getResponseString());
			
			String response = proxy.getResponseString();
			JSONObject json = new JSONObject(response);
			String code = json.get("code").toString();
			String msg = json.get("msg").toString();
			
			if("0000".equals(code)){
				//存储同步时间到SharedPreferences
				String syncTime = DateTimeUtil.format(new Date());
				sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putString(Constants.PREF_GROUP_SYNCTIME,syncTime);
				editor.commit();
				
				String jdata=json.get("data").toString();
				JSONObject json_data = new JSONObject(jdata);
				String delId = json_data.get("delId").toString();
				String add = json_data.get("add").toString();
				JSONArray addAry = new JSONArray(add);
				for (int i = 0; i < addAry.length(); i++) {
					JSONObject addItem = (JSONObject) addAry.get(i);
					GroupEntity group = new GroupEntity(addItem);
					ctx.getContentResolver().insert(GroupEntity.CONTENT_URI,
							group.toContentValues());
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
		return Result.fail("组信息同步错误");
	}
}
