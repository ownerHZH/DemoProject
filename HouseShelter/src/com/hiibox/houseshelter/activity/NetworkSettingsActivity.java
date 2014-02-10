package com.hiibox.houseshelter.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
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
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableRow;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.DropDownBoxAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.StringUtil;
import com.tutk.IOTC.AVIOCTRLDEFs;
import com.tutk.IOTC.Camera;
import com.tutk.IOTC.IRegisterIOTCListener;
import com.tutk.IOTC.Packet;
import com.zg.camera.MyCamera;

    
  
  
  
  
  
  
  
public class NetworkSettingsActivity extends ShaerlocActivity implements IRegisterIOTCListener {

    @ViewInject(id = R.id.root_layout) RelativeLayout rootLayout;
    @ViewInject(id = R.id.cancel_iv, click = "onClick") ImageView cancelIV;
    @ViewInject(id = R.id.confirm_iv, click = "onClick") ImageView confirmIV;
    @ViewInject(id = R.id.two_dimension_code_iv, click = "onClick") ImageView codeIV;
    @ViewInject(id = R.id.device_code_et) EditText deviceCodeET;
                                                               
    @ViewInject(id = R.id.wifi_drop_down_box_et, click = "onClick") EditText wifiET;
    @ViewInject(id = R.id.password_et) EditText passwordET;
    @ViewInject(id = R.id.wifi_layout) TableRow wifiLayout;
    @ViewInject(id = R.id.password_layout) TableRow passwordLayout;
    @ViewInject(id = R.id.progress_bar) ProgressBar progressBar;
    
    private DropDownBoxAdapter adapter = null;
    private List<String> wifiNameList = null;
    private View cardView = null;
    private ListView cardNumberLV = null;
    public static List<MyCamera> CameraList = new ArrayList<MyCamera>();
    private MyCamera camera = null;
    private static List<AVIOCTRLDEFs.SWifiAp> m_wifiList = new ArrayList<AVIOCTRLDEFs.SWifiAp>();
    private int mPosition = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_settings_layout);
        adapter = new DropDownBoxAdapter(mContext);
        wifiNameList = new ArrayList<String>();
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
    
    public void onClick(View v) {
        if (v == cancelIV) {
            MianActivity.getScreenManager().exitActivity(mActivity);
        } else if (v == confirmIV) {
        	if (wifiLayout.getVisibility() == View.VISIBLE && passwordLayout.getVisibility() == View.VISIBLE) {
        		String ssid = wifiET.getText().toString();
            	if (StringUtil.isEmpty(ssid)) {
            		MessageUtil.alertMessage(mContext, R.string.prompt_choose_wifi);
            		return;
            	}
            	String password = passwordET.getText().toString();
            	if (StringUtil.isEmpty(password)) {
            		MessageUtil.alertMessage(mContext, R.string.prompt_input_wifi_password);
            		return;
            	}
            	AVIOCTRLDEFs.SWifiAp wifi = m_wifiList.get(mPosition);
				camera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL,
						AVIOCTRLDEFs.IOTYPE_USER_IPCAM_SETWIFI_REQ,
						AVIOCTRLDEFs.SMsgAVIoctrlSetWifiReq.parseContent(
								wifi.ssid, password.getBytes(), wifi.mode,
								wifi.enctype));
            	Log.i("NetworkSettingsActivity", "onClick()  wifi.ssid = "+wifi.ssid);
        	} else {
        		String deviceCode = deviceCodeET.getText().toString().trim();
        		if (StringUtil.isEmpty(deviceCode)) {
            		MessageUtil.alertMessage(mContext, R.string.hint_input_device_code);
            		return;
            	}
                                                                      
                                                
                                                                                   
                        
                 
                                                                                                               
        		progressBar.setVisibility(View.VISIBLE);
        		MyCamera.init();
        		camera = new MyCamera("Camera", deviceCode, "admin");
        		camera.registerIOTCListener(this);
        		camera.connect(camera.getUID());
        		camera.start(0, "admin", camera.getPassword());
        		camera.sendIOCtrl(Camera.DEFAULT_AV_CHANNEL, AVIOCTRLDEFs.IOTYPE_USER_IPCAM_LISTWIFIAP_REQ, AVIOCTRLDEFs.SMsgAVIoctrlListWifiApReq.parseContent());
        	}
        } else if (v == wifiET) {
            showPopupWindow();
        } else if (v == codeIV) {
        	startActivityForResult(new Intent(this, ScanCodeActivity.class), 0x101);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x101 && resultCode == RESULT_OK) {
            deviceCodeET.setText(data.getStringExtra("code"));
        }
    }
    
	private void showPopupWindow() {
        if (null ==wifiNameList || wifiNameList.size() <= 0) {
            return;
        }
                                                                                                   
        final PopupWindow popupWindow = new PopupWindow(cardView, wifiET.getWidth(), LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.getBackground().setAlpha(128);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(wifiET);

        cardNumberLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	mPosition = position;
                String card = wifiNameList.get(position);
                wifiET.setText(card);
                popupWindow.dismiss();
            }
        });
    }

	@Override
	public void receiveChannelInfo(Camera arg0, int arg1, int arg2) {}

	@Override
	public void receiveFrameData(Camera arg0, int arg1, Bitmap arg2) {}

	@Override
	public void receiveFrameInfo(Camera arg0, int arg1, long arg2, int arg3,
			int arg4, int arg5, int arg6) {}

	@Override
	public void receiveIOCtrlData(final Camera cmr, int sessionChannel, int avIOCtrlMsgType, byte[] data) {
		if (cmr == camera) {
			Bundle bundle = new Bundle();
			bundle.putInt("sessionChannel", sessionChannel);
			bundle.putByteArray("data", data);
			Message msg = new Message();
			msg.what = avIOCtrlMsgType;
			msg.setData(bundle);
			cHandler.sendMessage(msg);
		}
		if (sessionChannel == 0x343) {
		    cHandler.sendEmptyMessage(0x343);
		}
		Log.i("NetworkSettingsActivity", "receiveIOCtrlData()  sessionChannel = "+sessionChannel+" ; avIOCtrlMsgType = "+avIOCtrlMsgType+" ; data = "+data.toString());
		                                                                                            
		                                                                                            
	}

	@Override
	public void receiveSessionInfo(Camera arg0, int arg1) {}
	
	@SuppressLint("HandlerLeak")
	private Handler cHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			byte[] data = bundle.getByteArray("data");
			switch (msg.what) {
				case AVIOCTRLDEFs.IOTYPE_USER_IPCAM_LISTWIFIAP_RESP:
					int cnt = Packet.byteArrayToInt_Little(data, 0);
					int size = AVIOCTRLDEFs.SWifiAp.getTotalSize();
					m_wifiList.clear();
					if (cnt > 0 && data.length >= 40) {
						int pos = 4;
						for (int i = 0; i < cnt; i++) {
							byte[] ssid = new byte[32];
							System.arraycopy(data, i * size + pos, ssid, 0, 32);
							byte mode = data[i * size + pos + 32];
							byte enctype = data[i * size + pos + 33];
							byte signal = data[i * size + pos + 34];
							byte status = data[i * size + pos + 35];
							m_wifiList.add(new AVIOCTRLDEFs.SWifiAp(ssid, mode, enctype, signal, status));
							wifiNameList.add(byteToString(ssid));
							Log.i("NetworkSettingsActivity", "cHandler  i = "+i+" ; ssid = "+byteToString(ssid));
						}
						wifiLayout.setVisibility(View.VISIBLE);
						passwordLayout.setVisibility(View.VISIBLE);
						adapter.setList(wifiNameList);
			            cardView = getLayoutInflater().inflate(R.layout.popupwindow_drop_down_layout, null);
			            cardNumberLV = (ListView) cardView.findViewById(R.id.popup_lv);
			            cardNumberLV.setAdapter(adapter);
			            progressBar.setVisibility(View.GONE);
					}
					break;
				case 0x343:
				    MessageUtil.alertMessage(mContext, R.string.setting_success);
				    MianActivity.getScreenManager().exitActivity(mActivity);
				    break;
				default:
					break;
			}
		}
	};
	
	private static String byteToString(byte[] data) {
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++) {
			if (data[i] == 0x0)
				break;
			sBuilder.append((char) data[i]);
		}
		return sBuilder.toString();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		cHandler.removeCallbacks(null);
	}
	
       
                  
          
                                   
                                                      
                                                  
                                                                                          
                                              
                                                
            
                                                                
                              
                                                   
                                                     
                                                                                                                                             
                
                                             
                                                                                                   
                                                                              
                                                
            
        
       
                                            
                                                                      
                                                                        
        
       
          
                    
                 
          
                                
                                   
                           
                
                                                                                          
                                                                                 
                                                                 
                                     
                                          
                                     
                                                    
                             
                    
                
                                     
                       
                                    
            
                            
        
    
}
