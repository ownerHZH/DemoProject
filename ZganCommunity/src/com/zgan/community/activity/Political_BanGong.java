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

import com.zgan.community.R;
import com.zgan.community.R.id;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.adapter.CommunityPolicitalAdapter;
import com.zgan.community.data.ContentData;
import com.zgan.community.data.MSZW_BGDD;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.MyProgressDialog;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Political_BanGong extends MainAcitivity {

	private ListView list2;
	private CommunityPolicitalAdapter adapter;
	private Context con;
	private List<MSZW_BGDD> MSZW_BGDDList = new ArrayList<MSZW_BGDD>();
	private String st;
	private String Data;
	private Handler handler;
	private MyProgressDialog dialog;
	
	private Button buttonCity; //市政府部门
	private Button buttonCounty;//区县政府
	private Button buttonCommonService;//公共服务单位
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_political__ban_gong);
		
		con = Political_BanGong.this;
		list2 = (ListView) findViewById(R.id.listViewPolitical2);
		buttonCity=(Button) findViewById(R.id.buttonCity);
		buttonCounty=(Button) findViewById(R.id.buttonCounty);
		buttonCommonService=(Button) findViewById(R.id.buttonCommonService);
		ButtonClickListener l = new ButtonClickListener();
		buttonCity.setOnClickListener(l);
		buttonCounty.setOnClickListener(l);
		buttonCommonService.setOnClickListener(l);
		
		handler = new Handler();
		dialog = new MyProgressDialog(this);
		dialog.start("加载中，请稍后...");
		
		adapter = new CommunityPolicitalAdapter(con, MSZW_BGDDList, 2);
		list2.setDivider(null);// 设置ListView没有分割线
		list2.setAdapter(adapter);
		
		getData();
	}
	
	// 获取网络数据
		private void getData() {
			// TODO Auto-generated method stub
			new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpGet get = new HttpGet(AppConstants.HttpHostAdress
							+ "zgancontent.aspx?did="
							+ ZganCommunityStaticData.User_Number);

					HttpClient client = new DefaultHttpClient();
					HttpResponse httpResponse;
					try {
						httpResponse = client.execute(get);
						HttpEntity entity = httpResponse.getEntity();
						StatusLine line = httpResponse.getStatusLine();
						// Log.i("linecode", "" + line.getStatusCode());
						if (line.getStatusCode() == 200) {
							st = EntityUtils.toString(entity);
							JSONObject jsonObject = new JSONObject(st);
							String status = jsonObject.get("status").toString();
							// Log.i("status", "" + status+"");
							if ("0".equals(status)) {
								Data = jsonObject.getString("data");
								JSONArray array = new JSONArray(Data);								

								JSONObject jsonObject3 = array.getJSONObject(1);
								String msStr = jsonObject3.getString("MSZW_BGDD");
								if (isNotNull(msStr)) {
									JSONArray MSZW_BGDDcontent = new JSONArray(
											msStr);
									if (MSZW_BGDDcontent.length() > 0) {
										
										for (int i = 0; i < MSZW_BGDDcontent
												.length(); i++) {
											MSZW_BGDD c = new MSZW_BGDD();
											JSONObject json = MSZW_BGDDcontent
													.getJSONObject(i);
											/*c.setAddLX(json.getString("AddLX"));
											c.setAddress(json.getString("Address"));
											c.setCID(json.getString("CID"));
											c.setGPSCoordinates(json
													.getString("GPSCoordinates"));
											c.setPublishers(json
													.getString("Publishers"));
											c.setPublishTime(json
													.getString("PublishTime"));
											c.setSName(json.getString("SName"));
											c.setTel(json.getString("Tel"));*/
											MSZW_BGDDList.add(c);
										}
									}
									handler.post(r);
								}
								
							} else {
								handler.post(none);
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
		}

		// 无数据处理操作
		Runnable none = new Runnable() {

			@Override
			public void run() {
				dialog.stop();
				Toast.makeText(con, "没有可供加载的数据", 2).show();
			}
		};

		// 数据加载完之后的操作
		Runnable r = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.dismiss();
				dialog.stop();
				if (MSZW_BGDDList.size() <= 0) {
					Toast.makeText(con, "实时政务没有可加载的数据", 2).show();
				}else
				{
					adapter.notifyDataSetChanged();
				}
			}
		};

		public boolean isNotNull(String str) {
			return ((str != null) && (str != "") && (!str.equals(null)) && (!str
					.equals("")));
		}
	
	public class ButtonClickListener implements View.OnClickListener {
		Intent intent = null;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.buttonCity:
				//市政府部门点击事件

				break;
			case R.id.buttonCounty:
				//区县政府点击事件

				break;
			case R.id.buttonCommonService:
				//公共服务单位点击事件

				break;

			default:
				break;
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_political__ban_gong, menu);
		return false;
	}

}
