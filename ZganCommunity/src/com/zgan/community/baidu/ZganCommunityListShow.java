package com.zgan.community.baidu;

import java.io.Serializable;
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

import com.baidu.mapapi.search.MKPoiInfo;
import com.zgan.community.R;
import com.zgan.community.activity.Reinfo_son;
import com.zgan.community.adapter.ListMapShowAdapter;
import com.zgan.community.adapter.ReinfoAdapter;
import com.zgan.community.data.Recinfo;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.MapShow;
import com.zgan.community.tools.ZganCommunityStaticData;

public class ZganCommunityListShow extends MainAcitivity {
	Button back;
	TextView top_title;
	ListView listview;
	ProgressDialog dialog;

	MapShow mapShowlist;
	List<MapShow> list = new ArrayList<MapShow>();
	List<MKPoiInfo> mkPoiInfos=new ArrayList<MKPoiInfo>();

	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.line_maplistshow);
		setContentView(R.layout.line_recinfo_son);
		handler = new Handler();

		back = (Button) findViewById(R.id.back);
		top_title = (TextView) findViewById(R.id.title);
		listview = (ListView) findViewById(R.id.list_reinfo);
		listview.setDivider(null);
		back.setOnClickListener(listener);
		ShowData();
	}

	@SuppressWarnings("unchecked")
	private void ShowData() {
		// TODO Auto-generated method stub
		if ((getIntent().getExtras().getString("button_key")) != null
				&& !"".equals(getIntent().getExtras().getString("button_key"))) {
			String button_key = getIntent().getExtras().getString("button_key");
			mkPoiInfos=ZganCommunityMapShow.mkPoiInfos;
			
			top_title.setText(button_key);
			dialog = new ProgressDialog(this);
			dialog.setTitle("提示");
			dialog.setMessage("加载中，请稍后");
			dialog.show();
			for (int i = 0; i < mkPoiInfos.size(); i++) {
				String SName = mkPoiInfos.get(i).name;
				String Address = mkPoiInfos.get(i).address;
				String Tel =  mkPoiInfos.get(i).phoneNum;
				
				Log.i("SName", SName);
				Log.i("Address", Address);
				Log.i("Tel", Tel);
				
				mapShowlist = new MapShow();
				mapShowlist.setS_name(SName);
				mapShowlist.setS_address(Address);
				mapShowlist.setS_tel(Tel);

				list.add(mapShowlist);

			}
			handler.post(r);
			
		}
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
