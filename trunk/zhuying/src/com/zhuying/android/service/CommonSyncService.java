package com.zhuying.android.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.zhuying.android.async.Result;
import com.zhuying.android.util.Constants;

/**
 * 同步汇总 
 * @author kevin
 */
public class CommonSyncService {
	private Context context;
	
	public CommonSyncService(Context ctx){
		this.context = ctx;
	}

	public Result sync(){
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
	
	}
}
