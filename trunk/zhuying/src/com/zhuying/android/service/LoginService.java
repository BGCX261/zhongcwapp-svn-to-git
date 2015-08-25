package com.zhuying.android.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.zhuying.android.async.Result;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.HttpProxy;
import com.zhuying.android.util.Md5;
import com.zhuying.android.util.MobiEncrptionUtil;
import com.zhuying.android.util.Network;
import com.zhuying.android.util.NetworkException;

/**
 * 登录服务
 */
public class LoginService {
	private static final String TAG = "LoginService";
	
	public static String url = "http://www.yewuben.com/openapi/MobiService";
	public static String sid = "00001000000";
	// openapi需要密码解密的public key
	public static String MOBISECRETPUBLIC_MODULUS = "18696804217482948445037818745695980007429686027732901914464524415743365446514523774761307099727390102895677398727759423504058143945734923195758352649148687746240246461783849696830142152395586457353569824224672854503221067129705699519835195490732771395824819919125960966456705799359313898425069275412317442758586688831932878501617484797599667534944302137040966725575843706915655784196128071056294068512616124775543119621993096056671836005654854420618273500351180203642976206807126631891412262117805468518566583225512352159053495950610972822468203743836160620763764238641922183166461082718238518774911206319488259877307";
	public static String MOBISECRETPUBLIC_EXPONENT = "65537";
	public static String signKey = "zhuying1218@sign";// 校验key
//	private static String ticketId = "827e404afa2b4844a74a66a3d6534223";
//	private static String syncTime="2010-10-01 00:00:00";
	
	private Context ctx;
	private SharedPreferences sharedPrefs;
	
	public LoginService(Context context){
		this.ctx = context;
	}
	
	/**
	 * 登录接口
	 * @param userName 用户名
	 * @param password 密码
	 * @return
	 */
	public Result login(String userName,String password){
		String opUrl = url + "/login";// 访问登陆服务

		Map bodyData = new HashMap();// 发送的业务数据
		bodyData.put("model", "Android Emulator");
		bodyData.put("os", "Android 2.3.3");
		bodyData.put("vender", "Google");
		bodyData.put("password", MobiEncrptionUtil.Encrypt(password));// 密码加密发送
		bodyData.put("appVer", "1.0.0");
		bodyData.put("userName",MobiEncrptionUtil.Encrypt(userName));// 账户，加密发送
		bodyData.put("cid", "C4:2C:03:1D:4A:ED");
		
		String sign = "";
		try {
			sign = Md5.md5(signKey + sid + new JSONObject(bodyData).toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Map postData = new HashMap();// 发送数据组装
		postData.put("sendTime", DateTimeUtil.format(new Date()));
		postData.put("sid", sid);
		postData.put("data", new JSONObject(bodyData).toString());
		postData.put("sign", sign);

		try {
			String data = new JSONObject(postData).toString();
			Log.d(TAG, "登陆_Send:" + data);
			HttpProxy proxy = new HttpProxy(opUrl);
			proxy.setMethod("POST");
			proxy.setHeader("Content-Length", String
					.valueOf(data.getBytes().length));
			proxy.setBody(data);
			proxy.exec();
			Log.d(TAG,"登陆_Recv:" + proxy.getResponseString());
			
			String response = proxy.getResponseString();
			JSONObject json = new JSONObject(response);
			String code = json.get("code").toString();
			String msg = json.get("msg").toString();
			String jdata=json.get("data").toString();
			
			
			if("0000".equals(code)){
				JSONObject json_data = new JSONObject(jdata);
				String ticketId = json_data.getString("ticketId");
				//Pref
				sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putString(Constants.PREF_TICKETID, ticketId);
				editor.commit();
				
				return Result.success("登录成功");
			}else{
				return Result.fail(msg);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Result.fail("登录错误");
	}
}
