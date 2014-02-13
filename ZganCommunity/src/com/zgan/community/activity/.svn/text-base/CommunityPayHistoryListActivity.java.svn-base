package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.adapter.CommunityPayAdapter.ViewHolder;
import com.zgan.community.data.News;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CommunityPayHistoryListActivity extends MainAcitivity {

	private Button back;
	private TextView title;
	private Context con;
	private ListView list;
	private CommunityPayHistoryAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_new_pay);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText(R.string.community_pay_history);
		con=CommunityPayHistoryListActivity.this;
		
		list=(ListView) findViewById(R.id.listViewDetail);
		
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        List<String> dataList=new ArrayList<String>();
        dataList.add("12月账单");
        dataList.add("12月账单");
        dataList.add("12月账单");
        dataList.add("12月账单");
        dataList.add("12月账单");
        
        adapter=new CommunityPayHistoryAdapter(dataList);
        list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_community_pay_history_list,
				menu);
		return false;
	}
	
	public class CommunityPayHistoryAdapter extends BaseAdapter {
		
		private LayoutInflater inflater;//得到一个LayoutInfalter对象用来导入布局 
		private List<String> dataList;
		
		/**
		 * 构造函数
		 * @param context
		 */
		public CommunityPayHistoryAdapter(List<String> list)
		{
			inflater=LayoutInflater.from(con);
			this.dataList=list;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;        
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.community_political_item,null);	
				holder = new ViewHolder();

				/**得到各个控件的对象*/                    
				holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
				//holder.date = (TextView) convertView.findViewById(R.id.itemDate);

				convertView.setTag(holder);//绑定ViewHolder对象                   
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                  
			}

			/**设置TextView显示的内容 和Title点击事件*/ 
			final String str=dataList.get(position);
			holder.title.setText(str);

			/**
			 * 为Item添加样式
			 */
			if (dataList.size() == 1) {
				convertView.setBackgroundResource(R.drawable.circle_list_single);
			} else if (dataList.size() > 1) {
				if (position == 0) {
					convertView.setBackgroundResource(R.drawable.circle_list_top);
				} else if (position == (dataList.size() - 1)) {
					convertView.setBackgroundResource(R.drawable.circle_list_bottom);
				} else {
					convertView.setBackgroundResource(R.drawable.circle_list_middle);
				}
			}

			return convertView;
	       }
		
		/**
		 * 存放控件
		 * */
		public final class ViewHolder{
			public TextView title;    
		}
	}

}
