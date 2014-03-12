package com.zgan.community.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.zgan.community.R;
import com.zgan.community.downtools.DownloadFileService;
import com.zgan.community.jsontool.AppConstants;
import com.zgan.community.tools.MainAcitivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AQWSAppActivity extends MainAcitivity {
	PackageInfo info;

	Button back;

	ImageView app_logo;

	TextView top_title;
	//TextView app_name;

	Button app_dow,yckz_dow;
	
	public  Context con=AQWSAppActivity.this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_aqwsapp);

		back = (Button) findViewById(R.id.back);
		app_dow = (Button) findViewById(R.id.app_dowland);
		yckz_dow=(Button) findViewById(R.id.yckz_down);

		top_title = (TextView) findViewById(R.id.title);

		top_title.setText("详情");

		app_dow.setOnClickListener(listener);
		yckz_dow.setOnClickListener(listener);
		back.setOnClickListener(listener);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			case R.id.app_dowland:
				//startService(new Intent(getApplicationContext(), DownloadFileService.class));
				//app_dow.setEnabled(false);
				if (isAppInstalled("com.hiibox.houseshelter")) {
                    Intent remoteIntent = new Intent(Intent.ACTION_MAIN);
                    remoteIntent.setComponent(new ComponentName("com.hiibox.houseshelter", "com.hiibox.houseshelter.activity.SplashActivity"));
                    startActivity(remoteIntent);
                }else
                {
                	Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse(AppConstants.AppDownLoadAddress1));//AppConstants.AppDownLoadAddress+"APK/ZganCommunity.apk"
                    startActivity(intent1);
                }
			//	checkPrograme("com.hiibox.houseshelter", "");
				break;
			case R.id.yckz_down:
				//startService(new Intent(getApplicationContext(), DownloadFileService.class));
				//yckz_dow.setEnabled(false);
				if (isAppInstalled("com.zgan.yckz")) {
                    Intent remoteIntent = new Intent(Intent.ACTION_MAIN);
                    remoteIntent.setComponent(new ComponentName("com.zgan.yckz", "com.zgan.yckz.Welcome"));
                    startActivity(remoteIntent);
                }else
                {
                	Intent intent1 = new Intent(Intent.ACTION_VIEW);
                    intent1.setData(Uri.parse(AppConstants.AppDownLoadAddress2));//AppConstants.AppDownLoadAddress+"APK/ZganCommunity.apk"
                    startActivity(intent1);
                }
				
				break;
			}
		}

	};

	public boolean checkPrograme(String packName, String actvityName) {
		boolean flag = false;
		PackageManager manager = getPackageManager();
		// 只查找启动方式为LAUNCHER并且是ACTION_MAIN的APP
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 根据Intent值查询这样的app
		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);

		for (ResolveInfo app : apps) {
			// 该应用的包名和主Activity
			String pkg = app.activityInfo.packageName;
			String cls = app.activityInfo.name;
			if (pkg.equals(packName) && cls.equals(actvityName)) {
				flag = true;
				try {
					info = manager.getPackageInfo(pkg, 0);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				String mVersonName = info.versionName;
				int versionCode = info.versionCode;
				Log.v("TAG", "==========mVersonName  :" + mVersonName);
				Intent intent = new Intent();
				intent.setClassName(packName, actvityName);
				startActivity(intent);
				break;
			} else {
				startService(new Intent(getApplicationContext(),
						DownloadFileService.class));
				app_dow.setEnabled(false);
			}
			Log.v("TAG", "pkg  :" + pkg);
			Log.v("TAG", "cls  :" + cls);
		}
		return flag;
	}
	
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

	protected void installApk(String filename) {
		// TODO Auto-generated method stub
		File file = new File(filename);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW); // 浏览网页的Action(动作)
		String type = "application/vnd.android.package-archive";
		intent.setDataAndType(Uri.fromFile(file), type); // 设置数据类型
		startActivity(intent);
	}
}
