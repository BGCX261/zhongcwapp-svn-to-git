package com.zhuying.android.view;


import java.util.ArrayList;
import java.util.List;

import com.zhuying.android.adapter.ContactCursorAdapter;
import com.zhuying.android.adapter.ContactListAdapter;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * 拼音索引条
 */
public class PinYinIndexView extends View {
	private int width;
	private int height;
	private int offsetHeight = 6; // 高度偏移量

	private String[] alphabetArray = new String[] { "A", "B", "C", "D", "E",
			"F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
			"S", "T", "U", "V", "W", "X", "Y", "Z" }; // 字母表
	private List listViewIndexList = new ArrayList(); // ListView索引值集合

	private static String COLOR_GRAY = "#C0C0C0"; // 灰色
	private static String COLOR_BLUE = "#F0FFFF"; // 天蓝色

	private ContactCursorAdapter listAdapter;
	private ListView listView;
	
	private static final String TAG = "PinYinIndexView";

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public ContactCursorAdapter getListAdapter() {
		return listAdapter;
	}

	public void setListAdapter(ContactCursorAdapter listAdapter) {
		this.listAdapter = listAdapter;
	}

	public PinYinIndexView(Context context) {
		this(context, null);
	}

	/**
	 * 2个参数构造器 用于XML布局文件
	 * 
	 * @param context
	 * @param attrs
	 */
	public PinYinIndexView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setFocusable(true);
		setFocusableInTouchMode(true);

		setBackgroundColor(Color.parseColor(COLOR_GRAY));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		for (int i = 0; i < alphabetArray.length; i++) {
			Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
			foreground.setFakeBoldText(true);
			foreground.setColor(Color.GRAY);
			foreground.setStyle(Style.FILL);
			foreground.setTextSize(height * 0.75f);
			foreground.setTextAlign(Paint.Align.CENTER);
			FontMetrics fm = foreground.getFontMetrics();
			float x = width / 2;
			float y = height / 2 - (fm.ascent + fm.descent) / 2;
			// 注意：高度后面所加的6是为了改变位置效果的，在onTouch方法中相关的计算中需要减6
			canvas.drawText(alphabetArray[i], x, i * height + y + offsetHeight,
					foreground);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				|| event.getAction() == MotionEvent.ACTION_MOVE) {
			int tempY = (int) ((event.getY() - offsetHeight) / height);
			Log.d(TAG, "tempY = "+tempY);
			// 判断Touch的位置是否在View的范围内，并且索引值集合内含有所选择的值
			if (tempY < alphabetArray.length) {
				listAdapter = (ContactCursorAdapter) getListAdapter();
				String[] sectionAry = (String[]) listAdapter.getSections();
				Log.d(TAG, "sectionAry = "+sectionAry);
				if (sectionAry != null) {
					for (int i = 0; i < sectionAry.length; i++) {
						String section = sectionAry[i];
						listViewIndexList.add(section);
					}
					// =====================
					if (event.getY() > 0
							&& listViewIndexList
									.contains(alphabetArray[tempY])) {
						String alpha = alphabetArray[tempY]; //点击字母
						Log.d(TAG, "alpha = "+alpha);
						
						int section = listViewIndexList.indexOf(alpha);
						Log.d(TAG, "section = "+section);
						
						int position = listAdapter.getPositionForSection(section);
						Log.d(TAG, "position = "+position);
						
						getListView().setSelection(position);
					}
					setBackgroundColor(Color.parseColor(COLOR_BLUE));
				}
			
			}
			return true;
		} else {
			setBackgroundColor(Color.parseColor(COLOR_GRAY));
			return super.onTouchEvent(event);
		}
	}

	/**
	 * 当View被实例时首先运行的方法，早于onDraw()方法 给自定义的宽度和高度赋值
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h / alphabetArray.length;

		super.onSizeChanged(w, h, oldw, oldh);
	}
}
