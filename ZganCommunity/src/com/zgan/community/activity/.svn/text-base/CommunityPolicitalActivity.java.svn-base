package com.zgan.community.activity;

import java.lang.reflect.Field;
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

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.app.ProgressDialog;
import android.app.TabActivity;
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
	//private List<String> dList;//装载数据的List

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
	
	//private LinearLayout category;
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
		
		//category=(LinearLayout) findViewById(R.id.category);
		buttonCity=(Button) findViewById(R.id.buttonCity);
		buttonCounty=(Button) findViewById(R.id.buttonCounty);
		buttonCommonService=(Button) findViewById(R.id.buttonCommonService);
		
		ButtonClickListener l = new ButtonClickListener();
		back.setOnClickListener(l);
		buttonCity.setOnClickListener(l);
		buttonCounty.setOnClickListener(l);
		buttonCommonService.setOnClickListener(l);
		
		handler = new Handler();
		 
		//dialog = new MyProgressDialog(this);
		//dialog.start("加载中，请稍后...");

		
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
				if(contentDataList.size()>0)
				{
					showData(1);
				}else
				{
					getData();
				}
				
			} else if (str.equals("tab2")) {
			    getData_b(-1);			
			}
		}
	}

	/**
	 * 设置Tab切换背景
	 */
	@SuppressWarnings("deprecation")
	public void setTabBackground() {
		// 设置Tab背景
		View v0=tabWidget.getChildTabViewAt(0);
		View v1=tabWidget.getChildTabViewAt(1);
		//int count = tabWidget.getChildCount();
		if (tabHost.getCurrentTab() == 0) {
			//v.setBackgroundColor(Color.WHITE);
			// 在这里最好自己设置一个图片作为背景更好				
		    v0.setBackgroundDrawable(getResources().getDrawable(R.drawable.zheng2));
		    v1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ban1));
			
		} else {
			//v.setBackgroundColor(Color.GRAY);
		    v0.setBackgroundDrawable(getResources().getDrawable(R.drawable.zheng1));
		    v1.setBackgroundDrawable(getResources().getDrawable(R.drawable.ban2));
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
				.setIndicator(null,null)
			    .setContent(R.id.listViewPolitical));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(null,null)
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
			view.getLayoutParams().height = 78; // tabWidget.getChildAt(i)
			// view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
			/*final TextView tv = (TextView) view
					.findViewById(android.R.id.title);
			tv.setTextSize(20);
			tv.setTextColor(this.getResources().getColorStateList(
					android.R.color.black));*/
			Field mBottomLeftStrip;
	        Field mBottomRightStrip;
			 if (Float.valueOf(Build.VERSION.RELEASE.substring(0, 3)) <= 2.1) {
	               try {
	                  mBottomLeftStrip = tabWidget.getClass().getDeclaredField ("mBottomLeftStrip");
	                  mBottomRightStrip = tabWidget.getClass().getDeclaredField ("mBottomRightStrip");
	                  if(!mBottomLeftStrip.isAccessible()) {
	                    mBottomLeftStrip.setAccessible(true);
	                  }
	                  if(!mBottomRightStrip.isAccessible()){
	                    mBottomRightStrip.setAccessible(true);
	                  }
	                mBottomLeftStrip.set(tabWidget, getResources().getDrawable (R.drawable.no));
	                mBottomRightStrip.set(tabWidget, getResources().getDrawable (R.drawable.no));
	                 
	               } catch (Exception e) {
	                 e.printStackTrace();
	               }
	        } else {
	         
	         //如果是2.2,2.3版本开发,可以使用一下方法tabWidget.setStripEnabled(false)
	         //tabWidget.setStripEnabled(false);
	         
	         //但是很可能你开发的android应用是2.1版本，
	         //tabWidget.setStripEnabled(false)编译器是无法识别而报错的,这时仍然可以使用上面的
	         //反射实现，但是代码的改改
	         
	          try {
	           //2.2,2.3接口是mLeftStrip，mRightStrip两个变量，当然代码与上面部分重复了
	                 mBottomLeftStrip = tabWidget.getClass().getDeclaredField ("mLeftStrip");
	                 mBottomRightStrip = tabWidget.getClass().getDeclaredField ("mRightStrip");
	                 if(!mBottomLeftStrip.isAccessible()) {
	                   mBottomLeftStrip.setAccessible(true);
	                 }
	                 if(!mBottomRightStrip.isAccessible()){
	                   mBottomRightStrip.setAccessible(true);
	                 }
	               mBottomLeftStrip.set(tabWidget, getResources().getDrawable (R.drawable.no));
	               mBottomRightStrip.set(tabWidget, getResources().getDrawable (R.drawable.no));
	                
	              } catch (Exception e) {
	                e.printStackTrace();
	              }
	        }
		}
	}

	public class ButtonClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.buttonCity:
				//市政府部门点击事件
                 getData_b(0);
				break;
			case R.id.buttonCounty:
				//区县政府点击事件
				getData_b(1);
				break;
			case R.id.buttonCommonService:
				//公共服务单位点击事件
				getData_b(2);
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
	//社区公告数据
		private void getData() {
			// TODO Auto-generated method stub
			//newsList = new ArrayList<News>();

			HttpClientService svr = new HttpClientService(
					AppConstants.HttpHostAdress+"zgancontent.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
			//参数
			svr.addParameter("did",ZganCommunityStaticData.User_Number);
					
			HttpAndroidTask task = new HttpAndroidTask(con, svr,
					new HttpResponseHandler() {
						// 响应事件
						@SuppressWarnings("unchecked")
						public void onResponse(Object obj) {
							dialog.stop();
							JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
									obj,con,false);
							if (jsonEntity.getStatus() == 1) {
								handler.post(none);
							} else if (jsonEntity.getStatus() == 0) {
								contentDataList=(List<ContentData>) GsonUtil.getData(
											jsonEntity,AppConstants.type_contentDataList);	
									
									if(contentDataList.size()>0)
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
							dialog = new MyProgressDialog(context);
							DialogUtil.setAttr4progressDialog(dialog);
						}
					});
			task.execute(new String[] {});	
		}
		
		private void getData_b(int flid) {
			// TODO Auto-generated method stub
			//newsList = new ArrayList<News>();

			HttpClientService svr = new HttpClientService(
					AppConstants.HttpHostAdress+"zgancontent.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
			//参数
			svr.addParameter("did",ZganCommunityStaticData.User_Number);
			if(flid==-1)
			{
				svr.addParameter("method","bgdd");
			}else
			{
				svr.addParameter("method","bgddfl");
				svr.addParameter("flid",flid);
			}
			
					
			HttpAndroidTask task = new HttpAndroidTask(con, svr,
					new HttpResponseHandler() {
						// 响应事件
						@SuppressWarnings("unchecked")
						public void onResponse(Object obj) {
							dialog.stop();
							JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
									obj,con,false);
							if (jsonEntity.getStatus() == 1) {
								handler.post(none);
							} else if (jsonEntity.getStatus() == 0) {
								MSZW_BGDDList.clear();
								MSZW_BGDDList=(List<MSZW_BGDD>) GsonUtil.getData(
											jsonEntity,AppConstants.type_mSZW_BGDDList);	
									
									if(MSZW_BGDDList.size()>0)
					                {
					                	//有数据的时候操作
										handler.post(r_b);
					                }else
					                {
					                	//没有数据时候提示
					                	handler.post(none_b);
					                }														
							}														
						}
					}, new HttpPreExecuteHandler() {
						public void onPreExecute(Context context) {
							dialog = new MyProgressDialog(context);
							DialogUtil.setAttr4progressDialog(dialog);
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
			if (contentDataList.size() <= 0) {
				Toast.makeText(con, "实时政务没有可加载的数据", 2).show();
			} else {
				tabHost.setCurrentTabByTag("tab1");// 选中第一个Tab
				showData(1);// 初始化数据
			}

		}
	};
	
	// 无数据处理操作
		Runnable none_b = new Runnable() {

			@Override
			public void run() {
				Toast.makeText(con, "没有可供加载的数据", 2).show();
			}
		};

		// 数据加载完之后的操作
		Runnable r_b = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// dialog.dismiss();
				if (MSZW_BGDDList.size() <= 0) {
					Toast.makeText(con, "没有可加载的数据", 2).show();
				} else {
					tabHost.setCurrentTabByTag("tab2");// 选中第一个Tab
					showData(2);// 初始化数据
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
