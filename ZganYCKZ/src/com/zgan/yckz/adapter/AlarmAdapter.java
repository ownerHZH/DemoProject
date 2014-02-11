package com.zgan.yckz.adapter;

import java.util.List;

import com.zgan.yckz.R;
import com.zgan.yckz.tools.SheBei;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlarmAdapter extends BaseAdapter {
	List<SheBei> list;
	Context context;

	public AlarmAdapter(Context context, List<SheBei> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.list_alarm_item, null);
			
		}
		TextView alarm_time=(TextView)convertView.findViewById(R.id.alarm_time);
		TextView alarm_type=(TextView)convertView.findViewById(R.id.alarm_type);
		alarm_time.setText(list.get(position).getAlarm_time());
		
		return convertView;
	}

}
