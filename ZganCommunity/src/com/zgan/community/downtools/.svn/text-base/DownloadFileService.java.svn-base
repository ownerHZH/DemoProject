package com.zgan.community.downtools;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.zgan.community.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
/**
 * 文件下载的service
 * @author 
 *
 */
public class DownloadFileService extends Service {

	private DownloadFileUtils downloadFileUtils;//文件下载工具类
	private String filePath;//保存在本地的路径
	private NotificationManager notificationManager;//状态栏通知管理类
	private Notification notification;//状态栏通知
	private RemoteViews remoteViews;//状态栏通知显示的view
	private final int notificationID = 1;//通知的id
	private final int updateProgress = 1;//更新状态栏的下载进度
	private final int downloadSuccess = 2;//下载成功
	private final int downloadError = 3;//下载失败
	private final String TAG = "DownloadFileService";
	private Timer timer;//定时器，用于更新下载进度
	private TimerTask task;//定时器执行的任务
	private String file_name="http://img2.zgantech.com/APK/HouseShelter_v7.7.apk";
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		init();
	}
	
	private void init(){
		filePath = Environment.getExternalStorageDirectory()+"/Zgan";
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = R.drawable.ic_launcher;//设置通知消息的图标
		notification.tickerText = "正在下载。。。";//设置通知消息的标题
		remoteViews = new RemoteViews(getPackageName(), R.layout.down_notification);
		remoteViews.setImageViewResource(R.id.IconIV, R.drawable.ic_launcher);
		timer = new Timer();
		task = new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(updateProgress);
			}
		};
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				downloadFileUtils = new DownloadFileUtils("http://img2.zgantech.com/APK/HouseShelter_v7.7.apk", filePath, "HouseShelter_v7.7.apk", 3,callback);
				downloadFileUtils.downloadFile();
			}
		}).start();
		timer.schedule(task, 500, 500);
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	@Override
	public void onDestroy() {
		Log.i(TAG, TAG+" is onDestory...");
		super.onDestroy();
	}


	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == updateProgress) {//更新下载进度
				long fileSize = downloadFileUtils.getFileSize();
				long totalReadSize = downloadFileUtils.getTotalReadSize();
				if(totalReadSize > 0){
					float size = (float) totalReadSize * 100 / (float) fileSize;
					DecimalFormat format = new DecimalFormat("0.00");
					String progress = format.format(size);
					remoteViews.setTextViewText(R.id.progressTv, "已下载" + progress+ "%");
					remoteViews.setProgressBar(R.id.progressBar, 100, (int) size,false);
					notification.contentView = remoteViews;
					notificationManager.notify(notificationID, notification);
				}
			} else if (msg.what == downloadSuccess) {//下载完成
				remoteViews.setTextViewText(R.id.progressTv, "下载完成");
				remoteViews.setProgressBar(R.id.progressBar, 100, 100, false);
				notification.contentView = remoteViews;
				notificationManager.notify(notificationID, notification);
				if(timer != null && task != null){
					timer.cancel();
					task.cancel();
					timer = null;
					task = null;
				}
				stopService(new Intent(getApplicationContext(),DownloadFileService.class));
				//stop service
				installApk(filePath+"/HouseShelter_v7.7.apk");
			} else if (msg.what == downloadError) {//下载失败
				if(timer != null && task != null){
					timer.cancel();
					task.cancel();
					timer = null;
					task = null;
				}
				notificationManager.cancel(notificationID);
				stopService(new Intent(getApplicationContext(),DownloadFileService.class));//stop service
			}
		}
		
	};
	protected void installApk(String filename) {
		// TODO Auto-generated method stub
		File file = new File(filename);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW); // 浏览网页的Action(动作)
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type); // 设置数据类型
		startActivity(intent);
	}
	/**
	 * 下载回调
	 */
	DownloadFileCallback callback = new DownloadFileCallback() {
		
		@Override
		public void downloadSuccess(Object obj) {
			handler.sendEmptyMessage(downloadSuccess);
		}
		
		@Override
		public void downloadError(Exception e, String msg) {
			handler.sendEmptyMessage(downloadError);
		}
	};
	

}
