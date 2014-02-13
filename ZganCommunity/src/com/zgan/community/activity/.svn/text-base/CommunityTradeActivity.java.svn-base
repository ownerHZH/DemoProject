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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CommunityTradeActivity extends MainAcitivity {

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
	private Button teahouse;     //茶楼
	private Button kidstrain;    //儿童培训
	private Button trafficsearch;//交通查询
	
	private Context con;
	private Intent intent=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_trade);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText(R.string.community_trade);
		
		supermarket=(Button) findViewById(R.id.buttonSupermarket);
		snack=(Button) findViewById(R.id.buttonSnack);
		pasta=(Button) findViewById(R.id.buttonPasta);
		barbecue=(Button) findViewById(R.id.buttonBarbecue);
		hairdressing=(Button) findViewById(R.id.buttonHairdressing);
		dryclean=(Button) findViewById(R.id.buttonDryclean);
		leatherware=(Button) findViewById(R.id.buttonLeatherware);
		fondue=(Button) findViewById(R.id.buttonFondue);
		chinesemeal=(Button) findViewById(R.id.buttonChinesemeal);
		teahouse=(Button) findViewById(R.id.buttonTeahouse);
		kidstrain=(Button) findViewById(R.id.buttonKidstrain);
		trafficsearch=(Button) findViewById(R.id.buttonTrafficsearch);
		
		con = CommunityTradeActivity.this;        //初始化一个全局的Context
		
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
        teahouse.setOnClickListener(l);
        kidstrain.setOnClickListener(l);
        trafficsearch.setOnClickListener(l);
        back.setOnClickListener(l);
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
            case R.id.buttonSupermarket:
            	//小超市点击响应事件
            	
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "超市");
				startActivity(intent);
				break;
            case R.id.buttonSnack:
            	//快餐点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "快餐");
				startActivity(intent);
  				break;
            case R.id.buttonPasta:
            	//面食点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "面食");
				startActivity(intent);
  				break;
            case R.id.buttonBarbecue:
            	//烧烤点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "烧烤");
				startActivity(intent);
  				break;
            case R.id.buttonHairdressing:
             	//美容美发点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "美容美发");
				startActivity(intent);
  				break;
            case R.id.buttonDryclean:
             	//干洗点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "干洗");
				startActivity(intent);
  				break;
            case R.id.buttonLeatherware:
             	//皮具保养点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "皮具保养");
				startActivity(intent);
  				break;
            case R.id.buttonFondue:
             	//火锅点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "火锅");
				startActivity(intent);
  				break;
            case R.id.buttonChinesemeal:
              	//中餐点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "中餐");
				startActivity(intent);
  				break;
            case R.id.buttonTeahouse:
              	//茶楼点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "茶楼");
				startActivity(intent);
  				break;
            case R.id.buttonTrafficsearch:
              	//公交站点点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "公交站点");
				startActivity(intent);
				break;

            case R.id.buttonKidstrain:
              	//儿童培训点击响应事件
            	intent=new Intent(con,ZganCommunityMapShow.class);
            	intent.putExtra("button_key", "儿童培训");
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
		//getMenuInflater().inflate(R.menu.activity_community_trade, menu);
		return true;
	}

}
