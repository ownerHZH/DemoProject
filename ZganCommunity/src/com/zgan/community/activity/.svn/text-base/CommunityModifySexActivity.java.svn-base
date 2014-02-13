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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class CommunityModifySexActivity extends MainAcitivity {

	private Button back;
	private TextView title;	
	private RadioGroup radioGroup;
	private RadioButton male,female;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_modify_sex);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText("性别设置");
		
		male=(RadioButton) findViewById(R.id.radio0);
		female=(RadioButton) findViewById(R.id.radio2);
		
		radioGroup=(RadioGroup) findViewById(R.id.sexRadioGroup);
		
		Intent intent=getIntent();
		if(intent!=null)
		{
			String s=intent.getStringExtra("s").trim();
			if(s.equals("男"))
			{
				male.setChecked(true);
				female.setChecked(false);
			}else if(s.equals("女"))
			{
				male.setChecked(false);
				female.setChecked(true);
			}
		}
		
		
		back.setOnClickListener(l);//后退注册监听
		radioGroup.setOnCheckedChangeListener(listener);		
	}
	
	private OnCheckedChangeListener listener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			Intent intent=new Intent();
			switch (checkedId) {
			case R.id.radio0://男性			
				intent.putExtra("sex", "男");
				setResult(-1, intent);
				finish();
				//Toast.makeText(CommunityModifySexActivity.this, "你选择了男", 2).show();
				break;
            case R.id.radio2://女性
            	intent.putExtra("sex", "女");
				setResult(-1, intent);
				finish();
            	//Toast.makeText(CommunityModifySexActivity.this, "你选择了女", 2).show();
				break;

			default:
				break;
			}
		}
	};
	
	//监听器
	private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				   finish();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_modify_sex, menu);
		return true;
	}


}
