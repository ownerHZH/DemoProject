package com.zgan.community.adapter;

import java.util.List;

import com.zgan.community.R;
import com.zgan.community.adapter.CommunityPolicitalAdapter.ViewHolder;
import com.zgan.community.data.CommunityService;
import com.zgan.community.data.ServiceInfo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityServiceAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<ServiceInfo> list;
	

	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public CommunityServiceAdapter(Context context, List<ServiceInfo> serviceInfoList) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = serviceInfoList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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

			convertView = inflater.inflate(R.layout.community_service_item,null);
			holder = new ViewHolder();

			/** 得到各个控件的对象 */
			holder.name = (TextView) convertView.findViewById(R.id.itemName);
			holder.phone = (TextView) convertView.findViewById(R.id.itemPhone);
			holder.address = (TextView) convertView.findViewById(R.id.itemAddress);
			holder.call = (ImageButton) convertView.findViewById(R.id.imageButtonCall);
			convertView.setTag(holder);//绑定ViewHolder对象 
		}else
        {
            holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                  
        }

		/** 设置TextView显示的内容 */
		holder.name.setText(list.get(position).getSName().toString());
		holder.address.setText(list.get(position).getAddress());
		holder.phone.setText(list.get(position).getTel());

		final String number = list.get(position).getTel().toString();
		holder.call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 用intent启动拨打电话
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ number));
				context.startActivity(intent);
			}
		});
		
		if (list.size() == 1) {
            convertView.setBackgroundResource(R.drawable.circle_list_single);
        } else if (list.size() > 1) {
            if (position == 0) {
                convertView.setBackgroundResource(R.drawable.circle_list_top);
            } else if (position == (list.size() - 1)) {
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
		public TextView name; // 姓名
		public TextView phone;
		public TextView address; // 地址
		private ImageButton call;  //拨号按钮
	}

}
