package com.hiibox.houseshelter.adapter;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.activity.AuthCodeDialogActivity;

    
  
  
  
  
  
  
  
public class AddressAdapter extends BaseAdapter {

    private Context context = null;
    private Activity mActivity = null;
    private List<Map<String, Object>> list = null;
    private String[] cnNumber = {"一","二","三","四","五","六","七","八","九","十"};
    private String address = null;
                                             
    
    public AddressAdapter(Context context, Activity maActivity) {
        super();
        this.context = context;
        this.mActivity = maActivity;
        address = context.getResources().getString(R.string.address);
                                                                                       
    }

    public void setList(List<Map<String, Object>> list) {
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

    @SuppressWarnings("unchecked")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Map<String, Object> map = (Map<String, Object>) getItem(position);
        ViewHolder holder = new ViewHolder();
        if (null == convertView) {
            convertView = View.inflate(context, R.layout.lv_item_address_layout, null);
        }
        holder.selectedIV = (ImageView) convertView.findViewById(R.id.selected_address_iv);
        holder.deleteIV = (ImageView) convertView.findViewById(R.id.delete_iv);
        holder.addressTitleTV = (TextView) convertView.findViewById(R.id.address_title_tv);
        holder.addressTV = (TextView) convertView.findViewById(R.id.address_tv);
        holder.deviceNumberTV = (TextView) convertView.findViewById(R.id.device_number_tv);
                                                                                      
        
        boolean defaultAddr = (Boolean) map.get("defaultAddr");
        if (!defaultAddr) {
            holder.selectedIV.setVisibility(View.INVISIBLE);
        } else {
            holder.selectedIV.setVisibility(View.VISIBLE);
        }
        if (position+1 > 10) {
            holder.addressTitleTV.setText(String.valueOf(position+1));
        } else {
            holder.addressTitleTV.setText(address+cnNumber[position]);
        }
        holder.addressTV.setText((CharSequence) map.get("address"));
        final String deviceNumber = (String) map.get("deviceNumber");
        holder.deviceNumberTV.setText(deviceNumber);
                                                                         
        
        final EditText et = new EditText(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.leftMargin = 10;
        params.rightMargin = 10;
        et.setLayoutParams(params);
        holder.deleteIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(mActivity, AuthCodeDialogActivity.class);
            	intent.putExtra("deviceNumber", deviceNumber);
            	intent.putExtra("position", position);
            	mActivity.startActivityForResult(intent, 0x104);
            }
        });
        
        return convertView;
    }
    
                                                                                   
                                                                                      
                                                                             
                                                                                       
                      
            
                                                                                       
                                           
                                                                        
                                                        
                                                     
                                                     
                
                                                        
                                                     
                                                     
                
                                                                                  
                                                                               
                                                                             
                      
            
                                                                                                 
                                                
                                                                                                                                  
            
                                                                                                           
                        
                                                          
                   
                
               
                        
                                                         
                            
                
              
        
    
    class ViewHolder {
        ImageView selectedIV;
        ImageView deleteIV;
        TextView addressTitleTV;
        TextView addressTV;
        TextView deviceNumberTV;
                               
    }

}
