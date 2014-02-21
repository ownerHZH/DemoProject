package com.zgan.yckz.fragment;

import java.util.List;

import com.zgan.yckz.R;
import com.zgan.yckz.activity.Alarm_Activity;
import com.zgan.yckz.activity.Index_Activity;
import com.zgan.yckz.activity.User_Login;
import com.zgan.yckz.socket.Constant;
import com.zgan.yckz.tcp.Frame;
import com.zgan.yckz.tools.SheBei;
import com.zgan.yckz.tools.YCKZ_SQLHelper;
import com.zgan.yckz.tools.YCKZ_Static;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
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
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class PublicFragment extends Fragment {
	/**
	 * 1位开关,二位开关，插座
	 */
	public int stautes1 = 2;
	public int stautes2 = 2;

	public String MAC1;

	public String Port1;
	public String Port2;

	ImageView deng_btn;
	ImageView deng_btn1;
	TextView textView;
	/**
	 * flag =2为二位 flag =1为一位和插座
	 */

	public int flag = 0;
	/**
	 * tag=1为插座tag=2为一位开关
	 */
	public int tag = 0;

	List<SheBei> list_shebei;
	public int postion;

	public int getTask() {

		return task;
	}

	public void setTask(int task) {
		this.task = task;
		Showview();

	}

	public int task;

	YCKZ_SQLHelper yckz_SQLHelper;

	SQLiteDatabase db;

	public PublicFragment() {

	}

	public PublicFragment(List<SheBei> list, int i) {
		list_shebei = list;
		postion = i;
	}

	public interface getPostion {
		public void onResultBack(int s);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (db != null) {
			db.close();
		}
		if (yckz_SQLHelper != null) {
			yckz_SQLHelper.close();
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.null_line, null);
		yckz_SQLHelper = new YCKZ_SQLHelper(getActivity(), "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();

		deng_btn = (ImageView) view.findViewById(R.id.deng_btn);
		deng_btn.setOnClickListener(l);
		textView = (TextView) view.findViewById(R.id.subname);
		deng_btn1 = (ImageView) view.findViewById(R.id.deng_btn1);
		deng_btn1.setOnClickListener(l);
		try {
			Select();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Showview();

		return view;
	}

	public void Select() {
		// TODO Auto-generated method stub
		Log.i("postion", "" + postion);
		MAC1 = list_shebei.get(postion).getMAC();
		Log.i("mac", "" + MAC1);
		Cursor c = db.rawQuery("select *from table_SubDev where _MAC=?",
				new String[] { MAC1 });
		if (c.moveToFirst()) {
			Log.i("c.getCount()", "" + c.getCount());
			if (c.getCount() == 2) {
				flag = 2;
				tag=2;
				deng_btn1.setVisibility(0);
				String st1 = c.getString(c.getColumnIndex("_JobStatus"));
				Log.i(st1, "" + st1);
				Port1 = c.getString(c.getColumnIndex("_Port"));
				Log.i(Port1, "" + Port1);
				textView.setText(c.getString(c.getColumnIndex("_DeviceName")));
				stautes1 = Integer.parseInt(st1.substring(0, 1));
				c.moveToNext();
				st1 = c.getString(c.getColumnIndex("_JobStatus"));
				Port2 = c.getString(c.getColumnIndex("_Port"));
				Log.i(st1, "" + st1);
				// textView.setText(c.getString(c.getColumnIndex("_DeviceName")));
				Log.i(Port2, "" + Port2);
				stautes2 = Integer.parseInt(st1.substring(2, 3));

				Showview();
			} else {
				flag = 1;
				String st1 = c.getString(c.getColumnIndex("_JobStatus"));
				Port1 = c.getString(c.getColumnIndex("_Port"));
				if (Port1.equals("72")) {
					tag = 1;
				} else {
					tag = 2;
				}
				textView.setText(c.getString(c.getColumnIndex("_DeviceName")));
				Log.i(st1, "" + st1);
				Log.i(Port1, "" + Port1);
				Log.i("tag", "" + tag);
				stautes1 = Integer.parseInt(st1.substring(1, 2));
				Showview();
			}
		}
		c.close();

	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.deng_btn:
				SetSwtich(MAC1, Port1, stautes1);
				break;

			case R.id.deng_btn1:
				SetSwtich(MAC1, Port2, stautes2);
				break;
			}
		}
	};

	protected void SetSwtich(String mac, String port, int stautes) {
		// TODO Auto-generated method stub
		try {
			byte[] logininfo;
			if (stautes == 1) {
				stautes = 0;
			} else {
				stautes = 1;
			}
			String Job_States = null;
			if (port.equals("92")) {
				Job_States = stautes + ",0" + ",0" + ",0";
			}
			if (port.equals("93")) {
				Job_States = "0," + stautes + ",0" + ",0";
			}
			if (port.equals("72")) {
				Job_States = "0," + stautes + ",0" + ",0";
			}
			if (port.equals("71")) {
				Job_States = "0," + "0," + stautes + ",0";
			}

			Log.i("mac", mac);
			Log.i("Job_States", "" + Job_States);
			Log.i("port", "" + port);

			String strLoginInfo = mac + "\t" + Job_States + "\t" + port;
			Log.i("strLoginInfo", strLoginInfo);
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
		if (stautes1 == 2) {

			if (stautes1 == 1) {
				if (tag == 1) {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.chazuo_true));
				} else {

					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_flase));
				}
			} else if (stautes1 == 0) {
				if (tag == 1) {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.chazuo_flase));
				} else {
					deng_btn.setImageDrawable(getResources().getDrawable(
							R.drawable.deng_true));
				}
			}

		} else if (stautes1 == 1) {
			if (tag == 1) {
				deng_btn.setImageDrawable(getResources().getDrawable(
						R.drawable.chazuo_true));
			} else {

				deng_btn.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_flase));
			}
		} else if (stautes1 == 0) {
			if (tag == 1) {
				deng_btn.setImageDrawable(getResources().getDrawable(
						R.drawable.chazuo_flase));
			} else {
				deng_btn.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_true));
			}
		}

		if (stautes2 == 2) {

			if (stautes1 == 1) {
				deng_btn1.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_flase));
			} else if (stautes1 == 0) {
				deng_btn1.setImageDrawable(getResources().getDrawable(
						R.drawable.deng_true));
			}

		} else if (stautes2 == 1) {

			deng_btn1.setImageDrawable(getResources().getDrawable(
					R.drawable.deng_flase));
		} else if (stautes2 == 0) {

			deng_btn1.setImageDrawable(getResources().getDrawable(
					R.drawable.deng_true));
		}

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		Showview();

		super.onStart();
	};

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