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
import com.zhuying.android.entity.AuthorityEntity;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.HanziToPinyin;
import com.zhuying.android.util.HttpProxy;
import com.zhuying.android.util.Md5;
import com.zhuying.android.util.MobiEncrptionUtil;
import com.zhuying.android.util.StringUtil;

/**
 * 任务同步服务
 */
public class TaskSyncService {
	private static final String TAG = "TaskSyncService";
	private Context ctx;
	private SharedPreferences sharedPrefs;
	
	public TaskSyncService(Context context){
		this.ctx = context;
	}
	
	/**
	 * 任务同步接口
	 * @param ticketId 票据id
	 * @return
	 */
	public Result syncTask(String ticketId){
		String opUrl = LoginService.url + "/taskSync";

		Map bodyData = new HashMap();// 发送的业务数据
		sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		String pref_syncTime = sharedPrefs.getString(Constants.PREF_TASK_SYNCTIME, "2010-10-01 00:00:00");
		bodyData.put("syncTime", pref_syncTime);
		
		//=================
		JSONArray add_ary = new JSONArray();
		Cursor addCursor = ctx.getContentResolver().query(TaskEntity.CONTENT_URI, null, "createdat > '"+ pref_syncTime+"'", null, null);
		for (addCursor.moveToFirst(); !addCursor.isAfterLast(); addCursor
				.moveToNext()) { //遍历
			TaskEntity entity = new TaskEntity(addCursor);
			JSONObject jsonObj = entity.toJSONObject();
			JSONArray addTaskUserAry = new JSONArray();//要增加的task_user
			Cursor taskUserCursor = ctx.getContentResolver().query(AuthorityEntity.CONTENT_URI, null, "dataId = '"+entity.getTaskid()+"'", null, null);
			for (taskUserCursor.moveToFirst(); !taskUserCursor.isAfterLast(); taskUserCursor
					.moveToNext()) {
				AuthorityEntity taskuser = new AuthorityEntity(taskUserCursor);
				JSONObject tujson = taskuser.toJSONObject();
				addTaskUserAry.put(tujson);
			}
			taskUserCursor.close();
			
			try {
				jsonObj.put("taskUserAdd", addTaskUserAry.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			add_ary.put(jsonObj);
		}
		addCursor.close();
		bodyData.put("taskAdd", add_ary.toString());
		
		JSONArray update_ary = new JSONArray();
		Cursor updateCursor = ctx.getContentResolver().query(TaskEntity.CONTENT_URI, null, "updatedat > '" + pref_syncTime + "' and createdat < '" + pref_syncTime + "'", null, null);
		for (updateCursor.moveToFirst(); !updateCursor.isAfterLast(); updateCursor
				.moveToNext()) {
			TaskEntity entity = new TaskEntity(updateCursor);
			JSONObject jsonObj = entity.toJSONObject();
			
			JSONArray addTaskUserAry = new JSONArray();//要增加的task_user
			Cursor taskUserCursor = ctx.getContentResolver().query(AuthorityEntity.CONTENT_URI, null, "dataId = '"+entity.getTaskid()+"'", null, null);
			for (taskUserCursor.moveToFirst(); !taskUserCursor.isAfterLast(); taskUserCursor
					.moveToNext()) {
				AuthorityEntity taskuser = new AuthorityEntity(taskUserCursor);
				JSONObject tujson = taskuser.toJSONObject();
				addTaskUserAry.put(tujson);
			}
			taskUserCursor.close();
			
			try {
				jsonObj.put("taskUserAdd", addTaskUserAry.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			update_ary.put(jsonObj);
		}
		updateCursor.close();
		bodyData.put("taskUpdate", update_ary.toString());
		
		Cursor allCursor = ctx.getContentResolver().query(TaskEntity.CONTENT_URI, null, null, null, null);
		String allId = "";
		for (allCursor.moveToFirst(); !allCursor.isAfterLast(); allCursor
				.moveToNext()) {
			allId += allCursor.getString(18)+ ",";
		}
		allCursor.close();
		bodyData.put("taskCheckAllId", allId);
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
			Log.d(TAG, "任务_Send:" + data);
			HttpProxy proxy = new HttpProxy(opUrl);
			proxy.setMethod("POST");
			proxy.setHeader("Content-Length", String
					.valueOf(data.getBytes().length));
			proxy.setBody(data);
			proxy.exec();
			Log.d(TAG,"任务_Recv:" + proxy.getResponseString());
			
			String response = proxy.getResponseString();
			JSONObject json = new JSONObject(response);
			String code = json.get("code").toString();
			String msg = json.get("msg").toString();
			if("0000".equals(code)){
				//存储同步时间到SharedPreferences
				String syncTime = DateTimeUtil.format(new Date());
				sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				Editor editor = sharedPrefs.edit();
				editor.putString(Constants.PREF_TASK_SYNCTIME,syncTime);
				editor.commit();
				
				String jdata=json.get("data").toString();
				JSONObject json_data = new JSONObject(jdata);
				String delId = json_data.get("taskDelId").toString();
				String[] dels = delId.split(",");
				for (int i = 0; i < dels.length; i++) {
					String del = dels[i];
					ctx.getContentResolver().delete(TaskEntity.CONTENT_URI, "taskid = '"+del+"'", null);
				}
				String add = json_data.get("taskAdd").toString();
				JSONArray addAry = new JSONArray(add);
				for (int i = 0; i < addAry.length(); i++) {
					JSONObject addItem = (JSONObject) addAry.get(i);
					TaskEntity task = new TaskEntity(addItem);
					ctx.getContentResolver().insert(TaskEntity.CONTENT_URI,
							task.toContentValues());
					
					String taskUserAdd = addItem.get("taskUserAdd").toString();
					if(!StringUtil.isEmpty(taskUserAdd)){
						//删除当前计划任务对应的所有权限数据
						ctx.getContentResolver().delete(AuthorityEntity.CONTENT_URI, "dataId = ?", new String[]{task.getTaskid()});
						
						JSONArray taskUserAddAry = new JSONArray(taskUserAdd);
						for (int j = 0; j < taskUserAddAry.length(); j++) {
							JSONObject taskUser = (JSONObject) taskUserAddAry.get(j);
							AuthorityEntity ae = new AuthorityEntity(taskUser);
							ctx.getContentResolver().insert(AuthorityEntity.CONTENT_URI,
									ae.toContentValues());
						}
					}
				}
				
				String update = json_data.get("taskUpdate").toString();
				JSONArray updateAry = new JSONArray(update);
				for (int i = 0; i < updateAry.length(); i++) {
					JSONObject updateItem = (JSONObject) updateAry.get(i);
					String taskid = updateItem.getString("taskid");
					TaskEntity task = new TaskEntity(updateItem);
					ctx.getContentResolver().update(TaskEntity.CONTENT_URI,
							task.toContentValues(),TaskEntity.KEY_TASKID + " = '" + taskid +"'", null);
					
					
					String taskUserAdd = updateItem.get("taskUserAdd").toString();
					if(!StringUtil.isEmpty(taskUserAdd)){
						//删除当前计划任务对应的所有权限数据
						ctx.getContentResolver().delete(AuthorityEntity.CONTENT_URI, "dataId = ?", new String[]{task.getTaskid()});
						
						JSONArray taskUserAddAry = new JSONArray(taskUserAdd);
						for (int j = 0; j < taskUserAddAry.length(); j++) {
							JSONObject taskUser = (JSONObject) taskUserAddAry.get(j);
							AuthorityEntity ae = new AuthorityEntity(taskUser);
							ctx.getContentResolver().insert(AuthorityEntity.CONTENT_URI,
									ae.toContentValues());
						}
					}
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
		return Result.fail("任务同步错误");
	}
}
