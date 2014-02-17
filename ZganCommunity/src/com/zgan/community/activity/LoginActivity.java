package com.zgan.community.activity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.zgan.community.R;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends MainAcitivity {
	/**
	 * 用户手机号
	 */
	EditText go_tel;
	/**
	 * 用户密码
	 */
	EditText go_pas;
	/**
	 * 登陆按钮
	 */
	ImageView go;
	ImageView reg;
	Handler handler = new Handler();
	ProgressDialog dialog;
	String szIme;
	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);
		preferences = getSharedPreferences("zgan_data", MODE_PRIVATE);
		editor = preferences.edit();

		go = (ImageView) findViewById(R.id.go);
		reg = (ImageView) findViewById(R.id.reg);

		go_tel = (EditText) findViewById(R.id.go_tel);
		go_pas = (EditText) findViewById(R.id.go_pas);

		go_tel.setText(preferences.getString("number", null));
		go_pas.setText(preferences.getString("password", null));

		go.setOnClickListener(listener);
		reg.setOnClickListener(listener);
		TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		szIme = TelephonyMgr.getDeviceId();
		Log.i("szIme", szIme);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.go:
				dialog = new ProgressDialog(LoginActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage("登陆中，请稍后");
				dialog.show();
				GetData(go_tel.getText().toString(), go_pas.getText()
						.toString(), szIme);

				break;

			case R.id.reg:

				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				break;
			}

		}

	};

	protected void GetData(final String userid, final String password,
			final String meid) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet(
						"http://community1.zgantech.com/ZganLogin.aspx?uname="
								+ userid + "&pwd=" + password + "&meid=" + meid);

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
									"请输入正确的账号和密码", Toast.LENGTH_SHORT).show();
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
			editor.putString("number", go_tel.getText().toString());
			editor.putString("password", go_pas.getText().toString());
			editor.commit();
			ZganCommunityStaticData.User_Number = preferences.getString(
					"number", null);
			ZganCommunityStaticData.User_PassWord = preferences.getString(
					"password", null);
			Intent intent = new Intent(LoginActivity.this,
					MainPageActivity.class);
			startActivity(intent);

			finish();
		}
	};
}
