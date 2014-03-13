package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.baidu.ZganCommunityMapShow;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CommunityServiceActivityFragment extends Fragment {

	private Button back;           //返回
	private TextView title;        //标题
	
	private Button move;           //搬家
	private Button unlock;         //开锁服务
	private Button plumber;        //水电维修
	private Button pcrepair;       //电脑维修
	private Button recycle;        //闲置回收
	private Button pet;            //宠物之家
	//private Button psychological;  //心理辅导
	//private Button health;         //健康咨询
	//private Button law;            //法律咨询
	private Button pipe;           //疏通管道
	private Button matron;         //月嫂
	private Button cleaning;       //保洁
	//private Button nanny;          //保姆
	
	private Context con;
	private Intent intent=null;    //装载跳转界面的 意图

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.activity_community_service, container,false);
		
		back=(Button) view.findViewById(R.id.back);
		title=(TextView) view.findViewById(R.id.title);
		//title.setText(R.string.community_service);    //标题 社区服务
		title.setBackgroundResource(R.drawable.title_shequfuwu);
		
		move=(Button) view.findViewById(R.id.buttonMove);
		unlock=(Button) view.findViewById(R.id.buttonUnlock);
		plumber=(Button) view.findViewById(R.id.buttonPlumber);
		pcrepair=(Button) view.findViewById(R.id.buttonPCrepair);
		recycle=(Button) view.findViewById(R.id.buttonRecycle);
		pet=(Button) view.findViewById(R.id.buttonPet);
		pipe=(Button) view.findViewById(R.id.buttonPipe);
		matron=(Button) view.findViewById(R.id.buttonMatron);
		cleaning=(Button) view.findViewById(R.id.buttonCleaning);
		
		con=this.getActivity();                     //初始化Context
		
        ButtonClickListener l=new ButtonClickListener();       //按钮点击监听器初始化
		
        back.setOnClickListener(l);                            //按钮注册监听器
        move.setOnClickListener(l);
        unlock.setOnClickListener(l);
        plumber.setOnClickListener(l);
        pcrepair.setOnClickListener(l);
        recycle.setOnClickListener(l);
        pet.setOnClickListener(l);
        pipe.setOnClickListener(l);
        matron.setOnClickListener(l);
        cleaning.setOnClickListener(l);
		return view;
	}

	public class ButtonClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				//返回点击响应事件
				((Activity) con).finish();
				break;
            case R.id.buttonMove:
            	//搬家点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "开锁服务");
				startActivity(intent);
				break;
            case R.id.buttonUnlock:
            	//开锁服务点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "水电维修");
				startActivity(intent);
				break;
            case R.id.buttonPlumber:
            	//水电维修点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "家政保洁");
				startActivity(intent);
				break;
             case R.id.buttonPCrepair:
            	//电脑维修点击响应事件
            	 intent=new Intent(con,ZganCommunityMapShow.class);
             	intent.putExtra("button_key", "搬家公司");
 				startActivity(intent);
 				break;
            case R.id.buttonRecycle:
            	//闲置回收点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "月嫂保姆");
				startActivity(intent);
				break;
            case R.id.buttonPet:
            	//宠物之家点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "二手回收");
				startActivity(intent);
				break;
            case R.id.buttonPipe:
            	//疏通管道点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "家电维修");
				startActivity(intent);
				break;
            case R.id.buttonMatron:
            	//月嫂点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "管道疏通");
				startActivity(intent);
				break;
            case R.id.buttonCleaning:
            	//保洁点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "洗衣服务");
				startActivity(intent);
				break;

			default:
				break;
			}
		}
		
	}

}
