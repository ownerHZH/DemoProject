package com.zgan.community.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zgan.community.R;
import com.zgan.community.adapter.CommunityPayAdapter;
import com.zgan.community.data.Pay;
import com.zgan.community.data.Recinfo;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.jsontool.GsonUtil;
import com.zgan.community.jsontool.HttpAndroidTask;
import com.zgan.community.jsontool.HttpClientService;
import com.zgan.community.jsontool.HttpPreExecuteHandler;
import com.zgan.community.jsontool.HttpResponseHandler;
import com.zgan.community.jsontool.JsonEntity;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.ZganCommunityStaticData;

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
	//private Button morePayButton;
	
	private CommunityPayAdapter communityPayAdapter;
	
	List<Pay> data=new ArrayList<Pay>();
	
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
              	
		listViewDetail.setDivider(null);
	}
	
	public void initView()
	{
		listViewDetail=(ListView) findViewById(R.id.listViewDetail);
		LayoutInflater inflater=LayoutInflater.from(con);
		View datePickerLayout=inflater.inflate(R.layout.date_picker_signal_layout,null);
		//View morePayButtonLayout=inflater.inflate(R.layout.button_signal_layout,null);
		datePicker=(DatePicker)datePickerLayout.findViewById(R.id.datePicker1);
		//morePayButton=(Button)morePayButtonLayout.findViewById(R.id.buttonMorePay);
		listViewDetail.addHeaderView(datePickerLayout);
		//listViewDetail.addFooterView(morePayButtonLayout);
		datePickerInit();//DatePicker控件初始化
		/*morePayButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Toast.makeText(con, "你点击了按钮", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(con,CommunityPayHistoryListActivity.class);
				startActivity(intent);
			}
		});*/
		
		initData();

	}

	private void initData() {
		Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int monthOfYear=calendar.get(Calendar.MONTH);
        int y=monthOfYear+1;
        String sy = "01";
        if(y<10)
        {
        	sy="0"+y;
        }
		getData(year+sy);
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
                int y=monthOfYear+1;
                String sy = "01";
                if(y<10)
                {
                	sy="0"+y;
                }
        		getData(year+sy);
            }
            
        });
	}
	
	//获取办事指南信息
	private void getData(String ny) {

		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"zganwyzd.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
		//参数
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		svr.addParameter("ny",ny);
				
		HttpAndroidTask task = new HttpAndroidTask(con, svr,
				new HttpResponseHandler() {
					// 响应事件
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,con,false);
						if (jsonEntity.getStatus() == 1) {
							Toast.makeText(con, "无此信息", Toast.LENGTH_SHORT).show();
						} else if (jsonEntity.getStatus() == 0) {
							data.clear();
							data=(List<Pay>) GsonUtil.getData(
										jsonEntity,AppConstants.type_payList);	
								
								if(data.size()>0)
				                {
				                	//有数据的时候操作
									communityPayAdapter=new CommunityPayAdapter(con,data);
									listViewDetail.setAdapter(communityPayAdapter);	
									communityPayAdapter.notifyDataSetChanged();
				                }else
				                {
				                	//没有数据时候提示
				                	Toast.makeText(con, "无此信息", Toast.LENGTH_SHORT).show();
				                }														
						}														
					}
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
					}
				});
		task.execute(new String[] {});	
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
    	((ViewGroup) dp.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
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
