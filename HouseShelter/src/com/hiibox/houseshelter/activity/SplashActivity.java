package com.hiibox.houseshelter.activity;

import java.io.IOException;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.VideoView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.ScreenUtil;
import com.hiibox.houseshelter.util.StringUtil;

    
  
  
  
  
  
  
  
@SuppressLint("HandlerLeak")
public class SplashActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.splash_iv) ImageView splashIV;
    @ViewInject(id = R.id.splash_video) VideoView splashVV;
    
    private String phone = null;
    private String password = null;
                                       
                                                  
    private long start = 0;
    private long end = 0;
    private int queryIndex = 0;
        
  
  
    private Animation mFadeIn;
    private Animation mFadeInScale;
    private Animation mFadeOut;
        
  
  
                                   
    
    private int status = -1;
    private String message = "";
    
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e("SplashActivity", "handleMessage()  status = "+msg.what);
            switch (msg.what) {
                case 0:
                    MyApplication.phone = phone;
                    MyApplication.password = password;
                    break;
                case 1:
                    message = (String) msg.obj;
                    if (status == 9) {
                        MessageUtil.alertMessage(mContext, getString(R.string.login_failed) + message);
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        MianActivity.getScreenManager().exitActivity(mActivity);
                    } else {
                        status = 1;
                    }
                                                                               
                                                                                
                    break;
                case 2:
                                      
                    MyApplication.phone = phone;
                    MyApplication.password = password;
                    MyApplication.initTcpManager();
                    MyApplication.mainClient = MyApplication.tcpManager.getMainClient(phone, password, "1111111111111111", "66666666");
                    MyApplication.fileClient = MyApplication.tcpManager.getFileClient(phone);
                    if (status == 9) {
                        if (queryIndex == 1 || queryIndex == 2) {
                            Intent intent = new Intent(mContext, ImprintingActivity.class);
                            intent.putExtra("queryIndex", queryIndex);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(mContext, HomepageActivity.class));
                        }
                        MianActivity.getScreenManager().exitActivity(mActivity);
                    } else {
                        status = 2;
                    }
                    
                                                              
                                                                                        
                                                                   
                                               
                             
                                                                                     
                      
                                                                                
                    break;
                case 3:
                    MessageUtil.alertMessage(mContext, R.string.receive_server_info_failed);
                    break;
                case 4:
                    MessageUtil.alertMessage(mContext, R.string.network_error);
                    break;
                case 5:
                    MessageUtil.alertMessage(mContext, R.string.network_not_response);
                    break;
                case 6:
                    MessageUtil.alertMessage(mContext, R.string.network_timeout);
                    break;
                case 9:
                    Log.e("SplashActivity", "onAnimationEnd()  start="+start+" ; end="+end+" ; status="+status+" ; diff="+(end-start)/1000);
                    if (status == 1) {
                        MessageUtil.alertMessage(mContext, getString(R.string.login_failed) + message);
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        MianActivity.getScreenManager().exitActivity(mActivity);
                    } else if (status == 2) {
                        if (queryIndex == 1 || queryIndex == 2) {
                            Intent intent = new Intent(mContext, ImprintingActivity.class);
                            intent.putExtra("queryIndex", queryIndex);
                            startActivity(intent);
                        } else {
                            startActivity(new Intent(mContext, HomepageActivity.class));
                        }
                        MianActivity.getScreenManager().exitActivity(mActivity);
                    } else if (status == 0) {
                        startActivity(new Intent(mActivity, LoginActivity.class));
                        MianActivity.getScreenManager().exitActivity(mActivity);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    
    public OnErrorListener videoErrorListener = new OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
                      
            splashVV.setVisibility(View.GONE);
            splashIV.clearAnimation();
            splashIV.startAnimation(mFadeIn);
            return true;
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                                                                                         
        queryIndex = getIntent().getIntExtra("queryIndex", 0);
        setContentView(R.layout.activity_splash_layout);
        
        start = System.currentTimeMillis();
        
        splashVV.setOnErrorListener(videoErrorListener);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.splash_video;
        splashVV.setVideoURI(Uri.parse(uri));
        splashVV.start();
        
        phone = PreferenceUtil.getInstance(getApplicationContext()).getString("phone", null);
        password = PreferenceUtil.getInstance(getApplicationContext()).getString("password", null);
        GlobalUtil.mScreenWidth = ScreenUtil.getScreenWidth(mActivity);
        GlobalUtil.mScreenHeight = ScreenUtil.getScreenHeight(mActivity);
        
        if (StringUtil.isEmpty(phone) && StringUtil.isEmpty(password)) {
            status = 0;
                             
        }
        autoLogin();
        initAnim();
        setListener();
        
        splashIV.startAnimation(mFadeIn);
                                                      
                        
                                  
                                                                                                                   
                                              
                                         
                                                                                                        
                                                                                     
                                                                                      
                                                
                                                         
                                                               
                                                            
                                                                                                                                                  
                                                                                                        
                                                                          
                                                  
                                    
                                                                
                                                                 
                                                       
                                
                            
                                                                    
                                                                                              
                                                                         
                                                     
                                   
                                                                                           
                            
                                                                                      
                        
                    
                
              
                          
    }

        
  
  
    private void autoLogin() {
        if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
            MessageUtil.alertMessage(mContext, R.string.sys_network_error);
            startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
            return;
        }
        if (StringUtil.isNotEmpty(phone) && StringUtil.isNotEmpty(password)) {
            try {
                String imei = LocationUtil.getDrivenToken(mContext);
                Log.e("SplashActivity", "autoLogin()  phone = " + phone + " ; password = " + password+" ; imei = "+imei);
                MyApplication.initTcpManager();
                MyApplication.tcpManager.login(phone, password, imei, handler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(
            ImageOperation.readBitMap(mContext, R.drawable.splash_iv));
        splashIV.setBackgroundDrawable(bitmapDrawable);
    }

    @Override
    protected void onStop() {
        super.onPause();
        splashIV.setBackgroundDrawable(null);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
    
        
  
  
    @SuppressWarnings("unused")
    private void loadAnimation() {
        Animation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(3000);
        anim.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (StringUtil.isEmpty(phone) && StringUtil.isEmpty(password)) {
                    startActivity(new Intent(mActivity, LoginActivity.class));
                    MianActivity.getScreenManager().exitActivity(mActivity);
                }
                                 
                                                                                    
                           
                                                                                 
                    
                            
            }
        });
        splashIV.startAnimation(anim);
    }
    
        
  
  
    private void initAnim() {
        mFadeIn = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.welcome_fade_in);
        mFadeIn.setDuration(500);
        mFadeInScale = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.welcome_fade_in_scale);
        mFadeInScale.setDuration(2000);
        mFadeOut = AnimationUtils.loadAnimation(SplashActivity.this,
                R.anim.welcome_fade_out);
        mFadeOut.setDuration(500);
    }
    
        
  
  
    private void setListener() {
            
  
  
  
        mFadeIn.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {}

            public void onAnimationRepeat(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
                splashIV.startAnimation(mFadeInScale);
            }
        });
        mFadeInScale.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {}

            public void onAnimationRepeat(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
                splashIV.startAnimation(mFadeOut);
            }
        });
        mFadeOut.setAnimationListener(new AnimationListener() {

            public void onAnimationStart(Animation animation) {}

            public void onAnimationRepeat(Animation animation) {}

            public void onAnimationEnd(Animation animation) {
                end = System.currentTimeMillis();
                handler.sendEmptyMessage(9);
                                                                                   
                                                                               
                                                                                
                           
                                                   
                    
                Log.e("SplashActivity", "onAnimationEnd()  start="+start+" ; end="+end+" ; status="+status+" ; diff="+(end-start)/1000);
                                              
                                         
                                                                                                        
                                                                                     
                                                                                      
                                                
                                                                    
                                                                                              
                                                                         
                                                     
                                   
                                                                                           
                            
                                                                                      
                                                
                                                                                     
                                                                                      
                        
                    
                                                                                 
                                                                               
                                                                                
                  
                               
                                                                                    
                           
                                                                                 
                    
                                                                            
            }
        });

    }

}
