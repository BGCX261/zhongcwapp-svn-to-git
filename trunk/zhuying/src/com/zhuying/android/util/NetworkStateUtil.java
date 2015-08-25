package com.zhuying.android.util;

import com.zhuying.android.activity.LoginActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * 网络状态工具类
 */
public class NetworkStateUtil {
	private static final String TAG = "NetworkStateUtil";

//	private Context context;
//
//	public NetworkStateUtil(Context ctx) {
//		this.context = ctx;
//	}

	/**
	 * 检测网络连接是否可用
	 * 
	 * @return
	 */
	public static boolean checkNetworkInfo(Context ctx) {
		boolean available = false;
		ConnectivityManager conMan = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobile = conMan
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		Log.d(TAG, "mobile = " + mobile + ", wifi = " + wifi);

		if (mobile.isConnected() || wifi.isConnected()) {
			available = true;
		} else {
			showNetworkUnavailableDialog(ctx);
		}
		return available;
	}
	
	/**
	 * 检测wifi是否可用
	 * @return
	 */
	public static boolean checkWifi(Context ctx) {
		boolean available = false;
		ConnectivityManager conMan = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		Log.d(TAG, " wifi = " + wifi);

		if (wifi.isConnected()) {
			available = true;
		} else {

		}
		return available;
	}
	
	/**
	 * 网络不可用提示
	 * @param ctx
	 */
	private static void showNetworkUnavailableDialog(Context ctx){
		new AlertDialog.Builder(ctx).setTitle("提示").setMessage("网络问题，请稍后重试！")
				.setCancelable(false).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
							}
						}).create().show();
	
	}
}
