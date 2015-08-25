package com.zhongcw.activity;

import com.zhongcw.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DialogActivity extends Activity {
	private static final int DIALOG_SINGLE_CHOICE = 5;
	final CharSequence[] items = { "Red", "Green", "Blue" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button b = (Button) findViewById(R.id.btn_hello);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_SINGLE_CHOICE);
			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SINGLE_CHOICE:
			return new AlertDialog.Builder(DialogActivity.this).setTitle(
					"Pick a color").setSingleChoiceItems(items, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							Toast.makeText(getApplicationContext(),
									items[item], Toast.LENGTH_SHORT).show();
							dialog.cancel();
						}
					}).create();
		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.onCreateDialog(id);
	}

}
