package com.hiibox.houseshelter.activity;

import java.util.ArrayList;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.AdvertisementAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class CloudPhotoAlbumDetailsActivity extends ShaerlocActivity implements OnPageChangeListener {

    @ViewInject(id = R.id.cloud_pictures_pager) ViewPager pager;
    @ViewInject(id = R.id.pic_date_tv) TextView dateTV;
    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.pre_iv, click = "onClick") ImageView preIV;
    @ViewInject(id = R.id.after_iv, click = "onClick") ImageView afterIV;
    @ViewInject(id = R.id.share_iv, click = "onClick") ImageView shareIV;
    @ViewInject(id = R.id.delete_iv, click = "onClick") ImageView deleteIV;
    @ViewInject(id = R.id.progress_bar) ProgressBar progressBar;
    
    private ArrayList<String> picUrlList = null;
    private ArrayList<String> picIdList = null;
    private int currPagerIndex = 0;
    private int picLen = 0;
    private String picUrl = null;
                                     
    private AdvertisementAdapter adapter = null;
    private boolean isDelete = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_photo_album_details_layout);
        setLargeScreenParams();
        Intent intent = getIntent();
        picUrlList = intent.getStringArrayListExtra("picUrlList");
        picIdList = intent.getStringArrayListExtra("picIdList");
        String picDate = intent.getStringExtra("picDate");
        picLen = picUrlList.size();
        dateTV.setText(picDate);
        adapter = new AdvertisementAdapter(mContext, picUrlList, finalBitmap);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isDelete) {
                Intent intent = new Intent();
                intent.putExtra("isDelete", isDelete);
                this.setResult(RESULT_OK, intent);
            }
            MianActivity.getScreenManager().exitActivity(mActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    public void onClick(View v) {
        int vId = v.getId();
        switch (vId) {
            case R.id.back_iv:
                if (isDelete) {
                    Intent intent = new Intent();
                    intent.putExtra("isDelete", isDelete);
                    this.setResult(RESULT_OK, intent);
                }
                MianActivity.getScreenManager().exitActivity(mActivity);
                break;
            case R.id.pre_iv:
                currPagerIndex --;
                if (currPagerIndex == -1) {
                    MessageUtil.alertMessage(mContext, R.string.the_first_picture);
                    currPagerIndex = 0;
                    return;
                }
                pager.setCurrentItem(currPagerIndex);
                break;
            case R.id.after_iv:
                currPagerIndex ++;
                if (currPagerIndex == picLen) {
                    MessageUtil.alertMessage(mContext, R.string.the_last_picture);
                    currPagerIndex = picLen - 1;
                    return;
                }
                pager.setCurrentItem(currPagerIndex);
                break;
            case R.id.share_iv:
                Intent intent = new Intent(mActivity, ShareActivity.class);
                picUrl = picUrlList.get(currPagerIndex);
                   










 







  
                intent.putExtra("picUrl", picUrl);
                startActivity(intent);
                break;
            case R.id.delete_iv:
                new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_delete_photo)
                    .setCancelable(true)
                    .setPositiveButton(R.string.positive, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (null != picIdList && picIdList.size() > 0) {
                                deleteIV.setEnabled(false);
                                deletePhotos(picIdList.get(currPagerIndex), currPagerIndex);
                            }
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.negative, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
                break;
            default:
                break;
        }
    }
    
    private void deletePhotos(String fileId, final int position) {
        Log.d("CloudPhotoAlbumDetailsActivity", "deletePhotos()  fileId = "+fileId+" ; position = "+position);
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
    	MyApplication.fileClient.deletePhoto(MyApplication.phone, fileId, new CommandListener() {
			@Override
			public void onTimeout(Frame src, Frame f) {
				handler.sendEmptyMessage(0);
			}
			@SuppressWarnings("unused")
            @Override
			public int onReceive(Frame src, Frame f) {
			    Log.d("CloudPhotoAlbumDetailsActivity", "deletePhotos()  onReceive : f.strData = "+f.strData+";");
				if (null != f) {
					if (f.strData.equals("0")) {
					    isDelete = true;
						Message msg = new Message();
						msg.what = 2;
						msg.arg1 = position;
						handler.sendMessage(msg);
					} else {
						handler.sendEmptyMessage(3);
					}
				} else {
					handler.sendEmptyMessage(1);
				}
				return 0;
			}
		});
    }
    
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				MessageUtil.alertMessage(mContext, R.string.network_timeout);
			} else if (msg.what == 1) {
				MessageUtil.alertMessage(mContext, R.string.network_not_response);
			} else if (msg.what == 2) {
				MessageUtil.alertMessage(mContext, R.string.operate_success);
				int position = msg.arg1;
				picIdList.remove(position);
				picUrlList.remove(position);
				adapter.setList(picUrlList);
				if (null != picUrlList && picUrlList.size() == 0) {
				    progressBar.setVisibility(View.GONE);
				}
			} else if (msg.what == 3) {
				MessageUtil.alertMessage(mContext, R.string.operate_failed);
			}
			deleteIV.setEnabled(true);
		}
    };
    
    private void setLargeScreenParams() {
        if (GlobalUtil.mScreenHeight > 854 && GlobalUtil.mScreenHeight <= 1280) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 700);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            pager.setLayoutParams(params);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {}

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    @Override
    public void onPageSelected(int position) {
                               
                                                                              
                                             
                                                                             
            
        currPagerIndex = position;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}
