package com.zhuying.android.async;


import android.content.Context;
import android.widget.Toast;

public abstract class UITask extends BaseAsyncTask<Void, Integer, Result> {
	public UITask(Context ctx) {
		super(ctx);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
	}

	@Override
	protected void onPostExecute(Result result) {
		String msg = null;
		if (result.isSuccess()) {
			msg = result.getMsg();
		} else {
			msg = "操作失败：" + result.getMsg();
		}
		Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
		toast.show();
		super.onPostExecute(result);
	}
}
