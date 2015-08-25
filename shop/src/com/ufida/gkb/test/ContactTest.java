package com.ufida.gkb.test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.widget.Toast;

public class ContactTest extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// writeContactNew();
		addContact("北上广", "13488758270");
	}

	// public ContentValues getSimpleCV() {
	// ContentValues cv = new ContentValues();
	// cv.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
	// cv.put(StructuredName.DISPLAY_NAME, "三 张");
	// return cv;
	// }
	//
	// public ContentValues getPhoneCV() {
	// ContentValues cv = new ContentValues();
	// cv.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
	// cv.put(Phone.NUMBER, "10086");
	// cv.put(Phone.TYPE, Phone.TYPE_COMPANY_MAIN);
	// return cv;
	// }

	/**
	 * 写联系人
	 */
	private void writeContact() {
		ContentValues values = new ContentValues();
		values.put(People.NAME, "旺铺2"); // 名称
		values.put(Contacts.People.STARRED, 0);

		long pid = 1;
		Uri newPerson = Contacts.People.addToGroup(getContentResolver(), pid,
				"shop");
		// Uri newPerson = Contacts.People.createPersonInMyContactsGroup(
		// getContentResolver(), values);

		Uri numberUri = null;
		values.clear();
		numberUri = Uri.withAppendedPath(newPerson,
				People.Phones.CONTENT_DIRECTORY);
		values.put(Contacts.Phones.TYPE, People.Phones.TYPE_MOBILE); // 手机
		values.put(People.NUMBER, "13987654321");
		getContentResolver().insert(numberUri, values);
	}

	//
	// /**
	// * 读联系人
	// */
	// private void readContact() {
	// Cursor c = getContentResolver().query(People.CONTENT_URI, null, null,
	// null, null);
	// while (c.moveToNext()) {
	// int contactNameIndex = c.getColumnIndex(People.NAME);
	// String contactName = c.getString(contactNameIndex);
	// int phoneNumberIndex = c.getColumnIndex(People.NUMBER);
	// String phoneNumber = c.getString(phoneNumberIndex);
	// Toast.makeText(ContactTest.this, contactName + "_" + phoneNumber,
	// Toast.LENGTH_SHORT).show();
	// }
	// }

	private void writeContactNew() {
		ContentValues values = new ContentValues();
		values.putNull(RawContacts.ACCOUNT_TYPE);
		values.putNull(RawContacts.ACCOUNT_NAME);
		Uri rawContactUri = getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);

		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.DISPLAY_NAME, "旺铺");
		getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// values.put(Data.RAW_CONTACT_ID, rawContactId);
		// values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		// values.put(Phone.NUMBER, "13123456789");
		// values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		// getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI,
		// values);

	}

	public void addContact(String name, String phone) {
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops
				.add(ContentProviderOperation
						.newInsert(ContactsContract.RawContacts.CONTENT_URI)
						.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE,
								null)
						.withValue(ContactsContract.RawContacts.ACCOUNT_NAME,
								null)
						.withValue(
								ContactsContract.RawContacts.AGGREGATION_MODE,
								ContactsContract.RawContacts.AGGREGATION_MODE_DISABLED)
						.build());

		// add name
		ops
				.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(
								ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(
								ContactsContract.Data.MIMETYPE,
								ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
						.withValue(
								ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
								name).build());

		// add phone
		ops
				.add(ContentProviderOperation
						.newInsert(ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(
								ContactsContract.Data.RAW_CONTACT_ID, 0)
						.withValue(
								ContactsContract.Data.MIMETYPE,
								ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
						.withValue(
								ContactsContract.CommonDataKinds.Phone.NUMBER,
								phone)
						.withValue(
								ContactsContract.CommonDataKinds.Phone.TYPE,
								ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
						.build());

		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {

		}
	}
}
