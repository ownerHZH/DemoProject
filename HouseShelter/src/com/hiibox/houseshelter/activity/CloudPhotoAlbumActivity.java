package com.hiibox.houseshelter.activity;

import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.CloudPhotoAlbumAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.listener.HandlerCommandListener;
import com.hiibox.houseshelter.net.CloudPhotoAlbumResult;
import com.hiibox.houseshelter.net.CloudPhotoAlbumResult.PhotoInfo;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.DateUtil;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class CloudPhotoAlbumActivity extends ShaerlocActivity    {

    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.date_tv, click = "onClick") TextView dateTV;
    @ViewInject(id = R.id.type_tv, click = "onClick") TextView typeTV;
                                                                                 
    @ViewInject(id = R.id.photo_album_lv) ListView photoAlbumLV;
    @ViewInject(id = R.id.root_layout) LinearLayout rootLayout;
    @ViewInject(id = R.id.progress_bar) ProgressBar progressBar;
    
    private CloudPhotoAlbumResult albumResult = null;
    private TreeMap<String, List<PhotoInfo>> map = null;
                                              
    private CloudPhotoAlbumAdapter adapter = null;
    private DatePickerDialog dateDialog = null;	
    private HandlerCommandListener commandListener = null;
    private String currDate = null;
                                        
                                       
    private final int DATE_DIALOG = 0;          
    private int year, month, day;
    
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (null != msg) {
				Frame[] f = (Frame[]) msg.obj;
				if (null == f) {
					return;
				}
				if (null == f[1]) {
					return;
				}
				if (msg.what == 0) {
				    if (f[1].subCmd == 39) {
	                    int rc = albumResult.prasePhotoAlbum(f[1]);
	                    if (albumResult.totalNumber == 0) {
	                        progressBar.setVisibility(View.GONE);
	                        MessageUtil.alertMessage(mContext, R.string.no_data);
	                        return;
	                    }
	                    Log.e("CloudPhotoAlbumActivity", "handleMessage()  rc = "+rc);
	                    if (rc == 0) {
	                        map = albumResult.getMap();
	                        adapter.setMap(map);
	                        photoAlbumLV.setAdapter(adapter);
	                        progressBar.setVisibility(View.GONE);
                                                   
	                    }
	                }
				} else {
				    progressBar.setVisibility(View.GONE);
				    MessageUtil.alertMessage(mContext, R.string.network_timeout);
				}
			}
		}
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_photo_album_layout);
        
        Calendar calendar = Calendar.getInstance();
         year = calendar.get(Calendar.YEAR);
         month = calendar.get(Calendar.MONTH);
         day = calendar.get(Calendar.DAY_OF_MONTH);
        currDate = getFormatDate(year, month, day);
        dateTV.setText(currDate);
          













  
        
                                                                               
        adapter = new CloudPhotoAlbumAdapter(mContext, mActivity, finalBitmap);
        commandListener = new HandlerCommandListener(handler);
        
        if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
        	MessageUtil.alertMessage(mContext, R.string.sys_network_error);
        	startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        }
        
        albumResult = new CloudPhotoAlbumResult();
        map = new TreeMap<String, List<PhotoInfo>>();
        queryPhotos(0x00, DateUtil.getcurrentDay());
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_DIALOG) {
            return new DatePickerDialog(this, new OnDateSetListener() {
                boolean firstTime = true;
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    if (firstTime) {
                        firstTime = false;
                        String date = getFormatDate(year, monthOfYear, dayOfMonth);
                        dateTV.setText(date);
                        queryItem(typeTV.getText().toString(), date);
                    }
                    removeDialog(DATE_DIALOG);
                }
            }, year, month, day);
        }
        return super.onCreateDialog(id);
    }
    
    private void queryPhotos(int queryCode, String time) {
        Log.e("CloudPhotoAlbumActivity", "queryPhotos()  queryCode = "+queryCode+" ; time = "+time);
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
		if (null != map) {
		    map.clear();
		    adapter.setMap(map);
		}
		if (null != albumResult) {
		    albumResult.clearMap();
		}
		progressBar.setVisibility(View.VISIBLE);
		MyApplication.fileClient.queryPhotos(phone, time, queryCode, commandListener);
    }
    
    private void queryItem(String type, String date) {
        Log.d("CloudPhotoAlbumActivity", "queryItem()  type = "+type+" ; date = "+date);
        String all = getString(R.string.all);
        if (all.equals(type) && currDate.equals(date)) {
            queryPhotos(0x00, DateUtil.getcurrentDay());
        } else if (all.equals(type) && !currDate.equals(date)) {
            queryPhotos(0x01, date);          
        } else if (!all.equals(type) && currDate.equals(date)) {
            if (type.equals(getString(R.string.invade))) {           
                queryPhotos(0x02, DateUtil.getcurrentDay());
            } else if (type.equals(getString(R.string.capture))) {           
                queryPhotos(0x03, DateUtil.getcurrentDay());
            }
        } else if (!all.equals(type) && !currDate.equals(date)) {
            if (type.equals(getString(R.string.invade))) {            
                queryPhotos(0x04, date);
            } else if (type.equals(getString(R.string.capture))) {
                queryPhotos(0x05, date);            
            }
        }
    }
    
    public void onClick(View v) {
        if (v == backIV) {
            MianActivity.getScreenManager().exitActivity(mActivity);
        } else if (v == dateTV) {
                                                                           
        	                     
        	showDialog(DATE_DIALOG);
        } else if (v == typeTV) {
        	Intent intent = new Intent(mActivity, FamilyMembersActivity.class);
        	intent.putExtra("selectedIndex", 3);
            startActivityForResult(intent, 0x201);
        }
    }
    
	@Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                                              
                                                                       
                                                            
    }
    
    @Override
    protected void onStop() {
        super.onStop();
                                                  
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Log.e("======", "=============");
    	if (requestCode == 0x202 && resultCode == RESULT_OK) {
    		if (null != data) {
    			Log.e("======", "============="+data.getBooleanExtra("isDelete", false));
    			if (data.getBooleanExtra("isDelete", false)) {
        			queryPhotos(0x00, DateUtil.getcurrentDay());
        		}
    		}
    	} else if (requestCode == 0x201 && resultCode == RESULT_OK) {
            String member = data.getStringExtra("member");
            if (StringUtil.isNotEmpty(member)) {
                typeTV.setText(member);
                queryItem(member, dateTV.getText().toString());
            }
        }
    }

	  











  
	
	private String getFormatDate(int year, int month, int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("/");
        if (month < 9) {
            sb.append("0");
        }
        sb.append((month+1)).append("/");
        if (day < 10) {
            sb.append("0");
        }
        sb.append(day);
        return sb.toString();
    }

}
