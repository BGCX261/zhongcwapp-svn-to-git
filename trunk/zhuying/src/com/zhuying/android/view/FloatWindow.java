//package com.zhuying.android.view;
//
//import com.zhuying.android.R;
//
//import android.content.Context;
//import android.graphics.PixelFormat;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.zhuying.android.activity.TaskListActivity;
//
//
///**
// * 悬浮窗口
// * 
// * 单例模式处理来电/去电悬浮窗口创建、删除
// */
//public class FloatWindow {
//	private static final String TAG = "FloatWindow";
//	private static FloatWindow floatWindow;
//
//	private static WindowManager wm;
//	private static View floatView;
//	private static LayoutInflater inflater;
//	
//	private Context context;
//
//	public static FloatWindow getInstance(Context context) {
//		if (floatWindow == null) {
//			floatWindow = new FloatWindow(context);
//		}
//		return floatWindow;
//	}
//
//	private FloatWindow(Context context) {
//		this.context = context;
//		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		inflater = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		floatView = inflater.inflate(R.layout.float_window, null, false);
//	}
//
//	/**
//	 * 创建来电/去电悬浮窗口
//	 */
//	public void createFloatWindow() {
//		((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_up_arrow);
//		
//		TextView all =  (TextView) floatView.findViewById(R.id.all);
//		all.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeFloatWindow();
////				Toast.makeText(context, "全部", Toast.LENGTH_SHORT).show();
//				((TaskListActivity)context).doList("all");
//				((TaskListActivity)context).title.setText("计划任务(全部)");
//				((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_down_arrow);
//			}
//		});
//		
//		TextView outdate =  (TextView) floatView.findViewById(R.id.outdate);
//		outdate.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeFloatWindow();
//				((TaskListActivity)context).doList("outdate");
//				((TaskListActivity)context).title.setText("计划任务(已过期)");
//				((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_down_arrow);
//			}
//		});
//		
//		TextView today =  (TextView) floatView.findViewById(R.id.today);
//		today.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeFloatWindow();
////				Toast.makeText(context, "今天", Toast.LENGTH_SHORT).show();
//				((TaskListActivity)context).doList("today");
//				((TaskListActivity)context).title.setText("计划任务(今天)");
//				((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_down_arrow);
//			}
//		});
//		
//		TextView tomorrow =  (TextView) floatView.findViewById(R.id.tomorrow);
//		tomorrow.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeFloatWindow();
//				((TaskListActivity)context).doList("tomorrow");
//				((TaskListActivity)context).title.setText("计划任务(明天)");
//				((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_down_arrow);
//			}
//		});
//		
//		TextView thisweek =  (TextView) floatView.findViewById(R.id.thisweek);
//		thisweek.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeFloatWindow();
//				((TaskListActivity)context).doList("thisweek");
//				((TaskListActivity)context).title.setText("计划任务(本周)");
//				((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_down_arrow);
//			}
//		});
//		
//		TextView nextweek =  (TextView) floatView.findViewById(R.id.nextweek);
//		nextweek.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeFloatWindow();
//				((TaskListActivity)context).doList("nextweek");
//				((TaskListActivity)context).title.setText("计划任务(下周)");
//				((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_down_arrow);
//			}
//		});
//		
//		TextView specify =  (TextView) floatView.findViewById(R.id.specify);
//		specify.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeFloatWindow();
//				((TaskListActivity)context).doList("specify");
//				((TaskListActivity)context).title.setText("计划任务(更迟的)");
//				((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_down_arrow);
//			}
//		});
//		
//		TextView finish =  (TextView) floatView.findViewById(R.id.finish);
//		finish.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				removeFloatWindow();
//				((TaskListActivity)context).doList("finish");
//				((TaskListActivity)context).title.setText("计划任务(已完成)");
//				((TaskListActivity)context).arrowView.setBackgroundResource(R.drawable.task_down_arrow);
//			}
//		});
//
////		Log.d("isShown", floatView.isShown() + " ");
//		if (!floatView.isShown()) {
//			wm.addView(floatView, setFloatWindowLayoutParam());
//		}
//		
//	}
//
//	/**
//	 * 删除来电/去电悬浮窗口
//	 */
//	public void removeFloatWindow() {
//		try {
//			if (floatView != null) {
//				wm.removeView(floatView);
//			}
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * 悬浮窗口布局参数
//	 * 
//	 * @return
//	 */
//	private WindowManager.LayoutParams setFloatWindowLayoutParam() {
//		WindowManager.LayoutParams param = new WindowManager.LayoutParams();
//		param.type = WindowManager.LayoutParams.TYPE_PHONE; // type是关键，这里的2002表示系统级窗口，你也可以试试2003
//		param.width = WindowManager.LayoutParams.WRAP_CONTENT;
//		param.height = WindowManager.LayoutParams.WRAP_CONTENT;
//		param.format = PixelFormat.RGBA_8888;
//		param.gravity = Gravity.TOP;
//		param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//				| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
//		
//		param.verticalMargin = 0.065f; //百分比
//		return param;
//	}
//
//}
