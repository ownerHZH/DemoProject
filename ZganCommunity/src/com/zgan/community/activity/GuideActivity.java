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
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.zgan.community.R;
import com.zgan.community.adapter.CommunityServiceAdapter;
import com.zgan.community.adapter.ReinfoAdapter;
import com.zgan.community.data.CommunityService;
import com.zgan.community.data.Recinfo;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

public class GuideActivity extends MainAcitivity {
	Button back;
	TextView top_title;

	ListView listview;
	Recinfo recinfo;
	List<Recinfo> list;
	ProgressDialog dialog;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_recinfo_son);
		handler = new Handler();

		back = (Button) findViewById(R.id.back);
		top_title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.list_reinfo);
		listview.setDivider(null);
		listview.setOnItemClickListener(ItemListener);
		back.setOnClickListener(listener);
		ShowData();

	}

	OnItemClickListener ItemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub

		}
	};

	/**
	 * 
	 */

	private void ShowData() {
		// TODO Auto-generated method stub
		if ((getIntent().getExtras().getString("button_key")) != null
				&& !"".equals(getIntent().getExtras().getString("button_key"))) {
			String button_key = getIntent().getExtras().getString("button_key");
			top_title.setText(button_key);
			Log.i("button_key", "button_key");

			dialog = new ProgressDialog(this);
			dialog.setTitle("提示");
			dialog.setMessage("加载中，请稍后");
			dialog.show();
			Log.i("22222222", "222222222");

			list = new ArrayList<Recinfo>();
			if (button_key.equals("户籍")) {
				GetData(1);
			} else if (button_key.equals("社保")) {
				GetData(3);
			} else if (button_key.equals("就业")) {
				GetData(4);
			} else if (button_key.equals("车辆")) {
				GetData(5);
			} else if (button_key.equals("公证")) {
				GetData(6);
			} else if (button_key.equals("婚姻")) {
				Log.i("111111111", "111111111111");

				GetData(7);
			} else if (button_key.equals("生育")) {
				GetData(8);
			} else if (button_key.equals("纳税")) {
				GetData(9);
			} else if (button_key.equals("住房")) {
				GetData(10);
			} else if (button_key.equals("出入境")) {
				GetData(11);
			} else if (button_key.equals("公租房")) {
				GetData(12);
			} else if (button_key.equals("兵役")) {
				GetData(13);
			} else {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "暂无招聘信息",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void GetData(final int sid) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet(
						"http://community1.zgantech.com/Zgan_BSZN.aspx?did="
								+ ZganCommunityStaticData.User_Number + "&sid="
								+ sid);
				Log.i("00000000000", "00000000000");
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

						String Data = jsonObject.getString("data");
						Log.i("Data", "" + Html.fromHtml(Data).toString());

						JSONArray array = new JSONArray(Data);
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject2 = array.getJSONObject(i);
							String content = jsonObject2.getString("CContent");
							String title = jsonObject2.getString("Title");
							recinfo = new Recinfo();
							recinfo.setCompany(title);
							recinfo.setRecruitment_info(content);
							list.add(recinfo);
							Log.i("content", content);
							Log.i("title", title);

						}
						handler.post(r);

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
			ReinfoAdapter adapter = new ReinfoAdapter(GuideActivity.this, list);

			listview.setAdapter(adapter);
			dialog.dismiss();
		}
	};

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

}
