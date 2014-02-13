package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.zgan.community.R;
import com.zgan.community.adapter.ReinfoAdapter;
import com.zgan.community.data.Recinfo;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserSetting extends MainAcitivity {
	Button back;

	TextView top_title;

	ListView listView;

	Button logout;
	private LinearLayout userInfoSetting;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	Handler handler = new Handler();
	ProgressDialog dialog;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(UserSetting.this, MainPageActivity.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_usersetting);
		preferences = getSharedPreferences("zgan_data", MODE_PRIVATE);
		editor = preferences.edit();
		back = (Button) findViewById(R.id.back);
		logout = (Button) findViewById(R.id.out);
		top_title = (TextView) findViewById(R.id.title);
		userInfoSetting = (LinearLayout) findViewById(R.id.userInfoSetting);

		top_title.setText("我的设置");

		back.setOnClickListener(l);
		logout.setOnClickListener(l);
		userInfoSetting.setOnClickListener(l);
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				Intent intent = new Intent(UserSetting.this,
						MainPageActivity.class);
				startActivity(intent);
				finish();
				break;

			case R.id.out:
				dialog = new ProgressDialog(UserSetting.this);
				dialog.setTitle("提示");
				dialog.setMessage("退出账号中，请稍后");
				dialog.show();
				OutUser(ZganCommunityStaticData.User_Number);
				break;

			case R.id.userInfoSetting:
				Intent intentU = new Intent(UserSetting.this,
						CommuntityUserInfoActivity.class);
				startActivity(intentU);
				break;
			}
		}
	};

	protected void OutUser(final String usrid) {
		// TODO Auto-generated method stub

		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet(
						"http://community1.zgantech.com/ZganQuit.aspx?did="
								+ usrid);

				HttpClient client = new DefaultHttpClient();
				HttpResponse httpResponse;
				try {
					httpResponse = client.execute(get);
					HttpEntity entity = httpResponse.getEntity();
					StatusLine line = httpResponse.getStatusLine();
					Log.i("linecode", "" + line.getStatusCode());
					if (line.getStatusCode() == 200) {
						String st = EntityUtils.toString(entity);

						Log.i("st", "" + st);
						JSONObject jsonObject = new JSONObject(st);
						String status = jsonObject.getString("status");
						Log.i("status", "" + status);
						if (status.equals("0")) {
							Log.i("00000000", "00000");
							handler.post(r);
						} else {
							Log.i("11111", "11111");

							Looper.prepare();

							dialog.dismiss();

							Toast.makeText(getApplicationContext(),
									"退出账号失败请检查网络", Toast.LENGTH_SHORT).show();
							Looper.loop();
						}
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				super.run();

			}
		}.start();
	}

	Runnable r = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			dialog.dismiss();
			editor.clear();
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	};
}
