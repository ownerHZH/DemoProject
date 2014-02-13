package com.zgan.community.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zgan.community.R;
import com.zgan.community.adapter.CommunityPayAdapter;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.os.Build.VERSION;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityNewPayActivity extends MainAcitivity {

	private Button back;
	private TextView title;
	private Context con;
	
	private DatePicker datePicker;
	private ListView listViewDetail;
	private Button morePayButton;
	
	private CommunityPayAdapter communityPayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_new_pay);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText(R.string.community_pay);
		con=CommunityNewPayActivity.this;
		initView();//初始化控件
								
        back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        showDetail();//显示假定数据
	}
	
	public void initView()
	{
		listViewDetail=(ListView) findViewById(R.id.listViewDetail);
		LayoutInflater inflater=LayoutInflater.from(con);
		View datePickerLayout=inflater.inflate(R.layout.date_picker_signal_layout,null);
		View morePayButtonLayout=inflater.inflate(R.layout.button_signal_layout,null);
		datePicker=(DatePicker)datePickerLayout.findViewById(R.id.datePicker1);
		morePayButton=(Button)morePayButtonLayout.findViewById(R.id.buttonMorePay);
		listViewDetail.addHeaderView(datePickerLayout);
		listViewDetail.addFooterView(morePayButtonLayout);
		datePickerInit();//DatePicker控件初始化
		morePayButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(con, "你点击了按钮", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(con,CommunityPayHistoryListActivity.class);
				startActivity(intent);
			}
		});
	}
	
	public void datePickerInit()
	{
		hideDayFromDatePicker(datePicker);
		Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){

            public void onDateChanged(DatePicker view, int year,
                    int monthOfYear, int dayOfMonth) {
                Toast.makeText(con,"您选择的日期是："+year+"年"+(monthOfYear+1)+"月",1).show();
                Map<String,String> map=new HashMap();
        		map.put("date", "物业账单");
        		map.put("money", "200");
        		data.add(map);
        		communityPayAdapter.notifyDataSetChanged();
            }
            
        });
	}
	
	List<Map<String,String>> data=new ArrayList<Map<String,String>>();
	/**
	 * 使各List显示数据
	 */
	private void showDetail() {
		//data=new ArrayList<Map<String,String>>();
		
		Map<String,String> map=new HashMap();
		map.put("date", "物业账单");
		map.put("money", "200");
		data.add(map);
		
		Map<String,String> map1=new HashMap();
		map1.put("date", "水费");
		map1.put("money", "400");
		data.add(map1);
		
		Map<String,String> map2=new HashMap();
		map2.put("date", "电费");
		map2.put("money", "1000");
		data.add(map2);	
		
		Map<String,String> map3=new HashMap();
		map3.put("date", "燃气费");
		map3.put("money", "1000");
		data.add(map3);
		
		communityPayAdapter=new CommunityPayAdapter(con,data);
		listViewDetail.setAdapter(communityPayAdapter);		
		listViewDetail.setDivider(null);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_community_new_pay, menu);
		return false;
	}
    
    /**
     * 隐藏DatePicker的日，只显示年和月
     * @param DatePicker dp
     */
    private void hideDayFromDatePicker(DatePicker dp)
    {
    	if (dp != null) {  
            Class c=dp.getClass();  
            Field f;  
            try {  
                    if(Integer.parseInt(VERSION.SDK) > 14){  
                        f = c.getDeclaredField("mDaySpinner");  
                        f.setAccessible(true );    
                        LinearLayout l= (LinearLayout)f.get(dp);     
                        l.setVisibility(View.GONE);  
                    }else{  
                        f = c.getDeclaredField("mDayPicker");  
                        f.setAccessible(true );    
                        LinearLayout l= (LinearLayout)f.get(dp);     
                        l.setVisibility(View.GONE);  
                    }  
            } catch (SecurityException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (NoSuchFieldException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (IllegalArgumentException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            } catch (IllegalAccessException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }    
              
        }   
    }
       

}
