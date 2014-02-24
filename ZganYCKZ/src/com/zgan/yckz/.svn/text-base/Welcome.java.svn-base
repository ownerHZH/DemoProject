package com.zgan.yckz;

import com.zgan.yckz.activity.User_Login;
import com.zgan.yckz.tools.YCKZ_Activity;
import com.zgan.yckz.tools.YCKZ_Static;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class Welcome extends YCKZ_Activity {

	SharedPreferences preferences;

	String user_name;
	String user_pas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		preferences = getSharedPreferences("yckz_user", MODE_PRIVATE);

		YCKZ_Static.Phone_number=user_name = preferences.getString("user_name", null);
		YCKZ_Static.USER_password=user_pas = preferences.getString("user_pas", null);
		LinearLayout layout = (LinearLayout) findViewById(R.id.line_welcome);
		layout.setOnClickListener(listener);

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

				
				Intent intent = new Intent(Welcome.this, User_Login.class);
				startActivity(intent);
				finish();
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
