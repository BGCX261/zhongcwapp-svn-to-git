package com.zhuying.android.activity;

import com.zhuying.android.R;
import com.zhuying.android.entity.ActionEntity;
import com.zhuying.android.entity.CompanyEntity;
import com.zhuying.android.entity.ContactEntity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 批注明细
 */
public class CommentDetailActivity extends Activity {
	private String subjectid; //关联对象id
	private String subjecttype; //关联对象类型
	private String subjectname; //关联对象名称
	
	private String person = "";
	private String content = "";
	
	private TextView bodyTextView;
	private TextView subjectNameTextView;
	private RelativeLayout subjectLayout; //关联对象布局
	private TextView dateView;
	private TextView personView;
	private ImageView arrow;
	
	private ActionEntity entity;
	private Cursor mCursor;
	private String id;//主键

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.comment_detail);
		initUI();
		
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		subjectid = b.get("subjectid").toString();
		subjecttype = b.get("subjecttype").toString();
		subjectname = b.get("subjectname").toString();
		id = b.get("id").toString();
		
		mCursor = managedQuery(ActionEntity.CONTENT_URI, null,ActionEntity.KEY_PARENTID + " = '" + id + "' and activitytype='comment'", null, null);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mCursor != null){
			mCursor.moveToFirst();
			entity = new ActionEntity(mCursor);
			bodyTextView.setText(entity.getActivitycontent());
			subjectNameTextView.setText(entity.getSubjectname());
//			dateView.setText(entity.getCreatedat().subSequence(0, 10));
			dateView.setText(entity.getActivitydate());
			personView.setText(entity.getOwnername());
		}
	}

	private void initUI() {
		// init Header
		TextView title = (TextView) findViewById(R.id.header_title);
		title.setText("批注");
		Button right = (Button) findViewById(R.id.header_right_btn);
		right.setVisibility(View.INVISIBLE);
//		right.setText("修改");
//		right.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent();
//				i.setAction(Intent.ACTION_EDIT);
//				i.setData(RecordEntity.CONTENT_URI);
//				i.setType("vnd.android.cursor.item/vnd.zhuying.records");
//				i.putExtra("id", id);
//				i.putExtra("subjectid", subjectid);
//				i.putExtra("subjecttype", subjecttype);
//				i.putExtra("subjectname", subjectname);
//				startActivity(i);
//			}
//		});
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
		bodyTextView = (TextView) findViewById(R.id.record_detail_body);
		subjectNameTextView = (TextView) findViewById(R.id.record_subjectName);
		/*subjectLayout = (RelativeLayout) findViewById(R.id.record_subject_layout);
		subjectLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri;
				 //联系人
				uri = ContactEntity.CONTENT_URI;
				String rid="";
				Cursor c = managedQuery(RecordEntity.CONTENT_URI, null,RecordEntity.KEY_NOTEID + " = '" + id +"'", null, null);
				while (c.moveToFirst()) {
					rid = c.getString(2);
					break;
				}
				c.close();
				
				Intent i = new Intent();
				i.setAction(Intent.ACTION_VIEW);
				i.setData(uri);
				i.setType("vnd.android.cursor.item/vnd.zhuying.contacts");
				i.putExtra("id", rid); //主键（partyid）
				startActivity(i);
			
			}
		});*/
		
		dateView = (TextView) findViewById(R.id.record_date);
		personView = (TextView) findViewById(R.id.record_person);
		arrow = (ImageView) findViewById(R.id.record_arrow);
	}
}
