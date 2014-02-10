package com.hiibox.houseshelter.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.net.CommandResult;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.SpliteUtil;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.DateUtil;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;
import com.hiibox.houseshelter.view.TemperatureTrendView;

    
  
  
  
  
  
  
  
public class TemperatureAbnormalActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.root_layout) LinearLayout rootLayout;
    @ViewInject(id = R.id.trend_content) LinearLayout trendLayout;
    @ViewInject(id = R.id.current_date_tv) TextView currDateTV;
    @ViewInject(id = R.id.start_time_tv) TextView startTimeTV;
    @ViewInject(id = R.id.end_time_tv) TextView endTimeTV;
    @ViewInject(id = R.id.current_temperature_tv) TextView currTemperatureTV;
    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.phone_iv, click = "onClick") ImageView phoneIV;
    
    private int trendLayoutHeight = 0;
    private String emergencyContact = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_abnormal_layout);
        String time = getIntent().getStringExtra("time");             
        String[] timeStr = time.split(" ");
        currDateTV.setText(DateUtil.changeDateToYmd(time));
        endTimeTV.setText(timeStr[1].substring(0, timeStr[1].lastIndexOf(":")));
        startTimeTV.setText(DateUtil.getLastHalfAnHour(timeStr[1]));
        getContact();
        sendRequest(time);
        
                                          
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
        
        ViewTreeObserver hvto = trendLayout.getViewTreeObserver();
        hvto.addOnPreDrawListener(new OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (trendLayoutHeight == 0) {
                    trendLayoutHeight = trendLayout.getMeasuredHeight();
                    return false;
                }
                return true;
            }
        });
        
    }
    
    private void sendRequest(String time) {
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
        MyApplication.mainClient.getTemperatureAry(phone, time, new CommandListener() {
            @Override
            public void onTimeout(Frame src, Frame f) {
                handler.sendEmptyMessage(0);
            }
            
            @Override
            public int onReceive(Frame src, Frame f) {
                Log.d("TemperatureAbnormalActivity", "onReceive()  f = "+f);
                if (null != f) {
                    Log.d("TemperatureAbnormalActivity", "onReceive()  f.strData = "+f.strData+" ; f.aryData = "+f.aryData);
                    CommandResult result = new CommandResult();
                    result.praseTemperatureAry(f);
                    int temp[] = result.getTempAry();
                    if (null != temp && temp.length > 0) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = temp;
                        handler.sendMessage(msg);
                    }
                } else {
                    handler.sendEmptyMessage(2);
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
            if (msg.what == 1) {
                int[] temp = (int[]) msg.obj;
                TemperatureTrendView trendView = new TemperatureTrendView(mContext, GlobalUtil.mScreenWidth-100, trendLayoutHeight, temp);
                trendLayout.addView(trendView);
                currTemperatureTV.setText(temp[0]+getString(R.string.temperature_unit));
            } else if (msg.what == 0) {
                MessageUtil.alertMessage(mContext, R.string.network_timeout);
            } else if (msg.what == 2) {
                MessageUtil.alertMessage(mContext, R.string.no_data);
            }
        }
    };
    
    private void getContact() {
                                                               
        emergencyContact = PreferenceUtil.getInstance(mContext).getString("emergencyContact", null);
        if (StringUtil.isEmpty(emergencyContact)) {
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
            MyApplication.mainClient.queryEmergencyTelephone(phone, new CommandListener() {
                
                @Override
                public void onTimeout(Frame src, Frame f) {
                    
                }
                
                @Override
                public int onReceive(Frame src, Frame f) {
                    if (null != f) {
                        if (f.subCmd == 79) {
                            if (f.strData.startsWith("1")) {
                                return 0;
                            }
                            emergencyContact = SpliteUtil.getResult(f.strData);
                                                                                   
                            PreferenceUtil.getInstance(mContext).saveString("emergencyContact", emergencyContact.trim());
                        }
                    }
                    return 0;
                }
            });
        }
    }
    
    @SuppressWarnings("static-access")
    public void onClick(View v) {
        if (v == backIV) {
            MianActivity.getScreenManager().exitActivity(mActivity);
        } else if (v == phoneIV) {
            Intent intent = new Intent();
            intent.setAction(intent.ACTION_DIAL);
            if (StringUtil.isNotEmpty(emergencyContact)) {
                intent.setData(Uri.parse("tel:"+emergencyContact));
            } else {
                intent.setData(Uri.parse("tel:"+110));
            }
            startActivity(intent);
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
