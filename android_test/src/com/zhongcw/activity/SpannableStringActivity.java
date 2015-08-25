package com.zhongcw.activity;

import com.zhongcw.R;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

public class SpannableStringActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView txtInfo = new TextView(this);
		SpannableString ss = new SpannableString("红色打电话斜体删除线绿色下划线图片:.");
//		ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2,
//				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		ss.setSpan(new URLSpan("tel:4155551212"), 2, 5,
//				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 7,
//				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		ss.setSpan(new StrikethroughSpan(), 0, 16,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		ss.setSpan(new UnderlineSpan(), 10, 16,
//				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		ss.setSpan(new ForegroundColorSpan(Color.GREEN), 10, 15,
//				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//		Drawable d = getResources().getDrawable(R.drawable.icon);
//		d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//		ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
//		ss.setSpan(span, 18, 19, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		txtInfo.setText(ss);
//		txtInfo.setMovementMethod(LinkMovementMethod.getInstance());
		setContentView(txtInfo);
	}
}
