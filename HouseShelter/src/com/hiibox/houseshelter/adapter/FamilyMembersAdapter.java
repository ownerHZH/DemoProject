package com.hiibox.houseshelter.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hiibox.houseshelter.R;

    
  
  
  
  
  
public class FamilyMembersAdapter extends BaseAdapter {

	private Context context = null;
	private List<String> list = null;
	
	public FamilyMembersAdapter(Context context, List<String> list) {
        super();
        this.context = context;
        this.list = list;
    }

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String member = getItem(position);
		ViewHolder holder = new ViewHolder();
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_family_members_item_layout, null);
		}
		holder.itemTV = (TextView) convertView.findViewById(R.id.family_member_name_tv);
		holder.itemTV.setText(member);
		return convertView;
	}
	
	class ViewHolder {
		TextView itemTV;
	}

}
