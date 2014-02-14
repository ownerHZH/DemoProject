package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.List;

import com.zgan.community.R;
import com.zgan.community.adapter.CommunityNewsAdapter;
import com.zgan.community.data.News;
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

import android.os.Bundle;
import android.os.Handler;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityNewNotificationActivity extends MainAcitivity {

	private Button back;
	private TextView title;

	private ListView list;
	private ListView list2;
	// private List<String> dList;//装载数据的List

	private Context con;
	private TabHost tabHost;
	private TabWidget tabWidget;
	private GestureDetector detector;

	private Handler handler;
	private MyProgressDialog dialog;
	int did = 1;
	int sid = 1;

	private List<News> newsList=new ArrayList<News>();;
	private MyProgressDialog pdialog;
	private CommunityNewsAdapter communityNewsAdapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_policital_news);

		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.community_policital);

		con = CommunityNewNotificationActivity.this;
		list = (ListView) findViewById(R.id.listViewPolitical);
		list2 = (ListView) findViewById(R.id.listViewPolitical2);
		ButtonClickListener l = new ButtonClickListener();
		back.setOnClickListener(l);

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
				.setIndicator(getString(R.string.community_notification_property_news),
						null).setContent(R.id.listViewPolitical));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(getString(R.string.community_notification_announcement), null)
				.setContent(R.id.listViewPolitical2));
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

			default:
				break;
			}
		}

	}

	private void showData(int i) {
		// dList=new ArrayList<String>();
		if (i == 1) {
			/*adapter = new CommunityPolicitalAdapter(con, contentDataList, 1);
			list.setDivider(null);// 设置ListView没有分割线
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();*/
			communityNewsAdapter=new CommunityNewsAdapter(con, newsList);
			list.setDivider(null);// 设置ListView没有分割线
			list.setAdapter(communityNewsAdapter);
			communityNewsAdapter.notifyDataSetChanged();
		} else {
			/*adapter = new CommunityPolicitalAdapter(con, MSZW_BGDDList, 2);
			list2.setDivider(null);// 设置ListView没有分割线
			list2.setAdapter(adapter);
			adapter.notifyDataSetChanged();*/
			communityNewsAdapter=new CommunityNewsAdapter(con, newsList);
			list2.setDivider(null);// 设置ListView没有分割线
			list2.setAdapter(communityNewsAdapter);
			communityNewsAdapter.notifyDataSetChanged();
		}

	}

	// 获取网络数据
	private void getData() {
		// TODO Auto-generated method stub
		//newsList = new ArrayList<News>();

		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"ZganNews.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
		//参数
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		//svr.addParameter("sid", StringTypeToInt.convertTypeToInt(button_key));
		
		HttpAndroidTask task = new HttpAndroidTask(con, svr,
				new HttpResponseHandler() {
					// 响应事件
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						pdialog.stop();
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,con,false);
						if (jsonEntity.getStatus() == 1) {
							handler.post(none);
						} else if (jsonEntity.getStatus() == 0) {
							newsList=(List<News>) GsonUtil.getData(
									jsonEntity,AppConstants.type_newsList);	
							
							if(newsList.size()>0)
			                {
			                	//有数据的时候操作
								handler.post(r);
			                }else
			                {
			                	//没有数据时候提示
			                	handler.post(none);
			                }
						}														
					}
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
						pdialog = new MyProgressDialog(context);
						DialogUtil.setAttr4progressDialog(pdialog);
					}
				});
		task.execute(new String[] {});	

	}

	// 无数据处理操作
	Runnable none = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// dialog.dismiss();
			//dialog.stop();
			Toast.makeText(con, "没有可供加载的数据", 2).show();
		}
	};

	// 数据加载完之后的操作
	Runnable r = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			// dialog.dismiss();
			//dialog.stop();
			if (newsList.size() <= 0) {
				Toast.makeText(con, "没有可加载的数据", 2).show();
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

