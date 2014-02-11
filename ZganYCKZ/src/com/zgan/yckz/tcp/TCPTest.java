package com.zgan.yckz.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.Selector;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zgan.yckz.R;
import com.zgan.yckz.Welcome;
import com.zgan.yckz.activity.Alarm_Activity;
import com.zgan.yckz.activity.Main_Acitivty;
import com.zgan.yckz.activity.User_Login;
import com.zgan.yckz.tools.SheBei;
import com.zgan.yckz.tools.YCKZ_RetriveArticleService;
import com.zgan.yckz.tools.YCKZ_SQLHelper;
import com.zgan.yckz.tools.YCKZ_Static;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

public class TCPTest extends Service {
	String a1 = null;
	SharedPreferences preferences;
	Editor editor;
	List<String> list_id;
	List<String> list_details;
	List<SheBei> list_shebei;
	/**
	 * mac地址
	 */
	String mac = null;
	SheBei sheBei = new SheBei();
	YCKZ_SQLHelper yckz_SQLHelper;

	SQLiteDatabase db;

	public static boolean PushMsg_Alarm = true; // 告警推送
	public static boolean PushMsg_IS = false;

	@Override
	public void onCreate() {
		yckz_SQLHelper = new YCKZ_SQLHelper(this, "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Selector s1 = null;

				try {
					s1 = Selector.open();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 登陆服务器Ip:192.168.1.72 端口21000
				// 消息服务器ip:61.186.245.251端口21011(远程控制)201012(家庭卫士)

				TCPSend ts = new TCPSend("61.186.245.251", 21011, s1,
						FrameTools.Queue_Send, FrameTools.Queue_Receive);
				// TCPSend ts = new TCPSend("192.168.1.72", 21000, s1,
				// FrameTools.Queue_Send, FrameTools.Queue_Receive);
				Thread t1 = new Thread(ts);

				t1.start();

				testSendData test = new testSendData();

				testGetData testg = new testGetData();

			}
		}).start();

	}

	class testGetData implements Runnable {
		public testGetData() {
			new Thread(this).start();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				if (FrameTools.Queue_Receive.size() > 0) {
					byte[] resultByte = null;

					resultByte = FrameTools.Queue_Receive.poll();

					Frame f = new Frame(resultByte);

					Log.i("TCPClient", "接收数据...");
					Log.i("TCPClient",
							"接收数据...平台代码" + Integer.toString(f.Platform));
					Log.i("TCPClient",
							"接收数据...版本号" + Integer.toString(f.Version));
					Log.i("TCPClient",
							"接收数据...主功能命令字" + Byte.toString(f.MainCmd));
					Log.i("TCPClient",
							"接收数据...子功能命令字" + Integer.toString(f.SubCmd));
					Log.i("TCPClient", "接收数据...数据" + f.strData);
					// 拆分数据

					if (f.Platform == 1 && f.strData.length() > 0) {

						if (!"1".equals(f.strData.substring(0, 1))
								&& f.strData.substring(0, 1) != "1") {

							String str = f.strData.substring(1);
							Log.i("(f.strData).substring(2)", "" + str);
							String Port[] = str.split("-");
							if (Port.length > 1) {

								for (int i = 0; i < Port.length; i++) {

									Log.i("Port[" + i + "]", "" + Port[i]);
									String[] data = Port[i].split(":");
									for (int j = 0; j < data.length; j++) {
										Log.i("data[" + j + "]", "" + data[j]);
									}

								}

							}

						}

					}

					if (f.Platform == 9) {
						if (f.MainCmd == 2 && f.SubCmd == 3) {
							Log.i("=====", "报警");
							showNotification(f.strData);
							SendReturn();
						}
						if (f.MainCmd == 1 && f.SubCmd == 0) {
							Log.i("f.strData", "" + f.strData);
							Intent intent = new Intent();
							intent.putExtra("strData", f.strData);
							intent.setAction("com.zgan.yckz");
							sendBroadcast(intent);
						}
						YCKZ_Static.ChildDeviceID = f.strData.split("\t");
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
							if (list_details.size() == 9
									&& list_details.size() > 1) {
								sheBei.setSubDevid(list_details.get(0));
								sheBei.setMAC(list_details.get(1));
								sheBei.setPort(list_details.get(2));
								sheBei.setProductNo(list_details.get(3));
								sheBei.setDeviceNo(list_details.get(4));
								sheBei.setDeviceName(list_details.get(5));
								sheBei.setStatus(list_details.get(6));
								sheBei.setRegTime(list_details.get(7));
								sheBei.setJobStatus(list_details.get(8));
								Log.i("list_details.get(0)",
										"" + list_details.get(0));
								Log.i("list_details.get(1)",
										"" + list_details.get(1));
								Log.i("list_details.get(2)",
										"" + list_details.get(2));
								Log.i("list_details.get(3)",
										"" + list_details.get(3));
								Log.i("list_details.get(4)",
										"" + list_details.get(4));
								Log.i("list_details.get(5)",
										"" + list_details.get(5));
								Log.i("list_details.get(6)",
										"" + list_details.get(6));
								Log.i("list_details.get(7)",
										"" + list_details.get(7));
								Log.i("list_details.get(8)",
										"" + sheBei.getJobStatus());
								list_shebei.add(sheBei);

								Cursor c = db
										.rawQuery(
												"select *from table_SubDev where _MAC=?",
												new String[] { list_details
														.get(1) });
								mac=list_details.get(1);
								Log.i("888888888888", "888888888");
								if (c.moveToNext()) {
									Log.i("更新新数据", "更新新数据");

									UpdateSQL(db, list_details.get(0),
											list_details.get(1),
											list_details.get(2),
											list_details.get(3),
											list_details.get(4),
											list_details.get(5),
											list_details.get(6),
											list_details.get(7),
											list_details.get(8));
								} else {
									Log.i("插入新数据", "插入新数据");

									InSertSQl(db, list_details.get(0),
											list_details.get(1),
											list_details.get(2),
											list_details.get(3),
											list_details.get(4),
											list_details.get(5),
											list_details.get(6),
											list_details.get(7),
											list_details.get(8));
								}

								c.close();
							}
							Log.i("list_details.size()",
									"" + list_details.size());

						}
						Log.i("list_id.size()", "" + list_id.size());
					}

				}
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private void showNotification(String strData) {
		// TODO Auto-generated method stub
		Log.i("=====", "报1100警");

		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification();

		notification.icon = R.drawable.ic_launcher;

		notification.tickerText = "注意，有报警消息!";

		Intent intent = new Intent(this, Alarm_Activity.class);
		intent.putExtra("shebeiname", "" + strData);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String time = df.format(new Date());
		intent.putExtra("baojingtime", "" + time);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		// 点击状态栏的图标出现的提示信息设置
		notification.setLatestEventInfo(this, "内容提示：", "我就是一个测试文件",
				pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失

		manager.notify(1, notification);

	}

	public boolean chekifnumber(String in) {
		// TODO Auto-generated method stub
		try {

			Integer.parseInt(in);

		} catch (NumberFormatException ex) {
			return false;
		}

		return true;
	}

	public void SendReturn() {
		// TODO Auto-generated method stub
		Log.i("返回报警", "返回报警");
		Frame f = new Frame();

		f.Platform = 11;
		f.MainCmd = 2;
		f.SubCmd = 3;
		f.strData = "0";
		FrameTools.toSendTcpData(f);
	}

	public void UpdateSQL(SQLiteDatabase db, String _SubDev, String _MAC,
			String _Port, String _ProductNo, String _DeviceNo,
			String _DeviceName, String _Status, String _RegTime,
			String _JobStatus) {
		Log.i("数据库操作", "正在插入数据");

		ContentValues cv = new ContentValues();
		cv.put("_SubDvId", _SubDev);
		cv.put("_Port", _Port);
		cv.put("_ProductNo", _ProductNo);
		cv.put("_DeviceNo", _DeviceNo);
		cv.put("_DeviceName", _DeviceName);
		cv.put("_Status", _Status);
		cv.put("_RegTime", _RegTime);
		cv.put("_JobStatus", _JobStatus);
		String[] args = { String.valueOf(_MAC) };
		db.update("table_SubDev", cv, "_MAC=?", args);
		Log.i("数据库操作", "更新数据完成");

		// TODO Auto-generated method stub

	}

	public void InSertSQl(SQLiteDatabase db, String _SubDev, String _MAC,
			String _Port, String _ProductNo, String _DeviceNo,
			String _DeviceName, String _Status, String _RegTime,
			String _JobStatus) {
		// TODO Auto-generated method stub
		Log.i("数据库操作", "正在插入数据");

		db.execSQL("insert into table_SubDev values(null,?,?,?,?,?,?,?,?,?)",
				new String[] { _SubDev, _MAC, _Port, _ProductNo, _DeviceNo,
						_DeviceName, _Status, _RegTime, _JobStatus });
		Log.i("数据库操作", "插入数据完成");

	}

	/**
	 * 登陆服务器
	 * */
	public void Login() {
		// TODO Auto-generated method stub
		Log.i("获取入网设备信息", "获取入网设备信息");
		Frame f = new Frame();
		f.Platform = 11;
		f.MainCmd = 1;
		f.SubCmd = 1;
		f.strData = "18716341029" + "\t" + "123";
		FrameTools.toSendTcpData(f);
	}

	/**
	 * 与服务器链接
	 */
	public void Regin() {
		Log.i("获取入网设备信息", "获取入网设备信息");
		Frame f = new Frame();
		// 家庭卫士主14子 21
		// 远程控制平台代码4主3子0
		// 登陆服务器平台代码3主1子1.密码userid\t密码
		f.Platform = 11;
		f.MainCmd = 3;
		f.SubCmd = 0;
		f.strData = "55555555";
		FrameTools.toSendTcpData(f);
	}

	public void Landing() {
		Log.i("获取入网设备信息", "获取入网设备信息");
		Frame f = new Frame();
		// 家庭卫士主14子 21
		// 远程控制平台代码4主3子0
		// 登陆服务器平台代码3主1子1.密码userid\t密码
		f.Platform = 11;
		f.MainCmd = 0x01;
		f.SubCmd = 1;
		f.strData = "1111111111111111" + "\t" + "66666666";
		FrameTools.toSendTcpData(f);
	}

	/**
	 * 获取入网设备信息
	 */
	public void getNetworkInfo() {
		// TODO Auto-generated method stub
		Log.i("获取入网设备信息", "获取入网设备信息");
		Frame f = new Frame();
		//Select();

		f.Platform = 11;
		f.MainCmd = 1;
		f.SubCmd = 2;
		f.strData = mac;
		FrameTools.toSendTcpData(f);
	}

	/**
	 * 获取离网设备信息
	 */
	public void getOutNetInfo() {
		Frame f = new Frame();
		Log.i("获取离网设备信息", "获取离网设备信息");
		Select();

		f.Platform = 11;
		f.MainCmd = 1;
		f.SubCmd = 3;
		f.strData = mac;
		FrameTools.toSendTcpData(f);
	}

	/**
	 * 获取子设备状态
	 */
	public void getChildDevice() {
		Log.i("获取子设备状态", "获取子设备状态");

		Frame f = new Frame();
		f.Platform = 11;
		f.MainCmd = 1;
		f.SubCmd = 4;
		f.strData = "1";
		FrameTools.toSendTcpData(f);
	}

	/**
	 * 灯光控制
	 */
	public void setSwitch() {
		Log.i("灯光控制", "灯光控制");

		Frame f = new Frame();

		Select();
		String statues = "0,0";
		f.Platform = 11;
		f.MainCmd = 1;
		f.SubCmd = 0;
		f.strData = mac + "\t" + statues;
		FrameTools.toSendTcpData(f);
	}

	

	private void Select() {
		// TODO Auto-generated method stub
		Cursor c = db.rawQuery("select *from table_SubDev where _DeviceName=?",
				new String[] { "2" });
		Log.i("subdevid", "2");
		if (c.moveToFirst()) {
			mac = c.getString(c.getColumnIndex("_MAC"));
			Log.i("mac", mac);

		}
		c.close();
	}

	class testSendData implements Runnable {
		public testSendData() {
			new Thread(this).start();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Regin();
			// Landing();
			getChildDevice();
			try {
				Thread.sleep(5000);
				getNetworkInfo();
				// getOutNetInfo();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		this.stopSelf();
		super.onDestroy();
	}
}
