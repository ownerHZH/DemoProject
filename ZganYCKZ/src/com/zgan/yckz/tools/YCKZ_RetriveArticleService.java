package com.zgan.yckz.tools;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.ArrayList;




import com.zgan.yckz.R;
import com.zgan.yckz.Welcome;
import com.zgan.yckz.tcp.FrameTools;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.WindowManager;

public class YCKZ_RetriveArticleService extends Service{
	public static boolean PushMsg_Alarm = true; // 告警推送
	private static int intMsgIndex = 0;
	public static boolean PushMsg_IS = false;

	
	private Intent intent = new Intent("com.ZgSafe.pages.RECEIVER");


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	//private Intent intent = new Intent("com.zgan.yckz.tools.YCKZ_RetriveArticleService");
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//initService();
		return super.onStartCommand(intent, flags, startId);
	}
	/*private void initService() {
		// TODO Auto-generated method stub
		Thread getArticlesThread = new Thread(null, mTask, "getZganPushMsg");
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
				// 消息服务器ip:61.186.245.251端口21011
				TCPSend ts = new TCPSend("61.186.245.251", 21011, s1,
						FrameTools.Queue_Send, FrameTools.Queue_Receive);

				Thread t1 = new Thread(ts);

				t1.start();

				
			}
		}).start();
		getArticlesThread.start();

	}*/
	Runnable mTask = new Runnable() {

		@Override
		public void run() {
			// get data from service
			while (true) {
				
						getUdpServiceMsg("");
					
				
			}
		}
	};
	private void getUdpServiceMsg(String strData) {
		String[] aryData = null;

		aryData = strData.split("\t");

		intMsgIndex++;

		// 发送Action为com.example.communication.RECEIVER的广播
		intent.putExtra("progress", 0);
		sendBroadcast(intent);
		Log.i("getUdpServiceMsg", "发送消息");

		PushMsg_IS = true;

		if (aryData[1].equals("0") && PushMsg_Alarm == true) {
			showNotification(intMsgIndex, aryData[2], aryData[3], aryData[1]);
		}

		

	}

	private Handler msgHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String[] aryData = new String[2];
			aryData = (String[]) msg.obj;
			dialog(aryData[0], aryData[1]);
		}
	};
	private void showNotification(int intIndex, String strTitle,
			String contentText, String strType) {

		// 获得通知管理器
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 构建一个通知对象(需要传递的参数有三个,分别是图标,标题和 时间)
		Notification notification = new Notification(R.drawable.alarm_icon,
				"家庭卫士消息推送", System.currentTimeMillis());
		Intent mI = null;
		PendingIntent pendingIntent = null;

		if (strType.endsWith("0")) {
			String[] aryData = new String[2];

			mI = new Intent(this, Welcome.class);

			aryData[0] = strTitle;
			aryData[1] = contentText;

			Message m = new Message();

			m.obj = aryData;

			msgHandler.sendMessage(m);

		} else if (strType.endsWith("7")) {
			mI = new Intent(this,Welcome.class);
		}

		pendingIntent = PendingIntent.getActivity(this, 0, mI, 0);

		notification.setLatestEventInfo(getApplicationContext(), strTitle,
				contentText, pendingIntent);// 这就是对通知的具体设置了
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		notification.defaults = Notification.DEFAULT_SOUND;// 声音默认
		manager.notify(intIndex, notification);// 发动通知
	}

	private void dialog(String strTitle, String contentText) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(strTitle).setMessage(contentText)
				.setPositiveButton("查看", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						Intent intent = new Intent(YCKZ_RetriveArticleService.this,
								Welcome.class);
						YCKZ_RetriveArticleService.this.startActivity(intent);
					}
				}).setNegativeButton("关闭", null);

		AlertDialog ad = builder.create();
		// ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
		// //系统中关机对话框就是这个属性
		ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		ad.setCanceledOnTouchOutside(false); // 点击外面区域不会让dialog消失
		ad.show();
	}
	

	@Override
	public void onDestroy() {
		// mNm.cancel(MOOD_NOTIFICATIONS);
		super.onDestroy();
	}

	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};
}
