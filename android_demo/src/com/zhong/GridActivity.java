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
//			map.put("ItemText", "�� " + i);
//			list.add(map);
//		}
		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("ItemImage", R.drawable.s1);
		map0.put("ItemText", "�����ϴ�");
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.s2);
		map1.put("ItemText", "����Ƭ");
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.s3);
		map2.put("ItemText", "д��־");
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.s4);
		map3.put("ItemText", "��״̬");
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemImage", R.drawable.s5);
		map4.put("ItemText", "������");
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("ItemImage", R.drawable.s6);
		map5.put("ItemText", "������ҳ");
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("ItemImage", R.drawable.s7);
		map6.put("ItemText", "����");
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		map7.put("ItemImage", R.drawable.s8);
		map7.put("ItemText", "�ص�");
		
		list.add(map0);
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		list.add(map5);
		list.add(map6);
		list.add(map7);
		

		SimpleAdapter saMenuItem = new SimpleAdapter(this, list, // ����Դ
				R.layout.grid_item, // xmlʵ��
				new String[] { "ItemImage", "ItemText" }, // ��Ӧmap��Key
				new int[] { R.id.ItemImage, R.id.ItemText }); // ��ӦR��Id

		// ���Item��������
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