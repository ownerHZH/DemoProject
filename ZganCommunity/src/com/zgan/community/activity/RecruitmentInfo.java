package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.tools.MainAcitivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RecruitmentInfo extends MainAcitivity {
	/**
	 * 返回按钮
	 */
	Button back;
	// 服务员
	Button waiter;
	// 保洁
	Button gamble;
	// 司机
	Button driver;
	// 厨师
	Button cook;
	// 快递员
	Button courier;
	// 销售
	Button sales;
	// 技工
	Button mechanic;
	// 客服
	Button customer_service;
	// 营业员
	Button assistant;
	// 会计
	Button accounting;
	// 文员
	Button secretary;
	// 其他
	Button other;

	TextView top_title;

	ProgressDialog dialog;

	String data_type = "ZganJob.aspx";

	int did = 1;
	int sid = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_recinfo);

		back = (Button) findViewById(R.id.back);
		waiter = (Button) findViewById(R.id.waiter);
		gamble = (Button) findViewById(R.id.gamble);
		driver = (Button) findViewById(R.id.driver);
		cook = (Button) findViewById(R.id.cook);
		courier = (Button) findViewById(R.id.courier);
		sales = (Button) findViewById(R.id.sales);
		mechanic = (Button) findViewById(R.id.mechanic);
		customer_service = (Button) findViewById(R.id.customer_service);
		assistant = (Button) findViewById(R.id.assistant);
		accounting = (Button) findViewById(R.id.accounting);
		secretary = (Button) findViewById(R.id.secretary);
		other = (Button) findViewById(R.id.other);

		top_title = (TextView) findViewById(R.id.title);

		top_title.setText("招工信息");
		back.setOnClickListener(listener);
		waiter.setOnClickListener(listener);
		gamble.setOnClickListener(listener);
		driver.setOnClickListener(listener);
		cook.setOnClickListener(listener);
		courier.setOnClickListener(listener);
		sales.setOnClickListener(listener);
		mechanic.setOnClickListener(listener);
		customer_service.setOnClickListener(listener);
		assistant.setOnClickListener(listener);
		accounting.setOnClickListener(listener);
		secretary.setOnClickListener(listener);
		other.setOnClickListener(listener);

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			case R.id.waiter:
				Intent intent = new Intent(RecruitmentInfo.this,
						Reinfo_son.class);
				Bundle bundle = new Bundle();
				bundle.putString("button_key", "服务员");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.gamble:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "保洁");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.driver:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "司机");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.cook:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "厨师");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.courier:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "快递员");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.sales:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "销售");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.mechanic:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "技工");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.customer_service:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "客服");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.assistant:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "营业员");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.accounting:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "会计");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.secretary:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "文员");
				intent.putExtras(bundle);
				startActivity(intent);
				break;
			case R.id.other:
				intent = new Intent(RecruitmentInfo.this, Reinfo_son.class);
				bundle = new Bundle();
				bundle.putString("button_key", "其他");
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			}
		}
	};

}
