package com.zhongcw.util;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;

/**
 * 手机联系人工具类
 */
public class ContactUtil extends Activity {
	private static final String TAG = "ContactUtil";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// String contactName = readContact("13522256966");
		// Log.d(TAG, "contactName = " + contactName);

		queryContact("13522256966");
	}

	/**
	 * 读联系人
	 * 
	 * @param phoneNumber
	 *            联系人手机号
	 * @return 联系人名称
	 */
	private String readContact(String phoneNumber) {
		// Cursor c = getContentResolver().query(
		// ContactsContract.Data.CONTENT_URI, null, null, null, null);
		// while (c.moveToNext()) {
		// // int contactNameIndex = c.getColumnIndex(People.NAME);
		// // String contactName = c.getString(contactNameIndex);
		// int phoneNumberIndex = c
		// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1);
		// String phone = c.getString(phoneNumberIndex);
		// Log.d(TAG, "phoneNumber = " + phone);
		// }
		// return "";

		Cursor cursor = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (cursor.moveToNext()) {
			// 取得联系人名字
			int nameFieldColumnIndex = cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME);
			String name = cursor.getString(nameFieldColumnIndex);
			Log.d(TAG, "name=" + name);
			// 取得联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phone = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);

			// 取得电话号码(可能存在多个号码)
			while (phone.moveToNext()) {
				String strPhoneNumber = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				Log.d(TAG, "strPhoneNumber=" + strPhoneNumber);
			}
			phone.close();
		}
		cursor.close();

		return "";
	}

	private void queryContact(String callingNumber) {
		Cursor phone = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // data表
				null,
				ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
						+ callingNumber + "' ", null, null);
		while (phone.moveToNext()) {
			// int index = phone
			// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
			// String contactName = phone.getString(index);
			int raw_contact_id_index = phone
					.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID);
			String raw_contact_id = phone.getString(raw_contact_id_index);
			// Log.d(TAG, "contactName = " + contactName);
			Log.d(TAG, "raw_contact_id = " + raw_contact_id);
			Cursor cur = getContentResolver().query(
					ContactsContract.Contacts.CONTENT_URI, // contact表
					null,
					ContactsContract.Contacts._ID + " = " + raw_contact_id,
					null, null);
			while (cur.moveToNext()) {
				int display_name_index = cur
						.getColumnIndex(PhoneLookup.DISPLAY_NAME);
				String display_name = cur.getString(display_name_index);
				Log.d(TAG, "display_name = " + display_name);
			}

		}
	}
}
