package com.zgan.community.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.zgan.community.R;
import com.zgan.community.data.News;
import com.zgan.community.data.Weather;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.jsontool.GsonUtil;
import com.zgan.community.jsontool.HttpAndroidTask;
import com.zgan.community.jsontool.HttpClientService;
import com.zgan.community.jsontool.HttpPreExecuteHandler;
import com.zgan.community.jsontool.HttpResponseHandler;
import com.zgan.community.jsontool.JsonEntity;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainPageActivity extends MainAcitivity {

	private Button communityService; // 社区服务
	//private ImageButton communityCheap; // 好便宜
	private Button communityPay; // 充值缴费
	private Button communityTrade; // 社区商圈
	private Button communityPolitical; // 阳光政务
	private Button communityRecruit; // 招工信息
	private Button communityLivelihood; // 生活百事
	private Button communityHouseShelter; // 家庭卫士
	private Button communitySettings; // 联系物业
	private ImageView setting;

	private TextView clock; //时间
	private TextView place; // 地点
	private TextView date; // 日期
	private TextView temperature; // 温度
	private TextView message;//通知
	private LinearLayout messageLinear;
	//private Timer timer;
	private Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);

		communityService = (Button) findViewById(R.id.communityService);
		//communityCheap = (ImageButton) findViewById(R.id.communityCheap);
		communityPay = (Button) findViewById(R.id.communityPay);
		communityTrade = (Button) findViewById(R.id.communityTrade);
		communityPolitical = (Button) findViewById(R.id.communityPolicital);
		communityRecruit = (Button) findViewById(R.id.communityRecruit);
		communityLivelihood = (Button) findViewById(R.id.communityLivelihood);
		communityHouseShelter = (Button) findViewById(R.id.communityHouseShelter);
		communitySettings = (Button) findViewById(R.id.communitySettings);
		setting=(ImageView) findViewById(R.id.headIco);
		/*Gallery gallery = (Gallery) findViewById(R.id.advertising);
		ImagAdapter adapter=new ImagAdapter(con);
		gallery.setAdapter(adapter);*/
		con = MainPageActivity.this; // 初始化一个全局的Context
		
		clock=(TextView) findViewById(R.id.clock);
		place=(TextView) findViewById(R.id.place);
		date=(TextView) findViewById(R.id.date);
		temperature=(TextView) findViewById(R.id.temperature);
		message=(TextView) findViewById(R.id.message);
		messageLinear=(LinearLayout) findViewById(R.id.meLinearLayout);
		
		ButtonClickListener l = new ButtonClickListener(); // 按钮点击监听器初始化

		communityService.setOnClickListener(l); // 按钮注册监听器
		//communityCheap.setOnClickListener(l);
		communityPay.setOnClickListener(l);
		communityTrade.setOnClickListener(l);
		communityPolitical.setOnClickListener(l);
		communityRecruit.setOnClickListener(l);
		communityLivelihood.setOnClickListener(l);
		communityHouseShelter.setOnClickListener(l);
		setting.setOnClickListener(l);
		//communitySmartHousing.setOnClickListener(l);
		//communityNeighbor.setOnClickListener(l);
		communitySettings.setOnClickListener(l);
		messageLinear.setOnClickListener(l);
		setMainPagePara();
	}
	
	//设置主页的一些时间参数
	private void setMainPagePara()
	{
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(0x1);
			}
			
		}, 0, 30000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date.setText(sdf.format(new Date())+"  "+getWeekOfDate());
        getNews();
        getTemperature();
	}
	
	private Handler handler= new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x1) {
				Date date = new Date();
				clock.setText(date.getHours()+":"+date.getMinutes());
			}
		}
	};
	
	public  String getWeekOfDate(){
		String[] weekdays = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		Calendar cal = Calendar.getInstance();
		Date curDate = new Date(System.currentTimeMillis());
		  cal.setTime(curDate);
		    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		    if (w < 0)
		        w = 0;
		    return weekdays[w];
		}

	public class ButtonClickListener implements View.OnClickListener {

		Intent intent = null;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.communityService:
				// 社区服务点击响应事件
				intent = new Intent(con, MainFragmentActivity.class);
				intent.putExtra("TAG", R.id.radio_button0);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;

			/*case R.id.communityCheap:
				// 好便宜点击响应事件
				intent = new Intent(con, CommunityCheapActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;*/
			case R.id.meLinearLayout:
				// 社区通知点击响应事件
				intent = new Intent(con, MainTabActivity.class);
				intent.putExtra("TAG", R.id.radio_button2);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityPay:
				// 充值缴费点击响应事件
				intent = new Intent(con, MainTabActivity.class);
				intent.putExtra("TAG", R.id.radio_button6);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityTrade:
				// 社区商圈点击响应事件
				intent = new Intent(con, MainTabActivity.class);
				intent.putExtra("TAG", R.id.radio_button1);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityPolicital:
				// 阳光政务点击响应事件
				intent = new Intent(con, MainTabActivity.class);
				intent.putExtra("TAG", R.id.radio_button5);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityRecruit:
				// 招工信息点击响应事件
				intent = new Intent(con, MainTabActivity.class);
				intent.putExtra("TAG", R.id.radio_button3);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityLivelihood:
				// 生活百事点击响应事件
				intent = new Intent(con, MainTabActivity.class);
				intent.putExtra("TAG", R.id.radio_button4);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityHouseShelter:
				// 家庭卫士点击响应事件
				intent = new Intent(con, MainTabActivity.class);
				intent.putExtra("TAG", R.id.radio_button8);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communitySettings:
				// 我的设置点击响应事件
				intent = new Intent(con, MainTabActivity.class);
				intent.putExtra("TAG", R.id.radio_button7);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				//finish();
				break;
			case R.id.headIco:
				// 我的设置点击响应事件
				intent = new Intent(con, CommunitySetting.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				//finish();
				break;

			default:
				break;
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	//获取当前温度
	private void getTemperature() {
		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"zganweather.aspx");
		//参数
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
				
		HttpAndroidTask task = new HttpAndroidTask(con, svr,
				new HttpResponseHandler() {					
					// 响应事件
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,con,false);
						if (jsonEntity.getStatus() == 1) {
							temperature.setText("0");
						} else if (jsonEntity.getStatus() == 0) {
								List<Weather> weathers = (List<Weather>) GsonUtil.getData(
										jsonEntity,AppConstants.type_weatherList);	
							    if(weathers.size()>0)
							    {
							    	temperature.setText(weathers.get(0).getWd());
							    }else
							    {
							    	temperature.setText("0");
							    }
						}														
					}					
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
					}
				});
		task.execute(new String[] {});	
	}
	
	//社区公告数据
	private void getNews() {
		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"zgannews.aspx");
		//参数
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		svr.addParameter("method","news_cq");
				
		HttpAndroidTask task = new HttpAndroidTask(con, svr,
				new HttpResponseHandler() {					
					// 响应事件
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,con,false);
						if (jsonEntity.getStatus() == 1) {
							handler.post(none_s);
						} else if (jsonEntity.getStatus() == 0) {
								List<News> newsList_s = (List<News>) GsonUtil.getData(
										jsonEntity,AppConstants.type_newsList);	
								
								if(newsList_s.size()>0)
				                {
				                	//有数据的时候操作
									handler.post(r(newsList_s.get(0)));
				                }														
						}														
					}					
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
					}
				});
		task.execute(new String[] {});	
	}
	
	//有公告的时候
	private Runnable r(final News news) {
		return new Runnable() {
			
			@Override
			public void run() {
				message.setText(news.getTitle());
			}
		};
	}
	
	//无公告的时候
	private Runnable none_s=new Runnable() {
		
		@Override
		public void run() {
			message.setText("暂无最新公告");
		}
	};
	/*class ImagAdapter extends BaseAdapter {
		private Context context;// 用于接收传递过来的Context对象

		public ImagAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.i("im.length", ""+im.length);
			return im.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.i("magc", ""+position);

			ImageView iv = new ImageView(MainPageActivity.this);// 针对外面传递过来的Context变量，
			//内存防止溢出
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1 ;
						BitmapFactory.Options opt = new BitmapFactory.Options();  
	        opt.inPreferredConfig = Bitmap.Config.RGB_565;  
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;  
	        // 获取资源图片  
	        Bitmap bitmap = BitmapFactory.decodeResource(MainPageActivity.this.getResources(), im[position]);

			iv.setImageBitmap(bitmap);
	        return iv;
		}*/


}
