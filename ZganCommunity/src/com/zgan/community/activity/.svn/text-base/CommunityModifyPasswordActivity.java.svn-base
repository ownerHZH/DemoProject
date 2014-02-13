package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CommunityModifyPasswordActivity extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private EditText oldpass;
	private EditText newpass;
	private EditText renewpass;
	private Button save;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_modify_password);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText("ÃÜÂëĞŞ¸Ä");
		
		oldpass=(EditText) findViewById(R.id.oldPassword);
		newpass=(EditText) findViewById(R.id.newPassword);
		renewpass=(EditText) findViewById(R.id.renewPassword);
		
		save=(Button) findViewById(R.id.save);
		
		back.setOnClickListener(l);//ºóÍË×¢²á¼àÌı
		save.setOnClickListener(l);//ÃÜÂëĞŞ¸ÄÈ·ÈÏ°´Å¥×¢²á¼àÌıÊÂ¼ş
	}
	
	//¼àÌıÆ÷
	private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				   finish();
				break;
				
			case R.id.save:
				   //ÃÜÂëÈ·ÈÏ²Ù×÷
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_modify_password,menu);
		return true;
	}

}
