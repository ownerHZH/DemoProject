package com.zgan.yckz.fragment;

import com.zgan.yckz.R;
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

public class CiwoFramgment extends Fragment {
	/**
	 * һλ����
	 */
	public  int stautes = 2;
	ImageView deng_btn;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_line, null);
		deng_btn=(ImageView)view.findViewById(R.id.deng_btn);
		deng_btn.setOnClickListener(l);
		Showview();

		return view;
	}
	OnClickListener l=new OnClickListener() {

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
	public void Show(){
		Log.i("show", "show");

		if(stautes==1){
			Log.i("ciwostautes", "1");

			deng_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.deng_flase));
		}else{
			deng_btn.setImageDrawable(getResources().getDrawable(
					R.drawable.deng_true));
		}
	}

	protected void SetSwtich() {
		// TODO Auto-generated method stub
		try {
			byte[] logininfo;
			if (stautes == 1) {
				stautes = 0;
			} else {
				stautes = 1;
			}
			Log.i("ciwostautes", "" + stautes);
			String strLoginInfo = YCKZ_Static.ZHUWO_DENGMAC2+"\t" + stautes +0x005d;
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
	public void Showview() {
		// TODO Auto-generated method stub
		if (stautes == 2) {
			if (YCKZ_Static.ZHUWO_DENGMAC2 != null
					&& YCKZ_Static.ZHUWO_DENGJOBSTAUTES2 != null) {
				stautes = Integer.parseInt(YCKZ_Static.ZHUWO_DENGJOBSTAUTES2
						.substring(1, 2));
				Log.i("ciwostautes", "" + stautes);
				if (stautes == 1) {

					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_flase));
				} else if (stautes == 0) {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_true));
				}
			} else if (stautes == 1) {
				Log.i("ciwostautes", "" + stautes);

				deng_btn.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_flase));
			} else if (stautes == 0) {
				Log.i("ciwostautes", "" + stautes);

				deng_btn.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_true));
			}
		}
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
}
