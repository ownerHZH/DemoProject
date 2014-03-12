package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.jsontool.DialogUtil;
import com.zgan.community.jsontool.GsonUtil;
import com.zgan.community.jsontool.HttpAndroidTask;
import com.zgan.community.jsontool.HttpClientService;
import com.zgan.community.jsontool.HttpPreExecuteHandler;
import com.zgan.community.jsontool.HttpResponseHandler;
import com.zgan.community.jsontool.JsonEntity;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.MyProgressDialog;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.os.Bundle;
import android.os.Handler;
import android.app.TabActivity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityContactProperty extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private Button commit;
	//private RadioGroup radioGroup;
	private EditText editText;
	private Context con;
	
	private RadioButton radio0,radio1,radio2,radio3,radio4;
	private TextView radio0Text,radio1Text,radio2Text,radio3Text,radio4Text;
	private MyProgressDialog pdialog;
    private Handler handler;
    
    private String str="";
    private View view=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_contact_property);
		
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		title.setText(R.string.community_contact_property);
		commit=(Button) findViewById(R.id.buttonCommit);
		//radioGroup=(RadioGroup) findViewById(R.id.radioGroup1);
		editText=(EditText) findViewById(R.id.adviceInput);
		editText.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				TabHost tabhost = ((TabActivity)getParent()).getTabHost();
				view=((ViewGroup)tabhost.getChildAt(0)).getChildAt(2);				
				view.setVisibility(View.GONE);
				return false;
			}
		});
		
		handler=new Handler();
		
		radio0=(RadioButton) findViewById(R.id.radio0);		
		radio0.setOnCheckedChangeListener(radioListener);
		radio0Text=(TextView) findViewById(R.id.radio0Text);
		
		radio1=(RadioButton) findViewById(R.id.radio1);
		radio1.setOnCheckedChangeListener(radioListener);
		radio1Text=(TextView) findViewById(R.id.radio1Text);
		
		radio2=(RadioButton) findViewById(R.id.radio2);
		radio2.setOnCheckedChangeListener(radioListener);
		radio2Text=(TextView) findViewById(R.id.radio2Text);
		
		radio3=(RadioButton) findViewById(R.id.radio3);
		radio3.setOnCheckedChangeListener(radioListener);
		radio3Text=(TextView) findViewById(R.id.radio3Text);
		
		radio4=(RadioButton) findViewById(R.id.radio4);
		radio4.setOnCheckedChangeListener(radioListener);
		radio4Text=(TextView) findViewById(R.id.radio4Text);
		
		con = CommunityContactProperty.this;
		
		back.setOnClickListener(l);
		commit.setOnClickListener(l);
		
		//radioGroup.setOnCheckedChangeListener(listener);
	}
	
	OnCheckedChangeListener radioListener=new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {	
			setTabVisible();
			if(isChecked)
			{
				str=check(buttonView.getId());//只让一个RadioButton被选择并获取要提交的字符串	
			}
			
		}				
	};
	
	// 提交联系物业的信息
	private void commit(String advice) {
		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"zganwymsg.aspx");
		//参数
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		svr.addParameter("method","wymsg");
		svr.addParameter("msg",advice);
				
		HttpAndroidTask task = new HttpAndroidTask(con, svr,
				new HttpResponseHandler() {
					// 响应事件
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						pdialog.stop();
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,con,false);
						if (jsonEntity.getStatus() == 1) {
							Toast.makeText(con, "提交失败！", Toast.LENGTH_SHORT).show();
						} else if (jsonEntity.getStatus() == 0) {																						
							handler.post(r);
						}														
					}					
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
						pdialog = new MyProgressDialog(context);
						DialogUtil.setAttr4progressDialog(pdialog);
					}
				});
		task.execute(new String[] {});	
	}
	
	Runnable r=new Runnable() {
		
		@Override
		public void run() {
			Toast.makeText(con, "提交成功！", Toast.LENGTH_SHORT).show();
			radio0.setChecked(false);
			radio1.setChecked(false);
			radio2.setChecked(false);
			radio3.setChecked(false);
			radio4.setChecked(false);
			editText.setText("");
		}
	};
	
	/**
	 * 只让给定的id RadioButton被选择 并获取要提交的字符串
	 * @param i
	 */
	private String check(int i) {
		String commitString="";
		switch (i) {
		case R.id.radio0:
			radio1.setChecked(false);
			radio2.setChecked(false);
			radio3.setChecked(false);
			radio4.setChecked(false);
			editText.setText("");
			editText.setEnabled(false);
			commitString=radio0Text.getText().toString().trim();
			break;
		case R.id.radio1:
			radio0.setChecked(false);
			radio2.setChecked(false);
			radio3.setChecked(false);
			radio4.setChecked(false);
			editText.setText("");
			editText.setEnabled(false);
			commitString=radio1Text.getText().toString().trim();
			break;
		case R.id.radio2:
			radio0.setChecked(false);
			radio1.setChecked(false);
			radio3.setChecked(false);
			radio4.setChecked(false);
			editText.setText("");
			editText.setEnabled(false);
			commitString=radio2Text.getText().toString().trim();
			break;
		case R.id.radio3:
			radio0.setChecked(false);
			radio2.setChecked(false);
			radio1.setChecked(false);
			radio4.setChecked(false);
			editText.setText("");
			editText.setEnabled(false);
			commitString=radio3Text.getText().toString().trim();
			break;
		case R.id.radio4:
			radio0.setChecked(false);
			radio2.setChecked(false);
			radio3.setChecked(false);
			radio1.setChecked(false);
			editText.setEnabled(true);
			commitString=editText.getText().toString().trim();
			break;

		default:
			break;
		}
		return commitString;
	}

	private void commitClick() {
		if(radio4.isChecked())
		{
			str=editText.getText().toString().trim();
		}
		if(str!=null&&!str.equals(""))
		{
			commit(str);
		}else
		{
			Toast.makeText(con, "建议不能为空", Toast.LENGTH_SHORT).show();
		}
	}
	
	private OnClickListener l=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
			case R.id.buttonCommit:
				setTabVisible();
				commitClick();
				break;

			default:
				break;
			}
		}	
	};
	
	private void setTabVisible() {
		if(view!=null&&!view.equals(null))
		{
			view.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_community_contact_property,
				menu);
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			setTabVisible();
		}		
		return super.onTouchEvent(event);
	}

}
