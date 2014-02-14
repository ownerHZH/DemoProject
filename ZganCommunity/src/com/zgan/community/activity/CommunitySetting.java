package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.activity.CommunityPolicitalActivity.ButtonClickListener;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CommunitySetting extends MainAcitivity {

	private Button back;
	private TextView title;
	private Context con;
	
	private LinearLayout about;//关于
	private LinearLayout feedback;//意见反馈
	private Button messageSwitch;//消息推送开关
	private Button passwordChange;//密码修改
	private RadioGroup sexRadioGroup;//性别sexRadioGroup
	private EditText birthText;//生日显示框
	private Button birthChange;//生日修改按钮
	private EditText nicknameText;//昵称显示框
	private Button nicknameChange;//昵称修改按钮
	private TextView balcony;//楼座号
	private TextView phone;//手机号码
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_setting);
		
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.personal_settings);
		con = CommunitySetting.this;
		
		about=(LinearLayout) findViewById(R.id.about);//关于
		feedback=(LinearLayout) findViewById(R.id.feedback);//意见反馈
		messageSwitch=(Button) findViewById(R.id.messageSwitch);//消息推送开关
		passwordChange=(Button) findViewById(R.id.passwordChange);//密码修改
		sexRadioGroup=(RadioGroup) findViewById(R.id.sexRadioGroup);//性别sexRadioGroup
		birthText=(EditText) findViewById(R.id.birthText);//生日显示框
		birthChange=(Button) findViewById(R.id.birthChange);//生日修改按钮
		nicknameText=(EditText) findViewById(R.id.nicknameText);//昵称显示框
		nicknameChange=(Button) findViewById(R.id.nicknameChange);//昵称修改按钮
		balcony=(TextView) findViewById(R.id.balcony);//楼座号
		phone=(TextView) findViewById(R.id.phone);//手机号码
				
		back.setOnClickListener(l);
		passwordChange.setOnClickListener(l);
		feedback.setOnClickListener(l);
		about.setOnClickListener(l);
		nicknameChange.setOnClickListener(l);
		
	}
	
	
	private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.passwordChange:
				 //密码修改操作
				Intent intent=new Intent(con,CommunityModifyPasswordActivity.class);
				startActivity(intent);
				break;
			case R.id.feedback:
				//意见反馈
				break;
			case R.id.about:
				//关于
				
				break;
			case R.id.nicknameChange:
				//修改昵称
				buttonToggleAction(nicknameChange,nicknameText);
				
				break;

			default:
				break;
			}
		}
	};

	
	public void buttonToggleAction(Button button,EditText editText)
	{
		if(!button.getText().toString().equals("确定"))
		{
			editText.setEnabled(true);
			editText.requestFocus();
			button.setText("确定");
		}else
		{
			editText.setEnabled(false);
			editText.clearFocus();
			button.setText("修改");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_community_setting, menu);
		return false;
	}

}
