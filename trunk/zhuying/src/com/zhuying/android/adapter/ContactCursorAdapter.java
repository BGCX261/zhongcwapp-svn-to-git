package com.zhuying.android.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

import com.zhuying.android.R;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.PhotoEntity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ContactCursorAdapter extends SimpleCursorAdapter implements SectionIndexer{
	private HashMap<String, Integer> alphaIndexer;// 保存每个索引在list中的位置【#-0，A-4，B-10】
	public String[] sections;// 每个分组的索引表【A,B,C,F...】
	String currentStr;
	private Context context;
	
	public ContactCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		
		this.context = context;
		
		this.alphaIndexer = new HashMap<String, Integer>();
		for (int i = 0; i < c.getCount(); i++) {
			c.moveToPosition(i);
			String sortKey = c.getString(18);
			String name = getAlpha(sortKey);
			if (!alphaIndexer.containsKey(name)) {// 只记录在list中首次出现的位置
				alphaIndexer.put(name, i);
			}
		}
		
		Set<String> sectionLetters = alphaIndexer.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		sections = new String[sectionList.size()];
		sectionList.toArray(sections);
	}
	
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		super.bindView(view, context, cursor);
		
		ImageView photoView = (ImageView) view.findViewById(R.id.imageView);
		TextView alphaView = (TextView) view.findViewById(R.id.alpha);
		
		String partyFace = cursor.getString(13);
		if (!TextUtils.isEmpty(partyFace)) {
			Cursor photoCursor = context.getContentResolver()
					.query(PhotoEntity.CONTENT_URI, null,
							PhotoEntity.KEY_NAME + " = '" + partyFace + "'",
							null, null);
			if (photoCursor.moveToFirst()) {
				String content = photoCursor.getString(4);
				byte[] data = Base64.decode(content, Base64.DEFAULT);
				Bitmap icon = BitmapFactory.decodeByteArray(data, 0,
						data.length);
				photoView.setImageBitmap(icon);
			} 
			else {
				Drawable d = context.getResources().getDrawable(R.drawable.contact_icon);
				Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
				photoView.setImageBitmap(bitmap);
			}
			photoCursor.close();
		}else{
			Drawable d = context.getResources().getDrawable(R.drawable.contact_icon);
			Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
			photoView.setImageBitmap(bitmap);
		}
		
		//当前联系人的sortKey
		currentStr = getAlpha(cursor.getString(18)); 
		// 上一个联系人的sortKey
		String previewStr;
		if(cursor.moveToPrevious()){
			previewStr = getAlpha(cursor.getString(18));
		}else{
			previewStr = " ";
		}
		/**
		 * 判断显示#、A-Z的TextView隐藏与可见
		 */
		if (!previewStr.equals(currentStr)) { // 当前联系人的sortKey！=上一个联系人的sortKey，说明当前联系人是新组。
			alphaView.setVisibility(View.VISIBLE);
			alphaView.setText(currentStr);
		} else {
			alphaView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]{1}");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // 大写输出
		} else {
			return "#";
		}
	}
	
	/*
	 * 此方法根据联系人的首字母返回在list中的位置
	 */
	@Override
	public int getPositionForSection(int section) {
		String later = sections[section];
		return alphaIndexer.get(later);
	}

	/*
	 * 本例中可以不考虑这个方法
	 */
	@Override
	public int getSectionForPosition(int position) {
		String key = getAlpha(currentStr);
		for (int i = 0; i < sections.length; i++) {
			if (sections[i].equals(key)) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public Object[] getSections() {
		return sections;
	}

}
