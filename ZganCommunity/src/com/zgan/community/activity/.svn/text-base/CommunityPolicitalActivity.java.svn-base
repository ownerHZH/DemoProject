package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

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
import com.zgan.community.adapter.CommunityPolicitalAdapter;
import com.zgan.community.data.ContentData;
import com.zgan.community.data.MSZW_BGDD;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.MyProgressDialog;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.os.Bundle;
import android.os.Handler;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class CommunityPolicitalActivity extends MainAcitivity {

	private Button back;
	private TextView title;

	private ListView list;
	private ListView list2;
	// private List<String> dList;//装载数据的List

	private Context con;
	private TabHost tabHost;
	private TabWidget tabWidget;
	private GestureDetector detector;

	private String st;
	private String Data;
	private Handler handler;
	private MyProgressDialog dialog;
	int did = 1;
	int sid = 1;

	private List<ContentData> contentDataList = new ArrayList<ContentData>();
	private List<MSZW_BGDD> MSZW_BGDDList = new ArrayList<MSZW_BGDD>();

	private CommunityPolicitalAdapter adapter;
	
	private LinearLayout category;
	private Button buttonCity; //市政府部门
	private Button buttonCounty;//区县政府
	private Button buttonCommonService;//公共服务单位

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_policital);		

		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.community_policital);

		con = CommunityPolicitalActivity.this;
		list = (ListView) findViewById(R.id.listViewPolitical);
		list2 = (ListView) findViewById(R.id.listViewPolitical2);
		
		category=(LinearLayout) findViewById(R.id.category);
		buttonCity=(Button) findViewById(R.id.buttonCity);
		buttonCounty=(Button) findViewById(R.id.buttonCounty);
		buttonCommonService=(Button) findViewById(R.id.buttonCommonService);
		
		ButtonClickListener l = new ButtonClickListener();
		back.setOnClickListener(l);
		buttonCity.setOnClickListener(l);
		buttonCounty.setOnClickListener(l);
		buttonCommonService.setOnClickListener(l);
		
		handler = new Handler();
		/*
		 * dialog = new ProgressDialog(this); dialog.setTitle(null);
		 * dialog.setMessage("加载中，请稍后"); dialog.show();
		 */
		dialog = new MyProgressDialog(this);
		dialog.start("加载中，请稍后...");

		initTabHost(); // 初始化TabHost
		// setTabViewParas();//设置Tab显示属性
		// tabHost.setCurrentTab(1);
		tabHost.setOnTabChangedListener(new TabChangeListener());
		// tabHost.setCurrentTab(0);
		// 手势内部类初始化
		detector = new GestureDetector(new MySimpleGestureDetector());
		getData();// 获取网络数据

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event); // 关联
	}

	/**
	 * 识别手势的内部类
	 * 
	 * @author Hzh
	 * 
	 */
	public class MySimpleGestureDetector extends
			GestureDetector.SimpleOnGestureListener {

		private static final int MIN_DISTANCE = 100; // 最小距离
		private static final int MIN_VELOCITY = 100; // 最小滑动速率

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (Math.abs(velocityX) > MIN_VELOCITY) {
				if ((e2.getX() - e1.getX()) > MIN_DISTANCE) { // 向右滑动
					flingRight();
				} else if ((e1.getX() - e2.getX()) > MIN_DISTANCE) { // 向左滑动
					flingLeft();
				}
			}
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		public void flingLeft() {
			int currentTab = tabHost.getCurrentTab();
			int count = tabHost.getTabWidget().getChildCount();
			if (currentTab != 0) {
				currentTab--;
				switchTab(currentTab);
			} else if (currentTab == 0) {
				switchTab(count - 1);
			}
		}

		public void flingRight() {
			int currentTab = tabHost.getCurrentTab();
			int count = tabHost.getTabWidget().getChildCount();
			if (currentTab != count - 1) {
				currentTab++;
				switchTab(currentTab);
			} else if (currentTab == count - 1) {
				switchTab(0);
			}
		}

		private void switchTab(final int toTab) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					tabHost.post(new Runnable() {
						@Override
						public void run() {
							tabHost.setCurrentTab(toTab);
						}
					});
				}
			}).start();
		}
	}

	/**
	 * Tab切换监听器
	 * 
	 * @return
	 */
	public class TabChangeListener implements OnTabChangeListener {

		public void onTabChanged(String str) {
			setTabBackground();
			if (str.equals("tab1")) {
				showData(1);
			} else if (str.equals("tab2")) {
				showData(2);
			}
		}
	}

	/**
	 * 设置Tab切换背景
	 */
	public void setTabBackground() {
		// 设置Tab背景
		int count = tabWidget.getChildCount();
		for (int i = 0; i < count; i++) {
			View v = tabWidget.getChildAt(i);
			if (tabHost.getCurrentTab() == i) {
				v.setBackgroundColor(Color.WHITE);
				// 在这里最好自己设置一个图片作为背景更好
				// v.setBackgroundDrawable(getResources().getDrawable(R.drawable.chat));
			} else {
				v.setBackgroundColor(Color.GRAY);
			}
		}
	}

	/**
	 * 初始化TabHost
	 */
	public void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		// 设置Tab的样式
		tabWidget = tabHost.getTabWidget();

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator(getString(R.string.community_policital_news),
						null).setContent(R.id.listViewPolitical));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(getString(R.string.community_policital_law), null)
				.setContent(R.id.linearLayoutPolitical2));
		setTabViewParas();// 设置Tab显示属性
		setTabBackground();// 第一次设置显示背景色
		// showData(1);//显示第一页的数据

	}

	/**
	 * 设置TabView的字体大小、高度
	 */
	public void setTabViewParas() {
		int count = tabWidget.getChildCount();// TabHost中有一个getTabWidget()的方法
		for (int i = 0; i < count; i++) {
			View view = tabWidget.getChildTabViewAt(i);
			view.getLayoutParams().height = 80; // tabWidget.getChildAt(i)
			// view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
			final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setTextColor(this.getResources().getColorStateList(
					android.R.color.black));
		}
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

	private void showData(int i) {
		// dList=new ArrayList<String>();
		if (i == 1) {
			adapter = new CommunityPolicitalAdapter(con, contentDataList, 1);
			list.setDivider(null);// 设置ListView没有分割线
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		} else {
			adapter = new CommunityPolicitalAdapter(con, MSZW_BGDDList, 2);
			list2.setDivider(null);// 设置ListView没有分割线
			list2.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

	}

	// 获取网络数据
	private void getData() {
		// TODO Auto-generated method stub
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpGet get = new HttpGet(AppConstants.HttpHostAdress
						+ "ZganContent.aspx?did="
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
										c.setCContent(json
												.getString("CContent"));
										c.setCID(json.getString("CID"));
										c.setContentTime(json
												.getString("ContentTime"));
										c.setPublishers(json
												.getString("Publishers"));
										c.setTitle(json.getString("Title"));
										contentDataList.add(c);
									}
								}
							}

							JSONObject jsonObject3 = array.getJSONObject(1);
							String msStr = jsonObject3.getString("MSZW_BGDD");
							if (isNotNull(msStr)) {
								JSONArray MSZW_BGDDcontent = new JSONArray(
										msStr);
								if (MSZW_BGDDcontent.length() > 0) {
									category.setVisibility(View.VISIBLE);
									
									for (int i = 0; i < MSZW_BGDDcontent
											.length(); i++) {
										MSZW_BGDD c = new MSZW_BGDD();
										JSONObject json = MSZW_BGDDcontent
												.getJSONObject(i);
										c.setAddLX(json.getString("AddLX"));
										c.setAddress(json.getString("Address"));
										c.setCID(json.getString("CID"));
										c.setGPSCoordinates(json
												.getString("GPSCoordinates"));
										c.setPublishers(json
												.getString("Publishers"));
										c.setPublishTime(json
												.getString("PublishTime"));
										c.setSName(json.getString("SName"));
										c.setTel(json.getString("Tel"));
										MSZW_BGDDList.add(c);
									}
								}else
								{
									category.setVisibility(View.GONE);
								}
							}
							handler.post(r);
						} else {
							handler.post(none);
						}

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// super.run();
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
			// TODO Auto-generated method stub
			// dialog.dismiss();
			dialog.stop();
			if (contentDataList.size() <= 0) {
				Toast.makeText(con, "实时政务没有可加载的数据", 2).show();
				tabHost.setCurrentTabByTag("tab2");// 选中第二个Tab
				showData(2);// 初始化数据
			} else {
				tabHost.setCurrentTabByTag("tab1");// 选中第一个Tab
				showData(1);// 初始化数据
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
		// getMenuInflater().inflate(R.menu.activity_community_policital, menu);
		return true;
	}

}
