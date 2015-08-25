package com.zhuying.android.activity;

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
import com.zhuying.android.service.CommonSyncService;
import com.zhuying.android.service.LoginService;
import com.zhuying.android.service.RegistService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.StringUtil;

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
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册
 */
public class RegistActivity extends Activity {
	private EditText usernameView;
	private EditText emailView;
	private EditText pwdView;
	private EditText secondpwdView;
	
	private String username;
	private String email;
	private String pwd;
	private String secondpwd;
	
	private SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.regist);
		
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("注册");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setText("注册");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(validateRegist()){
					if(!NetworkStateUtil.checkNetworkInfo(RegistActivity.this)){
						return;
					}
					showMsgDialog();
				}
			}
		});
		
		Button left = (Button) findViewById(R.id.header_left_btn);
		left.setText("返回");
		left.setVisibility(View.VISIBLE);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(RegistActivity.this,LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		
		//初始化widget
		usernameView = (EditText) findViewById(R.id.reg_username);
		emailView = (EditText) findViewById(R.id.reg_email);
		pwdView = (EditText) findViewById(R.id.reg_pwd);
		secondpwdView = (EditText) findViewById(R.id.reg_second_pwd);
		
	}
	
	/**
	 * 校验注册
	 * @return
	 */
	private boolean validateRegist(){
		username = usernameView.getText().toString();
		email = emailView.getText().toString();
		pwd = pwdView.getText().toString();
		secondpwd = secondpwdView.getText().toString();
		
		if(StringUtil.isEmpty(username)){
			Toast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(StringUtil.isChinese(username)){
			Toast.makeText(getApplicationContext(), "用户名请输入英文", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(StringUtil.isEmpty(email)){
			Toast.makeText(getApplicationContext(), "请输入邮箱", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(! StringUtil.checkEmail(email)){
			Toast.makeText(getApplicationContext(), "请输入合法格式邮箱", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(StringUtil.isEmpty(pwd)){
			Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(StringUtil.isEmpty(secondpwd)){
			Toast.makeText(getApplicationContext(), "请输入确认密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(! pwd.equals(secondpwd)){
			Toast.makeText(getApplicationContext(), "密码与确认密码不一致", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void showMsgDialog(){
		String msg = "请确认您填写的邮箱\n 您填写的邮箱是 " + email +" 填写正确的邮箱可以帮您保护自己的数据安全.请确认是否正确无误.";
		new AlertDialog.Builder(RegistActivity.this).setMessage(msg)
				.setCancelable(false).setPositiveButton("继续注册",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
				/*				new UITask(RegistActivity.this) {
									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										progressDialog.setMessage("注册中...");
									}
									
									@Override
									protected Result doInBackground(Void... params) {
										RegistService service = new RegistService(getApplicationContext());
										Result loginResult = service.regist(username, pwd, email);
										if(loginResult.isSuccess()){
											CommonSyncService common = new CommonSyncService(getApplicationContext());
											Result result =  common.sync();
											return result;
										}else{
											return Result.fail("注册错误");
										}
									}
									
									@Override
									protected void onPostExecute(Result result) {
										if (result.isSuccess()) {
											//记住用户名&密码
											sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
											Editor editor = sharedPrefs.edit();
											editor.putString(Constants.PREF_USERNAME, username);
											editor.putString(Constants.PREF_PWD, pwd);
											editor.commit();
											
											Intent i = new Intent();
											i.setClass(RegistActivity.this,TabNaviActivity.class);
											startActivity(i);
											finish();
										}else{
											
										}
										super.onPostExecute(result);
									}
								}.execute();*/
								
								new UITask(RegistActivity.this) {
									@Override
									protected void onPreExecute() {
										super.onPreExecute();
										progressDialog.setMessage("登录中...");
									}
									
									@Override
									protected Result doInBackground(Void... params) {
										RegistService registservice = new RegistService(getApplicationContext());
										Result registResult = registservice.regist(username, pwd, email);
										if(registResult.isSuccess()){
											LoginService loginService = new LoginService(getApplicationContext());
											Result loginResult = loginService.login(username,pwd);
											if(loginResult.isSuccess()){
												CommonSyncService common = new CommonSyncService(getApplicationContext());
												Result result =  common.sync();
												return result;
											}else{
												return loginResult;
											}
										}else{
											return registResult;
										}
									}
									
									@Override
									protected void onPostExecute(Result result) {
										if (result.isSuccess()) {
											//记住用户名&密码
											sharedPrefs = ctx.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
											Editor editor = sharedPrefs.edit();
											editor.putString(Constants.PREF_USERNAME, username);
											editor.putString(Constants.PREF_PWD, pwd);
											editor.commit();
											
											Intent i = new Intent();
											i.setClass(RegistActivity.this,TabNaviActivity.class);
											startActivity(i);
											finish();
										}else{
											
										}
										super.onPostExecute(result);
									}
								}.execute();
							}
						}).setNegativeButton("返回修改",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
								dialog.cancel();
								emailView.requestFocus();
							}
						}).create().show();
	}
}
