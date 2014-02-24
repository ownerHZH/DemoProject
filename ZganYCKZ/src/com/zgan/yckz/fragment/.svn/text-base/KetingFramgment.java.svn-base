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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class KetingFramgment extends Fragment {
	/**
	 * 二位开关
	 */
	public int stautes1 = 2;
	public int stautes2 = 2;

	ImageView deng_btn1;
	ImageView deng_btn2;
	String tag = "0";
	
	RadioGroup group;
	RadioButton radioButton_true;
	RadioButton radioButton_flase;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_bline, null);

		// User_Login.setHnadler(KetingDatahandler);
		deng_btn1 = (ImageView) view.findViewById(R.id.deng_btn);
		deng_btn2 = (ImageView) view.findViewById(R.id.deng_btn1);
	/*	radioButton_true = (RadioButton) view.findViewById(R.id.alltrue);
		radioButton_flase = (RadioButton) view.findViewById(R.id.allflase);
		group = (RadioGroup) view.findViewById(R.id.radiogroup);*/

		
		//group.setOnCheckedChangeListener(changeListener);
		deng_btn1.setOnClickListener(l);
		deng_btn2.setOnClickListener(l);
		return view;
	}
	
	public void Showview() {
		// TODO Auto-generated method stub
		if (stautes1 == 2 || stautes2 == 2) {
			if (YCKZ_Static.ZHUWO_DENGMAC1 != null
					&& YCKZ_Static.ZHUWO_DENGJOBSTAUTES1 != null) {
				stautes1 = Integer.parseInt(YCKZ_Static.ZHUWO_DENGJOBSTAUTES1
						.substring(0, 1));
				stautes2 = Integer.parseInt(YCKZ_Static.ZHUWO_DENGJOBSTAUTES1
						.substring(2, 3));
				Log.i("ketingstautes1", "" + stautes1);
				Log.i("ketingstautes1", "" + stautes1);

				if (stautes1 == 1) {
					deng_btn1.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_flase));
				} else if (stautes1 == 0) {
					deng_btn1.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_true));
				}
				if (stautes2 == 1) {
					deng_btn2.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_flase));
				} else if (stautes2 == 0) {
					deng_btn2.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_true));
				}
			} else if (stautes1 == 1) {
				Log.i("ketingstautes1", "" + stautes1);
				deng_btn1.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_flase));
			} else if (stautes1 == 0) {
				Log.i("ketingstautes1", "" + stautes1);
				deng_btn1.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_true));
			} else if (stautes2 == 1) {
				Log.i("ketingstautes2", "" + stautes2);
				deng_btn2.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_flase));
			} else if (stautes2 == 0) {
				Log.i("ketingstautes2", "" + stautes2);
				deng_btn2.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_true));
			}
		}
		// textView.setText(YCKZ_Static.ZHUWO_SHEBEINAME1);

	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.deng_btn:
				tag = "0x005c";
				if (stautes1 == 1) {
					stautes1 = 0;
				} else {
					stautes1 = 1;
				}
				Log.i("stautes1", "" + stautes1);
				SetSwtich(tag, stautes1);
				break;

			case R.id.deng_btn1:
				tag = "0x005d";
				if (stautes2 == 1) {
					stautes2 = 0;
				} else {
					stautes2 = 1;
				}
				Log.i("stautes2", "" + stautes2);
				SetSwtich(tag, stautes2);
				break;
			}
		}
	};

	protected void SetSwtich(String TAG, int stautes) {
		// TODO Auto-generated method stub
		try {
			byte[] logininfo;

			String strLoginInfo = YCKZ_Static.ZHUWO_DENGJOBSTAUTES4 + "\t"
					+ stautes + TAG ;
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
}
