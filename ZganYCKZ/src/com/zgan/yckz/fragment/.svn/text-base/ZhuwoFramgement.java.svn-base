package com.zgan.yckz.fragment;

import com.zgan.yckz.R;
import com.zgan.yckz.activity.Index_Activity;
import com.zgan.yckz.activity.User_Login;
import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tools.YCKZ_Static;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.Build.VERSION;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ZhuwoFramgement extends Fragment {
	/**
	 * 一位开关
	 */
	public int stautes = 2;
	ImageView deng_btn;
	TextView textView;
	String Mac = null;
	String JobStatus = null;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			super.handleMessage(msg);
		}
	};

	/**
	 * 一位开关
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.frag_line, null);
		deng_btn = (ImageView) view.findViewById(R.id.deng_btn);
		textView=(TextView)view.findViewById(R.id.shebeiname);
		deng_btn.setOnClickListener(l);
		Showview();

		return view;
	}

	public void Showview() {
		// TODO Auto-generated method stub
		if (stautes == 2) {
			if (YCKZ_Static.ZHUWO_DENGMAC1 != null
					&& YCKZ_Static.ZHUWO_DENGJOBSTAUTES1 != null) {
				/*stautes = Integer.parseInt(YCKZ_Static.ZHUWO_DENGJOBSTAUTES1
						.substring(1, 2));*/
				if(YCKZ_Static.GetStatues(YCKZ_Static.ZHUWO_DENGJOBSTAUTES1))
				{
					stautes=0;
				}else{
					stautes=1;
				}
				Log.i("zhuwostautes", "" + stautes);
				if (stautes == 1) {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_flase));
				} else if (stautes == 0) {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_true));
				}
			} else if (stautes == 1) {
				Log.i("zhuwostautes", "" + stautes);
				deng_btn.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_flase));
			} else if (stautes == 0) {
				Log.i("zhuwostautes", "" + stautes);
				deng_btn.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_true));
			}
		}
		textView.setText(YCKZ_Static.ZHUWO_SHEBEINAME1);
		
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.deng_btn:

				SetSwtich();

				break;

			default:
				break;
			}
		}
	};

	protected void SetSwtich() {
		// TODO Auto-generated method stub
		try {
			byte[] logininfo;
			if (stautes == 1) {
				stautes = 0;
			} else {
				stautes = 1;
			}
			Log.i("zhuwostautes", "" + stautes);
			String strLoginInfo = YCKZ_Static.ZHUWO_DENGMAC1+"\t"+ stautes+0x005d;
			Log.i("strLoginInfo",strLoginInfo);
			logininfo = strLoginInfo.getBytes();
			User_Login.SendData(9, logininfo, strLoginInfo.length(),
					Constant.List_cbMainCmdID, (byte) 0x00,
					Constant.INDEX_cbMessageVer);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("NewSocketInfo" + "e.printStackTrace() "
					+ e.getMessage());
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub  
		super.onCreate(savedInstanceState);

	}

	@SuppressLint("NewApi")
	public void Test_Version() {
		if (Integer.parseInt(VERSION.SDK) > 14
				|| Integer.parseInt(VERSION.SDK) == 14) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork() // or
					// .detectAll()
					// for
					// all
					// detectable
					// problems
					.penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
					.penaltyLog().penaltyDeath().build());
		}
	}

	public void Show() {
		Log.i("show", "show");

		if (stautes == 1) {
			Log.i("showstautes", "1");

			deng_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.deng_flase));

		} else {
			deng_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.deng_true));
		}
	}
}
