package com.zhuying.android.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract.Constants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.zhuying.android.R;
import com.zhuying.android.util.PinYinComparator;

/**
 * 联系人列表适配器
 * 其他项目使用时，只需要传进来一个有序的list即可
 */
public class ContactListAdapter extends BaseAdapter implements SectionIndexer {
	private LayoutInflater inflater;
	private List<ContentValues> list;
	private HashMap<String, Integer> alphaIndexer;// 保存每个索引在list中的位置【#-0，A-4，B-10】
	public String[] sections;// 每个分组的索引表【A,B,C,F...】
	private Context ctx;

	public ContactListAdapter(Context context, List<ContentValues> list) {
		this.ctx = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list; // 该list是已经排序过的集合，有些项目中的数据必须要自己进行排序。
		this.alphaIndexer = new HashMap<String, Integer>();

		// 对list排序
		Collections.sort(list, new PinYinComparator());

		for (int i = 0; i < list.size(); i++) {
			String name = getAlpha(list.get(i).getAsString("sort_key"));
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
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.contact_list_item, null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.image = (ImageView) convertView
					.findViewById(R.id.imageView);
			// holder.number = (TextView) convertView
			// .findViewById(R.id.number);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ContentValues cv = list.get(position);
		final String name = cv.getAsString("name");
//		String imageUri = cv.getAsString(IMAGEURI);
		// String number = cv.getAsString(NUMBER);
		holder.name.setText(name);
//		holder.image.setUrl(Constants.HOST + imageUri);
		// holder.number.setText(number);

		// 当前联系人的sortKey
		String currentStr = getAlpha(list.get(position).getAsString("sort_key"));
		// 上一个联系人的sortKey
		String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
				position - 1).getAsString("sort_key")) : " ";
		/**
		 * 判断显示#、A-Z的TextView隐藏与可见
		 */
		if (!previewStr.equals(currentStr)) { // 当前联系人的sortKey！=上一个联系人的sortKey，说明当前联系人是新组。
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(currentStr);
		} else {
			holder.alpha.setVisibility(View.GONE);
		}
		return convertView;
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
		String key = getAlpha(list.get(position).getAsString("sort_key"));
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
	
	class ViewHolder {
		TextView alpha;
		TextView name;
		ImageView image;
		// TextView number;
		Button privateMsg; // 私信按钮
	}
}
