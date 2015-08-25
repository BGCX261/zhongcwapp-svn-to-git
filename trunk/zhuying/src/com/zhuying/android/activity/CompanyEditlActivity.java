package com.zhuying.android.activity;

import java.io.FileNotFoundException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuying.android.R;
import com.zhuying.android.async.Result;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.service.CompanyContactSyncService;
import com.zhuying.android.service.PhotoSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.HanziToPinyin;
import com.zhuying.android.util.ImageUtil;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.StringUtil;
import com.zhuying.android.util.UUIDUtil;

/**
 *公司新增/编辑
 */
public class CompanyEditlActivity extends Activity {
	private int mState; //新增/编辑标识
	private static final int STATE_EDIT = 0;
	private static final int STATE_INSERT = 1;
	
	private EditText nameEditText;
	
	private CompanyEntity entity;
	
	private Cursor mCursor;
	private Uri uri;
	private String id; //主键
	
	//+-号
//	private LinearLayout mobileLayout;
//	private LinearLayout addressLayout;
//	private LinearLayout websiteLayout;
	
	private int mobileCount = 0; //点击+号次数
	private int addressCount = 0;
	private int websiteCount = 0;
	
	private static final int DIALOG_MOBILE_CHOICE = 0;
	private static final int DIALOG_ADDRESS_CHOICE = 2;
	private static final int DIALOG_WEBSITE_CHOICE = 4;
	
	final CharSequence[] mobileItems = new String[]{"手机","住宅","工作"};
	final CharSequence[] addressItems = new String[]{"住宅","工作"};
	final CharSequence[] websiteItems = new String[]{"工作","个人"};
	
	private Button currentMobileBtn;
	private Button currentAddressBtn;
	private Button currentWebsiteBtn;
	
	private Bundle bundle;
	
	private LinearLayout mobile_row_layout;
	private LinearLayout address_row_layout;
	private LinearLayout website_row_layout;
	
	
	/**
	 * 上传头像
	 */
	private RelativeLayout photoView;
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 4023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 4021;
	private Bitmap bitMap; // 用来保存图片
	private boolean hasImage; // 是否已经选择了图片
	PhotoEntity photo = new PhotoEntity();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.company_edit);
		initUI();

		Intent intent = getIntent();
		bundle = intent.getExtras();
//		uri = intent.getData();
		String action = intent.getAction();
		if(Intent.ACTION_EDIT.equals(action)){
			mState = STATE_EDIT;
		}else if(Intent.ACTION_INSERT.equals(action)){
			mState = STATE_INSERT;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		switch (mState) {
		case STATE_EDIT:
//			id = ContentUris.parseId(uri);
			id = bundle.get("id").toString();
			mCursor = managedQuery(CompanyEntity.CONTENT_URI, null, CompanyEntity.KEY_PARTYID + " = '" + id +"'", null, null);
			mCursor.moveToFirst();
			entity = new CompanyEntity(mCursor);
			nameEditText.setText(entity.getName());
			
			//电话
			String phoneJSON = entity.getPhone();
			mobile_row_layout.removeAllViews();
			try {
				if(!TextUtils.isEmpty(phoneJSON)){
					JSONArray phoneAry = new JSONArray(phoneJSON);
					for(int i=0;i<phoneAry.length();i++){
						JSONObject json = (JSONObject) phoneAry.get(i);
						genMobileLayout(true,json.getString("phoneType"), json.getString("phoneText"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//地址
			String addressJSON = entity.getAddress();
			address_row_layout.removeAllViews();
			try {
				if(!TextUtils.isEmpty(addressJSON)){
					JSONArray addressAry = new JSONArray(addressJSON);
					for(int i=0;i<addressAry.length();i++){
						JSONObject json = (JSONObject) addressAry.get(i);
						genAddressLayout(true,json.getString("addressType"), json.getString("addressText"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//网址
			String website = entity.getWebsites();
			website_row_layout.removeAllViews();
//			genWebsiteLayout(true,null, website);
			try {
				if(!TextUtils.isEmpty(website)){
					JSONArray webAry = new JSONArray(website);
					for(int i=0;i<webAry.length();i++){
						JSONObject json = (JSONObject) webAry.get(i);
						genWebsiteLayout(true,json.getString("websitesType"), json.getString("websitesText"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			//头像
			if(hasImage){ //选择过上传头像
				
			}else{
				String partyFace = entity.getPartyface();
				if(! StringUtil.isEmpty(partyFace)){
					Cursor photoCursor = managedQuery(PhotoEntity.CONTENT_URI, null, PhotoEntity.KEY_NAME + " = '" + partyFace +"'", null, null);
					if(photoCursor.moveToFirst()){
						String content = photoCursor.getString(4);
						byte[] data = Base64.decode(content, Base64.DEFAULT);
						Bitmap icon = BitmapFactory.decodeByteArray(data, 0, data.length);
//						photoView.setImageBitmap(icon);
						icon = ImageUtil.zoomBitmap(icon,80,80);
						BitmapDrawable bd = new BitmapDrawable(icon);
						photoView.setBackgroundDrawable(bd);
					}
					photoCursor.close();
				}
			}
			
			break;
		case STATE_INSERT: 
			entity = new CompanyEntity();
			
			//生成默认
			mobile_row_layout.removeAllViews();
			genMobileLayout(false,null,null);
			
			address_row_layout.removeAllViews();
			genAddressLayout(false,null,null);
			
			website_row_layout.removeAllViews();
			genWebsiteLayout(false,null, null);
			
			break;
		default:
			break;
		}
	}

	private void initUI() {
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("公司新增");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setText("保存");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String companyName = nameEditText.getText().toString();
				if(TextUtils.isEmpty(companyName)){
					Toast.makeText(getApplicationContext(), "公司不能为空", Toast.LENGTH_SHORT).show();
					return; 
				}
				
				entity.setPartytype(CompanyEntity.TYPE_COMPANY);
				entity.setName(companyName);
				entity.setSortKey(HanziToPinyin.getPinYin(nameEditText.getText().toString()));
				
				//电话
				JSONArray phoneAry = new JSONArray();
				for (int i = 0; i < mobile_row_layout.getChildCount(); i++) { //获取手机号。从1开始，排除RelativeLayout
					RelativeLayout mobileMinusLayout = (RelativeLayout) mobile_row_layout.getChildAt(i);
					Button label = (Button) mobileMinusLayout.getChildAt(0);
					EditText value = (EditText) mobileMinusLayout.getChildAt(1);
					if(! StringUtil.isEmpty(value.getText().toString())){
						JSONObject phone = new JSONObject();
						try {
							phone.put("phoneType", label.getText().toString());
							phone.put("phoneText", value.getText().toString());
							
							phoneAry.put(phone);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				if(phoneAry.length() > 0){
					entity.setPhone(phoneAry.toString());
				}
				
				//地址
				JSONArray addressAry = new JSONArray();
				for (int i = 0; i < address_row_layout.getChildCount(); i++) {
					RelativeLayout addressMinusLayout = (RelativeLayout) address_row_layout.getChildAt(i);
					Button label = (Button) addressMinusLayout.getChildAt(0);
					EditText value = (EditText) addressMinusLayout.getChildAt(1);
					if(! StringUtil.isEmpty(value.getText().toString())){
						JSONObject address = new JSONObject();
						try {
							address.put("addressType", label.getText().toString());
							address.put("addressText", value.getText().toString());
							address.put("province", "");
							address.put("city", "");
							address.put("zip", "");
							addressAry.put(address);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				if(addressAry.length()>0){
					entity.setAddress(addressAry.toString());
				}
				
				
				//Website
				JSONArray websiteAry = new JSONArray();
				for (int i = 0; i < website_row_layout.getChildCount(); i++) {
					RelativeLayout websiteMinusLayout = (RelativeLayout) website_row_layout.getChildAt(i);
					Button label = (Button) websiteMinusLayout.getChildAt(0);
					EditText value = (EditText) websiteMinusLayout.getChildAt(1);
					if(! StringUtil.isEmpty(value.getText().toString())){
						JSONObject website = new JSONObject();
						try {
							website.put("websitesType", label.getText().toString());
							website.put("websitesText", value.getText().toString());
							websiteAry.put(website);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
				if(websiteAry.length() > 0){
					entity.setWebsites(websiteAry.toString());
				}
				
				switch (mState) {
				case STATE_EDIT:
					entity.setUpdatedat(DateTimeUtil.format(new Date()));
					getContentResolver().update(CompanyEntity.CONTENT_URI, entity.toContentValues(), CompanyEntity.KEY_PARTYID + " = '" + id +"'", null);
					
					//更新最近行动表
					Cursor actionCursor = managedQuery(ActionEntity.CONTENT_URI, null, ActionEntity.KEY_SUBJECTID + " = '" + entity.getPartyId() +"'", null, null);
					while (actionCursor.moveToNext()) {
						actionCursor.moveToFirst();
						ActionEntity action = new ActionEntity(actionCursor);
//						actionCursor.close();
						action.setSubjectname(entity.getName());
						getContentResolver().update(ActionEntity.CONTENT_URI, action.toContentValues(), ActionEntity.KEY_SUBJECTID+ " = '" + entity.getPartyId() +"'", null);
						break;
					}
					
					if(bitMap != null){ //用户上传头像
						genPhoto();
						
						Cursor photoCursor = managedQuery(PhotoEntity.CONTENT_URI, null, "partyid = ?", new String[]{entity.getPartyId()}, null);
						if(photoCursor.getCount() > 0 ){ // 有记录，更新
							getContentResolver().update(PhotoEntity.CONTENT_URI, photo.toContentValues(), PhotoEntity.KEY_PARTYID+ " = '" + entity.getPartyId() +"'", null);
						}else{ // 无记录，插入
							getContentResolver().insert(PhotoEntity.CONTENT_URI, photo.toContentValues());
						}
					}else{ //无上传头像
					}
					
					wifiAutoSync();
//					finish();
					break;
				case STATE_INSERT: 
					entity.setPartyId(UUIDUtil.getUUID());
					String date = DateTimeUtil.format(new Date());
					entity.setCreatedat(date);
					entity.setUpdatedat(date);
					
					entity.setPartyface(entity.getPartyId()+".zy"); //头像
					
					getContentResolver().insert(CompanyEntity.CONTENT_URI, entity.toContentValues());
					
					if(bitMap != null){ //用户上传头像
						genPhoto();
						getContentResolver().insert(PhotoEntity.CONTENT_URI, photo.toContentValues()); //插入联系人头像到头像表
					}else{ //无上传头像
					}
					
					wifiAutoSync();
//					finish();
					break;
				default:
					break;
				}
			}
		});
		Button left = (Button) findViewById(R.id.header_left_btn);
		left.setText("返回");
		left.setVisibility(View.VISIBLE);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		//初始化界面元素
		nameEditText =  (EditText) findViewById(R.id.company_edit_name);
		
		//电话
//		mobileLayout = (LinearLayout) findViewById(R.id.mobile_layout);
		ImageView mobilePlus =  (ImageView) findViewById(R.id.mobile_plus);
		mobilePlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mobileCount++;
				genMobileLayout(false,null,null);
			}
		});
		
		//Address
//		addressLayout = (LinearLayout) findViewById(R.id.address_layout);
		ImageView addressPlus =  (ImageView) findViewById(R.id.address_plus);
		addressPlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				addressCount++;
				genAddressLayout(false,null,null);
			}
		});
		
		//Website
//		websiteLayout = (LinearLayout) findViewById(R.id.website_layout);
		ImageView websitePlus =  (ImageView) findViewById(R.id.website_plus);
		websitePlus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				websiteCount++;
				genWebsiteLayout(false,null,null);
			}
		});
		
		mobile_row_layout = (LinearLayout) findViewById(R.id.mobile_row_layout);
		address_row_layout = (LinearLayout) findViewById(R.id.address_row_layout);
		website_row_layout = (LinearLayout) findViewById(R.id.website_row_layout);
		
		
		photoView = (RelativeLayout) findViewById(R.id.contact_face);
		photoView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doPickPhotoAction();				
			}
		});
	}
	
	/**
	 * 生成手机布局（按钮、输入框、减号）
	 */
	private void genMobileLayout(boolean isJSON,String label,String value){
		/*final LinearLayout mobileMinusLayout = new LinearLayout(getApplicationContext());
		mobileMinusLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		mobileMinusLayout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams blp = new LayoutParams(120,LayoutParams.WRAP_CONTENT);
		Button label_btn = new Button(getApplicationContext());
		switch (mobileCount) {
		case 0:
			label_btn.setText("手机");
			break;
		case 1:
			label_btn.setText("住宅");
			break;
		case 2:
			label_btn.setText("工作");
			break;
		default:
			label_btn.setText("手机");
			break;
		}
		if(isJSON){
			label_btn.setText(label);
		}
		label_btn.setLayoutParams(blp);
		label_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentMobileBtn = (Button) v;
				showDialog(DIALOG_MOBILE_CHOICE);
			}
		});
		
		LayoutParams nlp = new LayoutParams(260,LayoutParams.WRAP_CONTENT);
		EditText mobileNumber = new EditText(getApplicationContext());
		mobileNumber.setLayoutParams(nlp);
		if(isJSON){
			mobileNumber.setText(value);
		}
		
		ImageView minus = new ImageButton(getApplicationContext());
		LayoutParams mlp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		minus.setLayoutParams(mlp);
		minus.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mobile_row_layout.removeView(mobileMinusLayout);
			}
		});
		mobileMinusLayout.addView(label_btn);
		mobileMinusLayout.addView(mobileNumber);
		mobileMinusLayout.addView(minus);
		mobile_row_layout.addView(mobileMinusLayout);*/
		
		
		final RelativeLayout mobileMinusLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.row_mobilel_edit, null);
		Button label_btn = (Button) mobileMinusLayout.findViewById(R.id.mobile_btn);
		switch (mobileCount) {
		case 0:
			label_btn.setText("手机");
			break;
		case 1:
			label_btn.setText("住宅");
			break;
		case 2:
			label_btn.setText("工作");
			break;
		default:
			label_btn.setText("手机");
			break;
		}
		if(isJSON){
			label_btn.setText(label);
		}
		label_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentMobileBtn = (Button) v;
				showDialog(DIALOG_MOBILE_CHOICE);
			}
		});
		
		
		EditText mobileNumber = (EditText) mobileMinusLayout.findViewById(R.id.mobile_edit);
		if(isJSON){
			mobileNumber.setText(value);
		}
		
		ImageView minus= (ImageView) mobileMinusLayout.findViewById(R.id.mobile_minus);
		minus.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mobile_row_layout.removeView(mobileMinusLayout);
			}
		});
		
		mobileMinusLayout.removeAllViews();
		mobileMinusLayout.addView(label_btn);
		mobileMinusLayout.addView(mobileNumber);
		mobileMinusLayout.addView(minus);
		
		mobile_row_layout.addView(mobileMinusLayout);
	}
	
	/**
	 * 生成Address布局（按钮、输入框、减号）
	 */
	private void genAddressLayout(boolean isJSON,String label,String value){
		/*final LinearLayout addressMinusLayout = new LinearLayout(getApplicationContext());
		addressMinusLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		addressMinusLayout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams blp = new LayoutParams(120,LayoutParams.WRAP_CONTENT);
		Button label_btn = new Button(getApplicationContext());
		switch (addressCount) {
		case 0:
			label_btn.setText("住宅");
			break;
		case 1:
			label_btn.setText("工作");
			break;
		default:
			label_btn.setText("住宅");
			break;
		}
		if(isJSON){
			label_btn.setText(label);
		}
		label_btn.setLayoutParams(blp);
		label_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentAddressBtn = (Button) v;
				showDialog(DIALOG_ADDRESS_CHOICE);
			}
		});
		
		LayoutParams nlp = new LayoutParams(260,LayoutParams.WRAP_CONTENT);
		EditText address = new EditText(getApplicationContext());
		address.setLayoutParams(nlp);
		if(isJSON){
			address.setText(value);
		}
		
		ImageView minus = new ImageButton(getApplicationContext());
		LayoutParams mlp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		minus.setLayoutParams(mlp);
		minus.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				address_row_layout.removeView(addressMinusLayout);
			}
		});
		addressMinusLayout.addView(label_btn);
		addressMinusLayout.addView(address);
		addressMinusLayout.addView(minus);
		address_row_layout.addView(addressMinusLayout);*/
		
		final RelativeLayout addressMinusLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.row_address_edit, null);
		Button label_btn = (Button) addressMinusLayout.findViewById(R.id.address_btn);
		switch (addressCount) {
		case 0:
			label_btn.setText("住宅");
			break;
		case 1:
			label_btn.setText("工作");
			break;
		default:
			label_btn.setText("住宅");
			break;
		}
		if(isJSON){
			label_btn.setText(label);
		}
		label_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentAddressBtn = (Button) v;
				showDialog(DIALOG_ADDRESS_CHOICE);
			}
		});
		
		
		EditText mobileNumber = (EditText) addressMinusLayout.findViewById(R.id.address_edit);
		if(isJSON){
			mobileNumber.setText(value);
		}
		
		ImageView minus= (ImageView) addressMinusLayout.findViewById(R.id.address_minus);
		minus.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				address_row_layout.removeView(addressMinusLayout);
			}
		});
		
		addressMinusLayout.removeAllViews();
		addressMinusLayout.addView(label_btn);
		addressMinusLayout.addView(mobileNumber);
		addressMinusLayout.addView(minus);
		address_row_layout.addView(addressMinusLayout);
	}
	
	/**
	 * 生成网址布局（按钮、输入框、减号）
	 */
	private void genWebsiteLayout(boolean isJSON,String label,String value){
		/*final LinearLayout websiteMinusLayout = new LinearLayout(getApplicationContext());
		websiteMinusLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		websiteMinusLayout.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams blp = new LayoutParams(120,LayoutParams.WRAP_CONTENT);
		Button label_btn = new Button(getApplicationContext());
		switch (websiteCount) {
		case 0:
			label_btn.setText("工作");
			break;
		case 1:
			label_btn.setText("个人");
			break;
		default:
			label_btn.setText("工作");
			break;
		}
		if(isJSON){
			label_btn.setText(label);
		}
		label_btn.setLayoutParams(blp);
		label_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentWebsiteBtn = (Button) v;
				showDialog(DIALOG_WEBSITE_CHOICE);
			}
		});
		
		LayoutParams nlp = new LayoutParams(260,LayoutParams.WRAP_CONTENT);
		EditText im = new EditText(getApplicationContext());
		im.setLayoutParams(nlp);
		if(isJSON){
			im.setText(value);
		}
		
		ImageView minus = new ImageButton(getApplicationContext());
		LayoutParams mlp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		minus.setLayoutParams(mlp);
		minus.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				website_row_layout.removeView(websiteMinusLayout);
			}
		});
		websiteMinusLayout.addView(label_btn);
		websiteMinusLayout.addView(im);
		websiteMinusLayout.addView(minus);
		website_row_layout.addView(websiteMinusLayout);*/
		
		final RelativeLayout websiteMinusLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.row_website_edit, null);
		Button label_btn = (Button) websiteMinusLayout.findViewById(R.id.website_btn);
		switch (websiteCount) {
		case 0:
			label_btn.setText("工作");
			break;
		case 1:
			label_btn.setText("个人");
			break;
		default:
			label_btn.setText("工作");
			break;
		}
		if(isJSON){
			label_btn.setText(label);
		}
		label_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentWebsiteBtn = (Button) v;
				showDialog(DIALOG_WEBSITE_CHOICE);
			}
		});
		
		
		EditText mobileNumber = (EditText) websiteMinusLayout.findViewById(R.id.website_edit);
		if(isJSON){
			mobileNumber.setText(value);
		}
		
		ImageView minus= (ImageView) websiteMinusLayout.findViewById(R.id.website_minus);
		minus.setBackgroundDrawable(getResources().getDrawable(R.drawable.minus));
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				website_row_layout.removeView(websiteMinusLayout);
			}
		});
		
		websiteMinusLayout.removeAllViews();
		websiteMinusLayout.addView(label_btn);
		websiteMinusLayout.addView(mobileNumber);
		websiteMinusLayout.addView(minus);
		website_row_layout.addView(websiteMinusLayout);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_MOBILE_CHOICE:
			return new AlertDialog.Builder(CompanyEditlActivity.this)
					.setTitle("选择电话")
					.setSingleChoiceItems(mobileItems, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									dialog.cancel();
									String label = mobileItems[item].toString();
									currentMobileBtn.setText(label);
								}
							}).create();
		case DIALOG_ADDRESS_CHOICE:
			return new AlertDialog.Builder(CompanyEditlActivity.this)
					.setTitle("选择地址")
					.setSingleChoiceItems(addressItems, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									dialog.cancel();
									String label = addressItems[item].toString();
									currentAddressBtn.setText(label);
								}
							}).create();
		case DIALOG_WEBSITE_CHOICE:
			return new AlertDialog.Builder(CompanyEditlActivity.this)
					.setTitle("选择网址")
					.setSingleChoiceItems(websiteItems, 0,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									dialog.cancel();
									String label = websiteItems[item].toString();
									currentWebsiteBtn.setText(label);
								}
							}).create();
		default:
			break;
		}
		return super.onCreateDialog(id);
	}
	
	/**
	 * Wifi下自动同步
	 */
	private void wifiAutoSync() {
		if (Constants.WIFI_AUTO_SYNC) { // wifi下自动同步打开
//			NetworkStateUtil net = new NetworkStateUtil(getApplicationContext());
			if (NetworkStateUtil.checkWifi(getApplicationContext())) { // wifi可用
				new WifiSyncTask().execute();
			}else{
				finish();
			}
		}else{
			finish();
		}
	}
	
	private class WifiSyncTask extends AsyncTask<Void, Void, Result> {
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			pDialog = ProgressDialog.show(CompanyEditlActivity.this, "", "公司同步中...", true, true);
		}

		@Override
		protected Result doInBackground(Void... params) {
			CompanyContactSyncService contactService = new CompanyContactSyncService(
					getApplicationContext());
			SharedPreferences pref = getApplicationContext()
					.getSharedPreferences(Constants.PREF,
							Context.MODE_PRIVATE);
			String ticketId = pref.getString(Constants.PREF_TICKETID, null);
			Result result = contactService.syncCompany(ticketId);
			
			PhotoSyncService photo = new PhotoSyncService(getApplicationContext());
			result = photo.syncPhoto(ticketId);
			return result;
		}

		@Override
		protected void onPostExecute(Result result) {
			pDialog.dismiss();
			
			finish();
		}
	}
	
	
	
	private void doPickPhotoAction() {
		Context context = CompanyEditlActivity.this;

		// Wrap our context to inflate list items using correct theme
		final Context dialogContext = new ContextThemeWrapper(context,
				android.R.style.Theme_Light);
		String cancel = "返回";
		String[] choices;
		choices = new String[2];
		choices[0] = "拍照"; // getString(R.string.take_photo); // 拍照
		choices[1] = "相册"; // getString(R.string.pick_photo); // 从相册中选择
		final ListAdapter adapter = new ArrayAdapter<String>(dialogContext,
				android.R.layout.simple_list_item_1, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				dialogContext);
		builder.setTitle("请选择");
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0: {
							String status = Environment
									.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
								doTakePhoto();// 用户点击了从照相机获取
							} else {
								// showToast("没有SD卡");
							}
							break;
						}
						case 1:
//							doPickPhotoFromGallery();// 从相册中去获取
							
							doSelectImageFromLoacal();
							break;
						}
					}
				});
		builder.setNegativeButton(cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}

				});
		builder.create().show();
	}
	
	protected void doTakePhoto() {
		try {
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 从本地手机中选择图片
	 */
	private void doSelectImageFromLoacal() {
		Intent localIntent = new Intent();
		localIntent.setType("image/*");
		localIntent.setAction("android.intent.action.GET_CONTENT");
		Intent localIntent2 = Intent.createChooser(localIntent, "选择图片");
		startActivityForResult(localIntent2, PHOTO_PICKED_WITH_DATA);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case PHOTO_PICKED_WITH_DATA: // 从本地选择图片
			if (bitMap != null && !bitMap.isRecycled()) {
				bitMap.recycle();
			}
			Uri selectedImageUri = data.getData();
			if (selectedImageUri != null) {
				try {
					bitMap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(selectedImageUri));
					
					bitMap = ImageUtil.zoomBitmap(bitMap,80,80);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				// 下面这两句是对图片按照一定的比例缩放，这样就可以完美地显示出来。有关图片的处理将重新写文章来介绍。
//				int scale = ImageThumbnail.reckonThumbnail(bitMap.getWidth(),
//						bitMap.getHeight(), 500, 600);
//
//				bitMap = ImageThumbnail.PicZoom(bitMap,
//						(int) (bitMap.getWidth() / scale),
//						(int) (bitMap.getHeight() / scale));

//				photoView.setImageBitmap(bitMap);
				BitmapDrawable bd = new BitmapDrawable(bitMap);
				photoView.setBackgroundDrawable(bd);

				hasImage = true;
			}
			break;
		case CAMERA_WITH_DATA: // 拍照
			Bundle bundle = data.getExtras();
			bitMap = (Bitmap) bundle.get("data");
			if (bitMap != null)
				bitMap.recycle();

			bitMap = (Bitmap) data.getExtras().get("data");
//			int scale = ImageThumbnail.reckonThumbnail(bitMap.getWidth(),
//					bitMap.getHeight(), 500, 600);
//
//			bitMap = ImageThumbnail.PicZoom(bitMap,
//					(int) (bitMap.getWidth() / scale),
//					(int) (bitMap.getHeight() / scale));

//			photoView.setImageBitmap(null);
//			photoView.setImageBitmap(bitMap);
			
			BitmapDrawable bd = new BitmapDrawable(bitMap);
			photoView.setBackgroundDrawable(bd);
			
			hasImage = true;

			break;
		}
	}
	
	private void genPhoto(){
		
		photo.setPartyId(entity.getPartyId());
		photo.setName(entity.getPartyface());
		byte[] input = ImageUtil.Bitmap2Bytes(bitMap);
		byte[] newly  = Base64.encode(input, Base64.DEFAULT);
		photo.setContent(new String(newly));
		photo.setPhotoUpdateDate(DateTimeUtil.format(new Date()));
	}
}
