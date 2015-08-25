package com.zhongcw.ui.widget.dialog;


import com.zhongcw.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义对话框
 * 
 */
public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context, R.style.dialog);

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		layout.setLayoutParams(new LayoutParams(200, 80));
		layout.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.loading_bg));

		LoadingCarToonView lct = new LoadingCarToonView(context);
		layout.addView(lct, new LayoutParams(80, 80));

		TextView text = new TextView(context);
		text.setText(context.getResources().getString(
				R.string.progressDialog_message));
		layout.addView(text, new LayoutParams(140, 100));

		setContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
	}

}
