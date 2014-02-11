package com.zgan.yckz.activity;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import android.widget.TextView;

import com.zgan.yckz.R;

import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tcp.FrameTools;


import com.zgan.yckz.tools.SheBei;
import com.zgan.yckz.tools.YCKZ_Activity;
import com.zgan.yckz.tools.YCKZ_NetworkDetector;
import com.zgan.yckz.tools.YCKZ_SQLHelper;
import com.zgan.yckz.tools.YCKZ_Static;
/**
 * 主界面
 * 
 * @author Administrator
 * 
 */
public class Main_Acitivty extends YCKZ_Activity {
	
	 // 主界面按钮
	 
	/**
	 * @param vip
	 *            :VIP积分
	 */
	TextView vip_btn;
	/**
	 * @param more
	 *            :更多应用
	 */
	Button more_btn;

	/**
	 * @param alarm
	 *            :报警记录
	 */
	TextView alarm_btn;
	/**
	 * @param swicth
	 *            :开关详情
	 */
	TextView swicth_btn;
	/**
	 * @param user_info
	 *            ： 用户信息
	 */
	TextView user_info;
	/**
	 * @param shebei_set
	 */
	TextView shebei_set;
	ImageView deng_btn;
	/**
	 * 开关
	 */
	RadioButton keting_btn;
	RadioButton zhuwo_btn;
	RadioButton ciwo_btn;
	RadioButton chazuo_btn;
	RadioButton chufang_btn;
	/**
	 * 设备开关状态0：关 1：开
	 */
	public static int keting_statues=0;
	public static int zhuwo_statues = 0;
	public static int chazuo_statues = 0;
	public static int chufang_statues = 0;
	public static int ciwo_statues = 0;
	
	List<String> list_id;
	List<String> list_details;
	List<SheBei> list_shebei;
	
	  Socket socket; private static final String TAG = "Socket_Android";
	 
	Intent intent2 = null;

	YCKZ_SQLHelper yckz_SQLHelper;

	SQLiteDatabase db;

	String keting_sta = null;

	String mac = null;

	SharedPreferences preferences;
	String name;
	String pas;

	private String progress;
	private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
	private ProgressBar progressBar;
	Handler SubDatahandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// super.handleMessage(msg);
			switch (msg.what) {
			case Constant.DATAERROR:
				System.out.println("NewSocketInfo  Constant.DATAERROR  "
						+ msg.obj);
				break;
			case Constant.CHECKCODEERROR:
				System.out.println("NewSocketInfo  Constant.CHECKCODEERROR "
						+ msg.obj);
				break;
			case Constant.NETERROR:
				System.out.println("NewSocketInfo  Constant.NETERROR  "
						+ msg.obj);
				break;
			case Constant.SERVERERROR:
				System.out.println("NewSocketInfo  Constant.SERVERERROR "
						+ msg.obj);
				break;
			case Constant.DATASUCCESS:
				byte[] buffer;
				// buffer = new byte[Constant.MSG_NUM];
				// Arrays.fill(buffer, (byte)0);
				buffer = (byte[]) msg.obj;
				Frame frame = new Frame(buffer);

				System.out.println("接收数据...平台代码" + frame.Platform);
				System.out.println("接收数据...版本号" + frame.Version);
				System.out.println("接收数据...主功能命令字" + frame.MainCmd);
				System.out.println("接收数据...子功能命令字" + frame.SubCmd);
				System.out.println("接收数据...数据" + frame.strData);
				System.out.println("NewSocketInfo  Constant.DATASUCCESS "
						+ buffer);
				if(frame.MainCmd==2 && frame.SubCmd==3){
					
				}
				if (frame.MainCmd == 1 && frame.SubCmd == 0
						&& "0".equals(frame.strData)) {
					
					if (keting_statues != 1) {
						deng_btn.setImageDrawable(getResources().getDrawable(
								R.drawable.deng_true));

					} else {
						deng_btn.setImageDrawable(getResources().getDrawable(
								R.drawable.deng_flase));
					}

				}

				if (frame.MainCmd == 3 && frame.SubCmd == 0
						&& "0".equals(frame.strData)) {
					GetChildList();
				}
				if (frame.Platform == 9) {
					if (frame.MainCmd == 2 && frame.SubCmd == 3) {
						Log.i("=====", "报警");
						// showNotification(frame.strData);
						// SendReturn();
					}
					if (frame.MainCmd == 1 && frame.SubCmd == 0) {
						Log.i("f.strData", "" + frame.strData);
						Intent intent = new Intent();
						intent.putExtra("strData", frame.strData);
						intent.setAction("com.zgan.yckz");
						sendBroadcast(intent);
					}
					if (frame.MainCmd == 1 && frame.SubCmd == 4) {

						YCKZ_Static.ChildDeviceID = frame.strData.split("\t");
						list_id = new ArrayList<String>();
						list_shebei = new ArrayList<SheBei>();
						list_details = new ArrayList<String>();

						/*
						 * if (!"0".equals(f.strData) && f.strData!="0") {
						 * Log.i("", ""); } else {
						 */
						for (int i = 0; i < YCKZ_Static.ChildDeviceID.length; i++) {
							String userinfo = YCKZ_Static.ChildDeviceID[i];
							Log.i("设备ID" + i, "" + userinfo);
							String[] user = userinfo.split(",");
							list_id.add(userinfo);
							list_details = new ArrayList<String>();

							for (int j = 0; j < user.length; j++) {
								String SubDev = user[j];
								Log.i("设备信息" + j, "" + SubDev);
								list_details.add("" + SubDev);

							}
							if (i == 0) {

								YCKZ_Static.ZHUWO_DENGMAC1 = list_details
										.get(1);
								YCKZ_Static.ZHUWO_DENGJOBSTAUTES1 = list_details
										.get(8);
								keting_statues=Integer.parseInt(YCKZ_Static.ZHUWO_DENGJOBSTAUTES1.substring(0,1));
								Log.i("YCKZ_Static.ZHUWO_DENGMAC1",
										YCKZ_Static.ZHUWO_DENGMAC1);
								Log.i("	YCKZ_Static.ZHUWO_DENGJOBSTAUTES1",
										YCKZ_Static.ZHUWO_DENGJOBSTAUTES1);

							}
							if (i == 1) {

								YCKZ_Static.ZHUWO_DENGMAC2 = list_details
										.get(1);
								YCKZ_Static.ZHUWO_DENGJOBSTAUTES2 = list_details
										.get(8);
								Log.i("YCKZ_Static.ZHUWO_DENGMAC2",
										YCKZ_Static.ZHUWO_DENGMAC2);
								Log.i("	YCKZ_Static.ZHUWO_DENGJOBSTAUTES2",
										YCKZ_Static.ZHUWO_DENGJOBSTAUTES2);

							}
							if (i == 2) {

								YCKZ_Static.ZHUWO_DENGMAC3 = list_details
										.get(1);
								YCKZ_Static.ZHUWO_DENGJOBSTAUTES3 = list_details
										.get(8);
								Log.i("YCKZ_Static.ZHUWO_DENGMAC3",
										YCKZ_Static.ZHUWO_DENGMAC3);
								Log.i("	YCKZ_Static.ZHUWO_DENGJOBSTAUTES3",
										YCKZ_Static.ZHUWO_DENGJOBSTAUTES3);

							}
							/*
							 * if (list_details.size() == 9 &&
							 * list_details.size() > 1) {
							 * sheBei.setSubDevid(list_details.get(0));
							 * sheBei.setMAC(list_details.get(1));
							 * sheBei.setPort(list_details.get(2));
							 * sheBei.setProductNo(list_details.get(3));
							 * sheBei.setDeviceNo(list_details.get(4));
							 * sheBei.setDeviceName(list_details.get(5));
							 * sheBei.setStatus(list_details.get(6));
							 * sheBei.setRegTime(list_details.get(7));
							 * 
							 * sheBei.setJobStatus(list_details.get(8));
							 * Log.i("list_details.get(0)", "" +
							 * list_details.get(0));
							 * Log.i("list_details.get(1)", "" +
							 * list_details.get(1));
							 * Log.i("list_details.get(2)", "" +
							 * list_details.get(2));
							 * Log.i("list_details.get(3)", "" +
							 * list_details.get(3));
							 * Log.i("list_details.get(4)", "" +
							 * list_details.get(4));
							 * Log.i("list_details.get(5)", "" +
							 * list_details.get(5));
							 * Log.i("list_details.get(6)", "" +
							 * list_details.get(6));
							 * Log.i("list_details.get(7)", "" +
							 * list_details.get(7));
							 * Log.i("list_details.get(8)", "" +
							 * sheBei.getJobStatus()); list_shebei.add(sheBei);
							 * 
							 * }
							 */
							Log.i("list_details.size()",
									"" + list_details.size());

						}
						Log.i("list_id.size()", "" + list_id.size());
					}
				}

				break;
			}
		}
	};

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_line);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			}
		if (Integer.parseInt(VERSION.SDK) > 14
				|| Integer.parseInt(VERSION.SDK) == 14) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() // or
																			// .detectAll()
																			// for
																			// all
																			// detectable
																			// problems
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}
		User_Login.setHnadler(SubDatahandler);

		try {
			byte[] logininfo;

			String strLoginInfo = "89898989";
			logininfo = strLoginInfo.getBytes();
			User_Login.SendData(9, logininfo, strLoginInfo.length(),
					Constant.INDEX_cbMainCmdID, /* (byte)0x21 */
					Constant.INDEX_cbSubCmdID, Constant.INDEX_cbMessageVer);

			Thread.sleep(100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("NewSocketInfo" + "e.printStackTrace() "
					+ e.getMessage());
		}
		
		yckz_SQLHelper = new YCKZ_SQLHelper(this, "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();

		preferences = getSharedPreferences("yckz_user", MODE_PRIVATE);
		// 初始化 主界面按钮
		

		vip_btn = (TextView) findViewById(R.id.vip);

		alarm_btn = (TextView) findViewById(R.id.alarm_detalis);
		swicth_btn = (TextView) findViewById(R.id.switch_detalis);
		user_info = (TextView) findViewById(R.id.user_info);
		shebei_set = (TextView) findViewById(R.id.shebei_set);
		deng_btn = (ImageView) findViewById(R.id.deng_btn);

		keting_btn = (RadioButton) findViewById(R.id.keting_btn);
		zhuwo_btn = (RadioButton) findViewById(R.id.zhuwo_btn);
		ciwo_btn = (RadioButton) findViewById(R.id.ciwo_btn);
		chazuo_btn = (RadioButton) findViewById(R.id.chazuo_btn);
		chufang_btn = (RadioButton) findViewById(R.id.chufang_btn);

		vip_btn.setOnClickListener(listener);
		user_info.setOnClickListener(listener);
		alarm_btn.setOnClickListener(listener);
		swicth_btn.setOnClickListener(listener);
		shebei_set.setOnClickListener(listener);

		keting_btn.setOnClickListener(listener);
		zhuwo_btn.setOnClickListener(listener);
		ciwo_btn.setOnClickListener(listener);
		chazuo_btn.setOnClickListener(listener);
		chufang_btn.setOnClickListener(listener);
		deng_btn.setOnClickListener(listener);
		//StartTCPServer();
		//show();

	}
	protected void GetChildList() {
		// TODO Auto-generated method stub

		try {

			User_Login.SendData(9, null, 0, Constant.List_cbMainCmdID,
					Constant.List_cbSubCmdID, Constant.INDEX_cbMessageVer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("NewSocketInfo" + "e.printStackTrace() "
					+ e.getMessage());
		}
	}
	private void show() {
		// TODO Auto-generated method stub
		Select();

	}

	private void Select() {
		// TODO Auto-generated method stub
		
		
	}

	/*private void StartTCPServer() {
		// TODO Auto-generated method stub
		Boolean networkState = YCKZ_NetworkDetector.detect(Main_Acitivty.this);
		if (!networkState) {
			new AlertDialog.Builder(this)
					.setTitle("网络错误")
					.setMessage("网络连接失败，请确认网络连接")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub
									arg0.dismiss();
								}
							}).show();
		} else {
			
		}

	}*/

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.vip:
				Intent intent = new Intent(Main_Acitivty.this, Vip_Info.class);
				startActivity(intent);
				break;

			case R.id.more:
				intent = new Intent(Main_Acitivty.this, More_Acitivity.class);
				startActivity(intent);
				break;

			case R.id.alarm_detalis:
				intent = new Intent(Main_Acitivty.this, Alarm_Activity.class);
				startActivity(intent);

				break;
			case R.id.user_info:
				intent = new Intent(Main_Acitivty.this, UserInfo_Activity.class);
				startActivity(intent);
				break;
			case R.id.shebei_set:
				intent = new Intent(Main_Acitivty.this,
						SheBeiSet_Activity.class);
				startActivity(intent);

				break;
			case R.id.switch_detalis:
				intent = new Intent(Main_Acitivty.this, More_Acitivity.class);
				startActivity(intent);
				break;
			case R.id.zhuwo_btn:
				if (ciwo_btn.isChecked()) {
					ciwo_btn.setChecked(false);
				}
				if (chazuo_btn.isChecked()) {
					chazuo_btn.setChecked(false);
				}
				if (chufang_btn.isChecked()) {
					chufang_btn.setChecked(false);
				}
				if (keting_btn.isChecked()) {
					keting_btn.setChecked(false);
				}
				break;
			case R.id.ciwo_btn:
				if (zhuwo_btn.isChecked()) {
					zhuwo_btn.setChecked(false);
				}
				if (chazuo_btn.isChecked()) {
					chazuo_btn.setChecked(false);
				}
				if (chufang_btn.isChecked()) {
					chufang_btn.setChecked(false);
				}
				if (keting_btn.isChecked()) {
					keting_btn.setChecked(false);
				}
				break;
			case R.id.chufang_btn:
				if (ciwo_btn.isChecked()) {
					ciwo_btn.setChecked(false);
				}
				if (chazuo_btn.isChecked()) {
					chazuo_btn.setChecked(false);
				}
				if (zhuwo_btn.isChecked()) {
					zhuwo_btn.setChecked(false);
				}
				if (keting_btn.isChecked()) {
					keting_btn.setChecked(false);
				}
				break;
			case R.id.chazuo_btn:
				if (ciwo_btn.isChecked()) {
					ciwo_btn.setChecked(false);
				}
				if (chufang_btn.isChecked()) {
					chufang_btn.setChecked(false);
				}
				if (zhuwo_btn.isChecked()) {
					zhuwo_btn.setChecked(false);
				}
				if (keting_btn.isChecked()) {
					keting_btn.setChecked(false);
				}
				break;
			case R.id.keting_btn:
				//StartTCPServer();
				if (zhuwo_btn.isChecked()) {
					zhuwo_btn.setChecked(false);
				}
				if (chazuo_btn.isChecked()) {
					chazuo_btn.setChecked(false);
				}
				if (chufang_btn.isChecked()) {
					chufang_btn.setChecked(false);
				}
				if (ciwo_btn.isChecked()) {
					ciwo_btn.setChecked(false);
				}
				if (keting_statues != 1) {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_true));

				} else {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_flase));
				}

				break;
			case R.id.deng_btn:
				if (keting_btn.isChecked()) {
					name = preferences.getString("user_name", null);
					pas = preferences.getString("user_pas", null);
					/*if (name != null && !"".equals(name) && pas != null
							&& !"".equals(pas)) {*/
						
							setSwitch();

					/*} else {
						Toast.makeText(Main_Acitivty.this, "你尚未登陆,请退出程序后重新登陆",
								Toast.LENGTH_SHORT).show();

					}*/
				}
				break;
			}
		}
	};

	protected void setSwitch() {
		// TODO Auto-generated method stub
		/**
		 * 灯光控制
		 */
		
		try {
			byte[] logininfo;
			if (keting_statues == 1) {
				keting_statues = 0;
			} else {
				keting_statues = 1;
			}
			Log.i("zhuwostautes", "" + keting_statues);
			String strLoginInfo = YCKZ_Static.ZHUWO_DENGMAC1 + "\t" + keting_statues + "," + keting_statues;
			logininfo = strLoginInfo.getBytes();
			User_Login.SendData(9, logininfo, strLoginInfo.length(),
					Constant.List_cbMainCmdID, (byte) 0x00,
					Constant.INDEX_cbMessageVer);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("NewSocketInfo" + "e.printStackTrace() "
					+ e.getMessage());
		}
	}

	
		

	// 定义一个广播接收器
	class MyBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 接收到Service发送的广播信息，得到数据，更新UI
			progress = intent.getStringExtra("strData");
			Log.i("progress.length()", "" + progress.length());

			Log.i("AAAAAAAAAAAAAAAAAAAA", "" + progress);
			if (keting_statues != 1) {
				if (!progress.equals("0")) {
					Toast.makeText(Main_Acitivty.this, "操作失败。",
							Toast.LENGTH_SHORT).show();
					keting_statues = 1;

				} else {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_true));

				}
			} else {

				if (!progress.equals("0")) {
					Toast.makeText(Main_Acitivty.this, "操作失败。",
							Toast.LENGTH_SHORT).show();
					keting_statues = 0;
				} else {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_flase));
				}
			}

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 注册广播接收器
		IntentFilter filter = new IntentFilter();
		// 设置接收广播的类型，这里要和Service里设置的类型匹配，还可以在AndroidManifest.xml文件中注册
		filter.addAction("com.zgan.yckz");
		this.registerReceiver(myBroadcastReceiver, filter);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (intent2 != null) {

			this.stopService(intent2);
		}

		super.onDestroy();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.unregisterReceiver(myBroadcastReceiver);
	}
}
