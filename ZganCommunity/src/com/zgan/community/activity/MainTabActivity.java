package com.zgan.community.activity;

import com.zgan.community.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TabHost;

public class MainTabActivity extends TabActivity implements OnCheckedChangeListener{
	
	private TabHost mTabHost;
	private Intent mAIntent;
	private Intent mBIntent;
	private Intent mCIntent;
	private Intent mDIntent;
	private Intent mEIntent;
	private Intent mFIntent;
	
	private RadioButton radio_button0;
	private RadioButton radio_button1;
	private RadioButton radio_button2;
	private RadioButton radio_button3;
	private RadioButton radio_button4;
	private RadioButton radio_button5;
	
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
        
        setupIntent();
        int tag=getIntent().getIntExtra("TAG", 0);
        if(tag!=0)
        {
        	((RadioButton)findViewById(tag)).setChecked(true);
        }
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

	}
	
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, int resIcon,
			final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(resLabel,
				getResources().getDrawable(resIcon)).setContent(content);
	}
}