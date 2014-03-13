package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.R.id;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.adapter.FragmentAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class MainFragmentActivity extends FragmentActivity implements OnCheckedChangeListener {
	
	public static final int TAB0 = 0;
	public static final int TAB1 = 1;
	public static final int TAB2 = 2;
	public static final int TAB3 = 3;
	public static final int TAB4 = 4;
	public static final int TAB5 = 5;
	public static final int TAB6 = 6;
	public static final int TAB7 = 7;
	public static final int TAB8 = 8;
	
	private ViewPager viewPager;
	private RadioButton radio_button0;
	private RadioButton radio_button1;
	private RadioButton radio_button2;
	private RadioButton radio_button3;
	private RadioButton radio_button4;
	private RadioButton radio_button5;
	private RadioButton radio_button6;
	private RadioButton radio_button7;
	private RadioButton radio_button8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_fragement);
		initView();
		addListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_fragement, menu);
		return false;
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
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
		
		FragmentAdapter adapter = new FragmentAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(adapter);
	}

	private void addListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int id) {
				switch (id) {
				case TAB0:
					radio_button0.setChecked(true);
					break;
				case TAB1:
					radio_button1.setChecked(true);
					break;
				case TAB2:
					radio_button2.setChecked(true);
					break;
				case TAB3:
					radio_button3.setChecked(true);
					break;
				case TAB4:
					radio_button4.setChecked(true);
					break;
				case TAB5:
					radio_button5.setChecked(true);
					break;
				case TAB6:
					radio_button6.setChecked(true);
					break;
				case TAB7:
					radio_button7.setChecked(true);
					break;
				case TAB8:
					radio_button8.setChecked(true);
					break;

				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.radio_button0:
			viewPager.setCurrentItem(TAB0);
			break;
		case R.id.radio_button1:
			viewPager.setCurrentItem(TAB1);
			break;
		case R.id.radio_button2:
			viewPager.setCurrentItem(TAB2);
			break;
		case R.id.radio_button3:
			viewPager.setCurrentItem(TAB3);
			break;
		case R.id.radio_button4:
			viewPager.setCurrentItem(TAB4);
			break;
		case R.id.radio_button5:
			viewPager.setCurrentItem(TAB5);
			break;
		case R.id.radio_button6:
			viewPager.setCurrentItem(TAB6);
			break;
		case R.id.radio_button7:
			viewPager.setCurrentItem(TAB7);
			break;
		case R.id.radio_button8:
			viewPager.setCurrentItem(TAB8);
			break;

		default:
			break;
		}
		
	}

}
