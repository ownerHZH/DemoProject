package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CommunitySettingFeedBack extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private Button commit;
	private Context con;
	private EditText adviceInput;//意见输入框
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_setting_feed_back);
		con = CommunitySettingFeedBack.this;
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		adviceInput=(EditText) findViewById(R.id.adviceInput);
		title.setText(R.string.activity_community_setting_feed_back_title);
		commit=(Button) findViewById(R.id.buttonCommit);
		commit.setOnClickListener(l);
		back.setOnClickListener(l);
	}
	
	private OnClickListener l=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.back:
					finish();
					break;
				case R.id.buttonCommit:
					Toast.makeText(con, "提交", Toast.LENGTH_SHORT).show();
					break;
	
				default:
					break;
				}
			}
		};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_community_setting_feed_back,
				menu);
		return false;
	}

}
