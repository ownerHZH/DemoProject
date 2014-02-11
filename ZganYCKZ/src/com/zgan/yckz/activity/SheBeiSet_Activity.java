package com.zgan.yckz.activity;

import java.nio.channels.SelectableChannel;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.zgan.yckz.R;
import com.zgan.yckz.adapter.KaiGuanAdapter;
import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tools.SheBei;
import com.zgan.yckz.tools.YCKZ_Activity;
import com.zgan.yckz.tools.YCKZ_SQLHelper;

public class SheBeiSet_Activity extends Activity {
	
	/**
	 * 
	 */
	Button back;
	ListView listView;
	TextView textView;
	SheBei data;

	YCKZ_SQLHelper yckz_SQLHelper;

	SQLiteDatabase db;

	String mac = null;

	String subname = null;

	List<SheBei> list = new ArrayList<SheBei>();

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
					Index_Activity.alarmstr = Port[1];
					Index_Activity.SendReturn();
					NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					Notification notification = new Notification();
					notification.icon = R.drawable.ic_launcher;
					notification.defaults = Notification.DEFAULT_SOUND;

					Intent intent = new Intent(SheBeiSet_Activity.this,
							Alarm_Activity.class);
					if (db != null) {
						Index_Activity.InSertAlarmSQl(db, Port[2]);

					} else {
						yckz_SQLHelper = new YCKZ_SQLHelper(
								SheBeiSet_Activity.this, "yckz.db3", 1);
						db = yckz_SQLHelper.getReadableDatabase();
						Index_Activity.InSertAlarmSQl(db, Port[2]);
					}
					PendingIntent pendingIntent = PendingIntent.getActivity(
							SheBeiSet_Activity.this, 0, intent,
							PendingIntent.FLAG_ONE_SHOT);
					notification.setLatestEventInfo(SheBeiSet_Activity.this,
							"报警信息：", "有一条报警消息", pendingIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失

					manager.notify(1, notification);

				}
			}
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (db != null) {
			db.close();
		}
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_line);

		yckz_SQLHelper = new YCKZ_SQLHelper(this, "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();

		User_Login.setHnadler(SubDatahandler);

		textView = (TextView) findViewById(R.id.title);
		back = (Button) findViewById(R.id.back);
		listView = (ListView) findViewById(R.id.listview);

		textView.setText("设备设置");
		back.setOnClickListener(listener);

		Select();
		KaiGuanAdapter adapter = new KaiGuanAdapter(this, list);
		listView.setAdapter(adapter);

	}

	private void Select() {
		// TODO Auto-generated method stub
		Cursor c = db.rawQuery("select *from SubDevList", new String[] {});

		while (c.moveToNext()) {
			mac = c.getString(c.getColumnIndex("_MAC"));
			subname = c.getString(c.getColumnIndex("_DeviceName"));
			Log.i("mac", mac);
			Log.i("subname", subname);
			SheBei sheBei = new SheBei();
			sheBei.setMAC(mac);
			sheBei.setDeviceName(subname);
			list.add(sheBei);
		}
		c.close();
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				Intent intent=new Intent(SheBeiSet_Activity.this,Index_Activity.class);
				startActivity(intent);
				finish();
				break;

			
				
			}
		}
	};

}
