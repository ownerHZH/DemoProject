package com.zgan.community.activity;

import java.util.Calendar;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.activity.CommunityPolicitalActivity.ButtonClickListener;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.SlipButton;
import com.zgan.community.tools.SlipButton.OnChangedListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CommunitySetting extends MainAcitivity {

	private Button back;
	private TextView title;
	private Context con;
	
	private LinearLayout about;//关于
	private LinearLayout feedback;//意见反馈
	//private Button messageSwitch;
	private Button passwordChange;//密码修改
	private RadioGroup sexRadioGroup;//性别sexRadioGroup
	private EditText birthText;//生日显示框
	private Button birthChange;//生日修改按钮
	private EditText nicknameText;//昵称显示框
	private Button nicknameChange;//昵称修改按钮
	private TextView balcony;//楼座号
	private TextView phone;//手机号码
	private SlipButton messageSwitch;//消息推送开关
	
	private boolean clicked=false;
	
	//创建一个常量，标识DatePickerDialog  
    private static final int DATE_PICKER_ID = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_setting);
		
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.personal_settings);
		con = CommunitySetting.this;
		
		about=(LinearLayout) findViewById(R.id.about);//关于
		feedback=(LinearLayout) findViewById(R.id.feedback);//意见反馈
		passwordChange=(Button) findViewById(R.id.passwordChange);//密码修改
		sexRadioGroup=(RadioGroup) findViewById(R.id.sexRadioGroup);//性别sexRadioGroup
		birthText=(EditText) findViewById(R.id.birthText);//生日显示框
		birthChange=(Button) findViewById(R.id.birthChange);//生日修改按钮
		nicknameText=(EditText) findViewById(R.id.nicknameText);//昵称显示框
		nicknameChange=(Button) findViewById(R.id.nicknameChange);//昵称修改按钮
		balcony=(TextView) findViewById(R.id.balcony);//楼座号
		phone=(TextView) findViewById(R.id.phone);//手机号码
		messageSwitch=(SlipButton) findViewById(R.id.slipbutton);
				
		back.setOnClickListener(l);
		passwordChange.setOnClickListener(l);
		feedback.setOnClickListener(l);
		about.setOnClickListener(l);
		nicknameChange.setOnClickListener(l);
		birthChange.setOnClickListener(l);
		
		messageSwitch.SetOnChangedListener(new OnChangedListener() {
			
			@Override
			public void OnChanged(boolean CheckState) {
				Toast.makeText(con, ""+CheckState, Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	
	private OnClickListener l=new OnClickListener() {
		
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.passwordChange:
				 //密码修改操作
				Intent intent=new Intent(con,CommunityModifyPasswordActivity.class);
				startActivity(intent);
				break;
			case R.id.feedback:
				//意见反馈
				Intent i=new Intent(con,CommunitySettingFeedBack.class);
				startActivity(i);
				break;
			case R.id.about:
				//关于
				
				break;
			case R.id.nicknameChange:
				//修改昵称
				buttonToggleAction(nicknameChange,nicknameText);
				
				break;
			case R.id.birthChange:
				//生日
				showDialog(DATE_PICKER_ID);
				break;

			default:
				break;
			}
		}
	};

	
	public void buttonToggleAction(Button button,EditText editText)
	{
		if(clicked)
		{
			clicked=false;
		}else
		{
			clicked=true;
		}
		if(clicked)
		{
			editText.setEnabled(true);
			editText.requestFocus();
		}else
		{
			editText.setEnabled(false);
			editText.clearFocus();
		}
	}
	
	/** 
     * 内部匿名类，实现DatePickerDialog.OnDateSetListener接口，重写onDateSet()方法 
     * 当弹出DatePickerDialog并设置完Date以后，左下方有个“Set”按钮，表示确定设置。当这个 
     * 按钮被点击的时候，就执行这里的onDateSet()方法。 
     */  
    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {  
        public void onDateSet(DatePicker view, int year, int monthOfYear,  
                int dayOfMonth) {  
            birthText.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        }  
    };  
      
    /** 
     * 复写Activity的onCreateDialog()方法，当调用showDialog()方法的时候，就执行这里 
     * 显示DatePickerDialog。并且它的默认date有这里的参数指定，为2014-Fre-17. 
     * 月份的1表示二月，0表示一月。 
     */  
    protected Dialog onCreateDialog(int id) {  
    	Calendar calendar = Calendar.getInstance();  
        
        Dialog dialog = null;
        switch(id){  
        case DATE_PICKER_ID:  
        dialog = new DatePickerDialog(this, 
        		onDateSetListener, 
                calendar.get(Calendar.YEAR), 
                calendar.get(Calendar.MONTH), 
                calendar.get(Calendar.DAY_OF_MONTH));   
        }  
          
        return dialog;  
    } 
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_community_setting, menu);
		return false;
	}

}
