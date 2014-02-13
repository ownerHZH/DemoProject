package com.hiibox.houseshelter.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.MyApplication;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.SmothActivity;
import com.hiibox.houseshelter.adapter.AdsAdapter;
import com.hiibox.houseshelter.adapter.MembersPagerAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.listener.HandlerCommandListener;
import com.hiibox.houseshelter.net.Frame;
import com.hiibox.houseshelter.net.MembersInfoResult;
import com.hiibox.houseshelter.net.SpliteUtil;
import com.hiibox.houseshelter.net.TCPMainClient;
import com.hiibox.houseshelter.service.PushMessageService;
import com.hiibox.houseshelter.util.BackToExitUtil;
import com.hiibox.houseshelter.util.DateUtil;
import com.hiibox.houseshelter.util.ImageOperation;
import com.hiibox.houseshelter.util.LocationUtil;
import com.hiibox.houseshelter.util.MessageUtil;
import com.hiibox.houseshelter.util.PreferenceUtil;
import com.hiibox.houseshelter.util.ScreenUtil;
import com.hiibox.houseshelter.util.StringUtil;
import com.hiibox.houseshelter.view.AdsDialog;

    
  
  
  
  
  
  
  
public class HomepageActivity extends ShaerlocActivity implements OnSeekBarChangeListener, OnPageChangeListener {

    @ViewInject(id = R.id.root_layout) RelativeLayout rootLayout;
    @ViewInject(id = R.id.top_info_layout) RelativeLayout topInfoLayout;
                                                                                                     
                                                                     
    @ViewInject(id = R.id.temperature_tv, click = "onClick") TextView temperatureTV;
    @ViewInject(id = R.id.temperature_unit_tv) TextView temperatureUnitTV;
    @ViewInject(id = R.id.temperature_description_tv) TextView tempDesTV;
    @ViewInject(id = R.id.humidity_tv) TextView humidityTV;
    @ViewInject(id = R.id.humidity_unit_tv) TextView humidityUnitTV;
    @ViewInject(id = R.id.humidity_description_tv) TextView humidityDesTV;
    @ViewInject(id = R.id.family_members_layout) ViewPager familyMembersLayout;
    @ViewInject(id = R.id.shield_iv) ImageView shieldIV;
    @ViewInject(id = R.id.eye_iv1) ImageView eye1IV;
    @ViewInject(id = R.id.eye_iv2) ImageView eye2IV;
    @ViewInject(id = R.id.eye_iv3) ImageView eye3IV;
    @ViewInject(id = R.id.cloud_eyes_iv, click = "onClick") ImageView cloudEyesIV;
    @ViewInject(id = R.id.imprinting_iv, click = "onClick") ImageView imprintingIV;
    @ViewInject(id = R.id.cloud_photo_album_layout, click = "onClick") RelativeLayout cloudPhotoAlbumLayout;
    @ViewInject(id = R.id.smart_app_iv, click = "onClick") ImageView smartAppIV;
    @ViewInject(id = R.id.self_center_iv, click = "onClick") ImageView selfCenterIV;
                                                                               
                                                                               
                                                                               
                                                                               
                                                                               
    @ViewInject(id = R.id.defence_seek_bar) SeekBar seekBar;
    @ViewInject(id = R.id.new_pictures_tv) TextView newPicNumbersTv;
                                                                
                                                                    
                                                              
    
    private BackToExitUtil exitPrompt = null;
    private AdsDialog adsDialog = null;
    private View splashView = null;
    private ViewPager pager = null;
                                      
    private static Drawable leftDrawable = null;
    private static Drawable rightDrawable = null;
    private Drawable leftLightDrawable = null;
    private Drawable rightLightDrawable = null;
    private Drawable leftGrayDrawable = null;
    private Drawable rightGrayDrawable = null;
                                                                    
                                                                          
    private Drawable tempNormalDrawable = null;     
    private Drawable tempWarningDrawable = null;     
    private Drawable humidityDrawable = null;     
    private Drawable waterDrawable1 = null;           
    private Drawable waterDrawable2 = null;              
    private Drawable waterDrawable3 = null;           
    private Drawable maleDrawable = null;           
    private Drawable maleLightdDrawable = null;           
    private Drawable recorderDrawable = null;            
    private Drawable recorderLightDrawable = null;            
    private int defenceStatus = 0;              
    private boolean defenceClikable = false;
    private HandlerCommandListener commandListener = null;
    private HandlerCommandListener cmdListener = null;
    public static List<MembersInfoResult> membersList = null;           
    private List<List<MembersInfoResult>> membersPagerList = null;
    private MembersPagerAdapter pagerAdapter = null;
    protected boolean isInitPager = true;
    private Animation anim1, anim2, anim3;
    private BitmapDrawable shieldDrawable = null;
    private BitmapDrawable eyesDrawable = null;
    private BitmapDrawable shieldLightDrawable = null;
    private BitmapDrawable eyesLightDrawable = null;
    
    @SuppressLint("HandlerLeak")
    private Handler cmdHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (null == msg.obj) {
            	return;
            }
            Frame[] frame = (Frame[]) msg.obj;
            if (null == frame[1]) {
            	return;
            }
            int subCmd = frame[1].subCmd;
            if (msg.what == 0) {
            	if (null == frame[1]) {
            		return;
            	}
            	String data = frame[1].strData;
            	Log.i("HomepageActivity", "cmdHandler  handleMessage() subCmd = "+subCmd+" ; data = "+data);
            	if (subCmd == 40 && data.startsWith("3") || data.startsWith("15")) {
            		String ret = SpliteUtil.getResult(data);
            		if (StringUtil.isNotEmpty(ret)) {
            			int n = Integer.valueOf(ret);
            			if (n > 0) {
            				newPicNumbersTv.setVisibility(View.VISIBLE);
            				newPicNumbersTv.setText(ret);
            			} else {
            				newPicNumbersTv.setVisibility(View.GONE);
            			}
            		}
            	}
            }
		}
    };
    
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
            	Log.d("HomepageActivity", "subCmd = "+subCmd+" ; data = " + ret);
                switch (subCmd) {
                    case 1:       
                        if (ret.equals("0")) {
                        	seekBar.setClickable(false);
                        	MessageUtil.alertMessage(mContext, R.string.open_denfence_request_success);
                        	defenceStatus = 1;
                        	seekBar.setProgress(100);
                            seekBar.setThumb(leftGrayDrawable);
                        } else {
                        	seekBar.setClickable(true);
                        	MessageUtil.alertMessage(mContext, R.string.open_denfence_request_failure);
                        	seekBar.setProgress(0);
                            seekBar.setThumb(rightDrawable);
                        }
                        break;
                    case 2:       
                        if (ret.equals("0")) {
                        	seekBar.setClickable(false);
                        	MessageUtil.alertMessage(mContext, R.string.close_denfence_request_success);
                        	defenceStatus = 0;
                        	seekBar.setProgress(0);
                            seekBar.setThumb(rightGrayDrawable);
                        } else {
                        	seekBar.setClickable(true);
                        	MessageUtil.alertMessage(mContext, R.string.close_denfence_request_failure);
                        	seekBar.setProgress(100);
                            seekBar.setThumb(leftDrawable);
                        }
                        break;
                    case 4:                   
                        if (ret.equals("6") || !ret.startsWith("0")) {
                            if (null != membersPagerList) {
                                membersPagerList.clear();
                                if (null != pagerAdapter) {
                                    pagerAdapter.notifyDataSetChanged();
                                }
                                                                          
                            }
                            return;
                        }
                    	MembersInfoResult member = MembersInfoResult.parse(frame[1]);
                    	if (null != member) {
                    		int total = member.totalMembers;
                        	int index = member.currIndex;
                        	Log.i("HomepageActivity", "[ɨ�迨Ƭ]  total = "+total+"; index = "+index);
                    		membersList.add(member);
                    		if (total-index == 1) {
                    			setMembersInfo();
                    		}
                    	}
                    	break;
                    case 72:               
                                                                                
                    	seekBar.setClickable(true);
                    	defenceClikable = true;
                    	if (SpliteUtil.getRuquestStatus(ret)) {
                    		if (SpliteUtil.getResult(ret).equals("1")) {       
                    			Log.e("HomepageActivity", "[��ѯ�豸��ǰ����״̬] �յ���Ӧ : ����");
                    			defenceStatus = 0;
                    			seekBar.setProgress(0);
                                seekBar.setThumb(rightDrawable);
                    		} else {       
                    			Log.e("HomepageActivity", "[��ѯ�豸��ǰ����״̬] �յ���Ӧ : ����");
                    			defenceStatus = 1;
                        		seekBar.setProgress(100);
                                seekBar.setThumb(leftDrawable);
                    		}
                    	} else {                 
                    		seekBar.setProgress(0);
                            seekBar.setThumb(rightDrawable);
                    	}
                        break;
                    case 73:           
                                         
                                                               
                                                                     
                                                                               
                                                 
                                                                                  
                                                      
                                                                            
                                 
                                                                                   
                                                                           
                                                                             
                          
                         
                                                                              
                        break;
                    case 74:         
                    	if (SpliteUtil.getRuquestStatus(ret)) {
                    		float temperature = Float.valueOf(SpliteUtil.getResult(ret));
                    		temperatureTV.setText(Math.round(temperature)+"");
                    		if (temperature <= 10) {      
                    			tempDesTV.setText(getString(R.string.cold));
                    		} else if (temperature > 10 && temperature <= 17) {      
                    			tempDesTV.setText(getString(R.string.cool));
                    		} else if (temperature > 18 && temperature <= 26) {       
                    			tempDesTV.setText(getString(R.string.comfortable));
                    		} else if (temperature > 27 && temperature <= 35) {      
                    			tempDesTV.setText(getString(R.string.warm));
                    		} else {      
                    			tempDesTV.setText(getString(R.string.hot));
                    		}
                    	}
                        break;
                    case 75:         
                    	if (SpliteUtil.getRuquestStatus(ret)) {
                    		float humidity = Float.valueOf(SpliteUtil.getResult(ret));
                    		humidityTV.setText(Math.round(humidity)+"");
                    		if (humidity < 40) {
                    		    humidityDesTV.setText(getString(R.string.humidity_dry));
                    		    humidityDesTV.setCompoundDrawables(null, null, waterDrawable1, null);
                    		} else if (humidity > 60) {
                    		    humidityDesTV.setText(getString(R.string.humidity_wet));
                    		    humidityDesTV.setCompoundDrawables(null, null, waterDrawable3, null);
                    		} else {
                    		    humidityDesTV.setText(getString(R.string.humidity_suitable));
                    		    humidityDesTV.setCompoundDrawables(null, null, waterDrawable2, null);
                    		}
                    	}
                        break;
                    case 76:           
                        break;
                    case 80:             
                    	if (SpliteUtil.getRuquestStatus(ret)) {
                    		MyApplication.deviceCode = SpliteUtil.getResult(ret);
                    	}
                    	break;
                    default:
                        break;
                }
            } else {       
                Log.d("HomepageActivity", "subCmd = "+subCmd+" : �յ���Ӧ��ʱ");
            	switch (subCmd) {
	                case 1:       
	                	MessageUtil.alertMessage(mContext, R.string.network_timeout);
	                	if (defenceStatus == 0) {
	                		seekBar.setProgress(0);
                            seekBar.setThumb(rightDrawable);
	                	} else if (defenceStatus == 1) {
	                		seekBar.setProgress(100);
                            seekBar.setThumb(leftDrawable);
	                	}
	                    break;
	                case 2:       
	                	MessageUtil.alertMessage(mContext, R.string.network_timeout);
	                	if (defenceStatus == 0) {
	                		seekBar.setProgress(0);
                            seekBar.setThumb(rightDrawable);
	                	} else if (defenceStatus == 1) {
	                		seekBar.setProgress(100);
                            seekBar.setThumb(leftDrawable);
	                	}
	                	break;
	                case 4:     
	                    break;
	                case 72:               
	                    break;
	                case 73:           
	                    break;
	                case 74:         
	                    break;
	                case 75:         
	                    break;
	                case 76:           
	                    break;
	                default:
	                    break;
	          }
                
            }
        }

		private void setMembersInfo() {
                                                                                        
		    if (null != membersList && membersList.size() > 0) {
		        int size = membersList.size();
		        int pages = (int) Math.ceil(size/5d);
		        pages = (pages == 0) ? 1 : pages;
		        int start = 0;
		        int end = start + 5;
		        if (pages == 1) {
		        	end = size;
		        }
                                                                                                              
		        for (int m = 0; m < pages; ) {
		        	List<MembersInfoResult> l = new ArrayList<MembersInfoResult>();
		            for (; start < end; start ++) {
		            	l.add(membersList.get(start));
                                                                                                    
		            }
		            membersPagerList.add(l);
		            m ++;
		            if (m == pages-1) {
		                start = end;
	                    end = size;
		            } else {
		                start = end;
	                    end = start + 5;
		            }
                                                                                                          
		        }
		        if (null != membersPagerList && membersPagerList.size() > 0) {
	                pagerAdapter.setList(membersPagerList);
                                                                                                                                                        
	                if (isInitPager) {
                                                                                                      
	                    familyMembersLayout.setAdapter(pagerAdapter);
	                    isInitPager = false;
	                }  

  
	            }
		    }
		}

    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initAnimations();
        
        membersList = new ArrayList<MembersInfoResult>();
        
        startService(new Intent(this, PushMessageService.class));         
        PushMessageService.handler = sHandler;
        
        sendRequest(72);
        sendRequest(80);
        sendRequest(4);
        sendRequest(74);
        sendRequest(75);
        getPhotoNumber();
    }
    
        
  
  
	private void initViews() {
	    PreferenceUtil.getInstance(mContext).saveBoolean("exitApp", false);
		if (!PreferenceUtil.getInstance(mContext).getBoolean("showedAds", false)) {
            loadAdsDialog();
        }
		
		setContentView(R.layout.activity_homepage_layout);
		
		Log.e("HomepageActivity", "onCreate()  w = "+ScreenUtil.getScreenWidth(mActivity)+" ; h = "+ScreenUtil.getScreenHeight(mActivity));
		familyMembersLayout.setOnPageChangeListener(this);
		exitPrompt = new BackToExitUtil();
        commandListener = new HandlerCommandListener(handler);
        cmdListener = new HandlerCommandListener(cmdHandler);
        Resources res = getResources();
        membersPagerList = new ArrayList<List<MembersInfoResult>>();
        pagerAdapter = new MembersPagerAdapter(mContext, mActivity);
        
        seekBar.setOnSeekBarChangeListener(this);
        cloudEyesIV.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Log.d("HomepageActivity", "onLongClick()  .....");
				eye1IV.setVisibility(View.VISIBLE);
				eye2IV.setVisibility(View.VISIBLE);
				eye3IV.setVisibility(View.VISIBLE);
				eye1IV.startAnimation(anim1);
		    	eye2IV.startAnimation(anim2);
		    	eye3IV.startAnimation(anim3);
				return false;
			}
		});
        
        setParams();
        
        leftDrawable = res.getDrawable(R.drawable.thumb_ttpod_left);
        rightDrawable = res.getDrawable(R.drawable.thumb_ttpod_right);
        leftLightDrawable = res.getDrawable(R.drawable.thumb_ttpod_light_left);
        rightLightDrawable = res.getDrawable(R.drawable.thumb_ttpod_light_right);
        leftGrayDrawable = res.getDrawable(R.drawable.thumb_ttpod_gray_left);
        rightGrayDrawable = res.getDrawable(R.drawable.thumb_ttpod_gray_right);
                                                                         
                                                                           
        tempNormalDrawable = res.getDrawable(R.drawable.thermometer_normal);
        tempWarningDrawable = res.getDrawable(R.drawable.thermometer_warning);
        tempNormalDrawable.setBounds(0, 0, tempNormalDrawable.getMinimumWidth(), tempNormalDrawable.getMinimumHeight());
        tempWarningDrawable.setBounds(0, 0, tempWarningDrawable.getMinimumWidth(), tempWarningDrawable.getMinimumHeight());
        humidityDrawable = res.getDrawable(R.drawable.thermometer_warning);
        humidityDrawable.setBounds(0, 0, humidityDrawable.getMinimumWidth(), humidityDrawable.getMinimumHeight());
        waterDrawable1 = res.getDrawable(R.drawable.water1);
        waterDrawable2 = res.getDrawable(R.drawable.water2);
        waterDrawable3 = res.getDrawable(R.drawable.water3);
        waterDrawable1.setBounds(0, 0, waterDrawable1.getMinimumWidth(), waterDrawable1.getMinimumHeight());
        waterDrawable2.setBounds(0, 0, waterDrawable2.getMinimumWidth(), waterDrawable2.getMinimumHeight());
        waterDrawable3.setBounds(0, 0, waterDrawable3.getMinimumWidth(), waterDrawable3.getMinimumHeight());
        maleDrawable = res.getDrawable(R.drawable.male_gray);
        maleDrawable.setBounds(0, 0, maleDrawable.getMinimumWidth(), maleDrawable.getMinimumHeight());
        maleLightdDrawable = res.getDrawable(R.drawable.male_light);
        maleLightdDrawable.setBounds(0, 0, maleLightdDrawable.getMinimumWidth(), maleLightdDrawable.getMinimumHeight());
        recorderDrawable = res.getDrawable(R.drawable.recorder_gray);
        recorderDrawable.setBounds(0, 0, recorderDrawable.getMinimumWidth(), recorderDrawable.getMinimumHeight());
        recorderLightDrawable = res.getDrawable(R.drawable.recorder_light);
        recorderLightDrawable.setBounds(0, 0, recorderLightDrawable.getMinimumWidth(), recorderLightDrawable.getMinimumHeight());
        shieldDrawable = new BitmapDrawable(ImageOperation.readBitMap(mContext, R.drawable.shield_silver));
        eyesDrawable = new BitmapDrawable(ImageOperation.readBitMap(mContext, R.drawable.cloud_eyes_silver));
        shieldLightDrawable = new BitmapDrawable(ImageOperation.readBitMap(mContext, R.drawable.shield_orange));
        eyesLightDrawable = new BitmapDrawable(ImageOperation.readBitMap(mContext, R.drawable.cloud_eyes_orange));
	}
    
    private void initAnimations() {
        anim1 = AnimationUtils.loadAnimation(this, R.anim.cloud_eyes_animation);
        anim1.setDuration(2000);
        anim1.setRepeatCount(3);
        anim1.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {
            	eye1IV.startAnimation(anim1);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            	eye1IV.clearAnimation();
            	eye1IV.setVisibility(View.INVISIBLE);
            }
        });
        anim2 = AnimationUtils.loadAnimation(this, R.anim.cloud_eyes_animation);
        anim2.setDuration(2000);
        anim2.setRepeatCount(3);
        anim2.setStartOffset(500);
        anim2.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {
            	eye2IV.startAnimation(anim2);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            	eye2IV.clearAnimation();
            	eye2IV.setVisibility(View.INVISIBLE);
            }
        });
        anim3 = AnimationUtils.loadAnimation(this, R.anim.cloud_eyes_animation);
        anim3.setDuration(2000);
        anim3.setRepeatCount(3);
        anim3.setStartOffset(1000);
        anim3.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {
            	eye3IV.startAnimation(anim3);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
            	eye3IV.clearAnimation();
            	eye3IV.setVisibility(View.INVISIBLE);
            }
        });
    }

        
  
  
  
	private void sendRequest(int subCmd) {
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
		if (null == MyApplication.mainClient) {
		    return;
		}
		switch (subCmd) {
			case 1: 
				System.out.println("发送子功能1");
				MyApplication.mainClient.startDefine(phone, DateUtil.getcurrentDay(), commandListener);
				break;
			case 3:  
				System.out.println("发送子功能3");
				MyApplication.mainClient.finishDefine(phone, DateUtil.getcurrentDay(), commandListener);
				break;
			case 4:                   
			    if (null == membersList) {
			        membersList = new ArrayList<MembersInfoResult>();
			    }
			    if (null == membersPagerList) {
			        membersPagerList = new ArrayList<List<MembersInfoResult>>();
			    }
			    membersList.clear();
                membersPagerList.clear();
                if (null != pagerAdapter) {
                    pagerAdapter.setList(membersPagerList);
                                                           
                }
				MyApplication.mainClient.readCard(phone, new TCPMainClient.QueryWarnListener2(this.commandListener));
				break;
			case 72:             
				MyApplication.mainClient.queryDefineStatus(phone, commandListener);
				break;
			case 73:           
				MyApplication.mainClient.queryUserLevel(phone, commandListener);
				break;
			case 74:         
				MyApplication.mainClient.queryTemperature(phone, commandListener);
				break;
			case 75:         
				MyApplication.mainClient.queryDampness(phone, commandListener);
				break;
			case 76:           
				MyApplication.mainClient.queryUserPoints(phone, commandListener);
				break;
			case 80:             
				MyApplication.mainClient.getDeviceCode(phone, commandListener);
				break;
			default:
				break;
		}
	}
	
	@Override
	protected void onRestart() {
	    super.onRestart();
	    sendRequest(72); 
	    sendRequest(4); 
        sendRequest(74);
        sendRequest(75);
	}
	
	private void getPhotoNumber() {
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
		if (null == MyApplication.fileClient) {
		    return;
		}
    	MyApplication.fileClient.registe(40, cmdListener);
    }
	
	@Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        resumePageStyle();
                                                              
                                                                            
                                                            
        sendRequest(72);
                          
    }
    
    @Override
    protected void onStop() {
        super.onStop();
                                                  
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
        sHandler.removeCallbacks(null);
        cmdHandler.removeCallbacks(null);
        if (null != membersList) {
            membersList.clear();
            membersList = null;
        }
        if (null != membersPagerList) {
            membersPagerList.clear();
            membersPagerList = null;
        }
    	MyApplication.showedAds = false;
    	if (null != MyApplication.mainClient) {
        	MyApplication.mainClient.stop();
        	MyApplication.mainClient = null;
        }
        if (null != MyApplication.fileClient) {
        	MyApplication.fileClient.stop();
            MyApplication.fileClient = null;
        }
        MyApplication.phone = null;
        MyApplication.password = null;
    }

        
  
  
    private void setParams() {
        int screenHeight = ScreenUtil.getScreenHeight(mActivity);
                                                                                                                                                   
                                                                                                                                                       
                                                                                                                                                 
        android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) familyMembersLayout.getLayoutParams();
        android.widget.FrameLayout.LayoutParams eyesParams = (android.widget.FrameLayout.LayoutParams) cloudEyesIV.getLayoutParams();
        android.widget.FrameLayout.LayoutParams eye1Params = (android.widget.FrameLayout.LayoutParams) eye1IV.getLayoutParams();
        android.widget.FrameLayout.LayoutParams eye2Params = (android.widget.FrameLayout.LayoutParams) eye2IV.getLayoutParams();
        android.widget.FrameLayout.LayoutParams eye3Params = (android.widget.FrameLayout.LayoutParams) eye3IV.getLayoutParams();
        if (screenHeight > 0 && screenHeight <= 800) {
            params.bottomMargin = 20;
            familyMembersLayout.setLayoutParams(params);
        } else if (screenHeight > 854 && screenHeight <= 1280) {
                                          
                                             
                                              
                                               
                                                                
                                                                   
               
                                                 
                                                
                                                            
                                                                                        
               
                                              
                                             
                                              
                                                          
                                                                                   
            
            params.bottomMargin = 70;
            familyMembersLayout.setLayoutParams(params);
            
            eyesParams.width = 350;
            eyesParams.height = 150;
            eyesParams.topMargin = 100;
            cloudEyesIV.setLayoutParams(eyesParams);
            
            eye1Params.width = 350;
            eye1Params.height = 150;
            eye1Params.topMargin = 100;
            eye1IV.setLayoutParams(eye1Params);
            
            eye2Params.width = 350;
            eye2Params.height = 150;
            eye2Params.topMargin = 100;
            eye2IV.setLayoutParams(eye2Params);
            
            eye3Params.width = 350;
            eye3Params.height = 150;
            eye3Params.topMargin = 100;
            eye3IV.setLayoutParams(eye3Params);
        }
    }
    
    private void loadAdsDialog() {
        AdsAdapter adapter = new AdsAdapter(this);
        splashView = getLayoutInflater().inflate(R.layout.advertisement_layout, null);
        pager = (ViewPager) splashView.findViewById(R.id.activity_poster_vp);
                                                                     
        adsDialog = new AdsDialog(this, pager, splashView);
        pager.setAdapter(adapter);
                                                            
                        
                                            
                                                                                                        
                                         
                
              
        adsDialog.show();
        MyApplication.showedAds = true;
        PreferenceUtil.getInstance(mContext).saveBoolean("showedAds", true);
    }
    
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.cloud_eyes_iv:
                intent.setClass(this, SmothActivity.class);
                intent.putExtra("activityFlag", 0);
                break;
            case R.id.imprinting_iv:
                intent.setClass(this, SmothActivity.class);
                intent.putExtra("activityFlag", 1);
                break;
            case R.id.cloud_photo_album_layout:
                if (newPicNumbersTv.getVisibility() == 0) {
                    newPicNumbersTv.setVisibility(View.GONE);
                }
                intent.setClass(this, SmothActivity.class);
                intent.putExtra("activityFlag", 2);
                break;
            case R.id.smart_app_iv:
                intent.setClass(this, SmothActivity.class);
                intent.putExtra("activityFlag", 3);
                break;
            case R.id.self_center_iv:
                intent.setClass(this, SmothActivity.class);
                intent.putExtra("activityFlag", 4);
                break;
            case R.id.temperature_tv:
                intent.setClass(this, TemperatureDialogActivity.class);
                                                                                               
                                                                           
            	break;
            default:
                break;
        }
        if (null != intent.getClass()) {
            startActivity(intent);
        }
    }
    
    private void createTemperatureDialog() {
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	Log.e("HomepageActivity", "onActivityResult()   requestCode = "+requestCode);
    	if (requestCode == 0x901 && resultCode == RESULT_OK) {
    		pagerAdapter.notifyDataSetChanged();
    	}
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pressAgainExit(); 
            return true; 
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @SuppressLint("ShowToast")
    private void pressAgainExit() {
        if (exitPrompt.isExit()) {
            MyApplication.isFirstTimeEntry = true;
        	PreferenceUtil.getInstance(mContext).saveBoolean("exitApp", true);
        	PreferenceUtil.getInstance(mContext).saveBoolean("showedAds", false);
            MianActivity.getScreenManager().exitAllActivityExceptOne();
        } else {
            Toast.makeText(this, getString(R.string.back_to_exit_app), Toast.LENGTH_SHORT).show();
            exitPrompt.doExitInOneSecond();
        }
    }

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		if (LocationUtil.checkNetWork(mContext).endsWith(GlobalUtil.NETWORK_NONE)) {
        	MessageUtil.alertMessage(mContext, R.string.sys_network_error);
        	startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
        	return;
        }
		if (!defenceClikable) {
			return;
		}
	    if (seekBar.getProgress() > 50) {
	        seekBar.setThumb(leftLightDrawable);
	    } else {
	        seekBar.setThumb(rightLightDrawable);
	    }
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int progress = seekBar.getProgress();
		seekBar.setClickable(false);
		defenceClikable = false;
        if (progress > 50) {
        	System.out.println("seekBar开启");
        	sendRequest(1);
        	seekBar.setProgress(100);
            seekBar.setThumb(leftGrayDrawable);
        } else {
        	System.out.println("seekBar关闭");
        	sendRequest(3);
        	seekBar.setProgress(0);
            seekBar.setThumb(rightGrayDrawable);
        }
		
	}
	
	@SuppressLint("HandlerLeak")
    public Handler sHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what;
			switch (what) {
				case 0:       
					if(null == membersList) {
						return;
					}
					if (null == membersPagerList) {
						return;
					}
                            
                                 
					sendRequest(4);
					sendRequest(73);
					break;
				case 1:       
					if(null == membersList) {
						return;
					}
					if (null == membersPagerList) {
						return;
					}
                            
                                 
					sendRequest(4);
					sendRequest(73);
					break;
				case 3:       
				    sendRequest(1);
					alarmPageStyle();
					break;
				case 4:         
					alarmPageStyle(Double.parseDouble((String) msg.obj));
					break;
				case 5:         
					Log.e("HomepageActivity", "sHandler   ��������....");
					if (null == seekBar) {
						return;
					}
					defenceClikable = true;
					seekBar.setClickable(true);
					seekBar.setProgress(100);
	                seekBar.setThumb(leftDrawable);
					break;
				case 6:         
					Log.e("HomepageActivity", "sHandler   �����....");
					if (null == seekBar) {
						return;
					}
					defenceClikable = true;
					seekBar.setClickable(true);
					seekBar.setProgress(0);
	                seekBar.setThumb(rightDrawable);
					break;
				default:
					break;
			}
		}
	};
	
	    
  
  
    private void alarmPageStyle() {
        topInfoLayout.setBackgroundResource(R.drawable.top_info_warning);
        temperatureTV.setTextColor(getResources().getColor(R.color.temperature_warning_font));
        temperatureUnitTV.setTextColor(getResources().getColor(R.color.temperature_warning_font));
        humidityTV.setTextColor(getResources().getColor(R.color.temperature_warning_font));
        humidityUnitTV.setTextColor(getResources().getColor(R.color.temperature_warning_font));
        shieldIV.setBackgroundDrawable(shieldLightDrawable);
    	cloudEyesIV.setBackgroundDrawable(eyesLightDrawable);
    }
    
        
  
  
    private void alarmPageStyle(double temp) {
        topInfoLayout.setBackgroundResource(R.drawable.top_info_warning);
    	temperatureTV.setText(Math.round(temp)+getString(R.string.temperature_unit));
    	temperatureTV.setTextColor(getResources().getColor(R.color.temperature_warning_font));
    	temperatureTV.setCompoundDrawables(tempWarningDrawable, null, null, null);
    	temperatureUnitTV.setTextColor(getResources().getColor(R.color.temperature_warning_font));
        humidityTV.setTextColor(getResources().getColor(R.color.temperature_warning_font));
        humidityUnitTV.setTextColor(getResources().getColor(R.color.temperature_warning_font));
    	shieldIV.setBackgroundDrawable(shieldLightDrawable);
    	cloudEyesIV.setBackgroundDrawable(eyesLightDrawable);
    }
    
        
  
  
    private void resumePageStyle() {
        topInfoLayout.setBackgroundResource(R.drawable.top_info_normal);
        temperatureTV.setTextColor(getResources().getColor(R.color.white));
        temperatureTV.setCompoundDrawables(tempNormalDrawable, null, null, null);
        temperatureUnitTV.setTextColor(getResources().getColor(R.color.white));
        humidityTV.setTextColor(getResources().getColor(R.color.white));
        humidityUnitTV.setTextColor(getResources().getColor(R.color.white));
        shieldIV.setBackgroundDrawable(shieldDrawable);
        cloudEyesIV.setBackgroundDrawable(eyesDrawable);
    }

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int arg0) {}
	
}
