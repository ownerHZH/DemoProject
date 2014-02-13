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
import com.zgan.community.tools.MainAcitivity;

import android.app.Activity;
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
	TextView app_name;

	Button app_dow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_aqwsapp);

		back = (Button) findViewById(R.id.back);
		app_dow = (Button) findViewById(R.id.app_dowland);

		top_title = (TextView) findViewById(R.id.title);
		app_name = (TextView) findViewById(R.id.app_name);

		top_title.setText("详情");
		app_name.setText("");

		app_dow.setOnClickListener(listener);
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
				startService(new Intent(getApplicationContext(), DownloadFileService.class));
				app_dow.setEnabled(false);
			//	checkPrograme("com.hiibox.houseshelter", "");
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
