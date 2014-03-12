package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.R.layout;
import com.zgan.community.R.menu;
import com.zgan.community.tools.MainAcitivity;

import android.net.ParseException;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

public class RecruitDetailActivity extends MainAcitivity {

	Button back;
	TextView top_title;
	WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recruit_detail);
		
		back = (Button) findViewById(R.id.back);
		top_title = (TextView) findViewById(R.id.title);
		webView=(WebView) findViewById(R.id.webViewDetail);
		back.setOnClickListener(listener);
		
		String title=getIntent().getExtras().getString("button_key");
        top_title.setText(title);
        
        webView.getSettings().setJavaScriptEnabled(true);
        try {
			webView.setWebViewClient(new WebViewClient(){       
			    public boolean shouldOverrideUrlLoading(WebView view, String url) {       
						view.loadUrl(url);       
						return true;				      
			    }       
			 });
		}catch(android.net.ParseException ee){
			ee.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
        
        showData(title);
        
	}
	
	private void showData(String button_key)
	{
		if (button_key.equals("服务员")) {
			String url="http://cq.58.com/cantfwy/?key=服务员&cmcskey=服务员&final=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("保洁")) {
			String url="http://cq.58.com/baojie/?key=%E4%BF%9D%E6%B4%81&cmcskey=%E4%BF%9D%E6%B4%81&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("司机")) {
			String url="http://cq.58.com/siji/?key=%E5%8F%B8%E6%9C%BA&cmcskey=%E5%8F%B8%E6%9C%BA&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("厨师")) {
			String url="http://cq.58.com/zplvyoujiudian/?key=%E5%8E%A8%E5%B8%88&cmcskey=%E5%8E%A8%E5%B8%88&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("快递员")) {
			String url="http://cq.58.com/zpwuliucangchu/?key=%E5%BF%AB%E9%80%92%E5%91%98&cmcskey=%E5%BF%AB%E9%80%92%E5%91%98&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("销售")) {
			String url="http://cq.58.com/yewu/?key=%E9%94%80%E5%94%AE&cmcskey=%E9%94%80%E5%94%AE&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("技工")) {
			String url="http://cq.58.com/zpshengchankaifa/?key=%E6%8A%80%E5%B7%A5&cmcskey=%E6%8A%80%E5%B7%A5&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("客服")) {
			String url="http://cq.58.com/kefu/?key=%E5%AE%A2%E6%9C%8D&cmcskey=%E5%AE%A2%E6%9C%8D&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("营业员")) {
			String url="http://cq.58.com/chaoshishangye/?key=%E8%90%A5%E4%B8%9A%E5%91%98&cmcskey=%E8%90%A5%E4%B8%9A%E5%91%98&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("会计")) {
			String url="http://cq.58.com/zpcaiwushenji/?key=%E4%BC%9A%E8%AE%A1&cmcskey=%E4%BC%9A%E8%AE%A1&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("文员")) {
			String url="http://cq.58.com/renli/?key=%E6%96%87%E5%91%98&cmcskey=%E6%96%87%E5%91%98&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		} else if (button_key.equals("其他")) {
			String url="http://cq.58.com/job/?key=%E5%85%B6%E4%BB%96&cmcskey=%E5%85%B6%E4%BB%96&final=1&jump=1&specialtype=gls&sourcetype=1";
	        webView.loadUrl(url);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_recruit_detail, menu);
		return false;
	}
	
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
	
	public   boolean   onKeyDown ( int   keyCode, KeyEvent event) {
	      if   ((keyCode == KeyEvent. KEYCODE_BACK ) &&   webView .canGoBack()) {
	           webView .goBack();
	          return   true ;
	     }
	         
	      return   super .onKeyDown(keyCode, event);
	}

}
