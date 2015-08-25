package com.zhong;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

public class GridActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);

		GridView gridview = (GridView) findViewById(R.id.GridView);
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

//		for (int i = 0; i < 8; i++) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("ItemImage", "R.drawable.sample_"+i);
//			map.put("ItemText", "项 " + i);
//			list.add(map);
//		}
		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("ItemImage", R.drawable.s1);
		map0.put("ItemText", "拍照上传");
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.s2);
		map1.put("ItemText", "传照片");
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.s3);
		map2.put("ItemText", "写日志");
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.s4);
		map3.put("ItemText", "发状态");
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemImage", R.drawable.s5);
		map4.put("ItemText", "新鲜事");
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("ItemImage", R.drawable.s6);
		map5.put("ItemText", "个人主页");
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("ItemImage", R.drawable.s7);
		map6.put("ItemText", "好友");
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		map7.put("ItemImage", R.drawable.s8);
		map7.put("ItemText", "地点");
		
		list.add(map0);
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		list.add(map6);
		list.add(map7);
		

		SimpleAdapter saMenuItem = new SimpleAdapter(this, list, // 数据源
				R.layout.grid_item, // xml实现
				new String[] { "ItemImage", "ItemText" }, // 对应map的Key
				new int[] { R.id.ItemImage, R.id.ItemText }); // 对应R的Id

		// 添加Item到网格中
		gridview.setAdapter(saMenuItem);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				System.out.println("click index:" + arg2);
				Toast.makeText(GridActivity.this, "" + arg2,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}