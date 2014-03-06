package com.zgan.community.activity;

import com.zgan.community.R;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.widget.TabHost;

@SuppressLint("NewApi")
public class MainTabActivity extends TabActivity implements OnCheckedChangeListener{
	
	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
	private Intent mEIntent;
	private Intent mFIntent;
	private Intent mGIntent;
	private Intent mHIntent;
	private Intent mIIntent;
	
	private RadioButton radio_button0;
	private RadioButton radio_button1;
	private RadioButton radio_button2;
	private RadioButton radio_button3;
	private RadioButton radio_button4;
	private RadioButton radio_button5;
	private RadioButton radio_button6;
	private RadioButton radio_button7;
	private RadioButton radio_button8;
	
	private HorizontalScrollView horizontalScrollView;
	RadioGroup radioGroup;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.maintabs);
        
        this.mAIntent = new Intent(this,CommunityServiceActivity.class);//社区服务
        this.mBIntent = new Intent(this,CommunityTradeActivity.class);//社区商圈
        this.mCIntent = new Intent(this,CommunityNewNotificationActivity.class);//通知公告
        this.mDIntent = new Intent(this,RecruitmentInfo.class);//招工信息
        this.mEIntent = new Intent(this,Life_Pepsi_son.class);//办事指南
        this.mFIntent = new Intent(this,CommunityPolicitalActivity.class);//民生政务
        
        this.mGIntent = new Intent(this,CommunityNewPayActivity.class);//账单查询
        this.mHIntent = new Intent(this,CommunityContactProperty.class);//联系物业
        this.mIIntent = new Intent(this,AQWSAppActivity.class);//慧应用
        
        horizontalScrollView=(HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        radioGroup=(RadioGroup) findViewById(R.id.main_radio);
        
        radio_button0=(RadioButton) findViewById(R.id.radio_button0);
        radio_button0.setOnCheckedChangeListener(this);
        
        radio_button1=(RadioButton) findViewById(R.id.radio_button1);
        radio_button1.setOnCheckedChangeListener(this);
        
        radio_button2=(RadioButton) findViewById(R.id.radio_button2);
        radio_button2.setOnCheckedChangeListener(this);
        
        radio_button3=(RadioButton) findViewById(R.id.radio_button3);
        radio_button3.setOnCheckedChangeListener(this);
        
        radio_button4=(RadioButton) findViewById(R.id.radio_button4);
        radio_button4.setOnCheckedChangeListener(this);
        
        radio_button5=(RadioButton) findViewById(R.id.radio_button5);
        radio_button5.setOnCheckedChangeListener(this);
        
        radio_button6=(RadioButton) findViewById(R.id.radio_button6);
        radio_button6.setOnCheckedChangeListener(this);
        
        radio_button7=(RadioButton) findViewById(R.id.radio_button7);
        radio_button7.setOnCheckedChangeListener(this);
        
        radio_button8=(RadioButton) findViewById(R.id.radio_button8);
        radio_button8.setOnCheckedChangeListener(this);
        
        setupIntent();
        int tag=getIntent().getIntExtra("TAG", 0);      
        if(tag!=0)
        {
        	RadioButton r=(RadioButton)findViewById(tag);
        	r.setChecked(true);
        	
        	final int count=changeIdToCount(tag);
            WindowManager wm = this.getWindowManager();       
            final int pWidth = wm.getDefaultDisplay().getWidth();
        	//增加整体布局监听
        	ViewTreeObserver vto = radioGroup.getViewTreeObserver();  
        	vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener(){ 
        	    @Override 
        	    public void onGlobalLayout() { 
        	    	radioGroup.getViewTreeObserver().removeGlobalOnLayoutListener(this);      
        	        int height =radioGroup.getMeasuredHeight(); 
        	        int width =radioGroup.getMeasuredWidth();  
        	        horizontalScrollView.scrollTo((int) ((width/9)*(count-0.5)-0.5*pWidth), height);
        	    }  
        	});
        }
    }

	private int changeIdToCount(int tag) {
		int count=0;
		switch (tag) {
		case R.id.radio_button0:
			count=1;
			break;
		case R.id.radio_button1:
			count=2;
			break;
		case R.id.radio_button2:
			count=3;
			break;
		case R.id.radio_button3:
			count=4;
			break;
		case R.id.radio_button4:
			count=5;
			break;
		case R.id.radio_button5:
			count=6;
			break;
		case R.id.radio_button6:
			count=7;
			break;
		case R.id.radio_button7:
			count=8;
			break;
		case R.id.radio_button8:
			count=9;																		
			break;
		default:
			count=0;
			break;
		}
		return count;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			switch (buttonView.getId()) {
			case R.id.radio_button0:
				this.mTabHost.setCurrentTabByTag("A_TAB");
				break;
			case R.id.radio_button1:
				this.mTabHost.setCurrentTabByTag("B_TAB");
				break;
			case R.id.radio_button2:
				this.mTabHost.setCurrentTabByTag("C_TAB");
				break;
			case R.id.radio_button3:
				this.mTabHost.setCurrentTabByTag("D_TAB");
				break;
			case R.id.radio_button4:
				this.mTabHost.setCurrentTabByTag("E_TAB");
				break;
			case R.id.radio_button5:
				this.mTabHost.setCurrentTabByTag("F_TAB");
				break;
			case R.id.radio_button6:
				this.mTabHost.setCurrentTabByTag("G_TAB");
				break;
			case R.id.radio_button7:
				this.mTabHost.setCurrentTabByTag("H_TAB");
				break;
			case R.id.radio_button8:
				this.mTabHost.setCurrentTabByTag("I_TAB");
				break;
			}
		}
		
	}
	
	private void setupIntent() {
		this.mTabHost = getTabHost();
		TabHost localTabHost = this.mTabHost;

		localTabHost.addTab(buildTabSpec("A_TAB", null,
				R.drawable.r0, this.mAIntent));

		localTabHost.addTab(buildTabSpec("B_TAB", null,
				R.drawable.r1, this.mBIntent));

		localTabHost.addTab(buildTabSpec("C_TAB",null, 
				R.drawable.r2,this.mCIntent));

		localTabHost.addTab(buildTabSpec("D_TAB", null,
				R.drawable.r3, this.mDIntent));

		localTabHost.addTab(buildTabSpec("E_TAB", null,
				R.drawable.r4, this.mEIntent));
		
		localTabHost.addTab(buildTabSpec("F_TAB", null,
				R.drawable.r5, this.mFIntent));
		
		localTabHost.addTab(buildTabSpec("G_TAB", null,
				R.drawable.r6, this.mGIntent));
		
		localTabHost.addTab(buildTabSpec("H_TAB", null,
				R.drawable.r7, this.mHIntent));
		
		localTabHost.addTab(buildTabSpec("I_TAB", null,
				R.drawable.r8, this.mIIntent));

	}
	
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, int resIcon,
			final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel,
				getResources().getDrawable(resIcon)).setContent(content);
	}
}