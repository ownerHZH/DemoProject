package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zgan.community.R;
import com.zgan.community.adapter.CommunityPayAdapter;
import com.zgan.community.adapter.ReinfoAdapter;
import com.zgan.community.data.Recinfo;
import com.zgan.community.data.ServiceInfo;
import com.zgan.community.adapter.CommunityServiceAdapter;
import com.zgan.community.data.CommunityService;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class Reinfo_son extends MainAcitivity {
	Button back;
	TextView top_title;

	ListView listview;
	Recinfo recinfo;
	List<Recinfo> list = new ArrayList<Recinfo>();
	ProgressDialog dialog;
	Handler handler;

	private CommunityServiceAdapter communityServiceAdapter;

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
		back.setOnClickListener(listener);
		ShowData();

	}

	/**
	 * 
	 */

	private void ShowData() {
		// TODO Auto-generated method stub
		if ((getIntent().getExtras().getString("button_key")) != null
				&& !"".equals(getIntent().getExtras().getString("button_key"))) {
			String button_key = getIntent().getExtras().getString("button_key");

			top_title.setText(button_key);
			if (button_key.equals("社区服务")) {
				/*
				 * List<ServiceInfo> dataList = new ArrayList<ServiceInfo>();
				 * dataList.add(new ServiceInfo("中感科技社区服务小李", "重庆市南岸区丹龙路",
				 * "18623648313")); dataList.add(new ServiceInfo("中感科技社区服务小赵",
				 * "重庆市南岸区丹龙路8号", "18716341029"));
				 * 
				 * communityServiceAdapter = new CommunityServiceAdapter(
				 * Reinfo_son.this, dataList);
				 * listview.setAdapter(communityServiceAdapter);
				 */
			} else {

				dialog = new ProgressDialog(this);
				dialog.setTitle("提示");
				dialog.setMessage("加载中，请稍后");
				dialog.show();
				if (button_key.equals("服务员")) {
					GetData(1);
				} else if (button_key.equals("保洁")) {
					GetData(2);
				} else if (button_key.equals("司机")) {
					GetData(3);
				} else if (button_key.equals("厨师")) {
					GetData(4);
				} else if (button_key.equals("快递员")) {
					GetData(5);
				} else if (button_key.equals("销售")) {
					GetData(6);
				} else if (button_key.equals("技工")) {
					GetData(7);
				} else if (button_key.equals("客服")) {
					GetData(8);
				} else if (button_key.equals("营业员")) {
					GetData(9);
				} else if (button_key.equals("会计")) {
					GetData(10);
				} else if (button_key.equals("文员")) {
					GetData(11);
				} else if (button_key.equals("其他")) {
					GetData(12);
				} else {
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "暂无招聘信息",
							Toast.LENGTH_SHORT).show();
				}

			}
		}
	}

	private void GetData(final int sid) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet(AppConstants.HttpHostAdress+
						"zganjobs.aspx?did="
								+ ZganCommunityStaticData.User_Number + "&sid="
								+ sid);

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
							String Data = jsonObject.getString("data");
							Log.i("Data", "" + Html.fromHtml(Data).toString());

							JSONArray array = new JSONArray(Data);
							for (int i = 0; i < array.length(); i++) {
								JSONObject jsonObject2 = array.getJSONObject(i);
								String content = jsonObject2
										.getString("CContent");
								String title = jsonObject2.getString("Title");
								recinfo = new Recinfo();
								recinfo.setCompany(title);
								recinfo.setRecruitment_info(content);
								list.add(recinfo);

							}
							handler.post(r);

						} else {
							Looper.prepare();
							dialog.dismiss();
							Toast.makeText(getApplicationContext(), "暂无相关招聘信息",
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

	Runnable r = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ReinfoAdapter adapter = new ReinfoAdapter(Reinfo_son.this, list);

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
