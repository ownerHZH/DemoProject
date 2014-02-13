package com.zgan.community.adapter;

import java.util.List;

import com.zgan.community.R;
import com.zgan.community.activity.CommunityPolicitalDetailActivity;
import com.zgan.community.data.ContentData;
import com.zgan.community.data.MSZW_BGDD;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommunityPolicitalAdapter extends BaseAdapter {

	private LayoutInflater inflater;//得到一个LayoutInfalter对象用来导入布局 
	/*	private List<ContentData> contentDataList;
	private List<MSZW_BGDD> MSZW_BGDDList;*/
	private List<?> dataList;
	private int currentP=0;
	private Context con;

	/**
	 * 构造函数
	 * @param context
	 * @param i 
	 */
	public CommunityPolicitalAdapter(Context context,List<?> list, int i)
	{
		con=context;
		inflater=LayoutInflater.from(context);
		currentP=i;
		/*if(i==1)
		{
			contentDataList=list;
		}else if(i==2)
		{
			MSZW_BGDDList=list;
		}*/
		dataList=list;
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

	@SuppressWarnings("unchecked")
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
		if(currentP==1)
		{
			final ContentData contentData=((List<ContentData>)dataList).get(position);
			holder.title.setText(contentData.getTitle());
			holder.title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent=new Intent(con,CommunityPolicitalDetailActivity.class);
				    Bundle bundle=new Bundle();
					bundle.putSerializable("contentData", contentData);
					bundle.putInt("pageNumber", 1);
					intent.putExtras(bundle);
					con.startActivity(intent);
				}
			});
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
		}else
		{
			final MSZW_BGDD mszwBgdd=((List<MSZW_BGDD>)dataList).get(position);
			holder.title.setText(mszwBgdd.getSName());
			holder.title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent=new Intent(con,CommunityPolicitalDetailActivity.class);
				    Bundle bundle=new Bundle();
					bundle.putSerializable("mszwBgdd", mszwBgdd);
					bundle.putInt("pageNumber", 2);
					intent.putExtras(bundle);
					con.startActivity(intent);
				}
			});
			
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
		}
		//holder.title.setText(dataList.get(position).toString());
		//holder.date.setText(list.get(position).toString());
		return convertView;
	}

	/**
	 * 存放控件
	 * */
	public final class ViewHolder{
		public TextView title;   //显示装载的时间
		//public TextView date;    //日期
	}
}



