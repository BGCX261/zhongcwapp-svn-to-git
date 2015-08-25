package com.zhuying.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.async.UITask;
import com.zhuying.android.service.ActionSyncService;
import com.zhuying.android.service.CategorySyncService;
import com.zhuying.android.service.CompanyContactSyncService;
import com.zhuying.android.service.GroupSyncService;
import com.zhuying.android.service.LoginService;
import com.zhuying.android.service.NoteSyncService;
import com.zhuying.android.service.PhotoSyncService;
import com.zhuying.android.service.TaskSyncService;
import com.zhuying.android.service.UserSyncService;
import com.zhuying.android.util.Constants;

public class WelcomeActivity extends Activity{
	private SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				sharedPrefs = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				final String userName = sharedPrefs.getString(Constants.PREF_USERNAME, "");
				final String pwd = sharedPrefs.getString(Constants.PREF_PWD, "");
				if(!TextUtils.isEmpty(userName)){
					//copy from LoginActivity
//					new UITask(WelcomeActivity.this) {
//						@Override
//						protected void onPreExecute() {
//							super.onPreExecute();
//							progressDialog.setMessage("登录中...");
//						}
//						
//						@Override
//						protected Result doInBackground(Void... params) {
//							LoginService loginService = new LoginService(getApplicationContext());
//							Result loginResult = loginService.login(userName,pwd);
//							if(loginResult.isSuccess()){
//								//pref
//								SharedPreferences pref = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
//								String ticketId = pref.getString(Constants.PREF_TICKETID, null);
//								
//								//同步头像
//								PhotoSyncService photoService = new PhotoSyncService(getApplicationContext());
//								Result photoResult = photoService.syncPhoto(ticketId);
//								if(photoResult.isSuccess()){
//									//同步公司、联系人
//									CompanyContactSyncService companyService = new CompanyContactSyncService(getApplicationContext());
//									Result companyResult = companyService.syncCompany(ticketId);
//									if(companyResult.isSuccess()){
//										//同步基础数据：操作员
//										UserSyncService dataService = new  UserSyncService(getApplicationContext());
//										Result userResult = dataService.syncUser(ticketId);
//										if(userResult.isSuccess()){
//											//同步基础数据：分类信息
//											CategorySyncService categoryService = new CategorySyncService(getApplicationContext());
//											Result categoryResult = categoryService.syncCategory(ticketId);
//											if(categoryResult.isSuccess()){
//												//同步基础数据：组信息
//												GroupSyncService groupService = new GroupSyncService(getApplicationContext());
//												Result groupResult = groupService.syncGroup(ticketId);
//												if(groupResult.isSuccess()){
//													//同步记录
//													NoteSyncService noteService = new NoteSyncService(getApplicationContext());
//													Result noteResult = noteService.syncNote(ticketId);
//													if(noteResult.isSuccess()){
//														//同步计划任务
//														TaskSyncService taskService = new TaskSyncService(getApplicationContext());
//														Result taskResult = taskService.syncTask(ticketId);
//														if(taskResult.isSuccess()){
//															//同步最近行动
//															ActionSyncService actionService = new ActionSyncService(getApplicationContext());
//															Result actionResult = actionService.syncAction(ticketId);
//															return Result.success("同步成功");
//														}
//													}
//												}
//											}
//										}
//									}
//								}
//							}else{
//								return Result.fail("登录错误");
//							}
//							return Result.fail("同步错误");
//						}
//						
//						@Override
//						protected void onPostExecute(Result result) {
//							if (result.isSuccess()) {
//								Intent i = new Intent();
//								i.setClass(WelcomeActivity.this,TabNaviActivity.class);
//								startActivity(i);
//								finish();
//							}else{
//								
//							}
//							super.onPostExecute(result);
//						}
//					}.execute();
				
					Intent i = new Intent();
					i.setClass(WelcomeActivity.this, TabNaviActivity.class);
					startActivity(i);
					finish(); //退出应用
				}else{
					Intent i = new Intent();
					i.setClass(WelcomeActivity.this, LoginActivity.class);
					startActivity(i);
					finish(); //退出应用
				}
			}
		}, 2000);
	}
}
