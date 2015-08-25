package com.zhuying.android.activity;


import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.service.ActionSyncService;
import com.zhuying.android.service.CategorySyncService;
import com.zhuying.android.service.CommonSyncService;
import com.zhuying.android.service.CompanyContactSyncService;
import com.zhuying.android.service.GroupSyncService;
import com.zhuying.android.service.NoteSyncService;
import com.zhuying.android.service.PhotoSyncService;
import com.zhuying.android.service.TaskSyncService;
import com.zhuying.android.service.UserSyncService;
import com.zhuying.android.util.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;

/**
 * 初始化
 */
public class LoadingActivity extends Activity {

	private ProgressBar xh_ProgressBar;
	protected static final int GUI_THREADING_NOTIFIER = 0x0001;
	public int photoProgress = 0;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.loading);

		xh_ProgressBar = (ProgressBar) findViewById(R.id.ProgressBar01);
		xh_ProgressBar.setIndeterminate(false);

		xh_ProgressBar.setVisibility(View.VISIBLE);
		// 设置ProgressBar的最大值
		xh_ProgressBar.setMax(100);
		// 设置ProgressBar当前值
		xh_ProgressBar.setProgress(0);

		context = getApplicationContext();
		
		new Thread(new Runnable() {
			@Override
			public void run() {

				//pref
				SharedPreferences pref = context.getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
				String ticketId = pref.getString(Constants.PREF_TICKETID, null);
				//同步头像
				PhotoSyncService photoService = new PhotoSyncService(context);
				Result photoResult = photoService.syncPhoto(ticketId);
				if(photoResult.isSuccess()){
					//同步公司、联系人
					CompanyContactSyncService companyService = new CompanyContactSyncService(context);
					Result companyResult = companyService.syncCompany(ticketId);
					if(companyResult.isSuccess()){
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
									//同步记录
									NoteSyncService noteService = new NoteSyncService(context);
									Result noteResult = noteService.syncNote(ticketId);
									if(noteResult.isSuccess()){
										//同步计划任务
										TaskSyncService taskService = new TaskSyncService(context);
										Result taskResult = taskService.syncTask(ticketId);
										if(taskResult.isSuccess()){
											//同步最近行动
											ActionSyncService actionService = new ActionSyncService(context);
											Result actionResult = actionService.syncAction(ticketId);
										}
									}
								}
							}
						}
					}
				}else{
				}
			
			
				
				
				for (int i = 0; i < 10; i++) {
					//设置进度值
					photoProgress = (i + 1) * 10;
					//睡眠1000毫秒
					try {
						Thread.sleep(1000);
						
						Message m = new Message();
						m.what = GUI_THREADING_NOTIFIER;
						h.sendMessage(m);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}

	Handler h = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GUI_THREADING_NOTIFIER:
				if (!Thread.currentThread().isInterrupted()) {
					//改变ProgressBar的当前值
					xh_ProgressBar.setProgress(photoProgress);
				}
				break;
			default:
				break;
			}
			
			super.handleMessage(msg);
		};
	};
}
