package com.zhuying.android.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import me.maxwin.view.XListView;
import me.maxwin.view.XListView.IXListViewListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuying.android.R;
import com.zhuying.android.adapter.ContactCursorAdapter;
import com.zhuying.android.adapter.ContactListAdapter;
import com.zhuying.android.async.Result;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;
import com.zhuying.android.entity.PhotoEntity;
import com.zhuying.android.entity.NoteEntity;
import com.zhuying.android.service.CommonSyncService;
import com.zhuying.android.service.CompanyContactSyncService;
import com.zhuying.android.service.NoticeSyncService;
import com.zhuying.android.util.Constants;
import com.zhuying.android.util.DateTimeUtil;
import com.zhuying.android.util.NetworkStateUtil;
import com.zhuying.android.util.StringUtil;
import com.zhuying.android.view.CompanyPinYinIndexView;
import com.zhuying.android.view.MyLetterListView;
import com.zhuying.android.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.zhuying.android.view.PinYinIndexView;


/**
 * 联系人列表
 */
public class ContactListActivity extends Activity implements IXListViewListener {
//	public PullToRefreshListView listView;
	private XListView mListView;
	
	private EditText searchEditText; //搜索框
	private Button searchX;
	
	private String syncTime = "最后同步：";
	
	private MyLetterListView letterListView;
	private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
	private String[] sections;// 存放存在的汉语拼音首字母
	private Handler handler;
	private OverlayThread overlayThread;
	private TextView overlay;
	private static final String NAME = "name", COMPANY = "company",SORT_KEY = "sort_key",PARTYFACE = "partyface",PARTYID = "partyid";
	
	private AsyncQueryHandler asyncQuery;
	
	private SharedPreferences sharedPrefs;
	String pref_syncTime;
	
	private boolean fromSearch;
	private boolean showSearchBar;
	private RelativeLayout searchBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contact_list);
		
		
		sharedPrefs = getSharedPreferences(Constants.PREF, Context.MODE_PRIVATE);
		pref_syncTime = sharedPrefs.getString(Constants.PREF_COMPANY_SYNCTIME, "2010-10-01 00:00:00");
		
		mListView = (XListView) findViewById(R.id.contact_xListView);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
		mListView.setRefreshTime(pref_syncTime);
		
		initUI();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		doList(null);
		
		getSysNotice();
	}
	
	private void initUI(){
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("联系人");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setText("新增");
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_INSERT,ContactEntity.CONTENT_URI));
			}
		});
		searchX = (Button) findViewById(R.id.search_x);
    	searchX.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchEditText.setText("");
				searchX.setVisibility(View.INVISIBLE);
			}
		});
    	
		searchEditText = (EditText) findViewById(R.id.search_button);
    	searchEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				searchX.setVisibility(View.VISIBLE);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				doList(searchEditText.getText().toString());
			}
			
		});
    	
    	
    	
//    	listView = (PullToRefreshListView) getListView();
//    	listView.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new GetDataTask().execute();
//            }
//        });
//		listView.setLastUpdated(syncTime + DateTimeUtil.format(new Date()));
    	
    	letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());
		
		searchBar = (RelativeLayout) findViewById(R.id.searchbar_layout);
		
		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		initOverlay();
	}
	
	/**
	 * 
	 * @param searchKeyword 搜索关键字
	 */
	private void doList(String searchKeyword){
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				view.setBackgroundResource(R.drawable.blue);  //选中项背景色
				
				ContentValues cv =  (ContentValues) mListView.getAdapter().getItem(position);
//				String rid="";
//				Cursor c = managedQuery(ContactEntity.CONTENT_URI, null,"_id = "+id, null, null);
//				while (c.moveToFirst()) {
//					rid = c.getString(19);
//					break;
//				}
//				c.close();
				Uri uri = ContactEntity.CONTENT_URI;
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.setData(uri);
				i.setType("vnd.android.cursor.item/vnd.zhuying.contacts");
				i.putExtra("id", cv.getAsString(PARTYID)); //主键（partyid）
				startActivity(i);
			}
		});
		
		String selection;
		if(TextUtils.isEmpty(searchKeyword)){
			selection = "partytype = '"+ CompanyEntity.TYPE_CONTACT +"'";
			
			fromSearch = false;
		}else{
			selection = "partytype = '"+ CompanyEntity.TYPE_CONTACT +"' and name like '%" + searchKeyword + "%'";
			
			fromSearch = true;
		}
		String sortOrder = "sort_key COLLATE LOCALIZED asc";
//		Cursor cursor = managedQuery(ContactEntity.CONTENT_URI, null, selection, null,sortOrder);
//		String[] from = {ContactEntity.KEY_NAME,ContactEntity.KEY_COMPANYNAME};
//		int[] to = {R.id.name,R.id.company};
//		adapter = new ContactCursorAdapter(this, R.layout.contact_list_item, cursor, from, to);
//		setListAdapter(adapter);
		
		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		asyncQuery.startQuery(0, null, ContactEntity.CONTENT_URI, null, selection, null,sortOrder);
	}
	
	 private class GetDataTask extends AsyncTask<Void, Void, String[]> {
	        @Override
	        protected String[] doInBackground(Void... params) {
	        	CommonSyncService common = new CommonSyncService(getApplicationContext());
				Result result =  common.sync();
				return null;
	        }

	        @Override
	        protected void onPostExecute(String[] result) {
//	        	listView.onRefreshComplete();
//	        	listView.setLastUpdated(syncTime + DateTimeUtil.format(new Date()));
	        	
	        	onLoad();
	        	//添加刷新列表
	        	doList(null);
	            
	            super.onPostExecute(result);
	        }
	    }
	 
	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {
		@Override
		public void onTouchingLetterChanged(final String s) {
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mListView.setSelection(position + 1); //+1，解决listview addheaderview问题
				overlay.setText(sections[position]);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// 延迟一秒后执行，让overlay为不可见
				handler.postDelayed(overlayThread, 1500);
			}
		}
	}
	
	// 设置overlay不可见
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}
	
	// 初始化汉语拼音首字母弹出提示框
	private void initOverlay() {
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}
	
	// 异步查询联系人
	private class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			if (cursor != null && cursor.getCount() > 0) {
				List<ContentValues> list = new ArrayList<ContentValues>();
				cursor.moveToFirst();
				for (int i = 0; i < cursor.getCount(); i++) {
					ContentValues cv = new ContentValues();
					cursor.moveToPosition(i);
					String name = cursor.getString(1);
					String sortKey = cursor.getString(18);
					String company = cursor.getString(12);
					String partyFace = cursor.getString(13);
					String partyID = cursor.getString(19);
					cv.put(NAME, name);
					cv.put(SORT_KEY, sortKey);
					cv.put(COMPANY, company);
					cv.put(PARTYFACE, partyFace);
					cv.put(PARTYID, partyID);
					list.add(cv);
				}
				cursor.close();
				if (list.size() > 0) {
					showSearchBar = true;
					setAdapter(list);
				}
			}else{ //搜索结果为空
				List<ContentValues> emptyList = new ArrayList<ContentValues>();
				setAdapter(emptyList);
			}
		}
	}
	
	private void setAdapter(List<ContentValues> list) {
		if(showSearchBar){
			searchBar.setVisibility(View.VISIBLE);
		}
		LinearLayout emptyView;
		if(fromSearch){
			emptyView = (LinearLayout) findViewById(R.id.empty_contact_list_view_search);
		}else{
			emptyView = (LinearLayout) findViewById(R.id.empty_contact_list_view);
		}
		mListView.setEmptyView(emptyView); //必须在setAdapter之前调用
		
		ListAdapter madapter = new ListAdapter(this, list);
//		listView.setAdapter(madapter);
		mListView.setAdapter(madapter);
		
		if(madapter.getCount() > 0){
			letterListView.setVisibility(View.VISIBLE);
		}else{
			letterListView.setVisibility(View.GONE);
		}
	}

	private class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<ContentValues> list;
		Context context;
		
		public ListAdapter(Context context, List<ContentValues> list) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			this.context = context;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];

			for (int i = 0; i < list.size(); i++) {
				// 当前汉语拼音首字母
				String currentStr = getAlpha(list.get(i).getAsString(SORT_KEY));
				// 上一个汉语拼音首字母，如果不存在为“ ”
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
						.getAsString(SORT_KEY)) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getAsString(SORT_KEY));
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.contact_list_item, null);
				holder = new ViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.company = (TextView) convertView.findViewById(R.id.company);
				holder.photoView = (ImageView) convertView.findViewById(R.id.imageView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ContentValues cv = list.get(position);
			holder.name.setText(cv.getAsString(NAME));
			holder.name.getPaint().setFakeBoldText(true); //粗体
			String companyName = cv.getAsString(COMPANY);
			if(!StringUtil.isEmpty(companyName)){
				holder.company.setText(companyName);
			}else{
				holder.company.setText("");
			}
			
			String partyFace = cv.getAsString(PARTYFACE);
			if (!TextUtils.isEmpty(partyFace)) {
				Cursor photoCursor = context.getContentResolver()
						.query(PhotoEntity.CONTENT_URI, null,
								PhotoEntity.KEY_NAME + " = '" + partyFace + "'",
								null, null);
				if (photoCursor.moveToFirst()) {
					String content = photoCursor.getString(4);
					byte[] data = Base64.decode(content, Base64.DEFAULT);
					Bitmap icon = BitmapFactory.decodeByteArray(data, 0,
							data.length);
					holder.photoView.setImageBitmap(scaleImg(icon));
				} 
				else {
					Drawable d = context.getResources().getDrawable(R.drawable.contact_icon);
					Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
					holder.photoView.setImageBitmap(scaleImg(bitmap));
				}
				photoCursor.close();
			}else{
				Drawable d = context.getResources().getDrawable(R.drawable.contact_icon);
				Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
				holder.photoView.setImageBitmap(scaleImg(bitmap));
			}
			
			String currentStr = getAlpha(list.get(position).getAsString(
					SORT_KEY));// 当前字母
			String previewStr = (position - 1) >= 0 ? getAlpha(list.get(
					position - 1).getAsString(SORT_KEY)) : " ";
			if (!previewStr.equals(currentStr)) {
				holder.alpha.setVisibility(View.VISIBLE);
				holder.alpha.setText(currentStr);
			} else {
				holder.alpha.setVisibility(View.GONE);
			}
			convertView.setBackgroundResource(R.drawable.listview_item_selector);
			return convertView;
		}

		private class ViewHolder {
			TextView alpha;
			TextView name;
			TextView company;
			ImageView photoView;
		}

	}
	
	// 获得汉语拼音首字母
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// 正则表达式，判断首字母是否是英文字母
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			quitDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void quitDialog() {
		AlertDialog ad = new AlertDialog.Builder(this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle("确认退出")
				.setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						})
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface Dialog,
									int whichButton) {
								// android.os.Process.killProcess(android.os.Process.myPid());
								forceStopPackage("com.zhuying.android");
							}
						}).create();
		ad.show();
	}
	
	private void forceStopPackage(String pkgName) {
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	
	/**
	 * 缩放图片
	 */
	private Bitmap scaleImg(Bitmap bm){
		 // 获得图片的宽高
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    // 设置想要的大小
	    int newWidth = 64;
	    int newHeight = 64;
	    // 计算缩放比例
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // 取得想要缩放的matrix参数
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    // 得到新的图片
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,true);
	    
	    return newbm;
	}

	@Override
	public void onRefresh() {
		if(!NetworkStateUtil.checkNetworkInfo(ContactListActivity.this)){
			mListView.stopRefresh();
		}else{
			new GetDataTask().execute();
		}	
	}

	@Override
	public void onLoadMore() {
		
	}
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.setRefreshTime(DateTimeUtil.format(new Date()));
	}
	
	private void getSysNotice() {
//		String ticketId = sharedPrefs.getString(Constants.PREF_TICKETID, null);
//		NoticeSyncService service = new NoticeSyncService(ContactListActivity.this);
//		service.syncNotice(ticketId);
		

		
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				String ticketId = sharedPrefs.getString(Constants.PREF_TICKETID, null);
				NoticeSyncService service = new NoticeSyncService(ContactListActivity.this);
				String r = service.syncNotice(ticketId);
				return r;
			}
			
			protected void onPostExecute(String result) {
				if(!StringUtil.isEmpty(result)){
					showNoticeDialog(ContactListActivity.this,result);
				}
			};
		}.execute();
	
	}
	
	/**
	 * 通知对话框
	 * @param ctx
	 */
	private void showNoticeDialog(Context ctx,String content){
		new AlertDialog.Builder(ctx).setTitle("提示").setMessage("系统通知：\n"+content)
				.setCancelable(false).setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int id) {
							}
						}).create().show();
	}
}
