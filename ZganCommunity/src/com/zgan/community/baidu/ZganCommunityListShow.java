package com.zgan.community.baidu;

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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zgan.community.R;
import com.zgan.community.activity.Reinfo_son;
import com.zgan.community.adapter.ListMapShowAdapter;
import com.zgan.community.adapter.ReinfoAdapter;
import com.zgan.community.data.Recinfo;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.MapShow;

public class ZganCommunityListShow extends MainAcitivity {
	Button back;
	TextView top_title;
	ListView listview;
	ProgressDialog dialog;

	MapShow mapShowlist;
	List<MapShow> list = new ArrayList<MapShow>();
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_maplistshow);
		setContentView(R.layout.line_recinfo_son);
		handler = new Handler();

		back = (Button) findViewById(R.id.back);
		top_title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.list_reinfo);
		listview.setDivider(null);
		back.setOnClickListener(listener);
		ShowData();
	}

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
			if (button_key.equals("超市")) {
				GetData(4);
			} else if (button_key.equals("快餐")) {
				GetData(1);
			} else if (button_key.equals("美容美发")) {
				GetData(3);
			} else if (button_key.equals("厨师")) {
				GetData(4);
			} else if (button_key.equals("干洗")) {
				GetData(7);
			} else if (button_key.equals("茶楼")) {
				GetData(2);
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
						"http://community1.zgantech.com/ZganService.aspx?did=15923258890&sid="
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
								String SName = jsonObject2.getString("SName");
								String Address = jsonObject2
										.getString("Address");
								String Tel = jsonObject2.getString("Tel");
								String Gpspoi = jsonObject2
										.getString("GPSCoordinates");
								Log.i("SName", SName);
								Log.i("Address", Address);
								Log.i("Tel", Tel);
								Log.i("Gpspoi", Gpspoi);
								mapShowlist=new MapShow();
								mapShowlist.setS_name(SName);
								mapShowlist.setS_address(Address);
								mapShowlist.setS_tel(Tel);
								mapShowlist.setS_poi(Gpspoi);

								list.add(mapShowlist);

							}
							handler.post(r);

						} else {
							dialog.dismiss();
							Toast.makeText(getApplicationContext(), "暂无相关招聘信息",
									Toast.LENGTH_SHORT).show();
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
			ListMapShowAdapter adapter = new ListMapShowAdapter(
					ZganCommunityListShow.this, list);

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
