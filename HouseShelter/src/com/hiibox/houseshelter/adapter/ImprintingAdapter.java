package com.hiibox.houseshelter.adapter;

import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hiibox.houseshelter.R;

    
  
  
  
  
  
  
  
public class ImprintingAdapter extends BaseAdapter {
    
    private Context context = null;
    @SuppressWarnings("unused")
    private FinalBitmap finalBitmap = null;
    private List<Map<String, String>> list = null;
    private int index = 0;                           
    private Resources res = null;
    private Drawable outDoorDrawable = null;
    private Drawable atHomeDrawable = null;
    private String outDoor = null;
    private String atHome = null;
    private String intrude = null;
    private String temperatureUnit = null;
                                                

    public ImprintingAdapter(Context context, FinalBitmap finalBitmap) {
        super();
        this.context = context;
        this.finalBitmap = finalBitmap;
        res = context.getResources();
        outDoor = res.getString(R.string.out_door);
        atHome = res.getString(R.string.at_home);
        intrude = res.getString(R.string.invade);
        temperatureUnit = res.getString(R.string.temperature_unit);
        outDoorDrawable = res.getDrawable(R.drawable.out_door_iv);
        outDoorDrawable.setBounds(0, 0, outDoorDrawable.getMinimumWidth(), outDoorDrawable.getMinimumHeight());
        atHomeDrawable = res.getDrawable(R.drawable.at_home_iv);
        atHomeDrawable.setBounds(0, 0, atHomeDrawable.getMinimumWidth(), atHomeDrawable.getMinimumHeight());
    }
    
    public void setList(List<Map<String, String>> list, int index) {
        this.list = list;
        this.index = index;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
    	if (null == list) {
    		return 0;
    	}
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Map<String, String> map = (Map<String, String>) getItem(position);
                                                         
        if (index == 0) {
            RecordsHolder accessHolder = new RecordsHolder();
            convertView = View.inflate(context, R.layout.lv_item_access_records_layout, null);
            accessHolder.portraitIV = (ImageView) convertView.findViewById(R.id.member_head_portrait_iv);
            accessHolder.dateTV = (TextView) convertView.findViewById(R.id.access_date_tv);
            accessHolder.timeTV = (TextView) convertView.findViewById(R.id.access_time_tv);
            accessHolder.nameTV = (TextView) convertView.findViewById(R.id.member_name_tv);
            accessHolder.accessFlagTV = (TextView) convertView.findViewById(R.id.access_flag_tv);
            
                                                                                    
                                                         
            accessHolder.dateTV.setText(map.get("date"));
            accessHolder.timeTV.setText(map.get("time"));
            accessHolder.nameTV.setText(map.get("name"));
            String accessFlag = map.get("accessFlag");
                                                                                                                                                         
            if (accessFlag.equals("0")) {             
                accessHolder.accessFlagTV.setText(outDoor);
                accessHolder.accessFlagTV.setCompoundDrawables(null, outDoorDrawable, null, null);
            } else {
                accessHolder.accessFlagTV.setText(atHome);
                accessHolder.accessFlagTV.setCompoundDrawables(null, atHomeDrawable, null, null);
            }
        } 
        else if (index == 1) {
            RecordsHolder defenceHolder = new RecordsHolder();
            convertView = View.inflate(context, R.layout.lv_item_defence_records_layout, null);
            defenceHolder.portraitIV = (ImageView) convertView.findViewById(R.id.member_head_portrait_iv);
            defenceHolder.defenceStatusIV = (ImageView) convertView.findViewById(R.id.defence_flag_iv);
            defenceHolder.dateTV = (TextView) convertView.findViewById(R.id.access_date_tv);
            defenceHolder.timeTV = (TextView) convertView.findViewById(R.id.access_time_tv);
            defenceHolder.nameTV = (TextView) convertView.findViewById(R.id.member_name_tv);
            defenceHolder.phoneTV = (TextView) convertView.findViewById(R.id.member_phone_tv);
            
                                                                                     
            defenceHolder.dateTV.setText(map.get("date"));
            defenceHolder.timeTV.setText(map.get("time"));
            defenceHolder.nameTV.setText(map.get("name"));
            defenceHolder.phoneTV.setText(map.get("phone"));
            String defenceStatus = map.get("accessFlag");
            defenceHolder.defenceStatusIV.setBackgroundDrawable(null);
            if (defenceStatus.equals("1")) {            
                defenceHolder.defenceStatusIV.setBackgroundResource(R.drawable.defence_iv);
            } else {
                defenceHolder.defenceStatusIV.setBackgroundResource(R.drawable.cancle_defence_iv);
            }
        } 
        else if (index == 2) {
            WarningHolder warningHolder = new WarningHolder();
            convertView = View.inflate(context, R.layout.lv_item_warning_records_layout, null);
            warningHolder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            warningHolder.timeTV = (TextView) convertView.findViewById(R.id.warning_time_tv);
            warningHolder.warningTypeTV = (TextView) convertView.findViewById(R.id.warning_type_tv);
            warningHolder.tempTV = (TextView) convertView.findViewById(R.id.curr_temperature_tv);
            
            String warningType = map.get("warningType");
            warningHolder.layout.setBackgroundDrawable(null);
            if (warningType.equals("3")) {                    
                warningHolder.layout.setBackgroundResource(R.drawable.bg_intrude);
                warningHolder.tempTV.setVisibility(View.INVISIBLE);
                warningHolder.warningTypeTV.setText(intrude);
                warningHolder.warningTypeTV.setBackgroundResource(R.drawable.shape_btn_red);
            } else if (warningType.equals("16")) {
                warningHolder.layout.setBackgroundResource(R.drawable.bg_temperatrue_abnormal);
                warningHolder.tempTV.setText(map.get("temperature")+temperatureUnit);
            }
            warningHolder.timeTV.setText(map.get("time").substring(0, map.get("time").lastIndexOf(":")));
        }
        
        return convertView;
    }
    
    class RecordsHolder {
        ImageView portraitIV;
        TextView dateTV;
        TextView timeTV;
        TextView nameTV;
        TextView accessFlagTV;
        ImageView defenceStatusIV;
        TextView phoneTV;
    }
    
    class WarningHolder {
        LinearLayout layout;
        TextView timeTV;
        TextView warningTypeTV;
        TextView tempTV;
    }

}
