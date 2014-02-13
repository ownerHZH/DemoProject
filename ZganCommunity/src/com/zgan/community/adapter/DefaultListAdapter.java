package com.zgan.community.adapter;

import java.util.List;
import java.util.Map;

import com.zgan.community.R;
import com.zgan.community.data.CommunityService;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DefaultListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;//得到一个LayoutInfalter对象用来导入布局 
	private List<String> list;
	private EditText editText;
	private Handler handler;
	
	/**
	 * 构造函数
	 * @param context
	 * @param editText 
	 * @param listHandler 
	 */
	public DefaultListAdapter(Context context,List list, EditText editText, Handler listHandler)
	{
		this.context=context;
		inflater=LayoutInflater.from(context);
		this.list=list;
		this.editText=editText;
		handler=listHandler;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		 ViewHolder holder;        
         if (convertView == null) {
        	 
	          convertView = inflater.inflate(R.layout.default_list_item,null);	
	          holder = new ViewHolder();
	          
	         /**得到各个控件的对象*/                    
	         holder.item = (TextView) convertView.findViewById(R.id.textViewDefault);
	         
	         convertView.setTag(holder);//绑定ViewHolder对象                   
         }
         else
         {
             holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象                  
         }
         
         final String n=list.get(position).toString();
         /**设置TextView显示的内容*/            
         holder.item.setText(n);
         
         holder.item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editText.setText(n);
				handler.sendEmptyMessage(0x123);
			}
		});
         
         /*if (list.size() == 1) {
             convertView.setBackgroundResource(R.drawable.circle_list_single);
         } else if (list.size() > 1) {
             if (position == 0) {
                 convertView.setBackgroundResource(R.drawable.circle_list_top);
             } else if (position == (list.size() - 1)) {
                 convertView.setBackgroundResource(R.drawable.circle_list_bottom);
             } else {
                 convertView.setBackgroundResource(R.drawable.circle_list_middle);
             }
         }*/
          
         return convertView;
       }
	
	/**
	 * 存放控件
	 * */
	public final class ViewHolder{
		public TextView item;         
	}
}
	
	

