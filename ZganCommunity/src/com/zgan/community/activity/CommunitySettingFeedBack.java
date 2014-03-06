package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.List;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.data.User;
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
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CommunitySettingFeedBack extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private Button commit;
	private Context con;
	private EditText adviceInput;//意见输入框
	
    private MyProgressDialog pdialog;
    private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_setting_feed_back);
		con = CommunitySettingFeedBack.this;
		back = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		adviceInput=(EditText) findViewById(R.id.adviceInput);
		title.setText("意见反馈");
		handler=new Handler();
		commit=(Button) findViewById(R.id.buttonCommit);
		commit.setOnClickListener(l);
		back.setOnClickListener(l);
	}
	
	private OnClickListener l=new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.back:
					finish();
					break;
				case R.id.buttonCommit:
					String advice=adviceInput.getText().toString().trim();
					if(advice!=null&&!advice.equals(""))
					{
						commitAdvice(advice);
					}else
					{
						Toast.makeText(con, "提交意见不能为空！", Toast.LENGTH_SHORT).show();
					}
					
					break;
	
				default:
					break;
				}
			}
		};
		
		// 提交反馈意见信息
		private void commitAdvice(String advice) {
			HttpClientService svr = new HttpClientService(
					AppConstants.HttpHostAdress+"zganwymsg.aspx");
			//参数
			svr.addParameter("did",ZganCommunityStaticData.User_Number);
			svr.addParameter("method","wyfq");
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
				adviceInput.setText("");
			}
		};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_community_setting_feed_back,
				menu);
		return false;
	}

}
