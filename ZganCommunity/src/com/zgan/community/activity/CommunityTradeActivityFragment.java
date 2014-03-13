package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
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
import android.widget.Button;
import android.widget.TextView;

public class CommunityTradeActivityFragment extends Fragment {

	private Button back;
	private TextView title;
	
	private Button supermarket;  //小超市
	private Button snack;        //快餐
	private Button pasta;        //面食
	private Button barbecue;     //烧烤
	private Button hairdressing; //美容美发
	private Button dryclean;     //干洗
	private Button leatherware;  //皮具保养
	private Button fondue;       //火锅
	private Button chinesemeal;  //中餐
	
	private Context con;
	private Intent intent=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.activity_community_trade, container, false);
		back=(Button) view.findViewById(R.id.back);
		title=(TextView) view.findViewById(R.id.title);
		//title.setText(R.string.community_trade);
		title.setBackgroundResource(R.drawable.title_shequshangquan);
		
		supermarket=(Button) view.findViewById(R.id.buttonSupermarket);
		snack=(Button) view.findViewById(R.id.buttonSnack);
		pasta=(Button) view.findViewById(R.id.buttonPasta);
		barbecue=(Button) view.findViewById(R.id.buttonBarbecue);
		hairdressing=(Button) view.findViewById(R.id.buttonHairdressing);
		dryclean=(Button) view.findViewById(R.id.buttonDryclean);
		leatherware=(Button) view.findViewById(R.id.buttonLeatherware);
		fondue=(Button) view.findViewById(R.id.buttonFondue);
		chinesemeal=(Button) view.findViewById(R.id.buttonChinesemeal);
		
		con = this.getActivity();        //初始化一个全局的Context
		
        ButtonClickListener l=new ButtonClickListener();       //按钮点击监听器初始化
		
        supermarket.setOnClickListener(l);                //按钮注册监听器
        snack.setOnClickListener(l);
        pasta.setOnClickListener(l);
        barbecue.setOnClickListener(l);
        hairdressing.setOnClickListener(l);                //按钮注册监听器
        dryclean.setOnClickListener(l);
        leatherware.setOnClickListener(l);
        fondue.setOnClickListener(l);
        chinesemeal.setOnClickListener(l);                //按钮注册监听器
        back.setOnClickListener(l);
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
            case R.id.buttonSupermarket:
            	//小超市点击响应事件
            	
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "餐饮美食");
				startActivity(intent);
				break;
            case R.id.buttonSnack:
            	//快餐点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "棋牌娱乐");
				startActivity(intent);
  				break;
            case R.id.buttonPasta:
            	//面食点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "便利超市");
				startActivity(intent);
  				break;
            case R.id.buttonBarbecue:
            	//烧烤点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "宠物美容");
				startActivity(intent);
  				break;
            case R.id.buttonHairdressing:
             	//美容美发点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "药房诊所");
				startActivity(intent);
  				break;
            case R.id.buttonDryclean:
             	//干洗点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "汽车美容");
				startActivity(intent);
  				break;
            case R.id.buttonLeatherware:
             	//皮具保养点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "母婴用品");
				startActivity(intent);
  				break;
            case R.id.buttonFondue:
             	//火锅点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "鲜花养殖");
				startActivity(intent);
  				break;
            case R.id.buttonChinesemeal:
              	//中餐点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "教育培训");
				startActivity(intent);
  				break;

			default:
				break;
			}
		}
		
	}

}
