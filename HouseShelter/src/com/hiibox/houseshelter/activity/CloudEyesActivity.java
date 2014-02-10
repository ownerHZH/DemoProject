package com.hiibox.houseshelter.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.listener.HandlerCommandListener;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.SpliteUtil;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.DateUtil;
import com.hiibox.houseshelter.util.FileUtil;
import com.hiibox.houseshelter.util.IBreakUtil;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.ScreenUtil;
import com.hiibox.houseshelter.util.StringUtil;
import com.hiibox.houseshelter.view.GestureDialog;
import com.hiibox.houseshelter.view.NineGridsView;
import com.hiibox.houseshelter.view.NineGridsView.OnCompleteListener;
import com.tutk.IOTC.AVIOCTRLDEFs;
import com.tutk.IOTC.Camera;
import com.tutk.IOTC.IRegisterIOTCListener;
import com.tutk.IOTC.Monitor;
import com.tutk.IOTC.AVIOCTRLDEFs.SMsgAVIoctrlGetSupportStreamReq;
import com.zg.camera.MyCamera;

    
  
  
  
  
  
  
  
public class CloudEyesActivity extends ShaerlocActivity implements
		IRegisterIOTCListener {

	@ViewInject(id = R.id.video_layout) RelativeLayout videoLayout;
	@ViewInject(id = R.id.function_button_layout) LinearLayout functionBtnLayout;
	@ViewInject(id = R.id.cloud_photo_album_iv, click = "onClick") ImageView cloudPhotoAlbumIV;
	@ViewInject(id = R.id.screenshot_iv, click = "onClick") ImageView screenshotIV;
	@ViewInject(id = R.id.voice_iv, click = "onClick") ImageView voiceIV;
	@ViewInject(id = R.id.mute_iv, click = "onClick") ImageView muteIV;
	@ViewInject(id = R.id.orientation_iv, click = "onClick") ImageView orientationIV;
	@ViewInject(id = R.id.settings_iv, click = "onClick") ImageView settingsIV;
	@ViewInject(id = R.id.turn_off_iv, click = "onClick") ImageView closeIV;
	@ViewInject(id = R.id.progress_bar) ProgressBar loadingBar;

	private Monitor monitor = null;         
	private MyCamera mCamera = null;
	private boolean gestureToggle = false;           
	private Bitmap bmp = null;
	private HandlerCommandListener commandListener = null;
	private String deviceCode = null;
	public static GestureDialog gestureDialog = null;
	private boolean mIsListening = false;
    private boolean mIsSpeaking = false;
    private String enter = null;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (null == msg.obj) {
            	return;
            }
            Frame[] frame = (Frame[]) msg.obj;
            if (null == frame[0]) {
            	return;
            }
            if (null == frame[1]) {
        		return;
        	}
            int subCmd = frame[0].subCmd;
            Log.i("CloudEyesActivity", "handleMessage()  subCmd = "+subCmd+" ; strData = "+frame[1].strData+";");
            if (msg.what == 0) {
            	if (subCmd == 18) {
            		int status = Integer.valueOf(frame[1].strData);
            		if (status == 0) {
            			Runnable runnable = new Runnable() {
							@Override
							public void run() {
								sendRequest(19);
							}
						};
						Thread thread = new Thread(runnable);
						thread.start();
            		} else if (status == 1){
            			MessageUtil.alertMessage(mContext, R.string.no_rights_to_upload_picture);
            		} else if (status == 2) {
            			MessageUtil.alertMessage(mContext, R.string.low_memory);
            		} else if (status == 3) {
            			MessageUtil.alertMessage(mContext, R.string.mistake_type);
            		}
                } else if (subCmd == 19) {
                	if (SpliteUtil.getRuquestStatus(frame[1].strData)) {
                		MessageUtil.alertMessage(mContext, R.string.photo_upload_to_cloud_album);
                		return;
                	} else {
                		MessageUtil.alertMessage(mContext, R.string.upload_picture_failed);
                	}
                	if (SpliteUtil.getResult(frame[1].strData).equals("1")) {
                		sendRequest(19);
                	}
                } else if (subCmd == 80) {
                	if (SpliteUtil.getRuquestStatus(frame[1].strData)) {
                		deviceCode = SpliteUtil.getResult(frame[1].strData);
                		MyApplication.deviceCode = deviceCode;
                		getDataByVideo();
                	} else {
                		MessageUtil.alertMessage(mContext, R.string.no_device);
                	}
                }
            } else if (msg.what == -1) {
            	MessageUtil.alertMessage(mContext, R.string.network_timeout);
            }
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enter = (String) getLastNonConfigurationInstance();
		if (StringUtil.isEmpty(enter)) {
		    enter = getIntent().getStringExtra("enter");
		}
		gestureToggle = PreferenceUtil.getInstance(mContext).getBoolean("gestureToggle", false);
		Log.e("CloudEyesActivity", "onCreate()  gestureToggle = "+gestureToggle+" ; isFirstTimeEntry = "+MyApplication.isFirstTimeEntry);
		if (gestureToggle) {
			  

  
		    if (StringUtil.isNotEmpty(enter) && enter.equals("1") && this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		        loadAdsDialog();
		        enter = "2";
		        MyApplication.isFirstTimeEntry = false;
		    }
                                                                                                        
                             
          
		}
		init();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
	    enter = "2";
	    return enter;
	}
	
	    
  
  
	private void init() {
		setContentView(R.layout.activity_cloud_eyes_layout);
		muteIV.setEnabled(false);
		functionBtnLayout.getBackground().setAlpha(200);
		commandListener = new HandlerCommandListener(handler);
		deviceCode = MyApplication.deviceCode;
		if (StringUtil.isEmpty(deviceCode)) {
			getDeviceCode();
		} else {
			getDataByVideo();
		}
	}
	
	private void loadAdsDialog() {
        final String currPassword = PreferenceUtil.getInstance(mContext).getString("gestureTracks", null);
        View splashView = getLayoutInflater().inflate(R.layout.activity_gesture_setting_layout, null);
        final NineGridsView ninePoints = (NineGridsView) splashView.findViewById(R.id.lock_screen_view);
        TextView promptTV = (TextView) splashView.findViewById(R.id.prompt_tv);
        ninePoints.setIsUnlock(true);
        promptTV.setText(R.string.draw_current_safe_gesture);
        gestureDialog = new GestureDialog(this, splashView);
        gestureDialog.show();
        ninePoints.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String password) {
                                                                                               
				if (currPassword.equals(password)) {
					ninePoints.clearUnlockInfo();
                    ninePoints.clearPassword();
                    password = null;
					gestureDialog.dismiss();
					gestureToggle = false;
					if (null != mCamera) {
					    mCamera.startListening(0);
			            mIsListening = true;
					}
				} else {         
                    MessageUtil.alertMessage(mContext, R.string.unlock_failure);
                }
				ninePoints.clearUnlockInfo();
                ninePoints.clearPassword();
                password = null;
			}
		});
    }
	
	public void getDataByVideo() {
		if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
			MessageUtil.alertMessage(mContext, R.string.sys_network_error);
			startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
			return;
		}
		MyCamera.init();
		Log.e("CloudEyesActivity", "getDataByVideo()  deviceCode = "+deviceCode);
		mCamera = new MyCamera("Camera", deviceCode, "admin");
		mCamera.registerIOTCListener(this);
		monitor = (com.tutk.IOTC.Monitor) this.findViewById(R.id.monitor);
		monitor.attachCamera(mCamera, 0);
		mCamera.connect(mCamera.getUID());
		mCamera.start(0, "admin", mCamera.getPassword());
		
                               
        if (!mCamera.isSessionConnected()) {
            mCamera.connect(mCamera.getUID());
            mCamera.start(0, "admin", "admin");
            mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
                    AVIOCTRLDEFs.IOTYPE_USER_IPCAM_GETSUPPORTSTREAM_REQ,
                    SMsgAVIoctrlGetSupportStreamReq.parseContent());
            mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
                    AVIOCTRLDEFs.IOTYPE_USER_IPCAM_DEVINFO_REQ,
                    AVIOCTRLDEFs.SMsgAVIoctrlDeviceInfoReq.parseContent());
            mCamera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
                    AVIOCTRLDEFs.IOTYPE_USER_IPCAM_GETAUDIOOUTFORMAT_REQ,
                    AVIOCTRLDEFs.SMsgAVIoctrlGetAudioOutFormatReq.parseContent());
        }
		
		mCamera.startShow(0, true);
		if (!gestureToggle) {
		    mCamera.startListening(0);
	        mIsListening = true;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setLargeScreenParams();
		if (monitor != null && null != mCamera) {
            monitor.attachCamera(mCamera, 0);
		}
        if (mCamera != null) {
            mCamera.startShow(0,true);
            if (mIsListening) {
                mCamera.startListening(0);
            }
            if (mIsSpeaking) {
                mCamera.startSpeaking(0);
            }
        }
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mCamera) {
			mCamera.unregisterIOTCListener(this);
			mCamera.stopListening(0);
			mCamera.stopSpeaking(0);
			mCamera.stopShow(0);
			mCamera.stop(0);
			mCamera.disconnect();
			mCamera = null;
		}
		if (null != monitor) {
            monitor.deattachCamera();
        }
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != handler) {
            handler.removeCallbacks(null);
        }
		if (null != cameraHandler) {
		    cameraHandler.removeCallbacks(null);
		}
		MyCamera.uninit();
	}
	
	public void onClick(View v) {
		int vid = v.getId();
		switch (vid) {
		case R.id.turn_off_iv:
			MianActivity.getScreenManager().exitActivity(mActivity);
			break;
		case R.id.orientation_iv:
			if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				Log.i("info", "landscape");       
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				Log.i("info", "portrait");       
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			}
			break;
		case R.id.cloud_photo_album_iv:
			startActivity(new Intent(mContext, CloudPhotoAlbumActivity.class));
			MianActivity.getScreenManager().exitActivity(mActivity);
			break;
		case R.id.screenshot_iv:
			if (null != mCamera) {
				bmp = mCamera.Snapshot(0);
				if (null != bmp) {
					sendRequest(18);
					if (FileUtil.isSdCardMounted()) {
						String path = IBreakUtil.SaveBitmap(bmp, "SNAPSHOT_"+System.currentTimeMillis()+".jpg");
						MessageUtil.alertMessage(mContext, getString(R.string.save_picure_path)+path);
					} else {
						MessageUtil.alertMessage(mContext, R.string.sd_card_unmounted);
					}
				} else {
					MessageUtil.alertMessage(mContext, R.string.save_picture_failed);
				}
			}
			break;
		case R.id.voice_iv:
			if (null != mCamera) {
			    mIsListening = false;
			    mIsSpeaking = true;
				mCamera.stopListening(0);
				mCamera.startSpeaking(0);
				Log.i("CloudEyesActivity", "onClick()   camera.startSpeaking(0)..........");
				voiceIV.setEnabled(false);
				muteIV.setEnabled(true);
				muteIV.setBackgroundResource(R.drawable.mute_off_icon);
				voiceIV.setBackgroundResource(R.drawable.voice_on_icon);
                                
                                                                              
                                     
                                                                                                  
               
        
                                                                                                                                                         
                                                                                                                             
                                                                                                                
        
                   
                                                                                    
			}
			break;
		case R.id.mute_iv:
			if (null != mCamera) {
			    mIsListening = true;
                mIsSpeaking = false;
				mCamera.stopSpeaking(0);
				mCamera.startListening(0);
				Log.i("CloudEyesActivity", "onClick()   camera.stopSpeaking(0)..........");
				voiceIV.setEnabled(true);
				muteIV.setEnabled(false);
				muteIV.setBackgroundResource(R.drawable.mute_on_icon);
				voiceIV.setBackgroundResource(R.drawable.voice_off_icon);
			}
			break;
		case R.id.settings_iv:
			startActivityForResult(new Intent(mContext, CloudEyesSettingsActivity.class), 0x111);
			break;
		default:
			break;
		}
	}

	    
  
  
  
	private void sendRequest(int subCmd) {
		if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
        	MessageUtil.alertMessage(mContext, R.string.sys_network_error);
        	startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        	return;
        }
		String phone = PreferenceUtil.getInstance(getApplicationContext()).getString("phone", null);
		if (StringUtil.isEmpty(phone)) {
			MessageUtil.alertMessage(mContext, R.string.please_login);
			if (null != MyApplication.mainClient) {
            	MyApplication.mainClient.stop();
            	MyApplication.mainClient = null;
            }
            if (null != MyApplication.fileClient) {
            	MyApplication.fileClient.stop();
                MyApplication.fileClient = null;
            }
            stopService(new Intent(mContext, PushMessageService.class));
			startActivity(new Intent(mContext, LoginActivity.class));
			MianActivity.getScreenManager().exitAllActivityExceptOne();
			return;
		}
		if (null == MyApplication.fileClient || !MyApplication.fileClient.isConnected()) {
    		MyApplication.initTcpManager();
            MyApplication.fileClient = MyApplication.tcpManager.getFileClient(phone);
    	}
		final byte[] file = IBreakUtil.bmpToByteArray(bmp, false);
		final int fileSize = file.length;           
		Log.i("CloudEyesActivity", "sendRequest()  �ļ��ֽڴ�С fileSize = " + fileSize);
		if (subCmd == 18) {
			Log.i("CloudEyesActivity", "sendRequest()  �������ϴ�ͼƬ�� �ļ��ֽڴ�С fileSize = " + fileSize);
			MyApplication.fileClient.saveFileRequest(phone, fileSize, DateUtil.getcurrentDay(), 2, commandListener);
		} else if (subCmd == 19) {
			Log.i("CloudEyesActivity", "sendRequest()  ���ϴ�ͼƬ�� �ļ��ֽڴ�С fileSize = " + fileSize);
			MyApplication.fileClient.uploadFile(fileSize, file, commandListener);
		}
	}
	
	    
  
  
  
	private void getDeviceCode() {
		if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
        	MessageUtil.alertMessage(mContext, R.string.sys_network_error);
        	startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        	return;
        }
		String phone = PreferenceUtil.getInstance(getApplicationContext()).getString("phone", null);
		if (StringUtil.isEmpty(phone)) {
			MessageUtil.alertMessage(mContext, R.string.please_login);
			if (null != MyApplication.mainClient) {
            	MyApplication.mainClient.stop();
            	MyApplication.mainClient = null;
            }
            if (null != MyApplication.fileClient) {
            	MyApplication.fileClient.stop();
                MyApplication.fileClient = null;
            }
            stopService(new Intent(mContext, PushMessageService.class));
			startActivity(new Intent(mContext, LoginActivity.class));
			MianActivity.getScreenManager().exitAllActivityExceptOne();
			return;
		}
		if (null == MyApplication.mainClient || !MyApplication.mainClient.isConnected()) {
    		MyApplication.initTcpManager();
            MyApplication.mainClient = MyApplication.tcpManager.getMainClient(phone, null, "1111111111111111", "66666666");
    	}
		MyApplication.mainClient.getDeviceCode(phone, commandListener);
	}

	    
  
  
	private void setLargeScreenParams() {
		int screenHeight = ScreenUtil.getScreenHeight(mActivity);
		if (screenHeight > 854 && screenHeight <= 1280) {
			LayoutParams videoParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, 650);
			videoParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			videoLayout.setLayoutParams(videoParams);

			android.widget.LinearLayout.LayoutParams photoAlbumParams = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			photoAlbumParams.leftMargin = 20;
			cloudPhotoAlbumIV.setLayoutParams(photoAlbumParams);

			android.widget.LinearLayout.LayoutParams screenshotParams = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			screenshotParams.rightMargin = 20;
			screenshotIV.setLayoutParams(screenshotParams);

			android.widget.LinearLayout.LayoutParams voiceParams = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			voiceParams.rightMargin = 20;
			voiceIV.setLayoutParams(voiceParams);

			android.widget.LinearLayout.LayoutParams muteParams = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			muteParams.rightMargin = 20;
			muteIV.setLayoutParams(muteParams);

			android.widget.LinearLayout.LayoutParams orientationParams = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			orientationParams.rightMargin = 20;
			orientationIV.setLayoutParams(orientationParams);

			android.widget.LinearLayout.LayoutParams settingsParams = new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
					android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
			settingsParams.rightMargin = 20;
			settingsIV.setLayoutParams(settingsParams);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i("CloudEyesActivity", "onActivityResult()  requestCode = "
				+ requestCode);
		if (requestCode == 0x101 && resultCode == RESULT_OK) {
			boolean unlockSuccess = data
					.getBooleanExtra("unlockSuccess", false);
			Log.i("CloudEyesActivity", "onActivityResult()  unlockSuccess = "
					+ unlockSuccess);
			if (!unlockSuccess) {
				MianActivity.getScreenManager().exitActivity(mActivity);
				return;
			} else {
				getDataByVideo();
			}
		} else if (requestCode == 0x111) {
		    if (null != loadingBar) {
		        loadingBar.setVisibility(View.VISIBLE);
		    }
		    getDataByVideo();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void receiveChannelInfo(final Camera camera, final int avChannel, final int status) {}

	@Override
	public void receiveFrameData(final Camera camera, final int avChannel, final Bitmap bmp) {}

	@Override
	public void receiveFrameInfo(final Camera camera, final int avChannel,
	                             final long bitRate, final int frameRate, final int onlineNm,
	                             final int frameCount, final int incompleteFrameCount) {}

	@Override
	public void receiveIOCtrlData(final Camera camera, final int avChannel,
	                              final int avIOCtrlMsgType, final byte[] data) {}

	@Override
	public void receiveSessionInfo(Camera camera, int resultCode) {
	    if (mCamera == camera) {
	        Bundle bundle = new Bundle();
            Message msg = cameraHandler.obtainMessage();
            msg.what = resultCode;
            msg.setData(bundle);
            cameraHandler.sendMessage(msg);
	    }
	}
	
	@SuppressLint("HandlerLeak")
    private Handler cameraHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Camera.CONNECTION_STATE_CONNECTING:
                    if (null != loadingBar) {
                        loadingBar.setVisibility(View.VISIBLE);
                    }
                    Log.d("CloudEyesActivity", "=== cameraHandler === ������");
                    break;
                case Camera.CONNECTION_STATE_CONNECTED:
                    if (null != loadingBar) {
                        loadingBar.setVisibility(View.GONE);
                    }
                    Log.d("CloudEyesActivity", "=== cameraHandler === ���ӳɹ�");
                    break;
                case Camera.CONNECTION_STATE_DISCONNECTED:
                    if (null != loadingBar) {
                        loadingBar.setVisibility(View.GONE);
                    }
                    MessageUtil.alertMessage(mContext, R.string.connstus_disconnect);
                    Log.d("CloudEyesActivity", "=== cameraHandler === ���ӶϿ�");
                    break;
                case Camera.CONNECTION_STATE_UNKNOWN_DEVICE:
                    if (null != loadingBar) {
                        loadingBar.setVisibility(View.GONE);
                    }
                    MessageUtil.alertMessage(mContext, R.string.connstus_unknown_device);
                                         
                    Log.d("CloudEyesActivity", "=== cameraHandler === δ֪�豸");
                    break;
                case Camera.CONNECTION_STATE_TIMEOUT:
                    if (null != loadingBar) {
                        loadingBar.setVisibility(View.GONE);
                    }
                    MessageUtil.alertMessage(mContext, R.string.connstus_timeout);
                    Log.d("CloudEyesActivity", "=== cameraHandler === ���ӳ�ʱ");
                    break;
                case Camera.CONNECTION_STATE_CONNECT_FAILED:
                    if (null != loadingBar) {
                        loadingBar.setVisibility(View.GONE);
                    }
                    MessageUtil.alertMessage(mContext, R.string.connstus_connection_failed);
                    Log.d("CloudEyesActivity", "=== cameraHandler === ����ʧ��");
                    break;
                case Camera.CONNECTION_STATE_WRONG_PASSWORD:
                    if (null != loadingBar) {
                        loadingBar.setVisibility(View.GONE);
                    }
                    MessageUtil.alertMessage(mContext, R.string.connstus_wrong_password);
                                         
                    Log.d("CloudEyesActivity", "=== cameraHandler === ��������");
                    break;
                default:
                    break;
            }
        }
	};
	
	public View makeView() {
        return null;
    }
}
