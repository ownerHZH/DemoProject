package com.hiibox.houseshelter.service;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.activity.ImprintingActivity;
import com.hiibox.houseshelter.activity.SplashActivity;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.TCPServiceClientV2.ClientListener;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.util.DateUtil;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class PushMessageService extends Service implements ClientListener {

	private Resources res = null;
	private String phone = null;
	private String password = null;
	public static Handler handler = null;
	private WindowManager mWindowManager = null;
	private LayoutParams params = null;
	private View temperatureView = null;
	private View intrudeView = null;
	private boolean temperatureViewShowing = false;
	private boolean intrudeViewShowing = false;
	
	private static int isRunning = 0;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("PushMessageService", "onStartCommand.......................");
		res = getApplicationContext().getResources();
		phone = PreferenceUtil.getInstance(getApplicationContext()).getString(
				"phone", null);
		password = PreferenceUtil.getInstance(getApplicationContext())
				.getString("password", null);
		mWindowManager = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		if(isRunning == 0) {
    		recieveMsg();
    		isRunning = 1;
		}
		return super.onStartCommand(intent, flags, startId);
	}

	

	private void recieveMsg() {
		if (null ==MyApplication.tcpManager) {
			return;
		}
                                                   
             
      
		MyApplication.msgClient = MyApplication.tcpManager.getMsgClient(
				phone, password, new CommandListener() {
					@Override
					public void onTimeout(Frame src, Frame f) {
						isRunning = 0;
					}

					@Override
					public int onReceive(Frame src, Frame f) {
						if (null != f) {
							int subCmd = f.subCmd;
							int msgId = spliteData(f.strData);
							Log.d("PushMessageService", "onReceive()    data = " + f.strData
											+ " ; MainCmd = " + f.mainCmd
											+ " ; subCmd = " + f.subCmd
											+ " ; msgId = " + msgId);
							String content = null;
							switch (subCmd) {
							case 1:         
							    replyServer(subCmd, msgId);
								if (null != handler) {
									handler.sendEmptyMessage(5);           
								}
								boolean openDefence = PreferenceUtil
										.getInstance(getApplicationContext())
										.getBoolean("defenceMessage", true);
								if (openDefence) {
									String actor = getDefenceActor(f.strData);
									if (isMobileNumber(actor)) {
                                            content =
                                                    res.getString(R.string.your_family_number)
                                                            + actor
                                                            + res.getString(R.string.at)
                                                            + getTime(f.strData, 2)
                                                            + res.getString(R.string.do_something_when)
                                                            + res.getString(R.string.defence_open);
									} else {
									    content =
                                                res.getString(R.string.your_family)
                                                        + actor
                                                        + res.getString(R.string.at)
                                                        + getTime(f.strData, 2)
                                                        + res.getString(R.string.do_something_when)
                                                        + res.getString(R.string.defence_open);
									}
									createNotification(msgId, content, ImprintingActivity.class);
								}
								break;
							case 2:         
							    replyServer(subCmd, msgId);
								if (null != handler) {
									handler.sendEmptyMessage(6);           
								}
								boolean cancelDefence = PreferenceUtil
										.getInstance(getApplicationContext())
										.getBoolean("defenceMessage", true);
								if (cancelDefence) {
									String actor = getDefenceActor(f.strData);
                                        if (isMobileNumber(actor)) {
                                            content =
                                                    res.getString(R.string.your_family_number)
                                                            + actor
                                                            + res.getString(R.string.at)
                                                            + getTime(f.strData, 2)
                                                            + res.getString(R.string.do_something_when)
                                                            + res.getString(R.string.defence_cancel);
                                        } else {
                                            content =
                                                    res.getString(R.string.your_family)
                                                            + actor
                                                            + res.getString(R.string.at)
                                                            + getTime(f.strData, 2)
                                                            + res.getString(R.string.do_something_when)
                                                            + res.getString(R.string.defence_cancel);
                                        }
                                        createNotification(msgId, content, ImprintingActivity.class);
								}
								break;
							case 3:         
							    replyServer(subCmd, msgId);
								handler.sendEmptyMessage(3);
								mHandler.sendEmptyMessage(101);
								boolean alarmMessage = PreferenceUtil
										.getInstance(getApplicationContext())
										.getBoolean("alarmMessage", true);
								if (alarmMessage) {
									createNotification(msgId, res.getString(R.string.invade_msg), ImprintingActivity.class);
								}
								break;
							case 16:         
							    replyServer(subCmd, msgId);
								Message temp = new Message();
								temp.obj = getTemperature(f.strData);
								temp.what = 4;
								handler.sendMessage(temp);
								mHandler.sendEmptyMessage(102);
								boolean alarmMsg = PreferenceUtil
                                        .getInstance(getApplicationContext())
                                        .getBoolean("alarmMessage", true);
                                if (alarmMsg) {
                                    createNotification(msgId, res.getString(R.string.temperature_abnormal_msg), ImprintingActivity.class);
                                }
								break;
							case 17:         
                                     
                                                              
                                      
                                       
								replyServer(subCmd, msgId);
								break;
							case 23:          
							    replyServer(subCmd, msgId);
								Message msg = new Message();
								msg.obj = getCardNumber(f.strData);
								msg.what = getHomeType(f.strData);
								Log.i("PushMessageService", "onReceive()  [��ң��������]  cardNumber ����"+msg.obj+" ; status = "+msg.obj+" ��0��ʾ��ҡ�1��ʾ���");
								handler.sendMessage(msg);
								boolean goHome = PreferenceUtil.getInstance(
										getApplicationContext()).getBoolean(
										"goHomeMessage", true);
								if (goHome) {
									if (getHomeType(f.strData) == 0) {       
										createNotification(
												msgId,
                                                      
												res.getString(R.string.your_family)
														+ getNickname(f.strData)
														+ res.getString(R.string.at)
														+ getTime(f.strData, 4)
														+ res.getString(R.string.already)
														+ res.getString(R.string.out_of_home),
												ImprintingActivity.class);
									} else if (getHomeType(f.strData) == 1) {       
										createNotification(
												msgId,
                                                      
												res.getString(R.string.your_family)
														+ getNickname(f.strData)
														+ res.getString(R.string.at)
														+ getTime(f.strData, 4)
														+ res.getString(R.string.already)
														+ res.getString(R.string.go_back_home),
												ImprintingActivity.class);
									}
								}
								break;
							default:
								break;
							}
						}
						return 0;
					}

				});
		if (null != MyApplication.msgClient) {
			MyApplication.msgClient.listener = this;
		}

	}

	    
  
  
  
  
  
	private String getCardNumber(String data) {
		if (null == data) {
			return null;
		}
		String[] str = data.split("\t");
		if (str.length != 6) {
			return null;
		}
		return str[5];
	}

	    
  
  
  
  
  
  
	private int spliteData(String recieveData) {
		if (StringUtil.isEmpty(recieveData)) {
			return 0;
		}
		String[] str = recieveData.split("\t");
                                                                                                      
		if (str.length < 2) {
			return 0;
		}
		return Integer.valueOf(str[1]);
	}

	    
  
  
  
  
  
	private String getDefenceActor(String recieveData) {
		if (StringUtil.isEmpty(recieveData)) {
			return "";
		}
		String[] str = recieveData.split("\t");
		if (str.length < 5) {
			return "";
		}
		return str[4].trim();
	}
	
	    
  
  
  
  
  
	private String getNickname(String recieveData) {
		if (StringUtil.isEmpty(recieveData)) {
			return "";
		}
		String[] str = recieveData.split("\t");
		if (str.length < 2) {
		    return "";
		}
		return str[2].trim();
	}

	    
  
  
  
  
  
	private int getHomeType(String recieveData) {
		if (StringUtil.isEmpty(recieveData)) {
			return -1;
		}
		String[] str = recieveData.split("\t");
		return Integer.valueOf(str[3]);
	}

	private String getTemperature(String strData) {
		if (StringUtil.isEmpty(strData)) {
			return "0";
		}
		String[] str = strData.split("\t");
		if (str.length != 3) {
			return "0";
		}
		return str[2];
	}

	    
  
  
  
  
  
  
	private String getTime(String recieveData, int index) {
		if (StringUtil.isEmpty(recieveData)) {
			return getApplicationContext().getResources().getString(
					R.string.just_now);
		}
		String[] str = recieveData.split("\t");
		String[] time = str[index].split(" ");
		return time[1];
		                                                                
	}

	    
  
  
  
  
  
  
  
	private void replyServer(int subCmd, int msgId) {
		MyApplication.msgClient.replyMsg(subCmd, msgId);
	}

    private boolean isMobileNumber(String mobiles) {
		Pattern p = Pattern
				.compile("((13[0-9])|(15[^4,\\D])|(18[0,1,3,5-9]))\\d{8}");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	    
  
  
	public void createNotification(int notificationId,     String content, Class<?> cls) {
		Intent intent = new Intent(this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				content, System.currentTimeMillis());
		                                                          
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.setLatestEventInfo(getApplicationContext(), getApplicationContext().getResources().getString(R.string.remind),
				content, pendingIntent);
                                                                    
                                 

		notificationManager.notify(notificationId, notification);
	}
	
	    
  
  
  
	private void addTemperatureView() {
		params = new LayoutParams();

		                 
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

		                 
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		temperatureView = View.inflate(getApplicationContext(), R.layout.alarm_dialog_layout, null);
		TextView typeTV = (TextView) temperatureView.findViewById(R.id.stop_voice_iv);
		TextView timeTV = (TextView) temperatureView.findViewById(R.id.alarm_time_tv);
		ImageView iv = (ImageView) temperatureView.findViewById(R.id.alarm_iv);
		iv.setBackgroundDrawable(new BitmapDrawable(ImageOperation.readBitMap(getApplicationContext(), R.drawable.temperature_abnormal_iv)));
		typeTV.setText(getApplicationContext().getResources().getString(R.string.temperature_warning));
		timeTV.setText(getString(R.string.alarm_time)+" "+DateUtil.getTime());
		temperatureView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (PreferenceUtil.getInstance(getApplicationContext()).getBoolean("exitApp", false)) {
					intent.setClass(getApplicationContext(), SplashActivity.class);
				} else {
					intent.setClass(getApplicationContext(), ImprintingActivity.class);
				}
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("callAppFromService", true);
				intent.putExtra("queryIndex", 1);
				startActivity(intent);
				mWindowManager.removeView(temperatureView);
				temperatureViewShowing = false;
			}
		});
		mWindowManager.addView(temperatureView, params);
		temperatureViewShowing = true;
	}
	
	    
  
  
  
	private void addIntrudeView() {
		params = new LayoutParams();
		
		                 
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		
		                 
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		intrudeView = View.inflate(getApplicationContext(), R.layout.alarm_dialog_layout, null);
		TextView typeTV = (TextView) intrudeView.findViewById(R.id.stop_voice_iv);
		TextView timeTV = (TextView) intrudeView.findViewById(R.id.alarm_time_tv);
		ImageView iv = (ImageView) intrudeView.findViewById(R.id.alarm_iv);
		iv.setBackgroundDrawable(new BitmapDrawable(ImageOperation.readBitMap(getApplicationContext(), R.drawable.alarm_iv)));
		typeTV.setText(getApplicationContext().getResources().getString(R.string.invade_warning));
		timeTV.setText(getString(R.string.alarm_time)+" "+DateUtil.getTime());
		intrudeView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (PreferenceUtil.getInstance(getApplicationContext()).getBoolean("exitApp", false)) {
					intent.setClass(getApplicationContext(), SplashActivity.class);
				} else {
					intent.setClass(getApplicationContext(), ImprintingActivity.class);
				}
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("queryIndex", 2);
				startActivity(intent);
				mWindowManager.removeView(intrudeView);
				intrudeViewShowing = false;
			}
		});
		mWindowManager.addView(intrudeView, params);
		intrudeViewShowing = true;
	}
	
	@SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 101) {
			    boolean alarmMessage = PreferenceUtil
                        .getInstance(getApplicationContext())
                        .getBoolean("alarmMessage", true);
                if (!alarmMessage) {
                    return;
                }
				if (intrudeViewShowing) {
					mWindowManager.removeView(intrudeView);
					addIntrudeView();
					return;
				}
				if (temperatureViewShowing) {
					mWindowManager.removeView(temperatureView);
					addIntrudeView();
					return;
				}
				addIntrudeView();
			} else if (msg.what == 102) {
			    boolean alarmMsg = PreferenceUtil
                        .getInstance(getApplicationContext())
                        .getBoolean("alarmMessage", true);
                if (!alarmMsg) {
                    return;
                }
				if (temperatureViewShowing) {
					mWindowManager.removeView(temperatureView);
					addTemperatureView();
					return;
				}
				if (intrudeViewShowing) {
					mWindowManager.removeView(intrudeView);
					addTemperatureView();
					return;
				}
				addTemperatureView();
			}
		}
	};

	@Override
	public void onClientStop() {
		Log.i("PushMessageService", "onClientStop().................................");
	}

	@Override
	public void onClientStart() {
		Log.i("PushMessageService", "onClientStart().................................");

	}

	@Override
	public void onClientClose() {
		Log.e("PushMessageService", "onClientClose().................................");
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		recieveMsg();
	}

	@Override
	public void onClientException(IOException ex) {
		Log.i("PushMessageService", "onClientException()...........ex = "+ex.toString());
	}

	@Override
	public void onLoginFail() {
		Log.i("PushMessageService", "onLoginFail().................................");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(null);
		if (null != MyApplication.msgClient) {
        	MyApplication.msgClient.stop();
        	MyApplication.msgClient = null;
        }
		stopSelf();
		Log.e("PushMessageService", "onDestroy().................................");
	}

}
