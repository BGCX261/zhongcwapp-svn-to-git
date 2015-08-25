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
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.HanziToPinyin;
import com.zhuying.android.util.HttpProxy;
import com.zhuying.android.util.Md5;
import com.zhuying.android.util.MobiEncrptionUtil;

/**
 * 头像同步服务
 */
public class PhotoSyncService {
	private static final String TAG = "PhotoSyncService";
	private Context ctx;
	private SharedPreferences sharedPrefs;
	
	public PhotoSyncService(Context context){
		this.ctx = context;
	}
	
	/**
	 * 头像同步接口
	 * @param ticketId 票据id
	 * @return
	 */
	public Result syncPhoto(String ticketId){
		String opUrl = LoginService.url + "/photoSync";

		Map bodyData = new HashMap();// 发送的业务数据
		sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		String pref_syncTime = sharedPrefs.getString(Constants.PREF_PHOTO_SYNCTIME, "2010-10-01 00:00:00");
		bodyData.put("syncTime", pref_syncTime); //同步时间,默认值(2010-10-01 00:00:00)
		//=================
		JSONArray add_ary = new JSONArray(); //新增数组
		Cursor addCursor = ctx.getContentResolver().query(PhotoEntity.CONTENT_URI, null, "photoUpdateDate > ?", new String[]{pref_syncTime}, null);
		for (addCursor.moveToFirst(); !addCursor.isAfterLast(); addCursor
				.moveToNext()) { //遍历
			PhotoEntity entity = new PhotoEntity(addCursor);
			JSONObject jsonObj = entity.toJSONObject();
			add_ary.put(jsonObj);
		}
		addCursor.close();
		bodyData.put("photo", add_ary.toString());
		
		String sign = "";
		try {
			sign = Md5.md5(LoginService.signKey + LoginService.sid + ticketId + new JSONObject(bodyData).toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Map postData = new HashMap();// 发送数据组装
		postData.put("sendTime", DateTimeUtil.format(new Date()));
		postData.put("sid", LoginService.sid);
		postData.put("data", new JSONObject(bodyData).toString());
		postData.put("sign", sign);
		postData.put("ticketId", ticketId);

		try {
			String data = new JSONObject(postData).toString();
			Log.d(TAG, "同步头像_Send:" + data);
			HttpProxy proxy = new HttpProxy(opUrl);
			proxy.setMethod("POST");
			proxy.setHeader("Content-Length", String
					.valueOf(data.getBytes().length));
			proxy.setBody(data);
			proxy.exec();
			Log.d(TAG,"同步头像_Recv:" + proxy.getResponseString());
			
			String response = proxy.getResponseString();
			JSONObject json = new JSONObject(response);
			String code = json.get("code").toString();
			String msg = json.get("msg").toString();
			if("0000".equals(code)){
				//存储同步时间到SharedPreferences
				String syncTime = DateTimeUtil.format(new Date());
				sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putString(Constants.PREF_PHOTO_SYNCTIME,syncTime);
				editor.commit();
				
				String jdata = json.get("data").toString();
//				JSONObject json_data = new JSONObject(jdata);
				JSONArray ary = new JSONArray(jdata);
				for(int i = 0; i < ary.length(); i++){
					JSONObject obj = (JSONObject) ary.get(i);
					PhotoEntity photo = new PhotoEntity(obj);
					Cursor photoCursor = ctx.getContentResolver().query(PhotoEntity.CONTENT_URI, null, "partyid = ?", new String[]{photo.getPartyId()}, null);
					if(photoCursor.getCount() > 0){ //记录存在,修改
//						Log.d(TAG, "记录存在,修改");
						ctx.getContentResolver().update(PhotoEntity.CONTENT_URI, photo.toContentValues(), "partyid = ?", new String[]{photo.getPartyId()});
					}else{ //记录不存在,新增
//						Log.d(TAG, "不存在");
						ctx.getContentResolver().insert(PhotoEntity.CONTENT_URI,
								photo.toContentValues());
					}
					photoCursor.close();
				}
				return Result.success("头像成功");
			}else{
				return Result.fail(msg);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Result.fail("头像同步错误");
	}
}
