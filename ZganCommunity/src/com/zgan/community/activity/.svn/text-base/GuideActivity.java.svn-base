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
import android.content.Context;
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
import com.zgan.community.data.BgddDetail;
import com.zgan.community.data.CommunityService;
import com.zgan.community.data.Recinfo;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.jsontool.DialogUtil;
import com.zgan.community.jsontool.GsonUtil;
import com.zgan.community.jsontool.HttpAndroidTask;
import com.zgan.community.jsontool.HttpClientService;
import com.zgan.community.jsontool.HttpPreExecuteHandler;
import com.zgan.community.jsontool.HttpResponseHandler;
import com.zgan.community.jsontool.JsonEntity;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.MyProgressDialog;
import com.zgan.community.tools.ZganCommunityStaticData;

public class GuideActivity extends MainAcitivity {
	Button back;
	TextView top_title;

	ListView listview;
	Recinfo recinfo;
	List<Recinfo> list;
	ProgressDialog dialog;
	Handler handler;
	
	private Context context=GuideActivity.this;

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

			dialog = new ProgressDialog(this);
			dialog.setTitle("提示");
			dialog.setMessage("加载中，请稍后");
			dialog.show();

			list = new ArrayList<Recinfo>();
			if (button_key.equals("户籍")) {
				getData(1);
			} else if (button_key.equals("社保")) {
				getData(3);
			} else if (button_key.equals("就业")) {
				getData(4);
			} else if (button_key.equals("车辆")) {
				getData(5);
			} else if (button_key.equals("公证")) {
				getData(6);
			} else if (button_key.equals("婚姻")) {
				getData(7);
			} else if (button_key.equals("生育")) {
				getData(8);
			} else if (button_key.equals("纳税")) {
				getData(9);
			} else if (button_key.equals("住房")) {
				getData(10);
			} else if (button_key.equals("出入境")) {
				getData(11);
			} else if (button_key.equals("公租房")) {
				getData(12);
			} else if (button_key.equals("兵役")) {
				getData(13);
			} else {
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "暂无招聘信息",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/*private void GetData(final int sid) {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet(AppConstants.HttpHostAdress+
						"zgancontent.aspx?did="
								+ ZganCommunityStaticData.User_Number + "&method=bszn");
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
							String content = jsonObject2.getString("content");
							String title = jsonObject2.getString("title");
							String id = jsonObject2.getString("id");
							String releasetime = jsonObject2.getString("releasetime");
							
							recinfo = new Recinfo();
							recinfo.setCompany(title);
							recinfo.setRecruitment_info(content);
							recinfo.setId(id);
							recinfo.setTitle(title);
							recinfo.setContent(content);
							recinfo.setReleasetime(releasetime);
							list.add(recinfo);

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

	}*/

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
	
	//获取办事指南信息
	private void getData(int i) {
		// TODO Auto-generated method stub
		//newsList = new ArrayList<News>();

		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"zgancontent.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
		//参数
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		svr.addParameter("method","bsznfllist");
	    svr.addParameter("flid",i);
				
		HttpAndroidTask task = new HttpAndroidTask(context, svr,
				new HttpResponseHandler() {
					// 响应事件
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,context,false);
						if (jsonEntity.getStatus() == 1) {
							Toast.makeText(context, "暂时没有信息", Toast.LENGTH_SHORT).show();
						} else if (jsonEntity.getStatus() == 0) {
							list=(List<Recinfo>) GsonUtil.getData(
										jsonEntity,AppConstants.type_recinfoList);	
								
								if(list.size()>0)
				                {
				                	//有数据的时候操作
									handler.post(r);
				                }else
				                {
				                	//没有数据时候提示
				                	Toast.makeText(context, "暂时没有信息", Toast.LENGTH_SHORT).show();
				                }														
						}														
					}
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
					}
				});
		task.execute(new String[] {});	
	}

}
