package com.zgan.wifi;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.Manifest.permission;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText name;
	private EditText password;
	private Button connect;
	//private TextView showInfo;
	private ListView list;
	private Handler handler;
	//private StringBuilder builder;
	private WifiAdmin wifiAdmin;
	private List<ScanResult> wifiList;
	private WifiListAdapter adapter;
	/** WIFI开关状态监听广播 **/
	private BroadcastReceiver receiver;
	/** WIFI设置成功状态监听广播 **/
	private BroadcastReceiver setReceiver;
	
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		name=(EditText) findViewById(R.id.wifiNameEditText);
		password=(EditText) findViewById(R.id.wifiPasswordEditText);
		connect=(Button) findViewById(R.id.connectButton);
		list=(ListView) findViewById(R.id.listView1);
		context=MainActivity.this;
		//showInfo=(TextView) findViewById(R.id.showInfoTextView);
		//builder=new StringBuilder();
		handler=new Handler();
		wifiAdmin = new WifiAdmin(MainActivity.this);
		
		registerWifiChangeBoradCast();		 		
		getCurrentConnectedWifiInfo();			
		
		connect.setOnClickListener(new OnClickListener() {		
		@Override
		public void onClick(View v) {
			if (!wifiAdmin.getmWifiManager().isWifiEnabled()) {
				if (wifiAdmin.getmWifiManager().setWifiEnabled(true)) {
					String na=name.getText().toString();
					String pa=password.getText().toString();
					connectTargetWifi(na, pa);//链接指定wifi
				}else{
					Toast.makeText(MainActivity.this, "wifi开启失败，请手动开启", Toast.LENGTH_LONG).show();
				}
			}else{
				String na=name.getText().toString();
				String pa=password.getText().toString();
				connectTargetWifi(na, pa);//链接指定wifi
			}
			
			//registerWifiChangeBoradCast();					    
		}
	});
		
	}

	private void getCurrentConnectedWifiInfo() {
		//android:text="Zgan_JTWS"
		//android:text="Zg123456"
		wifiAdmin.openWifi();//打开wifi
		if (!wifiAdmin.getmWifiManager().isWifiEnabled()) {
			if (wifiAdmin.getmWifiManager().setWifiEnabled(true)) {
				
				//WifiInfo wifiInfo=wifiAdmin.GetWifiInfo();//获取链接的wifi信息
				//name.setText(wifiInfo.getSSID());
				//WifiConfiguration  wifiConfiguration=wifiAdmin.IsExsits(wifiInfo.getSSID());
				//String pass=wifiConfiguration.preSharedKey==null?wifiConfiguration.wepKeys[0]:wifiConfiguration.preSharedKey;
				//password.setText(pass);
				
				
			}else{
				Toast.makeText(MainActivity.this, "wifi开启失败，请手动开启", Toast.LENGTH_LONG).show();
				return;
			}
		}
		WifiInfo wifiInfo=wifiAdmin.GetWifiInfo();//获取链接的wifi信息
		name.setText(wifiInfo.getSSID());
		
		wifiAdmin.startScan();
		wifiList=wifiAdmin.getWifiList();
		adapter=new WifiListAdapter(wifiList);
		list.setAdapter(adapter);
		
	}
	
	private Handler cHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==0x111)
			{
				Log.e("开启执行", "开启执行");
				String na=name.getText().toString();
				String pa=password.getText().toString();
				connectTargetWifi(na, pa);
			}			
		}
	};
	
	
	/**
	 * Description 注册WIFI状态改变监听广播
	 */
	private void registerWifiChangeBoradCast(){
		receiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// WIFI可用时连接到指定热点
				System.out.println( wifiAdmin.getmWifiManager().getWifiState());
				if (wifiAdmin.getmWifiManager().getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
					Log.i("TAG", "WIFI成功开启");					
					Message msg=new Message();
					msg.what=0x111;
					cHandler.sendMessageDelayed(msg, 5000);
					unregisterWifiChangeReceiver();
				}
			}
		};
		
		IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
		String broadcastPermission = permission.ACCESS_WIFI_STATE;
		registerReceiver(receiver, filter, broadcastPermission, new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				System.out.println("注册广播的msg"+msg.toString());
			}
		});
	}

	/**
	 * Description 注销WIFI状态改变监听广播 
	 */
	private void unregisterWifiChangeReceiver(){
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void connectTargetWifi(final String na, final String pa) {
		 
		 //builder.append("初始时的wifi信息"+wifiAdmin.GetWifiInfo()+"\n");
		 wifiAdmin.openWifi(); 
		 wifiAdmin.startScan();

		 WifiConfiguration cofg3=wifiAdmin.CreateWifiInfo(na, pa, 3);
		 WifiConfiguration cofg2=wifiAdmin.CreateWifiInfo(na, pa, 2);
		 WifiConfiguration cofg1=wifiAdmin.CreateWifiInfo(na, "", 1);
		 if(wifiAdmin.AddNetwork(cofg3)
				 ||wifiAdmin.AddNetwork(cofg2)
				    ||wifiAdmin.AddNetwork(cofg1))
		 {
			 //registerSetWifiChangedBoradCast();    
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					String str=wifiAdmin.GetWifiInfo().getSupplicantState().toString();
					if(str.equals("COMPLETED"))
					{
						//builder.append("链接成功后的wifi信息"+str+"\n");
						Toast.makeText(context, "链接成功", Toast.LENGTH_SHORT).show();
					}else
					{
						//builder.append("链接失败后的wifi信息"+str+"\n");
						connectTargetWifi(na,pa);
						//Toast.makeText(context, "链接失败", Toast.LENGTH_SHORT).show();
					}
					//showInfo.setText(builder.toString());					
				}
			}, 3000);
		 }else
		 {
			 //builder.append("链接出错！请手动链接");
			 Toast.makeText(context, "链接出错！请手动链接", Toast.LENGTH_SHORT).show();
			 //showInfo.setText(builder.toString());
		 }
		
	}
	
	public class WifiListAdapter extends BaseAdapter {

		private LayoutInflater inflater;//得到一个LayoutInfalter对象用来导入布局 
		private List<ScanResult> dataList;

		/**
		 * 构造函数
		 * @param context
		 * @param i 
		 */
		public WifiListAdapter(List<ScanResult> list)
		{
			inflater=LayoutInflater.from(context);
			dataList=list;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;        
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.community_political_item,null);	
				holder = new ViewHolder();

				/**得到各个控件的对象*/                    
				holder.title = (TextView) convertView.findViewById(R.id.itemTitle);

				convertView.setTag(holder);//绑定ViewHolder对象                   
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                  
			}

			/**设置TextView显示的内容 和Title点击事件*/ 
				final ScanResult news=dataList.get(position);
				holder.title.setText(news.SSID);
				holder.title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						name.setText(news.SSID);
					}
				});

			return convertView;
		}

		/**
		 * 存放控件
		 * */
		public final class ViewHolder{
			public TextView title;   
		}
	}

	
}


