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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.zhuying.android.activity.SuggestActivity;
import com.zhuying.android.async.Result;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.HanziToPinyin;
import com.zhuying.android.util.HttpProxy;
import com.zhuying.android.util.Md5;
import com.zhuying.android.util.MobiEncrptionUtil;

/**
 * 反馈意见
 */
public class SuggestSyncService {
	private static final String TAG = "SuggestSyncService";
	private Context context;
	
	public SuggestSyncService(Context context){
		this.context = context;
	}
	
	/**
	 * 反馈意见接口
	 * @param ticketId 票据id
	 * @return
	 */
	public Result syncSuggest(String ticketId,String content){
		String opUrl = LoginService.url + "/suggestSync";

		Map bodyData = new HashMap();// 发送的业务数据
		
		JSONObject jsonSuggest = new JSONObject();
//		Build build = new Build();
		String s1 = Build.BOARD;
		String s2 = Build.BOOTLOADER;
		String s3 = Build.BRAND;
		String s4 = Build.DEVICE;
		String s5 = Build.DISPLAY;
		String s6 = Build.MANUFACTURER;
		String s7 = Build.MODEL;
		String s8 = Build.PRODUCT;
		
		String s9 = Build.VERSION.SDK;
		String s10 = Build.VERSION.RELEASE;
		
		try {
			jsonSuggest.put("os", "Android " + s10);
			jsonSuggest.put("vender", s3);
			jsonSuggest.put("model", s7);
			jsonSuggest.put("resolution", SuggestActivity.SCREEN_WIDTH + "*" +SuggestActivity.SCREEN_HEIGHT);
			jsonSuggest.put("productVersion", getVersionName());
			jsonSuggest.put("content", content);
			jsonSuggest.put("createdat", DateTimeUtil.format(new Date()));
			bodyData.put("suggest", jsonSuggest.toString());
		} catch (JSONException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
			Log.d(TAG, "反馈意见_Send:" + data);
			HttpProxy proxy = new HttpProxy(opUrl);
			proxy.setMethod("POST");
			proxy.setHeader("Content-Length", String
					.valueOf(data.getBytes().length));
			proxy.setBody(data);
			proxy.exec();
			Log.d(TAG,"反馈意见_Recv:" + proxy.getResponseString());
			
			String response = proxy.getResponseString();
			JSONObject json = new JSONObject(response);
			String code = json.get("code").toString();
			String msg = json.get("msg").toString();
			if("0000".equals(code)){
				return Result.success("反馈意见成功");
			}else{
				return Result.fail(msg);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Result.fail("反馈意见错误");
	}
	
	
	
	//获取当前应用的版本号：
	private String getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		String version = packInfo.versionName;
		return version;
	}
}
