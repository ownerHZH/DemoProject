package com.hiibox.houseshelter.activity;

import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;

public class LoginDialogActivity extends ShaerlocActivity {

    private String phone = null;
    private String password = null;
    
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    MyApplication.phone = phone;
                    MyApplication.password = password;
                    PreferenceUtil.getInstance(mContext).saveString("phone", phone);
                    PreferenceUtil.getInstance(mContext).saveString("password", password);
                    MyApplication.initTcpManager();
                    MyApplication.mainClient = MyApplication.tcpManager.getMainClient(phone, password, "1111111111111111", "66666666");
                    MyApplication.fileClient = MyApplication.tcpManager.getFileClient(phone);
                    startActivity(new Intent(LoginDialogActivity.this, HomepageActivity.class));
                    MianActivity.getScreenManager().exitAllActivityExceptOne();
                    break;
                case 3:
                    MessageUtil.alertMessage(mContext, R.string.receive_server_info_failed);
                    MianActivity.getScreenManager().exitActivity(mActivity);
                    break;
                case 4:
                    MessageUtil.alertMessage(mContext, R.string.network_error);
                    MianActivity.getScreenManager().exitActivity(mActivity);
                    break;
                case 5:
                    MessageUtil.alertMessage(mContext, R.string.network_not_response);
                    MianActivity.getScreenManager().exitActivity(mActivity);
                    break;
                case 6:
                    MessageUtil.alertMessage(mContext, R.string.network_timeout);
                    MianActivity.getScreenManager().exitActivity(mActivity);
                    break;
                default:
                    break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog_layout);
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");
        String imei = LocationUtil.getDrivenToken(mContext);
        MyApplication.initTcpManager();
        try {
            MyApplication.tcpManager.login(phone, password, imei, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}
