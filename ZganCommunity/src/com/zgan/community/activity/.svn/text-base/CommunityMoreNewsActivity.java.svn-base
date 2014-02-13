package com.zgan.community.activity;

import java.util.List;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.activity.CommunityNotificationActivity.ClickListener;
import com.zgan.community.adapter.CommunityNewsAdapter;
import com.zgan.community.data.News;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityMoreNewsActivity extends MainAcitivity {

	private Button back;
	private TextView title;
	private ListView list;
	private CommunityNewsAdapter communityNewsAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_more_news);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText(R.string.news_list_title);
		list=(ListView)findViewById(R.id.newslist);
		list.setDivider(null);
		ClickListener l=new ClickListener();
		back.setOnClickListener(l);
		
		
		Bundle bundle=getIntent().getExtras();
		@SuppressWarnings("unchecked")
		List<News> newsList=(List<News>) bundle.getSerializable("news");
		if(newsList.size()>0)
		{
			communityNewsAdapter=new CommunityNewsAdapter(CommunityMoreNewsActivity.this, newsList);
			list.setAdapter(communityNewsAdapter);
		}else
		{
			Toast.makeText(this, "没有可加载的数据", 2).show();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_more_news, menu);
		return true;
	}
	
	public class ClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			default:
				break;
			}
		}
		
	}

}
