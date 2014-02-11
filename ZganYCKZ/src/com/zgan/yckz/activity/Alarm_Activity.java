package com.zgan.yckz.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.zgan.yckz.R;
import com.zgan.yckz.adapter.AlarmAdapter;
import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tools.SheBei;
import com.zgan.yckz.tools.YCKZ_SQLHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 报警记录
 * 
 * @author Administrator
 * 
 */

public class Alarm_Activity extends Activity {
	/**
	 * 返回按钮
	 */
	Button back;
	/**
	 * 时间按钮
	 */
	Button baojing_time;
	TextView title;
	TextView textView;
	TextView size;
	/**
	 * 报警记录列表
	 */
	ListView listView;
	List<SheBei> list=new ArrayList<SheBei>();
	AlarmAdapter adapter;
	YCKZ_SQLHelper yckz_SQLHelper;

	SQLiteDatabase db;
	
	String device_name;
	String alarm_time;
	
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
					notification.defaults=Notification.DEFAULT_SOUND;

					Intent intent = new Intent(Alarm_Activity.this,
							Alarm_Activity.class);
					if (db!=null) {
						Index_Activity.InSertAlarmSQl(db, Port[2]);

					}else{
						yckz_SQLHelper = new YCKZ_SQLHelper(Alarm_Activity.this, "yckz.db3", 1);
						db = yckz_SQLHelper.getReadableDatabase();
						Index_Activity.InSertAlarmSQl(db, Port[2]);
					}

					PendingIntent pendingIntent = PendingIntent.getActivity(
							Alarm_Activity.this, 0, intent,
							PendingIntent.FLAG_ONE_SHOT);
					notification.setLatestEventInfo(Alarm_Activity.this,
							"报警信息：", "有一条报警消息", pendingIntent);
					notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失

					manager.notify(1, notification);

				}
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarm_line);
		yckz_SQLHelper = new YCKZ_SQLHelper(this, "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();
		
		title = (TextView) findViewById(R.id.title);
		size=(TextView)findViewById(R.id.cishu);
		back = (Button) findViewById(R.id.back);
		baojing_time = (Button) findViewById(R.id.baojing_time);
		listView=(ListView)findViewById(R.id.alarm_data_listview);
		
		
		show();
		
		title.setText("报警记录");
		size.setText(list.size()+"次");
		back.setOnClickListener(listener);
		baojing_time.setOnClickListener(listener);
		getNowTime();
		
	}

	private void show() {
		// TODO Auto-generated method stub
			Cursor c = db.rawQuery("select *from AlarmDevList",new String[]{});
			if (c.getCount()!=0) {
				while (c.moveToNext()) {
					
					
					alarm_time=c.getString(c.getColumnIndex("_AlarmTime"));
					Log.i("alarm_time", alarm_time);
					SheBei bei=new SheBei();
					
					bei.setAlarm_time(alarm_time);
					//bei.setAlarm_type(getIntent().getExtras().getString("shebeiname"));
					list.add(bei);
					}
					AlarmAdapter adapter=new AlarmAdapter(Alarm_Activity.this, list);
					listView.setAdapter(adapter);
					
				
			}		
			
			c.close();
		
	}

	/*
	 *  获取系统当前年月日
	 */
	private void getNowTime() {
		// TODO Auto-generated method stub
		Time time = new Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;
		int date = time.monthDay;
		baojing_time.setText(year + "/" + (month+1) + "/" + date);
	}
	/**
	 * 按钮的事件监听
	 */
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			case R.id.baojing_time:
				showTimeDialog();
				break;
			
			}

		}
	};

	protected void showTimeDialog() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		// 创建一个时间对话
		AlertDialog alertDialog = new DatePickerDialog(Alarm_Activity.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						baojing_time.setText(year + "/" + (monthOfYear+1) + "/"
								+ dayOfMonth);
					}
				}, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		alertDialog.show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (db!=null) {
			db.close();
		}
		super.onDestroy();
	}
}
