package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CommuntityUserInfoActivity extends MainAcitivity {

	private Button back;
	private TextView title;	
	private ImageView headIco;//头像
	private TextView realName;//真实姓名
	private TextView idNumber;//身份证号
	private TextView password;//密码
	private TextView cId;//社区ID
	private TextView sex;//性别
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communtity_user_info);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText("个人设置");
		
		headIco=(ImageView) findViewById(R.id.headIco);
		realName=(TextView) findViewById(R.id.realName);
		idNumber=(TextView) findViewById(R.id.idNumber);
		password=(TextView) findViewById(R.id.password);
		cId=(TextView) findViewById(R.id.cId);
		sex=(TextView) findViewById(R.id.sex);
		
		back.setOnClickListener(l);//后退注册监听
		password.setOnClickListener(l);//密码修改注册监听事件
		sex.setOnClickListener(l);//性别设置注册监听器
	}
	
	//监听器
	private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				   finish();
				break;
				
			case R.id.password:
				   //密码修改操作
				Intent intent=new Intent(CommuntityUserInfoActivity.this,CommunityModifyPasswordActivity.class);
				startActivity(intent);
				break;
			case R.id.sex:
				   //性别修改操作
				Intent sexIntent=new Intent(CommuntityUserInfoActivity.this,CommunityModifySexActivity.class);
				sexIntent.putExtra("s", sex.getText().toString());
				startActivityForResult(sexIntent, 100);
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_communtity_user_info, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==100)
		{
			if(resultCode==-1)
			{
				sex.setText(data.getStringExtra("sex"));
			}
			
		}
	}

}
