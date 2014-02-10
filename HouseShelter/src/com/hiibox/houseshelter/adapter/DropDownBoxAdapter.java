package com.hiibox.houseshelter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hiibox.houseshelter.R;

    
  
  
  
  
  
  
  
public class DropDownBoxAdapter extends BaseAdapter {

	private Context context = null;
	private List<String> list = null;
	
	public DropDownBoxAdapter(Context context) {
		super();
		this.context = context;
	}
	
	public void setList(List<String> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String str = list.get(position);
		ViewHolder holder = new ViewHolder();
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.popupwindow_drop_down_item_layout, null);
		}
		holder.itemTV = (TextView) convertView.findViewById(R.id.popupwindown_item_tv);
		holder.itemTV.setText(str);
		return convertView;
	}
	
	class ViewHolder {
		TextView itemTV;
	}

}
