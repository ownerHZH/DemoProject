package com.zgan.yckz.activity;

import java.io.File;

import com.zgan.yckz.R;
import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tools.YCKZ_Activity;
import com.zgan.yckz.tools.YCKZ_SQLHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class More_Acitivity extends Activity {
	Button back;
	TextView title;
	TextView textView;
	YCKZ_SQLHelper yckz_SQLHelper;
	Button app;
	Button yun;
	Button phone;

	SQLiteDatabase db;

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
					Intent intent = new Intent(More_Acitivity.this,
							Alarm_Activity.class);
					if (db != null) {
						Index_Activity.InSertAlarmSQl(db, Port[2]);

					} else {
						yckz_SQLHelper = new YCKZ_SQLHelper(
								More_Acitivity.this, "yckz.db3", 1);
						db = yckz_SQLHelper.getReadableDatabase();
						Index_Activity.InSertAlarmSQl(db, Port[2]);
					}
					PendingIntent pendingIntent = PendingIntent.getActivity(
							More_Acitivity.this, 0, intent,
							PendingIntent.FLAG_ONE_SHOT);
					notification.setLatestEventInfo(More_Acitivity.this,
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
		setContentView(R.layout.more_line);

		yckz_SQLHelper = new YCKZ_SQLHelper(this, "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();

		title = (TextView) findViewById(R.id.title);
		back = (Button) findViewById(R.id.back);
		app = (Button) findViewById(R.id.app);
		yun = (Button) findViewById(R.id.yun);
		phone = (Button) findViewById(R.id.phone);

		title.setText("更多应用");
		back.setOnClickListener(listener);
		app.setOnClickListener(listener);
		yun.setOnClickListener(listener);
		phone.setOnClickListener(listener);

	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			case R.id.app:
				Intent intent = new Intent(More_Acitivity.this,
						AppActivtiy.class);
				startActivity(intent);

				break;
			case R.id.yun:
				intent = new Intent(More_Acitivity.this, AppActivtiy.class);
				
				startActivity(intent);
				break;
			case R.id.phone:
				intent = new Intent(More_Acitivity.this, PhoneFile_Activity.class);
				intent.putExtra("explorer_title",
						getString(R.string.dialog_read_from_dir));
				intent.setDataAndType(Uri.fromFile(new File("/sdcard")), "*/*");
				startActivity(intent);
				break;

			}

		}
	};
}
