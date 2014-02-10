package com.hiibox.houseshelter.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.RFIDCardAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.listener.HandlerCommandListener;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.MembersInfoResult;
import com.hiibox.houseshelter.net.SpliteUtil;
import com.hiibox.houseshelter.net.TCPMainClient;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class ManageRFIDCardActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.root_layout) RelativeLayout rootLayout;
    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.manage_address_tv) TextView titleTV;
    @ViewInject(id = R.id.add_address_iv, click = "onClick") ImageView addCardIV;
    @ViewInject(id = R.id.address_list, itemClick = "onItemClick") ListView rfidCardLV;
    @ViewInject(id = R.id.progress_bar) ProgressBar progressBar;
    
    private RFIDCardAdapter adapter = null;
    private ProgressDialog dialog = null;
    private List<MembersInfoResult> membersList = null;           
    private HandlerCommandListener commandListener = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_address_layout);
        titleTV.setText(getString(R.string.manage_rfid_card));
        adapter = new RFIDCardAdapter(this, finalBitmap);
        
        commandListener = new HandlerCommandListener(handler);
        membersList = new ArrayList<MembersInfoResult>();
        prepare(4);
                                                                                                 
                                                          
                                           
            
        
        dialog = new ProgressDialog(this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(getString(R.string.dialog_get_rfid_card));
    }
    
    public void onClick(View v) {
        if (v == backIV) {
            MianActivity.getScreenManager().exitActivity(mActivity);
        } else if (v == addCardIV) {
        	dialog.show();
        	prepare(5);
        }
    }
    
        
  
  
  
  
  
    private void prepare(int subCmd) {
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
		if (subCmd == 5) {
		    MyApplication.mainClient.regCard(0, MyApplication.phone, "", "", "".getBytes(), commandListener);
                                                                                                         
		} else if (subCmd == 4) {
		    if (null != membersList) {
		        membersList.clear();
		    }
		    MyApplication.mainClient.readCard(phone, new TCPMainClient.QueryWarnListener2(this.commandListener));
		}
                   
    }
    
    @SuppressWarnings("unused")
    private void getRfidCard() {
    	MyApplication.mainClient.regCard(0, MyApplication.phone, "", "", "".getBytes(), new CommandListener() {
    		@Override
    		public void onTimeout(Frame src, Frame f) {
    			dialog.dismiss();
    			Log.e("ManageRFIDCardActivity", "addMember()   onTimeout() time out....");
    			handler.sendEmptyMessage(0);
    		}
    		
    		@Override
    		public int onReceive(Frame src, Frame f) {
    			dialog.dismiss();    
    			Log.e("ManageRFIDCardActivity", "addMember()  onReceive()  data = "+f.strData);
    			if (null != f) {
    				if (SpliteUtil.getRuquestStatus(f.strData)) {
    					Intent intent = new Intent(ManageRFIDCardActivity.this, AddRFIDCardActivity.class);
    					intent.putExtra("rfidCard", SpliteUtil.getResult(f.strData));
    					startActivity(intent);
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
    
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
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
                Log.d("ManageRFIDCardActivity", "subCmd = "+subCmd+" ; data = " + ret);
                if (subCmd == 4) {
                    if (ret.equals("6")) {
                        progressBar.setVisibility(View.GONE);
                        MessageUtil.alertMessage(mContext, R.string.no_data);
                        return;
                    }
                    if (!ret.startsWith("0")) {
                        progressBar.setVisibility(View.GONE);
                        MessageUtil.alertMessage(mContext, R.string.get_data_failure);
                        return;
                    }
                    MembersInfoResult member = MembersInfoResult.parse(frame[1]);
                    if (null != member) {
                        int total = member.totalMembers;
                        int index = member.currIndex;
                        Log.i("ManageRFIDCardActivity", "[ɨ�迨Ƭ]  total = "+total+"; index = "+index);
                        membersList.add(member);
                        if (total-index == 1) {
                            progressBar.setVisibility(View.GONE);
                            adapter.setList(membersList);
                            rfidCardLV.setAdapter(adapter);
                        }
                    }
                } else if (subCmd == 5) {
                    if (null != dialog && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    if (SpliteUtil.getRuquestStatus(ret)) {
                        Intent intent = new Intent(ManageRFIDCardActivity.this, AddRFIDCardActivity.class);
                        intent.putExtra("rfidCard", SpliteUtil.getResult(ret));
                        startActivityForResult(intent, 0x107);
                    } else {
                      MessageUtil.alertMessage(mContext, R.string.request_rfid_card_failed);
                    }
                }
            } else {
                if (subCmd == 4) {
                    progressBar.setVisibility(View.GONE);
                } else if (subCmd == 5) {
                    if (null != dialog && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                Log.d("ManageRFIDActivity", "subCmd = "+subCmd+" : �յ���Ӧ��ʱ");
            }
                          
                      
                                                                    
                             
                                                                         
                             
                                                                             
       
		}
    };

    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x107 && resultCode == RESULT_OK) {
            if (null != data) {
                if (data.getBooleanExtra("isAdd", false)) {
                    prepare(4);
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
    	if (null != membersList) {
    	    membersList.clear();
    	    membersList = null;
    	}
    }
    
}
