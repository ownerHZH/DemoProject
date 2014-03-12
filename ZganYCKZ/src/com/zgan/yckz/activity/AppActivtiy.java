package com.zgan.yckz.activity;

import java.util.List;

import com.zgan.yckz.R;
import com.zgan.yckz.dowtools.DownloadFileService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AppActivtiy extends Activity {

	TextView huishequ_dow;
	TextView aqws_dow;
	Button back;
	TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.line_app);

		huishequ_dow = (TextView) findViewById(R.id.huishequ_dowland);
		aqws_dow = (TextView) findViewById(R.id.aqws_dowland);
		back=(Button) findViewById(R.id.back);
		title=(TextView) findViewById(R.id.title);
		title.setText("应用信息");
		back.setOnClickListener(l);

		huishequ_dow.setOnClickListener(l);
		aqws_dow.setOnClickListener(l);

	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.huishequ_dowland:
				if (isAppInstalled("com.zgan.community")) {
                    Intent remoteIntent = new Intent(Intent.ACTION_MAIN);
                    remoteIntent.setComponent(new ComponentName("com.zgan.community", "com.zgan.community.Advertising"));
                    startActivity(remoteIntent);
                }else
                {
                	startService(new Intent(getApplicationContext(),
    						DownloadFileService.class));
    				huishequ_dow.setEnabled(false);
                }
				
				break;

			case R.id.aqws_dowland:
				if (isAppInstalled("com.hiibox.houseshelter")) {
                    Intent remoteIntent = new Intent(Intent.ACTION_MAIN);
                    remoteIntent.setComponent(new ComponentName("com.hiibox.houseshelter", "com.hiibox.houseshelter.activity.SplashActivity"));
                    startActivity(remoteIntent);
                }else
                {
                	startService(new Intent(getApplicationContext(),
    						DownloadFileService.class));
    				aqws_dow.setEnabled(false);
                }
				
				break;
			case R.id.back:
				finish();
				break;
			}
		}
	};
	
	private boolean isAppInstalled(String packageName) {
        PackageManager packageManager = getPackageManager();
                                  
        List<PackageInfo> mPackageInfo = packageManager.getInstalledPackages(0);
        boolean flag = false;
        if (mPackageInfo != null) {
            String tempName = null;
            for (int i = 0; i < mPackageInfo.size(); i++) {
                tempName = mPackageInfo.get(i).packageName;
                if (tempName != null && tempName.equals(packageName)) {
                    Log.i("SmartAppActivity", "Package[" + packageName + "]:is installed.");
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
