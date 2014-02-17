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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityContactProperty extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private Button commit;
	private RadioGroup radioGroup;
	private EditText editText;
	private Context con;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_contact_property);
		
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.community_contact_property);
		commit=(Button) findViewById(R.id.buttonCommit);
		radioGroup=(RadioGroup) findViewById(R.id.radioGroup1);
		editText=(EditText) findViewById(R.id.adviceInput);
		
		con = CommunityContactProperty.this;
		
		back.setOnClickListener(l);
		commit.setOnClickListener(l);
		
		radioGroup.setOnCheckedChangeListener(listener);
	}
	
	private OnCheckedChangeListener listener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if(group.getChildAt(group.getChildCount()-1).getId()==checkedId)
			{
				String s=editText.getText().toString();
				if(!s.equals(null)&&!s.equals(""))
				{
					Toast.makeText(con, "你填写的数据为："+s, Toast.LENGTH_SHORT).show();
				}else
				{
					Toast.makeText(con, "你填写的数据为空", Toast.LENGTH_SHORT).show();
				}
			}else
			{
				RadioButton radioButton=(RadioButton) findViewById(checkedId);
				Toast.makeText(con, "你选择了"+radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
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
		getMenuInflater().inflate(R.menu.activity_community_contact_property,
				menu);
		return false;
	}

}
