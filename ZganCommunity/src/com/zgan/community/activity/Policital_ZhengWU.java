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
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class Policital_ZhengWU extends MainAcitivity {

	private ListView list;
	private CommunityPolicitalAdapter adapter;
	private Context con;
	private List<ContentData> contentDataList = new ArrayList<ContentData>();
	private String st;
	private String Data;
	private Handler handler;
	private MyProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_policital__zheng_wu);
		con=Policital_ZhengWU.this;
		
		handler = new Handler();
		dialog = new MyProgressDialog(this);
		dialog.start("加载中，请稍后...");
		
		list = (ListView) findViewById(R.id.listViewPolitical);
		adapter = new CommunityPolicitalAdapter(con, contentDataList, 1);
		list.setDivider(null);// 设置ListView没有分割线
		list.setAdapter(adapter);
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
							+ " zgancontent.aspx?did="
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

								JSONObject jsonObject2 = array.getJSONObject(0);
								String conStr = jsonObject2
										.getString("ContentData");
								if (isNotNull(conStr)) {
									JSONArray ContentDatacontent = new JSONArray(
											conStr);
									System.out.println("条数==="+ContentDatacontent.length());
									if (ContentDatacontent.length() > 0) {
										for (int i = 0; i < ContentDatacontent
												.length(); i++) {
											ContentData c = new ContentData();
											JSONObject json = ContentDatacontent
													.getJSONObject(i);
											/*c.setCContent(json
													.getString("CContent"));
											c.setCID(json.getString("CID"));
											c.setContentTime(json
													.getString("ContentTime"));
											c.setPublishers(json
													.getString("Publishers"));*/
											c.setTitle(json.getString("Title"));
											contentDataList.add(c);
										}
									}
									handler.post(r);
								}                                          								
								
							} else {
								handler.post(none);
							}

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}.start();
		}
		
		// 无数据处理操作
		Runnable none = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.dismiss();
				dialog.stop();
				Toast.makeText(con, "没有可供加载的数据", 2).show();
			}
		};

		// 数据加载完之后的操作
		Runnable r = new Runnable() {

			@Override
			public void run() {
				dialog.stop();
				if (contentDataList.size() <= 0) {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_policital__zheng_wu, menu);
		return false;
	}

}
