package com.zgan.yckz.activity;

import com.zgan.yckz.R;
import com.zgan.yckz.dowtools.DownloadFileService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AppActivtiy extends Activity {

	TextView huishequ_dow;
	TextView aqws_dow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.line_app);

		huishequ_dow = (TextView) findViewById(R.id.huishequ_dowland);
		aqws_dow = (TextView) findViewById(R.id.aqws_dowland);

		huishequ_dow.setOnClickListener(l);
		aqws_dow.setOnClickListener(l);

	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.huishequ_dowland:
				startService(new Intent(getApplicationContext(),
						DownloadFileService.class));
				huishequ_dow.setEnabled(false);
				break;

			case R.id.aqws_dowland:
				startService(new Intent(getApplicationContext(),
						DownloadFileService.class));
				aqws_dow.setEnabled(false);
				break;
			}
		}
	};
}
