package com.zgan.community.adapter;

import java.util.List;

import com.zgan.community.R;
import com.zgan.community.activity.Reinfo_son_details;
import com.zgan.community.baidu.ZganCommunityMapListDetial;
import com.zgan.community.data.Recinfo;
import com.zgan.community.tools.MapShow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListMapShowAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	List<MapShow> list;

	TextView store_name;
	TextView store_address;
	TextView store_tel;

	MapShow mapShowlist = null;

	public ListMapShowAdapter(Context context, List<MapShow> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.line_maplistdetails, null);
		}

		store_name = (TextView) convertView.findViewById(R.id.store_name);
		store_address = (TextView) convertView.findViewById(R.id.store_adress);
		store_tel = (TextView) convertView.findViewById(R.id.store_tel);

		mapShowlist = list.get(position);

		store_name.setText(mapShowlist.getS_name());
		store_address.setText("µÿ÷∑£∫"+mapShowlist.getS_address());
		store_tel.setText("TEL:"+mapShowlist.getS_tel());

		return convertView;
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

}
