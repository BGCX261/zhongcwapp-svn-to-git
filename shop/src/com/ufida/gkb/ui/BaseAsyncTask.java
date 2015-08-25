package com.ufida.gkb.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;

import com.ufida.gkb.util.Constants;

public abstract class BaseAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	protected ProgressDialog progressDialog;
	protected Context ctx;

	public BaseAsyncTask(Context ctx) {
		this.ctx = ctx;
	}

	@Override
	protected void onPostExecute(Result result) {
		progressDialog.dismiss();
		if (result == null) {
			new AlertDialog.Builder(ctx)
					.setTitle(Constants.MSG_NETWORK_ERROR)
					.setCancelable(false)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
//									Intent intent = new Intent(ctx,
//											LoginActivity.class);
//									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//									ctx.startActivity(intent);
								}
							}).create().show();
		}
	}

	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(ctx, "", "请等待", true, false);
	}

}
