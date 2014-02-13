package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.tools.MainAcitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 生活百事
 * 
 * @author Administrator
 * 
 */
public class Life_Pepsi extends MainAcitivity {

	Button back;
	/**
	 * 办事指南
	 */
	Button guide;
	/**
	 * 生活资讯
	 */
	Button life_info;

	TextView top_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_life_pepsi);

		back = (Button) findViewById(R.id.back);
		guide = (Button) findViewById(R.id.guide);
		life_info = (Button) findViewById(R.id.life_info);

		top_title = (TextView) findViewById(R.id.title);

		top_title.setText("生活百事");
		back.setOnClickListener(listener);
		guide.setOnClickListener(listener);
		life_info.setOnClickListener(listener);

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			case R.id.guide:
				Intent intent = new Intent(Life_Pepsi.this, Life_Pepsi_son.class);
				Bundle bundle = new Bundle();
				bundle.putString("button_key", guide.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.life_info:
				intent = new Intent(Life_Pepsi.this, Life_Pepsi_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", life_info.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			}
		}
	};

}
