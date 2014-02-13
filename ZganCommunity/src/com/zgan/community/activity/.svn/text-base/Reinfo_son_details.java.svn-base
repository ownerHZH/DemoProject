package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.tools.MainAcitivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class Reinfo_son_details extends MainAcitivity {

	Button back;
	TextView top_title;

	WebView content;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_recinfo_son_details);

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(listener);

		top_title = (TextView) findViewById(R.id.title);
		content = (WebView) findViewById(R.id.info_content);
		title = (TextView) findViewById(R.id.info_title);
		if (getIntent().getExtras() != null) {
			String cotxt = getIntent().getExtras()
					.getString("Recruitment_info");
			String tit = getIntent().getExtras().getString("company");
			
			/*content.loadDataWithBaseURL(null, "<html>" + cotxt + "</html>",
					"text/html", "utf-8", null);*/
			content.loadDataWithBaseURL(null, cotxt,
					"text/html", "utf-8", null);
			title.setText(tit);
		}
		top_title.setText("œÍœ∏–≈œ¢");
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};

}
