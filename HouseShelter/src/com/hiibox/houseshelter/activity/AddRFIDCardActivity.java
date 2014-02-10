package com.hiibox.houseshelter.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.DropDownBoxAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.IBreakUtil;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class AddRFIDCardActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.upload_head_portrait_iv, click = "onClick") ImageView addPicIV;
    @ViewInject(id = R.id.submit_tv, click = "onClick") TextView submitTV;
    @ViewInject(id = R.id.rfid_card_et    ) EditText choseCardET;
    @ViewInject(id = R.id.name_et) EditText nameET;
    
    private DropDownBoxAdapter adapter = null;
    private List<String> rfidCardList = null;
    private View cardView = null;
    private ListView cardNumberLV = null;
    private Bitmap bitmap = null;
    private ProgressDialog dialog = null;
    private String rfidCard = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rfidCard = getIntent().getStringExtra("rfidCard");
        Log.d("AddRFIDCardActivity", "onCreate()  rfidCard = "+rfidCard);
        
        setContentView(R.layout.activity_add_rfid_card_layout);
        
        choseCardET.setText(rfidCard);
        adapter = new DropDownBoxAdapter(mContext);
        rfidCardList = new ArrayList<String>();
        for (int i = 0; i < 5; i ++) {
            rfidCardList.add("FD990234X"+i);
        }
        adapter.setList(rfidCardList);
        cardView = getLayoutInflater().inflate(R.layout.popupwindow_drop_down_layout, null);
        cardNumberLV = (ListView) cardView.findViewById(R.id.popup_lv);
        cardNumberLV.setAdapter(adapter);
        
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(getString(R.string.registering));
    }
    
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.back_iv:
                MianActivity.getScreenManager().exitActivity(mActivity);
                break;
            case R.id.upload_head_portrait_iv:
                startActivityForResult(new Intent(this, AddPictureActivity.class), 0x201);
                break;
            case R.id.submit_tv:
            	String nickname = nameET.getText().toString();       
                if (StringUtil.isEmpty(nickname)) {
                	setFocusable(nameET, R.string.hint_input_nickname);
                	return;
                }
                dialog.show();
                if (null == bitmap) {
                	prepare(rfidCard, nickname, "0".getBytes());
            	} else {
            		byte[] picFile = IBreakUtil.bmpToByteArray(bitmap, false);
            		Log.d("AddRFIDCardActivity", "addMember Click  fileSize = "+picFile.length);
            		prepare(rfidCard, nickname, picFile);
            	}
                break;
                                      
                                     
                         
            default:
                break;
        }
    }
    
        
  
  
  
  
  
    private void prepare (final String cardNum, final String nickname, final byte[] pic) {
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
		registerRfidCard(cardNum, nickname, pic);
    }

        
  
  
  
  
  
	private void registerRfidCard(String cardNum, String nickname, byte[] pic) {
		MyApplication.mainClient.regCard(1, MyApplication.phone, cardNum, nickname, pic, new CommandListener() {
			@Override
			public void onTimeout(Frame src, Frame f) {
				dialog.dismiss();
				Log.e("AddRFIDCardActivity", "addMember()   time out....");
                                                                    
				handler.sendEmptyMessage(0);
			}
			
			@SuppressWarnings("unused")
			@Override
			public int onReceive(Frame src, Frame f) {
				dialog.dismiss();
				Log.e("AddRFIDCardActivity", "addMember()   data = "+f.strData);
				if (null != f) {
				    if (f.strData.equals("0")) {
				        handler.sendEmptyMessage(3);
				    } else {
				        handler.sendEmptyMessage(2);
				    }
                                                     
                                                                         
                                     
                                                                      
                
                                     
         
				} else {
                                                                          
					handler.sendEmptyMessage(1);
				}
				return 0;
			}
		});
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			if (what == 0) {
				MessageUtil.alertMessage(mContext, R.string.network_timeout);
			} else if (what == 1) {
				MessageUtil.alertMessage(mContext, R.string.network_not_response);
			} else if (what == 2) {
				MessageUtil.alertMessage(mContext, R.string.register_failed);
			} else if (what == 3) {
				MessageUtil.alertMessage(mContext, R.string.register_success);
				Intent data = new Intent();
				data.putExtra("isAdd", true);
				setResult(RESULT_OK, data);
				MianActivity.getScreenManager().exitActivity(mActivity);
			}
		}
    };
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x201 && resultCode == RESULT_OK) {
            String path = data.getStringExtra("picturePath");
            if (StringUtil.isNotEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    bitmap = IBreakUtil.readBitMap(path);
                    addPicIV.setImageBitmap(bitmap);
                } else {
                    MessageUtil.alertMessage(mContext, R.string.the_picture_is_not_exist);
                }
            }
        }
    }
    
      















  
    
    public void setFocusable(EditText editText, int msg) {
        editText.setText("");
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
        Editable editable = editText.getText();
        Selection.setSelection(editable, 0);
        MessageUtil.alertMessage(mContext, msg);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
}
