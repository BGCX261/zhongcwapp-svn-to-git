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
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.zhuying.android.async.Result;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.HanziToPinyin;
import com.zhuying.android.util.HttpProxy;
import com.zhuying.android.util.Md5;
import com.zhuying.android.util.MobiEncrptionUtil;

/**
 * 注册服务
 */
public class RegistService {
	private static final String TAG = "RegistService";
	private Context context;
	private SharedPreferences sharedPrefs;
	
	public RegistService(Context context){
		this.context = context;
	}
	
	/**
	 * 注册接口
	 * @param ticketId 票据id
	 * @return
	 */
	public Result regist(String userName,String password,String email){
		String opUrl = LoginService.url + "/reg";

		Map bodyData = new HashMap();// 发送的业务数据
		bodyData.put("userName", MobiEncrptionUtil.Encrypt(userName));
		bodyData.put("password", MobiEncrptionUtil.Encrypt(password));
		bodyData.put("email", email);
		bodyData.put("regCode", "");
		bodyData.put("source", "Android");
		bodyData.put("appVer", getVersionName());
		bodyData.put("cid", "");
		bodyData.put("os", "Android " + Build.VERSION.RELEASE);
		bodyData.put("vender", Build.BRAND);
		bodyData.put("model", Build.MODEL);
		bodyData.put("resolution", "");
		
		String sign = "";
		try {
			sign = Md5.md5(LoginService.signKey + LoginService.sid  + new JSONObject(bodyData).toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Map postData = new HashMap();// 发送数据组装
		postData.put("sendTime", DateTimeUtil.format(new Date()));
		postData.put("sid", LoginService.sid);
		postData.put("data", new JSONObject(bodyData).toString());
		postData.put("sign", sign);

		try {
			String data = new JSONObject(postData).toString();
			Log.d(TAG, "注册_Send:" + data);
			HttpProxy proxy = new HttpProxy(opUrl);
			proxy.setMethod("POST");
			proxy.setHeader("Content-Length", String
					.valueOf(data.getBytes().length));
			proxy.setBody(data);
			proxy.exec();
			Log.d(TAG,"注册_Recv:" + proxy.getResponseString());
			
			String response = proxy.getResponseString();
			JSONObject json = new JSONObject(response);
			String code = json.get("code").toString();
			String msg = json.get("msg").toString();
			
			if("0000".equals(code)){
				String jdata=json.get("data").toString();
				JSONObject json_data = new JSONObject(jdata);
				
				String ticketId = json_data.getString("ticketId");
				//Pref
				sharedPrefs = context.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putString(Constants.PREF_TICKETID, ticketId);
				editor.commit();
				
				return Result.success("注册成功");
			}else{
				return Result.fail(msg);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return Result.fail("注册错误");
	}
	
	//获取当前应用的版本号：
	private String getVersionName() {
		String version = "";
		
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return version;
	}
}
