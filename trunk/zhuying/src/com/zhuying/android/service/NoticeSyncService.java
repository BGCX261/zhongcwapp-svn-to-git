package com.zhuying.android.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.zhuying.android.async.Result;
import com.zhuying.android.entity.CategoryEntity;
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
 * 通知同步服务
 */
public class NoticeSyncService {
	private static final String TAG = "NoticeSyncService";
	private Context ctx;
	private SharedPreferences sharedPrefs;
	
	public NoticeSyncService(Context context){
		this.ctx = context;
	}
	
	/**
	 * 通知同步接口
	 * @param ticketId 票据id
	 * @return
	 */
	public String syncNotice(String ticketId){
		String opUrl = LoginService.url + "/notice";

		Map bodyData = new HashMap();// 发送的业务数据
		sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		String pref_syncTime = sharedPrefs.getString(Constants.PREF_NOTICE_SYNCTIME, "2010-10-01 00:00:00");
		bodyData.put("syncTime", pref_syncTime);
		
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
//			Log.d(TAG, "通知_Send:" + data);
			HttpProxy proxy = new HttpProxy(opUrl);
			proxy.setMethod("POST");
			proxy.setHeader("Content-Length", String
					.valueOf(data.getBytes().length));
			proxy.setBody(data);
			proxy.exec();
//			Log.d(TAG,"通知_Recv:" + proxy.getResponseString());
			
			String response = proxy.getResponseString();
			JSONObject json = new JSONObject(response);
			String code = json.get("code").toString();
			String msg = json.get("msg").toString();
		
			if("0000".equals(code)){
				//存储同步时间到SharedPreferences
				String syncTime = DateTimeUtil.format(new Date());
				sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putString(Constants.PREF_NOTICE_SYNCTIME,syncTime);
				editor.commit();
				
				String jdata=json.get("data").toString();
				JSONObject json_data = new JSONObject(jdata);
//				JSONArray sysNoticeAry = json_data.getJSONArray("sysNotice");
				
				String ary = json_data.getString("sysNotice");
				JSONArray sysNoticeAry = new JSONArray(ary);
				String content = "";
				if(sysNoticeAry.length() > 0){
					for(int i=0;i<sysNoticeAry.length();i++){
						JSONObject sys = (JSONObject) sysNoticeAry.get(i);
						String c = sys.getString("content");
//						String title = sys.getString("title");
						
						content += (i + 1) +"." +c +"\n";
					}
//					showNoticeDialog(ctx,content);
					return content;
				}
				
//				return Result.success("登录成功");
			}else{
//				return Result.fail(msg);
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		return Result.fail("分类信息同步错误");
		return null;
	}
	
	/**
	 * 通知对话框
	 * @param ctx
	 */
	private void showNoticeDialog(Context ctx,String content){
		new AlertDialog.Builder(ctx).setTitle("提示").setMessage("系统通知：\n"+content)
				.setCancelable(false).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
							}
						}).create().show();
	}
}
