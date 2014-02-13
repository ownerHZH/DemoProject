package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.data.ContentData;
import com.zgan.community.data.MSZW_BGDD;
import com.zgan.community.data.News;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityPolicitalDetailActivity extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private TextView cTitle;  //信息标题
	private TextView date;    //发布时间
	private TextView content; //内容
	
	private ContentData contentData=new ContentData();
	private MSZW_BGDD mszwBgdd=new MSZW_BGDD();
	
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_policital_detail);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		
		cTitle=(TextView) findViewById(R.id.textViewTitle);
		date=(TextView) findViewById(R.id.textViewDate);
		content=(TextView) findViewById(R.id.textViewContent);
		
		title.setText(R.string.community_policital_detail_title);
		
		context=CommunityPolicitalDetailActivity.this;
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setContent(cTitle,date,content);
	}

	private void setContent(TextView title, TextView date, TextView content) {
		Bundle bundle=getIntent().getExtras();
		int pageNumber=bundle.getInt("pageNumber");
		if(pageNumber==1)
		{
			contentData=(ContentData) bundle.getSerializable("contentData");
			if(contentData==null)
			{
				Toast.makeText(context, "数据不完整", 2).show();
			}else
			{
				title.setText(contentData.getTitle());
				date.setText(contentData.getContentTime());
				content.setText(Html.fromHtml(contentData.getCContent()));
			}		
		}else if(pageNumber==2)
		{
			mszwBgdd=(MSZW_BGDD) bundle.getSerializable("mszwBgdd");
			if(mszwBgdd==null)
			{
				Toast.makeText(context, "数据不完整", 2).show();
			}else
			{
				title.setText(mszwBgdd.getSName());
				date.setText(mszwBgdd.getPublishTime());
				content.setText(Html.fromHtml("   地址："+mszwBgdd.getAddress()+"\r\n<br/>"+
						mszwBgdd.getAddLX()+
						"<br/>联系电话："+mszwBgdd.getTel()));
			}		
		}else if(pageNumber==20)
		{
			//显示更多通知的列表信息
			News news=(News) bundle.getSerializable("newsData");
			if(news==null)
			{
				Toast.makeText(context, "数据不完整", 2).show();
			}else
			{
				title.setText(news.getTitle());
				date.setText(news.getReleaseTime());
				content.setText(Html.fromHtml(news.getNContent()));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_policital_detail,menu);
		return true;
	}

}
