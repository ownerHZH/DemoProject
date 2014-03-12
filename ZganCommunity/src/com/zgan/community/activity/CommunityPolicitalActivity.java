package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.List;
import com.zgan.community.R;
import com.zgan.community.adapter.CommunityPolicitalAdapter;
import com.zgan.community.data.ContentData;
import com.zgan.community.data.MSZW_BGDD;
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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityPolicitalActivity extends MainAcitivity {

	private Button back;
	private TextView title;

	private ListView list;
	private ListView list2;
	//private List<String> dList;//装载数据的List

	private Context con;
	private TabHost tabHost;
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
	private RadioGroup group;	

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_policital);		

		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.community_policital);
		
		group=(RadioGroup) findViewById(R.id.group);
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
		group.setOnCheckedChangeListener(listener);
		handler = new Handler();
		 		
		initTabHost(); // 初始化TabHost
		getData();// 获取网络数据
	}	
	
	private OnCheckedChangeListener listener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.zhengwuyaowen:
				if(contentDataList.size()>0)
				{
					showData(1);
				}else
				{
					getData();
				}
				tabHost.setCurrentTabByTag("tab1");
				break;
            case R.id.bangongdidian:
            	getData_b(-1);
            	tabHost.setCurrentTabByTag("tab2");
				break;

			default:
				break;
			}
		}
	};

	/**
	 * 初始化TabHost
	 */
	public void initTabHost() {
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();
		tabHost.getTabWidget();

		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator(null,null)
			    .setContent(R.id.listViewPolitical));

		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator(null,null)
				.setContent(R.id.linearLayoutPolitical2));
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
