package com.zhuying.android.activity;

import com.zhuying.android.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;

/**
 * 导航
 */
public class TabNaviActivity extends TabActivity {

	private TabHost tabHost;
	private TabWidget tabWidget;
	private boolean fromLoginFlag = false;
	private static final String TAG = "MainActivity";
	
	public String SELECTED_TAB = "index";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tabHost = getTabHost();

		TabHost.TabSpec spec = tabHost.newTabSpec("index");
		Intent indexIntent = new Intent(this, ActionListActivity.class);
		spec.setContent(indexIntent);
		spec.setIndicator("最近行动",
				getResources().getDrawable(R.drawable.tab_action_selector));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("account");
		spec.setContent(new Intent(this, ContactListActivity.class));
		spec.setIndicator("联系人",
				getResources().getDrawable(R.drawable.tab_contact_selector));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("activity");
		spec.setContent(new Intent(this, CompanyListActivity.class));
		spec.setIndicator("公司",
				getResources().getDrawable(R.drawable.tab_company_selector));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("report");
		spec.setContent(new Intent(this, TaskListActivity.class));
		spec.setIndicator("计划任务",
				getResources().getDrawable(R.drawable.tab_plan_selector));
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("knowleage");
		spec.setContent(new Intent(this, SettingActivity.class));
		spec.setIndicator("设置",
				getResources().getDrawable(R.drawable.tab_setting_selector));
		tabHost.addTab(spec);
		
		tabWidget = tabHost.getTabWidget();
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				SELECTED_TAB = tabId;
				setTabBackground();
			}
		});
		
		setTabBackground();

		/*for (int i = 0; i < tabWidget.getChildCount(); i++) {
			View v = tabWidget.getChildAt(i);
			int currntTab = tabHost.getCurrentTab();
			if(currntTab == i){
				v.setBackgroundResource(R.drawable.tab_select); //选中
			}else{
				v.setBackgroundResource(R.drawable.tab_unselect); //非选中
			}
		}*/
	}
	/**
	 * 将选中的Tab索引由字符串转换为数字
	 * @param tab
	 * @return
	 */
	private int getSelectedTabIndex(String tab) {
		if (tab.equals("index")) {
			return 0;
		} else if (tab.equals("account")) {
			return 1;
		} else if (tab.equals("activity")) {
			return 2;
		}else if (tab.equals("report")) {
			return 3;
		}else if (tab.equals("knowleage")){
			return 4;
		}
		return 0;
	}
	
	/**
	 * 设置Tab背景图片：选中状态/非选中状态
	 */
	private void setTabBackground() {
		int count = tabWidget.getTabCount();
		for (int i = 0; i < count; i++) {
			if (i == getSelectedTabIndex(SELECTED_TAB)) {
				tabWidget.getChildAt(i).setBackgroundResource(
						R.drawable.tab_select); // 选中状态图片
			} else {
				tabWidget.getChildAt(i).setBackgroundResource(
						R.drawable.tab_unselect); // 非选中状态图片
			}
		}
	}

}
