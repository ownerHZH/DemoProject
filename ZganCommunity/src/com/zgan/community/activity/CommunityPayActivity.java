package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.activity.MainPageActivity.ButtonClickListener;
import com.zgan.community.adapter.CommunityPayAdapter;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CommunityPayActivity extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private ListView goodsManager;
	private ListView electric;
	private ListView water;
	private ListView phone;
	private TextView totalMoney;
	
	private CommunityPayAdapter communityPayAdapter;
	
	private Context con;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_pay);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		
		goodsManager=(ListView) findViewById(R.id.listViewGoodsManage);
		electric=(ListView) findViewById(R.id.listViewElectric);
		water=(ListView) findViewById(R.id.listViewWater);
		phone=(ListView) findViewById(R.id.listViewPhone);
		totalMoney=(TextView) findViewById(R.id.textViewTotalMoney);
		
		title.setText(R.string.community_pay);
		
		con = CommunityPayActivity.this;        //初始化一个全局的Context
		
		showList();
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	

	/**
	 * 使各List显示数据
	 */
	private void showList() {
		List<Map<String,String>> data=new ArrayList<Map<String,String>>();
		
		Map<String,String> map=new HashMap();
		map.put("date", "1日");
		map.put("money", "200");
		data.add(map);
		
		Map<String,String> map1=new HashMap();
		map1.put("date", "5日");
		map1.put("money", "400");
		data.add(map1);
		
		Map<String,String> map2=new HashMap();
		map2.put("date", "15日");
		map2.put("money", "1000");
		data.add(map2);
		
		int Total=0;		
		for(int i=0;i<data.size();i++)
		{
			Map<String,String> moneyMap=data.get(i);
			Total+=Integer.parseInt(moneyMap.get("money"));
		}
		
		communityPayAdapter=new CommunityPayAdapter(con,data);
		goodsManager.setAdapter(communityPayAdapter);
		electric.setAdapter(communityPayAdapter);
		water.setAdapter(communityPayAdapter);
		phone.setAdapter(communityPayAdapter);
		
		//总的费用
		totalMoney.setText("￥"+(Total*4));
		
		goodsManager.setDivider(null);
		electric.setDivider(null);
		phone.setDivider(null);
		water.setDivider(null);
		
		setListViewHeightBasedOnChildren(goodsManager);
		setListViewHeightBasedOnChildren(electric);
		setListViewHeightBasedOnChildren(phone);
		setListViewHeightBasedOnChildren(water);
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		((MarginLayoutParams) params).setMargins(0, 0, 0, 0);
		listView.setLayoutParams(params);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_pay, menu);
		return true;
	}

}
