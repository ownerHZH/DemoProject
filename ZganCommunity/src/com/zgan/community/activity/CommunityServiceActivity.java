package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.baidu.ZganCommunityMapShow;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CommunityServiceActivity extends MainAcitivity {

	private Button back;           //返回
	private TextView title;        //标题
	
	private Button move;           //搬家
	private Button unlock;         //开锁服务
	private Button plumber;        //水电维修
	private Button pcrepair;       //电脑维修
	private Button recycle;        //闲置回收
	private Button pet;            //宠物之家
	private Button psychological;  //心理辅导
	private Button health;         //健康咨询
	private Button law;            //法律咨询
	private Button pipe;           //疏通管道
	private Button matron;         //月嫂
	private Button cleaning;       //保洁
	private Button nanny;          //保姆
	
	private Context con;
	private Intent intent=null;    //装载跳转界面的 意图
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_service);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText(R.string.community_service);    //标题 社区服务
		
		move=(Button) findViewById(R.id.buttonMove);
		unlock=(Button) findViewById(R.id.buttonUnlock);
		plumber=(Button) findViewById(R.id.buttonPlumber);
		pcrepair=(Button) findViewById(R.id.buttonPCrepair);
		recycle=(Button) findViewById(R.id.buttonRecycle);
		pet=(Button) findViewById(R.id.buttonPet);
		psychological=(Button) findViewById(R.id.buttonPsychological);
		health=(Button) findViewById(R.id.buttonHealth);
		law=(Button) findViewById(R.id.buttonLaw);
		pipe=(Button) findViewById(R.id.buttonPipe);
		matron=(Button) findViewById(R.id.buttonMatron);
		cleaning=(Button) findViewById(R.id.buttonCleaning);
		nanny=(Button) findViewById(R.id.buttonNanny);
		
		con=CommunityServiceActivity.this;                     //初始化Context
		
        ButtonClickListener l=new ButtonClickListener();       //按钮点击监听器初始化
		
        back.setOnClickListener(l);                            //按钮注册监听器
        move.setOnClickListener(l);
        unlock.setOnClickListener(l);
        plumber.setOnClickListener(l);
        pcrepair.setOnClickListener(l);
        recycle.setOnClickListener(l);
        pet.setOnClickListener(l);
        psychological.setOnClickListener(l);
        health.setOnClickListener(l);
        law.setOnClickListener(l);
        pipe.setOnClickListener(l);
        matron.setOnClickListener(l);
        cleaning.setOnClickListener(l);
        nanny.setOnClickListener(l);
	}

	public class ButtonClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				//返回点击响应事件
				finish();
				break;
            case R.id.buttonMove:
            	//搬家点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "搬家");
				startActivity(intent);
				break;
            case R.id.buttonUnlock:
            	//开锁服务点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "开锁服务");
				startActivity(intent);
				break;
            case R.id.buttonPlumber:
            	//水电维修点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "水电维修");
				startActivity(intent);
				break;
             case R.id.buttonPCrepair:
            	//电脑维修点击响应事件
            	 intent=new Intent(con,ZganCommunityMapShow.class);
             	intent.putExtra("button_key", "电脑维修");
 				startActivity(intent);
 				break;
            case R.id.buttonRecycle:
            	//闲置回收点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "闲置回收");
				startActivity(intent);
				break;
            case R.id.buttonPet:
            	//宠物之家点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "宠物之家");
				startActivity(intent);
				break;
            case R.id.buttonPsychological:
            	//心理辅导点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "心理辅导");
				startActivity(intent);
				break;
            case R.id.buttonHealth:
            	//健康咨询点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "健康咨询");
				startActivity(intent);
				break;
            case R.id.buttonLaw:
            	//法律咨询点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "法律咨询");
				startActivity(intent);
				break;
            case R.id.buttonPipe:
            	//疏通管道点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "疏通管道");
				startActivity(intent);
				break;
            case R.id.buttonMatron:
            	//月嫂点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "月嫂");
				startActivity(intent);
				break;
            case R.id.buttonCleaning:
            	//保洁点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "保洁");
				startActivity(intent);
				break;
            case R.id.buttonNanny:
            	//保姆点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key","保姆");//nanny.getText().toString()
				startActivity(intent);
				break;

			default:
				break;
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_service, menu);
		return true;
	}

}
