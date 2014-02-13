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

public class Life_Pepsi_son extends MainAcitivity {
	/**
	 * 返回按钮
	 */
	Button back;

	TextView top_title;

	Button house_magament;
	Button social_security;
	Button labour_employment;
	Button traffic_management;
	Button consumer_protection;
	Button marriage_registration;
	Button family_plann;
	Button labor_arbitration;
	Button housing;
	Button entry_exit;
	Button other_kid;
	Button military_service;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_life_pepsi_son);
		back = (Button) findViewById(R.id.back);
		house_magament = (Button) findViewById(R.id.house_magament);
		social_security = (Button) findViewById(R.id.social_security);
		labour_employment = (Button) findViewById(R.id.labour_employment);
		traffic_management = (Button) findViewById(R.id.traffic_management);
		consumer_protection = (Button) findViewById(R.id.consumer_protection);
		marriage_registration = (Button) findViewById(R.id.marriage_registration);
		family_plann = (Button) findViewById(R.id.family_plann);
		labor_arbitration = (Button) findViewById(R.id.labor_arbitration);
		housing = (Button) findViewById(R.id.housing);
		entry_exit = (Button) findViewById(R.id.entry_exit);
		other_kid = (Button) findViewById(R.id.other_kid);
		military_service = (Button) findViewById(R.id.military_service);


		top_title = (TextView) findViewById(R.id.title);
		top_title.setText("办事指南");
		back.setOnClickListener(l);

		house_magament.setOnClickListener(l);
		social_security.setOnClickListener(l);
		labour_employment.setOnClickListener(l);
		traffic_management.setOnClickListener(l);
		consumer_protection.setOnClickListener(l);
		marriage_registration.setOnClickListener(l);
		family_plann.setOnClickListener(l);
		labor_arbitration.setOnClickListener(l);
		housing.setOnClickListener(l);
		entry_exit.setOnClickListener(l);
		other_kid.setOnClickListener(l);
		military_service.setOnClickListener(l);

		//ShowData();

	}

	private void ShowData() {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		if ((getIntent().getExtras().getString("button_key")) != null
				&& !"".equals(getIntent().getExtras().getString("button_key"))) {
			String button_key = getIntent().getExtras().getString("button_key");

			top_title.setText(button_key);

		}
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			case R.id.house_magament:
				Intent intent = new Intent(Life_Pepsi_son.this,
						GuideActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("button_key", "户籍");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.social_security:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key","社保");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.labour_employment:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key", "就业");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.traffic_management:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key", "车辆");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.consumer_protection:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key", "公证");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.marriage_registration:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key", "婚姻");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.family_plann:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key", "生育");
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			case R.id.labor_arbitration:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key","纳税");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.housing:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key", "住房");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.entry_exit:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key", "出入境");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.other_kid:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key","公租房");
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			case R.id.military_service:
				intent = new Intent(Life_Pepsi_son.this, GuideActivity.class);
				bundle = new Bundle();
				bundle.putString("button_key", "兵役");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			}
		}
	};

}
