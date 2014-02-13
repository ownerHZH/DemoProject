package com.zgan.community;

import com.zgan.community.activity.LoginActivity;
import com.zgan.community.activity.MainPageActivity;
import com.zgan.community.activity.MapActivitiy;
import com.zgan.community.activity.RegisterActivity;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

public class Advertising extends MainAcitivity {

	SharedPreferences preferences;
	/**
	 * 用户名
	 */
	String user_name;
	/**
	 * 用户密码
	 */
	String user_pas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ShowView();
		preferences = getSharedPreferences("zgan_data", MODE_PRIVATE);

		user_name = preferences.getString("number", null);
		user_pas = preferences.getString("password", null);
		LinearLayout layout = (LinearLayout) findViewById(R.id.line_welcome);
		layout.setOnClickListener(listener);

	}

	private void ShowView() {
		// TODO Auto-generated method stub
		View view = View
				.inflate(Advertising.this, R.layout.activity_main, null);
		setContentView(view);

		Animation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(5000);
		view.setAnimation(animation);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (ZganCommunityStaticData.User_Number != null
					&& !ZganCommunityStaticData.User_Number.equals("")) {
				Intent intent = new Intent(Advertising.this,
						MainPageActivity.class);
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(Advertising.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
