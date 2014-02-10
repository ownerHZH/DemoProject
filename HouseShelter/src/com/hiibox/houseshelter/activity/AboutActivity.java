package com.hiibox.houseshelter.activity;

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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.SpliteUtil;
import com.hiibox.houseshelter.net.TCPServiceClientV2.CommandListener;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.StringUtil;

public class AboutActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.about_web) WebView webView;
    @ViewInject(id = R.id.root_layout) RelativeLayout rootLayout;
    @ViewInject(id = R.id.progress_bar) ProgressBar progressBar;
    @ViewInject(id = R.id.back_iv, click = "onClick") ImageView backIV;
    
    private ProgressDialog dialog = null;
    
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage(getString(R.string.dialog_message_loading_data));
        dialog.setCancelable(true);
        
        setContentView(R.layout.activity_about_layout);
        
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLightTouchEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAllowFileAccess(true);             
                                                                   
        webView.setBackgroundColor(0);
        webView.getBackground().setAlpha(0);
        
        sendRequest();
    }
    
    private void sendRequest() {
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
        MyApplication.mainClient.getAboutInfo(phone, new CommandListener() {
            @Override
            public void onTimeout(Frame src, Frame f) {
                handler.sendEmptyMessage(0);
                dialog.dismiss();
            }
            
            @Override
            public int onReceive(Frame src, Frame f) {
                dialog.dismiss();
                if (null != f) {
                    if (f.strData.startsWith("1")) {
                        handler.sendEmptyMessage(1);
                        return 0;
                    }
                    Message message = new Message();
                    message.what = 2;
                    message.obj = SpliteUtil.getResult(f.strData);
                    handler.sendMessage(message);
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
            if (msg.what == 0) {
                progressBar.setVisibility(View.GONE);
                MessageUtil.alertMessage(mContext, R.string.network_timeout);
            } else if (msg.what == 1) {
                progressBar.setVisibility(View.GONE);
                MessageUtil.alertMessage(mContext, R.string.no_data);
            } else if (msg.what == 2) {
                progressBar.setVisibility(View.GONE);
                String url = (String) msg.obj;
                Log.i("AboutActivity", "handleMessage()  url = "+url);
                webView.loadUrl(url);
            }
        }
        
    };
    
    public void onClick(View v) {
        if (v == backIV) {
            MianActivity.getScreenManager().exitActivity(mActivity);
        }
    }
    
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(
            ImageOperation.readBitMap(mContext, R.drawable.bg_app));
        rootLayout.setBackgroundDrawable(bitmapDrawable);
    };
    
    @Override
    protected void onStop() {
        super.onStop();
        rootLayout.setBackgroundDrawable(null);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    };
    
}
