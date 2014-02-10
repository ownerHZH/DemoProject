package com.hiibox.houseshelter.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class SelfCenterActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.root_layout) LinearLayout rootLayout;
    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.modify_nickname_iv, click = "onClick") ImageView nicknameIV;
    @ViewInject(id = R.id.modify_emergency_contacts_iv, click = "onClick") ImageView contactsIV;
    @ViewInject(id = R.id.exit_account_iv, click = "onClick") ImageView exitIV;
    @ViewInject(id = R.id.phone_tv) TextView phoneTV;
                                                       
                                                                                                
    @ViewInject(id = R.id.nickname_et) EditText nicknameET;
    @ViewInject(id = R.id.emergency_contacts_et) EditText contactsET;
    @ViewInject(id = R.id.password_setting_tv, click = "onClick") TextView passwordSettingTV;
    @ViewInject(id = R.id.new_or_maintain_address_tv, click = "onClick") TextView addressTV;
    @ViewInject(id = R.id.maintain_rfid_card_tv, click = "onClick") TextView rfidCardTV;
    @ViewInject(id = R.id.camera_setting_tv, click = "onClick") TextView cameraSettingTV;
    @ViewInject(id = R.id.advice_feedback_tv, click = "onClick") TextView feedbackTV;
    @ViewInject(id = R.id.about_tv, click = "onClick") TextView aboutTV;
    @ViewInject(id = R.id.gesture_toggle_btn_layout, click = "onClick") LinearLayout alarmToggleLayout;
    @ViewInject(id = R.id.gesture_toggle_btn_prompt_tv) TextView alarmToggleTV;
    @ViewInject(id = R.id.gesture_toggle_btn_on_iv) ImageView alarmToggleOnIV;
    @ViewInject(id = R.id.gesture_toggle_btn_off_iv) ImageView alarmToggleOffIV;
    @ViewInject(id = R.id.home_toggle_btn_layout, click = "onClick") LinearLayout homeToggleLayout;
    @ViewInject(id = R.id.home_toggle_btn_prompt_tv) TextView homeToggleTV;
    @ViewInject(id = R.id.home_toggle_btn_on_iv) ImageView homeToggleOnIV;
    @ViewInject(id = R.id.home_toggle_btn_off_iv) ImageView homeToggleOffIV;
    @ViewInject(id = R.id.defence_toggle_btn_layout, click = "onClick") LinearLayout defenceToggleLayout;
    @ViewInject(id = R.id.defence_toggle_btn_prompt_tv) TextView defenceToggleTV;
    @ViewInject(id = R.id.defence_toggle_btn_on_iv) ImageView defenceToggleOnIV;
    @ViewInject(id = R.id.defence_toggle_btn_off_iv) ImageView defenceToggleOffIV;
    
    private String nickname = null;       
    private String contacts = null;           
    private boolean modifyNickname = false;               
    private boolean modifyContacts = false;                   
    private boolean alarmToggle = true;
    private boolean homeToggle = true;
    private boolean defenceToggle = true;
    
    @SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                Frame[] frame = (Frame[]) msg.obj;
                if (null == frame) {
                	return;
                }
                if (null == frame[0] && null == frame[1]) {
                	return;
                }
                int subCmd = frame[0].subCmd;
                String ret = frame[1].strData;
                switch (subCmd) {
                    case 73:           
                    	if (SpliteUtil.getRuquestStatus(ret)) {
                    		MyApplication.userLevel = Integer.valueOf(SpliteUtil.getResult(ret));
                                        
                    	}
                        Log.d("SelfCenterActivity", "[��ѯ�û���Ϣ] �յ���Ӧ :" + ret);
                        break;
                    case 77:           
                    	Log.d("SelfCenterActivity", "[��ѯ�û���Ϣ] �յ���Ӧ :" + ret);
                    	if (ret.startsWith("1")) {
                    	    return;
                    	}
                    	if (!ret.contains("\t")) {
                    	    return;
                    	}
                    	String[] result = ret.split("\t");
                    	if (result[0].equals("0")) {
                    	    if (result.length == 2) {
                    	        nicknameET.setText(result[1].trim());
                    	        nickname = result[1].trim();
                    	    } else if (result.length == 3) {
                    	        nicknameET.setText(result[1].trim());
                    	        contactsET.setText(result[2].trim());
                    	        nickname = result[1].trim();
                                contacts = result[2].trim();
                                PreferenceUtil.getInstance(mContext).saveString("emergencyContact", contacts);
                                                                               
                    	    }
                    	}
                    	break;
                    case 78:           
                        Log.d("SelfCenterActivity", "[�޸��û���Ϣ] �յ���Ӧ :" + ret);
                        if (ret.equals("0")) {
                            MessageUtil.alertMessage(mContext, R.string.modify_msg_successful);
                            PreferenceUtil.getInstance(mContext).saveString("emergencyContact", contacts);
                        } else {
                            MessageUtil.alertMessage(mContext, R.string.modify_msg_failure);
                        }
                        break;
                    case 79:             
                        Log.d("SelfCenterActivity", "[��ѯ������ϵ�绰] �յ���Ӧ :" + ret);
                        break;
                    default:
                        break;
                }
            } else {
                      
            }
        }
        
    };
    
    private HandlerCommandListener commandListener = new HandlerCommandListener(handler);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_center_layout);
        
        String phone = PreferenceUtil.getInstance(getApplicationContext()).getString("phone", null);
        phoneTV.setText(phone);
        
        alarmToggle = PreferenceUtil.getInstance(mContext).getBoolean("alarmMessage", true);
        homeToggle = PreferenceUtil.getInstance(mContext).getBoolean("goHomeMessage", true);
        defenceToggle = PreferenceUtil.getInstance(mContext).getBoolean("defenceMessage", true);
        if (!alarmToggle) {
        	closeAlarmToggle();
        }
        if (!homeToggle) {
        	closeHomeToggle();
        }
        if (!defenceToggle) {
        	closeDefenceToggle();
        }
        
        if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
        	MessageUtil.alertMessage(mContext, R.string.sys_network_error);
        	startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        	return;
        }
                          
        sendRequest(77, null, null);
                                       
    }

	  












  
    

        
  
  
  
	private void sendRequest(final int subCmd, String nickname, String emergencyTel) {
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
		switch (subCmd) {
			case 73:
				MyApplication.mainClient.queryUserLevel(phone, commandListener);
				break;
			case 77:           
				MyApplication.mainClient.queryUserInfo(phone, commandListener);
				break;
			case 78:           
				MyApplication.mainClient.modifyUserInfo(phone, nickname, emergencyTel, commandListener);
				break;
			case 79:             
				MyApplication.mainClient.queryEmergencyTelephone(phone, commandListener);
				break;
			default:
				break;
		}
	}
    
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.back_iv:
                MianActivity.getScreenManager().exitActivity(mActivity);
                return;
            case R.id.modify_nickname_iv:
                if (!modifyNickname) {
                	nicknameET.setEnabled(true);
                	nicknameET.requestFocus();
                	nicknameET.setSelection(nicknameET.getText().toString().length());
                	InputMethodManager imm = (InputMethodManager) nicknameET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
                	imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    nicknameIV.setBackgroundResource(R.drawable.confirm_iv);
                    modifyNickname = true;
                } else {
                	nicknameET.setEnabled(false);
                    nicknameIV.setBackgroundResource(R.drawable.pencel);
                    modifyNickname = false;
                    String _nickname = nicknameET.getText().toString();
                    String _contacts = contactsET.getText().toString();
                    if (nickname.equals(_nickname)) {
                    	return;
                    }
                    sendRequest(78, _nickname, _contacts);
                }
                break;
            case R.id.modify_emergency_contacts_iv:
                if (!modifyContacts) {
                	contactsET.setEnabled(true);
                	contactsET.requestFocus();
                	contactsET.setSelection(contactsET.getText().toString().trim().length());
                	InputMethodManager imm = (InputMethodManager) contactsET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
                	imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                    contactsIV.setBackgroundResource(R.drawable.confirm_iv);
                    modifyContacts = true;
                } else {
                	contactsET.setEnabled(false);
                    contactsIV.setBackgroundResource(R.drawable.pencel);
                    modifyContacts = false;
                    String _nickname = nicknameET.getText().toString();
                    String _contacts = contactsET.getText().toString();
                    if (contacts.equals(_contacts)) {
                    	return;
                    }
                    contacts = _contacts;
                    sendRequest(78, _nickname, _contacts);
                            
                                                                                                       
                                                  
                             
         
                }
                break;
                                             
                                                                                  
                         
            case R.id.password_setting_tv:
                startActivity(new Intent(this, PasswordSettingActivity.class));
                break;
            case R.id.new_or_maintain_address_tv:
                startActivity(new Intent(this, ManageAddressActivity.class));
                break;
            case R.id.maintain_rfid_card_tv:
                startActivity(new Intent(this, ManageRFIDCardActivity.class));
                break;
            case R.id.camera_setting_tv:
                startActivity(new Intent(this, CloudEyesSettingsActivity.class));
                break;
            case R.id.advice_feedback_tv:
                startActivity(new Intent(this, AdviceFeedbackActivity.class));
                break;
            case R.id.about_tv:
                startActivity(new Intent(mContext, AboutActivity.class));
                break;
            case R.id.gesture_toggle_btn_layout:
                if (alarmToggle) {               
                    alarmToggle = false;
                    closeAlarmToggle();
                } else {               
                    alarmToggle = true;
                    openAlarmToggle();
                }
                PreferenceUtil.getInstance(mContext).saveBoolean("alarmMessage", alarmToggle);
                break;
            case R.id.home_toggle_btn_layout:
                if (homeToggle) {
                    homeToggle = false;
                    closeHomeToggle();
                } else {
                    homeToggle = true;
                    openHomeToggle();
                }
                PreferenceUtil.getInstance(mContext).saveBoolean("goHomeMessage", homeToggle);
                break;
            case R.id.defence_toggle_btn_layout:
                if (defenceToggle) {
                    defenceToggle = false;
                    closeDefenceToggle();
                } else {
                    defenceToggle = true; 
                    openDefenceToggle();
                }
                PreferenceUtil.getInstance(mContext).saveBoolean("defenceMessage", defenceToggle);
                break;
            case R.id.exit_account_iv:
            	MyApplication.phone = null;
                MyApplication.password = null;
                PreferenceUtil.getInstance(mContext).saveBoolean("exitApp", true);
            	PreferenceUtil.getInstance(mContext).remove("phone");
            	PreferenceUtil.getInstance(mContext).remove("password");
            	PreferenceUtil.getInstance(mContext).remove("gestureToggle");
            	PreferenceUtil.getInstance(mContext).remove("captureToggle");
            	PreferenceUtil.getInstance(mContext).remove("gestureTracks");
            	PreferenceUtil.getInstance(mContext).remove("buzzerSwitch");
            	PreferenceUtil.getInstance(mContext).remove("alarmMessage");
            	PreferenceUtil.getInstance(mContext).remove("goHomeMessage");
            	PreferenceUtil.getInstance(mContext).remove("defenceMessage");
            	PreferenceUtil.getInstance(mContext).remove("captureNumbers");
            	PreferenceUtil.getInstance(mContext).remove("captureTime");
            	
            	if (null != MyApplication.mainClient) {
                	MyApplication.mainClient.close();
                	MyApplication.mainClient = null;
                }
                if (null != MyApplication.fileClient) {
                	MyApplication.fileClient.close();
                    MyApplication.fileClient = null;
                }
                stopService(new Intent(mContext, PushMessageService.class));
                startActivity(new Intent(mActivity, LoginActivity.class));
                MianActivity.getScreenManager().exitAllActivityExceptOne();
                break;
            default:
                break;
        }
    }

	private void openDefenceToggle() {
		defenceToggleOnIV.setVisibility(View.VISIBLE);
		defenceToggleOffIV.setVisibility(View.INVISIBLE);
		defenceToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_on);
		defenceToggleTV.setText(getString(R.string.turn_on));
	}

	private void closeDefenceToggle() {
		defenceToggleOnIV.setVisibility(View.INVISIBLE);
		defenceToggleOffIV.setVisibility(View.VISIBLE);
		defenceToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_off);
		defenceToggleTV.setText(getString(R.string.turn_off));
	}

	private void openHomeToggle() {
		homeToggleOnIV.setVisibility(View.VISIBLE);
		homeToggleOffIV.setVisibility(View.INVISIBLE);
		homeToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_on);
		homeToggleTV.setText(getString(R.string.turn_on));
	}

	private void closeHomeToggle() {
		homeToggleOnIV.setVisibility(View.INVISIBLE);
		homeToggleOffIV.setVisibility(View.VISIBLE);
		homeToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_off);
		homeToggleTV.setText(getString(R.string.turn_off));
	}

	private void openAlarmToggle() {
		alarmToggleOnIV.setVisibility(View.VISIBLE);
		alarmToggleOffIV.setVisibility(View.INVISIBLE);
		alarmToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_on);
		alarmToggleTV.setText(getString(R.string.turn_on));
	}

	private void closeAlarmToggle() {
		alarmToggleOnIV.setVisibility(View.INVISIBLE);
		alarmToggleOffIV.setVisibility(View.VISIBLE);
		alarmToggleLayout.setBackgroundResource(R.drawable.bg_toggle_btn_off);
		alarmToggleTV.setText(getString(R.string.turn_off));
	}
    
	@Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(
            ImageOperation.readBitMap(mContext, R.drawable.bg_app));
        rootLayout.setBackgroundDrawable(bitmapDrawable);
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        rootLayout.setBackgroundDrawable(null);
    }
    
                
                                                           
                                            
                                                                  
                                                                 
                   
      
                                            
     
    
    @SuppressWarnings("unused")
	private void hideSoftInput(EditText et) {
        try {
            Class<EditText> cls = EditText.class;
            java.lang.reflect.Method setSoftInputShownOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            setSoftInputShownOnFocus.setAccessible(true);
            setSoftInputShownOnFocus.invoke(et, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
