package com.zgan.community.activity;

import java.util.List;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.data.BgddDetail;
import com.zgan.community.data.ContentData;
import com.zgan.community.data.MSZW_BGDD;
import com.zgan.community.data.News;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.jsontool.DialogUtil;
import com.zgan.community.jsontool.GsonUtil;
import com.zgan.community.jsontool.HttpAndroidTask;
import com.zgan.community.jsontool.HttpClientService;
import com.zgan.community.jsontool.HttpPreExecuteHandler;
import com.zgan.community.jsontool.HttpResponseHandler;
import com.zgan.community.jsontool.JsonEntity;
import com.zgan.community.tools.MainAcitivity;
import com.zgan.community.tools.MyProgressDialog;
import com.zgan.community.tools.ZganCommunityStaticData;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityPolicitalDetailActivity extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private TextView cTitle;  //信息标题
	private TextView date;    //发布时间
	private TextView staticDate;    //发布时间
	private TextView content; //内容
	
	private ContentData contentData=new ContentData();
	private MSZW_BGDD mszwBgdd=new MSZW_BGDD();
	private List<BgddDetail> bgddDetailList;
	
	private Context context;
	private MyProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_policital_detail);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		
		cTitle=(TextView) findViewById(R.id.textViewTitle);
		date=(TextView) findViewById(R.id.textViewDate);
		staticDate=(TextView) findViewById(R.id.textViewApply);
		content=(TextView) findViewById(R.id.textViewContent);
		
		title.setText(R.string.community_policital_detail_title);
		
		context=CommunityPolicitalDetailActivity.this;
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		setContent(cTitle,date,content);
	}

	private void setContent(TextView title, TextView date, TextView content) {
		Bundle bundle=getIntent().getExtras();
		int pageNumber=bundle.getInt("pageNumber");
		if(pageNumber==1)
		{
			contentData=(ContentData) bundle.getSerializable("contentData");
			if(contentData==null)
			{
				Toast.makeText(context, "数据不完整", 2).show();
			}else
			{
				title.setText(contentData.getTitle());
				date.setText(contentData.getReleasetime());
				content.setText(Html.fromHtml(contentData.getContent()));
			}		
		}else if(pageNumber==2)
		{
			mszwBgdd=(MSZW_BGDD) bundle.getSerializable("mszwBgdd");
			if(mszwBgdd==null)
			{
				Toast.makeText(context, "数据不完整", 2).show();
			}else
			{
				title.setText(mszwBgdd.getName());
				date.setVisibility(View.GONE);
				staticDate.setVisibility(View.INVISIBLE);
				//获取详细信息
				getData(mszwBgdd.getId());
				/*date.setText(mszwBgdd.getPublishTime());
				content.setText(Html.fromHtml("   地址："+mszwBgdd.getAddress()+"\r\n<br/>"+
						mszwBgdd.getAddLX()+
						"<br/>联系电话："+mszwBgdd.getTel()));*/
			}		
		}else if(pageNumber==20)
		{
			//显示更多通知的列表信息
			News news=(News) bundle.getSerializable("newsData");
			if(news==null)
			{
				Toast.makeText(context, "数据不完整", 2).show();
			}else
			{
				title.setText(news.getTitle());
				date.setText(news.getTime());
				content.setText(Html.fromHtml(news.getContent()));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_policital_detail,menu);
		return true;
	}
	
	//根据办公地点ID获取办公地点详细信息
	private void getData(String bgId) {
		// TODO Auto-generated method stub
		//newsList = new ArrayList<News>();

		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"zgancontent.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
		//参数
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		svr.addParameter("method","bgddxx");
		svr.addParameter("bid",bgId);
				
		HttpAndroidTask task = new HttpAndroidTask(context, svr,
				new HttpResponseHandler() {
					// 响应事件
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						dialog.stop();
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,context,false);
						if (jsonEntity.getStatus() == 1) {
							Toast.makeText(context, "暂时没有信息", Toast.LENGTH_SHORT).show();
						} else if (jsonEntity.getStatus() == 0) {
							bgddDetailList=(List<BgddDetail>) GsonUtil.getData(
										jsonEntity,AppConstants.type_bgddDetailList);	
								
								if(bgddDetailList.size()>0)
				                {
				                	//有数据的时候操作
									date.setVisibility(View.INVISIBLE);
									content.setText(Html.fromHtml("   地址："+bgddDetailList.get(0).getAddr()+"\r\n<br/>"+
											bgddDetailList.get(0).getAddlx()+
											"<br/>联系电话："+bgddDetailList.get(0).getTel()));
				                }else
				                {
				                	//没有数据时候提示
				                	Toast.makeText(context, "暂时没有信息", Toast.LENGTH_SHORT).show();
				                }														
						}														
					}
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
						dialog = new MyProgressDialog(context);
						DialogUtil.setAttr4progressDialog(dialog);
					}
				});
		task.execute(new String[] {});	
	}

}
