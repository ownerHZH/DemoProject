package com.hiibox.houseshelter.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
public class AdviceFeedbackActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    @ViewInject(id = R.id.send_feedback_tv, click = "onClick") TextView sendTV;
    @ViewInject(id = R.id.advice_et) EditText adviceET;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_feedback_layout);
    }
    
    public void onClick(View v) {
        if (v == backIV) {
            MianActivity.getScreenManager().exitActivity(mActivity);
        } else if (v == sendTV) {
            String advice = adviceET.getText().toString().trim();
            if (StringUtil.isEmpty(advice)) {
                setFocusable(adviceET, R.string.prompt_input_feedback_msg);
                return;
            }
            sendRequest(advice);
        }
    }
    
        
  
  
  
    private void sendRequest(String advice) {
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
        MessageUtil.alertMessage(mContext, R.string.upload_feedback_info);
        MyApplication.mainClient.uploadFeedBack(phone, advice, new CommandListener() {
            @Override
            public void onTimeout(Frame src, Frame f) {
                handler.sendEmptyMessage(0);
            }
            @SuppressWarnings("unused")
            @Override
            public int onReceive(Frame src, Frame f) {
                Log.d("AdviceFeedbackActivity", "onReceive()  data = "+f.strData);
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
                MessageUtil.alertMessage(mContext, R.string.operate_failed);
            } else if (msg.what == 3) {
                MessageUtil.alertMessage(mContext, R.string.feed_back_success);
                MianActivity.getScreenManager().exitActivity(mActivity);
            }
        }
    };
    
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
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
}
