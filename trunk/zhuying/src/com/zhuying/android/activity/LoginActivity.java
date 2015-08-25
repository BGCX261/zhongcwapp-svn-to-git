package com.zhuying.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.async.UITask;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.AuthorityEntity;
import com.zhuying.android.entity.CategoryEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.GroupEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.entity.TaskEntity;
import com.zhuying.android.entity.UserEntity;
import com.zhuying.android.service.ActionSyncService;
import com.zhuying.android.service.CategorySyncService;
import com.zhuying.android.service.CommonSyncService;
import com.zhuying.android.service.GroupSyncService;
import com.zhuying.android.service.NoteSyncService;
import com.zhuying.android.service.PhotoSyncService;
import com.zhuying.android.service.TaskSyncService;
import com.zhuying.android.service.UserSyncService;
import com.zhuying.android.service.CompanyContactSyncService;
import com.zhuying.android.service.LoginService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.NetworkStateUtil;

/**
 * 登录
 */
public class LoginActivity extends Activity {
	private EditText loginName;
	private EditText loginPwd;
	private Button loginBtn;
	private Button registBtn;
	private SharedPreferences sharedPrefs;
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		context = getApplicationContext();
		initUI();
	}

	private void initUI() {
		loginName =  (EditText) findViewById(R.id.login_name);
		loginPwd =  (EditText) findViewById(R.id.login_pwd);
		
		loginBtn = (Button) findViewById(R.id.login_btn);
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				NetworkStateUtil nu = new NetworkStateUtil(getApplicationContext());
				if(!NetworkStateUtil.checkNetworkInfo(LoginActivity.this)){
//					showNetworkDialog();
					return;
				}
				
				new UITask(LoginActivity.this) {
					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						progressDialog.setMessage("登录中...");
					}
					
					@Override
					protected Result doInBackground(Void... params) {
						LoginService loginService = new LoginService(getApplicationContext());
						Result loginResult = loginService.login(loginName.getText().toString(),loginPwd.getText().toString());
						if(loginResult.isSuccess()){
							/*CommonSyncService common = new CommonSyncService(getApplicationContext());
							Result result =  common.sync();
							return result;*/
							
							/**
							 * 依次调用各个同步接口，发布进度更新信息
							 * */
							//pref
							SharedPreferences pref = context.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
							String ticketId = pref.getString(Constants.PREF_TICKETID, null);
							publishProgress(1);
							//同步头像
							PhotoSyncService photoService = new PhotoSyncService(context);
							Result photoResult = photoService.syncPhoto(ticketId);
							if(photoResult.isSuccess()){
								publishProgress(2);
								//同步公司、联系人
								CompanyContactSyncService companyService = new CompanyContactSyncService(context);
								Result companyResult = companyService.syncCompany(ticketId);
								if(companyResult.isSuccess()){
									publishProgress(3);
									//同步基础数据：操作员
									UserSyncService dataService = new  UserSyncService(context);
									Result userResult = dataService.syncUser(ticketId);
									if(userResult.isSuccess()){
										//同步基础数据：分类信息
										CategorySyncService categoryService = new CategorySyncService(context);
										Result categoryResult = categoryService.syncCategory(ticketId);
										if(categoryResult.isSuccess()){
											//同步基础数据：组信息
											GroupSyncService groupService = new GroupSyncService(context);
											Result groupResult = groupService.syncGroup(ticketId);
											if(groupResult.isSuccess()){
												publishProgress(4);
												//同步记录
												NoteSyncService noteService = new NoteSyncService(context);
												Result noteResult = noteService.syncNote(ticketId);
												if(noteResult.isSuccess()){
													publishProgress(5);
													//同步计划任务
													TaskSyncService taskService = new TaskSyncService(context);
													Result taskResult = taskService.syncTask(ticketId);
													if(taskResult.isSuccess()){
														publishProgress(6);
														//同步最近行动
														ActionSyncService actionService = new ActionSyncService(context);
														Result actionResult = actionService.syncAction(ticketId);
														return Result.success("同步成功");
													}
												}
											}
										}
									}
								}
							}else{
								return Result.fail("同步错误");
							}
							return Result.fail("同步错误");
						
						
							
						}else{
//							return Result.fail("登录错误");
							return loginResult;
						}
					}
					
					@Override
					protected void onPostExecute(Result result) {
						if (result.isSuccess()) {
							//记住用户名&密码
							sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
							Editor editor = sharedPrefs.edit();
							editor.putString(Constants.PREF_USERNAME, loginName.getText().toString());
							editor.putString(Constants.PREF_PWD, loginPwd.getText().toString());
							editor.commit();
							
							Intent i = new Intent();
							i.setClass(LoginActivity.this,TabNaviActivity.class);
							startActivity(i);
							finish();
						}else{
							
						}
						super.onPostExecute(result);
					}
					
					@Override
					protected void onProgressUpdate(Integer... values) {
						switch (values[0].intValue()) {
						case 1:
							progressDialog.setMessage("同步头像...");
							break;
						case 2:
							progressDialog.setMessage("同步公司、联系人...");
							break;
						case 3:
							progressDialog.setMessage("同步基础数据...");
							break;
						case 4:
							progressDialog.setMessage("同步记录...");
							break;
						case 5:
							progressDialog.setMessage("同步计划任务...");
							break;
						case 6:
							progressDialog.setMessage("同步最近行动...");
							break;
						default:
							break;
						}
					}
				}.execute();
			}
		});
		
		registBtn = (Button) findViewById(R.id.register_btn);
		registBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(LoginActivity.this,RegistActivity.class);
				startActivity(i);
				finish();
			}
		});
	}
	
	private void showNetworkDialog(){
		new AlertDialog.Builder(LoginActivity.this).setTitle("提示").setMessage("网络问题，请稍后重试！")
				.setCancelable(false).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
							}
						}).create().show();
	
	}
}