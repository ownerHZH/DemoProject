package com.hiibox.houseshelter.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.DropDownBoxAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.listener.HandlerCommandListener;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.SpliteUtil;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.DateUtil;
import com.hiibox.houseshelter.util.FileUtil;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class CloudEyesSettingsActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.root_layout) LinearLayout rootLayout;
    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.clear_cache_iv, click = "onClick") ImageView clearCacheIV;
                                                                                            
    @ViewInject(id = R.id.safe_gesture_setting_tv, click = "onClick") TextView gestureTV;
    @ViewInject(id = R.id.video_quality_drop_down_box_et, click = "onClick") EditText videoQualityET;
    @ViewInject(id = R.id.capture_drop_down_box_et, click = "onClick") EditText captureNumET;
    @ViewInject(id = R.id.timing_drop_down_box_et, click = "onClick") EditText timingET;
    @ViewInject(id = R.id.gesture_toggle_btn_layout, click = "onClick") LinearLayout gestureToggleLayout;
    @ViewInject(id = R.id.gesture_toggle_btn_on_iv) ImageView gestureToggleOnIV;
    @ViewInject(id = R.id.gesture_toggle_btn_off_iv) ImageView gestureToggleOffIV;
    @ViewInject(id = R.id.gesture_toggle_btn_prompt_tv) TextView gestureTogglePromptTV;
    @ViewInject(id = R.id.capture_toggle_btn_layout, click = "onClick") LinearLayout captureToggleLayout;
    @ViewInject(id = R.id.capture_toggle_btn_on_iv) ImageView captureToggleOnIV;
    @ViewInject(id = R.id.capture_toggle_btn_off_iv) ImageView captureToggleOffIV;
    @ViewInject(id = R.id.capture_toggle_btn_prompt_tv) TextView captureTogglePromptTV;
    
    private DropDownBoxAdapter adapter = null;
    private String[] captureNumbersArr = null;
    private List<String> captureNumbersList = null;
    private View cardView = null;
    private ListView cardNumberLV = null;
    private TimePickerDialog picker = null;
    private boolean gestureToggle = false;
    private boolean captureToggle = false;
    private String gestureTracks = null;
    private int picNum = 0;         
    private String time = null;         
    private HandlerCommandListener commandListener = null;
    private ProgressDialog loginDialog = null;
                                              
    
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
            int subCmd = frame[0].subCmd;
            if (msg.what == 0) {
                if (null == frame[1]) {
                	return;
                }
                String ret = frame[1].strData;
                Log.d("CameraSettingsActivity", "subCmd = " + subCmd+" ; ret = "+ret);
                switch (subCmd) {
                    case 7:           
                        loginDialog.dismiss();
                        if (ret.equals("0")) {
                            captureNumET.setText(captureNumbersList.get(picNum));
                            PreferenceUtil.getInstance(mContext).saveInt("captureNumbers", picNum);
                            MessageUtil.alertMessage(mContext, R.string.setting_success);
                        } else {
                            MessageUtil.alertMessage(mContext, R.string.setting_failed);
                        }
                        Log.d("CameraSettingsActivity", "[ץ����������] �յ���Ӧ :" + ret);
                        break;
                    case 12:             
                    	loginDialog.dismiss();
                    	if (ret.equals("0")) {
                    	    timingET.setText(time);
                            PreferenceUtil.getInstance(mContext).saveString("captureTime", time);
                            MessageUtil.alertMessage(mContext, R.string.setting_success);
                    	} else {
                    	    MessageUtil.alertMessage(mContext, R.string.setting_failed);
                    	}
                    	Log.d("CameraSettingsActivity", "[��ʱץ��ʱ������] �յ���Ӧ :" + ret);
                    	break;
                    case 88:
                        if (!ret.startsWith("0")) {
                            captureNumET.setText(captureNumbersList.get(picNum));
                        } else {
                            if (SpliteUtil.getResult(ret).equals("1")) {
                                captureNumET.setText(captureNumbersList.get(0));
                            } else if (SpliteUtil.getResult(ret).equals("2")) {
                                captureNumET.setText(captureNumbersList.get(1));
                            } else if (SpliteUtil.getResult(ret).equals("3")) {
                                captureNumET.setText(captureNumbersList.get(2));
                            } else {
                                captureNumET.setText(getString(R.string.spaceholder));
                            }
                        }
                        break;
                    case 89:
                        if (!ret.startsWith("0")) {
                            timingET.setText(getString(R.string.spaceholder));
                        } else {
                            timingET.setText(SpliteUtil.getResult(ret));
                        }
                        break;
                    case 90:
                        if (!ret.startsWith("0")) {
                            timingET.setText(getString(R.string.spaceholder));
                        } else {
                            String[] str = ret.split("\t");
                            timingET.setText(str[1]);
                            time = str[1];
                            if (str.length == 3) {
                                if (str[2].equals("0")) {
                                    captureToggle = true;
                                    captureToggleOnIV.setVisibility(View.VISIBLE);
                                    captureToggleOffIV.setVisibility(View.INVISIBLE);
                                    captureToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_on);
                                    captureTogglePromptTV.setText(getString(R.string.turn_on));
                                } else {
                                    captureToggle = false;
                                    captureToggleOnIV.setVisibility(View.INVISIBLE);
                                    captureToggleOffIV.setVisibility(View.VISIBLE);
                                    captureToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_off);
                                    captureTogglePromptTV.setText(getString(R.string.turn_off));
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            } else {       
                switch (subCmd) {
					case 7:
						loginDialog.dismiss();
						MessageUtil.alertMessage(mContext, R.string.network_timeout);
						Log.d("CameraSettingsActivity", "[ץ����������] �յ���Ӧ : ��ʱ");
						break;
					case 12:
						loginDialog.dismiss();
						MessageUtil.alertMessage(mContext, R.string.network_timeout);
						Log.d("CameraSettingsActivity", "[��ʱץ��ʱ������] �յ���Ӧ : ��ʱ");
						break;
					case 88:
					    captureNumET.setText(captureNumbersList.get(picNum));
					    break;
					case 89:
					    timingET.setText(getString(R.string.spaceholder));
					    break;
					default:
						break;
				}
            }
        }
        
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_eyes_settings_layout);
        commandListener = new HandlerCommandListener(handler);
        gestureToggle = PreferenceUtil.getInstance(mContext).getBoolean("gestureToggle", false);
        captureToggle = PreferenceUtil.getInstance(mContext).getBoolean("captureToggle", false);
        picNum = PreferenceUtil.getInstance(mContext).getInt("captureNumbers", 2);
                                                                                                                 
                                  
        
        if (gestureToggle) {          
        	gestureToggleOnIV.setVisibility(View.VISIBLE);
            gestureToggleOffIV.setVisibility(View.INVISIBLE);
            gestureToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_on);
            gestureTogglePromptTV.setText(getString(R.string.turn_on));
        } else {          
        	gestureToggleOnIV.setVisibility(View.INVISIBLE);
            gestureToggleOffIV.setVisibility(View.VISIBLE);
            gestureToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_off);
            gestureTogglePromptTV.setText(getString(R.string.turn_off));
        }
        
        initAdapter();
        
        loginDialog = new ProgressDialog(this);
        loginDialog.setCancelable(true);
        loginDialog.setCanceledOnTouchOutside(true);
        
        sendRequest(88, null, 0);
        sendRequest(90, null, 0);
    }
    
	@Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(
            ImageOperation.readBitMap(mContext, R.drawable.bg_app));
        rootLayout.setBackgroundDrawable(bitmapDrawable);
        gestureTracks = PreferenceUtil.getInstance(mContext).getString("gestureTracks", null);
        if (StringUtil.isNotEmpty(gestureTracks)) {
            gestureTV.setText(getString(R.string.modify_safe_gesture));
        }
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        rootLayout.setBackgroundDrawable(null);
    }

    private void initAdapter() {
        captureNumbersArr = getResources().getStringArray(R.array.capture_picture_numbers_array);
        captureNumbersList = new ArrayList<String>();
        for (int i = 0; i < captureNumbersArr.length; i ++) {
            captureNumbersList.add(captureNumbersArr[i]);
        }
                                                                
        adapter = new DropDownBoxAdapter(mContext);
        adapter.setList(captureNumbersList);
        cardView = getLayoutInflater().inflate(R.layout.popupwindow_drop_down_layout, null);
        cardNumberLV = (ListView) cardView.findViewById(R.id.popup_lv);
        cardNumberLV.setAdapter(adapter);
    }

    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.back_iv:
                MianActivity.getScreenManager().exitActivity(mActivity);
                break;
            case R.id.clear_cache_iv:
                if (FileUtil.isSdCardMounted()) {
                    MessageUtil.alertMessage(mContext, R.string.clearing_cache);
                    if (FileUtil.delFolder(GlobalUtil.GLOBAL_PATH)) {
                        MessageUtil.alertMessage(mContext, R.string.clear_all_cache);
                    } else {
                        MessageUtil.alertMessage(mContext, R.string.clear_cache_failure);
                    }
                } else {
                    MessageUtil.alertMessage(mContext, R.string.sd_card_unmounted);
                }
                break;
                                                 
                   
                         
            case R.id.safe_gesture_setting_tv:
                startActivity(new Intent(this, GestureSettingActivity.class));
                break;
            case R.id.video_quality_drop_down_box_et:
                MessageUtil.alertMessage(mContext, R.string.only_open_medium_video_quality);
                break;
            case R.id.capture_drop_down_box_et:
                showPopupWindow();
                break;
            case R.id.timing_drop_down_box_et:
                showTimePicker();
                break;
            case R.id.gesture_toggle_btn_layout:
                if (gestureToggle) {               
                    startActivityForResult(new Intent(CloudEyesSettingsActivity.this, CloseGestureActivity.class), 0x502);
                } else {               
                    if (StringUtil.isEmpty(gestureTracks)) {
                        startActivityForResult(new Intent(CloudEyesSettingsActivity.this, GestureSettingActivity.class), 0x501);
                    } else {
                        openGesture();
                    }
                }
                break;
            case R.id.capture_toggle_btn_layout:
                if (captureToggle) {               
                    sendRequest(12, time, 1);
                    captureToggle = false;
                    captureToggleOnIV.setVisibility(View.INVISIBLE);
                    captureToggleOffIV.setVisibility(View.VISIBLE);
                    captureToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_off);
                    captureTogglePromptTV.setText(getString(R.string.turn_off));
                } else {               
                    sendRequest(12, time, 0);
                    captureToggle = true;
                    captureToggleOnIV.setVisibility(View.VISIBLE);
                    captureToggleOffIV.setVisibility(View.INVISIBLE);
                    captureToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_on);
                    captureTogglePromptTV.setText(getString(R.string.turn_on));
                }
                break;
            default:
                break;
        }
    }

    private void openGesture() {
        gestureToggle = true;
        gestureToggleOnIV.setVisibility(View.VISIBLE);
        gestureToggleOffIV.setVisibility(View.INVISIBLE);
        gestureToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_on);
        gestureTogglePromptTV.setText(getString(R.string.turn_on));
        PreferenceUtil.getInstance(mContext).saveBoolean("gestureToggle", gestureToggle);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x501 && resultCode == RESULT_OK) {
            boolean gestureSuccess = data.getBooleanExtra("unlockSuccess", false);
            if (gestureSuccess) {
                openGesture();
            }
        } else if (requestCode == 0x502 && resultCode == RESULT_OK) {
            if (null == data) {
                return;
            }
            boolean allowCloseGesture = data.getBooleanExtra("allowCloseGesture", false);
            if (allowCloseGesture) {
                gestureToggle = false;
                gestureToggleOnIV.setVisibility(View.INVISIBLE);
                gestureToggleOffIV.setVisibility(View.VISIBLE);
                gestureToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_off);
                gestureTogglePromptTV.setText(getString(R.string.turn_off));
                PreferenceUtil.getInstance(mContext).saveBoolean("gestureToggle", gestureToggle);
            }
        }
    }
    
	private void showPopupWindow() {
        final PopupWindow popupWindow = new PopupWindow(cardView, captureNumET.getWidth(), LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.getBackground().setAlpha(128);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(captureNumET);

        cardNumberLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                             
            	picNum = position;
                                   
                                                                                                      
            	sendRequest(7, String.valueOf(picNum+1), 0);
                popupWindow.dismiss();
            }
        });
    }
    
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());  
        int hour = calendar.get(Calendar.HOUR_OF_DAY);  
        int minute = calendar.get(Calendar.MINUTE);  
        picker = new TimePickerDialog(this, new TimeSetListener(), hour, minute, true);
        picker.show();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
    
    class TimeSetListener implements OnTimeSetListener {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        	time = hourOfDay+":"+minute;
                                    
        	Log.e("=======", "========== time = "+time);
        	if (captureToggle) {
        	    sendRequest(12, time, 0);
        	} else {
        	    sendRequest(12, time, 1);
        	}
        }
    }
    
    private void sendRequest(int queryCode, String time, int type) {
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
        if (null == MyApplication.mainClient) {
            return;
        }
        if (queryCode == 12) {             
            MyApplication.mainClient.setCaptureTime(MyApplication.phone, time, type, commandListener);
        } else if (queryCode == 88) {           
            MyApplication.mainClient.getCaptureNumber(MyApplication.deviceCode, commandListener);
        } else if (queryCode == 89) {             
            MyApplication.mainClient.getCaptureTime(MyApplication.deviceCode, commandListener);
        } else if (queryCode == 7) {
            MyApplication.mainClient.setCaptureNum(MyApplication.phone, Integer.valueOf(time), DateUtil.getcurrentDay(), commandListener);
        } else if (queryCode == 90) {
            MyApplication.mainClient.getCaptureParams(MyApplication.deviceCode, commandListener);
        }
    }
    
        
  
  
  
    private void setCapturePicNum (final int picNum) {
    	if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
        	MessageUtil.alertMessage(mContext, R.string.sys_network_error);
        	startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        	return;
        }
    	loginDialog.setMessage(getString(R.string.capture_number_setting));
    	loginDialog.show();
    	boolean isConnect = MyApplication.mainClient.isConnected();
    	Log.d("CameraSettingsActivity", "sendOrder()  connected = "+isConnect);
    	if (isConnect) {         
    		MyApplication.mainClient.setCaptureNum(MyApplication.phone, picNum, DateUtil.getcurrentDay(), commandListener);
    	} else {          
    		MyApplication.initTcpManager();
    		MyApplication.mainClient = MyApplication.tcpManager.getMainClient(MyApplication.phone, MyApplication.password, "1111111111111111", "66666666");
    		MyApplication.mainClient.userAuth(MyApplication.phone, new CommandListener() {
    			@Override
    			public void onTimeout(Frame src, Frame f) {
    				loginDialog.dismiss();
    				MessageUtil.alertMessage(mContext, R.string.network_timeout);
    				Log.e("CameraSettingsActivity", "sendOrder()  userAuth()  ��ͥ��ʿ���������ӳ�ʱ������");
    			}
    			
    			@Override
    			public int onReceive(Frame src, Frame f) {
    				if (null != f && SpliteUtil.getRuquestStatus(f.strData)) {
    					Log.e("CameraSettingsActivity", "sendOrder()  userAuth()  ��ͥ��ʿ���������ӳɹ�������");
    					MyApplication.mainClient.setCaptureNum(MyApplication.phone, picNum, DateUtil.getcurrentDay(), commandListener);
    				} else {
    					loginDialog.dismiss();
    					Log.e("CameraSettingsActivity", "sendOrder()  userAuth()  ��ͥ��ʿ����������ʧ�ܡ�����");
    				}
    				return 0;
    			}
    		});
    	}
    }
    
        
  
  
  
    private void setCaptureTime (final String time) {
    	if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
        	MessageUtil.alertMessage(mContext, R.string.sys_network_error);
        	startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        	return;
        }
    	loginDialog.setMessage(getString(R.string.capture_time_setting));
    	loginDialog.show();
    	boolean isConnect = MyApplication.mainClient.isConnected();
    	Log.d("CameraSettingsActivity", "sendOrder()  connected = "+isConnect);
    	if (MyApplication.mainClient.isConnected()) {         
    		MyApplication.mainClient.setCaptureTime(MyApplication.phone, time, 0, commandListener);
    	} else {          
    		MyApplication.initTcpManager();
    		MyApplication.mainClient = MyApplication.tcpManager.getMainClient(MyApplication.phone, MyApplication.password, "1111111111111111", "66666666");
    		MyApplication.mainClient.userAuth(MyApplication.phone, new CommandListener() {
    			@Override
    			public void onTimeout(Frame src, Frame f) {
    				loginDialog.dismiss();
    				MessageUtil.alertMessage(mContext, R.string.network_timeout);
    				Log.e("CameraSettingsActivity", "sendOrder()  userAuth()  ��ͥ��ʿ���������ӳ�ʱ������");
    			}
    			
    			@Override
    			public int onReceive(Frame src, Frame f) {
    				if (null != f && SpliteUtil.getRuquestStatus(f.strData)) {
    					Log.e("CameraSettingsActivity", "sendOrder()  userAuth()  ��ͥ��ʿ���������ӳɹ�������");
    					MyApplication.mainClient.setCaptureTime(MyApplication.phone, time, 0, commandListener);
    				} else {
    					loginDialog.dismiss();
    					Log.e("CameraSettingsActivity", "sendOrder()  userAuth()  ��ͥ��ʿ����������ʧ�ܡ�����");
    				}
    				return 0;
    			}
    		});
    	}
    }
    
}
