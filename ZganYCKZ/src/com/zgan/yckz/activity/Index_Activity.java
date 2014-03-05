package com.zgan.yckz.activity;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;

import com.zgan.yckz.R;
import com.zgan.yckz.fragment.ChazuoFramgement;
import com.zgan.yckz.fragment.ChufangFramgement;
import com.zgan.yckz.fragment.CiwoFramgment;
import com.zgan.yckz.fragment.KetingFramgment;
import com.zgan.yckz.fragment.PublicFragment;
import com.zgan.yckz.fragment.ZhuwoFramgement;
import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tools.SheBei;
import com.zgan.yckz.tools.YCKZ_Activity;
import com.zgan.yckz.tools.YCKZ_SQLHelper;
import com.zgan.yckz.tools.YCKZ_Static;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("NewApi")
public class Index_Activity extends FragmentActivity {

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

	List<SheBei> list_shebei = new ArrayList<SheBei>();

	ViewPager viewPager = null;

	RadioGroup radioGroup = null;
	// 客厅——按钮
	RadioButton btn_keting;
	// 主卧——按钮
	RadioButton btn_zhuwo;
	// 次卧——按钮
	RadioButton btn_ciwo;
	// 插座——按钮
	RadioButton btn_chazuo;
	// 厨房——按钮
	RadioButton btn_chufang;

	YCKZ_SQLHelper yckz_SQLHelper;

	SQLiteDatabase db;

	// 初始化5个fragment
	/*ZhuwoFramgement zhuwoFramgement = new ZhuwoFramgement();
	CiwoFramgment ciwoFramgment = new CiwoFramgment();
	ChazuoFramgement chazuoFramgement = new ChazuoFramgement();
	KetingFramgment ketingFramgment = new KetingFramgment();
	ChufangFramgement chufangFramgement = new ChufangFramgement();*/
	PublicFragment publicFragment;

	public int tag = 0;
	List<Fragment> list;

	String mac = null;

	String subname = null;

	SheBei sheBei;

	public static String MAC = null;

	public static String alarmstr = null;
	
	int number=0;
	/**
	 * 获取系统通知服务
	 */
	NotificationManager manager;
	Handler SubDatahandler = new Handler() {
		@SuppressLint("NewApi")
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
				//Toast.makeText(Index_Activity.this, "网络已断开或不可用！", Toast.LENGTH_LONG).show();
				break;
			case Constant.DATASUCCESS:
				Log.i("index", "1");
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

				if (frame.MainCmd == 13 && frame.SubCmd == 3
						&& frame.strData != null) {
					String Port[] = frame.strData.split("\t");
					alarmstr = Port[1];
					SendReturn();
					NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification();
					notification.icon = R.drawable.ic_launcher;
					notification.defaults=Notification.DEFAULT_SOUND;
					Intent intent = new Intent(Index_Activity.this,
							Alarm_Activity.class);
					if (db!=null) {
						InSertAlarmSQl(db, Port[2]);
					}else{
						yckz_SQLHelper = new YCKZ_SQLHelper(Index_Activity.this, "yckz.db3", 1);
						db = yckz_SQLHelper.getReadableDatabase();
						InSertAlarmSQl(db, Port[2]);
					}
					

					PendingIntent pendingIntent = PendingIntent.getActivity(
							Index_Activity.this, 0, intent,
							PendingIntent.FLAG_ONE_SHOT);
					notification.setLatestEventInfo(Index_Activity.this,
							"报警信息：", "有一条报警消息", pendingIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
				

					manager.notify(number++, notification);

				}
				if (frame.MainCmd == 2 && frame.SubCmd == 2) {
					String Port[] = frame.strData.split(",");
					PublicFragment f = (PublicFragment) list.get(tag);
					Cursor c = null;
					try {
						if (Port[0].equals(f.MAC1)) {
							if (f.flag == 1) {
								if (Port[1].length() > 1) {
									f.stautes1 = Integer.parseInt(Port[1]
											.substring(1, 2));
									f.Showview();
								} else {
									f.stautes1 = Integer.parseInt(Port[1]);
									f.Showview();
								}
							} else if (f.flag == 2) {
								f.stautes1 = Integer.parseInt(Port[1].substring(0,
										1));
								f.stautes2 = Integer.parseInt(Port[1].substring(2,
										3));
								f.Showview();
							}
						}
						c = db.rawQuery(
								"select *from table_SubDev where _MAC=?",
								new String[] { Port[0] });

						if(c.moveToFirst())
						{
							if (c.getCount() == 2) {

								if (c.getString(c.getColumnIndex("_Port")).equals("92")) {
									String staues = Port[1].substring(0, 1) + "000";
									UpdateSQL1(db, Port[0],
											c.getString(c.getColumnIndex("_Port")),
											staues);
								} else if (c.getString(c.getColumnIndex("_Port"))
										.equals("71")) {
									String staues = "00" + Port[1].substring(2, 3)
											+ "0";
									UpdateSQL1(db, Port[0],
											c.getString(c.getColumnIndex("_Port")),
											staues);
								}
								c.moveToNext();
								if (c.getString(c.getColumnIndex("_Port")).equals("92")) {
									String staues = Port[1].substring(0, 1) + "000";
									UpdateSQL1(db, Port[0],
											c.getString(c.getColumnIndex("_Port")),
											staues);
								} else if (c.getString(c.getColumnIndex("_Port"))
										.equals("71")) {
									String staues = "00" + Port[1].substring(2, 3)
											+ "0";
									UpdateSQL1(db, Port[0],
											c.getString(c.getColumnIndex("_Port")),
											staues);
								}

							} else {
								String staues = "0" + Port[1].substring(1, 2) + "00";
								UpdateSQL1(db, Port[0],
										c.getString(c.getColumnIndex("_Port")), staues);
							}
						}
					}catch (ArrayIndexOutOfBoundsException e) {
						e.printStackTrace();
					}catch (Exception e) {
						e.printStackTrace();
					}finally
					{
						c.close();
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		setContentView(R.layout.index_line);
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
		try {
			User_Login.setHnadler(SubDatahandler);
		} catch (NullPointerException e) {			
			Intent intent=new Intent(this,User_Login.class);
			intent.putExtra("restart", "restart");
			startActivity(intent);
			finish();
			//User_Login.setHnadler(SubDatahandler,this);
			e.printStackTrace();
		}
		yckz_SQLHelper = new YCKZ_SQLHelper(this, "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();

		manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		vip_btn = (TextView) findViewById(R.id.vip);

		alarm_btn = (TextView) findViewById(R.id.alarm_detalis);
		swicth_btn = (TextView) findViewById(R.id.switch_detalis);
		user_info = (TextView) findViewById(R.id.user_info);
		shebei_set = (TextView) findViewById(R.id.shebei_set);

		// radioGroup = (RadioGroup) findViewById(R.id.radio_group);
		btn_keting = (RadioButton) findViewById(R.id.keting_btn);
		btn_zhuwo = (RadioButton) findViewById(R.id.zhuwo_btn);
		btn_ciwo = (RadioButton) findViewById(R.id.ciwo_btn);
		btn_chufang = (RadioButton) findViewById(R.id.chufang_btn);
		btn_chazuo = (RadioButton) findViewById(R.id.chazuo_btn);

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		// 将5个fragement添加到list中
		list = new ArrayList<Fragment>();
		Select();		
		// 加载5个fragment到viewpager中

		// 对radioGroup和viewPager做监听事件
		// radioGroup.setOnCheckedChangeListener(changelistener);
		viewPager.setOnPageChangeListener(pagelistener);

		user_info.setOnClickListener(listener);
		alarm_btn.setOnClickListener(listener);
		swicth_btn.setOnClickListener(listener);
		shebei_set.setOnClickListener(listener);

	}

	protected void UpdateSQL1(SQLiteDatabase db2, String _MAC, String _Port,
			String _JobStatus) {
		// TODO Auto-generated method stub
		Log.i("数据库操作", "正在更新数据");

		db.execSQL("update  table_SubDev set _JobStatus='" + _JobStatus
				+ "'  where _MAC='" + _MAC + "'and  _Port='" + _Port + "'");
		Log.i("数据库操作", "更新数据完成");
	}

	public static void InSertAlarmSQl(SQLiteDatabase db, String _AlarmTime) {
		// TODO Auto-generated method stub
		Log.i("数据库操作", "正在插入数据");

		db.execSQL("insert into AlarmDevList values(null,?)",
				new String[] { _AlarmTime, });
		Log.i("数据库操作", "插入数据完成");

	}

	protected static void SendReturn() {
		// TODO Auto-generated method stub
		String strLoginInfo = alarmstr + "\t" + 0;
		Log.i("strLoginInfo", strLoginInfo);
		byte[] logininfo = strLoginInfo.getBytes();
		User_Login.SendData(7, logininfo, strLoginInfo.length(),
				Constant.INDEX_bjMainCmdID, Constant.INDEX_cbMainCmdID,
				Constant.INDEX_cbMessageVer);
	}

	private void Select() {
		// TODO Auto-generated method stub

		Cursor c = db.rawQuery("SELECT COUNT(DISTINCT _MAC)FROM SubDevList",
				new String[] {});

		if (c.moveToFirst()) {
			int str = c.getInt(0);
			Log.i("str", "" + str);
			Cursor cus = db.rawQuery("SELECT * FROM SubDevList",
					new String[] {});
			if (cus.moveToFirst()) {
				for (int i = 0; i < str; i++) {

					mac = cus.getString(cus.getColumnIndex("_MAC"));
					subname = cus.getString(cus.getColumnIndex("_DeviceName"));

					sheBei = new SheBei();
					sheBei.setMAC(mac);
					sheBei.setDeviceName(subname);
					list_shebei.add(sheBei);

					publicFragment = new PublicFragment(list_shebei, i);
					list.add(publicFragment);

					cus.moveToNext();

				}

			}
			cus.close();
			viewPager.setAdapter(new MyFragmentAdapter(
					getSupportFragmentManager()));
			viewPager.getAdapter().notifyDataSetChanged();
			viewPager.setOffscreenPageLimit(str);
		}

		c.close();

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.vip:
				Intent intent = new Intent(Index_Activity.this, Vip_Info.class);
				startActivity(intent);
				break;

			case R.id.more:
				intent = new Intent(Index_Activity.this, More_Acitivity.class);
				startActivity(intent);
				break;

			case R.id.alarm_detalis:
				intent = new Intent(Index_Activity.this, Alarm_Activity.class);
				startActivity(intent);

				break;
			case R.id.user_info:
				intent = new Intent(Index_Activity.this,
						UserInfo_Activity.class);
				startActivity(intent);
				break;
			case R.id.shebei_set:
				intent = new Intent(Index_Activity.this,
						SheBeiSet_Activity.class);
				startActivity(intent);
				finish();

				break;
			case R.id.switch_detalis:
				intent = new Intent(Index_Activity.this, More_Acitivity.class);
				startActivity(intent);
				break;
			}
		}
	};

	/*
	 * 对pagerview做监听
	 */
	OnPageChangeListener pagelistener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			tag = position;
			PublicFragment fragment = (PublicFragment) list.get(position);
			try {
				fragment.Select();
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fragment.Showview();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

	};
	// radiongroup监听事件
	OnCheckedChangeListener changelistener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub

			viewPager.setCurrentItem(checkedId);
		}
	};

	class MyFragmentAdapter extends FragmentStatePagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {

			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (list != null) {
				return list.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object instantiateItem(View container, int position) {
			// TODO Auto-generated method stub

			return super.instantiateItem(container, position);
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (db != null) {
			db.close();
		}
		if (yckz_SQLHelper != null) {
			yckz_SQLHelper.close();
		}
		super.onDestroy();
	}

}
