package com.zhongcw.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zhongcw.R;

public class IntentActivity extends Activity {
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		Button b = (Button) findViewById(R.id.btn_hello);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Intent i = new Intent(Intent.ACTION_PICK,
				// ContactsContract.Contacts.CONTENT_URI);
				// startActivityForResult(i, 0);

				Intent i = new Intent(Intent.ACTION_GET_CONTENT);
				i.setType(Phone.CONTENT_ITEM_TYPE);
				startActivityForResult(i, 0);
			}
		});

		tv = (TextView) findViewById(R.id.tv);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 0:
			if (data == null) {
				return;
			}
			Uri uri = data.getData();
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			cursor.moveToFirst();
			String name = cursor
					.getString(cursor
							.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
			String number = cursor.getString(cursor
					.getColumnIndexOrThrow(Phone.NUMBER));

			tv.setText(name + "," + number);
			break;
		default:
			break;
		}
	}
}
