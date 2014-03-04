package com.zgan.community.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zgan.community.R;
import com.zgan.community.adapter.DefaultListAdapter;
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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class CommunityNotificationActivity extends MainAcitivity {

	private Button back;
	private TextView title;
	
	private WebView newsview;
	private TextView lordland;
	private TextView vote;
	private TextView apply;
	
	private List<News> newsList;
	private Context con;
	private MyProgressDialog pdialog;
	private PopupWindow mPopupWindow;
	private PopupWindow defaultListPopupWindow;
	
	private DefaultListAdapter defaultListAdapter;
	
	private Handler handler;
	private Handler listHandler;
	
	private Button addDefaultBtn,more;
	private EditText editText;
	//private View popupWindowView;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_notification);
		
		//pdialog=new MyProgressDialog(this);
		
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText(R.string.community_notification);
		newsview=(WebView) findViewById(R.id.webViewNotification);
		
		lordland=(TextView) findViewById(R.id.textViewLordland);
		vote=(TextView) findViewById(R.id.textViewVote);
		apply=(TextView) findViewById(R.id.textViewApply);
		more=(Button) findViewById(R.id.moreBtn);
		lordland.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		vote.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		apply.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		
		con=CommunityNotificationActivity.this;
		
		newsview.getSettings().setDefaultTextEncodingName("utf-8");
		//newsview.loadUrl("file:///android_asset/notification.html");
		
		ClickListener l=new ClickListener();
		back.setOnClickListener(l);
		lordland.setOnClickListener(l);
		vote.setOnClickListener(l);
		apply.setOnClickListener(l);
		more.setOnClickListener(l);
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0x111)
				{
					show();//显示弹出框
				}
			}
			
		};
		
		listHandler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				if(msg.what==0x121)
				{
					showDefaultList();//显示弹出框列表
				}
				if(msg.what==0x123)
				{
					defaultListPopupWindow.dismiss();
				}
			}
			
		};
		
		showData(newsview);//显示通知
		
	}
    
	private void showData(final WebView webView) {
		newsList = new ArrayList<News>();

		HttpClientService svr = new HttpClientService(
				AppConstants.HttpHostAdress+"ZganNews.aspx");//"http://community1.zgantech.com/ZganNews.aspx?did=15923258890"
		//参数
		svr.addParameter("did",ZganCommunityStaticData.User_Number);
		//svr.addParameter("sid", StringTypeToInt.convertTypeToInt(button_key));
		
		HttpAndroidTask task = new HttpAndroidTask(con, svr,
				new HttpResponseHandler() {
					// 响应事件
					@SuppressWarnings("unchecked")
					public void onResponse(Object obj) {
						pdialog.stop();
						JsonEntity jsonEntity = GsonUtil.parseObj2JsonEntity(
								obj,con,false);
						if (jsonEntity.getStatus() == 1) {
							Toast.makeText(con, "没有最新的通知", 2).show();
						} else if (jsonEntity.getStatus() == 0) {
							newsList=(List<News>) GsonUtil.getData(
									jsonEntity,AppConstants.type_newsList);	
							
							if(newsList.size()>0)
			                {
			                	//有数据的时候操作
			                	/*communityServiceAdapter = new CommunityServiceAdapter(
			    						CommunityServiceListActivity.this, serviceInfoList);
			    				listview.setAdapter(communityServiceAdapter);*/
								//webView.loadDataWithBaseURL(null,newsList.get(0).getNContent(), "text/html", "utf-8",null);
			                }else
			                {
			                	//没有数据时候提示
			                   Toast.makeText(con, "没有最新的通知", 2).show();
			                	//finish();//返回上层界面
			                }
						}														
					}
				}, new HttpPreExecuteHandler() {
					public void onPreExecute(Context context) {
						pdialog = new MyProgressDialog(context);
						DialogUtil.setAttr4progressDialog(pdialog);
					}
				});
		task.execute(new String[] {});	
	}

	public class ClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;
            case R.id.textViewLordland:
            	handler.sendEmptyMessage(0x111);//显示一个弹出框
				break;
            case R.id.textViewVote:
            	Toast.makeText(getApplicationContext(), "投票功能未实现", Toast.LENGTH_LONG).show();
	            break;
            case R.id.textViewApply:
            	Toast.makeText(getApplicationContext(), "报名功能未实现", Toast.LENGTH_LONG).show();
	            break;
            case R.id.moreBtn:
            	Intent intent=new Intent(con,CommunityMoreNewsActivity.class);
            	Bundle bundle=new Bundle();
				bundle.putSerializable("news", (Serializable) newsList);
				intent.putExtras(bundle);
				con.startActivity(intent);
	            break;

			default:
				break;
			}
		}
		
	}
	
	/**
	 * 弹出框的设置
	 */
	protected void show() {
		View view=getLayoutInflater().inflate(R.layout.popupwindow_layer,null);
		if(mPopupWindow==null)
		{
			mPopupWindow=new PopupWindow(view,
					LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		}
		mPopupWindow.setFocusable(true);//设置获取焦点		
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//为了让在外点击消失弹出框
		mPopupWindow.setOutsideTouchable(true);// 设置允许在外点击消失 
		mPopupWindow.setAnimationStyle(R.style.AnimationPreview);//设置弹窗进入与退出的动画效果
		mPopupWindow.showAtLocation(findViewById(R.id.lin), Gravity.CENTER, 0, 0);
		
		//提交按钮
		Button submit_btn=(Button) view.findViewById(R.id.submit_button);
		//添加默认文字按钮
		addDefaultBtn=(Button) view.findViewById(R.id.add_button);
		editText=(EditText) view.findViewById(R.id.editTextPopupWindow);
		submit_btn.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
			}
		});
		addDefaultBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				listHandler.sendEmptyMessage(0x121);
			}
		});
	}
	
	/**
	 * 弹出框的弹出list列表
	 */
	public void showDefaultList() {
		View view=LayoutInflater.from(con).inflate(R.layout.default_list,null);
		if(defaultListPopupWindow==null)
		{
			defaultListPopupWindow=new PopupWindow(view,
					LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		}
		defaultListPopupWindow.setFocusable(true);//设置获取焦点		
		defaultListPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		defaultListPopupWindow.setOutsideTouchable(true);// 设置允许在外点击消失 
		//mPopupWindow.setAnimationStyle(R.style.AnimationPreview);//设置弹窗进入与退出的动画效果
		ListView default_list=(ListView) view.findViewById(R.id.default_list);
		default_list.setDivider(null);
		
		List<String> dataList=new ArrayList<String>();
		dataList.add("报事维修");
		dataList.add("我需要物业服务，请立即联系我");
		dataList.add("我需要室内电气维修");
		dataList.add("我需要室内管道维修");
		
		defaultListAdapter=new DefaultListAdapter(con, dataList,editText,listHandler);
		default_list.setAdapter(defaultListAdapter);
		
		/*default_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				editText.setText(((TextView)view.findViewById(R.id.textViewDefault)).getText());
				defaultListPopupWindow.dismiss();
			}
		});*/
		
		if(defaultListPopupWindow.isShowing())
		{
			defaultListPopupWindow.dismiss();
		}else
		{
			defaultListPopupWindow.showAtLocation(findViewById(R.id.lin), Gravity.CENTER, 0, 0);//showAsDropDown(addDefaultBtn);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_community_notification, menu);
		return true;
	}

}
