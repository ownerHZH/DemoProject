package com.zgan.community.adapter;

import java.util.List;

import com.zgan.community.R;
import com.zgan.community.activity.Reinfo_son_details;
import com.zgan.community.data.Recinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReinfoAdapter extends BaseAdapter {
	LayoutInflater inflater;
	Context context;
	List<Recinfo> list;

	TextView company;
	TextView recruitment_info;

	Recinfo recinfo = null;

	public ReinfoAdapter(Context context, List<Recinfo> list) {
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
			convertView = inflater.inflate(R.layout.list_recinfo_item, null);
		}
		LinearLayout layout = (LinearLayout) convertView
				.findViewById(R.id.list_item);

		company = (TextView) convertView.findViewById(R.id.company);

		recinfo = list.get(position);
		
		company.setText(recinfo.getCompany());

		layout.setTag(recinfo);
		layout.setOnClickListener(listener);

		return convertView;
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(v.getContext(), Reinfo_son_details.class);
			Bundle bundle = new Bundle();
			recinfo = (Recinfo) v.getTag();
			bundle.putString("company", recinfo.getCompany());
			bundle.putString("Recruitment_info", recinfo.getRecruitment_info());

			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	};

}
