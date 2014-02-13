package com.zgan.community.baidu;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class ZganCommunityApplictation extends Application {

	private static ZganCommunityApplictation mInstance = null;
	public boolean m_bKeyRight = true;
	BMapManager mBMapManager = null;

	public static final String strKey = "1tuuknYYDsA46s5wRDkA0Zph";

	// 1tuuknYYDsA46s5wRDkA0Zph
	// 9704f4fb82b7e1c612c02cef630f10ef
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initEngineManager(this);
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(
					ZganCommunityApplictation.getInstance()
							.getApplicationContext(), "BMapManager  初始化错误!",
					Toast.LENGTH_LONG).show();
		}
	}

	public static ZganCommunityApplictation getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(
						ZganCommunityApplictation.getInstance()
								.getApplicationContext(), "您的网络出错啦！",
						Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(
						ZganCommunityApplictation.getInstance()
								.getApplicationContext(), "输入正确的检索条件！",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			// 非零值表示key验证未通过
			if (iError != 0) {
				// 授权Key错误：
				Log.i("请在 DemoApplication.java文件输入正确的授权Key,并检查您的网络连接是否正常！error: ",
						"" + iError);
				ZganCommunityApplictation.getInstance().m_bKeyRight = false;
			} else {
				ZganCommunityApplictation.getInstance().m_bKeyRight = true;
				Log.i("key认证成功", "key认证成功");
			}
		}
	}
}