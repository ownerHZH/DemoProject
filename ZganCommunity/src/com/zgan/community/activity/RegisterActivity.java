package com.zgan.community.activity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zgan.community.R;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

public class RegisterActivity extends MainAcitivity {
	ImageView back;
	EditText reg_numb;
	EditText reg_pas;
	EditText reg_repas;

	ImageView register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_register);

		back = (ImageView) findViewById(R.id.reg_back);
		register = (ImageView) findViewById(R.id.reg);
		reg_numb = (EditText) findViewById(R.id.reg_user_numb);
		reg_pas = (EditText) findViewById(R.id.reg_user_pas);
		reg_repas = (EditText) findViewById(R.id.reg_user_repas);

		register.setOnClickListener(l);
		back.setOnClickListener(l);
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.reg:
				if (reg_numb.getText().toString() != null
						|| reg_pas.getText().toString() != null
						|| reg_repas.getText().toString() != null) {
					String numb = reg_numb.getText().toString();
					String pas = reg_pas.getText().toString();
					Reg(numb, pas);
				} else {
					Toast.makeText(getApplicationContext(), "密码或手机号不能为空",
							Toast.LENGTH_SHORT).show();
				}

				break;

			case R.id.reg_back:
				finish();
				break;
			}
		}
	};

	protected void Reg(final String userid, final String password) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet(
						"http://community1.zgantech.com/ZganRegister.aspx?uname="
								+ userid + "&pwd=" + password );
				//http://community1.zgantech.com/ZganRegister.aspx?uname=1871634102&pwd=123456
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
						if ("0".equals(status)) {
							String msg = jsonObject.getString("msg");
							Log.i("msg", "" + Html.fromHtml(msg).toString());
							Looper.prepare();
							// dialog.dismiss();
							Toast.makeText(getApplicationContext(), "注册成功！",
									Toast.LENGTH_SHORT).show();
							Looper.loop();

						} else {
							Looper.prepare();
							// dialog.dismiss();
							String msg = jsonObject.getString("msg");
							Log.i("msg", "" + Html.fromHtml(msg).toString());
							Toast.makeText(getApplicationContext(), "" + msg,
									Toast.LENGTH_SHORT).show();
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
}
