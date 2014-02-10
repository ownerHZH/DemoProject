package com.hiibox.houseshelter.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.AddressAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.listener.HandlerCommandListener;
import com.hiibox.houseshelter.net.DeviceInfoResult;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.SpliteUtil;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class ManageAddressActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.root_layout) RelativeLayout rootLayout;
    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.add_address_iv, click = "onClick") ImageView addAddressIV;
    @ViewInject(id = R.id.address_list, itemClick = "onItemClick") ListView addressLV;
    @ViewInject(id = R.id.progress_bar) ProgressBar progressBar;
    
    private AddressAdapter adapter = null;
    private List<Map<String, Object>> list = null;
    private int defaultAddrId = 0;                 
    private HandlerCommandListener commandListener = null;
    private String deviceCode = null;
    private EditText et = null;
    private int position = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address_layout);
        list = new ArrayList<Map<String,Object>>();
        adapter = new AddressAdapter(mContext, this);
        commandListener = new HandlerCommandListener(handler);
        deviceCode = MyApplication.deviceCode;
        if (StringUtil.isEmpty(deviceCode)) {
            sendRequest(80, null, null);
        }
        sendRequest(82, null, null);
        et = new EditText(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = 10;
        params.rightMargin = 10;
        et.setLayoutParams(params);
    }
    
    public void onClick(View v) {
        if (v == backIV) {
            MianActivity.getScreenManager().exitActivity(mActivity);
        } else if (v == addAddressIV) {
            startActivityForResult(new Intent(this, AddAddressActivity.class), 0x401);
        }
    }
    
    public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {
        showPromptDialog(position);
                                           
                      
                   
                                        
                                          
            
    }

    private void showPromptDialog(final int position) {
        Map<String, Object> map = list.get(position);
        final String deviceNumber = (String) map.get("deviceNumber"); 
        final String authCode = (String) map.get("authCode"); 
        Dialog dialog = new AlertDialog.Builder(this)
        .setMessage(getString(R.string.change_default_address) + map.get("address"))
        .setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(mActivity, AuthCodeDialogActivity.class);
                intent.putExtra("deviceNumber", deviceNumber);
                intent.putExtra("position", position);
                startActivityForResult(intent, 0x105);
            }
        })
        .setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
        .create();
        dialog.show();
    }
    
    private void sendRequest(int subCmd, final String deviceNumber, final String authCode) {
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
		if (subCmd == 71) {
		    MyApplication.mainClient.switchTerminal(phone, deviceNumber, authCode, commandListener);
		} else if (subCmd == 82) {
		    MyApplication.mainClient.getDeviceInfo(phone, commandListener);
		} else if (subCmd == 80) {
		    MyApplication.mainClient.getDeviceCode(phone, commandListener);
		} else if (subCmd == 87) {
			MyApplication.mainClient.deleteDevice(phone, deviceNumber, authCode, commandListener);
		}
    }
    
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
                Log.d("ManageAddressActivity", "handlerMessage()  subCmd = "+subCmd+" ; data = "+ret+";");
                if (subCmd == 71) {
                    if (ret.equals("0")) {
                        for (int i = 0; i < list.size(); i ++) {
                            Map<String, Object> m = list.get(i);
                            if (i == defaultAddrId) {
                                MyApplication.deviceCode = (String) m.get("deviceNumber");
                                m.put("defaultAddr", true);
                                list.set(i, m);
                            } else {
                                m.put("defaultAddr", false);
                                list.set(i, m);
                            }
                        }
                        adapter.setList(list);
                    } else {
                        MessageUtil.alertMessage(mContext, R.string.change_device_failed);
                    }
                } else if (subCmd == 82) {
                    if (ret.startsWith("1")) {
                        MessageUtil.alertMessage(mContext, R.string.no_data);
                        return;
                    } else if (SpliteUtil.getRuquestStatus(ret)) {
                        DeviceInfoResult infoResult = new DeviceInfoResult(deviceCode);
                        infoResult.praseDeviceInfo(frame[1]);
                        list = infoResult.getDeviceList();
                        if (null != list && list.size() > 0) {
                            adapter.setList(list);
                            addressLV.setAdapter(adapter);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                } else if (subCmd == 80) {
                    if (SpliteUtil.getRuquestStatus(ret)) {
                        MyApplication.deviceCode = SpliteUtil.getResult(ret);
                        deviceCode = MyApplication.deviceCode;
                    }
                } else if (subCmd == 87) {
                	if (ret.equals("0")) {
                		if (null != list && list.size() > 0) {
                			if (position >= 0) {
                				list.remove(position);
                				adapter.setList(list);
                				MessageUtil.alertMessage(mContext, R.string.success_delete_device);
                			}
                		}
                	} else {
                		MessageUtil.alertMessage(mContext, R.string.failed_delete_device);
                	}
                }
            } else if (msg.what == -1) {
                
            }
		}
    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x401 && resultCode == RESULT_OK) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("address", data.getStringExtra("address"));
            map.put("deviceNumber", data.getStringExtra("deviceNumber"));
            map.put("authCode", data.getStringExtra("authCode"));
            map.put("defaultAddr", data.getBooleanExtra("defaultAddr", false));
            list.add(map);
            adapter.setList(list);
        } else if (requestCode == 0x104 && resultCode == RESULT_OK) {
        	if (null != data) {
        		String deviceNumber = data.getStringExtra("deviceNumber");
        		String authCode = data.getStringExtra("authCode");
        		position = data.getIntExtra("position", -1);
        		Log.d("ManageAddressActivity", "onActivityResult()  deviceNumber = "+deviceNumber+" ; authCode = "+authCode+" ; position = "+position);
        		if (StringUtil.isNotEmpty(deviceNumber) && StringUtil.isNotEmpty(authCode)) {
        			sendRequest(87, deviceNumber, authCode);
        		}
        	}
        } else if (requestCode == 0x105 && resultCode == RESULT_OK) {
            if (null != data) {
                String deviceNumber = data.getStringExtra("deviceNumber");
                String authCode = data.getStringExtra("authCode");
                position = data.getIntExtra("position", -1);
                Log.d("ManageAddressActivity", "onActivityResult()  deviceNumber = "+deviceNumber+" ; authCode = "+authCode+" ; position = "+position);
                if (StringUtil.isNotEmpty(deviceNumber) && StringUtil.isNotEmpty(authCode)) {
                    defaultAddrId = position;
                    sendRequest(71, deviceNumber, authCode);
                }
            }
        }
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
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	handler.removeCallbacks(null);
    }
    
}
